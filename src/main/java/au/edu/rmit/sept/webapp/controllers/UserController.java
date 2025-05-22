package au.edu.rmit.sept.webapp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.services.UserService;

@RestController
@RequestMapping("/users")  // Base URL for all User operations
public class UserController {
    // Endpoint to get all users (or modify to suit your needs)
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @Autowired
    private UserService userService;

    // Endpoint to create a new user
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    // Endpoint to get a user by their ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.findUserById(id);
    }
}
