package com.poligran.redsocial.retos.entities;

import com.google.gson.annotations.SerializedName;

public class Usuario {

    private int id;

    private String name;

    private String urlprofile;

    private String description;

    private boolean active;

    private String last_name;


    private String on_location;

    private int gender_id;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlprofile() {
        return urlprofile;
    }

    public void setUrlprofile(String urlprofile) {
        this.urlprofile = urlprofile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }


    public int getGender_id() {
        return gender_id;
    }

    public void setGender_id(int gender_id) {
        this.gender_id = gender_id;
    }

    public String getOn_location() {
        return on_location;
    }

    public void setOn_location(String on_location) {
        this.on_location = on_location;
    }


}


