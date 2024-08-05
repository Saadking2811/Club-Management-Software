package com.club.Model;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalTime;
import java.sql.* ;

public class Groupe {
private String nomGroupe;
    private int groupeId;
 private int sportID;
private int nombreSeances;
private int coachId;
private String jour1;
private LocalTime tempsd1;
private LocalTime tempsf1;
private String jour2;
private LocalTime tempsd2;
private LocalTime tempsf2;
private int taille;
private int priorite;
public Groupe() {

    }
public Groupe(int groupeId,String nomGroupe,String jour1,String jour2,int sportID,LocalTime tempsd1,LocalTime tempsd2,LocalTime tempsf1,LocalTime tempsf2,int taille,int priorite,int nombreSeances){
this.sportID=sportID;
    this.nomGroupe = nomGroupe;
this.groupeId = groupeId;
this.jour1 = jour1;
this.jour2 = jour2;
this.tempsd1 = tempsd1;
this.tempsd2 = tempsd2;
this.tempsf1  = tempsf1 ; 
this.tempsf2 = tempsf2;
this.taille = taille; 
this.priorite = priorite;
this.nombreSeances = nombreSeances;
}
//getters et setters

public int getPriorite() {
    return priorite;

}
public String getJour1() {
    return jour1;
}
public String getJour2() {
    return jour2;
}
public LocalTime getTempsd1() {
    return tempsd1;
}
public LocalTime getTempsd2() {
    return tempsd2;
}
public LocalTime getTempsf1() {
    return tempsf1;
}
public LocalTime getTempsf2() {
    return tempsf2;
}
public int getTaille() {
    return taille;
}
public int getNombreSeances() {
    return nombreSeances;
}
public int getGroupeId() {
    return groupeId;
}

public void setNombreSeances(int nombreSeances) {
    this.nombreSeances = nombreSeances;
}
public String getNomGroupe() {
    return nomGroupe;
}
public void setJour1(String jour1) {
    this.jour1 = jour1;
}
public void setJour2(String jour2) {
    this.jour2 = jour2;
}
public void setTempsd1(LocalTime tempsd1) {
    this.tempsd1 = tempsd1;
}
public void setTempsd2(LocalTime tempsd2) {
    this.tempsd2 = tempsd2;
}
public void setTempsf1(LocalTime tempsf1) {
    this.tempsf1 = tempsf1;
}
public void setTempsf2(LocalTime tempsf2) {
    this.tempsf2 = tempsf2;
}
public void setPriorite(int priorite) {
    this.priorite = priorite;
}
public void setGroupeId(int groupeId) {
    this.groupeId = groupeId;
}
public void ajouterGroupe(Connection connect){
    try{
    String query = "INSERT INTO groupes (Taille,Priorite,NombreSeances,GroupeNom) VALUES(?,?,?,?)";
PreparedStatement ps = connect.prepareStatement(query);

ps.setInt(1,this.taille);
ps.setInt(2,this.priorite);
ps.setInt(3,this.nombreSeances);
ps.setString(4, this.nomGroupe);
ps.executeUpdate();
System.out.println("Group added");
    }catch(SQLException e){
        e.printStackTrace();
    }
}
public void mettreAJourGroupe(Connection connection) throws SQLException {
    if (this.tempsd1 != null) {
    String query = "UPDATE groupes SET  SportID = ?, TempsDebut1 = ?, TempsFin1 = ?, TempsDebut2 = ?, TempsFin2 = ?, GroupeNom = ?, Jour1 = ?, Jour2 = ?,Priorite = ? WHERE GroupeID = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setInt(1, this.sportID);
        
        preparedStatement.setString(2, String.valueOf(this.tempsd1));
        preparedStatement.setString(3, String.valueOf(this.tempsf1));
        preparedStatement.setString(4, String.valueOf(this.tempsd2));
        preparedStatement.setString(5, String.valueOf(this.tempsf2));
    
        preparedStatement.setString(6, this.nomGroupe);
        preparedStatement.setString(7, this.jour1);
        preparedStatement.setString(8, this.jour2);
        preparedStatement.setInt(9, this.priorite);
        preparedStatement.setInt(10, this.groupeId);
        preparedStatement.executeUpdate();
    }

        
    }else{
        String query = "UPDATE groupes SET  SportID = ?, GroupeNom = ?, Jour1 = ?, Jour2 = ?,Priorite = ? WHERE GroupeID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.sportID);
        
            preparedStatement.setString(2, this.nomGroupe);
            preparedStatement.setString(3, this.jour1);
            preparedStatement.setString(4, this.jour2);
            preparedStatement.setInt(5, this.priorite);
            preparedStatement.setInt(6, this.groupeId);
            preparedStatement.executeUpdate();
        
    }
}
}
    // Méthode pour récupérer un groupe par ID
    public static Groupe getGroupeByID(Connection connection, int groupeID) throws SQLException {
        String query = "SELECT * FROM groupes WHERE GroupeID = ? AND Supprime = false";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, groupeID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
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

                    // Convert Time to LocalTime, handling null values
                    LocalTime localTempsDebut1 = tempsDebut1 != null ? tempsDebut1.toLocalTime() : null;
                    LocalTime localTempsFin1 = tempsFin1 != null ? tempsFin1.toLocalTime() : null;
                    LocalTime localTempsDebut2 = tempsDebut2 != null ? tempsDebut2.toLocalTime() : null;
                    LocalTime localTempsFin2 = tempsFin2 != null ? tempsFin2.toLocalTime() : null;

                    // Create Groupe object using existing constructor
                    Groupe groupe = new Groupe(groupeID, groupeNom, jour1, jour2, sportID, localTempsDebut1, localTempsDebut2, localTempsFin1, localTempsFin2, 0, priorite, 0);
                    return groupe;
                }
            }
        }
        return null;
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

    // Méthode pour supprimer un groupe de la base de données (suppression logique)
    public void supprimerGroupe(Connection connection) throws SQLException {
        String query = "UPDATE groupes SET Supprime = true WHERE GroupeID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, this.groupeId);
            preparedStatement.executeUpdate();
        }
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

}
