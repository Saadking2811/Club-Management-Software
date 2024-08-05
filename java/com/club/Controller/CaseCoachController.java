package com.club.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import com.club.Model.Adherent2;
import com.club.Model.Gestion;

public class CaseCoachController  {

    @FXML
    private Button add;

    @FXML
    private Label dateee;

    @FXML
    private Label datenn;

    @FXML
    private Button delete;

    @FXML
    private Label em;

    @FXML
    private Label fullname;



    @FXML
    private Label phone;

    @FXML
    private ImageView photo;

    
    private int AdherentID;
    public int getAdherentID() {
        return AdherentID;
    }
    public void setAdherentID(int AdherentID) {
        this.AdherentID = AdherentID;
    }


    public void setData(Adherent2 adherent2){
        try {

            fullname.setText(adherent2.getFullname());
            em.setText(adherent2.getEm());
            phone.setText(adherent2.getPhone());
            datenn.setText(adherent2.getDatenn());
            dateee.setText(adherent2.getDateee());
            AdherentID = adherent2.getAdhID();
                        // Vérifie si le chemin est absolu
String picStream = adherent2.getPhoto();
if (picStream != null) {
    if (Paths.get(picStream).isAbsolute()) {
        // C'est un chemin absolu
        File imageFile = new File(picStream);
        if (imageFile.exists()) {
            Image image = new Image(imageFile.toURI().toString());
            photo.setImage(image); // Définition de l'image sur l'ImageView pic
        } else {
            System.out.println("Le fichier d'image n'existe pas: " + picStream);
        }
    } else {
        System.out.println("Le chemin de l'image n'est pas absolu: " + picStream);
    }
} else {
    // Gérer la ressource manquante
    System.out.println("Ressource de l'image non trouvée: " + adherent2.getPhoto());
}

        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
        }
    }
    @FXML
    public void switchToDetailCoach(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("detailCoach.fxml"));
        Parent root = loader.load();

        // Obtenir le contrôleur du détail de l'adhérent
        ConnexionController detailController = loader.getController();

        // Passer les données de la carte sélectionnée au contrôleur du détail de l'adhérent
        detailController.initialiserCoach(this.AdherentID);

        // Remplacer la scène actuelle par la scène des détails de l'adhérent
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 1024, 760));
        stage.setResizable(false);
        stage.show();
    }

    // Méthode pour supprimer la carte sélectionnée
    @FXML
    private void Supprimercard(ActionEvent event) {
            // Créer une boîte de dialogue de confirmation
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Voulez-vous vraiment supprimer cet coach ?");
            confirmation.setContentText("Nom et Prénom : " + fullname.getText()  + "\nDate de Naissance : " + datenn.getText()+"\nDate d'entree : "+ dateee.getText());

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
            // Afficher la boîte de dialogue de confirmation et attendre la réponse de l'utilisateur
            Optional<ButtonType> result = confirmation.showAndWait();

            // Si l'utilisateur a cliqué sur le bouton OK, procéder à la suppression de l'adhérent
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    Connection connection = new Gestion().connectionBd(); // Créez une instance de Gestion
                    Gestion gestion = new Gestion(); // Créez une instance de Gestion
                    gestion.supprimerUtilisateur(connection,this.AdherentID); // Appelez la méthode supprimerUtilisateur sur cette instance
                    
                    showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Coach supprimé avec succès!");
                    
                    switchToVoirCoachs(event);
                    
                } catch (Exception e) {
                    e.printStackTrace(); // Gérer d'autres exceptions génériques ici
                }
            }
    }



    private Stage stage;
    private Parent root;
    public void switchToVoirCoachs(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("voirCoachs.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
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


}
