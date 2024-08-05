package com.club.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class MembreOffre {
    private int membreID;
    private int offreID;
    private String temps;
    private boolean supprime;

    public MembreOffre(int membreID, int offreID) {
        this.membreID = membreID;
        this.offreID = offreID;
        this.supprime = false;
    }

    public int getMembreID() {
        return membreID;
    }

    public void setMembreID(int membreID) {
        this.membreID = membreID;
    }

    public int getOffreID() {
        return offreID;
    }

    public void setOffreID(int offreID) {
        this.offreID = offreID;
    }

    public String getTemps() {
        return temps;
    }

    public void setTemps(String temps) {
        this.temps = temps;
    }

    public boolean isSupprime() {
        return supprime;
    }

    public void setSupprime(boolean supprime) {
        this.supprime = supprime;
    }

    public void ajouterMembreOffre(Connection connection) throws SQLException {
        // Vérifier si le MembreOffre existe déjà dans la base de données
        MembreOffre existingMembreOffre = getMembreOffre(connection, membreID, offreID);
        if (existingMembreOffre != null) {
            // S'il existe, mettre à jour le temps vers la date actuelle
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String query = "UPDATE membreoffre SET Temps = ? WHERE MembreID = ? AND OffreID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, currentDate);
                statement.setInt(2, membreID);
                statement.setInt(3, offreID);
                statement.executeUpdate();
            }
        } else {
            // S'il n'existe pas, insérer un nouveau MembreOffre
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String query = "INSERT INTO membreoffre (MembreID, OffreID, Temps, Supprime) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, membreID);
                statement.setInt(2, offreID);
                statement.setString(3, currentDate);
                statement.setBoolean(4, supprime);
                statement.executeUpdate();
            }

        }
        // Envoyer un e-mail de validation
        String mailEnvoyer = Gestion.mail; // Adresse e-mail de l'expéditeur
        String password = Gestion.pass; // Mot de passe de l'expéditeur
        String mailRecepteur = Adherent.recupererAdherentParID(connection, membreID).getEmail(); // Adresse e-mail du destinataire
        String Duree = String.valueOf( Offre.getOffreByID(connection,offreID).getDuree()); // Durée de l'abonnement
        String sportNom = Sport.getSportByID(connection, Offre.getOffreByID(connection,offreID).getSportID()).getSportNom(); // Nom du sport de l'abonnement
        //System.out.println("\n\nmailEnvoyer: "+mailEnvoyer+" password: "+password+" mailRecepteur: "+mailRecepteur+" Duree: "+Duree+" sportNom: "+sportNom);
        new Gestion().validerAbonnement(mailEnvoyer, password, mailRecepteur, Duree, sportNom);
    }

    public void supprimerMembreOffre(Connection connection) throws SQLException {
        String query = "UPDATE membreoffre SET Supprime = true WHERE MembreID = ? AND OffreID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, membreID);
            statement.setInt(2, offreID);
            statement.executeUpdate();
            String mailEnvoyer = Gestion.mail; // Adresse e-mail de l'expéditeur
            String password = Gestion.pass; // Mot de passe de l'expéditeur
            String mailRecepteur = Adherent.recupererAdherentParID(connection, membreID).getEmail(); // Adresse e-mail du destinataire
            String sportNom = Sport.getSportByID(connection, Offre.getOffreByID(connection,offreID).getSportID()).getSportNom(); // Nom du sport de l'abonnement
            new Gestion().expirationAbonnement(mailEnvoyer, password, mailRecepteur, sportNom);
        }
    }

    public static List<MembreOffre> getOffresParMembreID(Connection connection, int membreID) throws SQLException {
        List<MembreOffre> offresList = new ArrayList<>();
        String query = "SELECT * FROM membreoffre WHERE MembreID = ? AND Supprime = false";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, membreID);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    MembreOffre membreOffre = new MembreOffre(
                        resultSet.getInt("MembreID"),
                        resultSet.getInt("OffreID")
                    );
                    membreOffre.setSupprime(resultSet.getBoolean("Supprime"));
                    membreOffre.setTemps(resultSet.getString("Temps"));
                    offresList.add(membreOffre);
                }
            }
        }
        return offresList;
    }

    public static MembreOffre getMembreOffre(Connection connection, int membreID, int offreID) throws SQLException {
        String query = "SELECT * FROM membreoffre WHERE MembreID = ? AND OffreID = ? AND Supprime = false";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, membreID);
            statement.setInt(2, offreID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    MembreOffre membreOffre = new MembreOffre(
                        resultSet.getInt("MembreID"),
                        resultSet.getInt("OffreID")
                    );
                    membreOffre.setSupprime(resultSet.getBoolean("Supprime"));
                    membreOffre.setTemps(resultSet.getString("Temps"));
                    return membreOffre;
                }
            }
        }
        return null;
    }

    public static String calculerDateFin(String tempsDebut, int nombreSemaines) {
        // Convertir la chaîne de temps de début en LocalDate
        LocalDate dateDebut = LocalDate.parse(tempsDebut, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Ajouter le nombre de semaines à la date de début pour obtenir la date de fin
        LocalDate dateFin = dateDebut.plusWeeks(nombreSemaines);

        // Formater la date de fin en une chaîne au format "yyyy-MM-dd"
        return dateFin.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public boolean dateFinAtteinte() throws SQLException {
        Connection connection = new Gestion().connectionBd();
        LocalDate dateFin = LocalDate.parse(calculerDateFin(temps, Offre.getOffreByID(connection,offreID).getDuree()), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate currentDate = LocalDate.now();
        return currentDate.isAfter(dateFin);
    }
}
