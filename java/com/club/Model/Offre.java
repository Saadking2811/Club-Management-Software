package com.club.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Offre {
    private int offreID;
    private int sportID;
    private double prix;
    private int duree; // nombre de semaines
    private String categorie;
    private String offreNom;
    private boolean supprime; // Champ pour la suppression logique

    // Constructeur avec le champ supprime
    public Offre(int offreID, int sportID, double prix, int duree, String categorie, String offreNom, boolean supprime) {
        this.offreID = offreID;
        this.sportID = sportID;
        this.prix = prix;
        this.duree = duree;
        this.categorie = categorie;
        this.offreNom = offreNom;
        this.supprime = supprime;
    }

    // Mettre à jour les méthodes pour utiliser un int pour la durée

    public void ajouterOffre(Connection connection) throws SQLException {
        String query = "INSERT INTO offres (SportID, Prix, Duree, Categorie, OffreNom) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.sportID);
            preparedStatement.setDouble(2, this.prix);
            preparedStatement.setInt(3, this.duree);
            preparedStatement.setString(4, this.categorie);
            preparedStatement.setString(5, this.offreNom);
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour obtenir une offre par ID
    public static Offre getOffreByID(Connection connection, int offreID) throws SQLException {
        String query = "SELECT * FROM offres WHERE OffreID = ? AND Supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, offreID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int sportID = resultSet.getInt("SportID");
                    double prix = resultSet.getDouble("Prix");
                    int duree = resultSet.getInt("Duree");
                    String categorie = resultSet.getString("Categorie");
                    String offreNom = resultSet.getString("OffreNom");
                    boolean supprime = resultSet.getBoolean("Supprime");
                    return new Offre(offreID, sportID, prix, duree, categorie, offreNom, supprime);
                }
            }
        }
        return null;
    }

    // Méthode pour obtenir l'ID d'une offre par son nom
    public static int getOffreIDParNom(Connection connection, String nomOffre) throws SQLException {
        String query = "SELECT OffreID FROM offres WHERE OffreNom = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nomOffre);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("OffreID");
                }
            }
        }
        return -1; // Retourne -1 si l'offre n'est pas trouvée
    }
    // Méthode pour mettre à jour une offre existante
    public void mettreAJourOffre(Connection connection) throws SQLException {
        String query = "UPDATE offres SET SportID = ?, Prix = ?, Duree = ?, Categorie = ?, OffreNom = ? WHERE OffreID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.sportID);
            preparedStatement.setDouble(2, this.prix);
            preparedStatement.setInt(3, this.duree);
            preparedStatement.setString(4, this.categorie);
            preparedStatement.setString(5, this.offreNom);
            preparedStatement.setInt(6, this.offreID);
            preparedStatement.executeUpdate();
        }
    }


    // Méthode pour la suppression logique
    public void supprimerOffre(Connection connection) throws SQLException {
        String query = "UPDATE offres SET Supprime = true WHERE OffreID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.offreID);
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour récupérer la liste des offres non supprimées
    public static List<Offre> getListeOffres(Connection connection) {
        List<Offre> listeOffres = new ArrayList<>();
        String query = "SELECT * FROM offres WHERE Supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int offreID = resultSet.getInt("OffreID");
                    int sportID = resultSet.getInt("SportID");
                    double prix = resultSet.getDouble("Prix");
                    int duree = resultSet.getInt("Duree"); 
                    String categorie = resultSet.getString("Categorie");
                    String offreNom = resultSet.getString("OffreNom");
                    boolean supprime = resultSet.getBoolean("Supprime");
                    Offre offre = new Offre(offreID, sportID, prix, duree, categorie, offreNom, supprime);
                    listeOffres.add(offre);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listeOffres;
    }
    // Getters et setters
    public int getOffreID() {
        return offreID;
    }

    public void setOffreID(int offreID) {
        this.offreID = offreID;
    }

    public int getSportID() {
        return sportID;
    }

    public void setSportID(int sportID) {
        this.sportID = sportID;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getOffreNom() {
        return offreNom;
    }

    public void setOffreNom(String offreNom) {
        this.offreNom = offreNom;
    }

    public boolean isSupprime() {
        return supprime;
    }

    public void setSupprime(boolean supprime) {
        this.supprime = supprime;
    }
}
