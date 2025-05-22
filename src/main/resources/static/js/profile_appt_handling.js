// Tracks selected appointment
let selectedAppointmentId = null;

// Set the appointment ID when the "Cancel" button is clicked
function setAppointmentId(appointmentId) {
  selectedAppointmentId = appointmentId;
  console.log(selectedAppointmentId);
}

// Cancellation of appointment
function confirmCancelAppt() {
    if (selectedAppointmentId !== null) {
        fetch(`/appointments/cancel/${selectedAppointmentId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ status: 'Cancelled' })
        })
        .then(response => response.text()) // Use .text() to handle plain text responses
        .then(text => {
            // Optionally parse the text if it contains JSON, or use it directly
            console.log(text); // For debugging purposes
            if (text.includes("Appointment cancelled successfully")) {
                alert('Appointment cancelled successfully.');
                // Reload the page to reflect the changes
                window.location.reload(); 
            } else if (text.includes("Failed to cancel the appointment")) {
                alert('Failed to cancel the appointment.');
            } else {
                alert('An unexpected error occurred.');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while cancelling the appointment.');
        });
    }
}

// Handle cancellation of appointment by disballing all buttons

function disableRowButtons(apptId) {
    // Select the row based on the appointment ID
    const row = document.querySelector(`tr[data-appt-id="${apptId}"]`);

    // Get all buttons within the row
    const buttons = row.querySelectorAll('button');

    // Disable all buttons in the row
    buttons.forEach(button => {
      button.disabled = true;
    });
  }

// Add event listeners to time slot buttons
document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.select-time').forEach(button => {
        button.addEventListener('click', function() {
            // Find the corresponding radio input
            const radioInput = this.previousElementSibling;
            
            // Deselect all other time slots
            document.querySelectorAll('.select-time').forEach(slot => {
                slot.classList.remove('selected');
            });
            
            // Select the clicked radio button and highlight the time slot
            radioInput.checked = true;
            this.classList.add('selected');
        });
    });
  
    // Handle form submission
    document.getElementById('appointmentForm').addEventListener('submit', function(event) {
        event.preventDefault();
        
        // Get the selected time slot
        const selectedRadio = document.querySelector('input[name="time-slot"]:checked');
        const selectedTimeSlot = selectedRadio ? selectedRadio.value : '';
        
        console.log('Selected Time Slot:', selectedTimeSlot);
    });
});

function consoleLogFormResched(event) {
    // Prevent the default form submission
    event.preventDefault();
  
    // Get the selected date from flatpickr input
    const selectedDate = event.target.appointmentDate.value;
  
    // Get the selected time from the time slot radio buttons
    const selectedTimeRadio = document.querySelector('input[name="time-slot"]:checked');
  
    // Check if a time slot is selected
    if (!selectedTimeRadio) {
      console.error("No time slot selected");
      return; // Exit the function if no time slot is selected
    }
  
    const selectedTime = selectedTimeRadio.value;
  
    // Log the selected date and time to the console
    console.log("Selected Date:", selectedDate);
    console.log("Selected Time:", selectedTime);
}

function confirmReschedAppt(event) {
    event.preventDefault();
    const selectedDate = event.target.appointmentDate.value;
    const selectedTimeRadio = document.querySelector('input[name="time-slot"]:checked');
    if (!selectedTimeRadio) {
        console.error("No time slot selected");
        return;
    }
    const selectedTime = selectedTimeRadio.value;
    const newApptDateTime = selectedDate + "T" + selectedTime;
    if (selectedAppointmentId !== null) {
        console.log("Selected Date:", selectedDate);
        console.log("Selected Time:", selectedTime);
        fetch(`/appointments/reschedule/${selectedAppointmentId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ appointment_date: newApptDateTime })
        })
        .then(response => response.text())
        .then(text => {
            console.log(text);
            if (text.includes("Appointment rescheduled successfully")) {
                alert('Appointment rescheduled successfully');
                window.location.reload();
            } else if (text.includes("Failed to reschedule the appointment")) {
                alert('Failed to reschedule the appointment.');
            } else {
                alert('An unexpected error occurred');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while rescheduling the appointment');
        });
    }
}

(function () {
    'use strict';
    window.addEventListener('load', function () {
        // Fetch all forms we want to apply custom Bootstrap validation styles to
        const forms = document.getElementsByClassName('needs-validation');
        
        // Loop over them and prevent submission if they're invalid
        Array.prototype.filter.call(forms, function (form) {
            form.addEventListener('submit', function (event) {
                if (form.checkValidity() === false) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        });
    }, false);
})();

// Add event listeners for validating card info fields
document.getElementById('cardNumberProfile').addEventListener('input', validateCardDetails);
document.getElementById('expiryDateProfile').addEventListener('input', validateCardDetails);
document.getElementById('cvvProfile').addEventListener('input', validateCardDetails);

// Validation function for card details
function validateCardDetails() {
    const cardNumber = document.getElementById('cardNumberProfile').value.trim();
    const expiryDate = document.getElementById('expiryDateProfile').value.trim();
    const cvv = document.getElementById('cvvProfile').value.trim();
    
    // Validate the card number: must be exactly 16 digits
    const cardNumberValid = /^\d{4}\s\d{4}\s\d{4}\s\d{4}$/.test(cardNumber);
    
    // Validate the expiry date: MM/YY format and not expired
    const expiryDateValid = /^(0[1-9]|1[0-2])\/\d{2}$/.test(expiryDate) && validateExpiryDate(expiryDate);
    
    // Validate the CVV: must be 3 digits
    const cvvValid = /^\d{3}$/.test(cvv);

    // Enable the Submit button if all are valid
    const isFormValid = cardNumberValid && expiryDateValid && cvvValid;
    document.getElementById('payNowBtn').disabled = !isFormValid;

    // For debugging: Log the results of the validation
    console.log({
        cardNumberValid,
        expiryDateValid,
        cvvValid,
        isFormValid
    });
}

// Function to validate expiry date (must not be in the past)
function validateExpiryDate(expiryDate) {
    const [month, year] = expiryDate.split('/');
    const now = new Date();
    const expiry = new Date(`20${year}`, month - 1, 1);  // Month is 0-indexed
    
    // Ensure expiry date is not in the past
    return expiry > now;
}