package au.edu.rmit.sept.webapp.controllers;

import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.User;

import au.edu.rmit.sept.webapp.services.UserService;

import au.edu.rmit.sept.webapp.services.PetService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
@Controller
public class PetController {
    // Controller class for Pet entity
    // This class is responsible for handling HTTP requests related to pets
    // It interacts with the PetService class to perform CRUD operations on the Pet
    // entity

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    // Constructor
    public PetController(PetService petService) {
        this.petService = petService;
    }

    // Endpoint to create a new pet
    @PostMapping("/pet/register")
    public ResponseEntity<String> createPet(
            @RequestParam("ownerId") int ownerId,
            @RequestParam(value = "petName", required = true) String petName,
            @RequestParam(value = "petType", required = true) String petType,
            @RequestParam(value = "petBreed", required = false) String petBreed) {

        // Retrieve the owner (patient) by ID
        User owner = userService.findUserById(ownerId);

        if (owner == null) {
            return ResponseEntity.badRequest().body(null); // Handle invalid owner
            // return "welcome/login";
        }

        // Create a new pet with the provided details
        Pet pet = petService.createPet(owner, petName, petType, petBreed);

        // System.out.println("Added successfully");
        return ResponseEntity.ok("Added successfully");

        // return "welcome/profile";
    }

    // Endpoint to get pets by owner ID
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Pet>> getPetsByOwnerId(@PathVariable int ownerId) {
        // Retrieve pets by owner ID
        List<Pet> pets = petService.findPetsByOwnerId(ownerId);

        return ResponseEntity.ok(pets);
    }

    // Endpoint to get pet by ID
    @GetMapping("/pet/{petId}")
    public ResponseEntity<Pet> getPetById(@PathVariable int petId) {
        // Retrieve pet by ID
        Pet pet = petService.findById(petId);

        return ResponseEntity.ok(pet);
    }

    @PostMapping("/pet/delete")
    public String deletePet(@RequestParam("petId") int petId) {
        // Call your service to delete the pet by ID
        petService.deletePetById(petId);
        // Redirect back to the profile page
        return "redirect:/profile";
    }
}
