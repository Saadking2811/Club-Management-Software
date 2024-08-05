package com.club.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.InputStream;

public class CarteTropheController {
    @FXML
    private Label nameTrophe;
    @FXML
    private Label nameCompetition;
    @FXML
    private Label nameCoach;
    @FXML
    private Label nameSport;
    @FXML
    private Label nameGagnant;

    @FXML
    private ImageView picTrophe;

    private CarteTrophe carte;
    private MyListenerTrophe myListenerTrophe;

    @FXML
    private void click(MouseEvent mouseEvent){
        myListenerTrophe.onClickListener(carte);
    }

    public void setData(CarteTrophe carte, MyListenerTrophe myListenerTrophe) {

        this.carte=carte;
        this.myListenerTrophe=myListenerTrophe;

        try {
            InputStream cartaStream = getClass().getResourceAsStream(carte.getPicTrophe());
            if (cartaStream != null) {
                Image cartaImage = new Image(cartaStream);
                picTrophe.setImage(cartaImage);
            }

            nameTrophe.setText(carte.getNameTrophe());
            nameCompetition.setText(carte.getNameCompetition());
            //nameCoach.setText(carte.getNameCoach());
            //nameSport.setText(carte.getNameSport());
            nameGagnant.setText(carte.getNameGagnant());

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

    public CarteTrophe getCarte() {
        return carte;
    }
}
