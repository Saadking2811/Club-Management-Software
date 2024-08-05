package com.club.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.InputStream;

public class CarteAdminController {

    @FXML
    private Label nameAdmin;

    @FXML
    private Label nameidAdmin;

    @FXML
    private ImageView picAdmin;

    @FXML
    private Label typeAdmin;


    private CarteAdmin carte;
    private MyListenerAdmin myListenerAdmin;


    @FXML
    private void click(MouseEvent mouseEvent){
        myListenerAdmin.onClickListener(carte);
    }


    public void setData(CarteAdmin carte,MyListenerAdmin myListenerAdmin) {

        this.carte=carte;
        this.myListenerAdmin=myListenerAdmin;

        try {
            InputStream cartaStream = getClass().getResourceAsStream(carte.getPicAdmin());
            if (cartaStream != null) {
                Image cartaImage = new Image(cartaStream);
                picAdmin.setImage(cartaImage);
            }



            nameAdmin.setText(carte.getNameAdmin());
            nameidAdmin.setText(carte.getNameidAdmin());
            typeAdmin.setText(carte.getTypeAdmin());
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
        }
    }


    @FXML
    private AnchorPane cardPane3;

    public void highlightBorder(boolean highlight) {
        if (highlight) {
            cardPane3.setStyle("-fx-border-color: #afdbf5; -fx-border-width: 2; -fx-border-radius: 25");
        } else {
            cardPane3.setStyle("-fx-border-color: none;");
        }
    }

    public CarteAdmin getCarte() {
        return carte;
    }


}
