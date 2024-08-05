package com.club.Controller;

public class CarteTrophe {

    private String nameTrophe;
    private String nameCompetition;
    private String nameCoach;
    private String nameSport;
    private String nameGagnant;
    private String picTrophe;
    private int TropheID;
    private boolean selected;

    public String getNameTrophe() {
        return nameTrophe;
    }

    public void setNameTrophe(String nameTrophe) {
        this.nameTrophe = nameTrophe;
    }

    public String getNameCompetition() {
        return nameCompetition;
    }

    public void setNameCompetition(String nameCompetition) {
        this.nameCompetition = nameCompetition;
    }

    public String getNameCoach() {
        return nameCoach;
    }

    public void setNameCoach(String nameCoach) {
        this.nameCoach = nameCoach;
    }

    public String getNameSport() {
        return nameSport;
    }

    public void setNameSport(String nameSport) {
        this.nameSport = nameSport;
    }

    public String getNameGagnant() {
        return nameGagnant;
    }

    public void setNameGagnant(String nameGagnant) {
        this.nameGagnant = nameGagnant;
    }

    public String getPicTrophe() {
        return picTrophe;
    }

    public void setPicTrophe(String picTrophe) {
        this.picTrophe = picTrophe;
    }

    public int getTropheID() {
        return TropheID;
    }

    public void setTropheID(int tropheID) {
        TropheID = tropheID;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
