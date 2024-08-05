package com.club.Model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DossierMedical {
    private int dossierID;
    private int membreID;
    private String groupeSanguin;
    private String maladieChronique;
    private String blessure;
    private String certificatHabilite;

    // Constructeur
    public DossierMedical(int dossierID, int membreID, String groupeSanguin, String maladieChronique, String blessure, String certificatHabilite) {
        this.dossierID = dossierID;
        this.membreID = membreID;
        this.groupeSanguin = groupeSanguin;
        this.maladieChronique = maladieChronique;
        this.blessure = blessure;
        this.certificatHabilite = certificatHabilite;
    }

    // Méthode pour récupérer un dossier médical à partir du membreID
    public static DossierMedical getDossierMedicalByMembreID(Connection connection, int membreID) throws SQLException {
        String query = "SELECT * FROM dossiersmedicales WHERE MembreID = ? AND Supprime=0";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, membreID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int dossierID = resultSet.getInt("DossierID");
                    String groupeSanguin = resultSet.getString("GroupeSanguin");
                    String maladieChronique = resultSet.getString("MaladieChronique");
                    String blessure = resultSet.getString("Blessure");
                    String certificatHabilite = resultSet.getString("CertificatHabilite");
                    return new DossierMedical(dossierID, membreID, groupeSanguin, maladieChronique, blessure, certificatHabilite);
                }
            }
        }
        return null;
    }
    // Getters et Setters
    public int getDossierID() {
        return dossierID;
    }

    public void setDossierID(int dossierID) {
        this.dossierID = dossierID;
    }

    public int getMembreID() {
        return membreID;
    }

    public void setMembreID(int membreID) {
        this.membreID = membreID;
    }

    public String getGroupeSanguin() {
        return groupeSanguin;
    }

    public void setGroupeSanguin(String groupeSanguin) {
        this.groupeSanguin = groupeSanguin;
    }

    public String getMaladieChronique() {
        return maladieChronique;
    }

    public void setMaladieChronique(String maladieChronique) {
        this.maladieChronique = maladieChronique;
    }

    public String getBlessure() {
        return blessure;
    }

    public void setBlessure(String blessure) {
        this.blessure = blessure;
    }

    public String getCertificatHabilite() {
        return certificatHabilite;
    }

    public void setCertificatHabilite(String certificatHabilite) {
        this.certificatHabilite = certificatHabilite;
    }

    // Méthodes pour manipuler la base de données (ajouter, récupérer, mettre à jour, supprimer)

    // Méthode pour ajouter un dossier médical à la base de données
    public void ajouterDossierMedical(Connection connection) throws SQLException {
        String query = "INSERT INTO dossiersmedicales (DossierID, MembreID, GroupeSanguin, MaladieChronique, Blessure, CertificatHabilite) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.dossierID);
            preparedStatement.setInt(2, this.membreID);
            preparedStatement.setString(3, this.groupeSanguin);
            preparedStatement.setString(4, this.maladieChronique);
            preparedStatement.setString(5, this.blessure);
            preparedStatement.setString(6, this.certificatHabilite);
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour récupérer un dossier médical à partir de son ID
    public static DossierMedical getDossierMedicalByID(Connection connection, int dossierID) throws SQLException {
        String query = "SELECT * FROM dossiersmedicales WHERE DossierID = ? AND Supprime=0";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, dossierID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int membreID = resultSet.getInt("MembreID");
                    String groupeSanguin = resultSet.getString("GroupeSanguin");
                    String maladieChronique = resultSet.getString("MaladieChronique");
                    String blessure = resultSet.getString("Blessure");
                    String certificatHabilite = resultSet.getString("CertificatHabilite");
                    return new DossierMedical(dossierID, membreID, groupeSanguin, maladieChronique, blessure, certificatHabilite);
                }
            }
        }
        return null;
    }

    // Méthode pour mettre à jour un dossier médical dans la base de données
    public void mettreAJourDossierMedical(Connection connection) throws SQLException {
        String query = "UPDATE dossiersmedicales SET MembreID = ?, GroupeSanguin = ?, MaladieChronique = ?, Blessure = ?, CertificatHabilite = ? WHERE DossierID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.membreID);
            preparedStatement.setString(2, this.groupeSanguin);
            preparedStatement.setString(3, this.maladieChronique);
            preparedStatement.setString(4, this.blessure);
            preparedStatement.setString(5, this.certificatHabilite);
            preparedStatement.setInt(6, this.dossierID);
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour supprimer un dossier médical de la base de données
    public void supprimerDossierMedical(Connection connection) throws SQLException {
        String query = "UPDATE dossiersmedicales SET Supprime=1 WHERE DossierID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.dossierID);
            preparedStatement.executeUpdate();
        }
    }
}
