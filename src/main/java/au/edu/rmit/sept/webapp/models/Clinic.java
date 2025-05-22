package au.edu.rmit.sept.webapp.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "Clinics")
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int clinicId;

    @Column(nullable = false)
    private String clinicName;

    @Column(nullable = false)
    private String clinicAddress;

    @Column(nullable = false)
    private Double price;

    // One clinic can have many appointments
    @OneToMany(mappedBy = "clinic")
    @JsonManagedReference
    private List<Appointment> appointments;

    // One clinic can have many vet schedules
    @OneToMany(mappedBy = "clinic")
    private List<VetSchedule> vetSchedules;

    // Location
    private double clinicLat;

    private double clinicLon;

    // Getters and Setters

    public int getClinicId() {
        return clinicId;
    }

    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<VetSchedule> getVetSchedules() {
        return vetSchedules;
    }

    public void setVetSchedules(List<VetSchedule> vetSchedules) {
        this.vetSchedules = vetSchedules;
    }

    public double getClinicLon() {
        return clinicLon;
    }

    public void setClinicLon(double clinicLon) {
        this.clinicLon = clinicLon;
    }

    public double getClinicLat() {
        return clinicLat;
    }

    public void setClinicLat(double clinicLat) {
        this.clinicLat = clinicLat;
    }

}