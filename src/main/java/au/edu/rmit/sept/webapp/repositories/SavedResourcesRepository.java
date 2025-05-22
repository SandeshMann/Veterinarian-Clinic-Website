package au.edu.rmit.sept.webapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import au.edu.rmit.sept.webapp.models.SavedResources;

public interface SavedResourcesRepository extends JpaRepository<SavedResources, Integer> {
    // Returns a list of saved resources for a user
    List<SavedResources> findByUser_UserId(int userId);
}
    