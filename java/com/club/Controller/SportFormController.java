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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SportFormController {

    @FXML
    private TextField Activite;


    @FXML
    private ComboBox<String> cmbCateg;

    @FXML
    private TextField txtNewCateg;

    @FXML
    private void initialize() {
        cmbCateg.getItems().addAll("Combat", "Balles","Natation", "Nouveau");
        cmbCateg.setOnAction(e -> {
            String selectedCategory = cmbCateg.getSelectionModel().getSelectedItem();
            txtNewCateg.setVisible(selectedCategory != null && selectedCategory.equals("Nouveau"));
        });

    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        // Vérifier si tous les champs sont remplis
        if (Activite.getText().isEmpty() || (cmbCateg.getValue().equals("Nouveau") && txtNewCateg.getText().isEmpty())) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez remplir tous les champs.");
            return; // Arrêter le traitement si un champ est vide
        }


        // Obtenez les autres valeurs des champs
        String sportNom = Activite.getText();
        String selectedCategory = cmbCateg.getSelectionModel().getSelectedItem();
        String category;

        if (selectedCategory.equals("Nouveau")) {
            category = txtNewCateg.getText();
        } else {
            category = selectedCategory;
        }
        final String finalCategory = category; // Déclarer comme final ici

        // Créez une boîte de dialogue de confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Voulez-vous vraiment ajouter ce sport ?");
        confirmation.setContentText("Nom : " + sportNom +"\nCatégorie : " + finalCategory );
        
        // Charger l'icône
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
                        // Ajouter le sport
                        Sport sport = new Sport(0,finalCategory,sportNom,false);
                         new Gestion().ajouterSport(connection, sport);
                        showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Sport ajouté avec succès !");
                        clearFields();
                        connection.close();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Impossible de se connecter à la base de données.");
                    }
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout du sport.");
                    e.printStackTrace();
                }
            }
        });
    }


    private void clearFields() {
        Activite.clear();
        cmbCateg.getSelectionModel().clearSelection();
        txtNewCateg.clear();
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
    //go to Gestion des sports page
    public void switchToScene3(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("gestionSport.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    
}

