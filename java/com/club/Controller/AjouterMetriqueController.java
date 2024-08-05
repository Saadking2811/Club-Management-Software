package com.club.Controller;

import com.club.Model.Coach;
import com.club.Model.Gestion;
import com.club.Model.Metrique;
import com.club.Model.Sport;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class AjouterMetriqueController implements Initializable {

    public ComboBox cmbMetSport2;
    public ComboBox cmbMetSport1;
    public ComboBox cmbMetCoach;
    public Button searchButtonOO;
    public TextField searchIDO;
    public TextField searchIDOO;

    @FXML
    private VBox MetriquesBox;

    private Map<String, Integer> sportMap = new HashMap<>();
    private Map<String, Integer> coachMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = new Gestion().connectionBd();

        List<Coach> listCoach = new ArrayList<>();
        List<Sport> listSport = new ArrayList<>();

        List<String> stringCoach = new ArrayList<>();
        List<String> stringSport = new ArrayList<>();
        // Récupérer les coachs
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
        // Récupérer les sports
        listSport = Gestion.getListeSports(connection);
        for (Sport sport : listSport){
            stringSport.add(sport.getSportNom());
        }
        for (Sport sport : listSport){
            // Assuming MyObject has getStringValue() and getIntegerValue() methods
            sportMap.put(sport.getSportNom(),sport.getSportID());
        }


        cmbMetCoach.getItems().addAll(stringCoach);
        cmbMetSport1.getItems().addAll(stringSport);
        cmbMetSport2.getItems().addAll(stringSport);

        AfficherMetriques();

        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private List<MetriqueItem> populateMetriquesListView() {
    Connection connection = new Gestion().connectionBd();
    List<Metrique> listMetriques = new ArrayList<Metrique>();
    try {
        listMetriques = Metrique.recupererToutesLesMetriques(connection);
    } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    List<MetriqueItem> metriqueItems = new ArrayList<>();

    for (Metrique metrique : listMetriques) {
        MetriqueItem metriqueItem = new MetriqueItem();
        
        try {
            metriqueItem = new MetriqueItem(Sport.getSportByID(connection,  metrique.getSportID()).getSportNom(), metrique.getMetriqueNom());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        metriqueItems.add(metriqueItem);
    }

    return metriqueItems;
}

private void AfficherMetriques() {
    List<MetriqueItem> metriqueItems = populateMetriquesListView();
    MetriquesBox.getChildren().clear();
    for (MetriqueItem item : metriqueItems) {
        MetriquesBox.getChildren().add(item);
    }
}

private static class MetriqueItem extends VBox {
    public MetriqueItem(){}
    public MetriqueItem(String selectedSport, String metriqueNom) {
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(5);
        setPadding(new Insets(10));
        setStyle("-fx-background-color: white; -fx-background-radius: 5px; -fx-border-color: #ddd; -fx-border-radius: 5px; -fx-border-width: 1px;");

        Label nameLabel = new Label("Sport : " +selectedSport + "\nMetrique : " + metriqueNom+"\t\t\t\t\t\t\t");
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
    public void handleSubmitEmailCoach(ActionEvent event) {
        if (cmbMetSport2.getSelectionModel().isEmpty() || cmbMetCoach.getSelectionModel().isEmpty() || searchIDOO.getText().isEmpty()) {
            // Show an alert or a message to the user
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez remplir tous les champs.");
            return;
        }
        String selectedSport = (String) cmbMetSport2.getSelectionModel().getSelectedItem();
        String selectedCoach = (String) cmbMetCoach.getSelectionModel().getSelectedItem();
        String lien = searchIDOO.getText();

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Voulez-vous vraiment envoyer cettes metriques ?");
        confirmation.setContentText("Coach : "+ selectedCoach + "\nSport : " + selectedSport);

        Image icon = new Image(getClass().getResourceAsStream("pics/Confirmation.png"));
        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(48);
        imageView.setFitHeight(48);
        confirmation.setGraphic(imageView);

        DialogPane dialogPane = confirmation.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
        dialogPane.getStyleClass().add("my-alert");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Connection connection = new Gestion().connectionBd();
                    if (connection != null) {
                        Coach coach = new Coach();
                        coach = Coach.recupererCoachParID(connection, coachMap.get(selectedCoach));
                        String email = coach.getEmail();
                        String name = coach.getNom() + " " + coach.getPrenom();
                        Gestion.envoyerMail("test","test",email,"test","testC");

                        //System.out.println("Trophe ajoutée : " + name + ", Limite Effectif : " + limitEffectif + ", Catégorie : " + finalCategory);
                        showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Email sent avec succès !");
                        connection.close();

                    } else {
                        showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Impossible de se connecter à la base de données.");
                    }
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'envoie.");
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void AfficherMetriqu(ActionEvent actionEvent) {
        List<MetriqueItem> metriqueItems = populateMetriquesListView();
    
        MetriquesBox.getChildren().clear(); 
        for (MetriqueItem item : metriqueItems) {
            MetriquesBox.getChildren().add(item);
        }
    }
    
    public void handleSubmitAddMetrique(ActionEvent actionEvent) {
        if (cmbMetSport1.getSelectionModel().isEmpty() || searchIDO.getText().isEmpty()) {
            // Show an alert or a message to the user
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez remplir tous les champs.");
            return;
        }
        String selectedSport = (String) cmbMetSport1.getSelectionModel().getSelectedItem();
        String metriqueNom = searchIDO.getText();

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Voulez-vous vraiment ajouter cette metrique ?");
        confirmation.setContentText("Sport : "+ selectedSport + "\nMetrique : " + metriqueNom);

        Image icon = new Image(getClass().getResourceAsStream("pics/Confirmation.png"));
        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(48);
        imageView.setFitHeight(48);
        confirmation.setGraphic(imageView);

        DialogPane dialogPane = confirmation.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
        dialogPane.getStyleClass().add("my-alert");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Connection connection = new Gestion().connectionBd();
                    if (connection != null) {

                        Metrique metrique = new Metrique();
                        metrique.setMetriqueNom(metriqueNom);
                        metrique.setSportID(sportMap.get(selectedSport));
                        metrique.setSupprime(false);
                        metrique.insererMetrique(connection);

                        showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Metrique ajoutée avec succès !");
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

        // Charger l'icône en dehors des conditions
        Image icon = null;
        if (type == Alert.AlertType.ERROR) {
            icon = new Image(getClass().getResourceAsStream("pics/Error.png"));
        } else if (type == Alert.AlertType.WARNING) {
            icon = new Image(getClass().getResourceAsStream("pics/Warning (2).png"));
        } else if (type == Alert.AlertType.CONFIRMATION) {
            icon = new Image(getClass().getResourceAsStream("pics/Confirmation.png"));
        } else if (type == Alert.AlertType.INFORMATION) {
            icon = new Image(getClass().getResourceAsStream("pics/info.png"));
        }

        if (icon != null) {
            ImageView imageView = new ImageView(icon);
            imageView.setFitWidth(48); // Définissez la largeur de l'icône
            imageView.setFitHeight(48); // Définissez la hauteur de l'icône
            alert.setGraphic(imageView);
        }

        // Appliquer du style CSS en ligne
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
        dialogPane.getStyleClass().add("my-alert");

        alert.showAndWait();
    }



    //*****************************************************

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

    public void switchToScene3b1(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("ajouterMetriques.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


}
