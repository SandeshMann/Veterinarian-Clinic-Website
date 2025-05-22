package au.edu.rmit.sept.webapp;

import au.edu.rmit.sept.webapp.models.MedicalRecords;
import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.repositories.MedicalRepository;
import au.edu.rmit.sept.webapp.services.MedicalRecordService;
import au.edu.rmit.sept.webapp.services.PetService;
import au.edu.rmit.sept.webapp.services.UserService;
import jakarta.transaction.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

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
public class MedicalRecordServiceTest {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private MedicalRepository medicalRepository;

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    private User vet;
    private User patient;
    private Pet pet;

    @BeforeEach
    public void setUp() {
        // Clear repository for testing
        medicalRepository.deleteAll();

        // Create sample patient
        patient = new User();
        patient.setFirstName("John");
        patient.setEmail("john@example.com");
        patient.setPassword("password");
        patient.setUserRole("Patient");
        userService.saveUser(patient);

        // Create sample vet
        vet = new User();
        vet.setFirstName("Dr. Smith");
        vet.setEmail("drsmith@example.com");
        vet.setPassword("password");
        vet.setUserRole("Vet");
        userService.saveUser(vet);

        // Create sample pet
        pet = new Pet();
        pet.setPetName("Max");
        pet.setPetType("Dog");
        pet.setOwner(patient); // Associate pet with patient
        petService.createPet(pet);
    }

    @Test
    public void testCreateMedicalRecord() {
        // Create a new medical record
        MedicalRecords newRecord = new MedicalRecords();
        newRecord.setPet(pet);
        newRecord.setVet(vet);
        newRecord.setPatient(patient);
        newRecord.setDateIssued(LocalDate.now());
        newRecord.setClinicName("New Clinic");
        newRecord.setRecordType("Routine Checkup"); // Setting the record type

        MedicalRecords savedRecord = medicalRepository.save(newRecord);

        // Validate the saved record
        assertNotNull(savedRecord, "Medical record should be saved");
        assertEquals("New Clinic", savedRecord.getClinicName(), "Clinic name should be New Clinic");
        assertEquals("Routine Checkup", savedRecord.getRecordType(), "Record type should be Routine Checkup");
        assertNotNull(savedRecord.getCreatedAt(), "createdAt should be automatically set");
        assertNotNull(savedRecord.getUpdatedAt(), "updatedAt should be automatically set");

        // Allow a small difference in time between createdAt and updatedAt (in
        // milliseconds)
        Duration timeDifference = Duration.between(savedRecord.getCreatedAt(), savedRecord.getUpdatedAt());
        assertTrue(timeDifference.toMillis() < 100, "createdAt and updatedAt should be almost equal");
    }

    @Test
    public void testFetchMedicalRecordById() {
        // Create and save a medical record
        MedicalRecords medicalRecord = new MedicalRecords();
        medicalRecord.setPet(pet);
        medicalRecord.setVet(vet);
        medicalRecord.setRecordType("Vaccination");
        medicalRecord.setPatient(patient);
        medicalRecord.setDateIssued(LocalDate.now());
        medicalRecord.setClinicName("Happy Pet Clinic");
        medicalRecord.setRecordType("Routine Checkup"); // Set the record type

        medicalRepository.save(medicalRecord);

        // Fetch the medical record by ID
        MedicalRecords fetchedRecord = medicalRecordService.findMedRecordById(medicalRecord.getRecordId());

        // Validate the record
        assertNotNull(fetchedRecord, "Medical record should not be null");
        assertEquals("Max", fetchedRecord.getPet().getPetName(), "Pet name should be Max");
        assertEquals("Dr. Smith", fetchedRecord.getVet().getFirstName(), "Vet name should be Dr. Smith");
        assertEquals("Routine Checkup", fetchedRecord.getRecordType(), "Record type should match");
    }

    @Test
    public void testDeleteMedicalRecord() {
        // Create and save a medical record
        MedicalRecords medicalRecord = new MedicalRecords();
        medicalRecord.setPet(pet);
        medicalRecord.setVet(vet);
        medicalRecord.setPatient(patient);
        medicalRecord.setRecordType("Vaccination");
        medicalRecord.setDateIssued(LocalDate.now());
        medicalRecord.setClinicName("Happy Pet Clinic");
        medicalRecord.setRecordType("Routine Checkup"); // Ensure this is set as it's required

        medicalRepository.save(medicalRecord);

        // Fetch the medical record before deletion
        MedicalRecords recordToDelete = medicalRecordService.findMedRecordById(medicalRecord.getRecordId());
        assertNotNull(recordToDelete, "Record to delete should exist");

        // Delete the medical record
        medicalRecordService.deleteMedRecordById(medicalRecord.getRecordId());

        // Attempt to fetch it again, expecting an exception
        assertThrows(IllegalArgumentException.class, () -> {
            medicalRecordService.findMedRecordById(medicalRecord.getRecordId());
        }, "Record should be deleted and not found.");
    }

    @Test
    public void testFetchMedicalRecordsByPatient() {
        // Create and save a medical record
        MedicalRecords medicalRecord = new MedicalRecords();
        medicalRecord.setPet(pet);
        medicalRecord.setVet(vet);
        medicalRecord.setPatient(patient);
        medicalRecord.setRecordType("Vaccination");
        medicalRecord.setDateIssued(LocalDate.now());
        medicalRecord.setClinicName("Happy Pet Clinic");
        medicalRecord.setRecordType("Routine Checkup"); // Set the record type, important!

        medicalRepository.save(medicalRecord);

        // Fetch medical records for the patient
        List<MedicalRecords> patientRecords = medicalRepository.findByPatient_UserId(patient.getUserId());

        // Assert that the patient has 1 medical record
        assertNotNull(patientRecords, "Patient records should not be null");
        assertEquals(1, patientRecords.size(), "Patient should have 1 medical record");
        assertEquals("Max", patientRecords.get(0).getPet().getPetName(), "Pet name should be Max");
    }
}
