package au.edu.rmit.sept.webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.models.MedicalRecords;
import au.edu.rmit.sept.webapp.repositories.MedicalRepository;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRepository medicalRepository;

    // Find a medical record by its ID
    public MedicalRecords findMedRecordById(int recordId) {
        return medicalRepository.findByRecordId(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid record ID"));
    }

    // Delete a medical record by its ID
    public void deleteMedRecordById(int recordId) {
        MedicalRecords medRecord = medicalRepository.findByRecordId(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid record ID"));
        medicalRepository.delete(medRecord);
    }
}
