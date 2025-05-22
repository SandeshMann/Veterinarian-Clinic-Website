package au.edu.rmit.sept.webapp.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import au.edu.rmit.sept.webapp.models.Appointment;
import au.edu.rmit.sept.webapp.models.Clinic;
import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.services.AppointmentService;
import au.edu.rmit.sept.webapp.services.ClinicService;
import au.edu.rmit.sept.webapp.services.PetService;
import au.edu.rmit.sept.webapp.services.UserService;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    @Autowired
    private ClinicService clinicService;

    // Endpoint to return list of clinics
    @GetMapping("/clinics")
    public ResponseEntity<List<Clinic>> getAllClinics() {
        List<Clinic> clinics = clinicService.getAllClinics();
        return ResponseEntity.ok(clinics);
    }

    @PostMapping("/create")
    public ResponseEntity<Appointment> createAppointment(
            @RequestParam("userId") int userId,
            @RequestParam("clinicId") int clinicId,
            @RequestParam("appointmentDateTime") String appointmentDateTime,
            @RequestParam("petId") int petId,
            @RequestParam(value = "petName", required = false) String petName,
            @RequestParam(value = "petType", required = false) String petType,
            @RequestParam(value = "petBreed", required = false) String petBreed,
            @RequestParam("consultationType") String consultationType) {
        // Get user by email
        User patient = userService.findUserById(userId);

        // Get clinic by clinicId
        Clinic clinic = clinicService.getClinicById(clinicId);

        // Create a new pet if the petId is not found
        Pet retrievedPet;
        try {
            retrievedPet = petService.findById(petId);
        } catch (Exception e) {
            retrievedPet = petService.createPet(patient, petName, petType, petBreed);

        }

        // Create appointment using the service
        Appointment newAppointment = appointmentService.createAppointment(
                patient, clinic, appointmentDateTime, retrievedPet, consultationType);

        return new ResponseEntity<>(newAppointment, HttpStatus.CREATED);
    }

    // Confirm appointment using form data (set patient, pet, consultation type, and
    // mark as unavailable)
    @PostMapping("/confirm")
    public ResponseEntity<Appointment> confirmAppointment(
            @RequestParam("email") String email,
            @RequestParam("appointmentId") int appointmentId,
            @RequestParam(value = "petName", required = false) String petName,
            @RequestParam(value = "petType", required = false) String petType,
            @RequestParam(value = "petBreed", required = false) String petBreed,
            @RequestParam("consultationType") String consultationType) {
        // Retrieve the user by email
        User patient = userService.findUserByEmail(email);

        // Retrieve pets by owner ID (patient ID)
        List<Pet> pets = petService.findPetsByOwnerId(patient.getUserId());

        // Create and save a pet for the patient if it doesn't exist
        Pet pet = pets.isEmpty() ? null : pets.get(0);

        if (pet == null) {
            // Create a new pet if none exists for the owner
            pet = petService.createPet(patient, petName, petType, petBreed);
        }

        // Confirm the appointment
        Appointment appointment = appointmentService.confirmAppointment(
                appointmentId, patient, pet, consultationType);

        return ResponseEntity.ok(appointment);
    }

    // Revert appointment to available state
    @PostMapping("/make-available")
    public ResponseEntity<Appointment> makeAppointmentAvailable(@RequestParam int appointmentId) {
        Appointment appointment = appointmentService.makeAppointmentAvailable(appointmentId);
        return ResponseEntity.ok(appointment);
    }

    // Get available appointments by clinic
    @PostMapping("/available")
    public ResponseEntity<List<Appointment>> getAvailableAppointments(@RequestParam int clinicId) {
        List<Appointment> appointments = appointmentService.getAvailableAppointments(clinicId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Appointment>> getApptsByUserId(@PathVariable int patientId) {
        // Retrieve pets by owner ID
        List<Appointment> appt = appointmentService.findApptsByUserId(patientId);

        return ResponseEntity.ok(appt);
    }

    @PutMapping("/cancel/{apptId}")
    public ResponseEntity<String> cancelAppointment(@PathVariable("apptId") int apptId) {
        try {
            // Call the service to handle the cancellation logic
            boolean isCancelled = appointmentService.cancelAppointment(apptId);

            if (isCancelled) {
                return ResponseEntity.ok("Appointment cancelled successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to cancel the appointment.");
            }
        } catch (Exception e) {
            // Handle the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while cancelling the appointment.");
        }
    }

    @PutMapping("/reschedule/{apptId}")
    public ResponseEntity<String> reschedAppointment(@PathVariable("apptId") int apptId,
                                                 @RequestBody Map<String, String> requestBody) {
        try {
            // Parse the appointment_date from the request body
            String newApptDateTimeString = requestBody.get("appointment_date");
            LocalDateTime newApptDateTime = LocalDateTime.parse(newApptDateTimeString); // Parsing the date string
            boolean isRescheduled = appointmentService.reschedAppointment(apptId, newApptDateTime);
            if (isRescheduled) {
                return ResponseEntity.ok("Appointment rescheduled successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to reschedule the appointment.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while rescheduling the appointment.");
        }
    }
}