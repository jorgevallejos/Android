package validators;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import transferobjects.AlarmTO;

public class AlarmValidator implements Validator {

    public boolean supports(Class<?> type) {
        return AlarmTO.class.equals(type);
    }

    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", null, "Title can't be empty.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "info", null, "Description can't be empty.");
        AlarmTO alarm = (AlarmTO) o;
        if(alarm.isRepeated() && alarm.getRepeatQuantity() <= 0){
            errors.rejectValue("repeatQuantity", null, null, "The repeat unit quantity must be higher than 0.");
        }
    }
}
