package au.edu.rmit.sept.webapp.controllers;

import au.edu.rmit.sept.webapp.repositories.SavedResourcesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/savedresources")
public class SavedResourcesController {

    @Autowired
    private SavedResourcesRepository savedResourcesRepository;

    @PostMapping("/delete/{id}")
    public String deleteSavedResource(@PathVariable("id") int id) {
        savedResourcesRepository.deleteById(id);    
        return "redirect:/profile#pills-resources-tab";
    }
}