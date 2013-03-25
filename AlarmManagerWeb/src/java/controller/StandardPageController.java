package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StandardPageController {
    
    @RequestMapping("/home")
    public String goHome(){
        return "Home";
    }
    
    @RequestMapping("/about.htm")
    public String goToAbout(){
        return "About";
    }
    
    @RequestMapping("/contact.htm")
    public String goToContact(){
        return "Contact";
    }
    
}
