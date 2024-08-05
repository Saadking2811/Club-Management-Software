package com.club.Controller;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.club.Model.Gestion;
import com.club.Model.Sport;

public class VoirSportsController implements Initializable, MyListenerSport {

    
    @FXML
    private GridPane grid;
    private List<CarteSport> cartes;

    private MyListenerSport myListenerSport;

    private List<CarteSportController> cardControllers = new ArrayList<>();

    private CarteSport carteSelectionnee;

    @FXML
    private TextField searchIdSport;
    public void setCarteSelectionnee(CarteSport carteSelectionnee) {
        this.carteSelectionnee = carteSelectionnee;
    }
    public CarteSport getCarteSelectionnee() {
        return this.carteSelectionnee;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
        cartes=recupererSportsFromDatabase();

        // setting the first chosen card

        if (cartes.size() > 0) {
            
            setChosenCard(cartes.get(0)); // Display details of the first card initially
            myListenerSport=new MyListenerSport() {
                
                @Override
                public void onClickListener(CarteSport carte) {

                    setCarteSelectionnee(carte);
                    setChosenCard(carte);
                    // Highlight the selected card and unhighlight others
                    for (CarteSportController controller : cardControllers) {
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
                fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/sportCard.fxml")); // Assurez-vous que le chemin est correct
                AnchorPane box = fxmlLoader.load();
                CarteSportController carteSportController = fxmlLoader.getController();
                carteSportController.setData(cartes.get(i), myListenerSport);
                cardControllers.add(carteSportController);

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
        searchIdSport.textProperty().addListener((observable, oldValue, newValue) -> {
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

    private List<CarteSport> recupererSportsFromDatabase() {
        List<CarteSport> sports = new ArrayList<>();

        try (Connection connection = new Gestion().connectionBd()){

            List<Sport> sportsFromDatabase = Gestion.getListeSports(connection);

            for (Sport sport : sportsFromDatabase) {
                CarteSport carte = new CarteSport();
                carte.setPicSport("./src/main/resources/com/club/Controller/pics/karate.png");
                carte.setNameSport(sport.getSportNom());
                carte.setCateg(sport.getSportCategorie());
                carte.setSportID(sport.getSportID());
                sports.add(carte);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sports;
    }

    @FXML
    private ImageView photo;

    @FXML
    private Text sport;

    @FXML
    private Text sportcat;



    public void setChosenCard (CarteSport carte){

        // Désélectionnez toutes les cartes
        for (CarteSport c : cartes) {
            c.setSelected(false);
        }

        // Sélectionnez la carte spécifiée
        carte.setSelected(true);


        sport.setText(carte.getNameSport());
        sportcat.setText(carte.getCateg());

        // Transition de fondu
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), photo);
        fadeTransition.setFromValue(0); // Opacité initiale
        fadeTransition.setToValue(1); // Opacité finale
        fadeTransition.play();
        InputStream picStream = getClass().getResourceAsStream(carte.getPicSport());
        if (picStream != null) {
            Image nImage = new Image(picStream);
            photo.setImage(nImage);
        }


    }



    
    // Méthode pour filtrer les cartes selon le texte de recherche
    @FXML
    private void rechercherCartes() {
        String texteRecherche = searchIdSport.getText();
        filtrerCartes(texteRecherche);
        afficherCartes();
    }
    // Méthode pour filtrer les cartes en fonction du texte de recherche
    private void filtrerCartes(String texteRecherche) {
        // Réinitialiser la liste des cartes
        cartes.clear();
        // Récupérer à nouveau les adhérents de la base de données
        List<CarteSport> sports = recupererSportsFromDatabase();

        // Filtrer les adhérents dont le nom commence par le texte de recherche
        for (CarteSport sport : sports) {
            if (sport.getNameSport().toLowerCase().contains(texteRecherche.toLowerCase())) {
                CarteSport carte = new CarteSport();
                carte.setPicSport("./src/main/resources/com/club/Controller/pics/karate.png");
                carte.setNameSport(sport.getNameSport());
                carte.setCateg(sport.getCateg());
                carte.setSportID(sport.getSportID());
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
            for (CarteSport carte : cartes) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/sportCard.fxml"));
                AnchorPane box = fxmlLoader.load();
                CarteSportController carteSportController = fxmlLoader.getController();
                carteSportController.setData(carte, myListenerSport);

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
            confirmation.setHeaderText("Voulez-vous vraiment supprimer cet sport ?");
            confirmation.setContentText("Nom du sport : " + carteSelectionnee.getNameSport() + "\nCategorie : " + carteSelectionnee.getCateg() );

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
                int sportID = carteSelectionnee.getSportID();
                try {
                    Connection connection = new Gestion().connectionBd(); // Créez une instance de Gestion
                    Gestion gestion = new Gestion(); // Créez une instance de Gestion
                    gestion.supprimerSport(connection, sportID); // Appelez la méthode supprimerUtilisateur sur cette instance
                    cartes.remove(carteSelectionnee);
                    if (!cartes.isEmpty()) {
                        setChosenCard(cartes.get(0));
                    } else {
                        clearCardDisplay();
                    }
                    showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Sport supprimé avec succès!");

                    // Appeler switchToS() pour basculer vers la page après la suppression d'une carte
                    switchToVoirSports(event);

                } catch (Exception e) {
                    e.printStackTrace(); // Gérer d'autres exceptions génériques ici
                }
            }
        }
    }




    // Méthode pour effacer l'affichage si aucune carte n'est sélectionnée
    private void clearCardDisplay() {
        sport.setText("");
        sportcat.setText("");
        
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("voirSports.fxml"));
            Parent root = loader.load();
            VoirSportsController controller = loader.getController();
            // Remplacer la scène actuelle par la nouvelle scène rechargée
            Scene scene = new Scene(root);
            Stage stage = (Stage) searchIdSport.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /************************************************************************************************ */
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToSportDetail(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sportDetail.fxml"));
        Parent root = loader.load();

        // Obtenir le contrôleur du détail de l'adhérent
        ConnexionController detailController = loader.getController();

        // Passer les données de la carte sélectionnée au contrôleur du détail de l'adhérent
        detailController.initialiserSport(carteSelectionnee.getSportID());

        // Remplacer la scène actuelle par la scène des détails de l'adhérent
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1024, 760));
        stage.setResizable(false);
        stage.show();
    }

    public void switchToScene3(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("gestionSport.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    public void switchToVoirSports(Event event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("voirSports.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void onClickListener(CarteSport carte) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onClickListener'");
    }
}
