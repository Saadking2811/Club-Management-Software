package com.club.Model;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

public class Utilisateurs {
    private int membreID; // Ajout du champ membreID
    private String nom;
    private String prenom;
    private Date dateNaissance;
    private String genre;
    private String photo;
    private String QRcode;
    private int age;
    private int categorie; // 1 pour membre, 0 pour coach
    private String email;
    private String address;
    private Date dateEntree;
    private String telephone;
    private double poids;
    private double taille;
    private boolean supprime; // Ajout du champ supprime

    public Utilisateurs(String nom, String prenom, Date dateNaissance, String genre, int categorie, String email,
                String address, Date dateEntree, String telephone, double poids, double taille) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.genre = genre;
        this.age = calculerAge(dateNaissance);
        this.categorie = categorie;
        this.email = email;
        this.address = address;
        this.dateEntree = dateEntree;
        this.telephone = telephone;
        this.poids = poids;
        this.taille = taille;
        this.supprime = false; // Par défaut, l'utilisateur n'est pas supprimé
    }

    private int calculerAge(Date dateNaissance) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateNaissance);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Les mois commencent à 0, donc ajoutez 1
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        
        // Récupérer la date actuelle
        Calendar now = Calendar.getInstance();
        int currentYear = now.get(Calendar.YEAR);
        int currentMonth = now.get(Calendar.MONTH) + 1; // Les mois commencent à 0, donc ajoutez 1
        int currentDay = now.get(Calendar.DAY_OF_MONTH);
        
        // Calculer l'âge
        int age = currentYear - year;
        
        // Vérifier si l'anniversaire de cette année n'est pas encore passé
        if (currentMonth < month || (currentMonth == month && currentDay < day)) {
            age--; // Diminuer l'âge
        }
        
        return age;
    }

    public Utilisateurs() {}

     //*********************** Getters & Setters *******************************
     public String getNom() {return nom;}
     public void setNom(String nom) {this.nom = nom;}
 
     public String getPrenom() {return prenom;}
     public void setPrenom(String prenom) {this.prenom = prenom;}
 
     public Date getDateNaissance() {return dateNaissance;}
     public void setDateNaissance(Date dateNaissance) {this.dateNaissance = dateNaissance;}
 
     public String getGenre() {return genre;}
     public void setGenre(String genre) {this.genre = genre;}
 
     public int getAge() {return age;}
     public void setAge() {this.age = calculerAge(dateNaissance);}
 
     public int getCategorie() {return categorie;}
     public void setCategorie(int categorie) {this.categorie = categorie;}
 
     public String getEmail() {return email;}
     public void setEmail(String email) {this.email = email;}
 
     public String getAddress() {return address;}
     public void setAddress(String address) {this.address = address;}
 
     public Date getDateEntree() {return dateEntree;}
     public void setDateEntree(Date dateEntree) {this.dateEntree = dateEntree;}
 
     public String getTelephone() {return telephone;}
     public void setTelephone(String telephone) {this.telephone = telephone;}
 
     public double getPoids() {return poids;}
     public void setPoids(double poids) {this.poids = poids;}
 
     public double getTaille() {return taille;}
     public void setTaille(double taille) {this.taille = taille;}
 
 
     
    public int getMembreID() {
        return membreID;
    }

    public void setMembreID(int membreID) {
        this.membreID = membreID;
    }

    public boolean isSupprime() {
        return supprime;
    }

    public void setSupprime(boolean supprime) {
        this.supprime = supprime;
    }

    // Méthode pour logique de suppression
    public void supprimerUtilisateur() {
        this.supprime = true;
    }

    // Méthode pour restaurer un utilisateur supprimé
    public void restaurerUtilisateur() {
        this.supprime = false;
    }
    /// Méthode pour logique de suppression
    public void supprimerUtilisateur(Connection connection) throws SQLException {
        String query = "UPDATE utilisateurs SET Supprime = true WHERE MembreID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, this.membreID);
        statement.executeUpdate();
        this.supprime = true;
    }

    // Méthode pour restaurer un utilisateur supprimé
    public void restaurerUtilisateur(Connection connection) throws SQLException {
        String query = "UPDATE utilisateurs SET Supprime = false WHERE MembreID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, this.membreID);
        statement.executeUpdate();
        this.supprime = false;
    }

    // Méthode pour ajouter un utilisateur à la base de données
    public void ajouterUtilisateur(Connection connection) throws SQLException {
        String query = "INSERT INTO db.utilisateurs(Nom,Prenom,DateNaissance,Genre,Categorie,Adresse_Domicil,Adresse_Mail,DateEntree,Telephone,Poid,Taille,CodeQR,Photo) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?);";
      try {
        
        genererCodeQR();
        //date conversion
          java.util.Date dateN = this.dateNaissance;
          java.sql.Date dateNsql = new java.sql.Date(dateN.getTime());
          java.util.Date dateE = this.dateEntree;
          java.sql.Date dateEsql = new java.sql.Date(dateE.getTime());
          PreparedStatement preparedStatement = connection.prepareStatement(query);
          
          preparedStatement.setString(1,this.nom);
          preparedStatement.setString(2,this.prenom);
          preparedStatement.setDate(3,dateNsql);
          preparedStatement.setString(4,this.genre);
          //preparedStatement.setBytes(6,membre.getCodeQR());
          preparedStatement.setInt(5,this.categorie);
          preparedStatement.setString(6,this.address);
          preparedStatement.setString(7,this.email);
          preparedStatement.setDate(8,dateEsql);
          // be carful for the conversion

          String data = this.nom + "_" + this.prenom + "_" + this.dateNaissance;
          preparedStatement.setString(9,this.telephone);
          preparedStatement.setDouble(10,this.poids);
          preparedStatement.setDouble(11,this.taille);
          preparedStatement.setString(12,data);
          preparedStatement.setString(13,this.photo);
          preparedStatement.executeUpdate();
          System.out.println("=:- User added !");
      }
      catch (SQLException e) {
          e.printStackTrace();
      }
    }

    // Méthode pour mettre à jour les informations d'un utilisateur dans la base de données
    public void mettreAJourUtilisateur(Connection connection) throws SQLException {
        String query = "UPDATE utilisateurs SET Nom = ?, Prenom = ?, DateNaissance = ?, Genre = ?, Categorie = ?, Adresse_Mail = ?, Adresse_Domicil = ?, DateEntree = ?, Telephone = ?, Poid = ?, Taille = ?, Supprime = ? " +
                        "WHERE MembreID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, this.nom);
        statement.setString(2, this.prenom);
        statement.setDate(3, new java.sql.Date(this.dateNaissance.getTime()));
        statement.setString(4, this.genre);
        statement.setInt(5, this.categorie);
        statement.setString(6, this.email);
        statement.setString(7, this.address);
        statement.setDate(8, new java.sql.Date(this.dateEntree.getTime()));
        statement.setString(9, this.telephone);
        statement.setDouble(10, this.poids);
        statement.setDouble(11, this.taille);
        statement.setBoolean(12, this.supprime);
        statement.setInt(13, this.membreID);
        statement.executeUpdate();
    }

    // Méthode pour récupérer les informations d'un utilisateur depuis la base de données
    public void chargerUtilisateur(Connection connection) throws SQLException {
        String query = "SELECT * FROM utilisateurs WHERE MembreID = ? AND Supprime = false";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, this.membreID);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            this.nom = resultSet.getString("Nom");
            this.prenom = resultSet.getString("Prenom");
            this.dateNaissance = resultSet.getDate("DateNaissance");
            this.genre = resultSet.getString("Genre");
            this.categorie = resultSet.getInt("Categorie");
            this.email = resultSet.getString("Adresse_Mail");
            this.address = resultSet.getString("Adresse_Domicil");
            this.dateEntree = resultSet.getDate("DateEntree");
            this.telephone = resultSet.getString("Telephone");
            this.poids = resultSet.getDouble("Poid");
            this.taille = resultSet.getDouble("Taille");
            this.supprime = resultSet.getBoolean("Supprime");
        }
        resultSet.close();
        statement.close();
    }

    // Méthode pour récupérer tous les utilisateurs de la base de données
    public static List<Utilisateurs> recupererTousLesUtilisateurs(Connection connection) throws SQLException {
        List<Utilisateurs> utilisateursList = new ArrayList<>();
        String query = "SELECT * FROM utilisateurs WHERE Supprime = false";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Utilisateurs utilisateur = new Utilisateurs();
                utilisateur.setMembreID(resultSet.getInt("MembreID"));
                utilisateur.setNom(resultSet.getString("Nom"));
                utilisateur.setPrenom(resultSet.getString("Prenom"));
                utilisateur.setDateNaissance(resultSet.getDate("DateNaissance"));
                utilisateur.setGenre(resultSet.getString("Genre"));
                utilisateur.setCategorie(resultSet.getInt("Categorie"));
                utilisateur.setEmail(resultSet.getString("Adresse_Mail"));
                utilisateur.setAddress(resultSet.getString("Adresse_Domicil"));
                utilisateur.setDateEntree(resultSet.getDate("DateEntree"));
                utilisateur.setTelephone(resultSet.getString("Telephone"));
                utilisateur.setPoids(resultSet.getDouble("Poid"));
                utilisateur.setTaille(resultSet.getDouble("Taille"));
                utilisateur.setSupprime(resultSet.getBoolean("Supprime"));
                utilisateursList.add(utilisateur);
            }
        }
        return utilisateursList;
    }

    // Méthode pour récupérer un utilisateur par son ID
    public static Utilisateurs recupererUtilisateurParID(Connection connection, int membreID) throws SQLException {
        String query = "SELECT * FROM utilisateurs WHERE MembreID = ? ";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, membreID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Utilisateurs utilisateur = new Utilisateurs();
                    utilisateur.setMembreID(resultSet.getInt("MembreID"));
                    utilisateur.setNom(resultSet.getString("Nom"));
                    utilisateur.setPrenom(resultSet.getString("Prenom"));
                    utilisateur.setDateNaissance(resultSet.getDate("DateNaissance"));
                    utilisateur.setGenre(resultSet.getString("Genre"));
                    utilisateur.setCategorie(resultSet.getInt("Categorie"));
                    utilisateur.setEmail(resultSet.getString("Adresse_Mail"));
                    utilisateur.setAddress(resultSet.getString("Adresse_Domicil"));
                    utilisateur.setDateEntree(resultSet.getDate("DateEntree"));
                    utilisateur.setTelephone(resultSet.getString("Telephone"));
                    utilisateur.setPoids(resultSet.getDouble("Poid"));
                    utilisateur.setTaille(resultSet.getDouble("Taille"));
                    utilisateur.setSupprime(resultSet.getBoolean("Supprime"));
                    return utilisateur;
                }
            }
        }
        return null;
    }

    public static int getDernierUserID(Connection connection) throws SQLException {
        int dernierID = -1;
        String query = "SELECT LAST_INSERT_ID() AS last_id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    dernierID = resultSet.getInt("last_id");
                }
            }
        }
        return dernierID;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getQRcode() {
        return QRcode;
    }

    public void setQRcode(String qRcode) {
        QRcode = qRcode;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void genererCodeQR() {
        int width = 300;
        int height = 300;
        java.sql.Date sqlDate = new java.sql.Date(this.dateNaissance.getTime());
        String data = this.nom + "_" + this.prenom + "_" + sqlDate;
        String filePath = "./CodeQR/" + data + ".png";
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, width, height, hints);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", FileSystems.getDefault().getPath(filePath));
            //System.out.println("QR code generated successfully at: " + filePath);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }
}

