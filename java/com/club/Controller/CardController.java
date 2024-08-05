package com.club.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class CardController {

    @FXML
    private Label nom;

    @FXML
    private Label prenom;

    @FXML
    private Label age;

    @FXML
    private Label categorie;

    @FXML
    private Label genre;

    @FXML
    private ImageView photo;

    private Card card;
    private MyListener myListener;

    @FXML
    private void click(MouseEvent mouseEvent) {
        myListener.onClickListener(card);
    }

    public void setData(Card card, MyListener myListener) {
        this.card = card;
        this.myListener = myListener;
        nom.setText(card.getName());
        prenom.setText(card.getSurname());
        age.setText(String.valueOf(card.getAge())); // Convertir int en String
        categorie.setText(card.getCategorie());
        genre.setText(card.getGenre());
        // Utiliser getResourceAsStream pour charger l'image à partir des ressources
        Image image = new Image(getClass().getResourceAsStream(card.getImgSrc()));
        photo.setImage(image);
    }
}
