document.addEventListener('DOMContentLoaded', function () {
    // Get filter buttons
    const filterButtons = document.querySelectorAll('button[data-filter]');
    const resourceTypeInput = document.getElementById('resourceType');
    const tagInput = document.getElementById('tag');

    // Function to reset active class for a group of buttons
    function resetActiveButtons(filterType) {
        filterButtons.forEach(function (button) {
            if (button.getAttribute('data-filter') === filterType) {
                button.classList.remove('selected');  // Remove the "selected" class from all buttons of the same group
            }
        });
    }

    // Add event listener to each button
    filterButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            const filterType = button.getAttribute('data-filter');
            const filterValue = button.value;

            // Set the value of the hidden input field based on the clicked button
            if (filterType === 'resourceType') {
                resourceTypeInput.value = filterValue;
                resetActiveButtons('resourceType'); // Reset other resourceType buttons
            } 
            else if (filterType === 'tag') {
                tagInput.value = filterValue;
                resetActiveButtons('tag'); // Reset other tag buttons
            }

            // Mark the clicked button as selected
            button.classList.add('selected');
        });
    });
});
