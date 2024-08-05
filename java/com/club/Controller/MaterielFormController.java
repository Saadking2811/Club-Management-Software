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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class MaterielFormController {

    @FXML
    private TextField txtName;

    @FXML
    private TextField Quantite;

    @FXML
    private ComboBox<String> cmbCategory;

    @FXML
    private TextField txtNewCategory;

    @FXML
    private ComboBox<String> cmbSalle;

    @FXML
    private DatePicker Ladate;

    @FXML
    private void initialize() {
        cmbCategory.getItems().addAll("Tapis de course", "Vélos stationnaires", "Haltères et poids", "Bancs de musculation", "Balles", "Nouveau");
        cmbCategory.setOnAction(e -> {
            String selectedCategory = cmbCategory.getSelectionModel().getSelectedItem();
            txtNewCategory.setVisible(selectedCategory != null && selectedCategory.equals("Nouveau"));
        });

        try (Connection connection = new Gestion().connectionBd()) {
            if (connection != null) {
                List<Salle> salles = new Gestion().getListeSalles(connection);
                ObservableList<String> nomsSalles = FXCollections.observableArrayList();
                
                for (Salle salle : salles) {
                    nomsSalles.add(salle.getNomSalle());
                }
                
                cmbSalle.setItems(nomsSalles);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la récupération des salles.");
            e.printStackTrace();
        }
        
        
    }
@FXML private TextField Prix;
    @FXML
private void handleSubmit(ActionEvent event) {
    
    // Vérifier si tous les champs sont remplis
    if (txtName.getText().isEmpty() || Quantite.getText().isEmpty() || Ladate.getValue() == null) {
        showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez remplir tous les champs.");
        return;
    }

    // Vérifier si la quantité est un entier
    int quantity;
    try {
        quantity = Integer.parseInt(Quantite.getText());
    } catch (NumberFormatException e) {
        showAlert(Alert.AlertType.ERROR, "Erreur", "La quantité doit être un nombre entier.");
        return;
    }

    // Vérifier si une salle est sélectionnée
    if (cmbSalle.getSelectionModel().isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez d'abord ajouter une salle.");
        return;
    }
    
    // Obtenez les valeurs des champs
    try{
    double somme = Double.parseDouble(Prix.getText());
    String name = txtName.getText();
    String selectedCategory = cmbCategory.getSelectionModel().getSelectedItem();
    String category;
    if (selectedCategory.equals("Nouveau")) {
        category = txtNewCategory.getText();
    } else {
        category = selectedCategory;
    }
    final String finalCategory = category; // Déclarer comme final ici

    LocalDate selectedDate = Ladate.getValue();
    
    // Créez une boîte de dialogue de confirmation
    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
    confirmation.setTitle("Confirmation");
    confirmation.setHeaderText("Voulez-vous vraiment ajouter cette salle ?");
    confirmation.setContentText("Nom : " + name + "\nQuantite : " + quantity + "\nCatégorie : " + finalCategory + "\nSalle : " + cmbSalle.getValue() + "\nDate : " + selectedDate);
    
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
                    Materiel materiel = new Materiel(0, name, finalCategory, quantity, Salle.getSalleIdByNom(connection, cmbSalle.getValue()),false);
                    new Gestion().ajouterMateriel(connection, materiel);

                    // Obtenir l'ID du dernier matériel inséré
                    int dernierMaterielID = DepensesJ.getDernierMaterielID(connection);

                    // Ajouter la dépense associée avec le dernier MaterielID inséré
                    DepensesJ depense = new DepensesJ(dernierMaterielID, selectedDate.toString(),false,somme);
                    depense.ajouterDepenseJ(connection);
                    
                    showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Matériel ajouté avec succès !");
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
}catch(NumberFormatException e){
    e.printStackTrace();
    showAlert(Alert.AlertType.ERROR, "Erreur", "le prix doit étre un nombre ");
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

    private void clearFields() {
        txtName.clear();
        Quantite.clear();
        cmbCategory.getSelectionModel().clearSelection();
        txtNewCategory.clear();
        cmbSalle.getSelectionModel().clearSelection();
        Ladate.setValue(null);
    }
    //````````````````````````````````````````````````````````````````````````````````````````````````````````````

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

