package controller;

import domain.AlarmOrganizer;
import entities.Alarm;
import entities.User;
import exceptions.DatabaseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import transferobjects.AlarmTO;
import transferobjects.UserTO;
import utils.TransferObjectConverter;

@Controller
public class AlarmUserRelationController {
    
    @Autowired
    private AlarmOrganizer organizer;
    
    @RequestMapping("/editAlarmUsers")
    public ModelAndView goToAddUserToAlarm(HttpServletRequest request) throws Exception{
        // Create Model Map
        Map<String, Object> model = new HashMap<String, Object>();
        
        // Get Alarm to add to
        Integer id = ServletRequestUtils.getIntParameter(request, "aID");
        Alarm alarm = organizer.getAlarm(id);
        AlarmTO alarmTO = TransferObjectConverter.getAlarmTO(alarm);
        
        // Create list of users to show and a list of users already linked
        List<UserTO> userTOsLinked = new LinkedList<UserTO>();
        List<UserTO> userTOsAvailable = new LinkedList<UserTO>();
        
        List<User> allUsers = organizer.getAllUsers();
        List<User> usersLinked = organizer.getUsersFromAlarm(alarm);
        for(User u : allUsers){
            System.out.println(u.getUserid());
            if(usersLinked.contains(u)){
                System.out.println("Linked:" + u.getUserid());
                userTOsLinked.add(TransferObjectConverter.getUserTO(u));
            }
            else {
                userTOsAvailable.add(TransferObjectConverter.getUserTO(u));
            }
        }
        
        model.put("usersAvailable", userTOsAvailable);
        model.put("usersLinked", userTOsLinked);
        model.put("alarm", alarmTO);
        
        return new ModelAndView("EditAlarmUsers", model);
    }
    
    @RequestMapping("/addUserToAlarmAction")
    public ModelAndView addUserToAlarm(HttpServletRequest request) throws Exception{
        Integer aID = addAlarmUserRelation(request).get("aID");
        return new ModelAndView("redirect:editAlarmUsers.htm?aID=" + aID);
    }
    
    @RequestMapping("/removeUserFromAlarm")
    public ModelAndView removeUserFromAlarm(HttpServletRequest request) throws Exception{
        Integer aID = removeAlarmUserRelation(request).get("aID");
        return new ModelAndView("redirect:editAlarmUsers.htm?aID=" + aID);
    }
    
    @RequestMapping("/editUserAlarms")
    public ModelAndView goToEditUserAlarms(HttpServletRequest request) throws Exception {
        // Create Model Map
        Map<String, Object> model = new HashMap<String, Object>();
        
        // Get Alarm to add to
        Integer id = ServletRequestUtils.getIntParameter(request, "uID");
        User user = organizer.getUser(id);
        UserTO userTO = TransferObjectConverter.getUserTO(user);
        
        // Create list of users to show and a list of users already linked
        List<AlarmTO> alarmTOsLinked = new LinkedList<AlarmTO>();
        List<AlarmTO> alarmTOsAvailable = new LinkedList<AlarmTO>();
        
        List<Alarm> allAlarms = organizer.getAllAlarms();
        List<Alarm> alarmsLinked = organizer.getAlarmsFromUser(user);
        for(Alarm a : allAlarms){
            if(alarmsLinked.contains(a)){
                alarmTOsLinked.add(TransferObjectConverter.getAlarmTO(a));
            }
            else {
                alarmTOsAvailable.add(TransferObjectConverter.getAlarmTO(a));
            }
        }
        
        model.put("alarmsAvailable", alarmTOsAvailable);
        model.put("alarmsLinked", alarmTOsLinked);
        model.put("user", userTO);
        
        return new ModelAndView("EditUserAlarms", model);
    }
    
    @RequestMapping("/addAlarmToUserAction")
    public ModelAndView addAlarmToUserAction(HttpServletRequest request) throws Exception{
        Integer uID = addAlarmUserRelation(request).get("uID");
        return new ModelAndView("redirect:editUserAlarms.htm?uID=" + uID);
    }
    
    @RequestMapping("/removeAlarmFromUser")
    public ModelAndView removeAlarmFromUser(HttpServletRequest request) throws Exception {
        Integer uID = removeAlarmUserRelation(request).get("uID");
        return new ModelAndView("redirect:editUserAlarms.htm?uID=" + uID);
    }

    private Map<String, Integer> addAlarmUserRelation(HttpServletRequest request) throws DatabaseException, ServletRequestBindingException {
        Integer uID = ServletRequestUtils.getIntParameter(request, "uID");
        Integer aID = ServletRequestUtils.getIntParameter(request, "aID");
        organizer.addAlarmToUser(uID, aID);
        Map<String, Integer> ids = new HashMap<String, Integer>();
        ids.put("uID", uID);
        ids.put("aID", aID);
        return ids;
    }

    private Map<String, Integer> removeAlarmUserRelation(HttpServletRequest request) throws ServletRequestBindingException, DatabaseException {
        Integer uID = ServletRequestUtils.getIntParameter(request, "uID");
        Integer aID = ServletRequestUtils.getIntParameter(request, "aID");
        organizer.removeAlarmFromUser(uID, aID);
        Map<String, Integer> ids = new HashMap<String, Integer>();
        ids.put("uID", uID);
        ids.put("aID", aID);
        return ids;
    }
}
