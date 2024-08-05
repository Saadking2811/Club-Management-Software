package com.club.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.InputStream;

public class CarteGroupeController {

    private CarteGroupe carte;
    private MyListenerGroupe myListenerGroupe;
    @FXML
    private void click(MouseEvent mouseEvent){
        myListenerGroupe.onClickListener(carte);
    }

    @FXML
    private Label gN;

    public void setData(CarteGroupe carte, MyListenerGroupe myListenerGroupe) {
        try {
            this.carte=carte;
            gN.setText(carte.getgN());
            this.myListenerGroupe=myListenerGroupe;

        } catch (Exception e) {
            // Handle any exceptions
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

    public CarteGroupe getCarte() {
        return carte;
    }

}