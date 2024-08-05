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

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.club.Model.Adherent;
import com.club.Model.Admin;
import com.club.Model.Gestion;
import com.club.Model.Groupe;
import com.club.Model.Offre;
import com.club.Model.Sport;
public class VoirEvaluationController implements Initializable {


    @FXML
    private ComboBox<String> cmbSport1;

    @FXML
    private ComboBox<String> cmbSport2;

    @FXML
    private ComboBox<String> cmbadh;

    @FXML
    private ComboBox<String> cmbG;

    @FXML
    private AnchorPane Anch1;
    
    @FXML
    private AnchorPane Anch2;


    private static final int NUM_CIRCLES = 50;
    private static final Duration DURATION = Duration.seconds(10);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try (Connection connection = new Gestion().connectionBd()) {
            if (connection != null) {
                List<Sport> sports = new Gestion().getListeSports(connection);
                sports.sort(Comparator.comparing(Sport::getSportNom));
                ObservableList<String> nomsSports = FXCollections.observableArrayList();

                for (Sport sport : sports) {
                    nomsSports.add(sport.getSportNom());
                }

                cmbSport1.setItems(nomsSports);
                cmbSport2.setItems(nomsSports);

                List<Groupe> groupes = new Gestion().getListeGroupes(connection);
                groupes.sort(Comparator.comparing(Groupe::getNomGroupe));
                ObservableList<String> nomsGroupes = FXCollections.observableArrayList();

                for (Groupe groupe : groupes) {
                    nomsGroupes.add(groupe.getNomGroupe());
                }

                cmbG.setItems(nomsGroupes);


                List<Adherent> adherents = new Gestion().recupererTousLesAdherents(connection);
                adherents.sort(Comparator.comparing(Adherent::getNom).thenComparing(Adherent::getPrenom));
                ObservableList<String> nomsAdh = FXCollections.observableArrayList();
                
                for (Adherent adh : adherents) {
                    nomsAdh.add(adh.getNom() + "\t\t" + adh.getPrenom());
                }
                
                cmbadh.setItems(nomsAdh);
                
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la récupération des sports.");
            e.printStackTrace();
        }
        LuxuriousParticlesAnimation animation = new LuxuriousParticlesAnimation();
        animation.startAnimation(Anch1);
        animation.startAnimation(Anch2);
        
        // Ajoutez d'autres éléments si nécessaire
    }

    
    
    public int getMembreIDFromNomPrenom(String nomPrenom) throws SQLException {
        String[] split = nomPrenom.split("\t\t"); // Sépare le nom et le prénom
        if (split.length == 2) {
            String nom = split[0];
            String prenom = split[1];
            Connection connection = new Gestion().connectionBd(); // Obtenez votre connexion à la base de données ici
            if (connection != null) {
                return Gestion.getMembreIDFromNomPrenom(connection, nom, prenom);
            } else {
                System.err.println("La connexion à la base de données est null.");
            }
        } else {
            System.err.println("Format de nom et prénom incorrect.");
        }
        return 0; // Retourne 0 si le MembreID n'a pas été trouvé
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


    
    private Stage stage;
    private Scene scene;
    private Parent root;
    //go to performance page
    public void switchToScene8(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("Performance.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }



}
