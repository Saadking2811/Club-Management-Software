package com.club.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Sport {
    private int sportID;
    private String sportCategorie;
    private String sportNom;
    private boolean supprime; // Champ pour la suppression logique

    public Sport(){}
    // Constructeur avec le champ supprime
    public Sport(int sportID, String sportCategorie, String sportNom, boolean supprime) {
        this.sportID = sportID;
        this.sportCategorie = sportCategorie;
        this.sportNom = sportNom;
        this.supprime = supprime;
    }

    // Getters et Setters
    public int getSportID() {
        return sportID;
    }

    public void setSportID(int sportID) {
        this.sportID = sportID;
    }

    public String getSportCategorie() {
        return sportCategorie;
    }

    public void setSportCategorie(String sportCategorie) {
        this.sportCategorie = sportCategorie;
    }

    public String getSportNom() {
        return sportNom;
    }

    public void setSportNom(String sportNom) {
        this.sportNom = sportNom;
    }

    public boolean isSupprime() {
        return supprime;
    }

    public void setSupprime(boolean supprime) {
        this.supprime = supprime;
    }

    // Méthodes pour manipuler la base de données (ajouter, récupérer, mettre à jour, supprimer)

    // Méthode pour ajouter un sport à la base de données
    public void ajouterSport(Connection connection) throws SQLException {
        String query = "INSERT INTO sports (SportCategorie, SportNom) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, this.sportCategorie);
            preparedStatement.setString(2, this.sportNom);
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour récupérer un sport par ID
    public static Sport getSportByID(Connection connection, int sportID) throws SQLException {
        String query = "SELECT * FROM sports WHERE SportID = ? AND Supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, sportID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String sportCategorie = resultSet.getString("SportCategorie");
                    String sportNom = resultSet.getString("SportNom");
                    boolean supprime = resultSet.getBoolean("Supprime");
                    return new Sport(sportID, sportCategorie, sportNom, supprime);
                }
            }
        }
        return null;
    }

    // Méthode pour mettre à jour un sport dans la base de données
    public void mettreAJourSport(Connection connection) throws SQLException {
        String query = "UPDATE sports SET SportCategorie = ?, SportNom = ? WHERE SportID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, this.sportCategorie);
            preparedStatement.setString(2, this.sportNom);
            preparedStatement.setInt(3, this.sportID);
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour supprimer un sport de la base de données (suppression logique)
    public void supprimerSport(Connection connection) throws SQLException {
        String query = "UPDATE sports SET Supprime = true WHERE SportID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.sportID);
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour récupérer la liste des sports non supprimés
    public static List<Sport> getListeSports(Connection connection) {
        List<Sport> listeSports = new ArrayList<>();
        String query = "SELECT * FROM sports WHERE Supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int sportID = resultSet.getInt("SportID");
                    String sportCategorie = resultSet.getString("SportCategorie");
                    String sportNom = resultSet.getString("SportNom");
                    boolean supprime = resultSet.getBoolean("Supprime");
                    Sport sport = new Sport(sportID, sportCategorie, sportNom, supprime);
                    listeSports.add(sport);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listeSports;
    }

    // Méthode pour récupérer l'ID d'un sport par son nom
    public static int getSportIDByNom(Connection connection, String sportNom) throws SQLException {
        String query = "SELECT SportID FROM sports WHERE SportNom = ? AND Supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, sportNom);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("SportID");
                }
            }
        }
        return -1;
    }
}
