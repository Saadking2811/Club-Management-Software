package com.club.Controller;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.TimerTask;
import java.util.Timer;


public class DotsController implements Initializable {

    @FXML
    private AnchorPane parent;


    @FXML
    private HBox dotsPane;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Circle dot1;

    @FXML
    private Circle dot2;

    @FXML
    private Circle dot3;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Fade away

        // Show components
        showComponentsForDuration(Duration.seconds(1));

        // Fade out components after they are shown
        fadeOutComponents(Duration.seconds(5));


        // the dots' animation
        SequentialTransition sequentialTransition = new SequentialTransition(
                createTranslateTransition(dot1, 0.0, -30.0),
                createTranslateTransition(dot1, -30.0, 0.0),
                createTranslateTransition(dot2, 0.0, -30.0),
                createTranslateTransition(dot2, -30.0, 0.0),
                createTranslateTransition(dot3, 0.0, -30.0),
                createTranslateTransition(dot3, -30.0, 0.0)
        );
        sequentialTransition.setCycleCount(SequentialTransition.INDEFINITE);
        sequentialTransition.play();
    }

    private TranslateTransition createTranslateTransition(Circle dot, double fromY, double toY) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), dot);
        translateTransition.setFromY(fromY);
        translateTransition.setToY(toY);
        translateTransition.setInterpolator(Interpolator.EASE_BOTH);
        return translateTransition;
    }


    private void showComponentsForDuration(Duration duration) {
        // Set visibility to true after the specified duration
        FadeTransition fadeTransition = new FadeTransition(duration, parent);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    private void fadeOutComponents(Duration duration) {
        // Fade out each component
        fadeOutComponent(welcomeLabel, duration);
        fadeOutComponent(dot1, duration);
        fadeOutComponent(dot2, duration);
        fadeOutComponent(dot3, duration);

        // Create a fade transition for the overall animation
        FadeTransition fadeTransition = new FadeTransition(duration);
        fadeTransition.setOnFinished(event -> {
            // Introduce a delay before calling loadNewFXML()
            PauseTransition pause = new PauseTransition(Duration.seconds(5)); // Adjust the duration as needed
            pause.setOnFinished(e -> {
                Platform.runLater(() -> {
                    // Call the loadNewFXML() method of the actual controller class
                    DotsController.this.loadNewFXML();
                });
            });
            pause.play();
        });
        fadeTransition.play();
    }

    private void fadeOutComponent(javafx.scene.Node component, Duration duration) {
        FadeTransition fadeTransition = new FadeTransition(duration, component);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(event -> component.setVisible(false));
        fadeTransition.play();
    }

    private void loadNewFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("connexion-view.fxml"));
            Parent root = loader.load();
            AnchorPane newPane = loader.getRoot();

            // Add the new FXML content to the parent pane
            parent.getChildren().add(newPane);

            // Set the size of the new FXML content to match the parent pane
            newPane.prefWidthProperty().bind(parent.widthProperty());
            newPane.prefHeightProperty().bind(parent.heightProperty());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



