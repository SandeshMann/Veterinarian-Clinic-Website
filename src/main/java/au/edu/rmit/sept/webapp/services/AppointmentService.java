package au.edu.rmit.sept.webapp.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.models.Appointment;
import au.edu.rmit.sept.webapp.models.Clinic;
import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.User;
import au.edu.rmit.sept.webapp.repositories.AppointmentRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public Appointment createAppointment(
            User patient, Clinic clinic, String appointmentDateTime, Pet pet, String consultationType) {

        LocalDateTime appointmentDateTimeParsed = LocalDateTime.parse(appointmentDateTime,
                DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // Create and save new appointment
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setClinic(clinic);
        appointment.setAppointmentDate(appointmentDateTimeParsed); // Convert to Date if needed
        appointment.setPet(pet);
        appointment.setConsultationType(consultationType);
        appointment.setAvailable(false); // Mark as booked
        appointment.setStatus("Booked");

        return appointmentRepository.save(appointment);
    }

    // Confirm appointment (set patient, pet, consultation type, and mark as
    // unavailable)
    public Appointment confirmAppointment(int appointmentId, User patient, Pet pet, String consultationType) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid appointment ID"));

        // Set the patient, pet, and consultation type
        appointment.setPatient(patient);
        appointment.setPet(pet);
        appointment.setConsultationType(consultationType);

        // Mark the appointment as unavailable
        appointment.setAvailable(false);

        // Save the updated appointment back to the database
        return appointmentRepository.save(appointment);
    }

    // Make appointment available again (reset patient, pet, consultation type, and
    // mark as available)
    public Appointment makeAppointmentAvailable(int appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid appointment ID"));

        // Reset patient, pet, and consultation type, and mark the appointment as
        // available
        appointment.setPatient(null);
        appointment.setPet(null);
        appointment.setConsultationType(null);
        appointment.setAvailable(true);

        // Save the updated appointment back to the database
        return appointmentRepository.save(appointment);
    }

    // Get available appointments by clinic ID
    public List<Appointment> getAvailableAppointments(int clinicId) {
        return appointmentRepository.findAvailableAppointmentsByClinic(clinicId);
    }

    public List<Appointment> findApptsByUserId(int patient_id) {
        return appointmentRepository.findByPatient_UserId(patient_id);
    }

    public boolean cancelAppointment(int apptId) {
        // optional to handle cases of non existant appts
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(apptId);

        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            appointment.setStatus("Cancelled");
            appointmentRepository.save(appointment);
            return true;
        } else {
            return false;
        }
    }

    public boolean reschedAppointment(int apptId, LocalDateTime newApptDate) {
        Optional<Appointment> optionalAppointment = appointmentRepository.findById(apptId);

        if (optionalAppointment.isPresent()) {
            Appointment appointment = optionalAppointment.get();
            appointment.setAppointmentDate(newApptDate);
            appointmentRepository.save(appointment);
            return true;
        }

        return false;
    }
}