document.addEventListener("DOMContentLoaded", function() {
    // Show the add-prescription-form and hide view-prescriptions when clicking "Add Prescription"
    const addPrescriptionBtn = document.getElementById("add-prescription-btn");
    const cancelPrescriptionBtn = document.getElementById("cancel-presc-btn");
    const viewPrescriptionsDiv = document.getElementById("view-prescriptions");
    const addPrescriptionFormDiv = document.getElementById("add-prescription-form");
  
    if (addPrescriptionBtn) {
        addPrescriptionBtn.addEventListener("click", function() {
            viewPrescriptionsDiv.style.display = "none";
            addPrescriptionFormDiv.style.display = "block";
        });
    }
  
    if (cancelPrescriptionBtn) {
        cancelPrescriptionBtn.addEventListener("click", function() {
            addPrescriptionFormDiv.style.display = "none";
            viewPrescriptionsDiv.style.display = "block";
        });
    }
});

function requestRefill(button) {
    // Disable the Request Refill button
    const refillButton = document.getElementById('refill-btn')
    refillButton.disabled = true;
    refillButton.innerText = 'Requested';

    // Show the Track button
    const trackButton = button.parentElement.querySelector('.track-btn'); // Use querySelector to get the Track button
    trackButton.style.display = 'inline-block'; // Change display style to show the button

    // Set the estimated delivery date to be 7 days from today (Change this later in the database)
    const estimatedDeliveryDate = new Date();
    estimatedDeliveryDate.setDate(estimatedDeliveryDate.getDate() + 7);
    const formattedDate = estimatedDeliveryDate.toDateString();
    document.querySelector('.estimated-date p').textContent = formattedDate;
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
document.getElementById('cardNumberScript').addEventListener('input', validateCardDetails);
document.getElementById('expiryDateScript').addEventListener('input', validateCardDetails);
document.getElementById('cvvScript').addEventListener('input', validateCardDetails);

// Validation function for card details
function validateCardDetails() {
    const cardNumber = document.getElementById('cardNumberScript').value.trim();
    const expiryDate = document.getElementById('expiryDateScript').value.trim();
    const cvv = document.getElementById('cvvScript').value.trim();
    
    // Validate the card number: must be exactly 16 digits
    const cardNumberValid = /^\d{4}\s\d{4}\s\d{4}\s\d{4}$/.test(cardNumber);
    
    // Validate the expiry date: MM/YY format and not expired
    const expiryDateValid = /^(0[1-9]|1[0-2])\/\d{2}$/.test(expiryDate) && validateExpiryDate(expiryDate);
    
    // Validate the CVV: must be 3 digits
    const cvvValid = /^\d{3}$/.test(cvv);

    // Enable the Submit button if all are valid
    const isFormValid = cardNumberValid && expiryDateValid && cvvValid;
    document.getElementById('payNowBtnScript').disabled = !isFormValid;

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


// const 
// circles = document.querySelectorAll(".track-circle"),


function setCurrentStep(arrivalCountdown){
    console.log(arrivalCountdown)
    let currentStep = getCurrentStepBasedOnNumber(arrivalCountdown)

    const trackCircles = document.querySelectorAll('.track-circle');
    const progressBar = document.querySelector(".track-indicator");

    trackCircles.forEach((circle,index) => {
        circle.classList[`${index < currentStep ? "add" : "remove"}`]("active");
    })

    progressBar.style.height = `${
    ((currentStep - 1) / (trackCircles.length - 1)) * 100
    }%`;

}

function getCurrentStepBasedOnNumber(number) {
 
    if (number >= 1 && number <= 2) {
        currentStep = 3;
    } else if (number >= 3 && number <= 5) {
        currentStep = 2;
    } else if (number >= 6 && number <= 7) {
        currentStep = 1;
    } else if (number == 0) {
        currentStep = 4;
    } else {
        currentStep = null;
    }

    return currentStep;
}

function setPrescriptionId(prescriptionId) {
    selectedPrescriptionId = prescriptionId;
    console.log(selectedPrescriptionId);
}

function prescriptionPaid() {
    if (selectedPrescriptionId !== null) {
        fetch(`/prescription/paid/${selectedPrescriptionId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ paid: '1' })
        })
        .then(response => response.text())
        .then(text => {
            console.log(text);
            if (text.includes("Prescription paid successfully.")) {
                alert('Prescription paid successfully.');
                window.location.reload();
            } else if (text.includes("Failed to pay for prescription.")) {
                alert('Failed to pay for prescription.')
            } else {
                alert('An unexpected error occurred.')
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('An error occurred while paying for prescriptions.');
        });
    }
}