package au.edu.rmit.sept.webapp;

import org.junit.jupiter.api.Test;

import au.edu.rmit.sept.webapp.controllers.SavedResourcesController;
import au.edu.rmit.sept.webapp.repositories.SavedResourcesRepository;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(SavedResourcesController.class)
public class SavedResourcesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SavedResourcesRepository savedResourcesRepository;

    @Test
    @WithMockUser(username = "johnny23@gmail.com", roles = { "USER" })
    public void testDeleteSavedResource() throws Exception {
        // Mock repository behavior
        Mockito.doNothing().when(savedResourcesRepository).deleteById(Mockito.anyInt());

        // Perform the POST request to delete a saved resource
        mockMvc.perform(post("/savedresources/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile#pills-resources-tab"));

        // Verify that the repository's deleteById method was called with the correct ID
        // (1)
        Mockito.verify(savedResourcesRepository, Mockito.times(1)).deleteById(1);
        // Success print mssg.
        System.out.println("\n\ntestDeleteSavedResource passed successfully!!!!!\n\n");
    }
}
