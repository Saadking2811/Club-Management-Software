package com.club.Controller;

import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import java.sql.*;
import javafx.scene.chart.PieChart;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

import com.club.Model.Evenement;
import com.club.Model.Gestion;
import com.club.Model.Groupe;
import com.jfoenix.controls.JFXToggleButton;
public class HomeController implements Initializable {


    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private GridPane grid;

    private List<Evenement> cartes;


    @FXML
    private JFXToggleButton toggleButton;
  

    static public String cheminlogo;

    @FXML
    private void addlogo(ActionEvent event) {
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
        cheminlogo = selectedFile.getAbsolutePath();
        
        // Utiliser le chemin du fichier sélectionné (par exemple, l'afficher)
        System.out.println("Chemin du fichier sélectionné : " + cheminlogo);
    }
}


public void switchtoajoutermail(MouseEvent event) throws IOException{
    root = FXMLLoader.load(getClass().getResource("Mail.fxml"));
    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    Scene scene = new Scene(root, 1024, 760);
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();

}




        

        static boolean selected ;
        @FXML
        public void toggleButtonStateChanged(ActionEvent event) {
            //ThemeManager.setDarkTheme(scene, toggleButton);
            if (toggleButton.isSelected()) {
                selected = true;
                try {
                    switchToHom(event);
                } catch (IOException e) {
                    // Handle the exception here
                    e.printStackTrace();
                }
            } else {
                // Si le bouton est désélectionné, vous pouvez restaurer le thème par défaut ici.
                
                 selected = false;
                 try {
                    switchToHom(event);
                } catch (IOException e) {
                    // Handle the exception here
                    e.printStackTrace();
                }
            }
        }
    


    public static void setTheme(Scene s) {
        //ThemeManager.setDarkTheme(scene, toggleButton);
        String darkBackgroundColor = "-fx-background-color: #0a113d;";
        String darkGradientBackgroundColor = "-fx-background-color: linear-gradient(to bottom left, #C7DCEE, #F3F4D6, #E0F0DF, #BBD3F5);";

        if (selected) {
            s.getRoot().setStyle(darkBackgroundColor );
        } else {
            // Si le bouton est désélectionné, vous pouvez restaurer le thème par défaut ici.
            
             s.getRoot().setStyle(darkGradientBackgroundColor);
        }
    }
    
    private List<CarteEvenementController> cardControllers = new ArrayList<>();
    public void initMail(){
        try{
        String query="SELECT * FROM mails WHERE Supprime=0";
        PreparedStatement ps =new Gestion().connectionBd().prepareStatement(query);
        ResultSet rs =ps.executeQuery();
        Gestion.mail=rs.getString("Mail");
        Gestion.pass=rs.getString("Password");
    }catch(SQLException e){
        e.printStackTrace();
    }
}
    //go to connexion page
    public void switchToScene1(MouseEvent event) throws IOException {
        initMail();
        root = FXMLLoader.load(getClass().getResource("connexion-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

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

    //go to Gestion des profils page using a button (from connexion page)
    public void switchToScene2(ActionEvent event) throws IOException{
        initMail();
        root = FXMLLoader.load(getClass().getResource("gestionProfil.fxml"));
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
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    //go to Gestion des profils-Admin page
    public void switchToScene2a(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("gestionProfilAdmin.fxml"));
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
        initMail();
        root = FXMLLoader.load(getClass().getResource("Paiement.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScene6a(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("validation.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
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


    public void switchToPlanner1(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("planner1.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    public void switchToScene6c(MouseEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("revenues.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
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




    @FXML
    private AnchorPane pane;

    @FXML
    private Button media;

    @FXML
    private AnchorPane pane1;

    @FXML
    private Button notice;

    @FXML
    private ImageView settingsIcon;

    boolean flag = true;
    boolean flag1 = true;

    @FXML
    private PieChart pieChart;

    private double x = 0,y = 0;

    @FXML
    private LineChart<?, ?> lineChart;

    @FXML
    private AnchorPane sideBar;

    @FXML
    private GridPane calendarGridPane;

    @FXML
    private Label monthLabel;

    @FXML
    private Label yearLabel;

    @FXML
    private BarChart<String, Number> barChart;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //settings icon rotation
        toggleButton.setSelected(selected);
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(2), settingsIcon);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.setAutoReverse(false);
        rotateTransition.play();

        //media
        pane.setVisible(false);
        //notification
        pane1.setVisible(false);

        //pieChart
   // Populate PieChart with data
   Gestion gest=new Gestion(); 
   if (gest.nbrAdherent()!=0){
   PieChart.Data slice1 = new PieChart.Data("Homme", (gest.nbrHomme()*100)/gest.nbrAdherent());
   PieChart.Data slice2 = new PieChart.Data("Femme", (gest.nbrFemme()*100)/gest.nbrAdherent());
        
   // Set custom colors for each segment
       
   
  
   if (pieChart != null) {
    pieChart.getData().addAll(slice1, slice2);
    slice1.getNode().setStyle("-fx-pie-color:  #1f3f75;");
    slice2.getNode().setStyle("-fx-pie-color: #FFC0CB;");
    } else {
    System.out.println("Pie chart object is null. Make sure it is properly injected.");
    }
   }



   
       
        // Hide the legend
        pieChart.setLegendVisible(false);

///
LocalDate today = LocalDate.now();
List<LocalDate> previousDates = new ArrayList<>();
        
        // Format for displaying the dates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // Add the previous four dates to the list
        for (int i = 1; i <= 4; i++) {
            LocalDate previousDate = today.minusDays(i);
            previousDates.add(0, previousDate); // Adding at index 0 to reverse the order
        }
        // lineChart
        lineChart.getXAxis().setStyle("-fx-text-fill: white;");
        lineChart.getXAxis().setLabel("Date");
        lineChart.getYAxis().setLabel("Nombre D'adhérent");

        XYChart.Series series1 = new XYChart.Series();
System.out.println(gest.evolution(previousDates.get(0)));
System.out.println(gest.evolution(previousDates.get(0)));

        series1.getData().add(new XYChart.Data(previousDates.get(0).toString(),gest.evolution(previousDates.get(0))));
        series1.getData().add(new XYChart.Data(previousDates.get(1).toString(),gest.evolution(previousDates.get(1))));
        series1.getData().add(new XYChart.Data(previousDates.get(2).toString(),gest.evolution(previousDates.get(2))));
        series1.getData().add(new XYChart.Data(previousDates.get(3).toString(),gest.evolution(previousDates.get(3))));
        

        
        lineChart.getData().addAll(series1);


        //calendar

        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        int dayOfMonth = 1;

// Set the current date to the first day of the month
        currentDate = LocalDate.of(year, month, dayOfMonth);
        int numDays = currentDate.lengthOfMonth();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd");

// Get the day of the week for the first day of the month
        DayOfWeek firstDayOfMonth = currentDate.withDayOfMonth(1).getDayOfWeek();
        int startDayOfWeek = firstDayOfMonth.getValue() % 7;

        int row = 1;
        int column = startDayOfWeek; // Adjust the starting column based on the day of the week

// Set the month and year labels
        monthLabel.setText(currentDate.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        yearLabel.setText(String.valueOf(currentDate.getYear()));

// Loop through the days of the month and add labels to the GridPane
        for (int i = 1; i <= numDays; i++) {
            Label label = new Label(dateFormatter.format(currentDate.withDayOfMonth(i)));
            label.setMinWidth(30); // Set the minimum width for the label
            label.setMinHeight(30); // Set the minimum height for the label
            label.setStyle("-fx-text-fill: #FFFFFF; -fx-font-weight: bold"); // Set the text color to white
            if (currentDate.withDayOfMonth(i).equals(LocalDate.now())) {
                label.setStyle("-fx-text-fill: #000000; -fx-font-weight: bold");
            }
            calendarGridPane.add(label, column, row);

            // Move to the next column if the current column is the last column
            if (column == 6) {
                column = 0;
                row++;
            } else {
                column++;
            }
        }


        // Bar chart

        XYChart.Series<String, Number> ser = new XYChart.Series<>();
        ser.setName("   Nombre d'adhérents par nombre d'abonnement");

        // Add data to series
        ser.getData().add(new XYChart.Data<>("0", gest.nbrAdherent()-gest.nbrAbonnements(1)-gest.nbrAbonnements(2)-gest.nbrAbonnements(3)-gest.nbrAbonnements(4)));
        ser.getData().add(new XYChart.Data<>("1", gest.nbrAbonnements(1)));
        ser.getData().add(new XYChart.Data<>("2",gest.nbrAbonnements(2) ));
        ser.getData().add(new XYChart.Data<>("3",gest.nbrAbonnements(3) ));

        barChart.getData().add(ser);

        // Change bar color and synchronize legend color
        String barColor = "#1f3f75";
        ser.getData().forEach(data -> {
            Node node = data.getNode();
            node.setStyle("-fx-bar-fill: " + barColor + ";");
        });

        barChart.applyCss();
        barChart.layout();

        // Adjust legend color
        for (Node n : barChart.lookupAll(".chart-legend-item-symbol")) {
            if (n instanceof StackPane) {
                n.setStyle("-fx-background-color: " + barColor + ", white;");
            }
        }


        // Evenements


        populateCards();
        startTimer();




    }


    @FXML
    private void clicked_on_media(ActionEvent event){
        if (flag){
            pane.setVisible(true);
            flag=false;
        } else{
            pane.setVisible(false);
            flag=true;
        }
    }

    @FXML
    private void clicked_on_notice(ActionEvent event){
        if (flag1){
            pane1.setVisible(true);
            flag1=false;
        } else{
            pane1.setVisible(false);
            flag1=true;
        }
    }

    @FXML
    private void clicked_on_notice1(MouseEvent event){
        if (flag1){
            pane1.setVisible(true);
            flag1=false;
        } else{
            pane1.setVisible(false);
            flag1=true;
        }
    }



    private List<Evenement> cartes() {
        List<Evenement> ls = new ArrayList<>();

        Evenement carte = new Evenement();
        carte.setNomSport("Karate");
        carte.setNomEvenement("Competition");
        carte.setDateEvenement("14/05/2024");
        ls.add(carte);

        carte = new Evenement();
        carte.setNomSport("Karate");
        carte.setNomEvenement("Competition1");
        carte.setDateEvenement("14/06/2024");
        ls.add(carte);

        carte = new Evenement();
        carte.setNomSport("Judo");
        carte.setNomEvenement("Competition2");
        carte.setDateEvenement("18/08/2024");
        ls.add(carte);

        return ls;
    }



    @FXML
    private StackPane containerPane;

    private int currentIndex = 0;



    private void populateCards() {
        cartes = cartes(); // This should fetch your cards
        if (!cartes.isEmpty()) {
            showCard(currentIndex); // Show the first card
        }
    }

    private void showCard(int index) {
        Evenement evenement = cartes.get(index);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/club/Controller/EventCard.fxml"));
            AnchorPane cardRoot = fxmlLoader.load();
            CarteEvenementController controller = fxmlLoader.getController();
            controller.setData(evenement);
            containerPane.getChildren().setAll(cardRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showNextCard() {
        currentIndex = (currentIndex + 1) % cartes.size();
        showCard(currentIndex);
    }

    @FXML
    private void showPreviousCard() {
        currentIndex = (currentIndex - 1 + cartes.size()) % cartes.size();
        showCard(currentIndex);
    }



    private Timer timer;

    private void startTimer() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    showNextCard(); // This will cycle through the cards
                });
            }
        };
        long delay = 3000; // Delay in milliseconds before task is to be executed
        long period = 5000; // Time in milliseconds between successive task executions.
        timer.scheduleAtFixedRate(task, delay, period);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @FXML
    private void Sauvegarder(ActionEvent event){

        // Vérifier si cmbG est vide
        if (cheminlogo == null) {
            showAlert(Alert.AlertType.WARNING, "Attention", "Veuillez d'abord ajouter un logo.");
            return; // Arrêter le traitement si la combobox est vide
        }

        
        
                // Si l'utilisateur a cliqué sur "OK", ajoutez le matériel et la dépense associée
                try {
                    Connection connection = new Gestion().connectionBd();
                    
                    if (connection != null) {
                        Gestion gestion = new Gestion();
                        gestion.supprimerlogo(connection);
                        gestion.ajouterlogo(connection, cheminlogo);
                        showAlert(Alert.AlertType.INFORMATION, "Confirmation", "Logo ajouté avec succès !");
                        connection.close();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Impossible de se connecter à la base de données.");
                    }
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'ajout du logo.");
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

}


