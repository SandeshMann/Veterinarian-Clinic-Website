package au.edu.rmit.sept.webapp.controllers;

import java.util.List;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import au.edu.rmit.sept.webapp.models.Appointment;
import au.edu.rmit.sept.webapp.models.Clinic;
import au.edu.rmit.sept.webapp.models.MedicalRecords;
import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.SavedResources;
import au.edu.rmit.sept.webapp.models.Prescriptions;
import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.repositories.AppointmentRepository;
import au.edu.rmit.sept.webapp.repositories.MedicalRepository;
import au.edu.rmit.sept.webapp.repositories.PrescriptionRepository;
import au.edu.rmit.sept.webapp.repositories.SavedResourcesRepository;
import au.edu.rmit.sept.webapp.repositories.UserRepository;
import au.edu.rmit.sept.webapp.services.AppointmentService;
import au.edu.rmit.sept.webapp.services.ClinicService;
import au.edu.rmit.sept.webapp.services.PetService;
import au.edu.rmit.sept.webapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.ui.Model;

@Controller
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private MedicalRepository medicalRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private ClinicService clinicsService;

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentService apptService;

    @Autowired
    private SavedResourcesRepository savedResourcesRepository;

    @GetMapping("/profile")
    public String getProfile(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authenticaiton: " + authentication);
        String signedInUserName = authentication.getName();
        // Find the user by user's email address
        User user = userRepository.findByEmail(signedInUserName);
        // if user is not signed in, pass an error message that user is not logged in.
        if (user == null) {
            // Handle anonymous users or unauthenticated access
            model.addAttribute("errorMsg", "You need to be logged in to access your profile.");
            return "welcome/login.html";
        }

        // Get the list of all the appointment/clinics for the signed in user.
        List<Appointment> appointments = appointmentRepository.findByPatient_UserId(user.getUserId());

        // Filter all the appointments by the upcoming appointment with booked status.
        List<Appointment> upcomingAppointments = appointments.stream()
                .filter(appointment -> appointment.getAppointmentLocalDate()
                        .isAfter(LocalDateTime.now()) && appointment.getStatus().equals("Booked"))
                .collect(Collectors.toList());

        // Get the list of all the saved resources for the signed in user.
        List<SavedResources> savedResources = savedResourcesRepository.findByUser_UserId(user.getUserId());

        // // Add the list of saved resources to the model
        model.addAttribute("savedResources", savedResources);

        List<MedicalRecords> medRecords = medicalRepository.findByPatient_UserId(user.getUserId());
        // List<Appointment> appointments =
        // appointmentRepository.findByPatient_UserId(user.getUserId());

        model.addAttribute("user", user);
        model.addAttribute("upcomingAppointments", upcomingAppointments);

        List<Clinic> clinics = clinicsService.getAllClinics();
        model.addAttribute("clinics", clinics);
        model.addAttribute("medRecords", medRecords);

        // Get the list of all the prescription for a the signed in user.
        List<Prescriptions> prescriptions = prescriptionRepository.findByPatient_UserId(user.getUserId());

        // If no prescriptions are found, add a message to be displayed
        if (prescriptions.isEmpty()) {
            model.addAttribute("infoMsg", "No prescriptions available.");
        }

        System.out.println("Prescription: " + prescriptions);
        model.addAttribute("prescriptions", prescriptions);

        // int ownerId = 12; // Replace with the actual owner ID for testing
        List<Pet> pets = petService.findPetsByOwnerId(user.getUserId());

        List<Appointment> allUserAppointments = apptService.findApptsByUserId(user.getUserId());

        model.addAttribute("pets", pets);

        model.addAttribute("appointments", allUserAppointments);

        return "welcome/profile.html";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String signedInUserName = authentication.getName();

        // Find the user by user's email address
        User foundUser = userService.findUserByEmail(signedInUserName);

        if (foundUser != null) {
            // Update user details
            foundUser.setFirstName(user.getFirstName());
            foundUser.setPassword(user.getPassword());

            // Save the updated user
            userRepository.save(foundUser);
            // Get the list of all the appointment for the signed in user.
            List<Appointment> appointments = appointmentRepository.findByPatient_UserId(foundUser.getUserId());

            // Filter all the appointments by the upcoming appointment with booked status.
            List<Appointment> upcomingAppointments = appointments.stream()
                    .filter(appointment -> appointment.getAppointmentLocalDate()
                            .isAfter(LocalDateTime.now()) && appointment.getStatus().equals("Booked"))
                    .collect(Collectors.toList());

            model.addAttribute("user", foundUser);
            model.addAttribute("upcomingAppointments", upcomingAppointments);

            List<Clinic> clinics = clinicsService.getAllClinics();
            model.addAttribute("clinics", clinics);

            List<Appointment> allUserAppointments = apptService.findApptsByUserId(foundUser.getUserId());

            // model.addAttribute("pets", pets);

            model.addAttribute("appointments", allUserAppointments);
            return "welcome/profile.html";

        }
        model.addAttribute("user", user);
        model.addAttribute("notUpdate", "An error has occured while updating your profile.");
        return "welcome/index.html";
    }

}
