/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import transferobjects.UserTO;

/**
 *
 * @author ivarv
 */
public class LoginChecker {
    
    public static boolean userLoggedInAndAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            return false;
        }
        UserTO user = (UserTO) session.getAttribute("user");
        if(user.isAdmin()){
            return true;
        }
        return false;
    }
    
    public static boolean userLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if(session.getAttribute("user") == null){
            return false;
        }
        return true;
    }
    
}
