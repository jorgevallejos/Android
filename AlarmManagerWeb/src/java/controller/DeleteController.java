package controller;

import domain.AlarmOrganizer;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DeleteController {
    
    @Autowired
    AlarmOrganizer organizer;
    
    @RequestMapping("/deleteAlarm")
    public ModelAndView deleteAlarm(HttpServletRequest request) throws Exception{
        Integer id = ServletRequestUtils.getIntParameter(request, "id");
        organizer.deleteAlarm(id);
        return new ModelAndView("redirect:alarms.htm");
    }
    
}
