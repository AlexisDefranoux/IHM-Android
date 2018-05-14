package fr.unice.polytech.polynews.models;

/**
 * Created by Florent on 14/05/2018.
 */

public class User {

    private int id;
    private String email;
    private String name;
    private String firstName;
    private String phone;
    private String mdp;

    public User (int id, String email, String name, String firstName, String phone, String mdp){
        this.email = email;
        this.name = name;
        this.firstName = firstName;
        this.phone = phone;
        this.mdp = mdp;
    }

    public String getEmail() {
        return email;
    }

    public String getMdp() {
        return mdp;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPhone() {
        return phone;
    }
}
