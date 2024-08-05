package com.club.Controller;

import com.jfoenix.controls.JFXToggleButton;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import com.club.Model.Gestion;
import com.club.Model.Sport;
import javafx.util.Duration;


public class VoirAdherentsController implements Initializable, MyListenerAdherent {

    @FXML
    private GridPane grid;

    private List<CarteAdherent> cartes;

    private MyListenerAdherent myListenerAdherent;

    private CarteAdherent carteSelectionnee;

    private List<Adherent2>  Adh2 = new ArrayList<Adherent2>() ;

    @FXML
    private ScrollPane scroll;
    
    @FXML
    private HBox H2;

    @FXML
    private Rectangle chosencardAdherent;

    @FXML
    private Button datails;

    @FXML
    private Button supprimer;

    @FXML
    private Label ageL;

    @FXML
    private Label catL;

    @FXML
    private Label gL;

    @FXML
    private JFXToggleButton switchButton;

    @FXML
    private ScrollPane scroll2;

    @FXML
    private AnchorPane a2;


    @FXML
    private TextField searchIdAdherent;

    @FXML
    private TextField searchIdAdherent1;

    @FXML
    private TextField searchIdAdherent11;

    @FXML
    private ComboBox<String> comboBoxGenre;

    @FXML
    private ComboBox<String> comboBoxSport;

    public void setCarteSelectionnee(CarteAdherent carteSelectionnee) {
        this.carteSelectionnee = carteSelectionnee;
    }
    public CarteAdherent getCarteSelectionnee() {
        return this.carteSelectionnee;
    }

    private List<CarteAdherentController> cardControllers = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try (Connection connection = new Gestion().connectionBd()) {
            if (connection != null) {
                Adh2 = adherents(Adherent.recupererTousLesAdherents(connection));
                List<Sport> sports = new Gestion().getListeSports(connection);
                ObservableList<String> nomsSports = FXCollections.observableArrayList();

                connection.close();
                for (Sport sport : sports) {
                    nomsSports.add(sport.getSportNom());
                }

                FXCollections.sort(nomsSports);
                comboBoxSport.setItems(nomsSports);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la récupération des salles.");
            e.printStackTrace();
        }

        cartes=recupererAdherentsFromDatabase();

        // setting the first chosen card


        if (cartes.size() > 0) {
            setChosenCard(cartes.get(0)); // Display details of the first card initially
            myListenerAdherent=new MyListenerAdherent() {

                @Override
                public void onClickListener(CarteAdherent carte) {
                    setCarteSelectionnee(carte);
                    setChosenCard(carte);
                    // Highlight the selected card and unhighlight others
                    for (CarteAdherentController controller : cardControllers) {
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
                fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/card.fxml")); // Assurez-vous que le chemin est correct
                AnchorPane box = fxmlLoader.load();
                CarteAdherentController carteAdherentController = fxmlLoader.getController();
                carteAdherentController.setData(cartes.get(i),myListenerAdherent);
                cardControllers.add(carteAdherentController);

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
        searchIdAdherent.textProperty().addListener((observable, oldValue, newValue) -> {
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
        prenom.setVisible(false);
        nom.setVisible(false);
        age.setVisible(false);
        genre.setVisible(false);
        categorie.setVisible(false);
        supprimer.setVisible(false);
        datails.setVisible(false);
        chosencardAdherent.setVisible(false);
        ageL.setVisible(false);
        catL.setVisible(false);
        gL.setVisible(false);
        photoAdherent.setVisible(false);


        double yPosition = 0.0;
        double hBoxHeight = 0;
        for (int j = 0; j < Adh2.size(); j++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/CaseAdherent2.fxml"));
            try {
                HBox hBox = fxmlLoader.load();
                CaseAdherent2Controller controller = fxmlLoader.getController();
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

    private List<Adherent2> adherents(List<Adherent> adherentsFromDatabase){
        List<Adherent2> ls2 = new ArrayList<>();
        try {
            Connection connection = new Gestion().connectionBd();
            for (Adherent adherent1 : adherentsFromDatabase) {
                Adherent2 adherent= new Adherent2();
        adherent.setFullname(adherent1.getNom()+" "+adherent1.getPrenom());
        adherent.setPhoto(adherent1.getPhoto());
        adherent.setDateee(adherent1.getDateEntree().toString());
        adherent.setDatenn(adherent1.getDateNaissance().toString());
        adherent.setEm(adherent1.getEmail());
        adherent.setNbA(String.valueOf(Absence.getNombreAbsences(connection, adherent1.getMembreID())));
        adherent.setPhone(String.valueOf(adherent1.getTelephone()));
        adherent.setAdhID(adherent1.getMembreID());
        adherent.setAge(adherent1.getAge());
        adherent.setGenre(adherent1.getGenre());
        ls2.add(adherent);
            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ls2;






    }



    private List<CarteAdherent> recupererAdherentsFromDatabase() {
        List<CarteAdherent> adherents = new ArrayList<>();

        try {
            Connection connection = new Gestion().connectionBd();
            List<Adherent> adherentsFromDatabase = Gestion.recupererTousLesAdherents(connection);

            for (Adherent adherent : adherentsFromDatabase) {
                CarteAdherent carte = new CarteAdherent();
                
                if (adherent.getPhoto() != null) {
                     carte.setPic(adherent.getPhoto());} 
                else carte.setPic("./src/main/resources/com/club/Controller/pics/userImage.png");
                carte.setCarta("./src/main/resources/com/club/Controller/pics/card.png");
                carte.setNamee(adherent.getNom());
                carte.setNameid(adherent.getPrenom());
                carte.setCategorie("Rien");
                carte.setTypee("Adherent");
                carte.setGenre(adherent.getGenre());
                carte.setAge(String.valueOf(adherent.getAge()));
                carte.setAdherentID(adherent.getMembreID());
                adherents.add(carte);
            }
            connection.close();
            

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return adherents;
    }

    @FXML
    private Label age;

    @FXML
    private Label categorie;

    @FXML
    private Label genre;

    @FXML
    private Label nom;

    @FXML
    private ImageView photoAdherent;

    @FXML
    private Label prenom;

    public void setChosenCard (CarteAdherent carte){

        // Désélectionnez toutes les cartes
        for (CarteAdherent c : cartes) {
            c.setSelected(false);
        }

        // Sélectionnez la carte spécifiée
        carte.setSelected(true);

        // Mettez à jour les détails de la carte sélectionnée
        nom.setText(carte.getNamee());
        prenom.setText(carte.getNameid());

        // Transition de fondu
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), photoAdherent);
        fadeTransition.setFromValue(0); // Opacité initiale
        fadeTransition.setToValue(1); // Opacité finale
        fadeTransition.play();
        InputStream picStream2 = getClass().getResourceAsStream(carte.getPic());

        String picStream = carte.getPic();
        if (Paths.get(picStream).isAbsolute()) {
        // C'est un chemin absolu
        File imageFile = new File(picStream);
        if (imageFile.exists()) {
            Image image = new Image(imageFile.toURI().toString());
            photoAdherent.setImage(image); // Définition de l'image sur l'ImageView pic
        } 
        } else {
            File imageFile = new File(picStream);
            Image image = new Image(imageFile.toURI().toString());
            photoAdherent.setImage(image);
        }

        age.setText(carte.getAge());
        genre.setText(carte.getGenre());
        categorie.setText(carte.getCategorie());


    }


    // Méthode pour filtrer les cartes selon le texte de recherche
    @FXML
    private void rechercherCartes() {
        String texteRecherche = searchIdAdherent.getText();
        filtrerCartes(texteRecherche);
        afficherCartes();
        filtrerCases(texteRecherche);
        afficherCasesAdherents();

    }
    // Méthode pour filtrer les cartes en fonction du texte de recherche
    private void filtrerCartes(String texteRecherche) {
        // Réinitialiser la liste des cartes
        cartes.clear();
        // Récupérer à nouveau les adhérents de la base de données
        List<CarteAdherent> adherents = recupererAdherentsFromDatabase();
        // Filtrer les adhérents dont le nom commence par le texte de recherche
        for (CarteAdherent adherent : adherents) {
            if (adherent.getNamee().toLowerCase().contains(texteRecherche.toLowerCase())) {
                CarteAdherent carte = new CarteAdherent();
                
                if (adherent.getPic() != null) {
                    carte.setPic(adherent.getPic());} 
                else carte.setPic("./src/main/resources/com/club/Controller/pics/userImage.png");
                carte.setCarta("./src/main/resources/com/club/Controller/pics/card.png");
                carte.setNamee(adherent.getNamee());
                carte.setNameid(adherent.getNameid());
                carte.setCategorie("Rien");
                carte.setTypee("Adherent");
                carte.setGenre(adherent.getGenre());
                carte.setAge(String.valueOf(adherent.getAge()));
                carte.setAdherentID(adherent.getAdherentID());
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
            for (CarteAdherent carte : cartes) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/card.fxml"));
                AnchorPane box = fxmlLoader.load();
                CarteAdherentController carteAdherentController = fxmlLoader.getController();
                carteAdherentController.setData(carte, myListenerAdherent);

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

        // Mettre à jour la taille de la grille
        grid.setMinWidth(Region.USE_COMPUTED_SIZE);
        grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
        grid.setMaxWidth(Region.USE_PREF_SIZE);

        grid.setMinHeight(Region.USE_COMPUTED_SIZE);
        grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
        grid.setMaxHeight(Region.USE_PREF_SIZE);



    }
    // Méthode pour filtrer les cartes en fonction du texte de recherche
    private void filtrerCases(String texteRecherche) {
        
        Adh2.clear();
        // Récupérer à nouveau les adhérents de la base de données
        try {
            Connection connection = new Gestion().connectionBd();
            List<Adherent> Adh22 = Adherent.recupererTousLesAdherents(connection);

            connection.close();
            List<Adherent> Ad23 = new ArrayList<Adherent>(); 

        
        // Filtrer les adhérents dont le nom commence par le texte de recherche
        for (Adherent adherent : Adh22) {
            if (adherent.getNom().toLowerCase().contains(texteRecherche.toLowerCase())) {
                
                Ad23.add(adherent);
            }
        }
        Adh2 = adherents(Ad23);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

private void afficherCasesAdherents() {

    // Nettoyer l'AnchorPane avant d'ajouter de nouveaux éléments
    a2.getChildren().clear();

        double yPosition = 0.0;
        double hBoxHeight = 0;
        for (int j = 0; j < Adh2.size(); j++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/CaseAdherent2.fxml"));
            try {
                HBox hBox = fxmlLoader.load();
                CaseAdherent2Controller controller = fxmlLoader.getController();
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
            confirmation.setHeaderText("Voulez-vous vraiment supprimer cet adhérent ?");
            confirmation.setContentText("Nom : " + carteSelectionnee.getNamee() + "\nPrénom : " + carteSelectionnee.getNameid() + "\nÂge : " + carteSelectionnee.getAge() + "\nGenre : " + carteSelectionnee.getGenre() );

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
                int adherentID = carteSelectionnee.getAdherentID();
                try {
                    Connection connection = new Gestion().connectionBd(); // Créez une instance de Gestion
                    Gestion gestion = new Gestion(); // Créez une instance de Gestion
                    gestion.supprimerUtilisateur(connection, adherentID); // Appelez la méthode supprimerUtilisateur sur cette instance
                    cartes.remove(carteSelectionnee);
                    connection.close();
                    if (!cartes.isEmpty()) {
                        setChosenCard(cartes.get(0));
                    } else {
                        clearCardDisplay();
                    }
                    showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Adherent supprimé avec succès!");

                    reloadPage();
                } catch (Exception e) {
                    e.printStackTrace(); // Gérer d'autres exceptions génériques ici
                }
            }
        }
    }




    // Méthode pour effacer l'affichage si aucune carte n'est sélectionnée
    private void clearCardDisplay() {
        nom.setText("");
        prenom.setText("");
        age.setText("");
        genre.setText("");
        categorie.setText("");
        // Effacer l'image de la photo
        photoAdherent.setImage(null);
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
        grid.getChildren().clear();
        try (Connection connection = new Gestion().connectionBd()) {
            if (connection != null) {
                Adh2 = adherents(Adherent.recupererTousLesAdherents(connection));
                connection.close();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la récupération des salles.");
            e.printStackTrace();
        }

        cartes=recupererAdherentsFromDatabase();

        // setting the first chosen card


        if (cartes.size() > 0) {
            setChosenCard(cartes.get(0)); // Display details of the first card initially
            myListenerAdherent=new MyListenerAdherent() {

                @Override
                public void onClickListener(CarteAdherent carte) {
                    setCarteSelectionnee(carte);
                    setChosenCard(carte);
                    // Highlight the selected card and unhighlight others
                    for (CarteAdherentController controller : cardControllers) {
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
                fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/card.fxml")); // Assurez-vous que le chemin est correct
                AnchorPane box = fxmlLoader.load();
                CarteAdherentController carteAdherentController = fxmlLoader.getController();
                carteAdherentController.setData(cartes.get(i),myListenerAdherent);
                cardControllers.add(carteAdherentController);

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

        

        double yPosition = 0.0;
        double hBoxHeight = 0;
        for (int j = 0; j < Adh2.size(); j++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/CaseAdherent2.fxml"));
            try {
                HBox hBox = fxmlLoader.load();
                CaseAdherent2Controller controller = fxmlLoader.getController();
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


    private void refreshPage() {
        

        // setting the first chosen card
        a2.getChildren().clear();
        grid.getChildren().clear();

        if (cartes.size() > 0) {
            setChosenCard(cartes.get(0)); // Display details of the first card initially
            myListenerAdherent=new MyListenerAdherent() {

                @Override
                public void onClickListener(CarteAdherent carte) {
                    setCarteSelectionnee(carte);
                    setChosenCard(carte);
                    // Highlight the selected card and unhighlight others
                    for (CarteAdherentController controller : cardControllers) {
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
                fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/card.fxml")); // Assurez-vous que le chemin est correct
                AnchorPane box = fxmlLoader.load();
                CarteAdherentController carteAdherentController = fxmlLoader.getController();
                carteAdherentController.setData(cartes.get(i),myListenerAdherent);
                cardControllers.add(carteAdherentController);

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

        


        double yPosition = 0.0;
        double hBoxHeight = 0;
        for (int j = 0; j < Adh2.size(); j++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/CaseAdherent2.fxml"));
            try {
                HBox hBox = fxmlLoader.load();
                CaseAdherent2Controller controller = fxmlLoader.getController();
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

    /*****************************************************  ************** */

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToScene2c(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("gestionProfilAdherent.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToDetailAdherent(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("detailAdherent.fxml"));
        Parent root = loader.load();

        // Obtenir le contrôleur du détail de l'adhérent
        ConnexionController detailController = loader.getController();

        // Passer les données de la carte sélectionnée au contrôleur du détail de l'adhérent
        detailController.initialiserAdherent(carteSelectionnee.getAdherentID());

        // Remplacer la scène actuelle par la scène des détails de l'adhérent
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1024, 760));
        stage.setResizable(false);
        stage.show();
    }

    public void switchToS(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("voirAdherents.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void onClickListener(CarteAdherent carte) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'onClickListener'");
    }



    //RECHERCHE PAR INTERVALLE D'AGE


    @FXML
    private void searchAdherentsParIntervalle(ActionEvent event) {
        if (searchIdAdherent1 != null) {
            String ageRange = searchIdAdherent1.getText();
            String[] ageRangeArray = ageRange.split("-"); // Assuming the format is minAge-maxAge

            if (ageRangeArray.length == 2) { // Check if the age range input is in the correct format
                int minAge = Integer.parseInt(ageRangeArray[0]);
                int maxAge = Integer.parseInt(ageRangeArray[1]);

                updateDisplayedCards(minAge, maxAge);
            } else {
                // Handle incorrect input format
                System.out.println("Invalid age range format. Please provide the age range in the format 'minAge-maxAge'.");
            }
        } else {
            // Handle case where searchIdAdherent is null
            System.out.println("searchIdAdherent is null. Ensure it is properly initialized in the FXML.");
        }
    }


    private void updateDisplayedCards(int minAge, int maxAge) {

        try (Connection connection = new Gestion().connectionBd()) {
            if (connection != null) {
                Adh2 = adherents(Adherent.recupererTousLesAdherents(connection));
                connection.close();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la récupération des salles.");
            e.printStackTrace();
        }

        cartes=recupererAdherentsFromDatabase();
        List<CarteAdherent> filteredCartes = cartes.stream()
                .filter(carte -> {
                    int age = Integer.parseInt(carte.getAge());
                    return age >= minAge && age <= maxAge;
                })
                .collect(Collectors.toList());

                List<Adherent2> filterlist = Adh2.stream()
                .filter(adherent -> {
                    int age = adherent.getAge(); // Appel de la méthode getAge() sur chaque instance de Adherent2
                    return age >= minAge && age <= maxAge;
                })
                .collect(Collectors.toList());

                Adh2 = filterlist;
                cartes = filteredCartes;

        refreshPage();
    }


    
    // Recherche par age

    @FXML
    private void searchAdherentsParAge(ActionEvent event) {

        
        if (searchIdAdherent11 != null) {
            String age = searchIdAdherent11.getText();
            try {
                int parsedAge = Integer.parseInt(age); // Try to parse the age as an integer
                updateDisplayedCards(parsedAge); // Update the displayed cards for the specific age
            } catch (NumberFormatException e) {
                System.out.println("Invalid age format. Please enter a valid age.");
            }
        } else {
            System.out.println("searchIdAdherent is null. Ensure it is properly initialized in the FXML.");
        }
    }

    private void updateDisplayedCards(int age) {

        try (Connection connection = new Gestion().connectionBd()) {
            if (connection != null) {
                Adh2 = adherents(Adherent.recupererTousLesAdherents(connection));
                connection.close();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la récupération des salles.");
            e.printStackTrace();
        }

        cartes=recupererAdherentsFromDatabase();
        List<CarteAdherent> filteredCartes = cartes.stream()
                .filter(carte -> Integer.parseInt(carte.getAge()) == age) // Filter for the specific age
                .collect(Collectors.toList());

                List<Adherent2> filterlist = Adh2.stream()
                .filter(adherent ->adherent.getAge() == age// Appel de la méthode getAge() sur chaque instance de Adherent2
                )
                .collect(Collectors.toList());

                Adh2 = filterlist;
                cartes = filteredCartes;
        refreshPage(); // Refresh the grid with filtered cards
    }

    // Recherche par Genre

    @FXML
    private void searchAdherentsParGenre(ActionEvent event) {
        String gender = comboBoxGenre.getValue(); // Get the selected item from the ComboBox
        if (gender != null && !gender.isEmpty()) {
            updateDisplayedCardsByGender(gender); // Filter and display cards by gender
        } else {
            System.out.println("Please select a gender to search for.");
        }
    }

    private void updateDisplayedCardsByGender(String gender) {

        try (Connection connection = new Gestion().connectionBd()) {
            if (connection != null) {
                Adh2 = adherents(Adherent.recupererTousLesAdherents(connection));
                connection.close();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la récupération des salles.");
            e.printStackTrace();
        }

        cartes=recupererAdherentsFromDatabase();
        List<CarteAdherent> filteredCartes = cartes.stream()
                .filter(carte -> carte.getGenre().equalsIgnoreCase(gender)) // Filter cards by gender
                .collect(Collectors.toList());

                List<Adherent2> filterlist = Adh2.stream()
                .filter(adherent ->adherent.getGenre().equalsIgnoreCase(gender)// Appel de la méthode getAge() sur chaque instance de Adherent2
                )
                .collect(Collectors.toList());

                Adh2 = filterlist;
                cartes = filteredCartes;

        refreshPage();
    }

// Recherche par Sport

    @FXML
    private void searchAdherentsParSport(ActionEvent event) {
        String selectedSport = comboBoxSport.getValue(); // Get the selected sport from the ComboBox
        if (selectedSport != null && !selectedSport.isEmpty()) {
            updateDisplayedCardsBySport(selectedSport); // Filter and display cards by sport
        } else {
            System.out.println("Please select a sport to search for.");
        }
    }


    private void updateDisplayedCardsBySport(String sport) {

        try (Connection connection = new Gestion().connectionBd()) {
            if (connection != null) {
                Adh2 = adherents(Adherent.recupererTousLesAdherents(connection));
                connection.close();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la récupération des salles.");
            e.printStackTrace();
        }

        cartes=recupererAdherentsFromDatabase();
        List<CarteAdherent> filteredCartes = cartes.stream()
                .filter(carte -> carte.getCategorie().equalsIgnoreCase(sport)) // Filter cards by sport
                .collect(Collectors.toList());

                List<Adherent2> filterlist = Adh2.stream()
                .filter(adherent ->adherent.getEm().equalsIgnoreCase(sport)// Appel de la méthode getAge() sur chaque instance de Adherent2
                )
                .collect(Collectors.toList());

                Adh2 = filterlist;
                cartes = filteredCartes;

        refreshPage();
    }




    @FXML
    public void handleTB(ActionEvent event) {
        if (switchButton.isSelected()) {
     
            // mode cartes on
            scroll.setVisible(true);
            prenom.setVisible(true);
            nom.setVisible(true);
            age.setVisible(true);
            genre.setVisible(true);
            categorie.setVisible(true);
            supprimer.setVisible(true);
            datails.setVisible(true);
            chosencardAdherent.setVisible(true);
            ageL.setVisible(true);
            catL.setVisible(true);
            gL.setVisible(true);
            photoAdherent.setVisible(true);

            //mode tableau off
            scroll2.setVisible(false);
            H2.setVisible(false);


        } else {
    
            // mode cartes off
            scroll.setVisible(false);
            prenom.setVisible(false);
            nom.setVisible(false);
            age.setVisible(false);
            genre.setVisible(false);
            categorie.setVisible(false);
            supprimer.setVisible(false);
            datails.setVisible(false);
            chosencardAdherent.setVisible(false);
            ageL.setVisible(false);
            catL.setVisible(false);
            gL.setVisible(false);
            photoAdherent.setVisible(false);

            //mode tableau on
            scroll2.setVisible(true);
            H2.setVisible(true);

        }
    }
}

