package au.edu.rmit.sept.webapp;

import au.edu.rmit.sept.webapp.models.Clinic;
import au.edu.rmit.sept.webapp.models.MedicalRecords;
import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.repositories.MedicalRepository;
import au.edu.rmit.sept.webapp.services.ClinicService;
import au.edu.rmit.sept.webapp.services.MedicalRecordService;
import au.edu.rmit.sept.webapp.services.PetService;
import au.edu.rmit.sept.webapp.services.UserService;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.config.location=classpath:application-test.properties")
@Transactional
@Rollback
public class MedicalServiceTest {

    @Autowired
    private MedicalRecordService medicalService;

    @Autowired
    private MedicalRepository medicalRepository;

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

        // Create a sample medical record

        MedicalRecords medRecord = new MedicalRecords();
        medRecord.setRecordId(1);
        medRecord.setClinicName(clinic.getClinicName());
        medRecord.setPatient(patient);
        medRecord.setVet(vet);
        medRecord.setPet(pet);
        medRecord.setDateIssued(LocalDate.of(2024, 9, 13));
        medRecord.setDescription(
                "Boris underwent a routine neutering procedure to remove the testes, preventing him from reproducing and reducing behaviors such as spraying and roaming. The surgery was performed under general anaesthesia, and there were no complications during the operation. ");
        medRecord.setPrescription("Metacam (meloxicam) 0.5mg once daily for 3 days.\n" + //
                "\n" + //
                "");
        medRecord.setUpdatedAt(LocalDateTime.of(2024, 9, 13, 10, 0));
        medRecord.setCreatedAt(LocalDateTime.of(2024, 9, 13, 10, 0));
        medRecord.setRecordType("Medical History");

        // Save the medRecord
        medicalRepository.save(medRecord);
    }

    @Test
    public void testFindMedRecordById() {

        // Retrieve the medical from the setup
        MedicalRecords medRecord = medicalRepository.findAll().get(0);

        // find the med record by id
        MedicalRecords foundMedicalRecordById = medicalService.findMedRecordById(medRecord.getRecordId());

        // Assert the med records are equal
        assertTrue(medRecord.getRecordId() == foundMedicalRecordById.getRecordId(),
                "Medical record ids equal should return true");

    }

}