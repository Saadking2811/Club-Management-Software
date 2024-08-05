package com.club.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.InputStream;

import com.club.Model.Evenement;

public class CarteEvenementController {

    @FXML
    private AnchorPane cardPane7;

    @FXML
    private Label date;

    @FXML
    private Label event;

    @FXML
    private Label sport;


    private Evenement carte;
    private AnchorPane cardPane; // Ajout de cardPane



    // Ajout de setData pour initialiser cardPane
    public void setData(Evenement carte) {

        this.carte=carte;


        // Initialisation de cardPane
        cardPane = new AnchorPane(); // Initialisation basique, vous pouvez ajuster selon vos besoins

        try {


            sport.setText(carte.getNomSport());
            event.setText(carte.getNomEvenement());
            date.setText(carte.getDateEvenement());
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
        }
    }

    @FXML
    private AnchorPane cardPane8;

    public void highlightBorder(boolean highlight) {
        if (highlight) {
            cardPane8.setStyle("-fx-effect: dropshadow(gaussian, #f4bf4f, 10, 0.5, 0, 0);");
        } else {
            cardPane8.setStyle("-fx-border-color: none;");
        }
    }

    public Evenement getCarte() {
        return carte;
    }



}
