package com.club.Controller;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.File;

import javafx.animation.FadeTransition;
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
import javafx.util.Duration;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import com.club.Model.Admin;
import com.club.Model.Gestion;

public class VoirAdminsController implements Initializable {

    

    @FXML
    private GridPane gridAdmin;
    private List<CarteAdmin> cartes;

    private MyListenerAdmin myListenerAdmin;

    private List<CarteAdminController> cardControllers = new ArrayList<>();

    private CarteAdmin carteSelectionnee;



    @FXML
    private TextField searchIdAdmin;
    public void setCarteSelectionnee(CarteAdmin carteSelectionnee) {
        this.carteSelectionnee = carteSelectionnee;
    }
    public CarteAdmin getCarteSelectionnee() {
        return this.carteSelectionnee;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        
        cartes=recupererAdminsFromDatabase();

        // setting the first chosen card

        if (cartes.size() > 0) {
            setChosenCard(cartes.get(0)); // Display details of the first card initially
            myListenerAdmin=new MyListenerAdmin() {

                @Override
                public void onClickListener(CarteAdmin carte) {
                    setCarteSelectionnee(carte);
                    setChosenCard(carte);
                    // Highlight the selected card and unhighlight others
                    for (CarteAdminController controller : cardControllers) {
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
                fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/adminCard.fxml")); // Assurez-vous que le chemin est correct
                AnchorPane box = fxmlLoader.load();
                CarteAdminController carteAdminController = fxmlLoader.getController();
                carteAdminController.setData(cartes.get(i),myListenerAdmin);
                cardControllers.add(carteAdminController);

                if (columns == 3) {
                    columns = 1;
                    ++row;
                }

                gridAdmin.add(box, columns++, row);
                GridPane.setMargin(box, new Insets(10));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Déplacer ces lignes à l'extérieur de la boucle for
        //set grid width
        gridAdmin.setMinWidth(Region.USE_COMPUTED_SIZE);
        gridAdmin.setPrefWidth(Region.USE_COMPUTED_SIZE);
        gridAdmin.setMaxWidth(Region.USE_PREF_SIZE);

        //set grid height
        gridAdmin.setMinHeight(Region.USE_COMPUTED_SIZE);
        gridAdmin.setPrefHeight(Region.USE_COMPUTED_SIZE);
        gridAdmin.setMaxHeight(Region.USE_PREF_SIZE);

        // Ajouter un écouteur de changement de texte au champ de recherche
        searchIdAdmin.textProperty().addListener((observable, oldValue, newValue) -> {
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


    private List<CarteAdmin> recupererAdminsFromDatabase() {
        List<CarteAdmin> admins = new ArrayList<>();

        try {
            Connection connection = new Gestion().connectionBd();
            List<Admin> adminsFromDatabase = Gestion.recupererTousLesAdmins(connection);

            for (Admin admin : adminsFromDatabase) {
                CarteAdmin carte = new CarteAdmin();
                carte.setPicAdmin("./src/main/resources/com/club/Controller/pics/userImage.png");
                carte.setNameAdmin(admin.getNomUtilisateur());
                carte.setNameidAdmin(admin.getMotDePasse());
                carte.setTypeAdmin("Admin");
                carte.setAdminID(admin.getAdminID());
                admins.add(carte);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admins;
    }


    @FXML
    private Label nomAdmin;

    @FXML
    private ImageView photoAdmin;

    @FXML
    private Label prenomAdmin;


    public void setChosenCard (CarteAdmin carte){

        // Désélectionnez toutes les cartes
        for (CarteAdmin c : cartes) {
            c.setSelected(false);
        }

        // Sélectionnez la carte spécifiée
        carte.setSelected(true);


        // Transition de fondu
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), photoAdmin);
        fadeTransition.setFromValue(0); // Opacité initiale
        fadeTransition.setToValue(1); // Opacité finale
        fadeTransition.play();
        nomAdmin.setText(carte.getNameAdmin());
        prenomAdmin.setText(carte.getNameidAdmin());

        InputStream picStream = getClass().getResourceAsStream(carte.getPicAdmin());
        if (picStream != null) {
            Image nImage = new Image(picStream);
            photoAdmin.setImage(nImage);
        }



    }

    // Méthode pour filtrer les cartes selon le texte de recherche
    @FXML
    private void rechercherCartes() {
        String texteRecherche = searchIdAdmin.getText();
        filtrerCartes(texteRecherche);
        afficherCartes();
    }
    // Méthode pour filtrer les cartes en fonction du texte de recherche
    private void filtrerCartes(String texteRecherche) {
        // Réinitialiser la liste des cartes
        cartes.clear();
        // Récupérer à nouveau les adhérents de la base de données
        List<CarteAdmin> admins = recupererAdminsFromDatabase();

        // Filtrer les adhérents dont le nom commence par le texte de recherche
        for (CarteAdmin admin : admins) {
            if (admin.getNameAdmin().toLowerCase().contains(texteRecherche.toLowerCase())) {
                CarteAdmin carte = new CarteAdmin();
                carte.setPicAdmin("./src/main/resources/com/club/Controller/pics/userImage.png");
                carte.setNameAdmin(admin.getNameAdmin());
                carte.setNameidAdmin(admin.getNameidAdmin());
                carte.setTypeAdmin("Admin");
                carte.setAdminID(admin.getAdminID());
                cartes.add(carte);
            }
        }
    }

    // Méthode pour afficher les cartes filtrées
    private void afficherCartes() {
        // Effacer la grille
        gridAdmin.getChildren().clear();

        // Réinitialiser les colonnes et les lignes
        int columns = 1;
        int row = 1;

        try {
            // Ajouter chaque carte filtrée à la grille
            for (CarteAdmin carte : cartes) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/adminCard.fxml"));
                AnchorPane box = fxmlLoader.load();
                CarteAdminController carteAdminController = fxmlLoader.getController();
                carteAdminController.setData(carte, myListenerAdmin);

                if (columns == 3) {
                    columns = 1;
                    ++row;
                }

                gridAdmin.add(box, columns++, row);
                GridPane.setMargin(box, new Insets(10));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Déplacer ces lignes à l'extérieur de la boucle for
        //set grid width
        gridAdmin.setMinWidth(Region.USE_COMPUTED_SIZE);
        gridAdmin.setPrefWidth(Region.USE_COMPUTED_SIZE);
        gridAdmin.setMaxWidth(Region.USE_PREF_SIZE);

        //set grid height
        gridAdmin.setMinHeight(Region.USE_COMPUTED_SIZE);
        gridAdmin.setPrefHeight(Region.USE_COMPUTED_SIZE);
        gridAdmin.setMaxHeight(Region.USE_PREF_SIZE);

    }


    // Vos autres membres et méthodes existants

    // Méthode pour supprimer la carte sélectionnée
    @FXML
    private void Supprimercard(ActionEvent event) {
        if (carteSelectionnee != null) {
            // Créer une boîte de dialogue de confirmation
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Voulez-vous vraiment supprimer cet admin ?");
            confirmation.setContentText("Nom Utilisateur : " + carteSelectionnee.getNameAdmin() + "\nMot de Passe : " + carteSelectionnee.getNameidAdmin() );

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
                int adminID = carteSelectionnee.getAdminID();
                try {
                    Connection connection = new Gestion().connectionBd(); // Créez une instance de Gestion
                    Gestion gestion = new Gestion(); // Créez une instance de Gestion
                    gestion.supprimerAdmin(connection, adminID); // Appelez la méthode supprimerUtilisateur sur cette instance
                    cartes.remove(carteSelectionnee);
                    if (!cartes.isEmpty()) {
                        setChosenCard(cartes.get(0));
                    } else {
                        clearCardDisplay();
                    }
                    showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Admin supprimé avec succès!");

                    // Appeler switchToS() pour basculer vers la page après la suppression d'une carte
                    switchToVoirAdmins(event);

                } catch (Exception e) {
                    e.printStackTrace(); // Gérer d'autres exceptions génériques ici
                }
            }
        }
    }




    // Méthode pour effacer l'affichage si aucune carte n'est sélectionnée
    private void clearCardDisplay() {
        nomAdmin.setText("");
        prenomAdmin.setText("");
        
        // Effacer l'image de la photo
        photoAdmin.setImage(null);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("voirAdmins.fxml"));
            Parent root = loader.load();
            VoirAdminsController controller = loader.getController();
            // Remplacer la scène actuelle par la nouvelle scène rechargée
            Scene scene = new Scene(root);
            Stage stage = (Stage) searchIdAdmin.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/**************************************************************************************************** */
private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToVoirAdmins(Event event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("voirAdmins.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToScene2a(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("gestionProfilAdmin.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }




}
