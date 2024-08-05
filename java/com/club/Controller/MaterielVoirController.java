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

import com.club.Model.DepensesJ;
import com.club.Model.Gestion;
import com.club.Model.Materiel;
import com.club.Model.Salle;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.scene.shape.Rectangle;

public class MaterielVoirController implements Initializable {

    @FXML
    private BorderPane rootPane;

    @FXML
    private VBox materialsBox;

    @FXML
    private TextField searchField;

    private Connection connection;
    private List<Materiel> allMateriels;

    public MaterielVoirController() {
        connection = new Gestion().connectionBd();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (connection != null) {
            afficherMateriels();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La connexion à la base de données a échoué.");
        }

        // Ajouter un écouteur de changement de texte au champ de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                // Recharger la page lorsque la zone de recherche est vide
                afficherMateriels();
            } else {
                // Filtrer et afficher les salles en fonction du texte de recherche
                filtrerEtAfficherMateriels(newValue);
            }
        });
    }

    private void afficherMateriels() {
        allMateriels = Materiel.getListeMateriels(connection);
        afficherMaterielsDansUI();
    }

    private void filtrerEtAfficherMateriels(String searchText) {
        allMateriels = allMateriels.stream()
                .filter(materiel -> materiel.getNomMateriel().toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());
        afficherMaterielsDansUI();
    }

    private void afficherMaterielsDansUI() {
        materialsBox.getChildren().clear(); // Efface les anciennes données


        for (Materiel materiel : allMateriels) {
            DepensesJ depensesJ = null;
            Salle salle = null;
            try {
                    depensesJ = DepensesJ.getDepensesJByID(connection, materiel.getMaterielID());
                    salle = Salle.getSalleByID(connection,materiel.getSalleID());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            MaterialItem materialItem = new MaterialItem(materiel,depensesJ,salle);
            materialsBox.getChildren().add(materialItem);
        }
    }

    // Gestionnaire d'événements pour le champ de recherche
    @FXML
    private void rechercherMateriels() {
        String searchText = searchField.getText();
        filtrerEtAfficherMateriels(searchText);
    }

    // Composant personnalisé pour afficher un matériel
    private class MaterialItem extends VBox {
        public MaterialItem(Materiel materiel,DepensesJ depensesJ, Salle salle) {
            setAlignment(Pos.CENTER_LEFT);
            setSpacing(5);
            setPadding(new Insets(10));
            setStyle("-fx-background-color: WHITE; -fx-background-radius: 5px; -fx-border-color: #ddd; -fx-border-radius: 5px; -fx-border-width: 1px;");

            Label nameLabel = new Label("Nom: " + materiel.getNomMateriel());
            Label categoryLabel = new Label("Catégorie: " + materiel.getCategorie());
            Label quantityLabel = new Label("Quantité: " + materiel.getQuantite());
            Label entryDateLabel ;
            Label salleLabel;
// Récupérer les dépenses journalières pour ce matériel
            
            

            if (depensesJ!=null) {
                entryDateLabel = new Label("Date d'entrée: " + depensesJ.getTemps());
            }
            else { entryDateLabel = new Label("Date d'entrée: Pas mentioné");}
            
            if (salle!=null) {
                salleLabel = new Label("Salle: " + salle.getNomSalle());
            }
            else { salleLabel = new Label("Salle: None");} 
                
            // Transition de fondu
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), this);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

            

            // Transition de survol (hover)
        setOnMouseEntered(event -> {
            setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        });

        setOnMouseExited(event -> {
            setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        });

            // Gestionnaire d'événements pour le clic sur le matériel
            setOnMouseClicked(event -> {
                // Animation de transition pour changer la couleur de fond
    String colorTransition = "-fx-background-color: #ADD8E6;"; // Bleu clair
    String originalColor = "-fx-background-color: WHITE;"; // Couleur d'origine
    
    // Appliquer la transition de couleur en utilisant du CSS
    setStyle(colorTransition);
    
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation de suppression");
                alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce matériel ?");
                alert.setContentText("Détails du matériel : \n\n" +
                        "Nom: " + materiel.getNomMateriel() + "\n" +
                        "Catégorie: " + materiel.getCategorie() + "\n" +
                        "Quantité: " + materiel.getQuantite()+ "\n"+
                          entryDateLabel.getText()+ "\n"+
                        "Salle: " + salleLabel.getText());

                // Charger l'icône
        File imageFile = new File("./src/main/resources/com/club/Controller/pics/Confirmation.png");
        
        Image image = new Image(imageFile.toURI().toString());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(48);
        imageView.setFitHeight(48);
                alert.setGraphic(imageView);        

                // Appliquer du style CSS
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
                dialogPane.getStyleClass().add("my-alert");        

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try  {
                        // Supprimer le matériel de la base de données
                        materiel.supprimerMateriel(connection);
                        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), this);
                        fadeOut.setFromValue(1);
                        fadeOut.setToValue(0);

                        TranslateTransition translate = new TranslateTransition(Duration.seconds(0.5), this);
                        translate.setByY(-this.getHeight());

                    ParallelTransition parallelTransition = new ParallelTransition(fadeOut, translate);
                    parallelTransition.setOnFinished(e -> {
                    materialsBox.getChildren().remove(this);
                       
                    });

                    showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Matériel supprimé avec succès!");
                    parallelTransition.play();
                      
                        
                    } catch (SQLException e) {
                        e.printStackTrace();
                        // Gérer les erreurs
                    }
                } else {
                    setStyle(originalColor);
                }
            });

            getChildren().addAll(nameLabel, categoryLabel, quantityLabel, salleLabel, entryDateLabel);
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

    // Méthode pour revenir à la vue précédente
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToScene4b(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("materiels.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}