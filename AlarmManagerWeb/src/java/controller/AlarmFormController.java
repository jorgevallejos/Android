package controller;

import domain.AlarmOrganizer;
import entities.Alarm;
import exceptions.DatabaseException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import transferobjects.AlarmTO;
import utils.TransferObjectConverter;
import validators.AlarmValidator;

@Controller
@RequestMapping("/alarmForm")
public class AlarmFormController {

    @Autowired
    AlarmOrganizer organizer;

    //Show Form
    @RequestMapping(method = RequestMethod.GET)
    public String initForm(ModelMap model) throws Exception {
        return "AlarmForm";
    }

    //Set Command Object
    @ModelAttribute("editAlarm")
    private AlarmTO formBackingObject(HttpServletRequest request) throws ServletRequestBindingException, DatabaseException {
        Integer id = ServletRequestUtils.getIntParameter(request, "id");
        AlarmTO alarmTO = null;
        if (id != null && id != -1) {
            Alarm alarm = organizer.getAlarm(id);
            alarmTO = new AlarmTO(alarm.getAlarmid(), alarm.getTitle(), alarm.getInfo(),
                    alarm.getRepeated(), alarm.getRepeatUnit(), alarm.getRepeatquantity(), alarm.getRepeatEnddate(), alarm.getDateInMillis());
        } else {
            alarmTO = new AlarmTO();
        }
        return alarmTO;
    }

    // Set submit processing
    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(@ModelAttribute("editAlarm") AlarmTO alarmTO, BindingResult result) throws Exception {
        AlarmValidator validator = new AlarmValidator();
        validator.validate(alarmTO, result);
        if (result.hasErrors()) {
            return "AlarmForm";
        } else {
            Alarm alarm = TransferObjectConverter.getAlarm(alarmTO);
            if (alarmTO.getId() == null || alarmTO.getId() == -1) {
                organizer.createAlarm(alarm);
            } else {
                organizer.updateAlarm(alarm);
            }
            return "forward:/alarms.htm";
        }
    }
}
