package com.club.Model;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public  class Membre extends Utilisateurs{
    private int memberID;
    private int abonnementID;
    private int groupeID;

    public Membre() {

    }
    public Membre(int memberID, int abonnementID, int groupeID, String nom, String prenom, Date dateNaissance, int categorie,
                  Date dateEntree, String genre, int age, String email, String address, String telephone, double poids, double taille) {
        super(nom,prenom,dateNaissance,genre,1,email,address,dateEntree,telephone,poids,taille);
        this.memberID = memberID;
        this.abonnementID = abonnementID;
        this.groupeID = groupeID;
        this.setNom(nom);
        this.setPrenom(prenom);

    }
    public int getMemberID() {return memberID;}
    public int getAbonnementID() {return abonnementID;}
    public int getGroupeID() {return groupeID;}
    public void setMemberID(int memberID) {this.memberID = memberID;}
    public void setAbonnementID(int abonnementID) {this.abonnementID = abonnementID;}
    public void setGroupeID(int groupeID) {this.groupeID = groupeID;}
}