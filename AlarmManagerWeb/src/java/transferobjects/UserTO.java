package transferobjects;

import entities.User;

public class UserTO {
    
    public Integer id;
    public String naam;
    public String achternaam;
    public String email;
    
    public UserTO(User user){
        id = user.getUserid();
        naam = user.getNaam();
        achternaam = user.getAchternaam();
        email = user.getEmail();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
}
