package com.club.Controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.File;
import javafx.application.Application;
import java.util.Objects;


import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Comparator;
import java.util.Collections;

import com.club.Model.Adherent;
import com.club.Model.Coach;
import com.club.Model.Gestion;
import com.club.Model.MembreOffre;
import com.club.Model.Offre;
import com.club.Model.Sport;
import com.club.Model.RevenuesJ;
import com.club.Model.Utilisateurs;


public class AbonnementController {

    @FXML
    private ScrollPane rootPane;

    @FXML
    private VBox AbonnementBox;
    

    private Connection connection;

    @FXML
    private ComboBox<String> cmbAdherent;

    @FXML
    private ComboBox<String> cmboffre;

    @FXML
    private Text AdhText;

    @FXML
    private void initialize() {
        // Connexion à la base de données (à remplacer par votre propre méthode de connexion)
        try {
            Connection connection = new Gestion().connectionBd();
           
        if (connection != null) {
            try {
                List<Adherent> adherents = new Gestion().recupererTousLesAdherents(connection);
                adherents.sort(Comparator.comparing(Adherent::getNom).thenComparing(Adherent::getPrenom));
                ObservableList<String> nomsAdh = FXCollections.observableArrayList();
                
                for (Adherent adh : adherents) {
                    nomsAdh.add(adh.getNom() + "\t" + adh.getPrenom());
                }
                
                cmbAdherent.setItems(nomsAdh);

                List<Offre> offres = new Gestion().getListeOffres(connection);
                offres.sort(Comparator.comparing(Offre::getOffreNom));
                ObservableList<String> nomsOffres = FXCollections.observableArrayList();

                for (Offre offre : offres) {
                    nomsOffres.add(offre.getOffreNom());
                }

                cmboffre.setItems(nomsOffres);
                
                
            } catch (SQLException e) {
                e.printStackTrace();
                // Gérer les erreurs SQL
            }
            connection.close();
        }
    }catch (SQLException e) {
        showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la récupération des donnees.");
        e.printStackTrace();
    }
}
    @FXML 
    private void afficherabbonements() throws SQLException {

        // Vérifier si cmbAdherent est vide
        if (cmbAdherent.getSelectionModel().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez d'abord selectionner un adherent.");
            return; // Arrêter le traitement si la combobox est vide
        }

        AbonnementBox.getChildren().clear(); // Efface les anciennes données
        connection = new Gestion().connectionBd();
        List<MembreOffre> membreoffres = MembreOffre.getOffresParMembreID(connection, getMembreIDFromNomPrenom(cmbAdherent.getValue()));
    
        AdhText.setText(cmbAdherent.getValue());
        AdhText.setVisible(true);
    
        for (MembreOffre membreoffre : membreoffres) {
            Offre offre = Offre.getOffreByID(connection, membreoffre.getOffreID());
            String sportNom = Sport.getSportByID(connection, offre.getSportID()).getSportNom();
            MembreoffreItem oMembreoffreItem = new MembreoffreItem(membreoffre,offre,sportNom);
            AbonnementBox.getChildren().add(oMembreoffreItem);
        }
    }


    private static class MembreoffreItem extends VBox {
        public MembreoffreItem(MembreOffre membreOffre,Offre offre,String sportname) {
            setAlignment(Pos.CENTER_LEFT);
            setSpacing(5);
            setPadding(new Insets(10));
            setStyle("-fx-background-color: White; -fx-background-radius: 5px; -fx-border-color: #ddd; -fx-border-radius: 5px; -fx-border-width: 1px;");
                Connection connection = new Gestion().connectionBd();
            Label nameLabel = new Label("Nom du offre : " +offre.getOffreNom()+"\nCategorie : "+offre.getCategorie()+"\nSport : " + sportname+"\nDate Debut :" +membreOffre.getTemps()+"\nDate Fin : "+membreOffre.calculerDateFin(membreOffre.getTemps(), offre.getDuree()) + "                                 " );
            
            // Transition de survol (hover)
            setOnMouseEntered(event -> {
                setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
            });

            setOnMouseExited(event -> {
                setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            });

            // Gestionnaire d'événements pour le clic sur la salle
            setOnMouseClicked(event -> {
                // Animation de transition pour changer la couleur de fond
                String colorTransition = "-fx-background-color: #ADD8E6;"; // Bleu clair
                String originalColor = "-fx-background-color: WHITE;"; // Couleur d'origine

                // Appliquer la transition de couleur en utilisant du CSS
                setStyle(colorTransition);

                // Afficher une alerte de confirmation pour la suppression de la salle (à adapter selon votre logique)
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation de suppression");
                alert.setHeaderText("Êtes-vous sûr de vouloir annuler cet abonnement ?");
                alert.setContentText("Nom du offre : " +offre.getOffreNom()+"\nCategorie : "+offre.getCategorie()+"\nSport : " + sportname+"\nDate Debut :" +membreOffre.getTemps()+"\nDate Fin : "+membreOffre.calculerDateFin(membreOffre.getTemps(), offre.getDuree())  );
            

                // Charger l'icône
                // Charger l'icône
            File imageFile = new File("./src/main/resources/com/club/Controller/pics/Confirmation.png");
        
            Image image = new Image(imageFile.toURI().toString());
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitWidth(48);
            imageView.setFitHeight(48);

                alert.setGraphic(imageView);        

                // Appliquer du style CSS
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
                dialogPane.getStyleClass().add("my-alert");        

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try  {
                        // Supprimer le matériel de la base de données
                        membreOffre.supprimerMembreOffre(connection);
    
                         double somme = Offre.getOffreByID(connection, membreOffre.getOffreID()).getPrix();
                              RevenuesJ rv = new RevenuesJ(membreOffre.getMembreID(),"cancel",somme);
                             rv.supprimerRevenuJ(connection);
                        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), this);
                        fadeOut.setFromValue(1);
                        fadeOut.setToValue(0);

                        TranslateTransition translate = new TranslateTransition(Duration.seconds(0.5), this);
                        translate.setByY(-this.getHeight());

                    ParallelTransition parallelTransition = new ParallelTransition(fadeOut, translate);
                    

                    AbonnementController controller = new AbonnementController();
                    controller.showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Abonnement annulé avec succès!");
                    parallelTransition.play();
                      
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Gérer les erreurs
                    }
                } else {
                    setStyle(originalColor);
                }
            });
            getChildren().addAll(nameLabel);
        }
    }


    @FXML
    private void handleSubmit(ActionEvent event) {

        if (cmbAdherent.getSelectionModel().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez d'abord selectionner un adherent");
            return; // Arrêter le traitement si la combobox est vide
        }

        if (cmboffre.getSelectionModel().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez d'abord selectionner un offre");
            return; // Arrêter le traitement si la combobox est vide
        }


        
        // Créez une boîte de dialogue de confirmation
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Voulez-vous vraiment ajouter cet abonnement ?");
        confirmation.setContentText("Nom d'adherent : " + cmbAdherent.getValue()+ "\nNom d'offre : " + cmboffre.getValue());
        
        // Charger l'icône
        // Charger l'icône
        File imageFile = new File("./src/main/resources/com/club/Controller/pics/Confirmation.png");
        
        Image image = new Image(imageFile.toURI().toString());
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(48);
        imageView.setFitHeight(48);
        confirmation.setGraphic(imageView);


        // Appliquer du style CSS
        DialogPane dialogPane = confirmation.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
        dialogPane.getStyleClass().add("my-alert");

        // Affichez la boîte de dialogue et attendez la réponse de l'utilisateur
        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Si l'utilisateur a cliqué sur "OK", ajoutez le matériel et la dépense associée
                try {
                    Connection connection = new Gestion().connectionBd();
                    if (connection != null) {
                        // Ajouter le matériel
                        
                        MembreOffre m = new MembreOffre(getMembreIDFromNomPrenom(cmbAdherent.getValue()),Offre.getOffreIDParNom(connection,cmboffre.getValue())); ;
                        m.ajouterMembreOffre(connection);
                        showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Abonnement ajouté avec succès !");
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            String dateTimeString = LocalDateTime.now().format(formatter);
                            Offre offre = Offre.getOffreByID(connection,Offre.getOffreIDParNom(connection,cmboffre.getValue()));
                            double somme = offre.getPrix();
                        RevenuesJ rv = new RevenuesJ(getMembreIDFromNomPrenom(cmbAdherent.getValue()),dateTimeString,somme);
                        cmboffre.getSelectionModel().clearSelection();
                        rv.ajouterRevenuJ(connection);
                        
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Impossible de se connecter à la base de données.");
                    }
                    connection.close();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout des Preferences.");
                    e.printStackTrace();
                }
            }
        });
    }


    


    public int getMembreIDFromNomPrenom(String nomPrenom) throws SQLException {
        String[] split = nomPrenom.split("\t"); // Sépare le nom et le prénom
        if (split.length == 2) {
            String nom = split[0];
            String prenom = split[1];
            Connection connection = new Gestion().connectionBd(); // Obtenez votre connexion à la base de données ici
            if (connection != null) {
                return Gestion.getMembreIDFromNomPrenom(connection, nom, prenom);
            } else {
                System.err.println("La connexion à la base de données est null.");
            }
        } else {
            System.err.println("Format de nom et prénom incorrect.");
        }
        return 0; // Retourne 0 si le MembreID n'a pas été trouvé
    }

    private void showAlert(Alert.AlertType type, String title, String message) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        
        File imageFile = null;
        
        ImageView imageView = new ImageView();
        // Charger l'icône en dehors des conditions
        if (type == Alert.AlertType.ERROR) {
             imageFile = new File("./src/main/resources/com/club/Controller/pics/Error.png");
        } else if (type == Alert.AlertType.WARNING) {
             imageFile = new File("./src/main/resources/com/club/Controller/pics/Warning (2).png");
        } else if (type == Alert.AlertType.CONFIRMATION) {
             imageFile = new File("./src/main/resources/com/club/Controller/pics/Confirmation.png");
        } else if (type == Alert.AlertType.INFORMATION) {
             imageFile = new File("./src/main/resources/com/club/Controller/pics/info (1).png");
        }

        if (imageFile != null) {
            Image image = new Image(imageFile.toURI().toString());
            imageView.setImage(image);
            imageView.setFitWidth(48); // Définissez la largeur de l'icône
            imageView.setFitHeight(48); // Définissez la hauteur de l'icône
            alert.setGraphic(imageView);
        }

        // Appliquer du style CSS
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
        dialogPane.getStyleClass().add("my-alert");

        alert.showAndWait();
    }

    // Méthode pour passer à une autre scène
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    
    //go to Paiement page
    public void switchToScene6(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("Paiement.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}