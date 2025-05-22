package au.edu.rmit.sept.webapp.models;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int petId;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = true)
    private User owner; // References the User (patient/owner)

    @Column(nullable = false)
    private String petName;

    @Column(nullable = false)
    private String petType; // e.g., 'Dog', 'Cat', 'Bird', etc.

    private String petBreed;

    private LocalDate createdAt;

    // Cascade delete related entities i.e. Medical records + Prescriptions

    // Cascade delete on MedicalRecord entities
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalRecords> medicalRecords;

    // Cascade delete on Prescription entities
    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prescriptions> prescriptions;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;

    // Getters and Setters

    public int getPetId() {
        return petId;
    }

    public void setPetId(Integer petId) {
        this.petId = petId;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(String petType) {
        this.petType = petType;
    }

    public String getPetBreed() {
        return petBreed;
    }

    public void setPetBreed(String petBreed) {
        this.petBreed = petBreed;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
