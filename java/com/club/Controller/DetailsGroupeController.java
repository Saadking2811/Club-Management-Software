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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Comparator;


import com.club.Model.Adherent;
import com.club.Model.Coach;
import com.club.Model.Gestion;
import com.club.Model.Groupe;
import com.club.Model.Salle;
import com.club.Model.SalleGroupe;


public class DetailsGroupeController {

    @FXML
    private ScrollPane rootPane;

    @FXML
    private VBox coachsBox;
    
    @FXML
    private VBox adherentsBox;

    @FXML
    private VBox sallesBox;

    private Connection connection;

    private int groupeID;

    @FXML
    private ComboBox<String> cmbAdh;

    @FXML
    private ComboBox<String> cmbAdhG;

    @FXML
    private ComboBox<String> cmbSall;

    @FXML
    private ComboBox<String> cmbSallG;

    @FXML
    private ComboBox<String> cmbCoach;

    @FXML
    private ComboBox<String> cmbCoachG;

    @FXML
    private Text NbAdherents;

    @FXML
    private Text NbCoaches;

    @FXML
    private Text NbSalles;

    @FXML
    private Text nomG;


    public void setGroupeID(int groupeID) {
        this.groupeID = groupeID;
    }
    public int getGroupeID() {
        return groupeID;
    }

    
    

    private void afficherCoachs() throws SQLException {
        coachsBox.getChildren().clear(); // Efface les anciennes données
    
        List<Coach> coachs = Gestion.getCoachsDuGroupe(connection, groupeID);
    
        // Tri de la liste des coaches par nom puis par prénom
        coachs.sort(Comparator.comparing(Coach::getNom).thenComparing(Coach::getPrenom));
    
        for (Coach coach : coachs) {
            CoachItem coachItem = new CoachItem(coach);
            coachsBox.getChildren().add(coachItem);
        }
    }
    private static class CoachItem extends VBox {
        public CoachItem(Coach coach) {
            setAlignment(Pos.CENTER_LEFT);
            setSpacing(5);
            setPadding(new Insets(10));
            setStyle(" -fx-background-radius: 5px; -fx-border-color: #ddd; -fx-border-radius: 5px; -fx-border-width: 1px;");

            Label nameLabel = new Label(coach.getNom() + "\t\t\t " + coach.getPrenom());
            nameLabel.setStyle("-fx-text-fill: white;-fx-font-family: 'Poppins';");
            getChildren().addAll(nameLabel);
        }
    }


    private void afficherSalles() throws SQLException {
        sallesBox.getChildren().clear(); // Efface les anciennes données
    // Appel à la méthode pour obtenir la liste de salleID
    List<Integer> salles = SalleGroupe.getSallesByGroupe(connection, groupeID);
        
    // Appel à la méthode pour obtenir la liste de salles correspondantes
    List<Salle> listeSalles = Salle.getListeSallesParIDs(connection, salles);
        
    
        // Tri de la liste des coaches par nom puis par prénom
        listeSalles.sort(Comparator.comparing(Salle::getNomSalle));
    
        for (Salle salle : listeSalles) {
            SalleItem salleItem = new SalleItem(salle);
            sallesBox.getChildren().add(salleItem);
        }
    }
    private static class SalleItem extends VBox {
        public SalleItem(Salle salle) {
            setAlignment(Pos.CENTER_LEFT);
            setSpacing(5);
            setPadding(new Insets(10));
            setStyle(" -fx-background-radius: 5px; -fx-border-color: #ddd; -fx-border-radius: 5px; -fx-border-width: 1px;");

            Label nameLabel = new Label(salle.getNomSalle());
            nameLabel.setStyle("-fx-text-fill: white;-fx-font-family: 'Poppins';");
            getChildren().addAll(nameLabel);
        }
    }



    private void afficheradherents() throws SQLException {
        adherentsBox.getChildren().clear(); // Efface les anciennes données
    
        List<Adherent> adherents = Gestion.getAdherentsDuGroupe(connection, groupeID);
    
        // Tri de la liste des adhérents par nom puis par prénom
        adherents.sort(Comparator.comparing(Adherent::getNom).thenComparing(Adherent::getPrenom));
    
        for (Adherent adherent : adherents) {
            AdherentItem adherentItem = new AdherentItem(adherent);
            adherentsBox.getChildren().add(adherentItem);
        }
    }

    private static class AdherentItem extends VBox {

        public AdherentItem(Adherent adherent) {
            setAlignment(Pos.CENTER_LEFT);
            setSpacing(5);
            setPadding(new Insets(10));
            setStyle(" -fx-background-radius: 5px; -fx-border-color: #ddd; -fx-border-radius: 5px; -fx-border-width: 1px;");

            Label nameLabel = new Label(adherent.getNom() + "\t\t\t " + adherent.getPrenom());

            nameLabel.setStyle("-fx-text-fill: white;-fx-font-family: 'Poppins';");
            getChildren().addAll(nameLabel);
        }
    }


    public void initData(int groupeID) throws SQLException {
        this.groupeID = groupeID;
        connection = new Gestion().connectionBd();
        System.out.println("We are here : "+groupeID);
        nomG.setText(Groupe.getGroupeByID(connection, groupeID).getNomGroupe());
        afficheradherents();
        afficherCoachs();
        afficherSalles();
        NbAdherents.setText(String.valueOf(Gestion.getAdherentsDuGroupe(connection,groupeID).size()));
        NbCoaches.setText(String.valueOf(Gestion.getCoachsDuGroupe(connection,groupeID).size()));
        NbSalles.setText(String.valueOf(SalleGroupe.getSallesByGroupe(connection,groupeID).size()));

        try (Connection connection = new Gestion().connectionBd()) {
            if (connection != null) {
                List<Adherent> adherents = new Gestion().recupererTousLesAdherents(connection);
                adherents.sort(Comparator.comparing(Adherent::getNom).thenComparing(Adherent::getPrenom));
                ObservableList<String> nomsAdh = FXCollections.observableArrayList();
                
                for (Adherent adh : adherents) {
                    nomsAdh.add(adh.getNom() + "\t\t" + adh.getPrenom());
                }
                
                cmbAdh.setItems(nomsAdh);
        
        
                List<Adherent> adherentsG = new Gestion().getAdherentsDuGroupe(connection,groupeID);
                adherentsG.sort(Comparator.comparing(Adherent::getNom).thenComparing(Adherent::getPrenom));
                ObservableList<String> nomsAdhG = FXCollections.observableArrayList();
                
                for (Adherent adh : adherentsG) {
                    nomsAdhG.add(adh.getNom() + "\t\t" + adh.getPrenom());
                }
                
                cmbAdhG.setItems(nomsAdhG);
        
        
                List<Coach> Coachs = new Gestion().recupererTousLesCoaches(connection);
                Coachs.sort(Comparator.comparing(Coach::getNom).thenComparing(Coach::getPrenom));
                ObservableList<String> nomscoach = FXCollections.observableArrayList();
                
                for (Coach adh : Coachs) {
                    nomscoach.add(adh.getNom() + "\t\t" + adh.getPrenom());
                }
                
                cmbCoach.setItems(nomscoach);
        
                List<Coach> CoachsG = new Gestion().getCoachsDuGroupe(connection,groupeID);
                CoachsG.sort(Comparator.comparing(Coach::getNom).thenComparing(Coach::getPrenom));
                ObservableList<String> nomscoachG = FXCollections.observableArrayList();
                
                for (Coach adh : CoachsG) {
                    nomscoachG.add(adh.getNom() + "\t\t" + adh.getPrenom());
                }
                
                cmbCoachG.setItems(nomscoachG);

                List<Salle> Salles = Salle.getListeSalles(connection);
                Salles.sort(Comparator.comparing(Salle::getNomSalle));
                ObservableList<String> nomssalles = FXCollections.observableArrayList();
                
                for (Salle salle : Salles ) {
                    nomssalles.add(salle.getNomSalle());
                }
                
                cmbSall.setItems(nomssalles);

                // Appel à la méthode pour obtenir la liste de salleID
                List<Integer> salles = SalleGroupe.getSallesByGroupe(connection, groupeID);
        
                // Appel à la méthode pour obtenir la liste de salles correspondantes
                List<Salle> listeSalles = Salle.getListeSallesParIDs(connection, salles);
                
                listeSalles.sort(Comparator.comparing(Salle::getNomSalle));
                ObservableList<String> nomssallesG = FXCollections.observableArrayList();
                
                for (Salle salle : listeSalles) {
                    nomssallesG.add(salle.getNomSalle());
                }
                
                cmbSallG.setItems(nomssallesG);
            }
        }
         catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la récupération des donnees.");
            e.printStackTrace();
        }
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
    public int getMembreIDFromNomPrenom(String nomPrenom) throws SQLException {
        String[] split = nomPrenom.split("\t\t"); // Sépare le nom et le prénom
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


    @FXML
    private void ajouterAdh(ActionEvent event) {
    

    // Vérifier si une salle est sélectionnée
    if (cmbAdh.getSelectionModel().isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez d'abord Selectionner un Adhernet.");
        return;
    }
    
    String selectedAdh = cmbAdh.getSelectionModel().getSelectedItem();
    
    // Créez une boîte de dialogue de confirmation
    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
    confirmation.setTitle("Confirmation");
    confirmation.setHeaderText("Voulez-vous vraiment ajouter cet Adherent ?");
    confirmation.setContentText(selectedAdh);
    
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
                    Gestion.ajouterMembreAuGroupe(connection, getMembreIDFromNomPrenom(selectedAdh), groupeID);
                    
                    showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Adherent ajouté avec succès !");
                    connection.close();
                    switchToDetailGroupe(event);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Impossible de se connecter à la base de données.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout du adherent.");
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    });
}


@FXML
    private void ajouterSalle(ActionEvent event) {
    

    // Vérifier si une salle est sélectionnée
    if (cmbSall.getSelectionModel().isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez d'abord Selectionner une salle.");
        return;
    }
    
    String selectedAdh = cmbSall.getSelectionModel().getSelectedItem();
    
    // Créez une boîte de dialogue de confirmation
    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
    confirmation.setTitle("Confirmation");
    confirmation.setHeaderText("Voulez-vous vraiment ajouter cet salle ?");
    confirmation.setContentText(selectedAdh);
    
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
                    SalleGroupe s = new SalleGroupe(0,Salle.getSalleIdByNom(connection, selectedAdh),groupeID,false);
                    s.ajouterSalleGroupe(connection);
                    
                    showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Salle ajouté avec succès !");
                    connection.close();
                    switchToDetailGroupe(event);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Impossible de se connecter à la base de données.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout du salle.");
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    });
}



@FXML
    private void ajouterCoach(ActionEvent event) {
    

    // Vérifier si une salle est sélectionnée
    if (cmbCoach.getSelectionModel().isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez d'abord Selectionner un Coach.");
        return;
    }
    
    String selectedAdh = cmbCoach.getSelectionModel().getSelectedItem();
    
    // Créez une boîte de dialogue de confirmation
    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
    confirmation.setTitle("Confirmation");
    confirmation.setHeaderText("Voulez-vous vraiment ajouter cet Coach ?");
    confirmation.setContentText(selectedAdh);
    
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
                    Gestion.ajouterCoachAuGroupe(connection, getMembreIDFromNomPrenom(selectedAdh), groupeID);
                    
                    showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Coach ajouté avec succès !");
                    connection.close();
                    switchToDetailGroupe(event);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Impossible de se connecter à la base de données.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout du coach.");
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    });
}


@FXML
    private void supprimerAdh(ActionEvent event) {
    

    // Vérifier si une salle est sélectionnée
    if (cmbAdhG.getSelectionModel().isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez d'abord Selectionner un Adhernet.");
        return;
    }
    
    String selectedAdh = cmbAdhG.getSelectionModel().getSelectedItem();
    
    // Créez une boîte de dialogue de confirmation
    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
    confirmation.setTitle("Confirmation");
    confirmation.setHeaderText("Voulez-vous vraiment supprimer cet Adherent ?");
    confirmation.setContentText(selectedAdh);
    
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
                    Gestion.supprimerMembreDuGroupe(connection, getMembreIDFromNomPrenom(selectedAdh), groupeID);
                    
                    showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Adherent supprimé avec succès !");
                    connection.close();
                    switchToDetailGroupe(event);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Impossible de se connecter à la base de données.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la suppression du adherent.");
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    });
}


@FXML
    private void supprimerSalle(ActionEvent event) {
    

    // Vérifier si une salle est sélectionnée
    if (cmbSallG.getSelectionModel().isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez d'abord Selectionner un Adhernet.");
        return;
    }
    
    String selectedAdh = cmbSallG.getSelectionModel().getSelectedItem();
    
    // Créez une boîte de dialogue de confirmation
    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
    confirmation.setTitle("Confirmation");
    confirmation.setHeaderText("Voulez-vous vraiment supprimer cet Adherent ?");
    confirmation.setContentText(selectedAdh);
    
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
                    
                    SalleGroupe.supprimerSalleGroupe(connection,Salle.getSalleIdByNom(connection, selectedAdh),groupeID);
                    showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Salle supprimé avec succès !");
                    connection.close();
                    switchToDetailGroupe(event);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Impossible de se connecter à la base de données.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la suppression du salle.");
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    });
}

@FXML
    private void supprimerCoach(ActionEvent event) {
    

    // Vérifier si une salle est sélectionnée
    if (cmbCoachG.getSelectionModel().isEmpty()) {
        showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez d'abord Selectionner un Coach.");
        return;
    }
    
    String selectedAdh = cmbCoachG.getSelectionModel().getSelectedItem();
    
    // Créez une boîte de dialogue de confirmation
    Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
    confirmation.setTitle("Confirmation");
    confirmation.setHeaderText("Voulez-vous vraiment supprimer cet Coach ?");
    confirmation.setContentText(selectedAdh);
    
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
                    Gestion.supprimerCoachDuGroupe(connection, getMembreIDFromNomPrenom(selectedAdh), groupeID);
                    
                    showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Adherent supprimé avec succès !");
                    connection.close();
                    switchToDetailGroupe(event);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Impossible de se connecter à la base de données.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la suppression du Coach.");
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    });
}


    /************************************************************************************************** 
 * 
 */
private Stage stage;
private Scene scene;
private Parent root;


public void switchToDetailGroupe(Event event) throws IOException, SQLException{
    FXMLLoader loader = new FXMLLoader(getClass().getResource("detailGroupe.fxml"));
    Parent root = loader.load();
    // Obtenir le contrôleur du détail de l'adhérent
    DetailsGroupeController detailController = loader.getController();

    // Passer les données de la carte sélectionnée au contrôleur du détail de l'adhérent
    detailController.initData(groupeID);

    // Remplacer la scène actuelle par la scène des détails de l'adhérent
    Scene scene = new Scene(root, 1024, 760);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.setResizable(false);
    stage.show();
}


public void switchToVoirGroupes(Event event) throws IOException{
    root = FXMLLoader.load(getClass().getResource("voirGroupes.fxml"));
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root, 1024, 760);
    HomeController.setTheme(scene);
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();
}

}
