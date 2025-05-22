package au.edu.rmit.sept.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.repositories.UserRepository;

@Service
public class UserAuthenticationService implements UserDetailsService {

    // Importing UserRepository
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Retrieving the user from the repository using the provided email
        User user = userRepository.findByEmail(email);
        // If the user is not found, throw an exception that user not found with provided email.
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        // If user found with the provided email
        // build and return a UserDetails object with the user's email and password.
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())  // Set the username (user email) 
                .password(user.getPassword())   // Set the password
                .build();   // Build the user object.
    }
}

