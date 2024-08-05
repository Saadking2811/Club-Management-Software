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


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.club.Model.Adherent;
import com.club.Model.EvaluationAdherent;
import com.club.Model.EvaluationGroupe;
import com.club.Model.Gestion;
import com.club.Model.Groupe;
import com.club.Model.Metrique;
import com.club.Model.Sport;

public class EvaluerGroupeController {

    //Evaluer adhérents

    @FXML
    private VBox vBoxContainer1;

    @FXML
    private ComboBox<String> cmbGroupe;

    @FXML
    private ComboBox<String> cmbSport;

    @FXML
    private void initialize() {
        // Connexion à la base de données (à remplacer par votre propre méthode de connexion)
        try {
            Connection connection = new Gestion().connectionBd();
        if (connection != null) {
                List<Groupe> groupes = new Gestion().getListeGroupes(connection);
                ObservableList<String> nomsGroupes = FXCollections.observableArrayList();

                for (Groupe groupe : groupes) {
                      nomsGroupes.add(groupe.getNomGroupe());
                }

                // Tri de la liste des noms de groupes par ordre alphabétique
                FXCollections.sort(nomsGroupes);

                cmbGroupe.setItems(nomsGroupes);

                List<Sport> sports = new Gestion().getListeSports(connection);
                sports.sort(Comparator.comparing(Sport::getSportNom));
                ObservableList<String> nomsSports = FXCollections.observableArrayList();

                for (Sport sport : sports) {
                    nomsSports.add(sport.getSportNom());
                }

                cmbSport.setItems(nomsSports);
                
                
            
            connection.close();
        }
    }catch (SQLException e) {
        showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la récupération des donnees.");
        e.printStackTrace();
    }
}


@FXML
    private void handleSubmit1(ActionEvent event) {

        // Vérifier si cmbSport est vide
        if (cmbSport.getSelectionModel().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez d'abord selectionner un sport.");
            return; // Arrêter le traitement si la combobox est vide
        }

        vBoxContainer1.getChildren().clear();
        try {
            Connection connection = new Gestion().connectionBd();
        if (connection != null) {
            try {
                // Récupérer toutes les métriques depuis la base de données
                List<String> metriquesList = Metrique.recupererMetriquesSport(connection, new Gestion().getSportIDByNom(connection, cmbSport.getValue()));

                // Définir la largeur maximale que peuvent prendre les labels pour uniformiser
                int labelWidth = 150; // Vous pouvez ajuster cette valeur selon vos besoins

                // Parcourir la liste des métriques et les afficher
                for (String metrique : metriquesList) {
                    HBox hBox = new HBox(10); // Espace de 10px entre les éléments
                    Label label = new Label(metrique);
                    label.setMinWidth(labelWidth); // Tous les labels auront la même largeur
                    label.setMaxWidth(labelWidth);

                    TextField textField = new TextField();
                    textField.setPromptText("Notez de 1 à 5");

                    hBox.getChildren().addAll(label, textField);
                    vBoxContainer1.getChildren().add(hBox);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Gérer les erreurs SQL
            }
            connection.close();
        }
    }catch (SQLException e) {
        showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la récupération des donnees.");
        e.printStackTrace();
    }


    }

    @FXML
private void sauvegarderValeurs(ActionEvent event) {
    try {
        Connection connection = new Gestion().connectionBd();
        if (connection != null) {
            try {
                // Parcourir les enfants du VBox
                for (Node node : vBoxContainer1.getChildren()) {
                    if (node instanceof HBox) {
                        HBox hBox = (HBox) node;

                        // Récupérer le nom de la métrique depuis le premier enfant (Label)
                        String metriqueNom = "";
                        for (Node childNode : hBox.getChildren()) {
                            if (childNode instanceof Label) {
                                Label label = (Label) childNode;
                                metriqueNom = label.getText();
                                break; // Sortir de la boucle dès que le nom de la métrique est récupéré
                            }
                        }

                        // Récupérer la valeur saisie dans le TextField associé
                        String valeur = "";
                        for (Node childNode : hBox.getChildren()) {
                            if (childNode instanceof TextField) {
                                TextField textField = (TextField) childNode;
                                valeur = textField.getText();
                                break; // Sortir de la boucle dès que la valeur est récupérée
                            }
                        }

                        // Sauvegarder la valeur dans la base de données des ValeursMetriques
                        if (!metriqueNom.isEmpty() && !valeur.isEmpty()) {
                            
                        // Récupérer l'ID du groupe sélectionné dans la combobox
                                int GroupeID = Groupe.getGroupeIDByNom(connection, cmbGroupe.getValue()); // Remplacer par la logique pour obtenir l'ID de l'adhérent

                                // Insérer une entrée dans la table EvaluationAdherent
                                EvaluationGroupe evaluationGroupe = new EvaluationGroupe(Integer.parseInt(valeur),Metrique.getMetriqueIDParNom(connection, metriqueNom),GroupeID);
                                evaluationGroupe.insererEvaluationGroupe(connection);
                        }
                    }
                }
                showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Metriques ajouté avec succès !");
                clearFields();
            } catch (SQLException e) {
                e.printStackTrace();
                // Gérer les erreurs SQL
            }
            connection.close();
        }
    } catch (SQLException e) {
        showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la connexion à la base de données.");
        e.printStackTrace();
    }
}

private void clearFields() {
    cmbGroupe.getSelectionModel().clearSelection();
    cmbSport.getSelectionModel().clearSelection();
    vBoxContainer1.getChildren().clear();
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

    public void switchToScene8a(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("evaluation.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


}
