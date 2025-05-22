package au.edu.rmit.sept.webapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import au.edu.rmit.sept.webapp.models.Clinic;

@Repository
public interface ClinicRepository extends JpaRepository<Clinic, Integer> {
    // Query to find all clinics
    @Query("SELECT c FROM Clinic c")
    List<Clinic> findAll();

    Clinic findByClinicName(String clinicName);
}
