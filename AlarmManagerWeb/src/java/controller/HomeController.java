package controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;

@Controller
public class HomeController {
    
    public HomeController(){
        
    }
    
    @RequestMapping("/home")
    public String goHome(){
        return "Home";
    }
    
}
