package com.club.Model;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.sql.Time;

//import com.google.protobuf.Timestamp;;
public class Planning {
//Attr
// arrays to manipulate the timeslots locally
// array of sessions
Creneau [] creneaux = new Creneau [200];
Groupe [] groupes = new Groupe[200];
int [][] groupeCreneaux = new int[200][2];
int nombreSeances=0;
public void ajouterCreneau(Connection connect,String jour,LocalTime tempsd,LocalTime tempsf,int  salleID){
    if(!avoirChevauchement(connect, jour,tempsd, tempsf, salleID)){
try{
 //traitemt de chevauchement   
PreparedStatement ps = connect.prepareStatement("INSERT INTO Creneaux (TempsDebut,TempsFin,SalleID,Jour) VALUES(?,?,?,?)");
ps.setObject(1, tempsd);
ps.setObject(2,tempsf);
ps.setInt(3,salleID);
ps.setString(4, jour);
ps.executeUpdate(); 
System.out.println("Done");
}
catch(SQLException e){
    
    
e.printStackTrace();
}
}else{
System.out.println("error");
}
}
public void modifierCreneau(Connection connect,LocalDateTime tempsda,LocalDateTime tempsfa,LocalDateTime tempsdn,LocalDateTime tempsfn)
{ 
    try{
    PreparedStatement ps = connect.prepareStatement("UPDATE Creneaux SET TempsDebut=?,TempsFin=? WHERE TempsDebut=?,TempsFin=?");
   ps.setObject(1,tempsdn);
   ps.setObject(2,tempsfn);
   ps.setObject(3,tempsda);
   ps.setObject(4,tempsfa);
   ps.executeUpdate();
    } catch(SQLException e){
        e.printStackTrace();
    }
}
public void supprimerCreneau(Connection connect,LocalDateTime tempsd,LocalDateTime tempsf){
    try{
 //traitemt de chevauchement  
    PreparedStatement ps = connect.prepareStatement("DELETE FROM Creneaux WHERE TempsDebut=? AND TempsFin=?");
    ps.setObject(1, tempsd);
    ps.setObject(2,tempsf);
    ps.executeUpdate();
    }
    catch(SQLException e){
    e.printStackTrace();
    }
    }


//traitement de chevauchement
//retourne vrai si il y'a chevauchement
public boolean avoirChevauchement(Connection connect,String jour,LocalTime tempsd,LocalTime tempsf,int salleID){
String sqlQuery="SELECT * FROM Creneaux Where SalleID=? and Jour=?";
boolean bool=false;
try{
PreparedStatement preparedStatement =connect.prepareStatement(sqlQuery);
preparedStatement.setInt (1,salleID);
preparedStatement.setString(2,jour);
ResultSet resultset = preparedStatement.executeQuery();
while(resultset.next()){
Time tempsdd =resultset.getTime("TempsDebut");
Time tempsff =resultset.getTime("TempsFin");
LocalTime tempsDebut=tempsdd.toLocalTime();
LocalTime tempsFin=tempsff.toLocalTime();
if ((tempsd.equals(tempsDebut)) ||((tempsd.isBefore(tempsDebut) && tempsf.isAfter(tempsDebut))) ||((tempsd.isAfter(tempsDebut)&&tempsd.isBefore(tempsFin))))
{
bool=true;
}
}
}catch(SQLException e){
e.printStackTrace();
}
return bool;
}
// i
// generation du planning
public void importCreneaux(Connection connect){
String query="SELECT * FROM creneaux WHERE Disponible=true";
try {
int limite=200;
PreparedStatement ps = connect.prepareStatement(query);
ResultSet rs = ps.executeQuery();
int i=0;
while(rs.next()){
Creneau cr = new Creneau(rs.getInt("CreneauID"),rs.getInt("SalleID"),rs.getString("Jour"),
rs.getObject("TempsDebut", LocalTime.class),rs.getObject("TempsFin",LocalTime.class),rs.getBoolean("Disponible"));
creneaux[i] = cr;
i++;
}
} catch(SQLException e){
    e.printStackTrace();
}
} 
public void importGroupes(Connection connect){
    String query="SELECT * FROM groupes";
    try {
    int i=0;
    PreparedStatement ps = connect.prepareStatement(query);
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
    Groupe grp = new Groupe(rs.getInt("GroupeID"), rs.getString("GroupeNom"),rs.getString("Jour1"), rs.getString("Jour2"),rs.getInt("SportID"),rs.getObject("TempsDebut1",LocalTime.class),  rs.getObject("TempsDebut2",LocalTime.class),
     rs.getObject("TempsFin1",LocalTime.class),  rs.getObject("TempsFin2",LocalTime.class),rs.getInt("Taille"),rs.getInt("Priorite"),rs.getInt("NombreSeances"));
      groupes[i] = grp;
      i++;
    }
    }catch(SQLException e){
        e.printStackTrace();
    }
}
Gestion gestion = new Gestion();

public boolean remplacerCreneau(int ecartJour,int ecartTempsD,int ecartTempsF,Creneau creneau,Groupe groupe){
// init du jour
Integer j1=0,j2=0,jc=0;
int somme = ecartJour + ecartTempsD +ecartTempsF;
switch(creneau.getJour()){
    
    case  "Dimanche": jc=1;
    break;
     case "Lundi": jc=2;
    break;
    case "Mardi": jc=3;
    break;
    case "Mercredi": jc=4;
    break;
    case "Jeudi": jc=5;
    break;
    case "Vendredi": jc=6;
    break;
    case "Samedi": jc=7;
    break;
}
switch(groupe.getJour1()){
    case  "Dimanche": j1=1;
    break;
     case "Lundi": j1=2;
    break;
    case "Mardi": j1=3;
    break;
    case "Mercredi": j1=4;
    break;
    case "Jeudi": j1=5;
    break;
    case "Vendredi": j1=6;
    break;
    case "Samedi": j1=7;
    break;
}
switch(groupe.getJour2()){
    case  "Dimanche": j2=1;
    break;
     case "Lundi": j2=2;
    break;
    case "Mardi": j2=3;
    break;
    case "Mercredi": j2=4;
    break;
    case "Jeudi": j2=5;
    break;
    case "Vendredi": j2=6;
    break;
    case "Samedi": j2=7;
    break;
}
boolean possible=false;
if((jc.compareTo(j1) + creneau.getTempsDebut().compareTo(groupe.getTempsd1()) + creneau.getTempsFin().compareTo(groupe.getTempsf1())) < somme)
{
    possible = true;
    ecartJour=jc.compareTo(j1) ;
    ecartTempsD=creneau.getTempsDebut().compareTo(groupe.getTempsd1());
    ecartTempsF=creneau.getTempsFin().compareTo(groupe.getTempsf1());
    somme= ecartJour + ecartTempsD +ecartTempsF;
}
if((jc.compareTo(j2) + creneau.getTempsDebut().compareTo(groupe.getTempsd2()) + creneau.getTempsFin().compareTo(groupe.getTempsf2())) < somme)
{
    possible = true;
    ecartJour=jc.compareTo(j1) ;
    ecartTempsD=creneau.getTempsDebut().compareTo(groupe.getTempsd1());
    ecartTempsF=creneau.getTempsFin().compareTo(groupe.getTempsf1());
    somme= ecartJour + ecartTempsD +ecartTempsF;
}
return possible;

}
//ajout groupecreneau
public void ajouterGroupeCreneau(Connection connect,int groupID,int creneauID ){
try{
String query="INSERT INTO groupeCreneaux (GroupeID,CreneauID) Values(?,?)";
PreparedStatement ps = connect.prepareStatement(query);
ps.setInt(1,groupID);
ps.setInt(2, creneauID);
ps.executeUpdate();
}catch(SQLException e){
e.printStackTrace();
}
}
//
public void genererPlanning(){
    int nombreSeances=0;

Connection connect= gestion.connectionBd();
// import des creneaux et des groupes
importCreneaux(connect);
importGroupes(connect);
System.out.println("test");

// traitement par priorité
//variables pour stocker les écarts
int ecartJour;
int ecartTempsD;
 int ecartTempsF;
 int creneauId;
for(int i=1;i<4;i++){
int j=0;
 while(groupes[j]!=null){
if(groupes[j].getPriorite()!= i){
    j++;
continue;
}
while(groupes[j].getNombreSeances()!=0){
int k=0;
// init des ecarts
ecartJour=1000;
ecartTempsD=1000;
ecartTempsF=1000;
creneauId = 0;
while(creneaux[k]!=null){
if(creneaux[k].getDisponible()!=true){
    k++;
continue;
}
if(groupes[j].getTempsd1()!=null && groupes[j].getJour1()!=null && groupes[j].getTempsd2()!=null && groupes[j].getTempsf1()!=null && groupes[j].getTempsf2()!=null && groupes[j].getJour2()!=null)
{if(remplacerCreneau(ecartJour, ecartTempsD, ecartTempsF,creneaux[k], groupes[j])){
creneauId = creneaux[k].getCreneauId();
}}else{
    if(creneaux[k].getDisponible()){
    creneauId = creneaux[k].getCreneauId();
    break;
    }
}
k++;
}
// set creneau non dispo
 k = 0;
while(creneaux[k]!=null){
if(creneaux[k].getCreneauId() == creneauId){
creneaux[k].setDisponible(false);
break;
}
k++;
}
// affectation du creneau au groupe
groupeCreneaux[nombreSeances][0]=groupes[j].getGroupeId();
groupeCreneaux[nombreSeances][1]=creneauId;
nombreSeances++;
// decrementation du nombre de seance restant
groupes[j].setNombreSeances(groupes[j].getNombreSeances()-1);

 }
 j++;
}

}

// ajout du nouvel planning à la BD 
int i=0;
while(i<=nombreSeances){
ajouterGroupeCreneau(connect, groupeCreneaux[i][0], groupeCreneaux[i][1]);
i++;
}
System.out.println("planning generated successfully");
}
public boolean echangerCreneau( String tempsd1,String tempsf1,String tempsd2,String tempsf2,String jour1,String jour2,int groupeId){
//recherche du creneauID1
try{

String query1="SELECT * FROM creneaux WHERE Jour=? AND TempsDebut=? AND TempsFin=?";
Connection connect = gestion.connectionBd();
PreparedStatement ps1 = connect.prepareStatement(query1);
ps1.setString(1,jour1);
ps1.setString(2,tempsd1);
ps1.setString(3,tempsf1);
ResultSet rs1 = ps1.executeQuery();
ResultSet rs2=null;
while(rs1.next()){
    String query2="SELECT * FROM groupecreneaux WHERE GroupeID=? AND CreneauID=?";
    PreparedStatement ps2 = connect.prepareStatement(query2);
    ps2.setInt(1,groupeId);
    ps2.setInt(2,rs1.getInt("CreneauID"));
    rs2 = ps2.executeQuery();
    if(rs2.next()){
        break;
    }
    
}
int id1 = rs2.getInt("CreneauID");
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        
//conversion
LocalTime tempsDebut1 = LocalTime.parse(tempsd1, formatter);
LocalTime tempsFin1 = LocalTime.parse(tempsf1, formatter);
LocalTime tempsDebut2 = LocalTime.parse(tempsd2, formatter);
LocalTime tempsFin2 = LocalTime.parse(tempsf2, formatter);

String query0="SELECT * FROM creneaux WHERE Jour=? AND TempsDebut=? AND TempsFin=? AND SalleID=?";
if(!avoirConflit(connect, jour2,tempsDebut2 ,tempsFin2,rs1.getInt("SalleID"))){
if(avoirChevauchement(connect, jour2, tempsDebut2, tempsFin2, rs1.getInt("SalleID"))){
String query8="DELETE FROM creneaux WHERE Jour=? AND TempsDebut=? AND TempsFin=? AND SalleID=?";
PreparedStatement ps8 =connect.prepareStatement(query8);
ps8.setString(1,jour2);
ps8.setString(2,tempsd2);
ps8.setString(3,tempsf2);
ps8.setInt(4,rs1.getInt("SalleID"));
ps8.executeUpdate();
}
String query3="Insert into creneaux (Jour,TempsDebut,TempsFin,SalleID,Disponible)  Values(?,?,?,?,false)";
PreparedStatement ps3 = connect.prepareStatement(query3);
ps3.setString(1,jour2);
ps3.setString(2,tempsd2);
ps3.setString(3, tempsf2);
ps3.setInt(4,rs1.getInt("SalleID") );
ps3.executeUpdate();
String query4="DELETE FROM groupecreneaux WHERE CreneauID=?";
PreparedStatement ps4 = connect.prepareStatement(query4);
ps4.setInt(1, id1);
ps4.executeUpdate();
String query5="Insert INTO groupecreneaux(GroupeID,CreneauID) Values(?,?)";
PreparedStatement ps5 = connect.prepareStatement(query5);
ps5.setInt(1,groupeId);
String query6="SELECT * FROM creneaux WHERE Jour=? AND TempsDebut=? AND TempsFin=? AND SalleID=?";
PreparedStatement ps6 = connect.prepareStatement(query6);
ps6.setString(1,jour2);
ps6.setString(2,tempsd2);
ps6.setString(3, tempsf2);
ps6.setInt(4,rs1.getInt("SalleID") );
ResultSet rs6 = ps6.executeQuery();
if(rs6.next()){
ps5.setInt(2,rs6.getInt("CreneauID"));
}
 ps5.executeUpdate();
 return true;
}else{
return false;
}
}catch(SQLException e){

    e.printStackTrace();
    return false;
}
}
public boolean avoirConflit(Connection connect,String jour,LocalTime tempsd,LocalTime tempsf,int salleID){
    String sqlQuery="SELECT * FROM Creneaux Where SalleID=? and Jour=?";
    boolean bool=false;
    try{
    PreparedStatement preparedStatement =connect.prepareStatement(sqlQuery);
    preparedStatement.setInt (1,salleID);
    preparedStatement.setString(2,jour);
    ResultSet resultset = preparedStatement.executeQuery();
    while(resultset.next()){
    Time tempsdd =resultset.getTime("TempsDebut");
    Time tempsff =resultset.getTime("TempsFin");
    LocalTime tempsDebut=tempsdd.toLocalTime();
    LocalTime tempsFin=tempsff.toLocalTime();
    if (((tempsd.equals(tempsDebut)) ||((tempsd.isBefore(tempsDebut) && tempsf.isAfter(tempsDebut))) ||((tempsd.isAfter(tempsDebut)&&tempsd.isBefore(tempsFin))))&&(resultset.getBoolean("Disponible")==false))
    {
    bool=true;
    }
    }
    }catch(SQLException e){
    e.printStackTrace();
    }
    return bool;
    }
}