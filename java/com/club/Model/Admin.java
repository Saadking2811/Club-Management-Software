package com.club.Model;
import com.club.Model.Admin;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Admin {
    private int adminID;
    private String nomUtilisateur;
    private String motDePasse;
    private boolean supprime; // Champ pour la suppression logique

    // Constructeur
    public Admin(String nomUtilisateur, String motDePasse) {
        this.nomUtilisateur = nomUtilisateur;
        this.motDePasse = motDePasse;
        this.supprime = false; // Par défaut, l'administrateur n'est pas supprimé
    }

    // Getters et setters
    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public boolean isSupprime() {
        return supprime;
    }

    public void setSupprime(boolean supprime) {
        this.supprime = supprime;
    }

    // Méthode pour supprimer l'administrateur logiquement
    public void supprimerAdmin() {
        this.supprime = true;
    }

    // Méthode pour restaurer l'administrateur
    public void restaurerAdmin() {
        this.supprime = false;
    }

    // Méthode pour authentifier l'administrateur
    public static boolean authentifierAdmin(Connection connection, String nomUtilisateur, String motDePasse) throws SQLException {
      String query = "SELECT * FROM admins WHERE NomUtilisateur = ? AND MotDePasse = ? AND Supprime = false";
      try (PreparedStatement statement = connection.prepareStatement(query)) {
          statement.setString(1, nomUtilisateur);
          statement.setString(2, motDePasse);
          try (ResultSet resultSet = statement.executeQuery()) {
              return resultSet.next();
          }
      }
  }

    // Méthode pour ajouter un administrateur à la base de données
    public void ajouterAdmin(Connection connection) throws SQLException {
        String query = "INSERT INTO admins (NomUtilisateur, MotDePasse, Supprime) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nomUtilisateur);
            statement.setString(2, motDePasse);
            statement.setBoolean(3, false);
            statement.executeUpdate();
        }
    }

    // Méthode pour supprimer un administrateur de la base de données
    public void supprimerAdmin(Connection connection) throws SQLException {
        String query = "UPDATE admins SET Supprime = true WHERE AdminID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, adminID);
            statement.executeUpdate();
        }
    }

    // Méthode pour restaurer un administrateur supprimé
    public void restaurerAdmin(Connection connection) throws SQLException {
        String query = "UPDATE admins SET Supprime = false WHERE AdminID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, adminID);
            statement.executeUpdate();
        }
    }

    // Méthode pour récupérer tous les administrateurs de la base de données
    public static List<Admin> recupererTousLesAdmins(Connection connection) throws SQLException {
        List<Admin> adminsList = new ArrayList<>();
        String query = "SELECT * FROM admins WHERE Supprime = false";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Admin admin = new Admin(resultSet.getString("NomUtilisateur"), resultSet.getString("MotDePasse"));
                admin.setAdminID(resultSet.getInt("AdminID"));
                admin.setSupprime(resultSet.getBoolean("Supprime"));
                adminsList.add(admin);
            }
        }
        return adminsList;
    }

    // Méthode pour récupérer un administrateur par son ID
    public static Admin recupererAdminParID(Connection connection, int adminID) throws SQLException {
        String query = "SELECT * FROM admins WHERE AdminID = ? AND Supprime = false";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, adminID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Admin admin = new Admin(resultSet.getString("NomUtilisateur"), resultSet.getString("MotDePasse"));
                    admin.setAdminID(adminID);
                    admin.setSupprime(resultSet.getBoolean("Supprime"));
                    return admin;
                }
            }
        }
        return null;
    }
}
