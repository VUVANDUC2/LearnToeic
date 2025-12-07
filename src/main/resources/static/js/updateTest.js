document.addEventListener('DOMContentLoaded', function() {
    const deleteButtons = document.querySelectorAll('.delete-test-btn'); 

    deleteButtons.forEach(button => {
        button.addEventListener('click', function(event) {
            event.preventDefault(); // Stop the default link action
            
            // Get the test ID from the data attribute
            const testId = this.getAttribute('data-test-id');
            
            if (confirm(`Are you sure you want to change the status for Test ID: ${testId}?`)) {
                // Perform the AJAX call
                fetch(`/api/admin/tests/${testId}/status`, { // 💡 The endpoint URL
                    method: 'PUT', // or PATCH, for updating a resource
                    headers: {
                        'Content-Type': 'application/json',
                        // Include CSRF token if enabled (essential for POST/PUT/DELETE)
                        // 'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').content
                    }
                })
                .then(response => {
                    if (response.ok) {
                        alert('Test status updated successfully!');
                        // Reload the page or update the specific row in the table
                        window.location.reload(); 
                    } else {
                        alert('Failed to update test status.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('An error occurred during the update.');
                });
            }
        });
    });
});