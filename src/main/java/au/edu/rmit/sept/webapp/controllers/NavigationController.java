package au.edu.rmit.sept.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class NavigationController {

    @GetMapping("/appointments")
    public String appointmentsPage(Model model) {
        model.addAttribute("message", "Appointments Page");
        return "appointments";  // Thymeleaf template name
    }
}