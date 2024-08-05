package com.club.Model;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
public class Creneau {
private int creneauId;
private String  jour ;
private int  salleId;
private int limiteSalle;
private boolean disponible;
private LocalTime tempsDebut;
private LocalTime tempsFin;
private boolean supprime; // Champ pour la suppression logique

public Creneau(int creneauId,int salleId,String  jour ,LocalTime tempsd,LocalTime tempsf,boolean disponible){
 this.creneauId = creneauId;
 this.salleId = salleId;
 this.jour = jour;
 this.disponible = disponible;   
 this.tempsDebut =tempsd;
 this.tempsFin = tempsf;


}
public Creneau(){}
//getters
public boolean getDisponible(){
    return this.disponible;
}
public int getCreneauId() {
    return creneauId;
}

public String getJour() {
    return jour;
}
public LocalTime getTempsDebut() {
    return tempsDebut;
}
public int getSalleId() {
    return salleId;
}
public LocalTime getTempsFin() {
    return tempsFin;
}
public void setDisponible(boolean disponible) {
    this.disponible = disponible;
}
public void setCreneauId(int creneauId) {
    this.creneauId = creneauId;
}

public void setJour(String jour) {
    this.jour = jour;
}
public void setSalleId(int salleId) {
    this.salleId = salleId;
}
public int getLimiteSalle() {
    return limiteSalle;
}
public void setLimiteSalle(int limiteSalle) {
    this.limiteSalle = limiteSalle;
}
public void setTempsDebut(LocalTime tempsDebut) {
    this.tempsDebut = tempsDebut;
}
public void setTempsFin(LocalTime tempsFin) {
    this.tempsFin = tempsFin;
}
public boolean isSupprime() {
    return supprime;
}
public void setSupprime(boolean supprime) {
    this.supprime = supprime;
}

 // Méthode pour récupérer la liste des créneaux non supprimés
    public static List<Creneau> getListeCreneaux(Connection connection) {
        List<Creneau> listeCreneaux = new ArrayList<>();
        String query = "SELECT * FROM creneaux WHERE Supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int creneauID = resultSet.getInt("CreneauID");
                    String jour = resultSet.getString("Jour");
                    LocalTime tempsDebut = resultSet.getObject("TempsDebut", LocalTime.class);
                    LocalTime tempsFin = resultSet.getObject("TempsFin", LocalTime.class);
                    int salleID = resultSet.getInt("SalleID");
                    boolean disponible = resultSet.getBoolean("Disponible");
                    int lim= resultSet.getInt("limiteSalle");
                    Creneau creneau = new Creneau(0, salleID,jour, tempsDebut, tempsFin,disponible);
                    creneau.setCreneauId(creneauID);
                    listeCreneaux.add(creneau);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listeCreneaux;
    }
    
    // Méthode pour supprimer un créneau de la base de données (suppression logique)
    public void supprimerCreneau(Connection connection) throws SQLException {
        String query = "UPDATE creneaux SET Supprime = true WHERE CreneauID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.creneauId);
            preparedStatement.executeUpdate();
        }
    }


}