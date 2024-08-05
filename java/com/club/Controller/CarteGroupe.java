package com.club.Controller;

public class CarteGroupe {

    private String gN;
    private int GroupeID;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getGroupeID() {
        return GroupeID;
    }
    public void setGroupeID(int GroupeID) {
        this.GroupeID = GroupeID;
    }

    public String getgN() {
        return gN;
    }

    public void setgN(String gN) {
        this.gN = gN;
    }
}
