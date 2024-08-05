package com.club.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.io.InputStream;

public class CarteOffreController {

    @FXML
    private Label OffreActPrem;

    @FXML
    private Label OffrePrixPrem;

    @FXML
    private ImageView picOffre;

    @FXML
    private Text type;



    private CarteOffre carte;
    private MyListenerOffre myListenerOffre;


    @FXML
    private void click(MouseEvent mouseEvent){
        myListenerOffre.onClickListener(carte);
    }


    public void setData(CarteOffre carte,MyListenerOffre myListenerOffre) {

        this.carte=carte;
        this.myListenerOffre=myListenerOffre;

        try {
            InputStream cartaStream = getClass().getResourceAsStream(carte.getPicOffre());
            if (cartaStream != null) {
                Image cartaImage = new Image(cartaStream);
                picOffre.setImage(cartaImage);
            }



            OffreActPrem.setText(carte.getOffreActPrem());
            OffrePrixPrem.setText(carte.getOffrePrixPrem());
            type.setText(carte.getType());
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
        }
    }


    @FXML
    private AnchorPane cardPane5;

    public void highlightBorder(boolean highlight) {
        if (highlight) {
            cardPane5.setStyle("-fx-border-color: #F4BF4F; -fx-border-width: 2; -fx-effect: dropshadow(gaussian, #f4bf4f, 10, 0.5, 0, 0);");
        } else {
            cardPane5.setStyle("-fx-border-color: none;");
        }
    }

    public CarteOffre getCarte() {
        return carte;
    }



}
