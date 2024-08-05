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
import com.club.Model.RevenuesJ;
import com.club.Model.Salle;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.scene.shape.Rectangle;


public class ChargesVoirController implements Initializable {
    
    @FXML
    private BorderPane rootPane;
    
    @FXML
    private DatePicker date1;
    
    @FXML
    private Button buttonRev;

    @FXML
    private Button buttonDep;


    @FXML
    private DatePicker date2;

    @FXML
    private VBox depensesBox;

    @FXML
    private VBox revenuesBox;


    private Connection connection;
    private List<DepensesJ> allDepenses;
    private List<RevenuesJ> allRevenuesJs;

    public ChargesVoirController() {
        connection = new Gestion().connectionBd();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (connection != null) {
            afficherRevenues();
            afficherDepenses();
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La connexion à la base de données a échoué.");
        }


        
        applyHoverAnimation(buttonRev);
        applyHoverAnimation(buttonDep);
        applyHoverAnimation(date1);
        applyHoverAnimation(date2);
        applyHoverAnimation(emailField); // Ajout de l'animation pour le champ de texte

    }

    private void afficherDepenses() {
        allDepenses = DepensesJ.getListeRevenuesJ(connection);
        afficherDepensesDansUI();
    }


    private void afficherDepensesDansUI() {
        depensesBox.getChildren().clear(); // Efface les anciennes données


        for (DepensesJ depensesJ : allDepenses) {
            DepenseItem depenseItem = new DepenseItem(depensesJ); 
            depensesBox.getChildren().add(depenseItem);
        }
    }

    private class DepenseItem extends VBox {
        public DepenseItem(DepensesJ depensesJ) {
            setAlignment(Pos.CENTER_LEFT);
            setSpacing(5);
            setPadding(new Insets(10));
            setStyle("-fx-background-color: WHITE; -fx-background-radius: 5px; -fx-border-color: #ddd; -fx-border-radius: 5px; -fx-border-width: 1px;");

            Label nameLabel = new Label("Date: " + depensesJ.getTemps()+ "\nSomme:"+depensesJ.getSomme()+ " DA" );

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


    private void afficherRevenues() {
        allRevenuesJs = RevenuesJ.getListeRevenuesJ(connection);
        afficherRevenuesDansUI();
    }


    private void afficherRevenuesDansUI() {
        revenuesBox.getChildren().clear(); // Efface les anciennes données


        for (RevenuesJ revenuesJ : allRevenuesJs) {
            RevenueItem revenueItem = new RevenueItem(revenuesJ); 
            revenuesBox.getChildren().add(revenueItem);
        }
    }

    private class RevenueItem extends VBox {
        public RevenueItem(RevenuesJ revenueJ) {
            setAlignment(Pos.CENTER_LEFT);
            setSpacing(5);
            setPadding(new Insets(10));
            setStyle("-fx-background-color: WHITE; -fx-background-radius: 5px; -fx-border-color: #ddd; -fx-border-radius: 5px; -fx-border-width: 1px;");

            Label nameLabel = new Label("Date: " + revenueJ.getTemps()+ "\nSomme:"+revenueJ.getSomme()+ " DA" );

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

    // Méthode pour revenir à la vue précédente
    private Stage stage;
    private Scene scene;
    private Parent root;

    //go to Paiement page
    public void switchToScene6(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("Paiement.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private TextField emailField;

    @FXML
    private Label emailV;



    @FXML
    private void validateEmail() {
        String email = emailField.getText();
        if (!isValidEmail(email)) {
            emailV.setText("Format d'email non valide");
            emailV.setTextFill(javafx.scene.paint.Color.RED);
        } else {
            emailV.setText("");
        }
    }

    private boolean isValidEmail(String email) {
        // Regular expression for basic email validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return email != null && pattern.matcher(email).matches();
    }


    // Méthode pour appliquer une animation de survol à un élément JavaFX
    private void applyHoverAnimation(Button button) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);

        button.setOnMouseEntered(event -> scaleTransition.play());
        button.setOnMouseExited(event -> {
            scaleTransition.setRate(-1);
            scaleTransition.play();
        });
    }

    // Méthode pour appliquer une animation de survol à un DatePicker
    private void applyHoverAnimation(DatePicker datePicker) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), datePicker);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);

        datePicker.setOnMouseEntered(event -> scaleTransition.play());
        datePicker.setOnMouseExited(event -> {
            scaleTransition.setRate(-1);
            scaleTransition.play();
        });
    }

    // Méthode pour appliquer une animation de survol à un élément JavaFX
private void applyHoverAnimation(TextField textField) {
    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), textField);
    scaleTransition.setToX(1.05);
    scaleTransition.setToY(1.05);

    textField.setOnMouseEntered(event -> scaleTransition.play());
    textField.setOnMouseExited(event -> {
        scaleTransition.setRate(-1);
        scaleTransition.play();
    });
}
public void envoiRapportRevenues(ActionEvent event){
    Gestion gest = new Gestion();
LocalDate Date1 = date1.getValue();
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
String date1String = Date1.format(formatter);
LocalDate Date2 = date2.getValue();
String date2String = Date2.format(formatter);
    String mailgérant = emailField.getText();

    gest.envoyerRapportRevenues(mailgérant, date1String, date2String);
}
public void envoiRapportDepenses(ActionEvent event){
    Gestion gest = new Gestion();
LocalDate Date1 = date1.getValue();
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
String date1String = Date1.format(formatter);
LocalDate Date2 = date2.getValue();
String date2String = Date2.format(formatter);
    String mailgérant = emailField.getText();

    gest.envoyerRapportRevenues(mailgérant, date1String, date2String);
}


}
