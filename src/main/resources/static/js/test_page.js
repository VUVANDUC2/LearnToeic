// In your test_page.js file

// 1. Reference the global variable defined in the HTML
const TEST_ID = GLOBAL_TEST_ID;

const api_base_url = "http://localhost:8080/api/admin";
const testENDPOINT = "/tests";

async function fetchTestData() {
  try {
    // Construct the URL using template literals
    const url = `${api_base_url}${testENDPOINT}/${TEST_ID}`;

    const response = await fetch(url);
    alert(`Attempting fetch for ID: ${TEST_ID}`); // Use backticks for proper logging

    if (!response.ok) {
      // 2. FIX: Use BACKTICKS (`) for string interpolation, not single quotes (')
      throw new Error(
        `Server returned error code ${response.status}. Check again`
      );
    }

    const testData = await response.json(); // Renamed 'tests' to 'testData' for clarity

    // Process your fetched testData here
    console.log("Fetched Data:", testData);
  } catch (error) {
    console.error("Fetch failed:", error);
  }
}

// Start the fetch process
fetchTestData();
