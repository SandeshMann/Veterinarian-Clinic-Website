package au.edu.rmit.sept.webapp;

import au.edu.rmit.sept.webapp.controllers.ProfileController;
import au.edu.rmit.sept.webapp.models.Appointment;
import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.repositories.UserRepository;
import au.edu.rmit.sept.webapp.services.AppointmentService;
import au.edu.rmit.sept.webapp.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("unused")
@SpringBootTest
@AutoConfigureMockMvc
public class UserProfileTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private UserRepository userRepository; // Mocking UserRepository

    @InjectMocks
    private ProfileController profileController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(profileController).build();
    }

    // Unit test for getting upcoming appointments for the logged-in user.
    @Test
    public void testGetUpcomingAppointments() throws Exception {
        // Defining a mock email and password for the user for testing.
        String userEmail = "patient1479@petowner.com";
        String userPassword = "Passs6666";
        int userId = 12;
        // Creating a mock user which does not exist
        User mockUser = new User();
        // Set the mocked user email
        mockUser.setEmail(userEmail);
        // Set the mocked user password
        mockUser.setPassword(userPassword);

        // Mock the UserService call
        when(userService.findUserByEmail(userEmail)).thenReturn(mockUser);

        // Mock authentication
        // Create a mock authentication token using the mock user. 
        Authentication authentication = new UsernamePasswordAuthenticationToken(mockUser, null, List.of());

        // Create an empty security context. 
        // This is required to simulate a security environment where the user is authenticated.
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        
        // Set the created authentication token into the security context.
        securityContext.setAuthentication(authentication);

        // Set the security context into the SecurityContextHolder.
        // This makes the mocked security context available for the application during the test.
        SecurityContextHolder.setContext(securityContext);

        // Mock the upcoming appointments for the user
        Appointment upcomingAppointment = new Appointment();
        upcomingAppointment.setAppointmentDate(LocalDateTime.now()); // Tomorrow
        upcomingAppointment.setStatus("Booked");


        // Mock the upcoming appointments for the user
        Appointment upcomingAppointment2 = new Appointment();
        upcomingAppointment2.setAppointmentDate(LocalDateTime.now()); // Tomorrow
        upcomingAppointment2.setStatus("Booked");

        // Creating list of upcoming appointment for the user.
        List<Appointment> mockAppointments = List.of(upcomingAppointment, upcomingAppointment2);

        // When findUserByEmail is called, return the mock appointments.
        when(appointmentService.findApptsByUserId(userId)).thenReturn(mockAppointments);

        // Perform GET request and verify the response
        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk());
    }

}

