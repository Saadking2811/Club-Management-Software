package com.club.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Materiel {
    private int materielID;
    private String nomMateriel;
    private String categorie;
    private int quantite;
    private int salleID; // Nouveau champ pour l'ID de la salle
    private boolean supprime; // Champ pour la suppression logique

    // Constructeur avec le champ supprime
    public Materiel(int materielID, String nomMateriel, String categorie, int quantite, int salleID, boolean supprime) {
        this.materielID = materielID;
        this.nomMateriel = nomMateriel;
        this.categorie = categorie;
        this.quantite = quantite;
        this.salleID = salleID;
        this.supprime = supprime;
    }

    // Méthodes pour manipuler la base de données (ajouter, récupérer, mettre à jour, supprimer)

    // Méthode pour ajouter un matériel à la base de données
    public void ajouterMateriel(Connection connection) throws SQLException {
        String query = "INSERT INTO materiels (NomMateriel, Categorie, Quantite, SalleID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, this.nomMateriel);
            preparedStatement.setString(2, this.categorie);
            preparedStatement.setInt(3, this.quantite);
            preparedStatement.setInt(4, this.salleID);
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour récupérer un matériel par ID
    public static Materiel getMaterielByID(Connection connection, int materielID) throws SQLException {
        String query = "SELECT * FROM materiels WHERE MaterielID = ? AND Supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, materielID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String nomMateriel = resultSet.getString("NomMateriel");
                    String categorie = resultSet.getString("Categorie");
                    int quantite = resultSet.getInt("Quantite");
                    int salleID = resultSet.getInt("SalleID");
                    boolean supprime = resultSet.getBoolean("Supprime");
                    return new Materiel(materielID, nomMateriel, categorie, quantite, salleID, supprime);
                }
            }
        }
        return null;
    }

    // Méthode pour mettre à jour un matériel dans la base de données
    public void mettreAJourMateriel(Connection connection) throws SQLException {
        String query = "UPDATE materiels SET NomMateriel = ?, Categorie = ?, Quantite = ?, SalleID = ? WHERE MaterielID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, this.nomMateriel);
            preparedStatement.setString(2, this.categorie);
            preparedStatement.setInt(3, this.quantite);
            preparedStatement.setInt(4, this.salleID);
            preparedStatement.setInt(5, this.materielID);
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour supprimer un matériel de la base de données (suppression logique)
    public void supprimerMateriel(Connection connection) throws SQLException {
        String query = "UPDATE materiels SET Supprime = true WHERE MaterielID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.materielID);
            preparedStatement.executeUpdate();
        }
    }

    // Méthode pour récupérer la liste des matériels non supprimés
    public static List<Materiel> getListeMateriels(Connection connection) {
        List<Materiel> listeMateriels = new ArrayList<>();
        String query = "SELECT * FROM materiels WHERE Supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int materielID = resultSet.getInt("MaterielID");
                    String nomMateriel = resultSet.getString("NomMateriel");
                    String categorie = resultSet.getString("Categorie");
                    int quantite = resultSet.getInt("Quantite");
                    int salleID = resultSet.getInt("SalleID");
                    boolean supprime = resultSet.getBoolean("Supprime");
                    Materiel materiel = new Materiel(materielID, nomMateriel, categorie, quantite, salleID, supprime);
                    listeMateriels.add(materiel);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listeMateriels;
    }

    // Getters et Setters pour les champs
    public int getMaterielID() {
        return materielID;
    }

    public void setMaterielID(int materielID) {
        this.materielID = materielID;
    }

    public String getNomMateriel() {
        return nomMateriel;
    }

    public void setNomMateriel(String nomMateriel) {
        this.nomMateriel = nomMateriel;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getSalleID() {
        return salleID;
    }

    public void setSalleID(int salleID) {
        this.salleID = salleID;
    }

    public boolean isSupprime() {
        return supprime;
    }

    public void setSupprime(boolean supprime) {
        this.supprime = supprime;
    }
}
