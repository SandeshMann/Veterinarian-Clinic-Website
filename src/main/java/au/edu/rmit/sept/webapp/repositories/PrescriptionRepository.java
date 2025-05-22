package au.edu.rmit.sept.webapp.repositories;


import au.edu.rmit.sept.webapp.models.Prescriptions;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescriptions, Integer> {
    
    // Custom query to find prescriptions by patient ID
    List<Prescriptions> findByPatient_UserId(int userId);

    // Custom query to find prescriptions by vet ID
    List<Prescriptions> findByVet_UserId(int vetId);
}
