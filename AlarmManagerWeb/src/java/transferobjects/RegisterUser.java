/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package transferobjects;

/**
 *
 * @author ivarv
 */
public class RegisterUser {
    
    private String naam;
    private String achternaam;
    private String email;
    private String paswoord;
    private String paswoordRepeat;

    public String getPaswoordRepeat() {
        return paswoordRepeat;
    }

    public void setPaswoordRepeat(String paswoordRepeat) {
        this.paswoordRepeat = paswoordRepeat;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPaswoord() {
        return paswoord;
    }

    public void setPaswoord(String paswoord) {
        this.paswoord = paswoord;
    }

}
