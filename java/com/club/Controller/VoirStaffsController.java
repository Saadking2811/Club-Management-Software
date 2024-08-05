package com.club.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import com.club.Model.Adherent2;
import com.club.Model.Gestion;
import com.club.Model.Staff;

public class VoirStaffsController implements Initializable {


    private List<Adherent2>  Adh2 = new ArrayList<Adherent2>() ;




    

    @FXML
    private ScrollPane scroll2;

    @FXML
    private AnchorPane a2;

    @FXML
    private TextField searchIdCoach;

    
    

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        

        try (Connection connection = new Gestion().connectionBd()) {
            if (connection != null) {
                Adh2 = adherents(Staff.recupererTousLesStaffs(connection));
                
                connection.close();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la récupération des staffs.");
            e.printStackTrace();
        }

        // Ajouter un écouteur de changement de texte au champ de recherche
        searchIdCoach.textProperty().addListener((observable, oldValue, newValue) -> {
            // Vérifier si le nouveau texte est vide
            if (newValue.isEmpty()) {
                // Recharger la page lorsque la zone de recherche est vide
                reloadPage();
            } else {
                // Filtrer et afficher les salles en fonction du texte de recherche
                rechercherCartes();
            }
        });


        

        double yPosition = 0.0;
        double hBoxHeight = 0;
        for (int j = 0; j < Adh2.size(); j++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/caseStaff.fxml"));
            try {
                HBox hBox = fxmlLoader.load();
                CaseStaffController controller = fxmlLoader.getController();
                controller.setData(Adh2.get(j));
                
                // Calculer la hauteur préférée de l'HBox en fonction de son contenu réel
                hBox.applyCss();
                hBox.layout();
                hBoxHeight = hBox.prefHeight(-1);
                
                // Positionner l'HBox dans l'AnchorPane avec sa hauteur calculée
                AnchorPane.setTopAnchor(hBox, yPosition);
                AnchorPane.setLeftAnchor(hBox, 10.0);
                
                // Ajouter l'HBox à l'AnchorPane
                a2.getChildren().add(hBox);
                
                // Ajouter la transition de survol à l'HBox
                hBox.setOnMouseEntered(event -> {
                    hBox.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                });
        
                hBox.setOnMouseExited(event -> {
                    hBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                });
                
                // Augmenter la position Y pour le prochain élément
                yPosition += hBoxHeight + 10.0;
                
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        a2.setPrefHeight(yPosition*1.1);
    }        



    
    // Méthode pour filtrer les cartes selon le texte de recherche
    @FXML
    private void rechercherCartes() {
        String texteRecherche = searchIdCoach.getText();
        
        filtrerCases(texteRecherche);
        afficherCasesAdherents();;
    }

    // Méthode pour filtrer les cartes en fonction du texte de recherche
    private void filtrerCases(String texteRecherche) {
        
        Adh2.clear();
        // Récupérer à nouveau les adhérents de la base de données
        try {
            Connection connection = new Gestion().connectionBd();
            List<Staff> Adh22 = Staff.recupererTousLesStaffs(connection);
            connection.close();
            List<Staff> Ad23 = new ArrayList<Staff>(); 

        
        // Filtrer les adhérents dont le nom commence par le texte de recherche
        for (Staff adherent : Adh22) {
            if (adherent.getNom().toLowerCase().contains(texteRecherche.toLowerCase())) {
                
                Ad23.add(adherent);
            }
        }
        Adh2 = adherents(Ad23);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


private List<Adherent2> adherents(List<Staff> adherentsFromDatabase){
        List<Adherent2> ls2 = new ArrayList<>();
        for (Staff adherent1 : adherentsFromDatabase) {
            Adherent2 adherent= new Adherent2();
      adherent.setFullname(adherent1.getNom()+" "+adherent1.getPrenom());
      adherent.setPhoto(adherent1.getPhoto());
      adherent.setDateee(adherent1.getDateEntree().toString());
      adherent.setDatenn(adherent1.getDateNaissance().toString());
      adherent.setEm(adherent1.getEmail());
      adherent.setPhone(String.valueOf(adherent1.getTelephone()));
      adherent.setGenre(adherent1.getGenre());
      adherent.setAdhID(adherent1.getMembreID());
      ls2.add(adherent);
        }

        return ls2;






    }

private void afficherCasesAdherents() {
    
    // Nettoyer l'AnchorPane avant d'ajouter de nouveaux éléments
    a2.getChildren().clear();

    double yPosition = 0.0;
        double hBoxHeight = 0;
        for (int j = 0; j < Adh2.size(); j++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/caseStaff.fxml"));
            try {
                HBox hBox = fxmlLoader.load();
                CaseStaffController controller = fxmlLoader.getController();
                controller.setData(Adh2.get(j));
                
                // Calculer la hauteur préférée de l'HBox en fonction de son contenu réel
                hBox.applyCss();
                hBox.layout();
                hBoxHeight = hBox.prefHeight(-1);
                
                // Positionner l'HBox dans l'AnchorPane avec sa hauteur calculée
                AnchorPane.setTopAnchor(hBox, yPosition);
                AnchorPane.setLeftAnchor(hBox, 10.0);
                
                // Ajouter l'HBox à l'AnchorPane
                a2.getChildren().add(hBox);
                
                // Ajouter la transition de survol à l'HBox
                hBox.setOnMouseEntered(event -> {
                    hBox.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                });
        
                hBox.setOnMouseExited(event -> {
                    hBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                });
                
                // Augmenter la position Y pour le prochain élément
                yPosition += hBoxHeight + 10.0;
                
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        a2.setPrefHeight(yPosition*1.1);
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


    private void reloadPage() {
        // Nettoyer l'AnchorPane avant d'ajouter de nouveaux éléments
        a2.getChildren().clear();
        try (Connection connection = new Gestion().connectionBd()) {
            if (connection != null) {
                Adh2 = adherents(Staff.recupererTousLesStaffs(connection));
                connection.close();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la récupération des staffs.");
            e.printStackTrace();
        }


        


        double yPosition = 0.0;
        double hBoxHeight = 0;
        for (int j = 0; j < Adh2.size(); j++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/com/club/Controller/caseStaff.fxml"));
            try {
                HBox hBox = fxmlLoader.load();
                CaseStaffController controller = fxmlLoader.getController();
                controller.setData(Adh2.get(j));
                
                // Calculer la hauteur préférée de l'HBox en fonction de son contenu réel
                hBox.applyCss();
                hBox.layout();
                hBoxHeight = hBox.prefHeight(-1);
                
                // Positionner l'HBox dans l'AnchorPane avec sa hauteur calculée
                AnchorPane.setTopAnchor(hBox, yPosition);
                AnchorPane.setLeftAnchor(hBox, 10.0);
                
                // Ajouter l'HBox à l'AnchorPane
                a2.getChildren().add(hBox);
                
                // Ajouter la transition de survol à l'HBox
                hBox.setOnMouseEntered(event -> {
                    hBox.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
                });
        
                hBox.setOnMouseExited(event -> {
                    hBox.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                });
                
                // Augmenter la position Y pour le prochain élément
                yPosition += hBoxHeight + 10.0;
                
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        a2.setPrefHeight(yPosition*1.1);
    }        





/***************************************************** */
    private Stage stage;
    private Parent root;

    

    

    

    

// page Acceuil:

       //si on veut clicker sur une image ( OnMouseClicked="#...." )
       @FXML
       public void switchToHome(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("homePage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }



       



// page gestionProfilStaff:

   //si on veut clicker sur une image ( OnMouseClicked="#...." )
   @FXML
   public void switchToGPS(MouseEvent event) throws IOException{
    root = FXMLLoader.load(getClass().getResource("gestionProfilStaff.fxml"));
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root, 1024, 760);
    HomeController.setTheme(scene);
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();
}




        



// page ajouterStaff:

   //si on veut clicker sur une image ( OnMouseClicked="#...." )
   @FXML
     public void switchToAjouterStaff(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("ajouterStaff.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }



        
// page detailStaff:

   //si on veut clicker sur une image ( OnMouseClicked="#...." )
   @FXML
     public void switchToDetailStaff(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("detailStaff.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }



       
// page voirStaff:

   //si on veut clicker sur une image ( OnMouseClicked="#...." )
   @FXML
     public void switchToVoirStaff(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("voirStaff.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }











    

}
