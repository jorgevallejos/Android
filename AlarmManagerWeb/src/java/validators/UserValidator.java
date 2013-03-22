/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package validators;

import domain.AlarmOrganizer;
import exceptions.DatabaseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import transferobjects.RegisterUser;

/**
 *
 * @author ivarv
 */
public class UserValidator implements Validator{

    public boolean supports(Class<?> type) {
        return RegisterUser.class.equals(type);
    }

    public void validate(Object o, Errors errors) {
        try {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "naam", null, "Name can't be empty.");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "achternaam", null, "Last name can't be empty.");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", null, "E-mail can't be empty.");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "paswoord", null, "Password can't be empty.");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "paswoordRepeat", null, "Name can't be empty.");
            RegisterUser user = (RegisterUser) o;
            if(!user.getPaswoord().equals(user.getPaswoordRepeat())){
                errors.rejectValue("paswoordRepeat", null, null, "The repeated password doesn't equal the password.");
            }
            if(!user.getEmail().matches("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b")){
                errors.rejectValue("email", null, null, "The e-mail adress is invalid");
            }
            AlarmOrganizer organizer = new AlarmOrganizer();
            if(organizer.checkIfEmailExists(user.getEmail())){
                errors.rejectValue("email", null, null, "There is already an account with this e-mail address.");
            }
        } catch (DatabaseException ex) {
            errors.reject(null, "Something went wrong during validation.");
        }
    }
    
}
