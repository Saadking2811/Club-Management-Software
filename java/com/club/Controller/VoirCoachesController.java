package com.club.Controller;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
import java.util.stream.Collectors;

import com.club.Model.Absence;
import com.club.Model.Adherent;
import com.club.Model.Adherent2;
import com.club.Model.Coach;
import com.club.Model.Gestion;
import com.club.Model.Sport;
import com.jfoenix.controls.JFXToggleButton;

public class VoirCoachesController implements Initializable {

    @FXML
    private GridPane gridCoach;

    private List<CarteCoach> cartes;

    private MyListenerCoach myListenerCoach;

    private CarteCoach carteSelectionnee;
    private List<Adherent2>  Adh2 = new ArrayList<Adherent2>() ;


    @FXML
    private HBox H2;

    @FXML
    private ScrollPane scroll;
    

    @FXML
    private Rectangle chosencardAdherent;

    @FXML
    private Button datails;

    @FXML
    private Button supprimer;

    

    @FXML
    private JFXToggleButton switchButton;

    @FXML
    private ScrollPane scroll2;

    @FXML
    private AnchorPane a2;

    @FXML
    private TextField searchIdAdherent1;

    @FXML
    private TextField searchIdAdherent11;

    @FXML
    private ComboBox<String> comboBoxGenre;

    @FXML
    private ComboBox<String> comboBoxSport;
    @FXML
    private TextField searchIdCoach;
    public void setCarteSelectionnee(CarteCoach carteSelectionnee) {
        this.carteSelectionnee = carteSelectionnee;
    }
    public CarteCoach getCarteSelectionnee() {
        return this.carteSelectionnee;
    }

    private List<CarteCoachController> cardControllers = new ArrayList<>();


    

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        

        try (Connection connection = new Gestion().connectionBd()) {
            if (connection != null) {
                Adh2 = adherents(Coach.recupererTousLesCoaches(connection));
                
                connection.close();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la récupération des salles.");
            e.printStackTrace();
        }
        cartes=recupererCoachsFromDatabase();

        // setting the first chosen card

        if (cartes.size() > 0) {
            setChosenCard(cartes.get(0)); // Display details of the first card initially
            myListenerCoach=new MyListenerCoach() {

                @Override
                public void onClickListener(CarteCoach carte) {
                    setCarteSelectionnee(carte);
                    setChosenCard(carte);
                    // Highlight the selected card and unhighlight others
                    for (CarteCoachController controller : cardControllers) {
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
                fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/coachCard.fxml")); // Assurez-vous que le chemin est correct
                AnchorPane box = fxmlLoader.load();
                CarteCoachController carteCoachController = fxmlLoader.getController();
                carteCoachController.setData(cartes.get(i),myListenerCoach);
                cardControllers.add(carteCoachController);

                if (columns == 3) {
                    columns = 1;
                    ++row;
                }

                gridCoach.add(box, columns++, row);
                GridPane.setMargin(box, new Insets(10));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Déplacer ces lignes à l'extérieur de la boucle for
        //set grid width
        gridCoach.setMinWidth(Region.USE_COMPUTED_SIZE);
        gridCoach.setPrefWidth(Region.USE_COMPUTED_SIZE);
        gridCoach.setMaxWidth(Region.USE_PREF_SIZE);

        //set grid height
        gridCoach.setMinHeight(Region.USE_COMPUTED_SIZE);
        gridCoach.setPrefHeight(Region.USE_COMPUTED_SIZE);
        gridCoach.setMaxHeight(Region.USE_PREF_SIZE);

        // Ajouter un écouteur de changement de texte au champ de recherche
        searchIdCoach.textProperty().addListener((observable, oldValue, newValue) -> {
            // Vérifier si le nouveau texte est vide
            if (newValue.isEmpty()) {
                // Recharger la page lorsque la zone de recherche est vide
                reloadPage();
            } else {
                // Filtrer et afficher les salles en fonction du texte de recherche
                rechercherCartes();
            }
        });


        // visibilité entre le mode tableau et le mode cartes
        // mode cartes off
        scroll.setVisible(false);
        prenomCoach.setVisible(false);
        nomCoach.setVisible(false);
        supprimer.setVisible(false);
        datails.setVisible(false);
        chosencardAdherent.setVisible(false);
        
        photoCoach.setVisible(false);


        double yPosition = 0.0;
        double hBoxHeight = 0;
        for (int j = 0; j < Adh2.size(); j++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/CaseCoach.fxml"));
            try {
                HBox hBox = fxmlLoader.load();
                CaseCoachController controller = fxmlLoader.getController();
                controller.setData(Adh2.get(j));
                
                // Calculer la hauteur préférée de l'HBox en fonction de son contenu réel
                hBox.applyCss();
                hBox.layout();
                hBoxHeight = hBox.prefHeight(-1);
                
                // Positionner l'HBox dans l'AnchorPane avec sa hauteur calculée
                AnchorPane.setTopAnchor(hBox, yPosition);
                AnchorPane.setLeftAnchor(hBox, 10.0);
                
                // Ajouter l'HBox à l'AnchorPane
                a2.getChildren().add(hBox);
                
                // Ajouter la transition de survol à l'HBox
                hBox.setOnMouseEntered(event -> {
                    hBox.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                });
        
                hBox.setOnMouseExited(event -> {
                    hBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                });
                
                // Augmenter la position Y pour le prochain élément
                yPosition += hBoxHeight + 10.0;
                
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        a2.setPrefHeight(yPosition*1.1);
    }        


    private List<CarteCoach> recupererCoachsFromDatabase() {
        List<CarteCoach> coachs = new ArrayList<>();

        try {
            Connection connection = new Gestion().connectionBd();
            List<Coach> CoachsFromDatabase = Gestion.recupererTousLesCoaches(connection);
            connection.close();
            for (Coach coach : CoachsFromDatabase) {
                CarteCoach carte = new CarteCoach();
                if (coach.getPhoto() != null) {
                    carte.setPicCoach(coach.getPhoto());} 
               else carte.setPicCoach("./src/main/resources/com/club/Controller/pics/userImage.png");
                carte.setNameidCoach(coach.getPrenom());
                carte.setNameCoach(coach.getNom());
                carte.setTypeCoach("Coach");
                carte.setCoachID(coach.getMembreID());
                coachs.add(carte);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return coachs;
    }

    @FXML
    private Label nomCoach;

    @FXML
    private ImageView photoCoach;

    @FXML
    private Label prenomCoach;


    public void setChosenCard (CarteCoach carte){

        // Désélectionnez toutes les cartes
        for (CarteCoach c : cartes) {
            c.setSelected(false);
        }
        // Sélectionnez la carte spécifiée
        carte.setSelected(true);

        nomCoach.setText(carte.getNameCoach());
        prenomCoach.setText(carte.getNameidCoach());
        // Transition de fondu
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), photoCoach);
        fadeTransition.setFromValue(0); // Opacité initiale
        fadeTransition.setToValue(1); // Opacité finale
        fadeTransition.play();
        InputStream picStream2 = getClass().getResourceAsStream(carte.getPicCoach());
        String picStream = carte.getPicCoach();
        if (Paths.get(picStream).isAbsolute()) {
        // C'est un chemin absolu
        File imageFile = new File(picStream);
        if (imageFile.exists()) {
            Image image = new Image(imageFile.toURI().toString());
            photoCoach.setImage(image); // Définition de l'image sur l'ImageView pic
        } 
        } else {
            File imageFile = new File(picStream);
            Image image = new Image(imageFile.toURI().toString());
            photoCoach.setImage(image);
        }



    }

    // Méthode pour filtrer les cartes selon le texte de recherche
    @FXML
    private void rechercherCartes() {
        String texteRecherche = searchIdCoach.getText();
        filtrerCartes(texteRecherche);
        afficherCartes();
        filtrerCases(texteRecherche);
        afficherCasesAdherents();;
    }
    // Méthode pour filtrer les cartes en fonction du texte de recherche
    private void filtrerCartes(String texteRecherche) {
        // Réinitialiser la liste des cartes
        cartes.clear();
        // Récupérer à nouveau les adhérents de la base de données
        List<CarteCoach> Coachs = recupererCoachsFromDatabase();

        
        // Filtrer les adhérents dont le nom commence par le texte de recherche
        for (CarteCoach coach : Coachs) {
            if (coach.getNameCoach().toLowerCase().contains(texteRecherche.toLowerCase())) {
                CarteCoach carte = new CarteCoach();
                if (coach.getPicCoach() != null) {
                    carte.setPicCoach(coach.getPicCoach());} 
               else carte.setPicCoach("pics/userImage.png");
                carte.setNameCoach(coach.getNameCoach());
                carte.setNameidCoach(coach.getNameidCoach());
                carte.setTypeCoach("Coach");
                carte.setCoachID(coach.getCoachID());
                cartes.add(carte);
            }
        }
    }

    // Méthode pour afficher les cartes filtrées
    private void afficherCartes() {
        // Effacer la grille
        gridCoach.getChildren().clear();

        // Réinitialiser les colonnes et les lignes
        int columns = 1;
        int row = 1;

        try {
            // Ajouter chaque carte filtrée à la grille
            for (CarteCoach carte : cartes) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/coachCard.fxml"));
                AnchorPane box = fxmlLoader.load();
                CarteCoachController carteCoachController = fxmlLoader.getController();
                carteCoachController.setData(carte, myListenerCoach);

                if (columns == 3) {
                    columns = 1;
                    ++row;
                }

                gridCoach.add(box, columns++, row);
                GridPane.setMargin(box, new Insets(10));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Mettre à jour la taille de la grille
        gridCoach.setMinWidth(Region.USE_COMPUTED_SIZE);
        gridCoach.setPrefWidth(Region.USE_COMPUTED_SIZE);
        gridCoach.setMaxWidth(Region.USE_PREF_SIZE);

        gridCoach.setMinHeight(Region.USE_COMPUTED_SIZE);
        gridCoach.setPrefHeight(Region.USE_COMPUTED_SIZE);
        gridCoach.setMaxHeight(Region.USE_PREF_SIZE);
    }


    // Méthode pour filtrer les cartes en fonction du texte de recherche
    private void filtrerCases(String texteRecherche) {
        
        Adh2.clear();
        // Récupérer à nouveau les adhérents de la base de données
        try {
            Connection connection = new Gestion().connectionBd();
            List<Coach> Adh22 = Coach.recupererTousLesCoaches(connection);
            connection.close();
            List<Coach> Ad23 = new ArrayList<Coach>(); 

        
        // Filtrer les adhérents dont le nom commence par le texte de recherche
        for (Coach adherent : Adh22) {
            if (adherent.getNom().toLowerCase().contains(texteRecherche.toLowerCase())) {
                
                Ad23.add(adherent);
            }
        }
        Adh2 = adherents(Ad23);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


private List<Adherent2> adherents(List<Coach> adherentsFromDatabase){
        List<Adherent2> ls2 = new ArrayList<>();
        for (Coach adherent1 : adherentsFromDatabase) {
            Adherent2 adherent= new Adherent2();
      adherent.setFullname(adherent1.getNom()+" "+adherent1.getPrenom());
      adherent.setPhoto(adherent1.getPhoto());
      adherent.setDateee(adherent1.getDateEntree().toString());
      adherent.setDatenn(adherent1.getDateNaissance().toString());
      adherent.setEm(adherent1.getEmail());
      adherent.setPhone(String.valueOf(adherent1.getTelephone()));
      adherent.setAdhID(adherent1.getMembreID());
      ls2.add(adherent);
        }

        return ls2;






    }

private void afficherCasesAdherents() {
    

    // Nettoyer l'AnchorPane avant d'ajouter de nouveaux éléments
    a2.getChildren().clear();

    double yPosition = 0.0;
        double hBoxHeight = 0;
        for (int j = 0; j < Adh2.size(); j++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/CaseCoach.fxml"));
            try {
                HBox hBox = fxmlLoader.load();
                CaseCoachController controller = fxmlLoader.getController();
                controller.setData(Adh2.get(j));
                
                // Calculer la hauteur préférée de l'HBox en fonction de son contenu réel
                hBox.applyCss();
                hBox.layout();
                hBoxHeight = hBox.prefHeight(-1);
                
                // Positionner l'HBox dans l'AnchorPane avec sa hauteur calculée
                AnchorPane.setTopAnchor(hBox, yPosition);
                AnchorPane.setLeftAnchor(hBox, 10.0);
                
                // Ajouter l'HBox à l'AnchorPane
                a2.getChildren().add(hBox);
                
                // Ajouter la transition de survol à l'HBox
                hBox.setOnMouseEntered(event -> {
                    hBox.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                });
        
                hBox.setOnMouseExited(event -> {
                    hBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                });
                
                // Augmenter la position Y pour le prochain élément
                yPosition += hBoxHeight + 10.0;
                
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        a2.setPrefHeight(yPosition*1.1);
    }        


    // Vos autres membres et méthodes existants

    // Méthode pour supprimer la carte sélectionnée
    @FXML
    private void Supprimercard(ActionEvent event) {
        if (carteSelectionnee != null) {
            // Créer une boîte de dialogue de confirmation
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Voulez-vous vraiment supprimer cet coach ?");
            confirmation.setContentText("Nom : " + carteSelectionnee.getNameCoach() + "\nPrénom : " + carteSelectionnee.getNameidCoach());

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
                int coachID = carteSelectionnee.getCoachID();
                try {
                    Connection connection = new Gestion().connectionBd(); // Créez une instance de Gestion
                    Gestion gestion = new Gestion(); // Créez une instance de Gestion
                    gestion.supprimerUtilisateur(connection, coachID); // Appelez la méthode supprimerUtilisateur sur cette instance
                    cartes.remove(carteSelectionnee);
                    connection.close();
                    if (!cartes.isEmpty()) {
                        setChosenCard(cartes.get(0));
                    } else {
                        clearCardDisplay();
                    }
                    showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Coach supprimé avec succès!");

                    // Appeler switchToS() pour basculer vers la page après la suppression d'une carte
                    reloadPage();

                } catch (Exception e) {
                    e.printStackTrace(); // Gérer d'autres exceptions génériques ici
                }
            }
        }
    }




    // Méthode pour effacer l'affichage si aucune carte n'est sélectionnée
    private void clearCardDisplay() {
        nomCoach.setText("");
        prenomCoach.setText("");
        // Effacer l'image de la photo
        photoCoach.setImage(null);
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
        // Nettoyer l'AnchorPane avant d'ajouter de nouveaux éléments
        a2.getChildren().clear();
        gridCoach.getChildren().clear();
        try (Connection connection = new Gestion().connectionBd()) {
            if (connection != null) {
                Adh2 = adherents(Coach.recupererTousLesCoaches(connection));
                connection.close();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la récupération des salles.");
            e.printStackTrace();
        }

        cartes=recupererCoachsFromDatabase();

        // setting the first chosen card

        if (cartes.size() > 0) {
            setChosenCard(cartes.get(0)); // Display details of the first card initially
            myListenerCoach=new MyListenerCoach() {

                @Override
                public void onClickListener(CarteCoach carte) {
                    setCarteSelectionnee(carte);
                    setChosenCard(carte);
                    // Highlight the selected card and unhighlight others
                    for (CarteCoachController controller : cardControllers) {
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
                fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/coachCard.fxml")); // Assurez-vous que le chemin est correct
                AnchorPane box = fxmlLoader.load();
                CarteCoachController carteCoachController = fxmlLoader.getController();
                carteCoachController.setData(cartes.get(i),myListenerCoach);
                cardControllers.add(carteCoachController);

                if (columns == 3) {
                    columns = 1;
                    ++row;
                }

                gridCoach.add(box, columns++, row);
                GridPane.setMargin(box, new Insets(10));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Déplacer ces lignes à l'extérieur de la boucle for
        //set grid width
        gridCoach.setMinWidth(Region.USE_COMPUTED_SIZE);
        gridCoach.setPrefWidth(Region.USE_COMPUTED_SIZE);
        gridCoach.setMaxWidth(Region.USE_PREF_SIZE);

        //set grid height
        gridCoach.setMinHeight(Region.USE_COMPUTED_SIZE);
        gridCoach.setPrefHeight(Region.USE_COMPUTED_SIZE);
        gridCoach.setMaxHeight(Region.USE_PREF_SIZE);

        

        double yPosition = 0.0;
        double hBoxHeight = 0;
        for (int j = 0; j < Adh2.size(); j++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/CaseCoach.fxml"));
            try {
                HBox hBox = fxmlLoader.load();
                CaseCoachController controller = fxmlLoader.getController();
                controller.setData(Adh2.get(j));
                
                // Calculer la hauteur préférée de l'HBox en fonction de son contenu réel
                hBox.applyCss();
                hBox.layout();
                hBoxHeight = hBox.prefHeight(-1);
                
                // Positionner l'HBox dans l'AnchorPane avec sa hauteur calculée
                AnchorPane.setTopAnchor(hBox, yPosition);
                AnchorPane.setLeftAnchor(hBox, 10.0);
                
                // Ajouter l'HBox à l'AnchorPane
                a2.getChildren().add(hBox);
                
                // Ajouter la transition de survol à l'HBox
                hBox.setOnMouseEntered(event -> {
                    hBox.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                });
        
                hBox.setOnMouseExited(event -> {
                    hBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                });
                
                // Augmenter la position Y pour le prochain élément
                yPosition += hBoxHeight + 10.0;
                
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        a2.setPrefHeight(yPosition*1.1);
    }        



/***************************************************** */
private Stage stage;
    private Scene scene;
    private Parent root;

    


    public void switchToDetailCoach(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("detailCoach.fxml"));
        Parent root = loader.load();

        // Obtenir le contrôleur du détail de l'adhérent
        ConnexionController detailController = loader.getController();

        // Passer les données de la carte sélectionnée au contrôleur du détail de l'adhérent
        detailController.initialiserCoach(carteSelectionnee.getCoachID());

        // Remplacer la scène actuelle par la scène des détails de l'adhérent
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1024, 760));
        stage.setResizable(false);
        stage.show();
    }

    public void switchToScene2b(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("gestionProfilCoach.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    

    public void switchToVoirCoachs(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("voirCoachs.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    




    @FXML
    public void handleTB(ActionEvent event) {
        if (switchButton.isSelected()) {
     
            // mode cartes on
            scroll.setVisible(true);
            prenomCoach.setVisible(true);
            nomCoach.setVisible(true);
            supprimer.setVisible(true);
            datails.setVisible(true);
            chosencardAdherent.setVisible(true);
            
            photoCoach.setVisible(true);

            //mode tableau off
            scroll2.setVisible(false);
            H2.setVisible(false);


        } else {
    
            // mode cartes off
            scroll.setVisible(false);
            prenomCoach.setVisible(false);
            nomCoach.setVisible(false);
            supprimer.setVisible(false);
            datails.setVisible(false);
            chosencardAdherent.setVisible(false);
           
            photoCoach.setVisible(false);
            H2.setVisible(true);

            //mode tableau on
            scroll2.setVisible(true);

        }
    }

}
