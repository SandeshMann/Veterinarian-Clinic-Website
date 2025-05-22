// File used for handling backend and form inputs


// Used to test if the form is working, and console logging the input of the user
function consoleLogForm(event, userId) {
  // Prevent the default form submission
  event.preventDefault();

  // Get form values
  const form = event.target;
  const email = userId; // Static value for example
  const clinicId = parseInt(form.clinic.value);
  const appointmentDateTime = form.appointmentDate.value + "T" + findChecked() + ":00";
  petId = parseInt(petId);
  const petName = form.petName.value;
  const petType = form.petType.value;
  const consultationType = form.consultationType.value;
  const petBreed = form.petBreed.value;; // Static value for example

  const errors = [];

  if(petId == null){
    petId = -1;
  }

  // close when form is submitted
  const modalElement = document.getElementById("myModal");
  const modalInstance = bootstrap.Modal.getInstance(modalElement); // Get modal instance
  modalInstance.hide();


  // Create an object with the form data
  const formData = {
    email: email,
    clinicId: clinicId,
    appointmentDateTime: appointmentDateTime,
    petId: petId,
    petName: petName,
    petType: petType,
    petBreed: petBreed,
    consultationType: consultationType,
  };

  // Log the form data object to the console
  console.log(formData);
  console.log(form['time-slot'].value);

  
  resetForm();

  // INSERT BOOKING CONFIRMATION HERE FOR CONSOLE LOG FORM
}

function findChecked(){
  const radios = document.getElementsByName('time-slot');
  
  // Initialize variable to store the selected value
  let selectedValue = '';

  // Loop through radio buttons to find the checked one
  for (const radio of radios) {
      if (radio.checked) {
          selectedValue = radio.value;
          break; // Exit the loop once the checked radio is found
      }
      console.log(radio.checked);
  }

  // Output the selected value
  return selectedValue;
}



function submitForm(event, id) {
  // Prevent the default form submission
  event.preventDefault();

  // Get form values
  const form = event.target;
  const userId= id;
  const clinicId = parseInt(form.clinic.value);
  const appointmentDateTime = form.appointmentDate.value + "T" + findChecked() + ":00";
  petId = petId;
  const petName = form.petName.value;
  const petType = form.petType.value;
  const consultationType = form.consultationType.value;
  const petBreed = form.petBreed.value;; // Static value for example


  if(petId == null){
    petId = -1;
  }

  const errors = [];

  // close when form is submitted
  const modalElement = document.getElementById("myModal");
  const modalInstance = bootstrap.Modal.getInstance(modalElement); // Get modal instance
  modalInstance.hide();


  // Create an object with the form data
  const formData = {
    userId: userId,
    clinicId: clinicId,
    appointmentDateTime: appointmentDateTime,
    petId: petId,
    petName: petName,
    petType: petType,
    petBreed: petBreed,
    consultationType: consultationType,
  };

  fetch("appointments/create", {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
    },
    body: new URLSearchParams(formData),
  })
    .then((response) => response.json())
    .then((data) => {
      console.log("Success:", data);
      alert("Appointment created successfully!");
      // Log the form data object to the console
      console.log("Form submitted to backend: " + formData);

      // Clear the forms if the user decides to book another appointment
      resetForm();

      // INSERT BOOKING CONFIRMATION HERE FOR CONSOLE LOG FORM
      window.location.reload();
      
    })
    .catch((error) => {
      console.error("Error:", error);
      alert("Failed to create appointment.");
    });

}


function resetForm(){
  // reset form - this only resets some input (not time slots or clinics)
  const form = document.getElementById("bookingForm");
  form.reset();

  const prevSelectedTime = document.querySelector('.select-time.selected');
  prevSelectedTime.classList.remove('selected');


  const prevSelectedClinic = document.querySelector('.select-btn.selected');
  prevSelectedClinic.classList.remove('selected');
  prevSelectedClinic.innerHTML = 'Select';
  selectedClinic = null;

}




function findChecked(){
  const radios = document.getElementsByName('time-slot');
  
  // Initialize variable to store the selected value
  let selectedValue = '';

  // Loop through radio buttons to find the checked one
  for (const radio of radios) {
      if (radio.checked) {
          selectedValue = radio.value;
          break; // Exit the loop once the checked radio is found
      }
  }

  // Output the selected value
  return selectedValue;
}