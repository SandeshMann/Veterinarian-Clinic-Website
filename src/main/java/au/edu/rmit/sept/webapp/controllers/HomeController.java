package au.edu.rmit.sept.webapp.controllers;

import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.services.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import au.edu.rmit.sept.webapp.models.Clinic;
import au.edu.rmit.sept.webapp.services.ClinicService;

import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.repositories.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class HomeController {

    @Autowired
    private ClinicService clinicsService;

    @Autowired
    private PetService petService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String index(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authenticaiton: " + authentication);
        String signedInUserName = authentication.getName();
        // Find the user by user's email address
        User user = userRepository.findByEmail(signedInUserName);
        model.addAttribute("user", user);

        List<Clinic> clinics = clinicsService.getAllClinics();
        model.addAttribute("clinics", clinics);

        if (user != null) {
            List<Pet> pets = petService.findPetsByOwnerId(user.getUserId());
            model.addAttribute("pets", pets);
        }
        model.addAttribute("message", "This is the home page");
        return "welcome/index.html";

    }

}
