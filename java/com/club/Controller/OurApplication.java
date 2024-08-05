package com.club.Controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.io.File;
import javafx.application.Application;
import java.util.Objects;

import com.club.Model.Gestion;

public class OurApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        // Charger l'icône à partir des ressources


        Connection conn = new Gestion().connectionBd();
        Gestion.ajouterSeance_Groupe_Creneaux_Absences(conn);
        Gestion.initlogo(conn);
        Image image;
        if (Gestion.logoPath!=null) {
            File file = new File(Gestion.logoPath);
            image = new Image(file.toURI().toString());
            
        }
        else {
            File imageFile = new File("./src/main/resources/com/club/Controller/pics/icon.png");
            image = new Image(imageFile.toURI().toString());
        }
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("first.fxml")));
        Scene scene = new Scene(root, 1024, 760);
        scene.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());

        

        // Définir l'icône de l'application
        stage.getIcons().add(image);
        stage.setResizable(false);
        stage.setTitle("Sport Club Management System");
        stage.setScene(scene);
        stage.show();
       
    }

    public static void main(String[] args) {
        launch();
    }
}
