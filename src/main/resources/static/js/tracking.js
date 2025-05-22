// Progress Bar
const circles = document.querySelectorAll(".track-circle"),
progressBar = document.querySelector(".track-indicator");

let currentStep = 1;

function setCurrentStep(){
    console.log("hahefoisfjojsd")
    let currentStep = 2

    // Update circles based on the current step
    circles.forEach((circle, index) => {
        circle.classList[`${index < currentStep ? "add" : "remove"}`]("active");
      });
    
      // Update the progress bar width based on the current step
      progressBar.style.width = `${
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
}

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
    progressBar.style.width = `${
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