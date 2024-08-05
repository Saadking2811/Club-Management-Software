package com.club.Controller;

public class CarteAdmin {


    private String nameAdmin;
    private String nameidAdmin;
    private String picAdmin;
    private String typeAdmin;
    private int AdminID;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getAdminID() {
        return AdminID;
    }
    public void setAdminID(int AID) {
        this.AdminID = AID;
    }


    public String getNameAdmin() {
        return nameAdmin;
    }

    public void setNameAdmin(String nameAdmin) {
        this.nameAdmin = nameAdmin;
    }

    public String getNameidAdmin() {
        return nameidAdmin;
    }

    public void setNameidAdmin(String nameidAdmin) {
        this.nameidAdmin = nameidAdmin;
    }

    public String getPicAdmin() {
        return picAdmin;
    }

    public void setPicAdmin(String picAdmin) {
        this.picAdmin = picAdmin;
    }

    public String getTypeAdmin() {
        return typeAdmin;
    }

    public void setTypeAdmin(String typeAdmin) {
        this.typeAdmin = typeAdmin;
    }
}
