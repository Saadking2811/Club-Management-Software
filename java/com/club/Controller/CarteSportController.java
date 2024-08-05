package com.club.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.InputStream;

public class CarteSportController {

    @FXML
    private Label nameSport;

    @FXML
    private Label nameidSport;

    @FXML
    private ImageView picSport;

    private CarteSport carte;
    private MyListenerSport myListenerSport;

    @FXML
    private void click(MouseEvent mouseEvent){
        myListenerSport.onClickListener(carte);
    }

    public void setData(CarteSport carte, MyListenerSport myListenerSport) {

        this.carte=carte;
        this.myListenerSport=myListenerSport;

        try {
            InputStream cartaStream = getClass().getResourceAsStream(carte.getPicSport());
            if (cartaStream != null) {
                Image cartaImage = new Image(cartaStream);
                picSport.setImage(cartaImage);
            }



            nameSport.setText(carte.getNameSport());
            nameidSport.setText(carte.getCateg());
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
        }
    }


    @FXML
    private AnchorPane cardPane4;

    public void highlightBorder(boolean highlight) {
        if (highlight) {
            cardPane4.setStyle("-fx-border-color: #afdbf5; -fx-border-width: 2; -fx-border-radius: 25;-fx-effect: dropshadow(gaussian, #afdbf5, 10, 0.5, 0, 0);");
        } else {
            cardPane4.setStyle("-fx-border-color: none;");
        }
    }

    public CarteSport getCarte() {
        return carte;
    }



}

