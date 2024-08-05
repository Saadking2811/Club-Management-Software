package com.club.Controller;
import com.club.Model.Admin;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import com.club.Model.Membre;
import com.club.Model.Planning;

import javafx.stage.FileChooser;

import java.io.File;
import java.util.regex.Pattern;

import com.club.Model.Absence;
import com.club.Model.Adherent;
import com.club.Model.Coach;
import com.club.Model.DossierMedical;
import com.club.Model.Gestion;
import com.club.Model.Groupe;
import com.club.Model.Sport;
import com.club.Model.Staff;
import com.club.Model.Trophe;
import com.club.Model.Utilisateurs;
import com.club.Model.Gestion;

import javafx.scene.control.Alert.AlertType;
import javafx.geometry.Insets;
import java.time.LocalDate;
import java.util.Date;
import java.time.ZoneId;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;

import java.awt.image.BufferedImage;
import javafx.scene.input.MouseEvent;


public class ConnexionController implements Initializable  {

        private Stage stage;
        private Scene scene;
        private Parent root;
   @FXML TextField user;
   @FXML PasswordField pass;
        //go to connexion page
        public void switchToScene1(MouseEvent event) throws IOException{
            root = FXMLLoader.load(getClass().getResource("connexion-view.fxml"));
            Connection conn = new Gestion().connectionBd();
            Gestion.initMail(conn);
            System.out.println("\n"+Gestion.mail+ Gestion.pass);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1024, 760);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
        public void verify(MouseEvent event) throws IOException{
         if(Gestion.seConnecter(new Gestion().connectionBd(), user.getText(),pass.getText())){
            switchToHome(event);
         }else{
            showAlert(Alert.AlertType.ERROR, "Erreur", "Nom d'utilisateur ou mot de passe incorrect ");

         }
        }

        //go to Gestion des profils page using a button (from connexion page)
        public void switchToScene2(ActionEvent event) throws IOException{
            root = FXMLLoader.load(getClass().getResource("gestionProfil.fxml"));
            Connection conn = new Gestion().connectionBd();
            Gestion.initMail(conn);
            System.out.println("\n"+Gestion.mail+ Gestion.pass);
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1024, 760);
            HomeController.setTheme(scene);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }

     //go to Gestion des profils page with a mouse click
    public void switchToScene22(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("gestionProfil.fxml"));
        Connection conn = new Gestion().connectionBd();
            Gestion.initMail(conn);
            System.out.println("\n"+Gestion.mail+ Gestion.pass);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    //go to Gestion des profils-Admin page
    public void switchToScene2a(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("gestionProfilAdmin.fxml"));
        Connection conn = new Gestion().connectionBd();
            Gestion.initMail(conn);
            System.out.println("\n"+Gestion.mail+ Gestion.pass);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    //go to Gestion des profils-Coach page
    public void switchToScene2b(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("gestionProfilCoach.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    //go to Gestion des profils-Adherent page
    public void switchToScene2c(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("gestionProfilAdherent.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    //go to Gestion des sports page
    public void switchToScene3(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("gestionSport.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScene3a(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("sport.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToScene3b(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("ajouterMetriques.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScene3b1(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("ajouterMetriques.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    //go to salles/materiels page
    public void switchToScene4(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("salle_materiel.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScene4a(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("salles.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScene4b(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("materiels.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScene4c(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("categories.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    //go to presences page
    public void switchToScene5(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("presences.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


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

    public void switchToScene6a(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("Abonnement.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScene6b(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("offres.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


   
    @FXML private GridPane gridgroupe;
    // l'injection ne marche pas !!!!
    public GridPane findGridPaneInRootgroupes(Parent root) {

        List<Node> children = root.getChildrenUnmodifiable();

        for (Node node : children) {
            if (node instanceof TabPane) {
                TabPane tabPane = (TabPane) node;
                for (Tab tab : tabPane.getTabs()) {
                    if ("Groupes".equals(tab.getText())) {
                        VBox vbox = (VBox) tab.getContent();
                        for (Node vboxNode : vbox.getChildren()) {
                            if (vboxNode instanceof ScrollPane) {
                                ScrollPane scrollPane = (ScrollPane) vboxNode;

                                Node content = scrollPane.getContent();
                                if (content instanceof GridPane) {
                                    return (GridPane) content;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null; // GridPane not found
    }
//

    // Method to find the GridPane with id "gridcoach" inside a TabPane
    public GridPane findGridPaneInRootcoaches(Parent root) {
        for (Node node : root.getChildrenUnmodifiable()) {
            if (node instanceof TabPane) {
                TabPane tabPane = (TabPane) node;
                for (Tab tab : tabPane.getTabs()) {
                    // Check if the tab text is "Coaches"
                    if ("Coaches".equals(tab.getText())) {
                        // If it is, get its content
                        Node content = tab.getContent();
                        if (content instanceof ScrollPane) {
                            // If the content is a ScrollPane, get its content
                            ScrollPane scrollPane = (ScrollPane) content;
                            Node gridPaneNode = scrollPane.getContent();
                            if (gridPaneNode instanceof GridPane && gridPaneNode.getId().equals("gridcoach")) {
                                // If the content is a GridPane with the id "gridcoach", return it
                                return (GridPane) gridPaneNode;
                            }
                        }
                    }
                }
            }
        }
        return null; // GridPane not found
    }

    //
    @FXML private TextField groupeField;
    Gestion gestion = new Gestion();
    String[] Jour = {
            "Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"
    };

    public void showPlannerGroupe(ActionEvent event) throws IOException{

    
        root = FXMLLoader.load(getClass().getResource("planner.fxml"));
        gridgroupe= findGridPaneInRootgroupes(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);


try{
        int groupeChoix = Groupe.getGroupeIDByNom(new Gestion().connectionBd(),textchoix.getText());
        Connection connect = gestion.connectionBd();
        String tempsinitial ="06:00:00";
        String tempsfinal="01:00:00";
        LocalTime tempsini = LocalTime.parse(tempsinitial);
        LocalTime tempsfin= LocalTime.parse(tempsfinal);

        try{
           
            String query0="SELECT * FROM groupecreneaux WHERE GroupeID=?";
            PreparedStatement ps0 = connect.prepareStatement(query0);
            ps0.setInt(1,groupeChoix);
            ResultSet rs0 = ps0.executeQuery();
            while(rs0.next()){
                for(int i=0;i<7;i++){
                    LocalTime tempscourant = tempsini;
                    String query1="SELECT * FROM creneaux WHERE CreneauID=? AND Jour=? ORDER BY TempsDebut";
                    PreparedStatement ps1 = connect.prepareStatement(query1);
                    ps1.setInt(1,rs0.getInt("CreneauID"));
                    ps1.setString(2,Jour[i]);
                    ResultSet rs1 = ps1.executeQuery();
                    int j=1;

                    while(rs1.next()){
                        Time tempsdebutTime = rs1.getTime("TempsDebut");
                        LocalTime tempsdebut = tempsdebutTime.toLocalTime();
                        Time tempsfinTime = rs1.getTime("TempsFin");
                        LocalTime tempsfinact = tempsfinTime.toLocalTime();
                        while(tempscourant!=tempsfin){
                            if(tempsdebut.equals(tempscourant) ) {
                            
                                String query2="SELECT * FROM salles WHERE SalleID=?";
                                PreparedStatement ps2 = connect.prepareStatement(query2);
                                ps2.setInt(1,rs1.getInt("SalleID"));
                                ResultSet rs2 = ps2.executeQuery();
                                if(rs2.next()){
                                    Text text = new Text(rs2.getString("NomSalle"));
                                    text.setFont(Font.font("Helvetica", 16));
                                    text.setFill(javafx.scene.paint.Color.WHITE);
                                    StackPane stack = new StackPane();
                                    stack.setPadding(new Insets(0));

                                    stack.setStyle("-fx-background-color: #1f3f75");

                                    stack.getChildren().add(text);
                                    stack.setMaxWidth(121.6-2);
                                    stack.setMinWidth(121.6-2);
                                    stack.setMaxHeight(30.4);
                                    stack.setMinHeight(30.4);
                                    gridgroupe.add(stack,i+1,j);
                                    tempscourant=tempscourant.plusMinutes(30);
                                    j++;
                                }
                                do{
                                    StackPane stack = new StackPane();
                                    stack.setStyle("-fx-background-color:  #1f3f75");
                                   
                                    stack.setPadding(new Insets(0));
                                    stack.setMaxWidth(121.6-2);
                                    stack.setMinWidth(121.6-2);
                                    stack.setMaxHeight(30.4);
                                    stack.setMinHeight(30.4);
                                    gridgroupe.add(stack,i+1,j);
                                    tempscourant=tempscourant.plusMinutes(30);
                                    j++;
                                }
                                while(!tempscourant.equals(tempsfinact));
                            }else{
                                tempscourant=tempscourant.plusMinutes(30);
                                j++;
                            }
                        }
                    }
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }}catch(SQLException e){
            e.printStackTrace();
        }

    }
//coaches

    public void showPlannerCoach(ActionEvent event) throws IOException {

      


        try{
        int groupeChoix = Coach.getCoachIDByNom(new Gestion().connectionBd(), textchoix.getText());
        Connection connect = gestion.connectionBd();
        String tempsinitial ="06:00:00";
        String tempsfinal="01:00:00";
        LocalTime tempsini = LocalTime.parse(tempsinitial);
        LocalTime tempsfin= LocalTime.parse(tempsfinal);

        try{
            LocalTime tempscourant = tempsini;
            String query3="SELECT * FROM coachgroupe WHERE MembreID=?";
            PreparedStatement ps3 = connect.prepareStatement(query3);
            ps3.setInt(1,Coach.getCoachIDByNom(connect, textchoix.getText()));
            ResultSet rs3 = ps3.executeQuery();
            while (rs3.next()) {


                String query0="SELECT * FROM groupecreneaux WHERE GroupeID=?";
                PreparedStatement ps0 = connect.prepareStatement(query0);
                ps0.setInt(1,rs3.getInt("GroupeID"));
                ResultSet rs0 = ps0.executeQuery();
                while(rs0.next()){
                    for(int i=0;i<7;i++){
                        tempscourant = tempsini;
                        String query1="SELECT * FROM creneaux WHERE CreneauID=? AND Jour=? ORDER BY TempsDebut";
                        PreparedStatement ps1 = connect.prepareStatement(query1);
                        ps1.setInt(1,rs0.getInt("CreneauID"));
                        ps1.setString(2,Jour[i]);
                        ResultSet rs1 = ps1.executeQuery();
                        int j=1;

                        while(rs1.next()){
                            Time tempsdebutTime = rs1.getTime("TempsDebut");
                            LocalTime tempsdebut = tempsdebutTime.toLocalTime();
                            Time tempsfinTime = rs1.getTime("TempsFin");
                            LocalTime tempsfinact = tempsfinTime.toLocalTime();
                            while(tempscourant!=tempsfin){
                                if(tempsdebut.equals(tempscourant) ) {
                                    BorderStroke borderStroke = new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, null);
                                    Border border = new Border(borderStroke);
                                    String query2="SELECT * FROM salles WHERE SalleID=?";
                                    PreparedStatement ps2 = connect.prepareStatement(query2);
                                    ps2.setInt(1,rs1.getInt("SalleID"));
                                    ResultSet rs2 = ps2.executeQuery();
                                    if(rs2.next()){
                                        Text text = new Text(rs2.getString("NomSalle"));
                                        text.setFont(Font.font("Helvetica", 16)); 
                                        text.setFill(javafx.scene.paint.Color.WHITE);
                                        StackPane stack = new StackPane();
                                        stack.setPadding(new Insets(0));

                                        stack.setStyle("-fx-background-color: #1f3f75;-fx-background-radius: 5;");

                                        stack.getChildren().add(text);
                                        stack.setMaxWidth(121.6-2);
                                        stack.setMinWidth(121.6-2);
                                        stack.setMaxHeight(30.4);
                                        stack.setMinHeight(30.4);
                                        gridgroupe.add(stack,i,j);
                                        tempscourant=tempscourant.plusMinutes(30);
                                        j++;
                                    }
                                    do{
                                        StackPane stack = new StackPane();
                                        stack.setStyle("-fx-background-color:  #1f3f75");

                                        stack.setPadding(new Insets(0));
                                        stack.setMaxWidth(121.6-2);
                                        stack.setMinWidth(121.6-2);
                                        stack.setMaxHeight(30.4);
                                        stack.setMinHeight(30.4);
                                        gridgroupe.add(stack,i,j);
                                        tempscourant=tempscourant.plusMinutes(30);
                                        j++;
                                    }
                                    while(!tempscourant.equals(tempsfinact));
                                }else{
                                    tempscourant=tempscourant.plusMinutes(30);
                                    j++;
                                }
                            }
                        }
                    }
                }}
        }catch(SQLException e){
            e.printStackTrace();
        }}catch(SQLException e){
            e.printStackTrace();
        }
        System.out.println("text");
    }
    @FXML private GridPane gridcoach;
    @FXML private Button clickgroupe;
    @FXML private Button clickcoach;
    @FXML private TextField textchoix;
    //
   public void changeToCoaches( Event event) throws IOException{
      textchoix.setPromptText("Coach");
       ongroup=false;
    }
    public void changeToGroupes(Event event) throws IOException{
        textchoix.setPromptText("Groupe");
     ongroup=true;  
    }
public void afficherplanning(ActionEvent event) throws IOException{
    if(ongroup){
        showPlannerGroupe(event);
    }else{
        showPlannerCoach(event);
    }
}
public void genererPlanning2(MouseEvent event) throws IOException{
   Planning plan = new Planning();
   Alert confirmation = new Alert(AlertType.CONFIRMATION);
   confirmation.setTitle("Confirmation");
   confirmation.setHeaderText("Voulez-vous vraiment générer un nouveau planning?");
 
   
   // Charger l'icône
   Image icon = new Image(getClass().getResourceAsStream("pics/Confirmation.png"));
   ImageView imageView = new ImageView(icon);
   imageView.setFitWidth(48);
   imageView.setFitHeight(48);
   confirmation.setGraphic(imageView);
   
   // Appliquer du style CSS
   DialogPane dialogPane = confirmation.getDialogPane();
   dialogPane.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
   dialogPane.getStyleClass().add("my-alert");
   confirmation.showAndWait().ifPresent(response -> {
       if (response == ButtonType.OK) {
        plan.genererPlanning();
        showAlert(Alert.AlertType.INFORMATION, "Cofirmation", "un planning a été généré"); 
       }});
}
//echange des creneaux
public static boolean ongroup=true;
@FXML private TextField tempsd1;
@FXML private TextField tempsf1;
@FXML private TextField tempsd2;
@FXML private TextField tempsf2;
@FXML private TextField jourchange1;
@FXML private TextField jourchange2;
@FXML private ComboBox groupechoix;
boolean remplace=false;
static String  jour;
static String  mois;
static String  annees;

@FXML public TextField Annee;
@FXML public TextField Mois;
@FXML public TextField Jourr;
public void echangecreneaux(ActionEvent event){
 Planning plan = new Planning();
 try{
    Alert confirmation = new Alert(AlertType.CONFIRMATION);
   confirmation.setTitle("Confirmation");
   confirmation.setHeaderText("Voulez-vous vraiment effectuer ce changement ?");
 
   
   // Charger l'icône
   Image icon = new Image(getClass().getResourceAsStream("pics/Confirmation.png"));
   ImageView imageView = new ImageView(icon);
   imageView.setFitWidth(48);
   imageView.setFitHeight(48);
   confirmation.setGraphic(imageView);
   
   // Appliquer du style CSS
   DialogPane dialogPane = confirmation.getDialogPane();
   dialogPane.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
   dialogPane.getStyleClass().add("my-alert");
   confirmation.showAndWait().ifPresent(response -> {
       if (response == ButtonType.OK) {
        plan.genererPlanning();
       
       }});
remplace = plan.echangerCreneau(tempsd1.getText(), tempsf1.getText(), tempsd2.getText(), tempsf2.getText(), jourchange1.getText(), jourchange2.getText(), Groupe.getGroupeIDByNom(new Gestion().connectionBd(),textchoix.getText()));
if(remplace){
    showAlert(Alert.AlertType.CONFIRMATION, "Cofirmation", "Modification effectuée"); 
}
if(!remplace){
showAlert(Alert.AlertType.ERROR, "Chevauchement", "le creneau ciblé est occupé par un autre groupe , deplacez le d'abbord");
}
 }catch(SQLException e){
    e.printStackTrace();
 }
} 
    public void switchToPlanner1(MouseEvent event) throws IOException{
    
        root = FXMLLoader.load(getClass().getResource("planner.fxml"));
        gridgroupe= findGridPaneInRootgroupes(root);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        // filling the gaps
        stage.show();
        jour = String.valueOf(LocalDate.now().getDayOfMonth());
        mois = String.valueOf(LocalDate.now().getMonth());
       annees = String.valueOf(LocalDate.now().getYear());
       Annee.setText(annees);
       Mois.setText(mois);
       Jourr.setText(jour);
    }



    public void switchToScene6c(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("charges.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    //go to planification avancee page
    public void switchToScene7(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("Planification_avancee.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScene7a(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("plannification.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScene7b(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("affectation.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    //go to performance page
    public void switchToScene8(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("Performance.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScene8a(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("evaluation.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScene8b(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("voirPerformances.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    // basculer entre ajouter un adhérent et ajouter dossier médical

    @FXML
    private AnchorPane page1;

    @FXML
    private AnchorPane page2;

    @FXML
    public void switchToPage1() {
        page1.setVisible(true);
        page2.setVisible(false);
    }

    @FXML
    public void switchToPage2() {
        page1.setVisible(false);
        page2.setVisible(true);
    }

    public void switchToF(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("formulaire.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToF1(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("formulaire1.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToF2(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("formulaire2.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToS(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("voirAdherents.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToVoirCoachs(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("voirCoachs.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToVoirAdmins(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("voirAdmins.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToVoirSports(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("voirSports.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSportDetail(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("sportDetail.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToAjouterSport(MouseEvent event) throws IOException{
      
        root = FXMLLoader.load(getClass().getResource("ajouterSport.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToDetailAdherent(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("detailAdherent.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToDetailAdherent1(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("detailAdherent.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToDetailCoach(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("detailCoach.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    public void switchToS1(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("voirSalles.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToS2(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("voirMateriels.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    public void switchToF3(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("FormSalle.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToF4(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("FormMateriel.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToAjouterDossier(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("ajouterDossierMedical.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToDossier(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("dossierMedical.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    

    

    public void switchToCerteficat(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("certeficat.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToAjouterOffre(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("ajouterOffre.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToVoirOffre(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("voirOffres.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToVoirGroupes(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("voirGroupes.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCreneaux(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("crenau.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCreneaux1(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("crenau.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToPreference(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("prefrerence.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToPriorite(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("priorite.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToEvaluerAdherent(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("evaluerAdherent.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToEvaluerGroupe(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("evaluerGroupe.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    public void switchToAbsences(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("absences.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }



    public void switchToVoirTrophes(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("voirTrophes.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    //Controlling the password validation

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text passlabel;


    @FXML
    private void handlePasswordChange() {
        String password = passwordField.getText();

        // Example validation rules, you can modify them according to your requirements
        boolean isValid = isPasswordValid(password);

        if (isValid) {
            passlabel.setText("le mot de passe est valide");
        } else {
            passlabel.setText("le mot de passe n'est pas valide");
        }
    }

    private boolean isPasswordValid(String password) {
        // Check if the password is at least 8 characters long
        boolean isLengthValid = password.length() >= 8;

        // Check if the password contains at least one special character
        boolean hasSpecialCharacter = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");

        // Check if the password starts with a capital letter
        boolean startsWithCapital = Character.isUpperCase(password.charAt(0));

        // Combine all conditions to determine overall password validity
        boolean isValid = isLengthValid && hasSpecialCharacter && startsWithCapital;


        return isValid;
    }

//////////////////////////////////////////////////////////////////////////////

//----------------------------------------------------------------
    /////chosen card

    @FXML
    private Label age;

    @FXML
    private Label categorie;

    @FXML
    private Rectangle chosencard;

    @FXML
    private Button datails;

    @FXML
    private Label genre;

    @FXML
    private Label nom;

    @FXML
    private ImageView photo;

    @FXML
    private Label prenom;

    @FXML
    private Button supprimer;

    @FXML
    private GridPane grid;

    @FXML
    private ScrollPane scroll;


    private List<Card> cards = new ArrayList<>();
    private Image image;
    private MyListener myListener;


    private List<Card> getData() {
        List<Card> cards = new ArrayList<>();
        Card card;

        for (int i = 0; i < 50; i++) {
            card = new Card();
            card.setName("Lazib");
            card.setAge(19);
            card.setCategorie("karate");
            card.setGenre("---");
            card.setType("Adherent");
            card.setNameID("0000");
            card.setSurname("Malak");
            card.setImgSrc("userImage.png");
            cards.add(card);
        }

        return cards;
    }

/* 
    private void setchosencard(Card card) {
        nom.setText(card.getName());
        prenom.setText(card.getSurname());
        categorie.setText(card.getCategorie());
        genre.setText(card.getGenre());
        Image image = new Image(getClass().getResourceAsStream(card.getImgSrc()));
        photo.setImage(image);
    }
*/
    /*@Override*/

    /////////////////////////////////////////////////////ajouter-dossier-medical

        @FXML
        private Text filePathTextField;





    @FXML
    private TextField emailField;

    @FXML
    private Label emailV;



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





/****************************************************************************        */
// Champs FXML pour les différents éléments de l'interface graphique
@FXML
private ImageView picAdherentD;

@FXML
private Text nomAdherentD;

@FXML
private Text prenomAdherentD;

@FXML
private Text dateNaissanceAdherentD;

@FXML
private Text dateEntreeAdherentD;

@FXML
private Text genreAdherentD;

@FXML
private Text adresseAdherentD;

@FXML
private Text phoneAdherentD;

@FXML
private Text emailAdherentD;

@FXML
private Text nbAbscencesAdherentD1;




public void switchToDossier1(ActionEvent event) throws IOException, SQLException {
        
    FXMLLoader loader = new FXMLLoader(getClass().getResource("dossierMedical.fxml"));
    Parent root = loader.load();
    // Obtenir le contrôleur du détail de l'adhérent
    ConnexionController detailController = loader.getController();

    // Passer les données de la carte sélectionnée au contrôleur du détail de l'adhérent
    detailController.initialiserDossier(IDADH);

    // Remplacer la scène actuelle par la scène des détails de l'adhérent
    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    stage.setScene(new Scene(root, 1024, 760));
    stage.setResizable(false);
    stage.show();
}

public void switchToDossier2(ActionEvent event) throws IOException, SQLException {
        
    FXMLLoader loader = new FXMLLoader(getClass().getResource("dossier2.fxml"));
    Parent root = loader.load();
    // Obtenir le contrôleur du détail de l'adhérent
    ConnexionController detailController = loader.getController();

    // Passer les données de la carte sélectionnée au contrôleur du détail de l'adhérent
    detailController.initialiserDossier(IDADH);

    // Remplacer la scène actuelle par la scène des détails de l'adhérent
    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    stage.setScene(new Scene(root, 1024, 760));
    stage.setResizable(false);
    stage.show();
}


public void switchToDossier3(ActionEvent event) throws IOException, SQLException {
        
    FXMLLoader loader = new FXMLLoader(getClass().getResource("dossier3.fxml"));
    Parent root = loader.load();
    // Obtenir le contrôleur du détail de l'adhérent
    ConnexionController detailController = loader.getController();

    // Passer les données de la carte sélectionnée au contrôleur du détail de l'adhérent
    detailController.initialiserDossier(IDADH);

    // Remplacer la scène actuelle par la scène des détails de l'adhérent
    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    stage.setScene(new Scene(root, 1024, 760));
    stage.setResizable(false);
    stage.show();
}


@FXML
private Label GS;
@FXML
private Label MC;
@FXML
private Label B;

String filepath;

// Méthode pour initialiser les données de l'adhérent dans l'interface graphique
public void initialiserDossier(int adhID) throws SQLException {
    Connection connection = new Gestion().connectionBd();
    DossierMedical D= DossierMedical.getDossierMedicalByMembreID(connection, adhID);
    GS.setText(D.getGroupeSanguin());
    MC.setText(D.getMaladieChronique());
    B.setText(D.getBlessure());
    filepath = D.getCertificatHabilite();
    connection.close();
}


    @FXML
    private Text tropheNom;
    @FXML
    private Text tcompetition;
    @FXML
    private Text tsportNom;
    @FXML
    private Text tcoachNom;
    @FXML
    private Text tgagnantNom;

    public void initialiserTrophe(int TropheID) throws SQLException {
        Connection connection = new Gestion().connectionBd();
        Trophe adh= Trophe.getTropheByID(connection,TropheID);

        tropheNom.setText(adh.getTropheNom());
        tcompetition.setText(adh.getCompetitionNom());
        tsportNom.setText(Sport.getSportByID(connection, adh.getSportID()).getSportNom());

        Utilisateurs utilisateurs = new Utilisateurs();
        utilisateurs = Utilisateurs.recupererUtilisateurParID(connection,adh.getCoachID());
        tcoachNom.setText(utilisateurs.getNom()+ " "+utilisateurs.getPrenom());

        String winner;
        if (adh.getCategorie()==0){
            Utilisateurs utilisateurs1 = new Utilisateurs();
            utilisateurs1 = Utilisateurs.recupererUtilisateurParID(connection,adh.getAdherentGroupeID());
            winner = utilisateurs1.getNom() + " " + utilisateurs1.getPrenom();
        }
        else {
            Groupe groupe = new Groupe();
            groupe = Groupe.getGroupeByID(connection,adh.getAdherentGroupeID());
            winner = groupe.getNomGroupe();
        }
        tgagnantNom.setText(winner);


        // initialize
    }

static int IDADH;

@FXML
private ImageView qrphoto;
// Méthode pour initialiser les données de l'adhérent dans l'interface graphique
public void initialiserAdherent(int adhID) throws SQLException {
    Connection connection = new Gestion().connectionBd();
    IDADH=adhID;
    Adherent adh= Adherent.recupererAdherentParID(connection,adhID);
    nomAdherentD.setText(adh.getNom());
    prenomAdherentD.setText(adh.getPrenom());
    dateNaissanceAdherentD.setText(String.valueOf(adh.getDateNaissance()));
    dateEntreeAdherentD.setText(String.valueOf(adh.getDateEntree()));
    genreAdherentD.setText(adh.getGenre());
    adresseAdherentD.setText(adh.getAddress());
    phoneAdherentD.setText(adh.getTelephone());
    emailAdherentD.setText(adh.getEmail());
    nbAbscencesAdherentD1.setText(String.valueOf(Absence.getNombreAbsences(connection, adhID)));
    String picStream = adh.getPhoto();
    if (picStream != null) {
        if (Paths.get(picStream).isAbsolute()) {
        // C'est un chemin absolu
        File imageFile = new File(picStream);
        if (imageFile.exists()) {
            Image image = new Image(imageFile.toURI().toString());
            picAdherentD.setImage(image); // Définition de l'image sur l'ImageView pic
        } 
    }
}
if (adh.getQRcode() != null) {
    
        File imageFile = new File("./CodeQR/"+adh.getQRcode()+".png");
        if (imageFile.exists()) {
            Image image = new Image(imageFile.toURI().toString());
            qrphoto.setImage(image); // Définition de l'image sur l'ImageView pic
        }
        
}
     connection.close();   
}


// Méthode pour initialiser les données de coacht dans l'interface graphique
public void initialiserCoach(int coachID) throws SQLException {
    Connection connection = null;
    try {
        connection = new Gestion().connectionBd();
        IDADH = coachID;
        Coach adh = Coach.recupererCoachParID(connection, coachID);
        nomAdherentD.setText(adh.getNom());
        prenomAdherentD.setText(adh.getPrenom());
        dateNaissanceAdherentD.setText(String.valueOf(adh.getDateNaissance()));
        dateEntreeAdherentD.setText(String.valueOf(adh.getDateEntree()));
        genreAdherentD.setText(adh.getGenre());
        adresseAdherentD.setText(adh.getAddress());
        phoneAdherentD.setText(adh.getTelephone());
        emailAdherentD.setText(adh.getEmail());
        String picStream = adh.getPhoto();
        if (picStream != null) {
            if (Paths.get(picStream).isAbsolute()) {
                // C'est un chemin absolu
                File imageFile = new File(picStream);
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    picAdherentD.setImage(image); // Définition de l'image sur l'ImageView pic
                }
            }
            
        }

        if (adh.getQRcode() != null) {
    
            File imageFile = new File("./CodeQR/"+adh.getQRcode()+".png");
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                qrphoto.setImage(image); // Définition de l'image sur l'ImageView pic
        }
            
    }
    } finally {
        if (connection != null) {
            connection.close();
        }
    }
}



// Méthode pour initialiser les données de coacht dans l'interface graphique
public void initialiserStaff(int staffID) throws SQLException {
    Connection connection = null;
    try {
        connection = new Gestion().connectionBd();
        IDADH = staffID;
        Staff adh = Staff.recupererStaffParID(connection, staffID);
        nomAdherentD.setText(adh.getNom());
        prenomAdherentD.setText(adh.getPrenom());
        dateNaissanceAdherentD.setText(String.valueOf(adh.getDateNaissance()));
        dateEntreeAdherentD.setText(String.valueOf(adh.getDateEntree()));
        genreAdherentD.setText(adh.getGenre());
        adresseAdherentD.setText(adh.getAddress());
        phoneAdherentD.setText(adh.getTelephone());
        emailAdherentD.setText(adh.getEmail());
        String picStream = adh.getPhoto();
        if (picStream != null) {
            if (Paths.get(picStream).isAbsolute()) {
                // C'est un chemin absolu
                File imageFile = new File(picStream);
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString());
                    picAdherentD.setImage(image); // Définition de l'image sur l'ImageView pic
                }
            }
            
        }

        if (adh.getQRcode() != null) {
    
            File imageFile = new File("./CodeQR/"+adh.getQRcode()+".png");
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                qrphoto.setImage(image); // Définition de l'image sur l'ImageView pic
        }
            
    }
    } finally {
        if (connection != null) {
            connection.close();
        }
    }
}


    @FXML
    private void handleOpenFileButtonAction(ActionEvent event) {
        if (filepath != null && !filepath.isEmpty()) {
            // Ouvre le fichier PDF avec le programme par défaut associé à son extension
            try {
                ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "start", "\"\"", filepath);
                processBuilder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
@FXML
public void switchToDetailAdherent1(ActionEvent event) throws IOException, SQLException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("detailAdherent.fxml"));
    Parent root = loader.load();

    // Obtenir le contrôleur du détail du coach
    ConnexionController detailController = loader.getController();

    // Passer l'ID du coach sélectionné au contrôleur du détail du coach
    detailController.initialiserAdherent(IDADH);

    // Remplacer la scène actuelle par la scène des détails du coach
    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    stage.setScene(new Scene(root, 1024, 760));
    stage.setResizable(false);
    stage.show();
}
@FXML
public void switchToDetailCoach1(ActionEvent event) throws IOException, SQLException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("detailCoach.fxml"));
    Parent root = loader.load();

    // Obtenir le contrôleur du détail du coach
    ConnexionController detailController = loader.getController();

    // Passer l'ID du coach sélectionné au contrôleur du détail du coach
    detailController.initialiserCoach(IDADH);

    // Remplacer la scène actuelle par la scène des détails du coach
    Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    stage.setScene(new Scene(root, 1024, 760));
    stage.setResizable(false);
    stage.show();
}
// Champs FXML pour les différents éléments de l'interface graphique



@FXML
private Text categorieAct;
@FXML
private Text sportidAct;
@FXML
private Text nbGroupsAct;
@FXML
private Text coachesAct;


// Méthode pour initialiser les données de sport dans l'interface graphique
public void initialiserSport(int SportID) throws SQLException {
Connection connection = new Gestion().connectionBd();
Sport adh= Sport.getSportByID(connection,SportID);

categorieAct.setText(adh.getSportCategorie());
sportidAct.setText(adh.getSportNom());
nbGroupsAct.setText(String.valueOf(Gestion.getNombreGroupesPourSport(connection, SportID)));
try {
        List<Integer> coachIDs = Gestion.getCoachesDuSport(connection, SportID);
        String coachsString = new String("");

        for (int coachID : coachIDs) {
            // Récupérer les détails du coach à partir de la base de données
            Coach coach = Coach.recupererCoachParID(connection, coachID);
            if (coach != null) {
                coachsString=coachsString+(coach.getNom())+" "+(coach.getPrenom())+"\t";
            }
        }

        coachesAct.setText(coachsString);

    
    } catch (SQLException e) {
            e.printStackTrace();
            coachesAct.setText("Aucune Coach"); 
    }
   // initialize 
}
@FXML
private ComboBox<String> sportcatComboBox;
private ObservableList<String> items = FXCollections.observableArrayList();
   @Override
   public void initialize(URL location, ResourceBundle resources) {
       // txtNewCategory.setVisible(selectedCategory != null && selectedCategory.equals("Nouveau"));
    
}       






///controller le temps pour l'ajout des créneaux

@FXML
private TextField TD;

@FXML
private TextField TF;

@FXML
private Text hhD;

@FXML
private Text hhF;

    @FXML
    private TextField TD1;

    @FXML
    private TextField TF1;

    @FXML
    private Text hhD1;

    @FXML
    private Text hhF1;


    @FXML
    private TextField TD2;

    @FXML
    private TextField TF2;

    @FXML
    private Text hhD2;

    @FXML
    private Text hhF2;

@FXML
private void handleTimeChange() {
    String timeD = TD.getText();
    String timeF = TF.getText();

    boolean isValidD = isValidTime(timeD);
    boolean isValidF = isValidTime(timeF);

    if (isValidD) {
        hhD.setText("Format valide");
    } else {
        hhD.setText("Le format doit être hh:mm");
    }

    if (isValidF) {
        hhF.setText("Format valide");
    } else {
        hhF.setText("Le format doit être hh:mm");
    }

}

    @FXML
    private void handleTimeChange1() {

        String timeD1 = TD1.getText();
        String timeF1 = TF1.getText();

        boolean isValidD1 = isValidTime(timeD1);
        boolean isValidF1 = isValidTime(timeF1);

        if (isValidD1) {
            hhD1.setText("Format valide");
        } else {
            hhD1.setText("Le format doit être hh:mm");
        }

        if (isValidF1) {
            hhF1.setText("Format valide");
        } else {
            hhF1.setText("Le format doit être hh:mm");
        }

        String timeD2 = TD2.getText();
        String timeF2 = TF2.getText();

        boolean isValidD2 = isValidTime(timeD2);
        boolean isValidF2 = isValidTime(timeF2);

        if (isValidD2) {
            hhD2.setText("Format valide");
        } else {
            hhD2.setText("Le format doit être hh:mm");
        }

        if (isValidF2) {
            hhF2.setText("Format valide");
        } else {
            hhF2.setText("Le format doit être hh:mm");
        }
    }


    private boolean isValidTime(String time) {
        return time.matches("([01]\\d|2[0-3]):[0-5]\\d");
    }


//copie des formulaires adh/coach
//admin

//adh
@FXML private TextField userField;
public void ValidationAdmin(ActionEvent event){
    String user = userField.getText();
    String pass = passwordField.getText();
    if(!user.equals("") && !pass.equals("")){
    if(isPasswordValid(pass)){
    
    Alert confirmation = new Alert(AlertType.CONFIRMATION);
    confirmation.setTitle("Confirmation");
    confirmation.setHeaderText("Voulez-vous vraiment ajouter cet Admin?");
    confirmation.setContentText("Nom d'utilisateur : " + user + "\nMot de passe : " + pass);
    
    // Charger l'icône
    Image icon = new Image(getClass().getResourceAsStream("pics/Confirmation.png"));
    ImageView imageView = new ImageView(icon);
    imageView.setFitWidth(48);
    imageView.setFitHeight(48);
    confirmation.setGraphic(imageView);
    
    // Appliquer du style CSS
    DialogPane dialogPane = confirmation.getDialogPane();
    dialogPane.getStylesheets().add(getClass().getResource("Styles.css").toExternalForm());
    dialogPane.getStyleClass().add("my-alert");
    confirmation.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
        try{
            Gestion gestion = new Gestion();
            Connection connect = gestion.connectionBd();
    Admin admin = new Admin( user, pass);
    gestion.ajouterAdmin(connect, admin);
    System.out.println("admin added ");
    connect.close();
        
        }
        catch(SQLException e){
            e.printStackTrace();
            }
        }
    });
    }else{
        showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent ètre remplis convenablement");
    }
    
    }else{
    showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent ètre remplis convenablement");
    }
    }
@FXML
private TextField nameField;
@FXML
private TextField surnameField;
@FXML
private TextField categorieField;
@FXML
private TextField phoneField;
@FXML
private TextField genderField;
@FXML
private TextField adressField;
@FXML
private TextField TailleAdherentField;
@FXML
private  TextField PoidsAdherentField;
@FXML private  TextField chronique;

@FXML
private DatePicker dateNaissance;
@FXML
private DatePicker dateEntree;
@FXML private Button AjouterButtonAdherent; 



@FXML private ComboBox GenreAdherentA;
@FXML private ComboBox GroupeS;

public void ValidationAdherent(ActionEvent event) {

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
Membre membre = new Membre(0, 0, 0, name, fname, dateNN, 1, dateEE, gender, 0, email, adress, phone, poids, taille);

membre.setPhoto(cheminphoto);
Gestion gestion = new Gestion(); 
// Créer une boîte de dialogue de confirmation
Alert confirmation = new Alert(AlertType.CONFIRMATION);
confirmation.setTitle("Confirmation");
confirmation.setHeaderText("Voulez-vous vraiment ajouter cet Adhérent?");
confirmation.setContentText("Nom : " + name + "\nPrénom : " + fname);

// Charger l'icône
Image icon = new Image(getClass().getResourceAsStream("pics/Confirmation.png"));
ImageView imageView = new ImageView(icon);
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
                gestion.ajouterMembre(membre, connect);
                DossierMedical D = new DossierMedical(0,Utilisateurs.getDernierUserID(connect),GroupeS.getValue().toString(),chronique.getText(),blessure.getText(),cheminfichier);
                D.ajouterDossierMedical(connect);
                showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Adherent ajouté avec succès !");
                connect.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Impossible de se connecter à la base de données.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout du matériel.");
        }
    }
}
);

} catch (NumberFormatException e) {
showAlert(Alert.AlertType.ERROR, "Attention !!!", "La taille et le poids de l'adhérent doivent être un nombre");
}
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
 //coach
 // coach//
@FXML
private TextField cnameField;
@FXML
private TextField csurnameField;
@FXML
private TextField ccategorieField;
@FXML
private TextField cphoneField;
@FXML 
private TextField cemailField;
@FXML
private TextField cgenderField;
@FXML
private TextField cadressField;
@FXML
private TextField TailleCoachField;
@FXML
private  TextField PoidsCoachField;

@FXML
private DatePicker cdateNaissance;
@FXML
private DatePicker cdateEntree;
@FXML private Button AjouterButtonCoach; 
@FXML private ComboBox GenreCoachA;

@FXML private  TextField blessure;

public void ValidationCoach(ActionEvent event) {

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
 

     Coach coach = new Coach( name, fname, dateNN, gender,0, dateEE, email, adress, phone, poids, taille);
     coach.setPhoto(cheminphoto);
     
     // Créer une boîte de dialogue de confirmation
     Alert confirmation = new Alert(AlertType.CONFIRMATION);
     confirmation.setTitle("Confirmation");
     confirmation.setHeaderText("Voulez-vous vraiment ajouter ce(ette) Coach?");
     confirmation.setContentText("Nom : " + name + "\nPrénom : " + fname);
      Gestion gestion = new Gestion();
     // Charger l'icône
     Image icon = new Image(getClass().getResourceAsStream("pics/Confirmation.png"));
     ImageView imageView = new ImageView(icon);
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
                    gestion.ajouterCoach(coach, connect);
                    DossierMedical D = new DossierMedical(0,Utilisateurs.getDernierUserID(connect),GroupeS.getValue().toString(),chronique.getText(),blessure.getText(),cheminfichier);
                    D.ajouterDossierMedical(connect);
                    showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Adherent ajouté avec succès !");
                     connect.close();
                 } else {
                     showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Impossible de se connecter à la base de données.");
                 }
             } catch (SQLException e) {
                 showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout du Coach");
                 e.printStackTrace();
             }
         }
 }
 );
 } catch (NumberFormatException e) {
     showAlert(Alert.AlertType.ERROR, "Attention !!!", "La taille et le poids de l'adhérent doivent être un nombre");
 }
 }
// alert
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



public void switchToScene5b(MouseEvent event) throws IOException {
    root = FXMLLoader.load(getClass().getResource("voirEntrantSortant.fxml"));
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root, 1024, 760);
    HomeController.setTheme(scene);
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();
}

public void switchToFTr(MouseEvent event) throws IOException{
    root = FXMLLoader.load(getClass().getResource("FormTrophe.fxml"));
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root, 1024, 760);
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();
}

public void switchToScene8c(MouseEvent event) throws IOException{
    root = FXMLLoader.load(getClass().getResource("palmares.fxml"));
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root, 1024, 760);
    HomeController.setTheme(scene);
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();
}

public void switchtoVTr(MouseEvent event) throws IOException{
    root = FXMLLoader.load(getClass().getResource("voirTrophes.fxml"));
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root, 1024, 760);
    HomeController.setTheme(scene);
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();
}


// page Acceuil:

       //si on veut clicker sur une image ( OnMouseClicked="#...." )
    @FXML
       public void switchToHom(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("homePage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
       public void switchToHome(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("homePage.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Connection conn = new Gestion().connectionBd();
        Gestion.initMail(conn);
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
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }



    public void ajouterScannedQRCode(MouseEvent event)  {
        String qrCodeText = scanQRCode(event);
        if (qrCodeText != null) {
            Connection connection = new Gestion().connectionBd();
            // Display the scanned QR code text in a new alert dialog
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("QR Code Scanned");
            alert.setHeaderText(null);
            alert.setContentText("QR Code: " + qrCodeText);
            alert.showAndWait();
            Gestion.ajouterEntrantSortantBD(connection, qrCodeText);
        }
    }

    private String scanQRCode(MouseEvent event) {
        // Create a new stage and scene to display the webcam feed
        Stage webcamStage = new Stage();
        ImageView webcamView = new ImageView();
        Pane pane = new Pane(webcamView); // Wrap ImageView in a Pane
        Scene webcamScene = new Scene(pane);
        webcamStage.setScene(webcamScene);
        webcamStage.show();

        // Open the PC's camera and start scanning for a QR code
        Webcam webcam = Webcam.getDefault();
        webcam.open();
        QRCodeReader qrCodeReader = new QRCodeReader();
        String qrCodeText = null;
        while (true) {
            BufferedImage image = webcam.getImage();
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                Result result = qrCodeReader.decode(bitmap);
                qrCodeText = result.getText();
                System.out.println("QR Code: " + qrCodeText);
                break;
            } catch (NotFoundException | ChecksumException | FormatException e) {
                // Handle exceptions
            }
        }
        webcam.close();
        return qrCodeText;
    }

    private WritableImage bufferedImageToFXImage(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int argb = bufferedImage.getRGB(x, y);
                pixelWriter.setPixels(x, y, 1, 1, PixelFormat.getIntArgbInstance(), new int[] { argb }, 0, 1);
            }
        }
        return writableImage;
    }




    // search bar

@FXML
private AnchorPane mainPane;

@FXML
private TextField searchField;

@FXML
private void handleSearch() {
    String searchText = searchField.getText().trim().toLowerCase();

    // Reset styles
    resetTextStyles(mainPane.getChildren());

    if (!searchText.isEmpty()) {
        highlightText(mainPane.getChildren(), searchText);
    }
}

private void resetTextStyles(ObservableList<Node> nodes) {
    for (Node node : nodes) {
        if (node instanceof Text) {
            ((Text) node).setStyle(""); // Reset to default style
        }
    }
}

private void highlightText(ObservableList<Node> nodes, String searchText) {
    for (Node node : nodes) {
        if (node instanceof Text) {
            Text text = (Text) node;
            if (text.getText().toLowerCase().contains(searchText)) {
                text.setStyle("-fx-fill: Blue;"); // Highlight style
            }
        }
    }
}






}