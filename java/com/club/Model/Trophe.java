package com.club.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Trophe {
    private int tropheID;
    private String tropheNom;
    private String competitionNom;
    private int categorie;
    private int sportID;
    private int coachID;
    private int adherentGroupeID;

    private boolean supprimer;

    public Trophe(){}
    public Trophe(int tropheID, String tropheNom, String competitionNom, int categorie, int sportID, int coachID, int adherentGroupeID, boolean supprimer){
        this.tropheID = tropheID;
        this.tropheNom = tropheNom;
        this.competitionNom = competitionNom;
        this.categorie = categorie;
        this.sportID = sportID;
        this.coachID = coachID;
        this.adherentGroupeID = adherentGroupeID;
        this.supprimer = supprimer;
    }


    public void ajouterTrophe(Connection connection) {
        String query = "INSERT INTO trophes (Competition, TropheNom, CoachID, SportID, Categorie, AdherentGroupeID) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, this.competitionNom);
            statement.setString(2, this.tropheNom);
            statement.setInt(3, this.coachID);
            statement.setInt(4, this.sportID);
            statement.setInt(5, this.categorie);
            statement.setInt(6, this.adherentGroupeID);
            statement.executeUpdate();
            System.out.println("Trophe added");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Trophe getTropheByID(Connection connection, int tropheID) throws SQLException {
        String query = "SELECT * FROM trophes WHERE TropheID = ? AND Supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, tropheID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String trophe = resultSet.getString("TropheNom");
                    String comp = resultSet.getString("Competition");
                    int categ = resultSet.getInt("Categorie");
                    int sportid = resultSet.getInt("SportID");
                    int coachid = resultSet.getInt("CoachID");
                    int gagn = resultSet.getInt("AdherentGroupeID");

                    boolean supprime = resultSet.getBoolean("Supprime");
                    return new Trophe(tropheID, trophe, comp, categ, sportid, coachid, gagn, supprime);
                }
            }
        }
        return null;
    }
    public String getTropheNom() {
        return tropheNom;
    }
    public void setTropheNom(String tropheNom) {
        this.tropheNom = tropheNom;
    }

    public int getTropheID() {
        return tropheID;
    }
    public void setTropheID(int tropheID) {
        this.tropheID = tropheID;
    }
    public void setCompetitionNom(String competitionNom) {
        this.competitionNom = competitionNom;
    }
    public void setSportID(int sportID) {
        this.sportID = sportID;
    }
    public void setCoachID(int coachID) {
        this.coachID = coachID;
    }
    public int getCoachID() {
        return coachID;
    }
    public String getCompetitionNom() {
        return competitionNom;
    }
    public int getSportID() {
        return sportID;
    }
    public void setAdherentGroupeID(int adherentGroupeID) {
        this.adherentGroupeID = adherentGroupeID;
    }
    public int getAdherentGroupeID() {
        return adherentGroupeID;
    }

    public void setCategorie(int categorie) {
        this.categorie = categorie;
    }

    public int getCategorie() {
        return categorie;
    }

    public boolean isSupprimer() {
        return supprimer;
    }

    public void setSupprimer(boolean supprimer) {
        this.supprimer = supprimer;
    }
}
