package au.edu.rmit.sept.webapp;

import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("unused")
@SpringBootTest
@AutoConfigureMockMvc
public class UserLoginTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    // Unit test for attempting to log in the user with valid credentials (email and
    // password).
    @Test
    public void testASuccessLogin() throws Exception {
        // Defining a mock email and password for the user for testing.
        String userEmail = "patient1479@petowner.com";
        String userPassword = "Passs6666";

        // Creating a mock user which does not exist
        User mockUser = new User();
        // Set the mocked user email
        mockUser.setEmail(userEmail);
        // Set the mocked user password
        mockUser.setPassword(userPassword);
        // When findUserByEmail is called, return the mock user
        when(userService.findUserByEmail(userEmail)).thenReturn(mockUser);

        // Now performing teh logging request
        // Now performing the logging request
        // param("username", userEmail) sets the form parameter "username"
        // to the value "patient1479@petowner.com".
        // .param("password", userPassword) sets the form parameter "password" to
        // "Passs6666"
        // which will also pass as it's a valid credential.
        mockMvc.perform(post("/login")
                .param("username", userEmail)
                .param("password", userPassword))
                .andExpect(status().is3xxRedirection()); // Expecting to be redirected to profile page.
        // .andExpect(redirectedUrl("/profile")); // Redirect to profile page with
        // successful login
    }

    // Unit test for attempting to log in the user with invalid credentials
    @Test
    public void testALoginFailure() throws Exception {
        // Defining a mock email and password for the user for testing.
        String userEmail = "esmat@gmail.com";
        String userPassword = "esmat2024";

        // Creating a mock user which will be returned by the UserService
        User mockUser = new User();
        // Set the mocked user email
        mockUser.setEmail(userEmail);
        // Set the mocked user password
        mockUser.setPassword(userPassword);
        // When findUserByEmail is called, return the mock user
        when(userService.findUserByEmail(userEmail)).thenReturn(mockUser);

        // Now performing the logging request
        // param("username", userEmail) sets the form parameter "username"
        // to the value "esmat@gmail.com".
        // .param("password", userPassword) sets the form parameter "password" to
        // "esmat2024"
        // which will also fail as it's not a valid credential.
        mockMvc.perform(post("/login")
                .param("username", userEmail)
                .param("password", userPassword))
                .andExpect(status().is3xxRedirection()) // Expecting a redirection due to failed login.
                .andExpect(redirectedUrl("/login?error=true")); // Redirect the user to login page with an error.

    }

}
