const api_base_url = "http://localhost:8080/api/admin";
const allTestsENDPOINT = "/tests";

tableBody = document.getElementById("test-table-body");

function getActionButtons(testId) {
  let buttons = `
        <a href="test_page?id=${testId}" class="btn btn-sm btn-primary" title="Edit Test">
            <i class="bi bi-pencil"></i>
        </a>
    `;
  if (status === "Live") {
    buttons += `
            <a href="results.html?id=${testId}" class="btn btn-sm btn-info" title="View Student Results">
                <i class="bi bi-bar-chart"></i>
            </a>
        `;
  }
  return `<div class="btn-group" role="group">${buttons}</div>`;
}

function displayTests(tests) {
  if (!tableBody) return;

  tableBody.innerHTML = "";
  if (tests.length === 0) {
    tableBody.innerHTML =
      '<tr><td colspan="6" class="text-center py-3 text-info">No tests found.</td></tr>';
    return;
  }
  const rowsHTML = tests
    .map((test) => {
      const formattedId = `T-${String(test.testId).padStart(5, "0")}`;
      const actionButtons = getActionButtons(test.testId);
      return `
            <tr>
                <td><strong>${formattedId}</strong></td>
                <td>${test.testName}</td>
                <td>'N/A'</td>
                <td>${test.totalQuestions || "N/A"}</td>
                <td>${test.testDate || "N/A"}</td>
                <td>${actionButtons}</td>
            </tr>
        `;
    })
    .join("");

  tableBody.innerHTML = rowsHTML;
}

async function fetchAllTests() {
  try {
    const response = await fetch(api_base_url + allTestsENDPOINT);

    if (!response.ok) {
      throw new Error(
        "Server returned error code ${response.status}. Check again"
      );
    }

    const tests = await response.json();
    displayTests(tests);
  } catch (error) {
    console.error("Fetch failed:", error);
  }
}

document.addEventListener("DOMContentLoaded", fetchAllTests);
