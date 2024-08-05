package com.club.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;

public class CarteAdherentController {

    @FXML
    private ImageView carta;

    @FXML
    private Label namee;

    @FXML
    private Label nameid;

    @FXML
    private ImageView pic;

    @FXML
    private Label typee;
    
    private CarteAdherent carte;
    private MyListenerAdherent myListenerAdherent;

    @FXML
    private void click(MouseEvent mouseEvent){
        myListenerAdherent.onClickListener(carte);
    }

    // Ajout de setData pour initialiser cardPane
    public void setData(CarteAdherent carte, MyListenerAdherent myListenerAdherent) {
        this.carte = carte;
        this.myListenerAdherent = myListenerAdherent;
    
        try {
            
            // Chargement de l'image à partir du chemin d'accès spécifié dans la carte
            // Vérifie si le chemin est absolu
String picStream = carte.getPic();
if (picStream != null) {
    if (Paths.get(picStream).isAbsolute()) {
        // C'est un chemin absolu
        File imageFile = new File(picStream);
        if (imageFile.exists()) {
            Image image = new Image(imageFile.toURI().toString());
            pic.setImage(image); // Définition de l'image sur l'ImageView pic
        } else {
            System.out.println("Le fichier d'image n'existe pas: " + picStream);
        }
    } else {
        System.out.println("Le chemin de l'image n'est pas absolu: " + picStream);
    }
} else {
    // Gérer la ressource manquante
    System.out.println("Ressource de l'image non trouvée: " + carte.getPic());
}
    
            // Définition des autres données de la carte
            namee.setText(carte.getNamee());
            nameid.setText(carte.getNameid());
            typee.setText(carte.getTypee());
        } catch (Exception e) {
            // Gérer les exceptions
            e.printStackTrace();
        }
    }
    
    @FXML
    private AnchorPane cardPane1;

    public void highlightBorder(boolean highlight) {
        if (highlight) {
            cardPane1.setStyle("-fx-effect: dropshadow(gaussian, #f4bf4f, 10, 0.5, 0, 0);");
        } else {
            cardPane1.setStyle("-fx-border-color: none;");
        }
    }

    public CarteAdherent getCarte() {
        return carte;
    }



    
}
