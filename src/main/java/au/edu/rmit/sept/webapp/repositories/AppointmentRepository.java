package au.edu.rmit.sept.webapp.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import au.edu.rmit.sept.webapp.models.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    // Query to find all available appointments by clinic
    @Query("SELECT a FROM Appointment a WHERE a.clinic.clinicId = :clinicId AND a.isAvailable = true")
    List<Appointment> findAvailableAppointmentsByClinic(@Param("clinicId") int clinicId);

    List<Appointment> findByPatient_UserId(int patientId);
}