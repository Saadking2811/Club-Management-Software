package com.club.Model;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;


import java.util.Collection;
import com.google.protobuf.Timestamp;
import com.google.zxing.Result;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.util.Date;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Gestion {
    public Gestion() {
    }

public static String mail; 
public static String pass; 
public static String logoPath; 


public void ajouterMembre(Membre membre, Connection connect) {
    String query = "INSERT INTO db.utilisateurs(Nom,Prenom,DateNaissance,Genre,Categorie,Adresse_Domicil,Adresse_Mail,DateEntree,Telephone,Poid,Taille,CodeQR,Photo) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?);";
    try {
      //date conversion
        java.util.Date dateN = membre.getDateNaissance();
        java.sql.Date dateNsql = new java.sql.Date(dateN.getTime());
        java.util.Date dateE = membre.getDateEntree();
        java.sql.Date dateEsql = new java.sql.Date(dateE.getTime());
        String data = membre.getNom() + "_" + membre.getPrenom() + "_" + dateNsql;
        membre.genererCodeQR();
        PreparedStatement preparedStatement = connect.prepareStatement(query);
        
        preparedStatement.setString(1,membre.getNom());
        preparedStatement.setString(2,membre.getPrenom());
        preparedStatement.setDate(3,dateNsql);
        preparedStatement.setString(4,membre.getGenre());
        //preparedStatement.setBytes(6,membre.getCodeQR());
        preparedStatement.setInt(5,1);
        preparedStatement.setString(6,membre.getAddress());
        preparedStatement.setString(7,membre.getEmail());
        preparedStatement.setDate(8,dateEsql);
        // be carful for the conversion
      
        preparedStatement.setString(9,membre.getTelephone());
        preparedStatement.setDouble(10,membre.getPoids());
        preparedStatement.setDouble(11,membre.getTaille());
        preparedStatement.setString(12,data);
        preparedStatement.setString(13,membre.getPhoto());
        preparedStatement.executeUpdate();
        System.out.println("=:- Membre added !");
        envoyerMailAjout(mail,pass,membre.getEmail(),"Adherent",membre.getNom()+" "+membre.getPrenom());
    }
    catch (SQLException e) {
        e.printStackTrace();
    }
}


public void ajouterStaff(Staff membre, Connection connect) {
    String query = "INSERT INTO db.utilisateurs(Nom,Prenom,DateNaissance,Genre,Categorie,Adresse_Domicil,Adresse_Mail,DateEntree,Telephone,Poid,Taille,CodeQR,Photo) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?);";
    try {
      //date conversion
        java.util.Date dateN = membre.getDateNaissance();
        java.sql.Date dateNsql = new java.sql.Date(dateN.getTime());
        java.util.Date dateE = membre.getDateEntree();
        java.sql.Date dateEsql = new java.sql.Date(dateE.getTime());
        String data = membre.getNom() + "_" + membre.getPrenom() + "_" + dateNsql;
        membre.genererCodeQR();
        PreparedStatement preparedStatement = connect.prepareStatement(query);
        
        preparedStatement.setString(1,membre.getNom());
        preparedStatement.setString(2,membre.getPrenom());
        preparedStatement.setDate(3,dateNsql);
        preparedStatement.setString(4,membre.getGenre());
        //preparedStatement.setBytes(6,membre.getCodeQR());
        preparedStatement.setInt(5,2);
        preparedStatement.setString(6,membre.getAddress());
        preparedStatement.setString(7,membre.getEmail());
        preparedStatement.setDate(8,dateEsql);
        // be carful for the conversion
      
        preparedStatement.setString(9,membre.getTelephone());
        preparedStatement.setDouble(10,membre.getPoids());
        preparedStatement.setDouble(11,membre.getTaille());
        preparedStatement.setString(12,data);
        preparedStatement.setString(13,membre.getPhoto());
        preparedStatement.executeUpdate();
        System.out.println("=:- Membre added !");
        envoyerMailAjout(mail,pass,membre.getEmail(),"Staff",membre.getNom()+" "+membre.getPrenom());
    }
    catch (SQLException e) {
        e.printStackTrace();
    }
}


    public Connection connectionBd() {
        String url = "jdbc:mysql://127.0.0.1:3306/db";
        String username = "root";
        String password = "Malak18@#2004";

        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void ajouterAdmin(Connection connection, Admin admin) {
        String query = "INSERT INTO admins (NomUtilisateur, MotDePasse) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, admin.getNomUtilisateur());
            statement.setString(2, admin.getMotDePasse());
            statement.executeUpdate();
            System.out.println("Admin ajouté");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void supprimerAdmin(Connection connection, int adminID) {
        String query = "UPDATE admins SET Supprime = true WHERE AdminID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, adminID);
            statement.executeUpdate();
            System.out.println("Admin supprimé");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    public void modifierAdmin(Connection connection, Admin oldAdmin, Admin newAdmin) {
        String query = "UPDATE admins SET NomUtilisateur = ?, MotDePasse = ? WHERE AdminID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newAdmin.getNomUtilisateur());
            statement.setString(2, newAdmin.getMotDePasse());
            statement.setInt(3, oldAdmin.getAdminID());
            statement.executeUpdate();
            System.out.println("Admin modifié");
        } catch (SQLException e) {
            e.printStackTrace();
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

    public static boolean seConnecter(Connection connect, String nom, String motPasse) {
        String query = "SELECT * FROM admins WHERE NomUtilisateur = ? AND MotDePasse = ? AND Supprime = false";

        try {
            PreparedStatement preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, motPasse);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int cpt = resultSet.getInt(1);
                    return cpt > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Méthode pour ajouter un adhérent à la base de données
    public void ajouterAdherent(Adherent adherent, Connection connect) throws SQLException {
        String query = "INSERT INTO db.utilisateurs(Nom,Prenom,DateNaissance,Genre,Categorie,Adresse_Domicil,Adresse_Mail,DateEntree,Telephone,Poid,Taille,CodeQR,Photo) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try {
            //date conversion
              java.sql.Date sqlDate = new java.sql.Date(adherent.getDateNaissance().getTime());
              String data = adherent.getNom() + "_" + adherent.getPrenom() + "_" + sqlDate;
              adherent.genererCodeQR();
              java.util.Date dateN = adherent.getDateNaissance();
              java.sql.Date dateNsql = new java.sql.Date(dateN.getTime());
              java.util.Date dateE = adherent.getDateEntree();
              java.sql.Date dateEsql = new java.sql.Date(dateE.getTime());
              PreparedStatement preparedStatement = connect.prepareStatement(query);
              
              preparedStatement.setString(1,adherent.getNom());
              preparedStatement.setString(2,adherent.getPrenom());
              preparedStatement.setDate(3,dateNsql);
              preparedStatement.setString(4,adherent.getGenre());
              //preparedStatement.setBytes(6,membre.getCodeQR());
              preparedStatement.setInt(5,1);
              preparedStatement.setString(6,adherent.getAddress());
              preparedStatement.setString(7,adherent.getEmail());
              preparedStatement.setDate(8,dateEsql);
              // be carful for the conversion
            
              preparedStatement.setString(9,adherent.getTelephone());
              preparedStatement.setDouble(10,adherent.getPoids());
              preparedStatement.setDouble(11,adherent.getTaille());
              preparedStatement.setString(12,data);
              preparedStatement.setString(13,adherent.getPhoto());
              preparedStatement.executeUpdate();
              System.out.println("=:- Membre added !");
              envoyerMailAjout(mail,pass,adherent.getEmail(),"Adherent",adherent.getNom()+" "+adherent.getPrenom());
          }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Méthode pour ajouter un coach à la base de données
    public void ajouterCoach(Coach coach, Connection connect) throws SQLException {
        String query = "INSERT INTO db.utilisateurs(Nom,Prenom,DateNaissance,Genre,Categorie,Adresse_Domicil,Adresse_Mail,DateEntree,Telephone,Poid,Taille,CodeQR,Photo) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try {
              java.sql.Date sqlDate = new java.sql.Date(coach.getDateNaissance().getTime());
              String data = coach.getNom() + "_" + coach.getPrenom() + "_" + sqlDate;
              System.out.println(data);
              coach.genererCodeQR();
            //date conversion
              java.util.Date dateN = coach.getDateNaissance();
              java.sql.Date dateNsql = new java.sql.Date(dateN.getTime());
              java.util.Date dateE = coach.getDateEntree();
              java.sql.Date dateEsql = new java.sql.Date(dateE.getTime());
              PreparedStatement preparedStatement = connect.prepareStatement(query);
              
              preparedStatement.setString(1,coach.getNom());
              preparedStatement.setString(2,coach.getPrenom());
              preparedStatement.setDate(3,dateNsql);
              preparedStatement.setString(4,coach.getGenre());
              //preparedStatement.setBytes(6,membre.getCodeQR());
              preparedStatement.setInt(5,0);
              preparedStatement.setString(6,coach.getAddress());
              preparedStatement.setString(7,coach.getEmail());
              preparedStatement.setDate(8,dateEsql);
              // be carful for the conversion
            
              preparedStatement.setString(9,coach.getTelephone());
              preparedStatement.setDouble(10,coach.getPoids());
              preparedStatement.setDouble(11,coach.getTaille());
              preparedStatement.setString(12,data);
              preparedStatement.setString(13,coach.getPhoto());
              preparedStatement.executeUpdate();
              System.out.println("=:- Membre added !");
              envoyerMailAjout(mail,pass,coach.getEmail(),"Coach",coach.getNom()+" "+coach.getPrenom());
          }catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void supprimerUtilisateur(Connection connect, int userID) {
        String query = "UPDATE utilisateurs SET Supprime = true WHERE MembreID = ?";
        try {
            System.out.println("Utilisateur supprimée");
            PreparedStatement preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1, userID);
            preparedStatement.executeUpdate();
            System.out.println("Utilisateur supprimée");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Adherent> recupererTousLesAdherents(Connection connection) throws SQLException {
        List<Adherent> adherentsList = new ArrayList<>();
        String query = "SELECT * FROM utilisateurs WHERE Categorie = 1  AND Supprime = false"; // Sélectionner seulement les adhérents
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Adherent adherent = new Adherent();
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

    public void ajouterSalle(Connection connect, Salle salle) {
        String query = "INSERT INTO salles (NomSalle, Categorie, LimiteEffectif, Supprime) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, salle.getNomSalle());
            preparedStatement.setString(2, salle.getCategorie());
            preparedStatement.setInt(3, salle.getLimiteEffectif());
            preparedStatement.setBoolean(4, false); // Nouvelle salle, donc non supprimée
            preparedStatement.executeUpdate();
            System.out.println("Salle ajoutée");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprimerSalle(Connection connect, int salleID) {
        String query = "UPDATE salles SET Supprime = true WHERE SalleID = ?";
        try {
            PreparedStatement preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1, salleID);
            preparedStatement.executeUpdate();
            System.out.println("Salle supprimée");
        } catch (SQLException e) {
            e.printStackTrace();
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

    public void ajouterMateriel(Connection connect, Materiel materiel) {
        String query = "INSERT INTO materiels (NomMateriel, Categorie, Quantite, SalleID, Supprime) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, materiel.getNomMateriel());
            preparedStatement.setString(2, materiel.getCategorie());
            preparedStatement.setInt(3, materiel.getQuantite());
            preparedStatement.setInt(4, materiel.getSalleID());
            preparedStatement.setBoolean(5, false); // Nouveau matériel, donc non supprimé
            preparedStatement.executeUpdate();
            System.out.println("Matériel ajouté");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 
    public void supprimerMateriel(Connection connect, int materielID) {
        String query = "UPDATE materiels SET Supprime = true WHERE MaterielID = ?";
        try {
            PreparedStatement preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1, materielID);
            preparedStatement.executeUpdate();
            System.out.println("Matériel supprimé");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

  public void modifierMateriel(Connection connect, Materiel oldMateriel, Materiel newMateriel) {
      try {
          String query = "UPDATE materiels SET NomMateriel=?, Categorie=?, Quantite=?, SalleID=? WHERE MaterielID=?";
          PreparedStatement preparedStatement = connect.prepareStatement(query);
          preparedStatement.setString(1, newMateriel.getNomMateriel());
          preparedStatement.setString(2, newMateriel.getCategorie());
          preparedStatement.setInt(3, newMateriel.getQuantite());
          preparedStatement.setInt(4, newMateriel.getSalleID());
          preparedStatement.setInt(5, oldMateriel.getMaterielID());
          preparedStatement.executeUpdate();
          System.out.println("Matériel modifié");
      } catch (SQLException e) {
          e.printStackTrace();
      }
  }

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


  public void ajouterSport(Connection connect, Sport sport) {
    String query = "INSERT INTO sports (SportCategorie, SportNom, Supprime) VALUES (?, ?, ?)";
    try {
        PreparedStatement preparedStatement = connect.prepareStatement(query);
        preparedStatement.setString(1, sport.getSportCategorie());
        preparedStatement.setString(2, sport.getSportNom());
        preparedStatement.setBoolean(3, false); // Nouveau sport, donc non supprimé
        preparedStatement.executeUpdate();
        System.out.println("Sport ajouté");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public void supprimerSport(Connection connect, int sportID) {
    String query = "DELETE FROM sports WHERE SportID = ?";
    try {
        PreparedStatement preparedStatement = connect.prepareStatement(query);
        preparedStatement.setInt(1, sportID);
        preparedStatement.executeUpdate();
        System.out.println("Sport supprimé");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


public static List<Sport> getListeSports(Connection connection) {
    List<Sport> listeSports = new ArrayList<>();
    String query = "SELECT * FROM sports WHERE Supprime = false";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int sportID = resultSet.getInt("SportID");
                String sportCategorie = resultSet.getString("SportCategorie");
                String sportNom = resultSet.getString("SportNom");
                boolean supprime = resultSet.getBoolean("Supprime");
                Sport sport = new Sport(sportID, sportCategorie, sportNom, supprime);
                listeSports.add(sport);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return listeSports;
}

    public int getSportIDByNom(Connection connect, String sportNom) {
        String query = "SELECT SportID FROM sports WHERE SportNom = ? AND Supprime = false";
        try {
            PreparedStatement preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, sportNom);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("SportID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getSalleIDByNom(Connection connect, String salleNom) {
        String query = "SELECT SalleID FROM salles WHERE NomSalle = ? AND Supprime = false";
        try {
            PreparedStatement preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, salleNom);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("SalleID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void ajouterOffre(Connection connect, Offre offre) {
        String query = "INSERT INTO offres (SportID, Prix, Duree, Categorie, OffreNom, Supprime) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1, offre.getSportID());
            preparedStatement.setDouble(2, offre.getPrix());
            preparedStatement.setInt(3, offre.getDuree());
            preparedStatement.setString(4, offre.getCategorie());
            preparedStatement.setString(5, offre.getOffreNom());
            preparedStatement.setBoolean(6, false); // Nouvelle offre, donc non supprimée
            preparedStatement.executeUpdate();
            System.out.println("Offre ajoutée");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    public void supprimerOffre(Connection connect, int offreID) {
        String query = "UPDATE offres SET Supprime = true WHERE OffreID = ?";
        try {
            PreparedStatement preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1, offreID);
            preparedStatement.executeUpdate();
            System.out.println("Offre supprimée");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static List<Offre> getListeOffres(Connection connection) {
        List<Offre> listeOffres = new ArrayList<>();
        String query = "SELECT * FROM offres WHERE Supprime = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setBoolean(1, false); // Sélectionne uniquement les offres non supprimées
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
    
    // Méthode pour calculer le nombre de groupes associés à un sport
    public static int getNombreGroupesPourSport(Connection connection, int sportID) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM groupes WHERE SportID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, sportID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count");
                }
            }
        }
        return 0;
    }

    public static int getGroupeIDByNom(Connection connection, String GroupeNom) throws SQLException {
        String query = "SELECT GroupeID FROM groupes WHERE GroupeNom  = ? AND Supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, GroupeNom);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("GroupeID");
                }
            }
        }
        return -1;
    }

    // Méthode pour récupérer les coachs associés à un sport spécifique
    public static List<Integer> getCoachesDuSport(Connection connection, int sportID) throws SQLException {
        List<Integer> coaches = new ArrayList<>();
        String query = "SELECT DISTINCT CoachID FROM groupes WHERE SportID = ? AND Supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, sportID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int coachID = resultSet.getInt("CoachID");
                    coaches.add(coachID);
                }
            }
        }
        return coaches;
    }

    // Méthode pour récupérer la liste des groupes non supprimés
    public static List<Groupe> getListeGroupes(Connection connection) {
        List<Groupe> listeGroupes = new ArrayList<>();
        String query = "SELECT * FROM groupes WHERE Supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int groupeID = resultSet.getInt("GroupeID");
                    int sportID = resultSet.getInt("SportID");
                    Time tempsDebut1 = resultSet.getTime("TempsDebut1");
                    Time tempsFin1 = resultSet.getTime("TempsFin1");
                    Time tempsDebut2 = resultSet.getTime("TempsDebut2");
                    Time tempsFin2 = resultSet.getTime("TempsFin2");
                    boolean supprime = resultSet.getBoolean("Supprime");
                    String groupeNom = resultSet.getString("GroupeNom");
                    String jour1 = resultSet.getString("Jour1");
                    String jour2 = resultSet.getString("Jour2");
                    int priorite = resultSet.getInt("Priorite");
                    int taille = resultSet.getInt("Taille");
                    int nbseances = resultSet.getInt("NombreSeances");

                    // Convert Time to LocalTime, handling null values
                    LocalTime localTempsDebut1 = tempsDebut1 != null ? tempsDebut1.toLocalTime() : null;
                    LocalTime localTempsFin1 = tempsFin1 != null ? tempsFin1.toLocalTime() : null;
                    LocalTime localTempsDebut2 = tempsDebut2 != null ? tempsDebut2.toLocalTime() : null;
                    LocalTime localTempsFin2 = tempsFin2 != null ? tempsFin2.toLocalTime() : null;

                    // Create your Groupe object with LocalTime parameters
                    Groupe groupe = new Groupe(groupeID, groupeNom, jour1, jour2, sportID, localTempsDebut1, localTempsDebut2, localTempsFin1, localTempsFin2, taille, priorite, nbseances);

                    listeGroupes.add(groupe);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listeGroupes;
    }

    // Méthode pour supprimer un groupe de la base de données (suppression logique)
    public void supprimerGroupe(Connection connection, int groupeID) throws SQLException {
        String query = "UPDATE groupes SET Supprime = true WHERE GroupeID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, groupeID);
            preparedStatement.executeUpdate();
        }
    }


        // Méthode pour récupérer les adhérents d'un groupe spécifique
        public static List<Adherent> getAdherentsDuGroupe(Connection connection, int groupeID) throws SQLException {
            List<Adherent> adherents = new ArrayList<>();
            String query = "SELECT * FROM utilisateurs WHERE MembreID IN (SELECT MembreID FROM membregroupe WHERE GroupeID = ? AND Supprime = false)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, groupeID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Adherent adherent = new Adherent();
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
                        // Ajouter d'autres attributs selon votre besoin
                        adherents.add(adherent);
                    }
                }
            }
            return adherents;
        }
    
        // Méthode pour récupérer les coachs d'un groupe spécifique
        public static List<Coach> getCoachsDuGroupe(Connection connection, int groupeID) throws SQLException {
            List<Coach> coachs = new ArrayList<>();
            String query = "SELECT * FROM utilisateurs WHERE MembreID IN (SELECT MembreID FROM coachgroupe WHERE GroupeID = ? AND Supprime = false)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, groupeID);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
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
                         coachs.add(coach);
                    }
                }
            }
            return coachs;
        }


        public static int getMembreIDFromNomPrenom(Connection connection, String nom, String prenom) throws SQLException {
            int membreID = 0; // Initialisation avec une valeur par défaut
            
            // Écrivez votre requête SQL pour récupérer le MembreID à partir du nom et du prénom
            String query = "SELECT MembreID FROM utilisateurs WHERE Nom = ? AND Prenom = ? AND Supprime = false";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, nom);
                statement.setString(2, prenom);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    membreID = resultSet.getInt("MembreID");
                }
            }
            
            return membreID;
        }

        public static void ajouterMembreAuGroupe(Connection connection, int membreID, int groupeID) throws SQLException {
            // Vérifiez d'abord si l'enregistrement existe déjà dans la table
            String query = "SELECT COUNT(*) AS count FROM membregroupe WHERE MembreID = ? AND GroupeID = ? AND Supprime = false";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, membreID);
                statement.setInt(2, groupeID);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next() && resultSet.getInt("count") == 0) {
                    // L'enregistrement n'existe pas, alors vous pouvez l'ajouter
                    String insertQuery = "INSERT INTO membregroupe (MembreID, GroupeID) VALUES (?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                        insertStatement.setInt(1, membreID);
                        insertStatement.setInt(2, groupeID);
                        insertStatement.executeUpdate();
                    }
                } else {
                    System.out.println("L'Adherent existe déjà dans la table membregroupe.");
                }
            }
        }

        public static void supprimerMembreDuGroupe(Connection connection, int membreID, int groupeID) throws SQLException {
            // Vérifiez d'abord si l'enregistrement existe dans la table
            String query = "SELECT COUNT(*) AS count FROM membregroupe WHERE MembreID = ? AND GroupeID = ? AND Supprime = false";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, membreID);
                statement.setInt(2, groupeID);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next() && resultSet.getInt("count") > 0) {
                    // L'enregistrement existe, alors vous pouvez le supprimer logiquement
                    String updateQuery = "UPDATE membregroupe SET Supprime = 1 WHERE MembreID = ? AND GroupeID = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setInt(1, membreID);
                        updateStatement.setInt(2, groupeID);
                        updateStatement.executeUpdate();
                    }
                } else {
                    System.out.println("L'Adherent n'existe pas dans la table membregroupe.");
                }
            }
        }

        public static void ajouterCoachAuGroupe(Connection connection, int membreID, int groupeID) throws SQLException {
            // Vérifier d'abord si l'enregistrement n'existe pas déjà dans la table
            String selectQuery = "SELECT COUNT(*) AS count FROM coachgroupe WHERE MembreID = ? AND GroupeID = ? AND Supprime = false";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
                selectStatement.setInt(1, membreID);
                selectStatement.setInt(2, groupeID);
                ResultSet resultSet = selectStatement.executeQuery();
                if (resultSet.next() && resultSet.getInt("count") == 0) {
                    // L'enregistrement n'existe pas, donc vous pouvez l'ajouter
                    String insertQuery = "INSERT INTO coachgroupe (MembreID, GroupeID) VALUES (?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                        insertStatement.setInt(1, membreID);
                        insertStatement.setInt(2, groupeID);
                        insertStatement.executeUpdate();
                    }
                } else {
                    System.out.println("L'enregistrement existe déjà dans la table coachgroupe.");
                }
            }
        }
    
        public static void supprimerCoachDuGroupe(Connection connection, int membreID, int groupeID) throws SQLException {
            // Vérifiez d'abord si l'enregistrement existe dans la table
            String query = "SELECT COUNT(*) AS count FROM coachgroupe WHERE MembreID = ? AND GroupeID = ? AND Supprime = false";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, membreID);
                statement.setInt(2, groupeID);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next() && resultSet.getInt("count") > 0) {
                    // L'enregistrement existe, alors vous pouvez le supprimer logiquement
                    String updateQuery = "UPDATE coachgroupe SET Supprime = 1 WHERE MembreID = ? AND GroupeID = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setInt(1, membreID);
                        updateStatement.setInt(2, groupeID);
                        updateStatement.executeUpdate();
                    }
                } else {
                    System.out.println("L'enregistrement n'existe pas dans la table coachgroupe.");
                }
            }

            
        }

        // Méthode pour calculer le nombre d'adhérents dans un groupe
    public static int getNombreAdherentsDansGroupe(Connection connection, int groupeID) throws SQLException {
        String query = "SELECT COUNT(*) FROM membregroupe WHERE GroupeID = ? AND Supprime = false";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, groupeID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0; // Retourne 0 s'il n'y a aucun adhérent dans le groupe ou s'il y a une erreur
    }

    // Méthode pour calculer le nombre de coachs dans un groupe
    public static int getNombreCoachsDansGroupe(Connection connection, int groupeID) throws SQLException {
        String query = "SELECT COUNT(*) FROM coachgroupe WHERE GroupeID = ? AND Supprime = false";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, groupeID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0; // Retourne 0 s'il n'y a aucun coach dans le groupe ou s'il y a une erreur
    }
    public static void envoyerMail(String mailEnvoyeur, String password, String mailRecepteur, String sujet, String contenu) {
        // Initialisation
        String host = "smtp.gmail.com";
    
        Properties properties = System.getProperties();
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587"); // Port 587 for TLS/STARTTLS
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
    
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailEnvoyeur, password);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailEnvoyeur));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(mailRecepteur));
            message.setSubject(sujet);
    
            // Utilisation du template HTML avec des images et un style CSS
            String template = "<!DOCTYPE html>\n" +
                "<html lang=\"fr\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Newsletter</title>\n" +
                "    <style>\n" +
                "        /* Styles généraux */\n" +
                "        body {\n" +
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            background-color: #F5F5F5;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "            background-color: #BBD3F5;\n" +
                "            border-radius: 10px;\n" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n" +
                "        }\n" +
                "        h1 {\n" +
                "            color: #333333;\n" +
                "            text-align: center;\n" +
                "            font-size: 28px;\n" +
                "            margin-bottom: 20px;\n" +
                "            text-transform: uppercase;\n" +
                "        }\n" +
                "        p {\n" +
                "            color: #444444;\n" +
                "            font-size: 16px;\n" +
                "            line-height: 1.6;\n" +
                "            margin-bottom: 10px;\n" +
                "        }\n" +
                "        img {\n" +
                "            max-width: 100%;\n" +
                "            height: auto;\n" +
                "            display: block;\n" +
                "            margin: 0 auto;\n" +
                "        }\n" +
                "        .highlight {\n" +
                "            color: #FFA500;\n" +
                "            font-weight: bold;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>Newsletter du Club Sportif</h1>\n" +
                
                "        <!-- Logo -->\n" +
                "        <img src=\"https://drive.google.com/uc?id=1CdIILLywtmHnSBhQFd4P_saNapskG5S2\" alt=\"Logo du club sportif\">\n" +
                
                "        <p>Bonjour,</p>\n" +
                "        <p class=\"highlight\">" + contenu + "</p>\n" +
                
                "        <p>Merci de faire partie de notre communauté sportive.</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";


            message.setContent(template, "text/html");
    
            Transport.send(message);
            System.out.println("Message envoyé");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Message non envoyé");
        }
    }
    
    
  public void validerAbonnement(String mailEnvoyer,String password,String mailRecepteur,String Duree,String sportNom){
    String sujet="Validation d'un Abonnement";
    String contenu="Cher(e) adhérent(e),\n vous bénéficiez d'un abonnement de "+ sportNom+" d'une durée de : "+Duree +" Semaines"+" \n cordialement.";
    this.envoyerMail(mailEnvoyer, password, mailRecepteur, sujet, contenu);
}
  public void expirationAbonnement(String mailEnvoyer,String password,String mailRecepteur,String sportNom){
    String sujet="Expiration d'un Abonnement";
    String contenu="Cher(e) adhérent(e) ,\n Votre abonnement de "+ sportNom+" est arrivé à expiration \n cordialement.";
    this.envoyerMail(mailEnvoyer, password, mailRecepteur, sujet, contenu);
  }

  public void envoyerMailAjout(String mailEnvoyer, String password, String mailRecepteur, String type, String nom) {
    String sujet = "Ajout d'un " + type + " au club sportif";
    String contenu = "Cher(e) ,"+type+"(e)\n\nNous sommes heureux de vous informer que " + nom + " a été ajouté en tant que " + type + " dans notre club sportif.\n\nCordialement.";
    envoyerMail(mailEnvoyer, password, mailRecepteur, sujet, contenu);
}

public void envoyerMailSuppression(String mailEnvoyer, String password, String mailRecepteur, String nom) {
    String sujet = "Suppression d'un membre du club sportif";
    String contenu = "Cher(e) membre du club sportif,\n\nNous vous informons que " + nom + " a été retiré de notre club.\n\nCordialement.";
    envoyerMail(mailEnvoyer, password, mailRecepteur, sujet, contenu);
}


  public static Image loadImage(String path) {
        try {
            // Charger l'image à partir du chemin spécifié
            Image image = new Image(path);
            return image;
        } catch (Exception e) {
            // Gérer les exceptions liées au chargement de l'image (par exemple : fichier introuvable)
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
            return null;
        }
    }

    public static void ajouterSeanceGroupeCreneaux(Connection connection){
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        String jour;
        switch (today) {
            case MONDAY -> {
                jour = "Lundi";
                break;
            }
            case TUESDAY -> {
                jour = "Mardi";
                break;
            }
            case WEDNESDAY -> {
                jour = "Mercredi";
                break;
            }
            case THURSDAY -> {
                jour = "Jeudi";
                break;
            }
            case FRIDAY -> {
                jour = "Vendredi";
                break;
            }
            case SATURDAY -> {
                jour = "Samedi";
                break;
            }
            case SUNDAY -> {
                jour = "Dimanche";
                break;
            }
            default -> throw new IllegalStateException("Unexpected value: " + today);
        }
        // Create a scheduled executor service with a single thread
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        // Calculate the initial delay until the next occurrence of the full hour or half hour
        int currentMinute = LocalTime.now().getMinute();
        int currentSecond = LocalTime.now().getSecond();
        int initialDelay;
        if (currentMinute < 30) {
            initialDelay = (30 - currentMinute - 1) * 60 + (60 - currentSecond); // Round to the nearest half hour
        } else {
            initialDelay = (60 - currentMinute - 1) * 60 + (60 - currentSecond); // Round to the next hour ajouter_S_G_C_BD(connection, jour, 23, 33);
        }
        // Schedule the method to be executed every half hour, starting at the next full hour or half hour
        scheduler.scheduleAtFixedRate(() -> ajouter_S_G_C_BD(connection, jour, LocalTime.now().getHour(), LocalTime.now().getMinute()), initialDelay, 30 * 60, TimeUnit.SECONDS);
    }

    private static void ajouter_S_G_C_BD(Connection connection, String jour, int hour, int minute) {
        if (minute == 0 || minute == 30) {
            try {
                // Execute the first query to get CreneauID and SalleID
                try (PreparedStatement statement1 = connection.prepareStatement("SELECT CreneauID, SalleID FROM creneaux WHERE Jour = ? AND HOUR(TempsDebut) = ? AND MINUTE(TempsDebut) = ?")) {
                    statement1.setString(1, jour);
                    statement1.setInt(2, hour);
                    statement1.setInt(3, minute);
                    ResultSet resultSet1 = statement1.executeQuery();
                    while (resultSet1.next()) {
                        int creneauID = resultSet1.getInt("CreneauID");
                        int salleID = resultSet1.getInt("SalleID");
                        // Execute the second query using the result from the first query
                        int groupeID = 0;
                        try (PreparedStatement statement2 = connection.prepareStatement("SELECT GroupeID FROM groupecreneaux WHERE CreneauID = ?")) {
                            statement2.setInt(1, creneauID);
                            ResultSet resultSet2 = statement2.executeQuery();
                            if (resultSet2.next()) {
                                groupeID = resultSet2.getInt("GroupeID");
                                // Execute the third query using the results from the first and second queries
                                try (PreparedStatement statement3 = connection.prepareStatement("INSERT INTO seances (GroupeID, Temps, SalleID) VALUES (?, CURDATE(), ?)")) {
                                    statement3.setInt(1, groupeID);
                                    statement3.setInt(2, salleID);
                                    int rowsInserted = statement3.executeUpdate();
                                    if (rowsInserted > 0) {
                                        System.out.println("A new row has been inserted successfully.");
                                    } else {
                                        System.out.println("Insertion failed.");
                                    }
                                }
                            } else {
                                System.out.println("No data found for the second query.");
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Trophe> recupererTousLesTrophes(Connection connection) {
        List<Trophe> tropheList = new ArrayList<>();
        String query = "SELECT * FROM trophes where Supprime=false"; // Sélectionner seulement les adhérents
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Trophe trophe = new Trophe();
                trophe.setTropheID(resultSet.getInt("TropheID"));
                trophe.setCompetitionNom(resultSet.getString("Competition"));
                trophe.setCoachID(resultSet.getInt("CoachID"));
                trophe.setSportID(resultSet.getInt("SportID"));
                trophe.setCategorie(resultSet.getInt("Categorie"));
                trophe.setAdherentGroupeID(resultSet.getInt("AdherentGroupeID"));
                trophe.setTropheNom(resultSet.getString("TropheNom"));
                tropheList.add(trophe);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tropheList;
    }

    public void supprimerTrophe(Connection connect, int tropheID) {
        String query = "UPDATE trophes SET Supprime = true WHERE TropheID = ?";
        try {
            PreparedStatement preparedStatement = connect.prepareStatement(query);
            preparedStatement.setInt(1, tropheID);
            preparedStatement.executeUpdate();
            System.out.println("Trophe supprimé");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void envoyerRapportRevenues(String mailGerant,String date1,String date2){
    try{
String query="SELECT * FROM revenuesj WHERE Temps Between ? AND ?";
PreparedStatement ps = connectionBd().prepareStatement(query);
ps.setString(1, date1);
ps.setString(2, date2);
double somme=0;
double moyenne=0;
ResultSet rs = ps.executeQuery();
int i =0;
while(rs.next()){

    somme = rs.getDouble("Somme")+somme;
    i++;
}
moyenne=somme/i;
//envoi du donnees
String contenu = "Bonjour, \n ci joint un récapitulatif sur les revenues de "+date1+" jusqu'a "+date2+" \n\n Somme : "+somme+"\n Moyenne des revenues par entrée : "+moyenne+"\n Cordialement.";
envoyerMail(mail, pass, mailGerant, "Rapport sur les revenues", contenu);
System.out.println("Rapport envoyé");
}catch(SQLException e){
    e.printStackTrace();
}
}

public void envoyerRapportDepenses(String mailGerant,String date1,String date2){
    try{
String query="SELECT * FROM revenuesj WHERE Date Between ? AND ?";
PreparedStatement ps = connectionBd().prepareStatement(query);
ps.setString(1, date1);
ps.setString(2, date2);
double somme=0;
double moyenne=0;
ResultSet rs = ps.executeQuery();
int i =0;
while(rs.next()){

    somme = rs.getDouble("Somme")+somme;
    i++;
}
moyenne=somme/i;
//envoi du donnees
String contenu = "Bonjour, \n ci joint un récapitulatif sur les dépenses de "+date1+" jusqu'a "+date2+" \n\n Somme : "+somme+"\n Moyenne des dépenses par entrée : "+moyenne+"\n Cordialement.";
envoyerMail(mail, pass, mailGerant, "Rapport sur les dépenses", contenu);
System.out.println("Rapport envoyé");
}catch(SQLException e){
    e.printStackTrace();
}
}



private static void ajouterAbsences(Connection connection, int membreID, int groupeID, int salleID){
    String query = "SELECT MAX(SeanceID) FROM seances WHERE GroupeID = ? AND SalleID = ?";
    int seanceID=-1;
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setInt(1, groupeID);
        preparedStatement.setInt(2, salleID);

        // Execute the query
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            // Check if there is a result
            if (resultSet.next()) {
                // Retrieve the SeanceID from the result
                seanceID = resultSet.getInt(1);
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    if (seanceID != -1) {

        // Execute the insert statement
        String insertQuery = "INSERT INTO absences (SeanceID, MembreID) VALUES (?, ?)";
        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setInt(1, seanceID);
            insertStatement.setInt(2, membreID);
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Inserted into Absences table successfully.");
    } else {
        System.out.println("No SeanceID found for the given GroupeID and SalleID.");
    }
}

public static List<Seances> recupererTousLesSeancesParGroupe(Connection connection, int groupeID) {
    List<Seances> seancesList = new ArrayList<>();
    String query = "SELECT * FROM seances WHERE GroupeID = ? AND Supprime = false"; // Sélectionner seulement les adhérents
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, groupeID);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Seances seances = new Seances();
                seances.setSeanceID(resultSet.getInt("SeanceID"));
                seances.setGroupeID(resultSet.getInt("GroupeID"));
                seances.setTemps(resultSet.getDate("Temps"));
                seances.setSalleID(resultSet.getInt("SalleID"));
                seancesList.add(seances);
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return seancesList;
}

public static List<Absence> recupererTousLesAbsencesParSeance(Connection connection, int seanceID) {
    List<Absence> absencesList = new ArrayList<>();
    String query = "SELECT * FROM absences WHERE SeanceID = ? and Supprime = false"; // Sélectionner seulement les adhérents
    try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setInt(1, seanceID);
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Absence absence = new Absence();
                absence.setSeanceID(resultSet.getInt("SeanceID"));
                absence.setMembreID(resultSet.getInt("MembreID"));
                absence.setJustification(resultSet.getString("justification"));
                absencesList.add(absence);
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    return absencesList;
}

    public static List<Metrique> recupererTousLesMetriquesParSport(Connection connection, int sportID){
        List<Metrique> metriqueList = new ArrayList<>();
        String query = "SELECT * FROM metriques WHERE SportID = ? AND Supprime = false"; // Sélectionner seulement les adhérents
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, sportID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Metrique metrique = new Metrique();
                    metrique.setMetriqueID(resultSet.getInt("MetriqueID"));
                    metrique.setSportID(resultSet.getInt("SportID"));
                    metrique.setMetriqueNom(resultSet.getString("MetriqueNom"));
                    metriqueList.add(metrique);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return metriqueList;
    }

    public static void ajouterEntrantSortantBD(Connection connection, String code){
        String query = "SELECT MembreID FROM utilisateurs WHERE CodeQR = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, code);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int membreID = resultSet.getInt("MembreID");
                    EntrantSortant entrantSortant = new EntrantSortant();
                    entrantSortant.setMembreID(membreID);
                    entrantSortant.ajouterEntrantSortant(connection,0);
                }
                else {
                    System.out.println("Code QR invalide");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void ajouterMail(Connection connection,String mail,String pass){
        String query = "INSERT INTO mails (Mail,Password) Values(?,?)";
        try{
        PreparedStatement preparedStatement = connection.prepareStatement(query) ;
           preparedStatement.setString(1,mail);
           preparedStatement.setString(2,pass);
          preparedStatement.executeUpdate();
          Gestion.mail = mail;
          Gestion.pass = pass;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    

    }

    public static void   initMail(Connection connection){
        String query = "select * from mails where Supprime=false ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                mail = resultSet.getString("Mail");
                pass = resultSet.getString("Password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    
    public void ajouterlogo(Connection connection,String path){
        String query = "INSERT INTO logos (logoPath) Values(?)";
        try{
        PreparedStatement preparedStatement = connection.prepareStatement(query) ;
           preparedStatement.setString(1,path);
           preparedStatement.executeUpdate();
          Gestion.logoPath = path;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    

    }


    public void supprimerlogo(Connection connection){
        String query = "Update logos SEt Supprime=1  WHERE Supprime=0 ";
        try{
        PreparedStatement preparedStatement = connection.prepareStatement(query); 
          preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    

    }

    public static void initlogo(Connection connection){
        String query = "select * from logos where Supprime=false ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                logoPath = resultSet.getString("logoPath");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void supprimerMail(Connection connection){
        String query = "Update mails SEt Supprime=1  WHERE Supprime=0 ";
        try{
        PreparedStatement preparedStatement = connection.prepareStatement(query); 
          preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    

    }


    public static void ajouterSeance_Groupe_Creneaux_Absences(Connection connection){
        DayOfWeek today = LocalDate.now().getDayOfWeek();
        String jour;
        switch (today) {
            case MONDAY -> {
                jour = "Lundi";
                break;
            }
            case TUESDAY -> {
                jour = "Mardi";
                break;
            }
            case WEDNESDAY -> {
                jour = "Mercredi";
                break;
            }
            case THURSDAY -> {
                jour = "Jeudi";
                break;
            }
            case FRIDAY -> {
                jour = "Vendredi";
                break;
            }
            case SATURDAY -> {
                jour = "Samedi";
                break;
            }
            case SUNDAY -> {
                jour = "Dimanche";
                break;
            }
            default -> throw new IllegalStateException("Unexpected value: " + today);
        }
        // Create a scheduled executor service with a single thread
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        // Calculate the initial delay until the next occurrence of the full hour or half hour
        int currentMinute = LocalTime.now().getMinute();
        int currentSecond = LocalTime.now().getSecond();
        int initialDelay;
        if (currentMinute < 30) {
            initialDelay = (30 - currentMinute - 1) * 60 + (60 - currentSecond); // Round to the nearest half hour
        } else {
            initialDelay = (60 - currentMinute - 1) * 60 + (60 - currentSecond); // Round to the next hour ajouter_S_G_C_BD(connection, jour, 23, 33);
        }
        // Schedule the method to be executed every half hour, starting at the next full hour or half hour
        scheduler.scheduleAtFixedRate(() -> ajouter_S_G_C_BD(connection, jour, LocalTime.now().getHour(), LocalTime.now().getMinute()), initialDelay, 30 * 60, TimeUnit.SECONDS);
	scheduler.scheduleAtFixedRate(() -> ajouter_Abs_BD(connection, jour, LocalTime.now().getHour(), LocalTime.now().getMinute()), initialDelay, 30 * 60, TimeUnit.SECONDS);
    }



    private static void ajouter_Abs_BD(Connection connection, String jour, int hour, int minute) {

            try {
                int entrantsCount = 0;
                Time entrantsLastTime = new Time(0,0,0);
                int sortantsCount = 0;
                Time sortantsLastTime = new Time(0,0,0);

                 Date selectedDay = new Date();

                String query1 = "SELECT CreneauID, TempsDebut, SalleID FROM creneaux WHERE Jour = ? AND HOUR(TempsFin) = ? AND MINUTE(TempsFin) = ?";
                try (PreparedStatement statement1 = connection.prepareStatement(query1)) {
                    // Set parameter for TempsFin
                    statement1.setString(1, jour);
                    statement1.setInt(2, hour);
                    statement1.setInt(3, minute);

                    try (ResultSet resultSet1 = statement1.executeQuery()) {
                        while (resultSet1.next()) {
                            // Retrieve CreneauID and TempsDebut
                            int creneauID = resultSet1.getInt("CreneauID");
                            Time tempsDebut = resultSet1.getTime("TempsDebut");
                            int salleID = resultSet1.getInt("SalleID");
                            System.out.println("|-we got this cre: "+ creneauID + "  and: "+ tempsDebut + "  and: "+ salleID);
                            LocalTime localTime = LocalTime.of(hour, minute);
                            Time tempsFin = Time.valueOf(localTime);

                            long startMillis1 = tempsDebut.getTime();
                            long endMillis1 = tempsFin.getTime();
                            long differenceInMillis1 = endMillis1 - startMillis1;
                            long halfDifferenceInMillis1 = differenceInMillis1 / 2;

                            long halfDiffInMinutes1 = halfDifferenceInMillis1 / (60 * 1000);

                            // Step 2: Execute a query on the groupecreneaux table
                            String query2 = "SELECT GroupeID FROM groupecreneaux WHERE CreneauID = ?";
                            try (PreparedStatement statement2 = connection.prepareStatement(query2)) {
                                // Set parameter for CreneauID
                                statement2.setInt(1, creneauID);

                                try (ResultSet resultSet2 = statement2.executeQuery()) {
                                    while (resultSet2.next()) {
                                        // Retrieve GroupeID
                                        int groupeID = resultSet2.getInt("GroupeID");
                                        System.out.println("|-|-we got this groupe: "+ groupeID);

                                        // Step 3: Execute a query on the membregroupe table
                                        String query3 = "SELECT MembreID FROM membregroupe WHERE GroupeID = ?";
                                        try (PreparedStatement statement3 = connection.prepareStatement(query3)) {
                                            // Set parameter for GroupeID
                                            statement3.setInt(1, groupeID);

                                            try (ResultSet resultSet3 = statement3.executeQuery()) {
                                                while (resultSet3.next()) {
                                                    // Retrieve MembreID
                                                    int membreID = resultSet3.getInt("MembreID");
                                                    System.out.println("|-|-|-we got this membre: "+ membreID);

                                                    String entrantsQuery = "SELECT COUNT(*), MAX(Temps) FROM entrants WHERE MembreID = ? AND DATE(Temps) = ?";
                                                    try (PreparedStatement entrantsStatement = connection.prepareStatement(entrantsQuery)) {
                                                        entrantsStatement.setInt(1, membreID); // Assuming membreID is the current MembreID
                                                        entrantsStatement.setDate(2, (java.sql.Date) new Date(selectedDay.getTime()));

                                                        try (ResultSet entrantsResultSet = entrantsStatement.executeQuery()) {
                                                            if (entrantsResultSet.next()) {
                                                                entrantsCount = entrantsResultSet.getInt(1);
                                                                java.sql.Timestamp entrantsTimestamp = entrantsResultSet.getTimestamp(2);
                                                                System.out.println("||||we got this entrantsCount: "+ entrantsCount + "  and: "+ entrantsTimestamp  );
                                                                if (entrantsTimestamp != null) {
                                                                    entrantsLastTime = Time.valueOf(entrantsTimestamp.toLocalDateTime().toLocalTime());
                                                                }
                                                            }
                                                        }
                                                    }

                                                    // Step 2: Execute queries on the sortants table (similar to entrants)
                                                    String sortantsQuery = "SELECT COUNT(*), MAX(Temps) FROM sortants WHERE MembreID = ? AND DATE(Temps) = ?";
                                                    try (PreparedStatement sortantsStatement = connection.prepareStatement(sortantsQuery)) {
                                                        sortantsStatement.setInt(1, membreID); // Assuming membreID is the current MembreID
                                                        sortantsStatement.setDate(2, (java.sql.Date) new Date(selectedDay.getTime()));

                                                        try (ResultSet sortantsResultSet = sortantsStatement.executeQuery()) {
                                                            if (sortantsResultSet.next()) {
                                                                sortantsCount = sortantsResultSet.getInt(1);
                                                                java.sql.Timestamp sortantsTimestamp = sortantsResultSet.getTimestamp(2);
                                                                System.out.println("||||we got this sortantsCount: "+ sortantsCount + "  and: "+ sortantsTimestamp  );
                                                                if (sortantsTimestamp != null) {
                                                                    sortantsLastTime = Time.valueOf(sortantsTimestamp.toLocalDateTime().toLocalTime());
                                                                }
                                                            }
                                                        }
                                                    }
                                                    if (entrantsCount != 0) {
                                                        int diffrows = entrantsCount - sortantsCount;
                                                        if (diffrows==0){
                                                            tempsFin = sortantsLastTime;
                                                        }
                                                        int compare = entrantsLastTime.compareTo(tempsDebut);
                                                        if (compare > 0){
                                                            tempsDebut = entrantsLastTime;
                                                        }
                                                        long startMillis2 = tempsDebut.getTime();
                                                        long endMillis2 = tempsFin.getTime();
                                                        long differenceInMillis2 = endMillis2 - startMillis2;
                                                        long halfDifferenceInMillis2 = differenceInMillis2 / 2;
                                                        long halfDiffInMinutes2 = halfDifferenceInMillis2 / (60 * 1000);
                                                        if (halfDiffInMinutes2 < halfDiffInMinutes1){
                                                            ajouterAbsences(connection,membreID,groupeID,salleID);
                                                        }
                                                    }
                                                    else {
                                                        ajouterAbsences(connection,membreID,groupeID,salleID);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }

    }

    // prct
    public int  nbrHomme(){
        Connection connect = this.connectionBd();
        try{
        String queryH="SELECT Count(*) FROM utilisateurs WHERE Genre=?";
        PreparedStatement ps = connect.prepareStatement(queryH);
        ps.setString(1,"Masculin");
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            return rs.getInt(1);
        }else{
        return 0;
        }
        }catch(SQLException e){
            e.printStackTrace();
            return 0;
        }
        
        }
        public int  nbrFemme(){
            Connection connect = this.connectionBd();
            try{
            String queryF="SELECT Count(*) FROM utilisateurs WHERE Genre=?";
            PreparedStatement ps = connect.prepareStatement(queryF);
            ps.setString(1,"Feminin");
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }else{
                return 0 ;
            }
            }catch(SQLException e){
                e.printStackTrace();
                return 0 ;
            }
            
        }
        public int  nbrAdherent(){
            Connection connect = this.connectionBd();
            try{
            String queryF="SELECT Count(*) FROM utilisateurs ";
            PreparedStatement ps = connect.prepareStatement(queryF);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }else{
                return 0 ;
            }
            }catch(SQLException e){
                e.printStackTrace();
                return 0 ;
            }
            
        }
        // savoir combien de personne ont nbr abonnements
        public int nbrAbonnements(int nbr){
        int cpt=0;
        Connection connection = this.connectionBd();
        try{
         String query="SELECT DISTINCT MembreID FROM membreoffre";
        PreparedStatement ps= connection.prepareStatement(query);
        ResultSet rs= ps.executeQuery();
        while(rs.next()){
        String query1="SELECT COUNT(*) FROM membreoffre where MembreID=?";
        PreparedStatement ps1 = connection.prepareStatement(query1);
        ps1.setInt(1, rs.getInt("MembreID"));
        ResultSet rs1  = ps1.executeQuery();
      if(rs1.next()){
        if(rs1.getInt(1)==nbr){
            cpt++;
        }
        }}
        }catch(SQLException e){
            e.printStackTrace();
            }
        return cpt;
        }
        public int evolution(LocalDate date){
            Connection connect = this.connectionBd();
            int cpt=0;
            try{
            String queryF="SELECT * FROM utilisateurs  ";
            PreparedStatement ps = connect.prepareStatement(queryF);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String st=rs.getString("DateEntree");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate dateEn = LocalDate.parse(st, formatter);
                if(dateEn.compareTo(date)<=0){
                    cpt ++;
                }
                
            }
        return cpt;
        }catch(SQLException e){
                e.printStackTrace();
                return 0 ;
            }
            
        }
}
    