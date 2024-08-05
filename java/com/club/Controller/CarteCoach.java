package com.club.Controller;

public class CarteCoach {

    private String nameCoach;
    private String nameidCoach;
    private String picCoach;
    private String typeCoach;
    private int CoachID;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getCoachID() {
        return CoachID;
    }
    public void setCoachID(int coachID) {
        this.CoachID = coachID;
    }

    public String getNameCoach() {
        return nameCoach;
    }

    public void setNameCoach(String nameCoach) {
        this.nameCoach = nameCoach;
    }

    public String getNameidCoach() {
        return nameidCoach;
    }

    public void setNameidCoach(String nameidCoach) {
        this.nameidCoach = nameidCoach;
    }

    public String getPicCoach() {
        return picCoach;
    }

    public void setPicCoach(String picCoach) {
        this.picCoach = picCoach;
    }

    public String getTypeCoach() {
        return typeCoach;
    }

    public void setTypeCoach(String typeCoach) {
        this.typeCoach = typeCoach;
    }


}
