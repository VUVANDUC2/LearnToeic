document.addEventListener("DOMContentLoaded", () => {
  const TEST_ID = GLOBAL_TEST_ID;
  const api_base_url = "http://localhost:8080/api/admin";
  const testENDPOINT = "/questions/test";
  const questionViewerBody = document.getElementById("question-viewer");

  async function fetchTestData() {
    try {
      const url = `${api_base_url}${testENDPOINT}/${TEST_ID}`;
      console.log("Fetching from URL:", url);
      const response = await fetch(url);

      if (!response.ok) {
        throw new Error(`Server returned ${response.status}`);
      }

      const testData = await response.json();
      console.log("Fetched Data:", testData);
      displayQuestions(testData);
    } catch (err) {
      console.error("Fetch failed:", err);
    }
  }

  function displayQuestions(testData) {
    if (!questionViewerBody) return;

    questionViewerBody.innerHTML = "";

    const questionHTML = testData
      .map((question, index) => {
        // The first question should be visible, the rest hidden
        const hiddenClass = index === 0 ? "" : "d-none";

        return `
        <div class="question-block ${hiddenClass}" data-question-id="${
          question.questionNumber
        }" data-part="${question.part}">
          <h2 class="mb-4 text-dark pt-3">
            <span class="badge bg-danger me-2">Part ${question.part}</span>
          </h2>
          <h4 class="text-muted mb-4 small">Question ${
            question.questionNumber
          }</h4>
          <div class="card shadow-lg mb-5 border-0">
            <div class="card-header bg-white h5 py-3 border-bottom">
              Do as following.
            </div>
            <div class="card-body">
              <div class="mb-3 text-center border p-3 bg-light">
                [Placeholder: Image or content]
              </div>
              <ul class="list-group list-group-flush mb-4">
                <li class="list-group-item correct-option">A) ${
                  question.optionA || ""
                }</li>
                <li class="list-group-item">B) ${question.optionB || ""}</li>
                <li class="list-group-item">C) ${question.optionC || ""}</li>
                <li class="list-group-item">D) ${question.optionD || ""}</li>
              </ul>
              <div class="alert alert-success small mt-3" role="alert">
                <h6 class="alert-heading small fw-bold text-success">
                  <i class="fas fa-lightbulb me-1"></i> Correct Answer & Explanation
                </h6>
                <p class="mb-0 small">
                  The correct answer is <strong>${
                    question.correctOption
                  }</strong><br>
                  Explanation: ...
                </p>
              </div>
            </div>
          </div>
        </div>
      `;
      })
      .join("");

    questionViewerBody.innerHTML = questionHTML;
  }

  fetchTestData();
});
