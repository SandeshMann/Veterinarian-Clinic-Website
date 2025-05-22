document.getElementById("editButton").addEventListener("click", function(event) {
  event.preventDefault(); // Prevents the form from submitting when "Edit" is clicked

  const nameInput = document.getElementById("nameInput");
  const passwordInput = document.getElementById("passwordInput");
  const editButton = document.getElementById("editButton");
  const confirmButton = document.getElementById("confirmButton");
  const cancelButton = document.getElementById("cancelButton");

  // Enable form inputs
  nameInput.disabled = false;
  passwordInput.disabled = false;

  // Hide the Edit button and show the Confirm button
  editButton.style.display = "none";
  confirmButton.style.display = "inline-block";
  cancelButton.style.display = "inline-block";
});

// Retrieve the saved active tab from local storage
const savedTab = localStorage.getItem('activeTab');

// Function to activate a tab and its corresponding content
function activateTab(tabId) {
    // Deactivate all tabs and hide all content
    document.querySelectorAll('.nav-link').forEach(button => {
        button.classList.remove('active');
    });

    document.querySelectorAll('.tab-pane').forEach(tabPane => {
        tabPane.classList.remove('show', 'active');
    });

    // Activate the selected tab
    const activeTab = document.querySelector(`button[data-tab="${tabId}"]`);
    if (activeTab) {
        activeTab.classList.add('active');
        const contentPane = document.querySelector(`#${tabId}`);
        if (contentPane) {
            contentPane.classList.add('show', 'active');
        }
    }
}

// If there is a saved tab, activate it
if (savedTab) {
    activateTab(savedTab);
} else {
    // Default to profile tab if no saved tab
    activateTab('pills-profile');
}

// Add event listeners to each tab button to save the active tab in local storage
const tabButtons = document.querySelectorAll('.nav-link');
tabButtons.forEach(button => {
    button.addEventListener('click', () => {
        const tabId = button.getAttribute('data-tab');
        // Save the currently active tab in local storage
        localStorage.setItem('activeTab', tabId);
        // Activate the clicked tab
        activateTab(tabId);
    });
});