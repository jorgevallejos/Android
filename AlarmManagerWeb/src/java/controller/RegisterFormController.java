/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import domain.AlarmOrganizer;
import entities.User;
import exceptions.DatabaseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import transferobjects.LoginUser;
import transferobjects.RegisterUser;
import transferobjects.UserTO;
import validators.UserValidator;

@Controller
@RequestMapping("/registerForm")
public class RegisterFormController {

    @Autowired
    AlarmOrganizer organizer;

    @RequestMapping(method = RequestMethod.GET)
    public String initForm(ModelMap model) throws Exception {
        return "Register";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String register(@ModelAttribute("registerUser") RegisterUser rUser, HttpServletRequest request, BindingResult result) {
        UserValidator validator = new UserValidator();
        validator.validate(rUser, result);
        if (result.hasErrors()) {
            return "Register";
        } else {
            try {
                User user = new User(rUser.getNaam(), rUser.getAchternaam(), rUser.getPaswoord(), rUser.getEmail());
                // TODO: Setuserid moet weg.
                user.setUserid(1000);
                organizer.createUser(user);
            } catch (DatabaseException ex) {
                Logger.getLogger(RegisterFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "Home";
        }
    }

    @ModelAttribute("registerUser")
    private RegisterUser formBackingObject(HttpServletRequest request) {
        return new RegisterUser();
    }
}
