package com.club.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Staff extends Utilisateurs {

    public Staff() {
        setCategorie(2); // 1 pour membre
    }

    public Staff(String nom, String prenom, Date dateNaissance, String genre,
                    Date dateEntree, String email, String address, String telephone, double poids, double taille) {
        super(nom, prenom, dateNaissance, genre, 1, email, address, dateEntree, telephone, poids, taille);
    }


    // Méthode pour ajouter un adhérent à la base de données
    public void ajouterStaff(Connection connection) throws SQLException {
        ajouterUtilisateur(connection);
    }

    // Méthode pour mettre à jour les informations d'un adhérent dans la base de données
    public void mettreAJourStaff(Connection connection) throws SQLException {
        mettreAJourUtilisateur(connection);
    }

    // Méthode pour récupérer tous les adhérents de la base de données
    public static List<Staff> recupererTousLesStaffs(Connection connection) throws SQLException {
        List<Staff> adherentsList = new ArrayList<>();
        String query = "SELECT * FROM utilisateurs WHERE Categorie = 2  AND Supprime = false"; // Sélectionner seulement les adhérents
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Staff adherent = new Staff();
                adherent.setMembreID(resultSet.getInt("MembreID"));
                adherent.setNom(resultSet.getString("Nom"));
                adherent.setPrenom(resultSet.getString("Prenom"));
                adherent.setDateNaissance(resultSet.getDate("DateNaissance"));
                adherent.setGenre(resultSet.getString("Genre"));
                adherent.setAge();
                adherent.setCategorie(resultSet.getInt("Categorie"));
                adherent.setEmail(resultSet.getString("Adresse_Mail"));
                adherent.setAddress(resultSet.getString("Adresse_Domicil"));
                adherent.setDateEntree(resultSet.getDate("DateEntree"));
                adherent.setTelephone(resultSet.getString("Telephone"));
                adherent.setPoids(resultSet.getDouble("Poid"));
                adherent.setTaille(resultSet.getDouble("Taille"));
                adherent.setSupprime(resultSet.getBoolean("Supprime"));
                adherent.setPhoto(resultSet.getString("Photo"));
                adherent.setQRcode(resultSet.getString("CodeQR"));
                adherentsList.add(adherent);
            }
        }
        return adherentsList;
    }

    // Méthode pour récupérer un adhérent par son ID
    public static Staff recupererStaffParID(Connection connection, int membreID) throws SQLException {
        String query = "SELECT * FROM utilisateurs WHERE MembreID = ? AND Categorie = 2 AND Supprime = false"; // Sélectionner seulement les adhérents
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, membreID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Staff adherent = new Staff();
                    adherent.setMembreID(resultSet.getInt("MembreID"));
                    adherent.setNom(resultSet.getString("Nom"));
                    adherent.setPrenom(resultSet.getString("Prenom"));
                    adherent.setDateNaissance(resultSet.getDate("DateNaissance"));
                    adherent.setGenre(resultSet.getString("Genre"));
                    adherent.setAge();
                    adherent.setCategorie(resultSet.getInt("Categorie"));
                    adherent.setEmail(resultSet.getString("Adresse_Mail"));
                    adherent.setAddress(resultSet.getString("Adresse_Domicil"));
                    adherent.setDateEntree(resultSet.getDate("DateEntree"));
                    adherent.setTelephone(resultSet.getString("Telephone"));
                    adherent.setPoids(resultSet.getDouble("Poid"));
                    adherent.setTaille(resultSet.getDouble("Taille"));
                    adherent.setSupprime(resultSet.getBoolean("Supprime"));
                    adherent.setPhoto(resultSet.getString("Photo"));
                    adherent.setQRcode(resultSet.getString("CodeQR"));
                    return adherent;
                }
            }
        }
        return null;
    }
    
}
