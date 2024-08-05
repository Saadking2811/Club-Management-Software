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

import java.time.LocalTime;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Comparator;
import java.util.Collections;

import com.club.Model.Adherent;
import com.club.Model.Coach;
import com.club.Model.Creneau;
import com.club.Model.Gestion;
import com.club.Model.Groupe;
import com.club.Model.Planning;
import com.club.Model.Salle;



public class CrenauxController {

    @FXML
    private ScrollPane rootPane;

    @FXML
    private VBox CreneauBox;
    

    private Connection connection;

    @FXML
    private ComboBox<String> cmbSall;

    @FXML
    private ComboBox<String> cmbJour1;


    @FXML
    private TextField TD1;

    @FXML
    private TextField TD2;

    @FXML
    private TextField TF1;

    @FXML
    private TextField TF2;


    private void afficherCreneaux() throws SQLException {
        CreneauBox.getChildren().clear(); // Efface les anciennes données
        connection = new Gestion().connectionBd();
        List<Creneau> creneaux = Creneau.getListeCreneaux(connection);
    
        // Tri de la liste par ordre alphabétique du nom de groupe
     Collections.sort(creneaux, Comparator.comparing(Creneau::getJour));
        
    
        for (Creneau creneau : creneaux) {
            CreneauItem creneauItem = new CreneauItem(creneau);
            CreneauBox.getChildren().add(creneauItem);
        }
    }
    private class CreneauItem extends VBox {
        public CreneauItem(Creneau creneau) throws SQLException {
            setAlignment(Pos.CENTER_LEFT);
            setSpacing(5);
            Connection connection = new Gestion().connectionBd();
            setPadding(new Insets(10));
            String Dispo ="";
            if (creneau.getDisponible()) Dispo = "Disponible";
            else Dispo = "Non Disponible";
            setStyle("-fx-background-color: white; -fx-background-radius: 5px; -fx-border-color: #ddd; -fx-border-radius: 5px; -fx-border-width: 1px;");
            Label nameLabel = new Label( "Jour :"+creneau.getJour()+" "+"\nSalle : " +Salle.getSalleByID(connection, creneau.getSalleId()).getNomSalle()+"\nTemp : "+ creneau.getTempsDebut()+" " +creneau.getTempsFin()+"\n"+Dispo+"\t\t\t\t\t\t\t\t"  );
            // Transition de survol (hover)
            setOnMouseEntered(event -> {
                setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            });

            setOnMouseExited(event -> {
                setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            });

            // Gestionnaire d'événements pour le clic sur la salle
            setOnMouseClicked(event -> {
                // Animation de transition pour changer la couleur de fond
                String colorTransition = "-fx-background-color: #ADD8E6;"; // Bleu clair
                String originalColor = "-fx-background-color: WHITE;"; // Couleur d'origine

                // Appliquer la transition de couleur en utilisant du CSS
                setStyle(colorTransition);

                // Afficher une alerte de confirmation pour la suppression de la salle (à adapter selon votre logique)
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation de suppression");
                alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce creneau ?");
                try {
                    alert.setContentText("Détails du creneau : \n\n" +"Jour :"+creneau.getJour()+" "+"\nSalle : " +Salle.getSalleByID(connection, creneau.getSalleId()).getNomSalle()+"\nTemp : "+ creneau.getTempsDebut()+" " +creneau.getTempsFin());
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

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
                        creneau.supprimerCreneau(connection);
                        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), this);
                        fadeOut.setFromValue(1);
                        fadeOut.setToValue(0);

                        TranslateTransition translate = new TranslateTransition(Duration.seconds(0.5), this);
                        translate.setByY(-this.getHeight());

                    ParallelTransition parallelTransition = new ParallelTransition(fadeOut, translate);
                    parallelTransition.setOnFinished(e -> {
                    CreneauBox.getChildren().remove(this);
                       
                    });

                    showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Creneau supprimé avec succès!");
                    parallelTransition.play();
                      
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Gérer les erreurs
                    }
                } else {
                    setStyle(originalColor);
                }
            });
            getChildren().addAll(nameLabel);
        }
    }


    @FXML
    private void initialize() {
        
        try (Connection connection = new Gestion().connectionBd()) {
            if (connection != null) {
                afficherCreneaux();
                List<Salle> salles = new Gestion().getListeSalles(connection);
                ObservableList<String> nomsSalles = FXCollections.observableArrayList();

                for (Salle salle : salles) {
                    nomsSalles.add(salle.getNomSalle());
                }

                

                // Tri de la liste des noms de salles par ordre alphabétique
                FXCollections.sort(nomsSalles);

                cmbSall.setItems(nomsSalles);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la récupération des groupes.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        // Vérifier si tous les champs sont remplis
        if (TD.getText().isEmpty() || TF.getText().isEmpty() ) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez remplir tous les champs.");
            return; // Arrêter le traitement si un champ est vide
        }

        if (cmbJour1.getSelectionModel().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez d'abord selectionner le Jour.");
            return; // Arrêter le traitement si la combobox est vide
        }

        if (cmbSall.getSelectionModel().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez d'abord selectionner une salle.");
            return; // Arrêter le traitement si la combobox est vide
        }


        // Obtenez les autres valeurs des champs
        String TemD = TD.getText();
        String TemF = TF.getText();
        // Créez une boîte de dialogue de confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Voulez-vous vraiment ajouter ces Preferences ?");
        confirmation.setContentText("Jour :"+cmbJour1.getValue()+" "+"\nSalle : " +cmbSall.getValue()+"\nTemp : "+ TemD+" " +TemF);
        
        // Charger l'icône
        Image icon = new Image(getClass().getResourceAsStream("pics/Confirmation.png"));
        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(48);
        imageView.setFitHeight(48);
        confirmation.setGraphic(imageView);

        // Appliquer du style CSS
        DialogPane dialogPane = confirmation.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
        dialogPane.getStyleClass().add("my-alert");

        // Affichez la boîte de dialogue et attendez la réponse de l'utilisateur
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Si l'utilisateur a cliqué sur "OK", ajoutez le matériel et la dépense associée
                try {
                    Connection connection = new Gestion().connectionBd();
                    if (connection != null) {
                        // Ajouter le Creneau
                        LocalTime localTemD = LocalTime.parse(TemD);
                        LocalTime localTemF = LocalTime.parse(TemF);
                        Creneau creneau = new Creneau(0, Salle.getSalleIdByNom(connection, cmbSall.getValue()), cmbJour1.getValue(), localTemD, localTemF, true);
                          Planning plan = new Planning();
                        plan.ajouterCreneau(connection,creneau.getJour(),creneau.getTempsDebut(),creneau.getTempsFin(),creneau.getSalleId());


                        showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Creneau ajouté avec succès !");
                        connection.close();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Impossible de se connecter à la base de données.");
                    }
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout du Creneau.");
                    e.printStackTrace();
                }
            }
        });
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


    // Méthode pour passer à une autre scène
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

    public void switchToCreneaux(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("crenau.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCreneaux1(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("crenau.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    ///controller le temps pour l'ajout des créneaux

@FXML
private TextField TD;

@FXML
private TextField TF;

@FXML
private Text hhD;

@FXML
private Text hhF;
    @FXML
    private Text hhD1;

    @FXML
    private Text hhF1;


    @FXML
    private Text hhD2;

    @FXML
    private Text hhF2;

@FXML
private void handleTimeChange() {
    String timeD = TD.getText();
    String timeF = TF.getText();

    boolean isValidD = isValidTime(timeD);
    boolean isValidF = isValidTime(timeF);

    if (isValidD) {
        hhD.setText("Format valide");
    } else {
        hhD.setText("Le format doit être hh:mm");
    }

    if (isValidF) {
        hhF.setText("Format valide");
    } else {
        hhF.setText("Le format doit être hh:mm");
    }

}

    @FXML
    private void handleTimeChange1() {

        String timeD1 = TD1.getText();
        String timeF1 = TF1.getText();

        boolean isValidD1 = isValidTime(timeD1);
        boolean isValidF1 = isValidTime(timeF1);

        if (isValidD1) {
            hhD1.setText("Format valide");
        } else {
            hhD1.setText("Le format doit être hh:mm");
        }

        if (isValidF1) {
            hhF1.setText("Format valide");
        } else {
            hhF1.setText("Le format doit être hh:mm");
        }

        String timeD2 = TD2.getText();
        String timeF2 = TF2.getText();

        boolean isValidD2 = isValidTime(timeD2);
        boolean isValidF2 = isValidTime(timeF2);

        if (isValidD2) {
            hhD2.setText("Format valide");
        } else {
            hhD2.setText("Le format doit être hh:mm");
        }

        if (isValidF2) {
            hhF2.setText("Format valide");
        } else {
            hhF2.setText("Le format doit être hh:mm");
        }
    }


    private boolean isValidTime(String time) {
        return time.matches("([01]\\d|2[0-3]):[0-5]\\d");
    }

    
}
