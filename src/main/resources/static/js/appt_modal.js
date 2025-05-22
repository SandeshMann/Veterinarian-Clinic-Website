let currentTab = 0;
showTab(currentTab);

function showTab(n) {
  var x = document.getElementsByClassName("appt-tab");
  x[n].style.display = "block";
  if (n === 0) {
    document.getElementById("prevBtn").style.display = "none";
  } else {
    document.getElementById("prevBtn").style.display = "inline";
  }
  if (n === x.length - 1) {
    document.getElementById("nextBtn").innerHTML = "Submit";
    document.getElementById("nextBtn").removeAttribute('onclick');
    document.getElementById("nextBtn").setAttribute('type', 'submit');
    document.getElementById("nextBtn").setAttribute('onclick', 'showToast()')
    document.getElementById("nextBtn").disabled=true;

    openPayType(null, "p-online");

    var tablinks = document.getElementsByClassName("tablinks");
    for (var i = 0; i < tablinks.length; i++) {
      tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    document
      .querySelector("button[onclick=\"openPayType(event, 'p-online')\"]")
      .classList.add("active");
    
  }
  else {
    document.getElementById("nextBtn").setAttribute('onclick', 'nextPrev(1)');
    document.getElementById("nextBtn").innerHTML = "Next";
    document.getElementById("nextBtn").setAttribute('type', 'button');
    document.getElementById("nextBtn").disabled=true;
  }
  
}

function nextPrev(n) {
  let x = document.getElementsByClassName("appt-tab");
  x[currentTab].style.display = "none";  // Hide the current tab
  currentTab += n;
  if (currentTab >= x.length) {
    document.getElementById("bookingForm").submit();
    return false;
  }

  showTab(currentTab);
}

// Progress Bar
const circles = document.querySelectorAll(".circle"),
  progressBar = document.querySelector(".indicator"),
  buttons = document.querySelectorAll("button");

let currentStep = 1;

// function that updates the current step and updates the DOM
// Function to update steps
const updateSteps = (button) => {
  if (button.id === "nextBtn") {
    currentStep++;

  } else if (button.id === "prevBtn") {
    currentStep--;
  }

  // Update circles based on the current step
  circles.forEach((circle, index) => {
    circle.classList[`${index < currentStep ? "add" : "remove"}`]("active");
  });

  // Update the progress bar width based on the current step
  progressBar.style.height = `${
    ((currentStep - 1) / (circles.length - 1)) * 100
  }%`;

  // Disable buttons as needed
  if (currentStep === circles.length) {
    buttons[1].disabled = true; // Disable the next button
  } else if (currentStep === 1) {
    buttons[0].disabled = true; // Disable the prev button
  } else {
    buttons.forEach((btn) => (btn.disabled = false)); // Enable both buttons if not at the start or end
  }
};

// Add event listeners to buttons with the button as an argument
buttons.forEach((button) => {
  button.addEventListener("click", function () {
    updateSteps(button);
  });
});

// add click event listeners to all buttons
buttons.forEach((button) => {
  button.addEventListener("click", updateSteps);
});

// Reset currentStep when modal is closed
const myModal = document.getElementById("myModal");
myModal.addEventListener("hide.bs.modal", function () {
  currentStep = 1;
  currentTab = 0;

  const nextButton = document.getElementById("nextBtn");
  nextButton.disabled=true;

  // Reset progress bar and circles
  circles.forEach((circle, index) => {
    circle.classList.remove("active");
    if (index === 0) circle.classList.add("active"); // Only the first circle should be active
  });

  progressBar.style.width = "0%";

  // Reset the form steps
  const x = document.getElementsByClassName("appt-tab");
  for (let i = 0; i < x.length; i++) {
    x[i].style.display = "none";
  }
  showTab(currentTab);
});

function openPayType(evt, pType) {
  // Declare all variables
  var i, tabcontent, tablinks;

  // Get all elements with class="tabcontent" and hide them
  tabcontent = document.getElementsByClassName("tabcontent");
  for (i = 0; i < tabcontent.length; i++) {
    tabcontent[i].style.display = "none";
  }

  // Get all elements with class="tablinks" and remove the class "active"
  tablinks = document.getElementsByClassName("tablinks");
  for (i = 0; i < tablinks.length; i++) {
    tablinks[i].className = tablinks[i].className.replace(" active", "");
  }

  // Show the current tab, and add an "active" class to the button that opened the tab
  document.getElementById(pType).style.display = "block";

  const cardNumber = document.getElementById('cardNumber');
  const cardholderName = document.getElementById('cardholderName');
  const expiryDate = document.getElementById('expiryDate');
  const cvv = document.getElementById('cvv');

  if(pType == "p-online"){
    cardNumber.setAttribute('required', 'required');
    cardholderName.setAttribute('required', 'required');
    expiryDate.setAttribute('required', 'required');
    cvv.setAttribute('required', 'required');
    document.getElementById("nextBtn").disabled=true;
  }
  else if("p-inperson"){
    cardNumber.removeAttribute('required');
    cardholderName.removeAttribute('required');
    expiryDate.removeAttribute('required');
    cvv.removeAttribute('required');
    document.getElementById("nextBtn").disabled=false;
  }

  if (evt) {
    evt.currentTarget.className += " active";
  } else {
    // If evt is null, apply "active" to the default tab (set on page load)
    document.querySelector(".tablinks").className += " active";
  }
}


let selectedClinic = null;

function toggleClinicSelection(button, clinicId) {

  // If another clinic was previously selected, deselect it
  if (selectedClinic) {
    const prevSelectedButton = document.querySelector('.select-btn.selected');
    if (prevSelectedButton) {
      prevSelectedButton.classList.remove('selected');
      prevSelectedButton.innerHTML = 'Select'; // Reset the previous button text
    }
  }

  // Select the new clinic and apply the 'selected' class to the button
  button.classList.add('selected');
  button.innerHTML = 'Selected <i class="ph ph-check-circle"></i>'; // Add the check icon
  selectedClinic = clinicId;
  document.getElementById('nextBtn').disabled=false;
  
  clinicId.checked = true;
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
document.getElementById('cardNumber').addEventListener('input', validateCardDetails);
document.getElementById('expiryDate').addEventListener('input', validateCardDetails);
document.getElementById('cvv').addEventListener('input', validateCardDetails);

// Validation function for card details
function validateCardDetails() {
    const cardNumber = document.getElementById('cardNumber').value.trim();
    const expiryDate = document.getElementById('expiryDate').value.trim();
    const cvv = document.getElementById('cvv').value.trim();
    
    // Validate the card number: must be exactly 16 digits
    const cardNumberValid = /^\d{4}\s\d{4}\s\d{4}\s\d{4}$/.test(cardNumber);
    
    // Validate the expiry date: MM/YY format and not expired
    const expiryDateValid = /^(0[1-9]|1[0-2])\/\d{2}$/.test(expiryDate) && validateExpiryDate(expiryDate);
    
    // Validate the CVV: must be 3 or 4 digits
    const cvvValid = /^\d{3,4}$/.test(cvv);

    // Enable the Submit button if all are valid
    const isFormValid = cardNumberValid && expiryDateValid && cvvValid;
    document.getElementById('nextBtn').disabled = !isFormValid;

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

function showToast() {
  const toastLiveExample = document.getElementById('liveToast');
  const toastBootstrap = bootstrap.Toast.getOrCreateInstance(toastLiveExample);
  toastBootstrap.show();
}



// Listen and update Pet type based on selected pet
// pet id is for the form later on, is needed to verify whether the pet exists
let petId = null;
document.addEventListener('DOMContentLoaded', function () {
  const petSelect = document.getElementById('petName');
  const petTypeSelect = document.getElementById('petType');
  const petBreedSelect = document.getElementById('petBreed');
  
  // Add event listener to the petSelect input
  petSelect.addEventListener('input', function () {
      const selectedPetId = petSelect.value;

      // Get the corresponding option from the datalist
      const selectedOption = document.querySelector(`#petList option[value="${selectedPetId}"]`);
      

      if (selectedOption) {
          // Extract the pet type/breed from the data attribute
          const petType = selectedOption.getAttribute('data-pet-type');
          const petBreed = selectedOption.getAttribute('data-pet-breed');

          // extrat petid for form
          petId = selectedOption.getAttribute('data-pet-id');

          // Update the petType and petBreed select to the chosen pet type/breed
          petTypeSelect.value = petType;
          petTypeSelect.setAttribute('disabled','disabled');

          petBreedSelect.value = petBreed;
          petBreedSelect.setAttribute('disabled','disabled');
      } else {
          // If no valid option is selected, reset the petType select
          petTypeSelect.removeAttribute('disabled');
          petBreedSelect.removeAttribute('disabled');
      }
  });
});


document.addEventListener("DOMContentLoaded", function () {
  const petForm = document.getElementById("Form2");
  const formElements = petForm.querySelectorAll("input, select");

  formElements.forEach(element => {
      element.addEventListener('input', function() {
          checkInputs(); // Validate on input change
      });
      element.addEventListener('change', function() {
          checkInputs(); // Validate on change
      });
  });
});

// Update checkInputs to include console logs
function checkInputs() {
    const petForm = document.getElementById("Form2");
    const formElements = petForm.querySelectorAll("input, select");

    let allFilled = true;

    formElements.forEach(el => {
        // Log the element's value for debugging

        if (el.tagName === 'INPUT' && el.value.trim() === '') {
            allFilled = false;
        } else if (el.tagName === 'SELECT' && el.value === '') {
            allFilled = false;
        }
    });

    // Update button state
    document.getElementById('nextBtn').disabled = !allFilled;

}

// Update checkInputs to include console logs
function checkInputs() {
    const petForm = document.getElementById("Form2");
    const formElements = petForm.querySelectorAll("input, select");

    let allFilled = true;

    formElements.forEach(el => {
        // Log the element's value for debugging


        if (el.tagName === 'INPUT' && el.value.trim() === '') {
            allFilled = false;
        } else if (el.tagName === 'SELECT' && el.value === '') {
            allFilled = false;
        }
    });

    // Update button state
    document.getElementById('nextBtn').disabled = !allFilled;

}