package au.edu.rmit.sept.webapp.controllers;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import au.edu.rmit.sept.webapp.models.Clinic;
import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.Prescriptions;
import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.repositories.ClinicRepository;
import au.edu.rmit.sept.webapp.repositories.PetRepository;
import au.edu.rmit.sept.webapp.repositories.PrescriptionRepository;
import au.edu.rmit.sept.webapp.repositories.UserRepository;
import au.edu.rmit.sept.webapp.services.ClinicService;
import au.edu.rmit.sept.webapp.services.PetService;
import au.edu.rmit.sept.webapp.services.PrescriptionService;
import au.edu.rmit.sept.webapp.services.UserService;

@Controller
public class PrescriptionController {
   @Autowired
   private UserRepository userRepository;

   @Autowired
   private PrescriptionRepository prescriptionRepository;

   @Autowired
   private PetRepository petRepository;

   @Autowired
   private ClinicRepository clinicRepository;

   @Autowired
   private PetService petService;

   @Autowired
   private UserService userService;

   @Autowired
   private ClinicService clinicService;

   @Autowired
   private PrescriptionService prescriptionService;

   @GetMapping("/add-prescription")
   public String addPrescription(Model model) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String signedInUserName = authentication.getName();
      // Find the user by user's email address
      User user = userRepository.findByEmail(signedInUserName);

      if (user == null) {
         System.out.println("============");
         System.out.println("No user found.");
      }

      // Fetch the list of pets and clinics
      List<Clinic> clinics = clinicRepository.findAll();
      List<Pet> pets = petRepository.findAll();

      // Add them to the model
      // Add them to the model
      model.addAttribute("clinics", clinics);
      model.addAttribute("pets", pets);
      return "welcome/prfoile.html";
   }

   @PostMapping("/add")
   public String addPrescription(
         @ModelAttribute("prescription") Prescriptions prescription,
         @RequestParam("petId") int petId,
         @RequestParam("clinicId") int clinicId,
         Model model) {

      // Get the authenticated user
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String signedInUserName = authentication.getName();

      // Get the currently authenticated user (both vet and patient)
      User foundUser = userService.findUserByEmail(signedInUserName);

      if (foundUser != null) {
         // Fetch Pet and Clinic by their IDs
         Pet selectedPet = petService.findById(petId); // Assuming petService exists
         Clinic selectedClinic = clinicService.getClinicById(clinicId); // Assuming clinicService exists

         // Set the relationships in the prescription entity
         prescription.setPet(selectedPet);
         prescription.setClinic(selectedClinic);
         prescription.setVet(foundUser); // Vet is the authenticated user
         prescription.setPatient(foundUser); // Patient is also the authenticated user

         // The remaining fields such as dosage, instructions, purpose, etc. are set
         // automatically from the form input
         // The remaining fields such as dosage, instructions, purpose, etc. are set
         // automatically from the form input

         // Set timestamps
         prescription.setDateIssued(LocalDateTime.now());
         prescription.setExpiryDate(prescription.getDateIssued().plusDays(25));
         prescription.setUpdatedAt(LocalDateTime.now());
         prescription.setArrivalCountDown(7);
         prescription.setPaid(0);

         // Save the prescription to the database
         prescriptionRepository.save(prescription);

         model.addAttribute("successMsg", "Prescription added successfully.");
         return "redirect:/profile"; // Redirect to profile page after success
      }

      model.addAttribute("errorMsg", "Error occurred while adding prescription.");
      return "welcome/index.html";
   }

   // This method handles the deletion of a prescription by its ID.
   @PostMapping("/prescriptions/delete/{id}")
   public String deletePrescription(@PathVariable("id") int id, Model model) {
      try {
         // Attempt to delete the prescription by calling the service layer.
         prescriptionService.deletePrescriptionById(id);

         // If successful, set a success message in the redirect attributes to be shown
         // on the profile page.
         model.addAttribute("successMsg", "Prescription deleted successfully.");
      } catch (Exception e) {
         // If an error occurs, set an error message in the redirect attributes to be
         // shown on the profile page.
         model.addAttribute("errorMsg", "Failed to delete prescription.");
      }
      return "redirect:/profile";
   }

   @PutMapping("/prescription/paid/{prescriptionId}")
   public ResponseEntity<String> payPrescription(@PathVariable("prescriptionId") int prescriptionId) {
      try {
         // Call the service to handle the cancellation logic
         boolean isPaid = prescriptionService.payPrescription(prescriptionId);

         if (isPaid) {
            return ResponseEntity.ok("Prescription paid successfully.");
         } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to pay for prescription.");
         }
      } catch (Exception e) {
         // Handle the exception
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
               .body("An error occurred while paying for prescriptions.");
      }
   }

}
