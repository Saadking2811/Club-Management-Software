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
import javafx.scene.text.Text;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.sql.SQLException;

import com.club.Model.Admin;
import com.club.Model.Gestion;
import com.club.Model.Offre;
import com.club.Model.Salle;
import com.club.Model.Sport;

public class VoirOffresController implements Initializable {

    




    @FXML
    private GridPane grid;
    private List<CarteOffre> cartes;

    private MyListenerOffre myListenerOffre;

    private List<CarteOffreController> cardControllers = new ArrayList<>();

    private CarteOffre carteSelectionnee;



    @FXML
    private TextField searchIDoffre;
    public void setCarteSelectionnee(CarteOffre carteSelectionnee) {
        this.carteSelectionnee = carteSelectionnee;
    }
    public CarteOffre getCarteSelectionnee() {
        return this.carteSelectionnee;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
        cartes=recupererOffresFromDatabase();

        // setting the first chosen card

        if (cartes.size() > 0) {
            setChosenCard(cartes.get(0)); // Display details of the first card initially
            myListenerOffre=new MyListenerOffre() {

                @Override
                public void onClickListener(CarteOffre carte) {
                    setCarteSelectionnee(carte);
                    setChosenCard(carte);
                    // Highlight the selected card and unhighlight others
                    for (CarteOffreController controller : cardControllers) {
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
                fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/offreCard.fxml")); // Assurez-vous que le chemin est correct
                AnchorPane box = fxmlLoader.load();
                CarteOffreController carteOffreController = fxmlLoader.getController();
                carteOffreController.setData(cartes.get(i),myListenerOffre);
                cardControllers.add(carteOffreController);

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
        searchIDoffre.textProperty().addListener((observable, oldValue, newValue) -> {
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


    private List<CarteOffre> recupererOffresFromDatabase() {
        List<CarteOffre> offres = new ArrayList<>();

        try (Connection connection = new Gestion().connectionBd();){
            
            List<Offre> OffresFromDatabase = Gestion.getListeOffres(connection);

            for (Offre offre : OffresFromDatabase) {
                CarteOffre carte = new CarteOffre();
                carte.setPicOffre("./src/main/resources/com/club/Controller/pics/prob.png");
                carte.setOffreActPrem(offre.getOffreNom());
                carte.setType(offre.getCategorie());
                carte.setOffrePrixPrem(String.valueOf( offre.getPrix()) + "DA");
                carte.setOffreID(offre.getOffreID());
                carte.setDuree(offre.getDuree());
                carte.setSport(Sport.getSportByID(connection, offre.getSportID()).getSportNom());
                offres.add(carte);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return offres;
    }
    @FXML
    private Text TypeOffreC;

    @FXML
    private Text actOffreC;

    @FXML
    private ImageView photo;

    @FXML
    private Text prixOffreC;

    @FXML
    private Text dureeoffre;

    @FXML
    private Text Sportoffre;



    public void setChosenCard (CarteOffre carte){

        // Désélectionnez toutes les cartes
        for (CarteOffre c : cartes) {
            c.setSelected(false);
        }

        // Sélectionnez la carte spécifiée
        carte.setSelected(true);

        TypeOffreC.setText(carte.getType());
        actOffreC.setText(carte.getOffreActPrem());
        prixOffreC.setText(carte.getOffrePrixPrem());
        dureeoffre.setText(String.valueOf(carte.getDuree())+" Semaines");
        Sportoffre.setText(carte.getSport());

        InputStream picStream = getClass().getResourceAsStream(carte.getPicOffre());
        if (picStream != null) {
            Image nImage = new Image(picStream);
            photo.setImage(nImage);
        }



    }


// Méthode pour filtrer les cartes selon le texte de recherche
    @FXML
    private void rechercherCartes() {
        String texteRecherche = searchIDoffre.getText();
        filtrerCartes(texteRecherche);
        afficherCartes();
    }
    // Méthode pour filtrer les cartes en fonction du texte de recherche
    private void filtrerCartes(String texteRecherche) {
        // Réinitialiser la liste des cartes
        cartes.clear();
        // Récupérer à nouveau les adhérents de la base de données
        List<CarteOffre> offres = recupererOffresFromDatabase();

        // Filtrer les adhérents dont le nom commence par le texte de recherche
        for (CarteOffre offre : offres) {
            if (offre.getOffreActPrem().toLowerCase().contains(texteRecherche.toLowerCase())) {
                CarteOffre carte = new CarteOffre();
                carte.setPicOffre("./src/main/resources/com/club/Controller/pics/prob.png");;
                carte.setOffreActPrem(offre.getOffreActPrem());
                carte.setType(offre.getType());
                carte.setOffrePrixPrem(offre.getOffrePrixPrem());
                carte.setOffreID(offre.getOffreID());
                carte.setDuree(offre.getDuree());
                carte.setSport(offre.getSport());
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
            for (CarteOffre carte : cartes) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/offreCard.fxml"));
                AnchorPane box = fxmlLoader.load();
                CarteOffreController carteOffreController = fxmlLoader.getController();
                carteOffreController.setData(carte, myListenerOffre);

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
            confirmation.setHeaderText("Voulez-vous vraiment supprimer cet Offre ?");
            confirmation.setContentText("Nom : " + carteSelectionnee.getOffreActPrem() + "\nSport : " + carteSelectionnee.getSport() + "\nCategorie : " + carteSelectionnee.getType() + "\nPrix : " + carteSelectionnee.getOffrePrixPrem() + "\nDuree :" + carteSelectionnee.getDuree());

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
                int offreID = carteSelectionnee.getOffreID();
                try {
                    Connection connection = new Gestion().connectionBd(); // Créez une instance de Gestion
                    Gestion gestion = new Gestion(); // Créez une instance de Gestion
                    gestion.supprimerOffre(connection, offreID); // Appelez la méthode supprimerUtilisateur sur cette instance
                    cartes.remove(carteSelectionnee);
                    if (!cartes.isEmpty()) {
                        setChosenCard(cartes.get(0));
                    } else {
                        clearCardDisplay();
                    }
                    showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Offre supprimé avec succès!");

                    // Appeler switchToS() pour basculer vers la page après la suppression d'une carte
                    switchToVoirOffre(event);

                } catch (Exception e) {
                    e.printStackTrace(); // Gérer d'autres exceptions génériques ici
                }
            }
        }
    }




    // Méthode pour effacer l'affichage si aucune carte n'est sélectionnée
    private void clearCardDisplay() {
        TypeOffreC.setText("");
        actOffreC.setText("");
        prixOffreC.setText("");
        dureeoffre.setText("");
        Sportoffre.setText("");
        // Effacer l'image de la photo
        photo.setImage(null);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("voirOffres.fxml"));
            Parent root = loader.load();
            VoirOffresController controller = loader.getController();
            // Remplacer la scène actuelle par la nouvelle scène rechargée
            Scene scene = new Scene(root);
            Stage stage = (Stage) searchIDoffre.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/************************************************************************************************************* */

private Stage stage;
private Scene scene;
private Parent root;

public void switchToScene6b(MouseEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getResource("offres.fxml"));
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root, 1024, 760);
    HomeController.setTheme(scene);
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();
}
    public void switchToVoirOffre(Event event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("voirOffres.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
