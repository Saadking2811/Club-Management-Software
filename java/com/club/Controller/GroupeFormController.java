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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import java.sql.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class GroupeFormController {

    @FXML
    private TextField Name;
    @FXML
    private TextField taille;
    @FXML
    private TextField NBseances;


    @FXML
    private ComboBox<String> cmbSport; 
    


    @FXML
    private void initialize() {
        
        try (Connection connection = new Gestion().connectionBd()) {
            if (connection != null) {
                List<Sport> sports = new Gestion().getListeSports(connection);
                ObservableList<String> nomsSports = FXCollections.observableArrayList();

                for (Sport sport : sports) {
                    nomsSports.add(sport.getSportNom());
                }

                FXCollections.sort(nomsSports);
                cmbSport.setItems(nomsSports);

                
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la récupération des sports et des salles.");
            e.printStackTrace();
        }
    }

    @FXML
private void handleSubmit(ActionEvent event) {
    // Vérifier si tous les champs sont remplis
    if (Name.getText().isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez remplir tous les champs.");
        return; // Arrêter le traitement si un champ est vide
    }

    // Vérifier si cmbSport est vide
    if (cmbSport.getSelectionModel().isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez d'abord ajouter un sport.");
        return; // Arrêter le traitement si la combobox est vide
    }

    // Vérifier si la limite effectif est un entier
    int limitEffectif;
    try {
        limitEffectif = Integer.parseInt(taille.getText());
    } catch (NumberFormatException e) {
        showAlert(Alert.AlertType.ERROR, "Erreur", "La Taille doit être un nombre entier.");
        return;
    }


    int NB;
    try {
        NB = Integer.parseInt(NBseances.getText());
    } catch (NumberFormatException e) {
        showAlert(Alert.AlertType.ERROR, "Erreur", "Le nombre de seances doit être un nombre entier.");
        return;
    }

    

    // Obtenez les autres valeurs des champs
    String groupeNom = Name.getText();

    // Créez une boîte de dialogue de confirmation
    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
    confirmation.setTitle("Confirmation");
    confirmation.setHeaderText("Voulez-vous vraiment ajouter cet Groupe ?");
    confirmation.setContentText("Nom du groupe : " + groupeNom + "\nSport : " + cmbSport.getValue()  + "\nTaille : " + limitEffectif  + "\nNombre de seances : " + NB  );

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
            // Si l'utilisateur a cliqué sur "OK", ajoutez le groupe et la dépense associée
            try {
                Connection connection = new Gestion().connectionBd();
                if (connection != null) {

                    // Ajouter le groupe
                    Groupe groupe = new Groupe(0, groupeNom,"","",Sport.getSportIDByNom(connection, cmbSport.getValue()),null, null, null, null, limitEffectif, 3, NB);
                    groupe.ajouterGroupe(connection);
                    showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Groupe ajouté avec succès !");
                    clearFields();
                    connection.close();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Impossible de se connecter à la base de données.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout du Groupe.");
                e.printStackTrace();
            }
        }
    });
}



    private void clearFields() {
        Name.setPromptText("Nom du groupe");
        taille.setPromptText("Taille du groupe");
        NBseances.setPromptText("Nombre de seances");

        cmbSport.getSelectionModel().clearSelection();
        cmbSport.setPromptText("Sport");
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
    public void switchToVoirGroupes(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("voirGroupes.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    
}
