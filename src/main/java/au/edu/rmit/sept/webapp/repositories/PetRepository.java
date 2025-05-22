package au.edu.rmit.sept.webapp.repositories;

import au.edu.rmit.sept.webapp.models.Pet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {

    // Find pets by the owner's userId
    List<Pet> findByOwner_UserId(int ownerId);

    // Find all the pets
    List<Pet> findAll();
}