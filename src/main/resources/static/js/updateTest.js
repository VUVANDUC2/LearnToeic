document.addEventListener("DOMContentLoaded", function () {
  const deleteButtons = document.querySelectorAll(".delete-test-btn");

  // Read CSRF token + header name from meta tags
  const csrfToken = document
    .querySelector('meta[name="_csrf"]')
    .getAttribute("content");
  const csrfHeader = document
    .querySelector('meta[name="_csrf_header"]')
    .getAttribute("content");

  deleteButtons.forEach((button) => {
    button.addEventListener("click", function (event) {
      event.preventDefault();

      const testId = this.getAttribute("data-test-id");

      if (
        confirm(
          `Are you sure you want to change the status for Test ID: ${testId}?`
        )
      ) {
        fetch(`/api/admin/tests/${testId}/status`, {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            [csrfHeader]: csrfToken, // <-- add CSRF header dynamically
          },
        })
          .then((response) => {
            if (response.ok) {
              alert("Test status updated successfully!");
              window.location.reload();
            } else {
              alert("Failed to update test status.");
            }
          })
          .catch((error) => {
            console.error("Error:", error);
            alert("An error occurred during the update.");
          });
      }
    });
  });
});
