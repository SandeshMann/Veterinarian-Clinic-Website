package au.edu.rmit.sept.webapp.controllers;
import org.springframework.web.bind.annotation.GetMapping;

public class LogoutController {

    @GetMapping("/logout")
    public String logout() {
        return "welcome/index.html";
    }
}
