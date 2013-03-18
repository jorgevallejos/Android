package be.cegeka.model;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author davidm
 */
public class User {
    
    private int id;
    private String naam;
    private String achternaam;
    private String emailadres;

    public User(int id, String naam, String achternaam, String emailadres) {
        this.id = id;
        this.naam = naam;
        this.achternaam = achternaam;
        this.emailadres=emailadres;
    }
    
    public User(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public String getEmailadres() {
        return emailadres;
    }

    public void setEmailadres(String emailadres) {
        this.emailadres = emailadres;
    }

    public String toString(){
        return "User: " + getId() + " " +getNaam() + " " + getAchternaam() + " " +getEmailadres();
    }
    
}
