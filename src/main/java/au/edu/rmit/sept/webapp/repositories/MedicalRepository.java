package au.edu.rmit.sept.webapp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import au.edu.rmit.sept.webapp.models.MedicalRecords;

@Repository
public interface MedicalRepository extends JpaRepository<MedicalRecords, Integer> {
    List<MedicalRecords> findByPatient_UserId(int patientId);

    Optional<MedicalRecords> findByRecordId(int recordId);
}
