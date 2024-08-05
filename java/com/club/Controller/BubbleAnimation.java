package com.club.Controller;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.scene.layout.AnchorPane;

public class BubbleAnimation {

    private static final int WIDTH = 377;
    private static final int HEIGHT = 760;
    private static final int NUM_BUBBLES = 20;
    private List<Bubble> bubbles = new ArrayList<>();

    public void startAnimation(AnchorPane anchorPane) {
        // Création du groupe racine
        Group root = new Group();


        // Création de la scène
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // Création des bulles et ajout à la scène
        for (int i = 0; i < NUM_BUBBLES; i++) {
            Bubble bubble = new Bubble();
            root.getChildren().add(bubble);
            bubbles.add(bubble);
        }

        // Animation des bulles
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (Bubble bubble : bubbles) {
                    bubble.move();
                }
            }
        };
        timer.start();

        // Interaction de la souris avec les bulles
        scene.setOnMouseMoved(event -> {
            for (Bubble bubble : bubbles) {
                if (bubble.isHover(event.getX(), event.getY())) {
                    bubble.setFill(Color.CYAN); // Change la couleur de la bulle lorsque la souris survole
                } else {
                    bubble.setFill(Color.BLUE); // Réinitialise la couleur si la souris ne survole pas
                }
            }
        });


        // Affichage de la scène sur le AnchorPane
        anchorPane.getChildren().add(root);
    }

    // Classe interne pour représenter une bulle
    private static class Bubble extends Circle {
        private static final Random random = new Random();
        private double speedX;
        private double speedY;

        public Bubble() {
            super(random.nextInt(WIDTH), random.nextInt(HEIGHT), random.nextInt(20) + 0);
            this.setFill(Color.BLUE);
            speedX = random.nextDouble(); // Accélère les bulles
            speedY = random.nextDouble(); // Accélère les bulles
        }

        public void move() {
            setCenterX(getCenterX() + speedX);
            setCenterY(getCenterY() + speedY);
            if (getCenterX() < 0 || getCenterX() > WIDTH) {
                speedX *= -1;
            }
            if (getCenterY() < 0 || getCenterY() > HEIGHT) {
                speedY *= -1;
            }
        }

        public boolean isHover(double x, double y) {
            return Math.sqrt(Math.pow(x - getCenterX(), 2) + Math.pow(y - getCenterY(), 2)) < getRadius();
        }
    }
}