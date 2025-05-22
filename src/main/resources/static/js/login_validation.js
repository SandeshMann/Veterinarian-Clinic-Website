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

// Fucntion to validate all the required fields.
function validateForm(event) {
    event.preventDefault(); // Prevent the form from submitting immediately
    let isValid = true;
    
    // Get form values (email, password)
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
   

    console.log("email:", email);
    console.log("password:", password);

    // Email validation
    if (email == "") {
        document.getElementById('email-error').innerText = 'Email address is required.';
        isValid = false;
    }
    else if (!isEmailValid(email)) {
        document.getElementById('email-error').innerText = 'Invalid email format.';
        isValid = false;
    } else {
        document.getElementById('email-error').innerText = '';
    }

    // Password validation
    if (password == "") {
        document.getElementById('password-error').innerText = 'Password field is required.';
        isValid = false;
    }
    else if (!isPasswordValid(password)) {
        document.getElementById('password-error').innerText = 'Password must be 8-15 characters and contain letters, numbers, or special characters';
        isValid = false;
    } else {
        document.getElementById('password-error').innerText = '';
    }

    // If form is valid, you can proceed with form submission
    if (isValid) {
        document.getElementById('login-form').submit();
    }
}