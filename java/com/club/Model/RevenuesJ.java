package com.club.Model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RevenuesJ {
    private int membreID;
    private String temps;
    private double somme;
    // Constructeur
    public RevenuesJ(int membreID, String temps,double somme) {
        this.membreID = membreID;
        this.temps = temps;
        this.somme =somme;
    }

    // Getters et Setters
    public int getMembreID() {
        return membreID;
    }

    public void setMembreID(int membreID) {
        this.membreID = membreID;
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
    public void setSomme(double somme) {
        this.somme = somme;
    }

    // Méthodes pour manipuler la base de données (ajouter, récupérer, mettre à jour, supprimer)

    // Méthode pour ajouter un revenu journalier à la base de données
    public void ajouterRevenuJ(Connection connection) throws SQLException {
        String query = "INSERT INTO revenuesj (MembreID, Temps,Somme) VALUES (?, ?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.membreID);
            preparedStatement.setString(2, this.temps);
            preparedStatement.setDouble(3, this.somme);
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour récupérer les revenus journaliers à partir de l'ID du membre
    public static RevenuesJ getRevenusJByID(Connection connection, int membreID) throws SQLException {
        String query = "SELECT * FROM revenuesj WHERE MembreID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, membreID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String temps = resultSet.getString("Temps");
                    double somme = resultSet.getDouble("Somme");
                    return new RevenuesJ(membreID, temps,somme);
                }
            }
        }
        return null;
    }

    // Méthode pour mettre à jour un revenu journalier dans la base de données
    public void mettreAJourRevenuJ(Connection connection) throws SQLException {
        String query = "UPDATE revenuesj SET Temps = ? WHERE MembreID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, this.temps);
            preparedStatement.setInt(2, this.membreID);

            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour supprimer un revenu journalier de la base de données
    public void supprimerRevenuJ(Connection connection) throws SQLException {
        String query = "UPDATE revenuesj SET Supprime=1 WHERE MembreID = ? AND Somme=? LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.membreID);
            preparedStatement.setDouble(2,this.somme);
            preparedStatement.executeUpdate();
        }
    }
    public static List<RevenuesJ> getListeRevenuesJ(Connection connection) {
        List<RevenuesJ> listeRevenuesJ = new ArrayList<>();
        String query = "SELECT * FROM revenuesj WHERE Supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int membreID = resultSet.getInt("MembreID");
                    double somme = resultSet.getDouble("Somme");
                   
                    String date = resultSet.getString("Temps");
                 
                   RevenuesJ rv = new RevenuesJ(membreID, date, somme);
                    listeRevenuesJ.add(rv);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listeRevenuesJ;
    }

}
