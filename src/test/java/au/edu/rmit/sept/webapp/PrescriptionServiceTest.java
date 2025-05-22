package au.edu.rmit.sept.webapp;

import au.edu.rmit.sept.webapp.models.Clinic;
import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.Prescriptions;
import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.repositories.PrescriptionRepository;
import au.edu.rmit.sept.webapp.services.ClinicService;
import au.edu.rmit.sept.webapp.services.PetService;
import au.edu.rmit.sept.webapp.services.PrescriptionService;
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
public class PrescriptionServiceTest {

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private UserService userService;

    @Autowired
    private PetService petService;

    private Clinic clinic;
    private User vet;
    private User patient;
    private Pet pet;

    @BeforeEach
    public void setUp() {
        // Clear the repository before each test (for testing purposes)

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

        // Create a sample patient
        pet = new Pet();
        pet.setPetName("Buddy");
        pet.setPetType("Dog");
        pet.setOwner(patient);
        petService.createPet(pet);

        // Create a sample prescription record
        Prescriptions prescriptions = new Prescriptions();
        prescriptions.setPrescriptionId(1);
        prescriptions.setPet(pet);
        prescriptions.setVet(vet);
        prescriptions.setPatient(patient);
        prescriptions.setClinicName(clinic.getClinicName());
        prescriptions.setClinic(clinic);
        prescriptions.setPrescriptionName("Rimadyl (Carprofen)");
        prescriptions.setPurpose("Pain relief and anti-inflammatory");
        prescriptions.setDosage("2 mg/lb (4.4 mg/kg) once daily, or 1 mg/lb (2.2 mg/kg) twice daily.");
        prescriptions.setDuration("7-14 days");
        prescriptions.setInstructions(
                "Administer with food to reduce the risk of gastrointestinal upset. Monitor for any signs of vomiting, diarrhea, or changes in appetite.");
        prescriptions.setDateIssued(LocalDateTime.of(2024, 9, 13, 10, 0));
        prescriptions.setExpiryDate(prescriptions.getDateIssued().plusDays(25));
        prescriptions.setUpdatedAt(LocalDateTime.of(2024, 9, 30, 10, 0));

        // Save the prescription
        prescriptionRepository.save(prescriptions);

    }

    /**
     * This test verifies the retrieval of prescription records for a specific
     * patient by their user ID.
     * The test first fetches the created prescription from the repository, ensuring
     * that it exists.
     * It then uses the service method to retrieve all prescriptions associated with
     * the patient.
     * The final assertion checks if the list of prescriptions contains the expected
     * record for the user.
     * This ensures that the getPrescriptionsByPatientId method correctly fetches
     * the patient's prescriptions from the database.
     */
    @Test
    public void testFindPresRecordById() {

        // Retrieve the prescription from the setup
        Prescriptions presRecord = prescriptionRepository.findAll().get(0);
        System.out.println("Prescription Record: " + presRecord.getPurpose());

        // find the precription record by user id
        List<Prescriptions> foundPrescriptionRecordById = prescriptionService
                .getPrescriptionsByPatientId(presRecord.getPatient().getUserId());

        // Assert the foundPrescriptionRecordById and return true if it contains created
        // prescription for the user.
        assertTrue(foundPrescriptionRecordById.contains(presRecord),
                "List of prescriptions should contain the created prescription for the user.");

    }

    /**
     * This test verifies the delete functionality of the prescription service.
     * It ensures that a prescription can be successfully removed from the database
     * by its ID. The test first confirms the existence of a prescription, then
     * deletes it, and finally checks if the prescription has been removed from the
     * repository.
     */
    @Test
    public void testDeletePrescriptionById() {
        // Retrieve the prescription from the setup
        Prescriptions presRecord = prescriptionRepository.findAll().get(0);

        // Ensure that the prescription exists in the repository
        assertNotNull(presRecord, "Prescription should exist in the database before deletion.");

        // Call the delete service to remove the prescription by ID
        prescriptionService.deletePrescriptionById(presRecord.getPrescriptionId());

        // Check if the prescription has been successfully deleted
        boolean successfullyDeleted = prescriptionRepository.existsById(presRecord.getPrescriptionId());

        // Assert that the prescription no longer exists in the repository
        assertFalse(successfullyDeleted, "Prescription should be deleted from the repository.");
    }

}
