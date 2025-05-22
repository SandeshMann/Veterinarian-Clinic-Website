package au.edu.rmit.sept.webapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.models.Prescriptions;
import au.edu.rmit.sept.webapp.repositories.PrescriptionRepository;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    // Get all the prescriptions from the repository.
    // This retrieves every prescription record stored in the database, regardless
    // of the patient or clinic.
    public List<Prescriptions> getAllPrescriptions() {
        return prescriptionRepository.findAll();
    }

    // Get all the prescriptions associated with a specific patient ID.
    // This function filters and returns only the prescriptions that belong to a
    // particular patient,
    // identified by their unique patientId (linked via the user).
    public List<Prescriptions> getPrescriptionsByPatientId(int patientId) {
        return prescriptionRepository.findByPatient_UserId(patientId);
    }

    // Delete a prescription by its unique ID.
    // This function removes a prescription from the database by its primary key
    // (prescriptionId),
    // which ensures that the specific prescription record is permanently deleted.
    public void deletePrescriptionById(int id) {
        prescriptionRepository.deleteById(id);
    }

    public boolean payPrescription(int prescriptionId) {
        // optional to handle cases of non existant appts
        Optional<Prescriptions> optionalPrescription = prescriptionRepository.findById(prescriptionId);

        if (optionalPrescription.isPresent()) {
            Prescriptions prescription = optionalPrescription.get();
            prescription.setPaid(1);
            prescription.setArrivalCountDown(7);
            prescriptionRepository.save(prescription);
            return true;
        } else {
            return false;
        }
    }

}
