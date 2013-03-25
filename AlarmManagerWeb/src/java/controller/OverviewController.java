package controller;

import domain.AlarmOrganizer;
import entities.Alarm;
import entities.User;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import transferobjects.AlarmTO;
import transferobjects.UserTO;
import utils.LoginChecker;
import utils.TransferObjectConverter;

@Controller
public class OverviewController {

    @Autowired
    AlarmOrganizer organizer;

    @RequestMapping("/alarms")
    public ModelAndView showAlarms(HttpServletRequest request) {
        if (LoginChecker.userLoggedInAndAdmin(request)) {
            List<Alarm> alarms = organizer.getAllAlarms();
            List<AlarmTO> alarmTOs = new LinkedList<AlarmTO>();
            for (Alarm a : alarms) {
                alarmTOs.add(TransferObjectConverter.getAlarmTO(a));
            }
            return new ModelAndView("Alarms", "alarms", alarmTOs);
        }
        return new ModelAndView("redirect:loginForm.htm?info='You have to be logged in as admin to view this page.'");
    }

    @RequestMapping("/users")
    public ModelAndView showUsers(HttpServletRequest request) {
        if (LoginChecker.userLoggedInAndAdmin(request)) {
            List<User> users = organizer.getAllUsers();
            List<UserTO> userTOs = new LinkedList<UserTO>();
            for (User u : users) {
                userTOs.add(TransferObjectConverter.getUserTO(u));
            }
            return new ModelAndView("Users", "users", userTOs);
        }
        return new ModelAndView("redirect:loginForm.htm?info='You have to be logged in as admin to view this page.'");
    }
}
