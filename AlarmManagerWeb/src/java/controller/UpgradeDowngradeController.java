/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import domain.AlarmOrganizer;
import entities.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import transferobjects.UserTO;

/**
 *
 * @author ivarv
 */
@Controller
public class UpgradeDowngradeController {
    
    @Autowired
    AlarmOrganizer organizer;
    
    @RequestMapping("/downgradeUser")
    public String downgradeUser(HttpServletRequest request) throws Exception{
        Integer tId = ServletRequestUtils.getIntParameter(request, "uID");
        User user = organizer.getUser(tId);
        HttpSession session = request.getSession();
        UserTO sourceTO = (UserTO) session.getAttribute("user");
        if(sourceTO == null){
            return "redirect:loginForm.htm?info='You have to be logged in as admin to view this page.'";
        }
        User source = organizer.getUser(sourceTO.getId());
        organizer.downgradeToUser(source, user);
        return "redirect:users.htm";
    }
    
    @RequestMapping("/upgradeUser")
    public String upgradeUser(HttpServletRequest request) throws Exception {
        Integer tId = ServletRequestUtils.getIntParameter(request, "uID");
        User user = organizer.getUser(tId);
        HttpSession session = request.getSession();
        UserTO sourceTO = (UserTO) session.getAttribute("user");
        if(sourceTO == null){
            return "redirect:loginForm.htm?info='You have to be logged in as admin to view this page.'";
        }
        User source = organizer.getUser(sourceTO.getId());
        organizer.upgradeToAdmin(source, user);
        return "redirect:users.htm";
    }
}
