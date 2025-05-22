package au.edu.rmit.sept.webapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import au.edu.rmit.sept.webapp.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Custom queries here if needed later
    
    // Method to find a user by email address
    User findByEmail(String email);    
}
