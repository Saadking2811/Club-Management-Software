package com.club.Model;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class Coach extends Utilisateurs {

    public Coach() {
        setCategorie(0); // 0 pour coach
    }

    public Coach(String nom, String prenom, Date dateNaissance, String genre, int age,
                 Date dateEntree, String email, String address, String telephone, double poids, double taille) {
        super(nom, prenom, dateNaissance, genre, 0, email, address, dateEntree, telephone, poids, taille);
    }

    // Méthode pour ajouter un coach à la base de données
    public void ajouterCoach(Connection connection) throws SQLException {
        ajouterUtilisateur(connection);
    }

    // Méthode pour mettre à jour les informations d'un coach dans la base de données
    public void mettreAJourCoach(Connection connection) throws SQLException {
        mettreAJourUtilisateur(connection);
    }

    // Méthode pour récupérer tous les coaches de la base de données
    public static List<Coach> recupererTousLesCoaches(Connection connection) throws SQLException {
        List<Coach> coachesList = new ArrayList<>();
        String query = "SELECT * FROM utilisateurs WHERE Categorie = 0  AND Supprime = false";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Coach coach = new Coach();
                coach.setMembreID(resultSet.getInt("MembreID"));
                coach.setNom(resultSet.getString("Nom"));
                coach.setPrenom(resultSet.getString("Prenom"));
                coach.setDateNaissance(resultSet.getDate("DateNaissance"));
                coach.setGenre(resultSet.getString("Genre"));
                coach.setAge();
                coach.setCategorie(resultSet.getInt("Categorie"));
                coach.setEmail(resultSet.getString("Adresse_Mail"));
                coach.setAddress(resultSet.getString("Adresse_Domicil"));
                coach.setDateEntree(resultSet.getDate("DateEntree"));
                coach.setTelephone(resultSet.getString("Telephone"));
                coach.setPoids(resultSet.getDouble("Poid"));
                coach.setTaille(resultSet.getDouble("Taille"));
                coach.setSupprime(resultSet.getBoolean("Supprime"));
                coach.setPhoto(resultSet.getString("Photo"));
                coach.setQRcode(resultSet.getString("CodeQR"));
                coachesList.add(coach);
            }
        }
        return coachesList;
    }

    // Méthode pour récupérer un coach par son ID
    public static Coach recupererCoachParID(Connection connection, int membreID) throws SQLException {
        String query = "SELECT * FROM utilisateurs WHERE MembreID = ? AND Categorie = 0  AND Supprime = false";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, membreID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Coach coach = new Coach();
                    coach.setMembreID(resultSet.getInt("MembreID"));
                    coach.setNom(resultSet.getString("Nom"));
                    coach.setPrenom(resultSet.getString("Prenom"));
                    coach.setDateNaissance(resultSet.getDate("DateNaissance"));
                    coach.setGenre(resultSet.getString("Genre"));
                    coach.setAge();
                    coach.setCategorie(resultSet.getInt("Categorie"));
                    coach.setEmail(resultSet.getString("Adresse_Mail"));
                    coach.setAddress(resultSet.getString("Adresse_Domicil"));
                    coach.setDateEntree(resultSet.getDate("DateEntree"));
                    coach.setTelephone(resultSet.getString("Telephone"));
                    coach.setPoids(resultSet.getDouble("Poid"));
                    coach.setTaille(resultSet.getDouble("Taille"));
                    coach.setSupprime(resultSet.getBoolean("Supprime"));
                    coach.setPhoto(resultSet.getString("Photo"));
                    coach.setQRcode(resultSet.getString("CodeQR"));
                    return coach;
                }
            }
        }
        return null;
    }
    public static int getCoachIDByNom(Connection connection, String Nom) throws SQLException {
        String query = "SELECT MembreID FROM utilisateurs WHERE Nom = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, Nom);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("MembreID");
                }
            }
        }
        return -1;
    }
}
