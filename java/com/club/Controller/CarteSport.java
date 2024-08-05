package com.club.Controller;

public class CarteSport {


    private String nameSport;


    private String picSport;

    private String categ;

    private int SportID;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getSportID() {
        return SportID;
    }
    public void setSportID(int sportID) {
        this.SportID = sportID;
    }


    public String getNameSport() {
        return nameSport;
    }

    public void setNameSport(String nameSport) {
        this.nameSport = nameSport;
    }


    public String getPicSport() {
        return picSport;
    }

    public void setPicSport(String picSport) {
        this.picSport = picSport;
    }

    public String getCateg() {
        return categ;
    }

    public void setCateg(String categ) {
        this.categ = categ;
    }
}
