package com.club.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class DepensesJ {
    private int materielID;
    private String temps;
    private boolean supprime; // Champ pour la suppression logique
    private double somme;

    // Constructeur avec le champ supprime
    public DepensesJ(int materielID, String temps, boolean supprime,double somme) {
        this.materielID = materielID;
        this.temps = temps;
        this.supprime = supprime;
        this.somme=somme;
    }

    // Getters et Setters
    public int getMaterielID() {
        return materielID;
    }

    public void setMaterielID(int materielID) {
        this.materielID = materielID;
    }

    public String getTemps() {
        return temps;
    }
    public double getSomme() {
        return somme;
    }
    public void setTemps(String temps) {
        this.temps = temps;
    }

    public boolean isSupprime() {
        return supprime;
    }

    public void setSupprime(boolean supprime) {
        this.supprime = supprime;
    }
 public void setSomme(double somme) {
     this.somme = somme;
 }
    // Méthodes pour manipuler la base de données (ajouter, récupérer, mettre à jour, supprimer)

    // Méthode pour ajouter une dépense journalière à la base de données
    public void ajouterDepenseJ(Connection connection) throws SQLException {
        String query = "INSERT INTO depensesj (MaterielID, Temps,Somme) VALUES (?, ?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.materielID);
            preparedStatement.setString(2, this.temps);
            preparedStatement.setDouble(3,this.somme);
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour récupérer les dépenses journalières à partir de l'ID du matériel
    public static DepensesJ getDepensesJByID(Connection connection, int materielID) throws SQLException {
        String query = "SELECT * FROM depensesj WHERE MaterielID = ? AND Supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, materielID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String temps = resultSet.getString("Temps");
                    boolean supprime = resultSet.getBoolean("Supprime");
                    double somme = resultSet.getDouble("Somme");
                    return new DepensesJ(materielID, temps, supprime,somme);
                }
            }
        }
        return null;
    }

    // Méthode pour mettre à jour une dépense journalière dans la base de données
    public void mettreAJourDepenseJ(Connection connection) throws SQLException {
        String query = "UPDATE depensesj SET Temps = ? WHERE MaterielID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, this.temps);
            preparedStatement.setInt(2, this.materielID);
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour supprimer une dépense journalière de la base de données (suppression logique)
    public void supprimerDepenseJ(Connection connection) throws SQLException {
        String query = "UPDATE depensesj SET Supprime = true WHERE MaterielID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.materielID);
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour obtenir l'ID du dernier matériel inséré
    public static int getDernierMaterielID(Connection connection) throws SQLException {
        int dernierMaterielID = -1;
        String query = "SELECT LAST_INSERT_ID() AS last_id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    dernierMaterielID = resultSet.getInt("last_id");
                }
            }
        }
        return dernierMaterielID;
    }

    public static List<DepensesJ> getListeRevenuesJ(Connection connection) {
        List<DepensesJ> listeRevenuesJ = new ArrayList<>();
        String query = "SELECT * FROM depensesj WHERE Supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int membreID = resultSet.getInt("MaterielID");
                    double somme = resultSet.getDouble("Somme");
                   
                    String date = resultSet.getString("Temps");
                 
                   DepensesJ rv = new DepensesJ(membreID, date, false,somme);
                    listeRevenuesJ.add(rv);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listeRevenuesJ;
    }
}
