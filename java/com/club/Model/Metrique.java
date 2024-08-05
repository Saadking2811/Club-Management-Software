package com.club.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Metrique {
    private int metriqueID;
    private String metriqueNom;
    private int sportID;
    private boolean supprime;

    
    public Metrique(String metriqueNom, int sportID) {
        this.metriqueNom = metriqueNom;
        this.sportID = sportID;
        this.supprime = false; // Par défaut, la métrique n'est pas supprimée
    }

    public Metrique() {

    }

    public static List<Metrique> recupererToutesLesMetriques(Connection connection) throws SQLException {
        List<Metrique> metriquesList = new ArrayList<>();
        String query = "SELECT * FROM metriques WHERE Supprime = false";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Metrique metrique = new Metrique("",0);
                metrique.setMetriqueID(resultSet.getInt("MetriqueID"));
                metrique.setMetriqueNom(resultSet.getString("MetriqueNom"));
                metrique.setSportID(resultSet.getInt("SportID"));
                metrique.setSupprime(resultSet.getBoolean("Supprime"));
                metriquesList.add(metrique);
            }
        }
        return metriquesList;
    }

    public static List<String> recupererMetriquesSport(Connection connection, int sportID) throws SQLException {
        List<String> metriques = new ArrayList<>();
        String query = "SELECT MetriqueNom FROM metriques WHERE SportID = ? AND Supprime = false";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, sportID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String metriqueNom = resultSet.getString("MetriqueNom");
                    metriques.add(metriqueNom);
                }
            }
        }
        return metriques;
    }
    // Méthode pour effectuer la suppression logique
    public void supprimerMetrique(Connection connection) throws SQLException {
        String query = "UPDATE metriques SET Supprime = true WHERE MetriqueID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, this.metriqueID);
            statement.executeUpdate();
            this.supprime = true;
        }
    }

    // Méthode pour insérer une nouvelle métrique
    public void insererMetrique(Connection connection) throws SQLException {
        String query = "INSERT INTO metriques (MetriqueNom, SportID, Supprime) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, this.metriqueNom);
            statement.setInt(2, this.sportID);
            statement.setBoolean(3, this.supprime);
            statement.executeUpdate();
        }
    }

    // Méthode pour récupérer l'ID d'une métrique à partir de son nom
    public static int getMetriqueIDParNom(Connection connection, String nomMetrique) throws SQLException {
        int metriqueID = -1; // Valeur par défaut si la métrique n'est pas trouvée

        String query = "SELECT MetriqueID FROM metriques WHERE MetriqueNom = ? AND Supprime = false";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nomMetrique);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    metriqueID = resultSet.getInt("MetriqueID");
                }
            }
        }

        return metriqueID;
    }
    public int getMetriqueID() {
        return metriqueID;
    }

    public void setMetriqueID(int metriqueID) {
        this.metriqueID = metriqueID;
    }

    public String getMetriqueNom() {
        return metriqueNom;
    }

    public void setMetriqueNom(String metriqueNom) {
        this.metriqueNom = metriqueNom;
    }

    public int getSportID() {
        return sportID;
    }

    public void setSportID(int sportID) {
        this.sportID = sportID;
    }

    public boolean isSupprime() {
        return supprime;
    }

    public void setSupprime(boolean supprime) {
        this.supprime = supprime;
    }
}
