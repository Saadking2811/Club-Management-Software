package com.club.Controller;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.club.Model.Coach;
import com.club.Model.DossierMedical;
import com.club.Model.Gestion;
import com.club.Model.Membre;
import com.club.Model.Staff;
import com.club.Model.Utilisateurs;


public class ForumControllerStaff implements Initializable {

    @FXML
    private AnchorPane layer1;

    @FXML
    private AnchorPane layer2;

    @FXML
    private ComboBox<?> GroupeS;

    @FXML
    private TextField blessure;

    @FXML
    private TextField chronique;

    @FXML
    private Label d2;

    @FXML
    private Button habilite;

    @FXML
    private Button revenor;

    @FXML
    private Label textrevenir;

    @FXML
    private Label textrevenir1;

    @FXML
    private Button revenor1;

    @FXML
    private Label d21;

    @FXML
    private Button AjouterPhAdherent;

    
    
    @FXML private Button AjouterButtonAdherent; 
    
    

    @FXML
    private TextField PoidsAdherentField;

    @FXML
    private TextField TailleAdherentField;

    @FXML
    private TextField adressField;

    @FXML
    private DatePicker dateEntree;

    @FXML
    private DatePicker dateNaissance;

    @FXML
    private TextField emailField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField surnameField;

    @FXML
    private ComboBox<?> GenreAdherentA;

    @FXML
    private Label emailV;






    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        d2.setVisible(false);
        GroupeS.setVisible(false);
        blessure.setVisible(false);
        chronique.setVisible(false);
        habilite.setVisible(false);
        revenor.setVisible(false);
        textrevenir.setVisible(false);
        AjouterButtonAdherent.setVisible(false);


        BubbleAnimation b= new BubbleAnimation();
        //...
        b.startAnimation(layer2);


    }



    @FXML
    public void btn (ActionEvent event){
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.7));
        slide.setNode(layer2);

        slide.setToX(650);
        slide.play();

        layer1.setTranslateX(-400);
        d2.setVisible(true);
        GroupeS.setVisible(true);
        blessure.setVisible(true);
        chronique.setVisible(true);
        habilite.setVisible(true);
        revenor.setVisible(true);
        textrevenir.setVisible(true);
        AjouterButtonAdherent.setVisible(true);

        textrevenir1.setVisible(false);
        revenor1.setVisible(false);
        d21.setVisible(false);
        AjouterPhAdherent.setVisible(false);
        PoidsAdherentField.setVisible(false);
        TailleAdherentField.setVisible(false);
        adressField.setVisible(false);
        dateEntree.setVisible(false);
        dateNaissance.setVisible(false);
        emailField.setVisible(false);
        nameField.setVisible(false);
        phoneField.setVisible(false);
        surnameField.setVisible(false);
        GenreAdherentA.setVisible(false);
        emailV.setVisible(false);

        slide.setOnFinished((e->{}));


    }

    @FXML
    public void btn1 (ActionEvent event){
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.7));
        slide.setNode(layer2);

        slide.setToX(0);
        slide.play();

        layer1.setTranslateX(0);
        d2.setVisible(false);
        GroupeS.setVisible(false);
        blessure.setVisible(false);
        chronique.setVisible(false);
        habilite.setVisible(false);
        revenor.setVisible(false);
        textrevenir.setVisible(false);
        AjouterButtonAdherent.setVisible(false);

        textrevenir1.setVisible(true);
        revenor1.setVisible(true);
        d21.setVisible(true);
        AjouterPhAdherent.setVisible(true);
        PoidsAdherentField.setVisible(true);
        TailleAdherentField.setVisible(true);
        adressField.setVisible(true);
        dateEntree.setVisible(true);
        dateNaissance.setVisible(true);
        emailField.setVisible(true);
        nameField.setVisible(true);
        phoneField.setVisible(true);
        surnameField.setVisible(true);
        GenreAdherentA.setVisible(true);
        emailV.setVisible(true);

        slide.setOnFinished((e->{}));


    }

    private Stage stage;
    private Scene scene;
    private Parent root;


    



    @FXML
    private void validateEmail() {
        String email = emailField.getText();
        if (!isValidEmail(email)) {
            emailV.setText("Format d'email non valide");
            emailV.setTextFill(javafx.scene.paint.Color.RED);
        } else {
            emailV.setText("");
        }
    }

    private boolean isValidEmail(String email) {
        // Regular expression for basic email validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return email != null && pattern.matcher(email).matches();
    }

    
String cheminphoto;

String cheminfichier;
@FXML
private void addPhoto(ActionEvent event) {
    // Créer un FileChooser
    FileChooser fileChooser = new FileChooser();
    
    // Définir le titre du FileChooser
    fileChooser.setTitle("Choisir une photo");

    // Filtrer les fichiers pour afficher uniquement les images
    FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png", "*.gif");
    fileChooser.getExtensionFilters().add(filter);

    // Afficher la boîte de dialogue pour sélectionner un fichier
    File selectedFile = fileChooser.showOpenDialog(null);

    // Vérifier si un fichier a été sélectionné
    if (selectedFile != null) {
        // Obtenir le chemin absolu du fichier sélectionné
        cheminphoto = selectedFile.getAbsolutePath();
        
        // Utiliser le chemin du fichier sélectionné (par exemple, l'afficher)
        System.out.println("Chemin du fichier sélectionné : " + cheminphoto);
    }
}


@FXML
    private void addFile(ActionEvent event) {
        // Créer un FileChooser
        FileChooser fileChooser = new FileChooser();
        
        // Définir le titre du FileChooser
        fileChooser.setTitle("Choisir un fichier");

        // Afficher la boîte de dialogue pour sélectionner un fichier
        File selectedFile = fileChooser.showOpenDialog((Stage) ((Node) event.getSource()).getScene().getWindow());

        // Vérifier si un fichier a été sélectionné
        if (selectedFile != null) {
            // Obtenir le chemin absolu du fichier sélectionné
            cheminfichier = selectedFile.getAbsolutePath();
            
            // Utiliser le chemin du fichier sélectionné (par exemple, l'afficher)
            System.out.println("Chemin du fichier sélectionné : " + cheminphoto);
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



public void ValidationStaff(ActionEvent event) {

    String name = nameField.getText();
String fname = surnameField.getText();
// String categorie = categorieField.getText();
String phone = phoneField.getText();
String adress = adressField.getText();
String gender = GenreAdherentA.getValue().toString();
String email = emailField.getText();

//date conversion
LocalDate dateN = dateNaissance.getValue();
LocalDate dateE = dateEntree.getValue();
Date dateNN =  java.util.Date.from(dateN.atStartOfDay(ZoneId.systemDefault()).toInstant());
Date dateEE =  java.util.Date.from(dateE.atStartOfDay(ZoneId.systemDefault()).toInstant());
double taille;
double poids;
 try {
     // Code à exécuter qui pourrait générer une exception
     taille = Double.parseDouble(TailleAdherentField.getText());
     poids = Double.parseDouble(PoidsAdherentField.getText());
 

     Staff staff = new Staff( name, fname, dateNN, gender,dateEE, email, adress, phone, poids, taille);
     staff.setPhoto(cheminphoto);
     
     // Créer une boîte de dialogue de confirmation
     Alert confirmation = new Alert(AlertType.CONFIRMATION);
     confirmation.setTitle("Confirmation");
     confirmation.setHeaderText("Voulez-vous vraiment ajouter ce(ette) Staff?");
     confirmation.setContentText("Nom : " + name + "\nPrénom : " + fname);
      Gestion gestion = new Gestion();
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
     confirmation.showAndWait().ifPresent(response -> {
         if (response == ButtonType.OK) {
             try {
                 Connection connect = gestion.connectionBd();
                 if (connect != null) {
                    gestion.ajouterStaff(staff, connect);
                    DossierMedical D = new DossierMedical(0,Utilisateurs.getDernierUserID(connect),GroupeS.getValue().toString(),chronique.getText(),blessure.getText(),cheminfichier);
                    D.ajouterDossierMedical(connect);
                    showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Staff ajouté avec succès !");
                     connect.close();
                 } else {
                     showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Impossible de se connecter à la base de données.");
                 }
             } catch (SQLException e) {
                 showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout du Staff");
                 e.printStackTrace();
             }
         }
 }
 );
 } catch (NumberFormatException e) {
     showAlert(Alert.AlertType.ERROR, "Attention !!!", "La taille et le poids de Staff doivent être un nombre entier");
 }
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

}


