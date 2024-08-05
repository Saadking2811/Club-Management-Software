package com.club.Controller;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import java.sql.*;
import javafx.scene.control.ButtonType;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

import com.club.Model.Evenement;
import com.club.Model.Gestion;
import com.jfoenix.controls.JFXToggleButton;


  

 

public class MailController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML private TextField mailchange;
    @FXML  private PasswordField mailpass;
    @FXML private Button sauvemail;
    public void switchtoajoutermail1(ActionEvent event){
         Alert confirmation = new Alert(AlertType.CONFIRMATION);
     confirmation.setTitle("Confirmation");
     confirmation.setHeaderText("Voulez-vous vraiment changer vers l'adresse mail");
     confirmation.setContentText("mail: " + mailchange.getText());
      Gestion gestion = new Gestion();
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
     confirmation.showAndWait().ifPresent(response -> {
         if (response == ButtonType.OK) {
        Gestion gest = new Gestion();
        Connection connect = gest.connectionBd();
        gest.supprimerMail(connect);
        gest.ajouterMail(connect, mailchange.getText(), mailpass.getText());}});
    }   
    public void switchbackmain(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    
    }
}

