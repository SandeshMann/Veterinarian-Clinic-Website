package au.edu.rmit.sept.webapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import au.edu.rmit.sept.webapp.models.Clinic;
import au.edu.rmit.sept.webapp.repositories.ClinicRepository;
import au.edu.rmit.sept.webapp.services.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// Ensure that test class is using the application-test.properties file
@SpringBootTest(properties = "spring.config.location=classpath:application-test.properties")
@Transactional
@Rollback
public class ClinicServiceTest {

    @Autowired
    private ClinicService clinicService;

    @Autowired
    private ClinicRepository clinicRepository;

    // Test case for getAllClinics() method
    // Positive testing: Verifying the service correctly retrieves clinics when they exist.
    // Boundary testing: Ensuring the service behaves as expected when two clinics are added.
    // User Story: Appointment Booking, viewing available clinics
    @Test
    public void testGetAllClinics() {
        // Step 1: Positive Test Case - Create and save two test clinics with all relevant fields
        Clinic clinic1 = new Clinic();
        clinic1.setClinicName("Test Clinic 1");
        clinic1.setClinicAddress("Test Address 1");  // Required field for Clinic
        clinic1.setPrice(100.00);  // Set the required price field
        clinicRepository.save(clinic1);

        Clinic clinic2 = new Clinic();
        clinic2.setClinicName("Test Clinic 2");
        clinic2.setClinicAddress("Test Address 2");  // Required field for Clinic
        clinic2.setPrice(150.00);  // Set the required price field
        clinicRepository.save(clinic2);

        // Step 2: Fetch all clinics and verify the results
        List<Clinic> clinics = clinicService.getAllClinics();

        // Assertions to verify correctness:
        assertNotNull(clinics, "The clinics list should not be null.");
        assertEquals(2, clinics.size(), "The clinics list should contain two clinics.");  // Boundary testing (2 clinics)

        // Step 3: Ensure the clinics' names and prices match the test data
        assertEquals("Test Clinic 1", clinics.get(0).getClinicName(), "First clinic name mismatch.");
        assertEquals("Test Clinic 2", clinics.get(1).getClinicName(), "Second clinic name mismatch.");
        assertEquals(100.00, clinics.get(0).getPrice(), "First clinic price mismatch.");
        assertEquals(150.00, clinics.get(1).getPrice(), "Second clinic price mismatch.");
    }
}