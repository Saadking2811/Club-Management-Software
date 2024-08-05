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

import com.club.Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class AbsenceController implements Initializable {

    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField heureField;

    //*******************************
    @FXML
    private ListView<String> absentListView;
    @FXML
    private TextField txtjustification;
    @FXML
    private ComboBox<String> cmbGroupeA;
    @FXML
    private ComboBox<String> cmbSeanceA;

    private ObservableList<String> adhérents;

    //********************************
    private Map<String, Integer> groupeMap = new HashMap<>();
    private Map<String, Integer> seanceMap = new HashMap<>();
    private Map<String, Integer> absenceMap = new HashMap<>();

    //********************************
    private Map<String, String> cellStates = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Connection connection = new Gestion().connectionBd();

        List<Groupe> listGroupe = new ArrayList<>();
        List<Seances> listSeance = new ArrayList<>();

        List<String> stringGroupe = new ArrayList<>();
        List<String> stringSeance = new ArrayList<>();

        cmbGroupeA.getItems().addAll(populateGroupesCombobox());

        cmbGroupeA.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            cmbSeanceA.getItems().clear(); // Clear the seancesComboBox
            //absentListView.getItems().clear(); // Clear the absentListView
            if (newValue != null) {
                populateSeancesCombobox(newValue); // Populate the seancesComboBox based on the selected group
            }
        });

        cmbSeanceA.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                List<String> absences = populateAbsenceListView(newValue); // Get the list of absences
                ObservableList<String> observableAbsences = FXCollections.observableArrayList(absences); // Convert it to an ObservableList
                absentListView.setItems(observableAbsences); // Set the ObservableList to the ListView
            }
        });

        absentListView.setCellFactory(param -> new ListCell<String>() {
            private TextField textField = new TextField();
            private Button saveButton = new Button("Save");
            private Button modifyButton = new Button("Modify");
            private HBox hbox = new HBox(textField, saveButton);
            {
                hbox.setAlignment(Pos.CENTER); // Center the items in the HBox
                hbox.setSpacing(10); // Add spacing between the items
                saveButton.setOnAction(event -> {
                    String item = getItem();
                    String text = textField.getText();
                    setText(item + " - Justifié le " + LocalDate.now() + " - " + textField.getText());
                    cellStates.put(item, text);
                    setGraphic(modifyButton);

                    String insertedText = textField.getText();

                    try {
                        Connection connection1 = new Gestion().connectionBd();
                        if (connection1 != null) {
                            Absence absence = new Absence();
                            absence.setMembreID(absenceMap.get(item));
                            absence.setSeanceID(seanceMap.get(cmbSeanceA.getValue()));
                            absence.setJustification(insertedText);
                            absence.mettreAJourAbsence(connection1);
                            connection1.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                modifyButton.setOnAction(event -> {
                    // Set the text of the textField to the saved text
                    if (cellStates.containsKey(getItem())) {
                        textField.setText(cellStates.get(getItem()));
                    }
                    setGraphic(hbox); // Show the textField and "Save" button for modification
                });
                absentListView.setFixedCellSize(50);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (cellStates.containsKey(item)) {
                        setText(item + " - Justifié le " + LocalDate.now() + " - " + cellStates.get(item));
                        setGraphic(modifyButton); // Show the "Modify" button if text is saved
                    } else {
                        Button justifyButton = new Button("Justifier");
                        justifyButton.setOnAction(event -> {
                            textField.setText("");
                            setGraphic(hbox);
                        });
                        setText(item);
                        setGraphic(justifyButton);
                    }
                }
            }
        });
    }

    public List<String> populateGroupesCombobox(){
        Connection connection = new Gestion().connectionBd();
        List<Groupe> listGroupe = new ArrayList<>();
        List<String> stringGroupe = new ArrayList<>();
        listGroupe = Gestion.getListeGroupes(connection);
        for (Groupe groupe : listGroupe){
            stringGroupe.add(groupe.getNomGroupe());
        }
        for (Groupe groupe : listGroupe){
            // Assuming MyObject has getStringValue() and getIntegerValue() methods
            groupeMap.put(groupe.getNomGroupe(),groupe.getGroupeId());
        }
        return stringGroupe;
    }

    public void populateSeancesCombobox(String groupeselected){
        Connection connection = new Gestion().connectionBd();
        List<Seances> listSeance = new ArrayList<>();
        List<String> stringSeance = new ArrayList<>();
        listSeance = Gestion.recupererTousLesSeancesParGroupe(connection, groupeMap.get(groupeselected));
        for (Seances seance : listSeance){
            stringSeance.add(String.valueOf(seance.getTemps()));
        }
        for (Seances seance : listSeance){
            // Assuming MyObject has getStringValue() and getIntegerValue() methods
            seanceMap.put(String.valueOf(seance.getTemps()),seance.getSeanceID());
        }
        cmbSeanceA.getItems().addAll(stringSeance);
    }

    public List<String> populateAbsenceListView(String seanceselected){
        Connection connection = new Gestion().connectionBd();
        List<Absence> listAbsence = new ArrayList<>();
        List<String> stringAbsence = new ArrayList<>();
        listAbsence = Gestion.recupererTousLesAbsencesParSeance(connection, seanceMap.get(seanceselected));
        for (Absence absence : listAbsence){
            Utilisateurs utilisateurs = new Utilisateurs();
            try {
                utilisateurs = Utilisateurs.recupererUtilisateurParID(connection, absence.getMembreID());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            stringAbsence.add(utilisateurs.getNom() + " " + utilisateurs.getPrenom());
        }
        for (Absence absence : listAbsence){
            Utilisateurs utilisateurs = new Utilisateurs();
            try {
                utilisateurs = Utilisateurs.recupererUtilisateurParID(connection, absence.getMembreID());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            // Assuming MyObject has getStringValue() and getIntegerValue() methods
            absenceMap.put(utilisateurs.getNom() + " " + utilisateurs.getPrenom(),absence.getMembreID());
        }
        return stringAbsence;
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToScene5(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("presences.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1024, 760);
        HomeController.setTheme(scene);
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