package au.edu.rmit.sept.webapp.controllers;

import au.edu.rmit.sept.webapp.models.Clinic;
import au.edu.rmit.sept.webapp.models.Resources;
import au.edu.rmit.sept.webapp.models.SavedResources;
import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.repositories.SavedResourcesRepository;
import au.edu.rmit.sept.webapp.repositories.UserRepository;
import au.edu.rmit.sept.webapp.services.ClinicService;
import au.edu.rmit.sept.webapp.services.ResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/resources")
public class ResourcesController {

    @Autowired
    private UserRepository userRepository;

    // Injecting the ResourcesService dependency
    @Autowired
    private ResourcesService resourcesService;

    @Autowired
    private SavedResourcesRepository savedResourcesRepository;

    @Autowired
    private ClinicService clinicService;

    // Endpoint to filter resources by type and/or tag and return an HTML view
    @GetMapping
    public String filterResources(@RequestParam(required = false) String resourceType,
            @RequestParam(required = false) String tag,
            Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String signedInUserName = authentication.getName();
        // Find the user by user's email address
        User user = userRepository.findByEmail(signedInUserName);

        List<Resources> resourcesList;

        // If both resourceType and tag are provided, filter by both
        if (resourceType != null && !resourceType.isEmpty() && tag != null && !tag.isEmpty()) {
            resourcesList = resourcesService.getResourcesByTypeAndTag(resourceType, tag);
        }
        // If only resourceType is provided, filter by resourceType
        else if (resourceType != null && !resourceType.isEmpty()) {
            resourcesList = resourcesService.getResourcesByType(resourceType);
        }
        // If only tag is provided, filter by tag
        else if (tag != null && !tag.isEmpty()) {
            resourcesList = resourcesService.getResourcesByTag(tag);
        }
        // If no filters are provided, return all resources
        else {
            resourcesList = resourcesService.getAllResources();
        }

        // Add the list of resources to the model
        model.addAttribute("resources", resourcesList);

        // Add the list of clinics to the model
        List<Clinic> clinics = clinicService.getAllClinics();
        model.addAttribute("clinics", clinics);

        model.addAttribute("user", user);

        // Return the resources.html file
        return "welcome/resources.html";
    }

    @PostMapping("/save")
    public String saveResource(@RequestParam("resourceId") int resourceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String signedInUserName = authentication.getName();
        // Find the user by user's email address
        User user = userRepository.findByEmail(signedInUserName);

        // Get the resource by its ID
        Resources resource = resourcesService.getAllResources().stream()
                .filter(r -> r.getResourceId() == resourceId)
                .findFirst()
                .orElse(null);

        // Save the resource to the user's saved resources
        SavedResources savedResource = new SavedResources();
        savedResource.setUser(user);
        savedResource.setResource(resource);
        savedResource.setSavedAt(new Timestamp(System.currentTimeMillis()));

        savedResourcesRepository.save(savedResource);

        return "redirect:/resources";
    }

    @GetMapping("/resource")
    public String index(Model model, @RequestParam("resourceId") int resourceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String signedInUserName = authentication.getName();
        // Find the user by user's email address
        User user = userRepository.findByEmail(signedInUserName);

        // Get the resource by its ID
        Resources resource = resourcesService.getAllResources().stream()
                .filter(r -> r.getResourceId() == resourceId)
                .findFirst()
                .orElse(null);

        // Add resource and user to the article page
        model.addAttribute("resource", resource);
        model.addAttribute("user", user);

        // Add the list of clinics to the model
        List<Clinic> clinics = clinicService.getAllClinics();
        model.addAttribute("clinics", clinics);

        // Return resource.html
        return "welcome/resource.html";
    }

}
