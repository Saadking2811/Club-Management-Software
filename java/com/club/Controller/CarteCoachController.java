package com.club.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;

public class CarteCoachController {


    @FXML
    private Label nameCoach;

    @FXML
    private Label nameidCoach;

    @FXML
    private ImageView picCoach;

    @FXML
    private Label typeCoach;

    private CarteCoach carte;
    private MyListenerCoach myListenerCoach;


    @FXML
    private void click(MouseEvent mouseEvent){
        myListenerCoach.onClickListener(carte);
    }


    public void setData(CarteCoach carte, MyListenerCoach myListenerCoach) {

        this.carte=carte;
        this.myListenerCoach=myListenerCoach;

        try {
            String picStream = carte.getPicCoach();
if (picStream != null) {
    if (Paths.get(picStream).isAbsolute()) {
        // C'est un chemin absolu
        File imageFile = new File(picStream);
        if (imageFile.exists()) {
            Image image = new Image(imageFile.toURI().toString());
            picCoach.setImage(image); // Définition de l'image sur l'ImageView pic
        } else {
            System.out.println("Le fichier d'image n'existe pas: " + picStream);
        }
    } else {
        System.out.println("Le chemin de l'image n'est pas absolu: " + picStream);
    }
} else {
    // Gérer la ressource manquante
    System.out.println("Ressource de l'image non trouvée: " + carte.getPicCoach());
}



            nameCoach.setText(carte.getNameCoach());
            nameidCoach.setText(carte.getNameidCoach());
            typeCoach.setText(carte.getTypeCoach());
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
        }
    }

    @FXML
    private AnchorPane cardPane2;

    public void highlightBorder(boolean highlight) {
        if (highlight) {
            cardPane2.setStyle("-fx-border-color: blue; -fx-border-width: 2; -fx-border-radius: 25");
        } else {
            cardPane2.setStyle("-fx-border-color: none;");
        }
    }

    public CarteCoach getCarte() {
        return carte;
    }

}
