package au.edu.rmit.sept.webapp.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appointmentId;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = true)
    private User patient; // Nullable because patient may be unset when available

    @ManyToOne
    @JoinColumn(name = "vet_id", nullable = true) // Might have to get rid of vet id, may not be necessary column
    private User vet; // Vet is always set

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = true)
    private Pet pet; // Nullable because pet may be unset when available

    @Column(nullable = true)
    private String consultationType; // Nullable because consultation type may be unset when available

    @Column(nullable = false)
    private boolean isAvailable = true; // Tracks availability status

    @Column(nullable = false)
    private String status; // Tracks status for booked, completed, cancelled

    @ManyToOne
    @JoinColumn(name = "clinic_id", nullable = false)
    @JsonBackReference
    private Clinic clinic; // Clinic is always set

    @Column(nullable = false)
    private LocalDateTime appointmentDate;

    // Getters and setters
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public User getVet() {
        return vet;
    }

    public void setVet(User vet) {
        this.vet = vet;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getConsultationType() {
        return consultationType;
    }

    public void setConsultationType(String consultationType) {
        this.consultationType = consultationType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public String getAppointmentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' h:mma");
        return appointmentDate.format(formatter);
    }

    public LocalDateTime getAppointmentLocalDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
}