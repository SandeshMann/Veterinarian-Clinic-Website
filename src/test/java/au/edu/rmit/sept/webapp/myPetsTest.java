package au.edu.rmit.sept.webapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.repositories.PetRepository;
import au.edu.rmit.sept.webapp.services.PetService;

public class myPetsTest {

    @InjectMocks
    private PetService petService;

    @Mock
    private PetRepository petRepository;

    private User owner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Initialize test data
        owner = new User();
        owner.setUserId(45);
        owner.setFirstName("SpiderMan");
    }

    @Test
    void testAddPet() {
        // Given
        Pet pet = new Pet();
        pet.setPetName("Qwert");
        pet.setPetType("Dog");
        pet.setOwner(owner);

        when(petRepository.save(any(Pet.class))).thenReturn(pet);

        // When
        Pet createdPet = petService.createPet(owner, "Qwert", "Dog", "Labrador");

        // Then
        assertNotNull(createdPet);
        assertEquals("Qwert", createdPet.getPetName());
        assertEquals("Dog", createdPet.getPetType());
        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    void testRemovePet() {
        // Given
        int petId = 1;
        doNothing().when(petRepository).deleteById(petId); // tells it not to act on this command 

        // When
        petService.deletePetById(petId);

        // Then
        verify(petRepository, times(1)).deleteById(petId);
    }
    
}
