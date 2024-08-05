package com.club.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class EvaluationAdherent {
    private int evaluationAdherentID;
    private int valeur;
    private int metriqueID;
    private int membreID;
    private Date date;
    private boolean supprime;

    public EvaluationAdherent(int valeur, int metriqueID, int membreID) {
        this.valeur = valeur;
        this.metriqueID = metriqueID;
        this.membreID = membreID;
        this.date = new Date(); // Initialisation avec la date actuelle
        this.supprime = false; // Par défaut, l'évaluation d'adhérent n'est pas supprimée
    }

    // Méthode pour effectuer la suppression logique
    public void supprimerEvaluationAdherent(Connection connection) throws SQLException {
        String query = "UPDATE evaluationadherent SET Supprime = true WHERE EvaluationAdherentID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, this.evaluationAdherentID);
            statement.executeUpdate();
            this.supprime = true;
        }
    }

    // Méthode pour insérer une nouvelle évaluation d'adhérent
    public void insererEvaluationAdherent(Connection connection) throws SQLException {
        String query = "INSERT INTO evaluationadherent (Valeur, MetriqueID, MembreID, Date, Supprime) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, this.valeur);
            statement.setInt(2, this.metriqueID);
            statement.setInt(3, this.membreID);
            statement.setDate(4, new java.sql.Date(this.date.getTime()));
            statement.setBoolean(5, this.supprime);
            statement.executeUpdate();
        }
    }

    public int getEvaluationAdherentID() {
        return evaluationAdherentID;
    }

    public void setEvaluationAdherentID(int evaluationAdherentID) {
        this.evaluationAdherentID = evaluationAdherentID;
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

    public int getMembreID() {
        return membreID;
    }

    public void setMembreID(int membreID) {
        this.membreID = membreID;
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
