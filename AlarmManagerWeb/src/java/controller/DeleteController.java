package controller;

import domain.AlarmOrganizer;
import exceptions.DatabaseException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import utils.LoginChecker;

@Controller
public class DeleteController {

    @Autowired
    AlarmOrganizer organizer;

    @RequestMapping("/deleteAlarm")
    public ModelAndView deleteAlarm(HttpServletRequest request) throws Exception {
        if (LoginChecker.userLoggedInAndAdmin(request)) {
            Integer id = ServletRequestUtils.getIntParameter(request, "id");
            organizer.deleteAlarm(id);
            return new ModelAndView("redirect:alarms.htm");
        }
        return new ModelAndView("redirect:loginForm.htm?info='You have to be logged in as admin to view this page.'");
    }
    
    @RequestMapping("/deleteUser")
    public ModelAndView deleteUser(HttpServletRequest request) throws Exception {
        if (LoginChecker.userLoggedInAndAdmin(request)) {
            Integer id = ServletRequestUtils.getIntParameter(request, "uID");
            organizer.deleteUser(id);
            return new ModelAndView("redirect:users.htm");
        }
        return new ModelAndView("redirect:loginForm.htm?info='You have to be logged in as admin to view this page.'");
    }
    
    @RequestMapping("/cleanupAlarms")
    public String cleanupAlarms() throws DatabaseException{
        organizer.cleanUpAlarms();
        return "redirect:alarms.htm";
    }
}
