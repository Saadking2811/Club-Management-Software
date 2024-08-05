package com.club.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class EvaluationGroupe {
    private int evaluationGroupeID;
    private int valeur;
    private int metriqueID;
    private int groupeID;
    private Date date;
    private boolean supprime;

    public EvaluationGroupe(int valeur, int metriqueID, int groupeID) {
        this.valeur = valeur;
        this.metriqueID = metriqueID;
        this.groupeID = groupeID;
        this.date = new Date(); // Initialisation avec la date actuelle
        this.supprime = false; // Par défaut, l'évaluation de groupe n'est pas supprimée
    }

    // Méthode pour effectuer la suppression logique
    public void supprimerEvaluationGroupe(Connection connection) throws SQLException {
        String query = "UPDATE evaluationgroupe SET Supprime = true WHERE EvaluationGroupeID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, this.evaluationGroupeID);
            statement.executeUpdate();
            this.supprime = true;
        }
    }

    // Méthode pour insérer une nouvelle évaluation de groupe
    public void insererEvaluationGroupe(Connection connection) throws SQLException {
        String query = "INSERT INTO evaluationgroupe (Valeur, MetriqueID, GroupeID, Date, Supprime) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, this.valeur);
            statement.setInt(2, this.metriqueID);
            statement.setInt(3, this.groupeID);
            statement.setDate(4, new java.sql.Date(this.date.getTime()));
            statement.setBoolean(5, this.supprime);
            statement.executeUpdate();
        }
    }

    public int getEvaluationGroupeID() {
        return evaluationGroupeID;
    }

    public void setEvaluationGroupeID(int evaluationGroupeID) {
        this.evaluationGroupeID = evaluationGroupeID;
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    public int getMetriqueID() {
        return metriqueID;
    }

    public void setMetriqueID(int metriqueID) {
        this.metriqueID = metriqueID;
    }

    public int getGroupeID() {
        return groupeID;
    }

    public void setGroupeID(int groupeID) {
        this.groupeID = groupeID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isSupprime() {
        return supprime;
    }

    public void setSupprime(boolean supprime) {
        this.supprime = supprime;
    }

    // Autres méthodes de la classe
    
}
