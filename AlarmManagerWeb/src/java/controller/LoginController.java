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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import transferobjects.LoginUser;
import transferobjects.UserTO;

@Controller
@RequestMapping("/loginForm")
public class LoginController {

    @Autowired
    AlarmOrganizer organizer;
    
    @RequestMapping(method = RequestMethod.GET)
    public String initForm(ModelMap model) throws Exception {
        return "Login";
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView login(@ModelAttribute("login") LoginUser lUser, HttpServletRequest request) {
        return loginAction(lUser, request);
    }

    private ModelAndView loginAction(LoginUser lUser, HttpServletRequest request) {
        try {
            String username = lUser.getName();
            String password = lUser.getPass();
            User user = organizer.getUser(username);
            if (user.authenticate(password)) {
                HttpSession session = request.getSession();
                UserTO sUser = new UserTO(user);
                session.setAttribute("user", sUser);
                return new ModelAndView("Home");
            }
        } catch (DatabaseException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ModelAndView("index", "message", "Somethin's wrong nigga");
    }
    
    @ModelAttribute("login")
    private LoginUser formBackingObject(HttpServletRequest request) {
        return new LoginUser();
    }
}
