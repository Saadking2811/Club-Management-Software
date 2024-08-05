package com.club.Model;
import java.sql.Date;

public class Seances {
    private int seanceID;
    private int groupeID;
    private Date temps;
    private int salleID;

    public Seances(int seanceID, int groupeID, Date temps, int salleID) {
        this.seanceID = seanceID;
        this.groupeID = groupeID;
        this.temps = temps;
        this.salleID = salleID;
    }

    public Seances() {

    }

    public int getSeanceID() {
        return seanceID;
    }

    public void setSeanceID(int seanceID) {
        this.seanceID = seanceID;
    }

    public int getGroupeID() {
        return groupeID;
    }

    public void setGroupeID(int groupeID) {
        this.groupeID = groupeID;
    }

    public Date getTemps() {
        return temps;
    }

    public void setTemps(Date temps) {
        this.temps = temps;
    }

    public int getSalleID() {
        return salleID;
    }

    public void setSalleID(int salleID) {
        this.salleID = salleID;
    }
}

