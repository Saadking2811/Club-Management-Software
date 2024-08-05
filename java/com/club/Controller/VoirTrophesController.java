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
import javafx.scene.control.Label;
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
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.club.Model.Adherent;
import com.club.Model.Coach;
import com.club.Model.Gestion;
import com.club.Model.Groupe;
import com.club.Model.Sport;
import com.club.Model.Trophe;
import com.club.Model.Utilisateurs;

public class VoirTrophesController implements Initializable, MyListenerTrophe{

    @FXML
    private GridPane grid;

    private List<CarteTrophe> cartes;

    private MyListenerTrophe myListenerTrophe;

    private List<CarteTropheController> cardControllers = new ArrayList<>();

    private CarteTrophe carteSelectionnee;

    @FXML
    private TextField searchIdTrophe;

    public void setCarteSelectionnee(CarteTrophe carteSelectionnee) {
        this.carteSelectionnee = carteSelectionnee;
    }
    public CarteTrophe getCarteSelectionnee() {
        return this.carteSelectionnee;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cartes=recupererTrophesFromDatabase();
        // setting the first chosen card

        if (cartes.size() > 0) {

            setChosenCard(cartes.get(0)); // Display details of the first card initially
            myListenerTrophe = new MyListenerTrophe() {

                @Override
                public void onClickListener(CarteTrophe carte) {

                    setCarteSelectionnee(carte);
                    setChosenCard(carte);
                    // Highlight the selected card and unhighlight others
                    for (CarteTropheController controller : cardControllers) {
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
                fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/tropheCard.fxml")); // Assurez-vous que le chemin est correct
                AnchorPane box = fxmlLoader.load();
                CarteTropheController carteTropheController = fxmlLoader.getController();
                carteTropheController.setData(cartes.get(i), myListenerTrophe);
                cardControllers.add(carteTropheController);

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
        searchIdTrophe.textProperty().addListener((observable, oldValue, newValue) -> {
            // Vérifier si le nouveau texte est vide
            if (newValue.isEmpty()) {
                // Recharger la page lorsque la zone de recherche est vide
                reloadPage();
            } else {
                // Filtrer et afficher les trophes en fonction du texte de recherche
                rechercherCartes();
            }
        });
    }

    private List<CarteTrophe> recupererTrophesFromDatabase() {
        List<CarteTrophe> trophes = new ArrayList<>();

        try (Connection connection = new Gestion().connectionBd()){

            List<Trophe> tropheFromDatabase = Gestion.recupererTousLesTrophes(connection);

            for (Trophe trophe : tropheFromDatabase) {
                CarteTrophe carte = new CarteTrophe();
                carte.setPicTrophe("./src/main/resources/com/club/Controller/pics/trophy.png");

                carte.setNameTrophe(trophe.getTropheNom());

                carte.setNameCompetition(trophe.getCompetitionNom());
                carte.setTropheID(trophe.getTropheID());

                Utilisateurs utilisateurs = new Utilisateurs();
                utilisateurs = Utilisateurs.recupererUtilisateurParID(connection,trophe.getCoachID());
                carte.setNameCoach(utilisateurs.getNom()+" "+utilisateurs.getPrenom());

                Sport sport = new Sport();
                sport = Sport.getSportByID(connection,trophe.getSportID());
                carte.setNameSport(sport.getSportNom());

                String winner ;
                if (trophe.getCategorie()==0){
                    Utilisateurs utilisateurs1 = new Utilisateurs();
                    utilisateurs1 = Utilisateurs.recupererUtilisateurParID(connection,trophe.getAdherentGroupeID());
                    winner = utilisateurs1.getNom()+" "+utilisateurs1.getPrenom();
                }
                else {
                    Groupe groupe = new Groupe();
                    groupe = Groupe.getGroupeByID(connection,trophe.getAdherentGroupeID());
                    winner = groupe.getNomGroupe();
                }
                carte.setNameGagnant(winner);

                trophes.add(carte);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return trophes;
    }

    @FXML
    private ImageView photo;

    @FXML
    private Text trophe;

    @FXML
    private Text competiton;
    
    @FXML
    private Text gagnant;

    public void setChosenCard (CarteTrophe carte){

        // Désélectionnez toutes les cartes
        for (CarteTrophe c : cartes) {
            c.setSelected(false);
        }

        // Sélectionnez la carte spécifiée
        carte.setSelected(true);

        trophe.setText(carte.getNameTrophe());
        competiton.setText(carte.getNameCompetition());
        //coach.setText(carte.getNameCoach());
        //sport.setText(carte.getNameSport());
        gagnant.setText(carte.getNameGagnant());

        // Transition de fondu
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), photo);
        fadeTransition.setFromValue(0); // Opacité initiale
        fadeTransition.setToValue(1); // Opacité finale
        fadeTransition.play();
        InputStream picStream = getClass().getResourceAsStream(carte.getPicTrophe());
        if (picStream != null) {
            Image nImage = new Image(picStream);
            photo.setImage(nImage);
        }

    }

    @FXML
    private void rechercherCartes() {
        String texteRecherche = searchIdTrophe.getText();
        filtrerCartes(texteRecherche);
        afficherCartes();
    }

    private void filtrerCartes(String texteRecherche) {
        // Réinitialiser la liste des cartes
        cartes.clear();
        // Récupérer à nouveau les adhérents de la base de données
        List<CarteTrophe> trophes = recupererTrophesFromDatabase();

        // Filtrer les adhérents dont le nom commence par le texte de recherche
        try (Connection connection = new Gestion().connectionBd()){
            for (CarteTrophe trophe : trophes) {
                if (trophe.getNameTrophe().toLowerCase().contains(texteRecherche.toLowerCase())) {
                    CarteTrophe carte = new CarteTrophe();
                    carte.setPicTrophe("./src/main/resources/com/club/Controller/pics/trophy.png");
                    carte.setNameTrophe(trophe.getNameTrophe());
                    carte.setNameCompetition(trophe.getNameCompetition());
                    carte.setTropheID(trophe.getTropheID());
                    carte.setNameGagnant(trophe.getNameGagnant());
                    carte.setNameCoach(trophe.getNameCoach());
                    carte.setNameSport(trophe.getNameSport());

                    cartes.add(carte);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void afficherCartes() {
        // Effacer la grille
        grid.getChildren().clear();

        // Réinitialiser les colonnes et les lignes
        int columns = 1;
        int row = 1;

        try {
            // Ajouter chaque carte filtrée à la grille
            for (CarteTrophe carte : cartes) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/tropheCard.fxml"));
                AnchorPane box = fxmlLoader.load();
                CarteTropheController carteTropheController = fxmlLoader.getController();
                carteTropheController.setData(carte, myListenerTrophe);

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

    @FXML
    private void Supprimercard(ActionEvent event) {
        if (carteSelectionnee != null) {
            // Créer une boîte de dialogue de confirmation
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Voulez-vous vraiment supprimer cet trophe ?");
            confirmation.setContentText("Nom du trophe : " + carteSelectionnee.getNameTrophe() + "\nCompetition : " + carteSelectionnee.getNameCompetition() );

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
                int tropheID = carteSelectionnee.getTropheID();
                try {
                    Connection connection = new Gestion().connectionBd(); // Créez une instance de Gestion
                    Gestion gestion = new Gestion(); // Créez une instance de Gestion
                    gestion.supprimerTrophe(connection, tropheID); // Appelez la méthode supprimerUtilisateur sur cette instance
                    cartes.remove(carteSelectionnee);
                    if (!cartes.isEmpty()) {
                        setChosenCard(cartes.get(0));
                    } else {
                        clearCardDisplay();
                    }
                    showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Trophe supprimé avec succès!");

                    // Appeler switchToS() pour basculer vers la page après la suppression d'une carte
                    switchToVoirtrophes(event);

                } catch (Exception e) {
                    e.printStackTrace(); // Gérer d'autres exceptions génériques ici
                }
            }
        }
    }

    private void clearCardDisplay() {
        trophe.setText("");
        competiton.setText("");
        gagnant.setText("");

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("voirTrophes.fxml"));
            Parent root = loader.load();
            VoirTrophesController controller = loader.getController();
            // Remplacer la scène actuelle par la nouvelle scène rechargée
            Scene scene = new Scene(root);
            Stage stage = (Stage) searchIdTrophe.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //*************************
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToTropheDetail(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("tropheDetail.fxml"));
        Parent root = loader.load();

        // Obtenir le contrôleur du détail de l'adhérent
        ConnexionController detailController = loader.getController();

        // Passer les données de la carte sélectionnée au contrôleur du détail de l'adhérent
        detailController.initialiserTrophe(carteSelectionnee.getTropheID());

        // Remplacer la scène actuelle par la scène des détails de l'adhérent
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1024, 760));
        stage.setResizable(false);
        stage.show();
    }

    public void switchToScene8c(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("palmares.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToVoirtrophes(Event event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("voirTrophes.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void onClickListener(CarteTrophe carte) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onClickListener'");
    }
}
