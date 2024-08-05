package com.club.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalleGroupe {
    private int sallegroupeID;
    private int salleID;
    private int groupeID;
    private boolean supprime;

    public SalleGroupe(int sallegroupeID, int salleID, int groupeID, boolean supprime) {
        this.sallegroupeID = sallegroupeID;
        this.salleID = salleID;
        this.groupeID = groupeID;
        this.supprime = supprime;
    }

    public int getSallegroupeID() {
        return sallegroupeID;
    }

    public void setSallegroupeID(int sallegroupeID) {
        this.sallegroupeID = sallegroupeID;
    }

    public int getSalleID() {
        return salleID;
    }

    public void setSalleID(int salleID) {
        this.salleID = salleID;
    }

    public int getGroupeID() {
        return groupeID;
    }

    public void setGroupeID(int groupeID) {
        this.groupeID = groupeID;
    }

    public boolean isSupprime() {
        return supprime;
    }

    public void setSupprime(boolean supprime) {
        this.supprime = supprime;
    }

    public void ajouterSalleGroupe(Connection connection) {
        if (salleGroupeExiste(connection)) {
            System.out.println("La salle pour ce groupe existe déjà.");
            return; // Sortie de la méthode si la salle existe déjà
        }
    
        try {
            String query = "INSERT INTO sallegroupe (SalleID, GroupeID) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, this.salleID);
            ps.setInt(2, this.groupeID);
            ps.executeUpdate();
            System.out.println("Salle ajoutée pour le groupe.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public static List<SalleGroupe> getListeSallesGroupes(Connection connection) {
        List<SalleGroupe> listeSallesGroupes = new ArrayList<>();
        String query = "SELECT * FROM sallegroupe WHERE Supprime =false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int sallegroupeID = resultSet.getInt("SalleGroupeID");
                    int salleID = resultSet.getInt("SalleID");
                    int groupeID = resultSet.getInt("GroupeID");
                    boolean supprime = resultSet.getBoolean("Supprime");
                    SalleGroupe salleGroupe = new SalleGroupe(sallegroupeID, salleID, groupeID, supprime);
                    listeSallesGroupes.add(salleGroupe);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listeSallesGroupes;
    }

    public static void supprimerSalleGroupe(Connection connection,int salleID,int groupeID) {
        try {
            String query = "UPDATE sallegroupe SET Supprime = true WHERE SalleID = ? AND GroupeID = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, salleID);
            ps.setInt(2, groupeID);
            ps.executeUpdate();
            System.out.println("SalleGroupe deleted (logical)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean salleGroupeExiste(Connection connection) {
        try {
            String query = "SELECT COUNT(*) AS count FROM sallegroupe WHERE SalleID = ? AND GroupeID = ? AND Supprime =false";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, this.salleID);
            ps.setInt(2, this.groupeID);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Integer> getSallesByGroupe(Connection connection, int groupeID) {
        List<Integer> salles = new ArrayList<>();
        String query = "SELECT SalleID FROM sallegroupe WHERE GroupeID = ? AND Supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, groupeID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int salleID = resultSet.getInt("SalleID");
                    salles.add(salleID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salles;
    }
    
}

