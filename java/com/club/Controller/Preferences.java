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
import java.time.LocalTime;

import com.club.Model.Adherent;
import com.club.Model.Coach;
import com.club.Model.Gestion;
import com.club.Model.Groupe;


public class Preferences {
    
    @FXML
    private ScrollPane rootPane;

    @FXML
    private VBox PreferenceBox;
    

    private Connection connection;

    @FXML
    private ComboBox<String> cmbG;

    @FXML
    private ComboBox<String> cmbJour1;

    @FXML
    private ComboBox<String> cmbJour2;

    @FXML
    private TextField TD1;

    @FXML
    private TextField TD2;

    @FXML
    private TextField TF1;

    @FXML
    private TextField TF2;


    private void afficherPreferences() throws SQLException {
        PreferenceBox.getChildren().clear(); // Efface les anciennes données
        connection = new Gestion().connectionBd();
        List<Groupe> groupes = Gestion.getListeGroupes(connection);
    
        // Tri de la liste par ordre alphabétique du nom de groupe
     Collections.sort(groupes, Comparator.comparing(Groupe::getNomGroupe));
        
    
        for (Groupe groupe : groupes) {
            GroupeItem groupeItem = new GroupeItem(groupe);
            PreferenceBox.getChildren().add(groupeItem);
        }
    }
    private static class GroupeItem extends VBox {
        public GroupeItem(Groupe groupe) {
            setAlignment(Pos.CENTER_LEFT);
            setSpacing(5);
            setPadding(new Insets(10));
            setStyle("-fx-background-color: white; -fx-background-radius: 5px; -fx-border-color: #ddd; -fx-border-radius: 5px; -fx-border-width: 1px;");
            Label nameLabel;
            if (groupe.getTempsd1()==null) nameLabel = new Label("Nom du Groupe : " +groupe.getNomGroupe()+"\nPreference1 : Rien"+"\nPreference2 : Rien\t\t\t\t\t\t"  );
            
            else nameLabel = new Label("Nom du Groupe : " +groupe.getNomGroupe()+"\nPreference1 : "+groupe.getJour1()+" " + groupe.getTempsd1()+" " +groupe.getTempsf1()+"\nPreference2 : "+groupe.getJour2()+" " +groupe.getTempsd2()+" " +groupe.getTempsf2()+"\t\t\t\t\t"  );
            // Transition de survol (hover)
            setOnMouseEntered(event -> {
                setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            });

            setOnMouseExited(event -> {
                setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            });
            getChildren().addAll(nameLabel);
        }
    }


    @FXML
    private void initialize() {
        
        try (Connection connection = new Gestion().connectionBd()) {
            if (connection != null) {
                afficherPreferences();
                List<Groupe> groupes = new Gestion().getListeGroupes(connection);
                ObservableList<String> nomsGroupes = FXCollections.observableArrayList();

                for (Groupe groupe : groupes) {
                      nomsGroupes.add(groupe.getNomGroupe());
                }

                // Tri de la liste des noms de groupes par ordre alphabétique
                FXCollections.sort(nomsGroupes);

              cmbG.setItems(nomsGroupes);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la récupération des groupes.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        // Vérifier si tous les champs sont remplis
        if (TD1.getText().isEmpty() || TF2.getText().isEmpty() || TF1.getText().isEmpty() || TD2.getText().isEmpty() ) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez remplir tous les champs.");
            return; // Arrêter le traitement si un champ est vide
        }

        // Vérifier si cmbSport est vide
        if (cmbG.getSelectionModel().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez d'abord ajouter un Groupe.");
            return; // Arrêter le traitement si la combobox est vide
        }

        if (cmbJour1.getSelectionModel().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez d'abord selectionner le Jour du Preference1.");
            return; // Arrêter le traitement si la combobox est vide
        }

        if (cmbJour2.getSelectionModel().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez d'abord selectionner le Jour du Preference2.");
            return; // Arrêter le traitement si la combobox est vide
        }


        // Obtenez les autres valeurs des champs
        String TemD1 = TD1.getText();
        String TemD2 = TD2.getText();
        String TemF1 = TF1.getText();
        String TemF2 = TF2.getText();
        // Créez une boîte de dialogue de confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Voulez-vous vraiment ajouter ces Preferences ?");
        confirmation.setContentText("Nom du groupe : " + cmbG.getValue()+ "\nPreference 1 : " + cmbJour1.getValue()+ " "+TemD1+" "+TemF1+ "\nPreference 2 : " + cmbJour2.getValue()+" "+TemD2+" "+TemF2);
        
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

        // Affichez la boîte de dialogue et attendez la réponse de l'utilisateur
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Si l'utilisateur a cliqué sur "OK", ajoutez le matériel et la dépense associée
                try {
                    Connection connection = new Gestion().connectionBd();
                    if (connection != null) {
                        // Ajouter le matériel
                        Groupe groupe = Groupe.getGroupeByID(connection, Groupe.getGroupeIDByNom(connection, cmbG.getValue()));
                        groupe.setJour1(cmbJour1.getValue());
                        groupe.setJour2(cmbJour2.getValue());
                        groupe.setTempsd1(LocalTime.parse(TemD1));
                        groupe.setTempsf1(LocalTime.parse(TemF1));
                        groupe.setTempsd2(LocalTime.parse(TemD2));
                        groupe.setTempsf2(LocalTime.parse(TemF2));
                        groupe.mettreAJourGroupe(connection);
                        showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Preferences ajouté avec succès !");
                        switchToPreference(event);
                        connection.close();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Impossible de se connecter à la base de données.");
                    }
                } catch (SQLException | IOException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout des Preferences.");
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

    public void switchToPreference(Event event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("prefrerence.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
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
