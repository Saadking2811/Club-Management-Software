package com.club.Model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Absence {
    private int membreID;
    private int seanceID;
    private String justification;
    private boolean supprime;

    // Constructeur
    public Absence(int membreID, int seanceID, String justification, boolean supprime) {
        this.membreID = membreID;
        this.seanceID = seanceID;
        this.justification = justification;
        this.supprime = supprime;
    }

    public Absence() {

    }

    // Méthode pour récupérer le nombre d'absences d'un adhérent
    public static int getNombreAbsences(Connection connection, int membreID) throws SQLException {
        int nombreAbsences = 0;
        String query = "SELECT COUNT(*) AS nombre_absences FROM absences WHERE MembreID = ? AND supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, membreID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    nombreAbsences = resultSet.getInt("nombre_absences");
                }
            }
        }
        return nombreAbsences;
    }
    // Getters et Setters
    public int getMembreID() {
        return membreID;
    }

    public void setMembreID(int membreID) {
        this.membreID = membreID;
    }

    public String getJustification() {
        return justification;
    }
    public void setJustification(String justification) {
        this.justification = justification;
    }



    // Méthodes pour manipuler la base de données (ajouter, récupérer, mettre à jour, supprimer)

    // Méthode pour ajouter une absence à la base de données
    public void ajouterAbsence(Connection connection) throws SQLException {
        String query = "INSERT INTO absences (MembreID, SeanceID, justification) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.membreID);
            preparedStatement.setInt(2, this.seanceID);
            preparedStatement.setString(3, this.justification);
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour récupérer une absence à partir de son membreID et de la date de la séance
    public static Absence getAbsence(Connection connection, int membreID, int seanceID) throws SQLException {
        String query = "SELECT * FROM absences WHERE MembreID = ? AND Seance = ? and supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, membreID);
            preparedStatement.setInt(2, seanceID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String justification = resultSet.getString("justification");
                    return new Absence(membreID, seanceID, justification, false);
                }
            }
        }
        return null;
    }

    // Méthode pour mettre à jour une absence dans la base de données
    public void mettreAJourAbsence(Connection connection) throws SQLException {
        String query = "UPDATE absences SET Justification = ? WHERE MembreID = ? AND SeanceID = ? and supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, this.justification);
            preparedStatement.setInt(2, this.membreID);
            preparedStatement.setInt(3, this.seanceID);
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour supprimer une absence de la base de données
    public void supprimerAbsence(Connection connection) throws SQLException {
        String query = "UPDATE absences SET Supprime = true WHERE MembreID = ? AND Seance = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.membreID);
            preparedStatement.setInt(2, this.seanceID);
            preparedStatement.executeUpdate();
        }
    }

    public int getSeanceID() {
        return seanceID;
    }

    public void setSeanceID(int seanceID) {
        this.seanceID = seanceID;
    }

    public boolean isSupprime() {
        return supprime;
    }

    public void setSupprime(boolean supprime) {
        this.supprime = supprime;
    }
}
