package com.club.Controller;

import com.club.Model.EntrantSortant;
import com.club.Model.Gestion;
import com.club.Model.Salle;
import com.club.Model.Utilisateurs;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EntrantSortantController implements Initializable {

    @FXML
    private ScrollPane rootPane;

    @FXML
    private VBox entrantsBox;
    @FXML
    private VBox sortantsBox;

    @FXML
    private TextField searchField;


    private Connection connection;
    private List<EntrantSortant> allEntrants;
    private List<EntrantSortant> allSortants;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        connection = new Gestion().connectionBd();
        if (connection != null) {
            afficherEntrantsSortants();
        } else {
            System.err.println("La connexion à la base de données est null.");
        }

        // Ajouter un écouteur de changement de texte au champ de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                // Recharger la page lorsque la zone de recherche est vide
                afficherEntrantsSortants();
            } else {
                // Filtrer et afficher les salles en fonction du texte de recherche
                filtrerEtAfficherEntrantsSortants(newValue);
            }
        });
    }

    private void afficherEntrantsSortants() {
        LocalDate today = LocalDate.now();
        //EntrantSortant entrantSortant = new EntrantSortant(today.atStartOfDay(),1,false);
        //allEntrants.add(entrantSortant);
        allEntrants = EntrantSortant.getListEntrantSortantbyDay(connection, String.valueOf(today),0);
        allSortants = EntrantSortant.getListEntrantSortantbyDay(connection,String.valueOf(today),1);
        filtrerEtAfficherEntrantsSortants("");
    }

    private void filtrerEtAfficherEntrantsSortants(String searchText) {
        entrantsBox.getChildren().clear(); // Efface les anciennes données
        sortantsBox.getChildren().clear();

        List<EntrantSortant> EntrantsFiltrees = allEntrants.stream()
                .filter(entrantSortant -> getnamefilter(entrantSortant).toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());

        List<EntrantSortant> SortantsFiltrees = allSortants.stream()
                .filter(entrantSortant -> getnamefilter(entrantSortant).toLowerCase().contains(searchText.toLowerCase()))
                .collect(Collectors.toList());


        for (EntrantSortant entrantSortant : EntrantsFiltrees) {
            EntrantSortantItem entrantItem = new EntrantSortantItem(entrantSortant);
            entrantsBox.getChildren().add(entrantItem);
        }
        for (EntrantSortant entrantSortant : SortantsFiltrees) {
            EntrantSortantItem sortantItem = new EntrantSortantItem(entrantSortant);
            sortantsBox.getChildren().add(sortantItem);
        }

    }

    public static String getnamefilter(EntrantSortant entrantSortant) {
        Connection connection = new Gestion().connectionBd();
        Utilisateurs utilisateurs = new Utilisateurs();
        try {
            utilisateurs = Utilisateurs.recupererUtilisateurParID(connection,entrantSortant.getMembreID());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return utilisateurs.getNom() + ' ' + utilisateurs.getPrenom();
    }

    private static class EntrantSortantItem extends VBox {
        public EntrantSortantItem(EntrantSortant entrantSortant) {
            setAlignment(Pos.CENTER_LEFT);
            setSpacing(5);
            setPadding(new Insets(10));
            setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5px; -fx-border-color: #ddd; -fx-border-radius: 5px; -fx-border-width: 1px;");

            Connection connection = new Gestion().connectionBd();
            Utilisateurs utilisateurs = new Utilisateurs();
            try {
                utilisateurs = Utilisateurs.recupererUtilisateurParID(connection,entrantSortant.getMembreID());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Label nameLabel = new Label("Nom: " + utilisateurs.getNom());
            Label categoryLabel = new Label("Prenom: " + utilisateurs.getPrenom());
            Timestamp timestamp = Timestamp.valueOf(entrantSortant.getTime());
            LocalDateTime localDateTime = timestamp.toLocalDateTime();
            int hour = localDateTime.getHour();
            int minute = localDateTime.getMinute();
            Label limiteLabel = new Label("Temps: " + hour + ":" + minute);

            setOnMouseEntered(event -> {
                setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            });

            setOnMouseExited(event -> {
                setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            });

            getChildren().addAll(nameLabel, categoryLabel, limiteLabel);
        }
    }

    @FXML
    private void rechercherEntrantSortant() {
        String searchText = searchField.getText();
        filtrerEtAfficherEntrantsSortants(searchText);
    }

    //````````````````````````````````````````````````````````````````````````````````````````````````````````````


    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToScene5(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("presences.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
