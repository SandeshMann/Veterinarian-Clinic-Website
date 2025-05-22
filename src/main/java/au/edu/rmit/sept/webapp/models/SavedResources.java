package au.edu.rmit.sept.webapp.models;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "savedresources")
public class SavedResources {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int savedResourcesId;

    // Foreign key to the user table
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Changed from int to User entity

    // Foreign key to the resources table
    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = false)
    private Resources resource;  // Changed from int to Resources entity

    // Timestamp for when the resource was saved
    @Column(name = "saved_at", nullable = false) 
    private Timestamp savedAt;

    // Getters and Setters

    public int getSavedResourcesId() {
        return savedResourcesId;
    }

    public void setSavedResourcesId(int savedResourcesId) {
        this.savedResourcesId = savedResourcesId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Resources getResource() {
        return resource;
    }

    public void setResource(Resources resource) {
        this.resource = resource;
    }

    public Timestamp getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(Timestamp savedAt) {
        this.savedAt = savedAt;
    }
}