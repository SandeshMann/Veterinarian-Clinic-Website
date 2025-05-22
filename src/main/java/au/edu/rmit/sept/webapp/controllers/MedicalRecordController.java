package au.edu.rmit.sept.webapp.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import au.edu.rmit.sept.webapp.services.MedicalRecordService;
import au.edu.rmit.sept.webapp.services.PetService;
import au.edu.rmit.sept.webapp.services.UserService;
import au.edu.rmit.sept.webapp.models.MedicalRecords;
import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.repositories.MedicalRepository;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/medicalrecords")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    @Autowired
    private MedicalRepository medicalRepository;

    @GetMapping("/{recordId}")
    public ResponseEntity<MedicalRecords> getMedRecById(@PathVariable int recordId) {
        try {

            return ResponseEntity.ok(medicalRecordService.findMedRecordById(recordId));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/create")
    public String createMedicalRecord(
            @ModelAttribute("medicalRecord") MedicalRecords medicalRecord,
            @RequestParam("petId") int petId,
            @RequestParam("clinicName") String clinicName,
            @RequestParam("description") String description,
            @RequestParam("recordType") String recordType,
            @RequestParam("dateIssued") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateIssued,
            Model model) {

        // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String signedInUserName = authentication.getName();

        // Find the currently authenticated user (both vet and patient)
        User foundUser = userService.findUserByEmail(signedInUserName);

        if (foundUser != null) {
            // Fetch the Pet and Clinic entities by their IDs
            Pet selectedPet = petService.findById(petId); // Assuming petService exists

            // Set the relationships in the medical record entity
            medicalRecord.setPet(selectedPet);
            medicalRecord.setClinicName(clinicName);
            medicalRecord.setPatient(foundUser);
            medicalRecord.setDescription(description);
            medicalRecord.setRecordType(recordType);
            medicalRecord.setDateIssued(dateIssued);
            medicalRecord.setFollowUpDate(LocalDateTime.now());
            medicalRecord.setUpdatedAt(LocalDateTime.now());
            medicalRecord.setCreatedAt(LocalDateTime.now());

            // Save the medical record to the database
            medicalRepository.save(medicalRecord);

            // Add success message
            model.addAttribute("successMsg", "Medical record created successfully.");
            return "redirect:/profile"; // Redirect to profile page after success
        }

        model.addAttribute("errorMsg", "Error occurred while adding prescription.");
        return "welcome/index.html"; // Redirect back to the form on failure
    }

    // New DELETE endpoint to handle medical record deletion
    @DeleteMapping("/delete/{recordId}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable int recordId) {
        try {
            medicalRecordService.deleteMedRecordById(recordId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
