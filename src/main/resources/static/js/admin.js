document.addEventListener("DOMContentLoaded", () => {
  fetch("/api/admin/tests")
    .then((response) => response.json())
    .then((data) => {
      console.log("API data:", data);
      const container = document.getElementById("testsContainer");

      data.forEach((test) => {
        const card = `
          <div class="col-12 col-md-6 col-lg-4">
            <article class="course-card h-100">
              <img src="https://images.unsplash.com/photo-1523580846011-d3a5bc25702b?q=80&w=1200&auto=format&fit=crop" 
                   alt="${test.testName}" />
              <div class="p-3">
                <h5 class="fw-bold">${test.testName}</h5>
                <p class="small text-secondary">${
                  test.description || "Không có mô tả"
                }</p>
                <div class="d-flex align-items-center justify-content-between">
                  <span class="badge text-bg-light">${
                    test.totalQuestions
                  } câu hỏi</span>
                  <a class="btn btn-sm btn-danger" href="/admin/test_page?testId=${
                    test.testId
                  }">Xem chi tiết</a>
                </div>
              </div>
            </article>
          </div>
        `;
        container.innerHTML += card;
      });
    })
    .catch((err) => console.error("Fetch error:", err));
});
