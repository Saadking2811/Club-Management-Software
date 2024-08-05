package com.club.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.InputStream;

public class CarteEvaluationController {

    @FXML
    private Label nomMetrique;

    @FXML
    private Label noteMetrique;

    public void setData(CarteEvaluation carte, MyListenerEvaluation myListenerEvaluation) {

        this.carte=carte;
        this.myListenerEvaluation=myListenerEvaluation;

        try {

            nomMetrique.setText(carte.getNomMetrique());
            noteMetrique.setText(carte.getNoteMetrique());
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
        }
    }

    private CarteEvaluation carte;
    @FXML
    private AnchorPane cardPanee;

    private MyListenerEvaluation myListenerEvaluation;


    @FXML
    private void click(MouseEvent mouseEvent){
        myListenerEvaluation.onClickListener(carte);
    }

    public void highlightBorder(boolean highlight) {
        if (highlight) {
            cardPanee.setStyle("-fx-border-color: #da722c; -fx-border-width: 2; -fx-border-radius: 25");
        } else {
            cardPanee.setStyle("-fx-border-color: none;");
        }
    }

    public CarteEvaluation getCarte() {
        return carte;
    }

}
