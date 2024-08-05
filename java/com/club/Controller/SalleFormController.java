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

import com.club.Model.Gestion;
import com.club.Model.Salle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class SalleFormController {
    //Classe pour formulaire materiels
     @FXML
    private TextField txtName;

    @FXML
    private ComboBox<String> cmbCategory;

    @FXML
    private TextField txtNewCategory;

    @FXML
    private TextField txtLimitEffectif;

    @FXML
    private void initialize() {
        cmbCategory.getItems().addAll("Cardio", "Musculation", "Combat", "Piscine", "Basketball", "Football", "Volley-ball", "Nouveau");
        cmbCategory.setOnAction(e -> {
            String selectedCategory = cmbCategory.getSelectionModel().getSelectedItem();
            txtNewCategory.setVisible(selectedCategory != null && selectedCategory.equals("Nouveau"));
        });
    }

    @FXML
private void handleSubmit(ActionEvent event) {
    
    // Vérifier si tous les champs sont remplis
    if (txtName.getText().isEmpty() || txtLimitEffectif.getText().isEmpty() || cmbCategory.getSelectionModel().isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez remplir tous les champs.");
        return;
    }

    // Vérifier si la limite effectif est un entier
    int limitEffectif;
    try {
        limitEffectif = Integer.parseInt(txtLimitEffectif.getText());
    } catch (NumberFormatException e) {
        showAlert(Alert.AlertType.ERROR, "Erreur", "La limite effectif doit être un nombre entier.");
        return;
    }


    String name = txtName.getText();
    String selectedCategory = cmbCategory.getSelectionModel().getSelectedItem();
    String category;
    if (selectedCategory.equals("Nouveau")) {
        category = txtNewCategory.getText();
    } else {
        category = selectedCategory;
    }
    final String finalCategory = category; // Déclarer comme final ici


    // Créer une boîte de dialogue de confirmation
    Alert confirmation = new Alert(AlertType.CONFIRMATION);
    confirmation.setTitle("Confirmation");
    confirmation.setHeaderText("Voulez-vous vraiment ajouter cette salle ?");
    confirmation.setContentText("Nom : " + name + "\nLimite Effectif : " + limitEffectif + "\nCatégorie : " + finalCategory);
    
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

    // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
    confirmation.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
            try {
                Connection connection = new Gestion().connectionBd();
                if (connection != null) {
                Salle salle = new Salle(0, name, finalCategory, limitEffectif, false);
                salle.ajouterSalle(connection);
                System.out.println("Salle ajoutée : " + name + ", Limite Effectif : " + limitEffectif + ", Catégorie : " + finalCategory);
                showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Salle ajouté avec succès !");
                clearFields();
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


private void clearFields() {
    txtName.clear();
    txtLimitEffectif.clear();
    cmbCategory.getSelectionModel().clearSelection();
    txtNewCategory.clear();
}
    //----------------------------------------------------------------------------------------------------------------
    private Stage stage;
    private Scene scene;
    private Parent root;
    public void switchToScene4a(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("salles.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

}
