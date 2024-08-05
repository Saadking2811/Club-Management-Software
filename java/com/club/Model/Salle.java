package com.club.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Salle {
    private int salleID;
    private String nomSalle;
    private String categorie;
    private int limiteEffectif;
    private boolean supprime; // Champ pour la suppression logique
    private List<Materiel> listeMateriels;

    public Salle(int salleID, String nomSalle, String categorie, int limiteEffectif, boolean supprime) {
        this.salleID = salleID;
        this.nomSalle = nomSalle;
        this.categorie = categorie;
        this.limiteEffectif = limiteEffectif;
        this.supprime = supprime;
        this.listeMateriels = new ArrayList<>();
    }

    // Getters et Setters

    public int getSalleID() {
        return salleID;
    }

    public void setSalleID(int salleID) {
        this.salleID = salleID;
    }

    public String getNomSalle() {
        return nomSalle;
    }

    public void setNomSalle(String nomSalle) {
        this.nomSalle = nomSalle;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public int getLimiteEffectif() {
        return limiteEffectif;
    }

    public void setLimiteEffectif(int limiteEffectif) {
        this.limiteEffectif = limiteEffectif;
    }

    public boolean isSupprime() {
        return supprime;
    }

    public void setSupprime(boolean supprime) {
        this.supprime = supprime;
    }

    public List<Materiel> getListeMateriels() {
        return listeMateriels;
    }

    public void setListeMateriels(List<Materiel> listeMateriels) {
        this.listeMateriels = listeMateriels;
    }

    // Méthodes pour manipuler la base de données

    public void ajouterSalle(Connection connection) throws SQLException {
        String query = "INSERT INTO salles (NomSalle, Categorie, LimiteEffectif) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, this.nomSalle);
            preparedStatement.setString(2, this.categorie);
            preparedStatement.setInt(3, this.limiteEffectif);
            preparedStatement.executeUpdate();
        }
    }

    public static Salle getSalleByID(Connection connection, int salleID) throws SQLException {
        String query = "SELECT * FROM salles WHERE SalleID = ? AND Supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, salleID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String nomSalle = resultSet.getString("NomSalle");
                    String categorie = resultSet.getString("Categorie");
                    int limiteEffectif = resultSet.getInt("LimiteEffectif");
                    boolean supprime = resultSet.getBoolean("Supprime");
                    return new Salle(salleID, nomSalle, categorie, limiteEffectif, supprime);
                }
            }
        }
        return null;
    }

    public void mettreAJourSalle(Connection connection) throws SQLException {
        String query = "UPDATE salles SET NomSalle = ?, Categorie = ?, LimiteEffectif = ? WHERE SalleID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, this.nomSalle);
            preparedStatement.setString(2, this.categorie);
            preparedStatement.setInt(3, this.limiteEffectif);
            preparedStatement.setInt(4, this.salleID);
            preparedStatement.executeUpdate();
        }
    }

    public void supprimerSalle(Connection connection) throws SQLException {
        String query = "UPDATE salles SET Supprime = true WHERE SalleID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.salleID);
            preparedStatement.executeUpdate();
        }
    }

    public static List<Salle> getListeSalles(Connection connection) {
        List<Salle> listeSalles = new ArrayList<>();
        String query = "SELECT * FROM salles WHERE Supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int salleID = resultSet.getInt("SalleID");
                    String nomSalle = resultSet.getString("NomSalle");
                    String categorie = resultSet.getString("Categorie");
                    int limiteEffectif = resultSet.getInt("LimiteEffectif");
                    boolean supprime = resultSet.getBoolean("Supprime");
                    Salle salle = new Salle(salleID, nomSalle, categorie, limiteEffectif, supprime);
                    listeSalles.add(salle);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listeSalles;
    }

    public static int getSalleIdByNom(Connection connection, String nomSalle) throws SQLException {
        String query = "SELECT SalleID FROM salles WHERE NomSalle = ? AND Supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nomSalle);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("SalleID");
                }
            }
        }
        // Si la salle n'est pas trouvée, retourner -1 ou une valeur qui indique l'absence
        return -1;
    }

    public static List<Salle> getListeSallesParIDs(Connection connection, List<Integer> listeSalleIDs) {
        List<Salle> listeSalles = new ArrayList<>();
        String query = "SELECT * FROM salles WHERE SalleID IN (";
        for (int i = 0; i < listeSalleIDs.size(); i++) {
            query += "?";
            if (i < listeSalleIDs.size() - 1) {
                query += ",";
            }
        }
        query += ") AND Supprime = false";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            for (int i = 0; i < listeSalleIDs.size(); i++) {
                preparedStatement.setInt(i + 1, listeSalleIDs.get(i));
            }
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int salleID = resultSet.getInt("SalleID");
                    String nomSalle = resultSet.getString("NomSalle");
                    String categorie = resultSet.getString("Categorie");
                    int limiteEffectif = resultSet.getInt("LimiteEffectif");
                    boolean supprime = resultSet.getBoolean("Supprime");
                    Salle salle = new Salle(salleID, nomSalle, categorie, limiteEffectif, supprime);
                    listeSalles.add(salle);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listeSalles;
    }
}
