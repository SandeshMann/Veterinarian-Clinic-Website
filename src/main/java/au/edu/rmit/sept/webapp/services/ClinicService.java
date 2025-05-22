package au.edu.rmit.sept.webapp.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import au.edu.rmit.sept.webapp.models.Clinic;
import au.edu.rmit.sept.webapp.repositories.ClinicRepository;

@Service
public class ClinicService {
    // Service class for Clinic entity
    // This class is used to interact with the database and perform CRUD operations
    // on the Clinic entity
    // It is responsible for handling business logic related to clinics

    // Dependencies
    @Autowired
    private ClinicRepository clinicRepository;

    // Constructor
    public ClinicService(ClinicRepository clinicRepository) {
        this.clinicRepository = clinicRepository;
    }

    // Method to get all clinics
    public List<Clinic> getAllClinics() {
        return clinicRepository.findAll();
    }

    // Method to get clinic by ID
    public Clinic getClinicById(int id) {
        return clinicRepository.findById(id).orElse(null);
    }

    // Method to save clinic
    public Clinic saveClinic(Clinic clinic) {
        return clinicRepository.save(clinic);
    }

    // Method to delete clinic by ID
    public void deleteClinicById(int id) {
        clinicRepository.deleteById(id);
    }

    public Clinic getClinicByName(String clinicName) {
        Clinic clinic = clinicRepository.findByClinicName(clinicName);
        if (clinic != null) {
            return clinic;
        }
        return null; // return null if clinic with the given name is not found
    }
}
