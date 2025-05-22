package au.edu.rmit.sept.webapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.services.UserService;
import org.springframework.ui.Model;


@Controller
public class SignUpController {

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "welcome/signup.html";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        // Check if the email is already in use
        User foundUser = userService.findUserByEmail(user.getEmail());
        if (foundUser != null) {
            model.addAttribute("userExistError", "Email is already in use. Please try to login.");
            return "welcome/signup.html";  // Return to signup page with error message
        }


        if(user.getUserRole() == null) {
            user.setUserRole("Patient");
        }

        System.out.println("User Data: " + user);

        // Call the saveUser function to save the new user into database.
        userService.saveUser(user);
        model.addAttribute("user", new User()); // Clear the form fields
        model.addAttribute("successMsg", "Account has been created successfully!");
        return "welcome/login.html";
        
    }
}
