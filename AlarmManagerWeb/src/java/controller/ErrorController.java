/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {
    
    @RequestMapping("/404")
    public String goTo404(){
        return "ErrorPage404";
    }
    
    @RequestMapping("/exception")
    public String goToException(){
        return "ErrorPageException";
    }
}
