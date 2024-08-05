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

import com.club.Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TropheFormController {

    @FXML
    private TextField txtCompetition;
    @FXML
    private TextField txtTrophe;
    @FXML
    private ComboBox<String> cmbCoach;
    @FXML
    private ComboBox<String> cmbAdherent;
    @FXML
    private ComboBox<String> cmbSport;
    //********************************************
    @FXML
    private TextField txtCompetition2;
    @FXML
    private TextField txtTrophe2;
    @FXML
    private ComboBox<String> cmbCoach2;
    @FXML
    private ComboBox<String> cmbGroupe;
    @FXML
    private ComboBox<String> cmbSport2;

    //*******************************

    private Map<String, Integer> coachMap = new HashMap<>();
    private Map<String, Integer> adherentMap = new HashMap<>();
    private Map<String, Integer> sportMap = new HashMap<>();
    private Map<String, Integer> groupeMap = new HashMap<>();


    @FXML
    private void initialize() {
        Connection connection = new Gestion().connectionBd();

        List<Coach> listCoach = new ArrayList<>();
        List<Adherent> listAdherent = new ArrayList<>();
        List<Sport> listSport = new ArrayList<>();
        List<Groupe> listGroupe = new ArrayList<>();

        List<String> stringCoach = new ArrayList<>();
        List<String> stringAdherent = new ArrayList<>();
        List<String> stringSport = new ArrayList<>();
        List<String> stringGroupe = new ArrayList<>();
        //*********************************************
        try {
            listCoach = Gestion.recupererTousLesCoaches(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Coach coach : listCoach){
            stringCoach.add(coach.getNom()+ " " + coach.getPrenom());
        }
        for (Coach coach : listCoach) {
            // Assuming MyObject has getStringValue() and getIntegerValue() methods
            coachMap.put(coach.getNom()+ " " +coach.getPrenom(), coach.getMembreID());
        }
        //*****************************************
        try {
            listAdherent = Gestion.recupererTousLesAdherents(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Adherent adherent : listAdherent){
            stringAdherent.add(adherent.getNom()+ " " + adherent.getPrenom());
        }
        for (Adherent adherent : listAdherent) {
            // Assuming MyObject has getStringValue() and getIntegerValue() methods
            adherentMap.put(adherent.getNom()+ " " + adherent.getPrenom(), adherent.getMembreID());
        }
        //************************************
        listSport = Gestion.getListeSports(connection);
        for (Sport sport : listSport){
            stringSport.add(sport.getSportNom());
        }
        for (Sport sport : listSport){
            // Assuming MyObject has getStringValue() and getIntegerValue() methods
            sportMap.put(sport.getSportNom(),sport.getSportID());
        }
        //*************************************
        listGroupe = Gestion.getListeGroupes(connection);
        for (Groupe groupe : listGroupe){
            stringGroupe.add(groupe.getNomGroupe());
        }
        for (Groupe groupe : listGroupe){
            // Assuming MyObject has getStringValue() and getIntegerValue() methods
            groupeMap.put(groupe.getNomGroupe(),groupe.getGroupeId());
        }
        initializeAdherent(stringCoach,stringAdherent,stringSport);
        initializeGroupe(stringCoach,stringGroupe,stringSport);

    }

    private void initializeAdherent(List<String> stringCoach, List<String> stringAdherent, List<String> stringSport){
        cmbCoach.getItems().addAll(stringCoach);
        cmbAdherent.getItems().addAll(stringAdherent);
        cmbSport.getItems().addAll(stringSport);
    }

    private void initializeGroupe(List<String> stringCoach, List<String> stringGroupe, List<String> stringSport){
        cmbCoach2.getItems().addAll(stringCoach);
        cmbGroupe.getItems().addAll(stringGroupe);
        cmbSport2.getItems().addAll(stringSport);
    }



    @FXML
    private void handleSubmitAdherent(ActionEvent event) {

        // Vérifier si tous les champs sont remplis
        if (txtTrophe.getText().isEmpty() || txtCompetition.getText().isEmpty() || cmbCoach.getSelectionModel().isEmpty() || cmbSport.getSelectionModel().isEmpty() || cmbAdherent.getSelectionModel().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez remplir tous les champs.");
            return;
        }

        String tropheNom = txtTrophe.getText();
        String competitionNom = txtCompetition.getText();
        String selectedCoach = cmbCoach.getSelectionModel().getSelectedItem();
        String selectedAdherent = cmbAdherent.getSelectionModel().getSelectedItem();
        String selectedSport= cmbSport.getSelectionModel().getSelectedItem();


        // Créer une boîte de dialogue de confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Voulez-vous vraiment ajouter cette trophe ?");
        confirmation.setContentText("Trophe: " + tropheNom + "\nCompetition : "+ competitionNom +"\nCoach : " + selectedCoach + "\nAdherent : " + selectedAdherent + "\nSport : " + selectedSport);

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

        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Connection connection = new Gestion().connectionBd();
                    if (connection != null) {
                        //Salle salle = new Salle(0, name, finalCategory, limitEffectif, false);
                        Trophe trophe = new Trophe();
                        trophe.setTropheNom(tropheNom);
                        trophe.setCompetitionNom(competitionNom);
                        trophe.setCategorie(0);
                        trophe.setCoachID(coachMap.get(selectedCoach));
                        trophe.setSportID(sportMap.get(selectedSport));
                        trophe.setAdherentGroupeID(adherentMap.get(selectedAdherent));
                        trophe.ajouterTrophe(connection);
                        //System.out.println("Trophe ajoutée : " + name + ", Limite Effectif : " + limitEffectif + ", Catégorie : " + finalCategory);
                        showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Trophe ajouté avec succès !");
                        clearFieldsAdherent();
                        connection.close();

                    } else {
                        showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Impossible de se connecter à la base de données.");
                    }
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout du matériel.");
                    e.printStackTrace();
                }
            }
        });
    }
    @FXML
    private void handleSubmitGroupe(ActionEvent event) {

        // Vérifier si tous les champs sont remplis
        if (txtTrophe2.getText().isEmpty() || txtCompetition2.getText().isEmpty() || cmbCoach2.getSelectionModel().isEmpty() || cmbSport2.getSelectionModel().isEmpty() || cmbGroupe.getSelectionModel().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez remplir tous les champs.");
            return;
        }

        String tropheNom2 = txtTrophe2.getText();
        String competitionNom2 = txtCompetition2.getText();
        String selectedCoach = cmbCoach2.getSelectionModel().getSelectedItem();
        String selectedGroupe = cmbGroupe.getSelectionModel().getSelectedItem();
        String selectedSport= cmbSport2.getSelectionModel().getSelectedItem();


        // Créer une boîte de dialogue de confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Voulez-vous vraiment ajouter cette trophe ?");
        confirmation.setContentText("Trophe: " + tropheNom2 + "\nCompetition : "+ competitionNom2 +"\nCoach : " + selectedCoach + "\nGroupe : " + selectedGroupe + "\nSport : " + selectedSport);

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

        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Connection connection = new Gestion().connectionBd();
                    if (connection != null) {
                        //Salle salle = new Salle(0, name, finalCategory, limitEffectif, false);
                        Trophe trophe = new Trophe();
                        trophe.setTropheNom(tropheNom2);
                        trophe.setCompetitionNom(competitionNom2);
                        trophe.setCategorie(1);
                        trophe.setCoachID(coachMap.get(selectedCoach));
                        trophe.setSportID(sportMap.get(selectedSport));
                        trophe.setAdherentGroupeID(groupeMap.get(selectedGroupe));
                        trophe.ajouterTrophe(connection);
                        //System.out.println("Trophe ajoutée : " + name + ", Limite Effectif : " + limitEffectif + ", Catégorie : " + finalCategory);
                        showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Trophe ajouté avec succès !");
                        clearFieldsGroupe();
                        connection.close();

                    } else {
                        showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Impossible de se connecter à la base de données.");
                    }
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout du matériel.");
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

    private void clearFieldsAdherent() {
        txtCompetition.clear();
        txtTrophe.clear();
        cmbCoach.getSelectionModel().clearSelection();
        cmbAdherent.getSelectionModel().clearSelection();
        cmbSport.getSelectionModel().clearSelection();
    }
    private void clearFieldsGroupe() {
        txtCompetition2.clear();
        txtTrophe2.clear();
        cmbCoach.getSelectionModel().clearSelection();
        cmbGroupe.getSelectionModel().clearSelection();
        cmbSport.getSelectionModel().clearSelection();
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToScene8c(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("palmares.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
