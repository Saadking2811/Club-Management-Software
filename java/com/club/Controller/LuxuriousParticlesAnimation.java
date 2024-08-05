package com.club.Controller;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.layout.AnchorPane;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LuxuriousParticlesAnimation {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int NUM_PARTICLES = 50;
    private List<Particle> particles = new ArrayList<>();

    public void startAnimation(AnchorPane anchorPane) {
        // Création du groupe racine
        Group root = new Group();

        

        // Création des particules lumineuses et ajout à la scène
        for (int i = 0; i < NUM_PARTICLES; i++) {
            Particle particle = new Particle();
            root.getChildren().add(particle);
            particles.add(particle);
        }

        // Animation des particules
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                for (Particle particle : particles) {
                    particle.move();
                }
            }
        };
        timer.start();

        // Affichage de la scène sur le AnchorPane
        anchorPane.getChildren().add(root);
    }

    // Classe interne pour représenter une particule lumineuse
    private static class Particle extends Circle {
        private static final Random random = new Random();
        private double speedX;
        private double speedY;

        public Particle() {
            super(2, Color.rgb(255, 255, 255, 0.8)); // Couleur blanche avec opacité réduite (80%)
            setBlendMode(BlendMode.ADD); // Mode de fusion pour un effet de lumière
            setTranslateX(random.nextDouble() * WIDTH);
            setTranslateY(random.nextDouble() * HEIGHT);
            speedX = random.nextDouble() * 2 - 1;
            speedY = random.nextDouble() * 2 - 1;
        }

        public void move() {
            setTranslateX(getTranslateX() + speedX);
            setTranslateY(getTranslateY() + speedY);
            if (getTranslateX() < 0 || getTranslateX() > WIDTH) {
                speedX *= -1;
            }
            if (getTranslateY() < 0 || getTranslateY() > HEIGHT) {
                speedY *= -1;
            }
        }
    }
}
