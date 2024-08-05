package com.club.Controller;

import com.club.Model.Sport;

public class CarteOffre {


    private String OffreActPrem;

    private String OffrePrixPrem;

    private String picOffre;

    private String type;

    private int duree;

    private String sport;

    

    private int OffreID;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }
    

    public void setSport(String sport) {
        this.sport = sport;
    }
    public String getSport() {
        return sport;
    }
    public int getOffreID() {
        return OffreID;
    }
    public void setOffreID(int AID) {
        this.OffreID = AID;
    }


    public String getOffreActPrem() {
        return OffreActPrem;
    }

    public void setOffreActPrem(String offreActPrem) {
        OffreActPrem = offreActPrem;
    }

    public String getOffrePrixPrem() {
        return OffrePrixPrem;
    }

    public void setOffrePrixPrem(String offrePrixPrem) {
        OffrePrixPrem = offrePrixPrem;
    }


    public String getPicOffre() {
        return picOffre;
    }

    public void setPicOffre(String picOffre) {
        this.picOffre = picOffre;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
