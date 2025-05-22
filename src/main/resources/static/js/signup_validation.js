// All Regular expressions source: https://regexlib.com/

// Function for user email field validation.
function isEmailValid(email) {
    const email_regex = /^\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$/;
    return email_regex.test(email);
}

// Function for user password field validation.
function isPasswordValid(password) {
    const pass_regex = /^([a-zA-Z0-9@*#]{8,15})$/;
    return pass_regex.test(password);
}


// Fucntion to check if password and confirm password fields match
function matchPassword(password, confirm_password) {
    return password === confirm_password;
}

document.addEventListener('DOMContentLoaded', (event) => {
    const form = document.getElementById('signup-form');
    form.addEventListener('submit', function(event) {
        if (!validateForm()) {
            event.preventDefault(); // Prevent form submission if validation fails
        }
    });
});

// Function to validate all the required fields.
function validateForm() {
    let isValid = true;
    
    // Get form values (firstName, lastName, email, password, and confirmPassword)
    const firstName = document.getElementById('firstName').value;
    const lastName = document.getElementById('lastName').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    // firstName input field validation
    if (firstName === "") {
        document.getElementById('firstName-error').innerText = 'First name is required.';
        isValid = false;
    } else {
        document.getElementById('firstName-error').innerText = "";
    }

    // lastName input field validation
    if (lastName === "") {
        document.getElementById('lastName-error').innerText = 'Last name is required.';
        isValid = false;
    } else {
        document.getElementById('lastName-error').innerText = "";
    }

    // Email validation
    if (email === "") {
        document.getElementById('email-error').innerText = 'Email address is required.';
        isValid = false;
    } else if (!isEmailValid(email)) {
        document.getElementById('email-error').innerText = 'Invalid email format.';
        isValid = false;
    } else {
        document.getElementById('email-error').innerText = '';
    }

    // Password validation
    if (password === "") {
        document.getElementById('password-error').innerText = 'Password field is required.';
        isValid = false;
    } else if (!isPasswordValid(password)) {
        document.getElementById('password-error').innerText = 'Password must be 8-15 characters and contain letters, numbers, or special characters';
        isValid = false;
    } else {
        document.getElementById('password-error').innerText = '';
    }

    // Confirm password validation
    if (confirmPassword === "") {
        document.getElementById('confirmPassword-error').innerText = 'Please confirm your password.';
        isValid = false;
    } else if (!matchPassword(password, confirmPassword)) {
        document.getElementById('confirmPassword-error').innerText = 'Passwords do not match';
        isValid = false;
    } else {
        document.getElementById('confirmPassword-error').innerText = '';
    }

    return isValid;
}

