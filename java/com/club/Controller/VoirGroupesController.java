package com.club.Controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.File;
import javafx.application.Application;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.club.Model.Gestion;
import com.club.Model.Groupe;

public class VoirGroupesController implements Initializable {

    @FXML
    private GridPane grid;
    private List<CarteGroupe> cartes;
    private MyListenerGroupe myListenerGroupe;
    private List<CarteGroupeController> cardControllers = new ArrayList<>();

    private CarteGroupe carteSelectionnee;

    @FXML
    private TextField searchIdGroupe;
    public void setCarteSelectionnee(CarteGroupe carteSelectionnee) {
        this.carteSelectionnee = carteSelectionnee;
    }
    public CarteGroupe getCarteSelectionnee() {
        return this.carteSelectionnee;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
        cartes=recupererGroupesFromDatabase();

        // setting the first chosen card

        if (cartes.size() > 0) {
            myListenerGroupe=new MyListenerGroupe() {

                
                @Override
                public void onClickListener(CarteGroupe carte) {
                    setCarteSelectionnee(carte);
                    // Highlight the selected card and unhighlight others
                    for (CarteGroupeController controller : cardControllers) {
                        controller.highlightBorder(controller.getCarte().equals(carte));
                    }
                }


                
            };
        }

        int columns = 1;
        int row = 1;

        try {

            for (int i = 0; i < cartes.size(); i++) {

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/groupeN.fxml")); // Assurez-vous que le chemin est correct
                AnchorPane box = fxmlLoader.load();
                CarteGroupeController carteGroupeController = fxmlLoader.getController();
                carteGroupeController.setData(cartes.get(i),myListenerGroupe);
                cardControllers.add(carteGroupeController);

                if (columns == 3) {
                    columns = 1;
                    ++row;
                }

                grid.add(box, columns++, row);
                GridPane.setMargin(box, new Insets(10));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Déplacer ces lignes à l'extérieur de la boucle for
        //set grid width
        grid.setMinWidth(Region.USE_COMPUTED_SIZE);
        grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
        grid.setMaxWidth(Region.USE_PREF_SIZE);

        //set grid height
        grid.setMinHeight(Region.USE_COMPUTED_SIZE);
        grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
        grid.setMaxHeight(Region.USE_PREF_SIZE);

        // Ajouter un écouteur de changement de texte au champ de recherche
        searchIdGroupe.textProperty().addListener((observable, oldValue, newValue) -> {
            // Vérifier si le nouveau texte est vide
            if (newValue.isEmpty()) {
                // Recharger la page lorsque la zone de recherche est vide
                reloadPage();
            } else {
                // Filtrer et afficher les salles en fonction du texte de recherche
                rechercherCartes();
            }
        });

    }


    private List<CarteGroupe> recupererGroupesFromDatabase() {
        List<CarteGroupe> groupes = new ArrayList<>();

        try (Connection connection = new Gestion().connectionBd();){
            List<Groupe> GroupesFromDatabase = Gestion.getListeGroupes(connection);

            for (Groupe groupe : GroupesFromDatabase) {
                CarteGroupe carte = new CarteGroupe();

                carte.setgN(groupe.getNomGroupe());
                carte.setGroupeID(groupe.getGroupeId());
                groupes.add(carte);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupes;
    }





    // Méthode pour filtrer les cartes selon le texte de recherche
    @FXML
    private void rechercherCartes() {
        String texteRecherche = searchIdGroupe.getText();
        filtrerCartes(texteRecherche);
        afficherCartes();
    }
    // Méthode pour filtrer les cartes en fonction du texte de recherche
    private void filtrerCartes(String texteRecherche) {
        // Réinitialiser la liste des cartes
        cartes.clear();
        // Récupérer à nouveau les adhérents de la base de données
        List<CarteGroupe> groupes = recupererGroupesFromDatabase();
        System.out.println(texteRecherche);

        // Filtrer les adhérents dont le nom commence par le texte de recherche
        for (CarteGroupe groupe : groupes) {

            if (groupe.getgN().toLowerCase().contains(texteRecherche.toLowerCase())) {
                CarteGroupe carte = new CarteGroupe();
                carte.setgN(groupe.getgN());
                carte.setGroupeID(groupe.getGroupeID());
                cartes.add(carte);
            }
        }
    }

    // Méthode pour afficher les cartes filtrées
    private void afficherCartes() {
        // Effacer la grille
        grid.getChildren().clear();

        // Réinitialiser les colonnes et les lignes
        int columns = 1;
        int row = 1;

        try {
            // Ajouter chaque carte filtrée à la grille
            for (CarteGroupe carte : cartes) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/groupeN.fxml"));
                AnchorPane box = fxmlLoader.load();
                CarteGroupeController carteGroupeController = fxmlLoader.getController();
                carteGroupeController.setData(carte, myListenerGroupe);

                if (columns == 3) {
                    columns = 1;
                    ++row;
                }

                grid.add(box, columns++, row);
                GridPane.setMargin(box, new Insets(10));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Déplacer ces lignes à l'extérieur de la boucle for
        //set grid width
        grid.setMinWidth(Region.USE_COMPUTED_SIZE);
        grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
        grid.setMaxWidth(Region.USE_PREF_SIZE);

        //set grid height
        grid.setMinHeight(Region.USE_COMPUTED_SIZE);
        grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
        grid.setMaxHeight(Region.USE_PREF_SIZE);

    }


    // Vos autres membres et méthodes existants

    // Méthode pour supprimer la carte sélectionnée
    @FXML
    private void Supprimercard(ActionEvent event) {
        if (carteSelectionnee != null) {
            // Créer une boîte de dialogue de confirmation
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Voulez-vous vraiment supprimer cet Groupe ?");
            confirmation.setContentText("Nom du Groupe : " + carteSelectionnee.getgN());

            // Charger l'icône
        File imageFile = new File("./src/main/resources/com/club/Controller/pics/Confirmation.png");
        
        Image image = new Image(imageFile.toURI().toString());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(48);
        imageView.setFitHeight(48);
        confirmation.setGraphic(imageView);

            // Appliquer du style CSS
            DialogPane dialogPane = confirmation.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
            dialogPane.getStyleClass().add("my-alert");
            // Afficher la boîte de dialogue de confirmation et attendre la réponse de l'utilisateur
            Optional<ButtonType> result = confirmation.showAndWait();

            // Si l'utilisateur a cliqué sur le bouton OK, procéder à la suppression de l'adhérent
            if (result.isPresent() && result.get() == ButtonType.OK) {
                int groupeID = carteSelectionnee.getGroupeID();
                try {
                    Connection connection = new Gestion().connectionBd(); // Créez une instance de Gestion
                    Gestion gestion = new Gestion(); // Créez une instance de Gestion
                    gestion.supprimerGroupe(connection, groupeID); // Appelez la méthode supprimerUtilisateur sur cette instance
                    cartes.remove(carteSelectionnee);
                    showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Groupe supprimé avec succès!");

                    // Appeler switchToS() pour basculer vers la page après la suppression d'une carte
                    switchToVoirGroupes(event);

                } catch (Exception e) {
                    e.printStackTrace(); // Gérer d'autres exceptions génériques ici
                }
            }
        }
    }






    private void showAlert(Alert.AlertType type, String title, String message) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        
        File imageFile = null;
        
        ImageView imageView = new ImageView();
        // Charger l'icône en dehors des conditions
        if (type == Alert.AlertType.ERROR) {
             imageFile = new File("./src/main/resources/com/club/Controller/pics/Error.png");
        } else if (type == Alert.AlertType.WARNING) {
             imageFile = new File("./src/main/resources/com/club/Controller/pics/Warning (2).png");
        } else if (type == Alert.AlertType.CONFIRMATION) {
             imageFile = new File("./src/main/resources/com/club/Controller/pics/Confirmation.png");
        } else if (type == Alert.AlertType.INFORMATION) {
             imageFile = new File("./src/main/resources/com/club/Controller/pics/info (1).png");
        }

        if (imageFile != null) {
            Image image = new Image(imageFile.toURI().toString());
            imageView.setImage(image);
            imageView.setFitWidth(48); // Définissez la largeur de l'icône
            imageView.setFitHeight(48); // Définissez la hauteur de l'icône
            alert.setGraphic(imageView);
        }

        // Appliquer du style CSS
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
        dialogPane.getStyleClass().add("my-alert");

        alert.showAndWait();
    }


    private void reloadPage() {
        try {
            // Recharger la page en rechargeant le fichier FXML et en réinitialisant le contrôleur
            FXMLLoader loader = new FXMLLoader(getClass().getResource("voirGroupes.fxml"));
            Parent root = loader.load();
            VoirGroupesController controller = loader.getController();
            // Remplacer la scène actuelle par la nouvelle scène rechargée
            Scene scene = new Scene(root);
            Stage stage = (Stage) searchIdGroupe.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    

/************************************************************************************************** 
 * 
 */
    private Stage stage;
    private Scene scene;
    private Parent root;


    public void switchToScene7b(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("affectation.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToDetailGroupe(ActionEvent event) throws IOException, SQLException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("detailGroupe.fxml"));
        Parent root = loader.load();
        // Obtenir le contrôleur du détail de l'adhérent
        DetailsGroupeController detailController = loader.getController();
    
        // Passer les données de la carte sélectionnée au contrôleur du détail de l'adhérent
        detailController.initData(carteSelectionnee.getGroupeID());
    
        // Remplacer la scène actuelle par la scène des détails de l'adhérent
        Scene scene = new Scene(root, 1024, 760);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void switchToAjouterGroupe(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("ajouterGroupe.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    public void switchToVoirGroupes(Event event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("voirGroupes.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
