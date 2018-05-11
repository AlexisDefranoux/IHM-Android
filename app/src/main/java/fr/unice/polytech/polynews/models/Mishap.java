package fr.unice.polytech.polynews.models;

/**
 * Created by Alex on 25/03/2018.
 */

public class Mishap {

    private int idMishap;
    private String titleMishap;
    private String category;
    private String description;
    private String location;
    private String locationDetails;
    private String urgency;
    private String email;
    private String date;

    public Mishap(int idMishap, String titleMishap, String category, String description, String location,
                  String locationDetails, String urgency, String email, String date) {
        this.idMishap = idMishap;
        this.titleMishap = titleMishap;
        this.category = category;
        this.description = description;
        this.location = location;
        this.locationDetails = locationDetails;
        this.urgency = urgency;
        this.email = email;
        this.date = date;
    }

    public int getIdMishap() {
        return idMishap;
    }

    public String getTitleMishap() {
        return titleMishap;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getLocationDetails() {
        return locationDetails;
    }

    public String getUrgency() {
        return urgency;
    }

    public String getEmail() {
        return email;
    }

    public String getDate() {
        return date;
    }
}
