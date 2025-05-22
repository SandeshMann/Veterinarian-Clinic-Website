package au.edu.rmit.sept.webapp.services;

import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.repositories.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    // Retrieve pets by owner (patient) ID
    public List<Pet> findPetsByOwnerId(int ownerId) {
        return petRepository.findByOwner_UserId(ownerId);
    }

    // Find a pet by its ID
    public Pet findById(int petId) {
        return petRepository.findById(petId).orElseThrow(() -> new IllegalArgumentException("Invalid pet ID"));
    }

    // Find a pet by its ID
        //Get all the prescriptions
    public List<Pet> getAllPrescriptions() {
        return petRepository.findAll();
    }

    // Create a new pet
    public Pet createPet(Pet pet) {
        return petRepository.save(pet);
    }

    // Create a new pet with patient as owner
    public Pet createPet(User owner, String petName, String petType, String petBreed) {
        Pet pet = new Pet();
        pet.setOwner(owner);
        pet.setPetName(petName);
        pet.setPetType(petType);
        pet.setPetBreed(petBreed);
        return petRepository.save(pet);
    }

    public void deletePetById(int petId) {
        petRepository.deleteById(petId);
    }

}