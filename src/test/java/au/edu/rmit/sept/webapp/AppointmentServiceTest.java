package au.edu.rmit.sept.webapp;

import au.edu.rmit.sept.webapp.models.Appointment;
import au.edu.rmit.sept.webapp.models.Clinic;
import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.repositories.AppointmentRepository;
import au.edu.rmit.sept.webapp.services.AppointmentService;
import au.edu.rmit.sept.webapp.services.ClinicService;
import au.edu.rmit.sept.webapp.services.PetService;
import au.edu.rmit.sept.webapp.services.UserService;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.config.location=classpath:application-test.properties")
@Transactional
@Rollback
public class AppointmentServiceTest {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    private Clinic clinic;
    private User vet;
    private User patient;

    @BeforeEach
    public void setUp() {
        // Clear the repository before each test (for testing purposes)
        appointmentRepository.deleteAll();

        // Create and save a sample clinic
        clinic = new Clinic();
        clinic.setClinicName("Test Clinic");
        clinic.setClinicAddress("123 Pet St");
        clinic.setPrice(100.0);
        clinicService.saveClinic(clinic);

        // Create a sample vet
        vet = new User();
        vet.setFirstName("Vet1");
        vet.setEmail("vet1@gmail.com");
        vet.setPassword("password");
        vet.setUserRole("Vet");
        userService.saveUser(vet); // Save the vet in the database

        // Create a sample patient
        patient = new User();
        patient.setFirstName("Patient1");
        patient.setEmail("patient1@gmail.com");
        patient.setPassword("password");
        patient.setUserRole("Patient");
        userService.saveUser(patient); // Save patient in the database

        // Create a sample appointment with null patient and pet fields, available by
        // default
        Appointment appointment = new Appointment();
        appointment.setClinic(clinic);
        appointment.setVet(vet);
        appointment.setAppointmentDate(LocalDateTime.of(2024, 9, 13, 10, 0)); // Date & time from your screenshot
        appointment.setAvailable(true); // Initially, the appointment is available
        appointment.setStatus("Available"); // Mark as available

        // Save the appointment
        appointmentRepository.save(appointment);
    }

    @Test
    public void testFetchAvailableAppointments() {
        // Fetch available appointments
        List<Appointment> availableAppointments = appointmentService.getAvailableAppointments(clinic.getClinicId());

        // Assert that we have 1 available appointment
        assertNotNull(availableAppointments);
        assertEquals(1, availableAppointments.size(), "There should be 1 available appointment for the clinic");

        // Assert that the appointment is available and the clinicId matches
        Appointment fetchedAppointment = availableAppointments.get(0);
        assertTrue(fetchedAppointment.isAvailable(), "The appointment should be available");
        assertEquals(clinic.getClinicId(), fetchedAppointment.getClinic().getClinicId(), "The clinicId should match");
    }

    @Test
    public void testConfirmAppointment() {
        // Fetch the appointment created in setUp()
        Appointment appointment = appointmentRepository.findAll().get(0);

        // Create a sample pet for the patient
        Pet pet = new Pet();
        pet.setPetName("Buddy");
        pet.setPetType("Dog");
        pet.setOwner(patient); // Set patient as owner of the pet
        petService.createPet(pet);

        // Confirm the appointment (set patient, pet, and consultation type)
        Appointment confirmedAppointment = appointmentService.confirmAppointment(
                appointment.getAppointmentId(), patient, pet, "Consultation");

        // Assert the appointment fields have been updated
        assertNotNull(confirmedAppointment);
        assertEquals("Patient1", confirmedAppointment.getPatient().getFirstName(),
                "The patient name should be Patient1");
        assertEquals("Buddy", confirmedAppointment.getPet().getPetName(), "The pet name should be Buddy");
        assertEquals("Consultation", confirmedAppointment.getConsultationType(),
                "Consultation type should be Consultation");
        assertFalse(confirmedAppointment.isAvailable(), "The appointment should now be unavailable");
    }

    @Test
    public void testCancelAppointment() {
        // Retrieve the appointment from the setup (assuming only 1 appointment in the
        // repository)
        Appointment appointment = appointmentRepository.findAll().get(0);

        // Cancel the appointment
        boolean isCancelled = appointmentService.cancelAppointment(appointment.getAppointmentId());

        // Retrieve the appointment after cancellation
        Appointment cancelledAppointment = appointmentRepository.findById(appointment.getAppointmentId()).orElseThrow();

        // Assert the cancellation succeeded
        assertTrue(isCancelled, "Appointment cancellation should return true");

        // Assert that the appointment status is updated to 'Cancelled'
        assertEquals("Cancelled", cancelledAppointment.getStatus(), "The appointment status should be 'Cancelled'");

        // Assert the appointment is marked as available
        assertTrue(cancelledAppointment.isAvailable(), "The appointment should be marked as available");
    }

    @Test
    public void testMakeAppointmentAvailable() {
        // Retrieve the appointment created in setUp
        Appointment appointment = appointmentRepository.findAll().get(0);

        // Ensure the appointment is unavailable after confirmation
        assertTrue(appointment.isAvailable(), "The appointment should initially be available");

        // Create and save a pet for the patient
        Pet pet = new Pet();
        pet.setPetName("Buddy");
        pet.setPetType("Dog");
        pet.setOwner(patient); // Set patient as owner of the pet
        Pet savedPet = petService.createPet(pet);

        // Confirm the appointment to simulate a booked state
        appointmentService.confirmAppointment(appointment.getAppointmentId(), patient, savedPet, "Consultation");

        // Ensure the appointment is unavailable after confirmation
        Appointment confirmedAppointment = appointmentRepository.findById(appointment.getAppointmentId()).orElseThrow();
        assertFalse(confirmedAppointment.isAvailable(), "The appointment should be unavailable after confirmation");

        // Now, make the appointment available again
        Appointment updatedAppointment = appointmentService
                .makeAppointmentAvailable(confirmedAppointment.getAppointmentId());

        // Assert that the appointment is now available
        assertTrue(updatedAppointment.isAvailable(), "The appointment should be marked as available");

        // Assert that the patient and pet fields are reset to null
        assertNull(updatedAppointment.getPatient(), "The patient should be reset to null");
        assertNull(updatedAppointment.getPet(), "The pet should be reset to null");

        // Assert that the consultation type is reset to null
        assertNull(updatedAppointment.getConsultationType(), "The consultation type should be reset to null");
    }

    @Test
    public void testConfirmAppointmentCreatesNewPet() {
        // Retrieve the appointment created in setUp()
        Appointment appointment = appointmentRepository.findAll().get(0);

        // Ensure the patient has no pets initially
        List<Pet> petsBefore = petService.findPetsByOwnerId(patient.getUserId());
        assertTrue(petsBefore.isEmpty(), "The patient should have no pets initially");

        // Create and save a pet for the patient
        Pet pet = new Pet();
        pet.setPetName("Buddy");
        pet.setPetType("Dog");
        pet.setOwner(patient); // Set patient as owner of the pet
        petService.createPet(pet);

        // Confirm the appointment and pass the new pet details
        Appointment confirmedAppointment = appointmentService.confirmAppointment(
                appointment.getAppointmentId(), patient, pet, "Consultation");

        // Fetch pets after confirming the appointment
        List<Pet> petsAfter = petService.findPetsByOwnerId(patient.getUserId());

        // Assert that a new pet was created
        assertFalse(petsAfter.isEmpty(), "A new pet should be created for the patient");
        assertEquals(1, petsAfter.size(), "The patient should now have 1 pet");

        // Assert the appointment is confirmed and associated with the new pet
        assertNotNull(confirmedAppointment.getPet(), "The confirmed appointment should have a pet associated");
        assertEquals(petsAfter.get(0).getPetName(), confirmedAppointment.getPet().getPetName(),
                "The pet associated with the appointment should match the newly created pet");
    }
}