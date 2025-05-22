const toggleBtn = document.querySelector(".toggle-sidebar");
const sidebar = document.querySelector(".sidebar");
const mainContent = document.querySelector(".main-content");

toggleBtn.addEventListener("click", () => {
  sidebar.classList.toggle("collapsed");
  mainContent.classList.toggle("expanded");
});


const searchInput = document.getElementById("courseSearch");
searchInput.addEventListener("input", (e) => {
  const searchTerm = e.target.value.toLowerCase();
  const courseCards = document.querySelectorAll(".course-card");

  courseCards.forEach((card) => {
    const title = card
      .querySelector(".course-title")
      .textContent.toLowerCase();
    if (title.includes(searchTerm)) {
      card.closest(".col-md-4").style.display = "block";
    } else {
      card.closest(".col-md-4").style.display = "none";
    }
  });
});

$(document).on("click", ".btn-outline-primary", function () {
  const courseId = $(this).closest(".course-card").data("course-id");
  console.log("Storing courseId:", courseId);
  localStorage.setItem("selectedCourseId", courseId);
});

$(document).ready(function () {
  const userId = localStorage.getItem("userId"); 
  $.ajax({
    url: `http://localhost:2025/api/enrollments/user/${userId}`,
    type: "GET",
    dataType: "json",
    success: function (enrollResponse) {
      const enrollments = enrollResponse.details || [];
      const container = $(".row");
      container.empty();

      if (enrollments.length === 0) {
        container.append(
          '<div class="col-12"><p>No enrolled courses found.</p></div>'
        );
        return;
      }

      enrollments.forEach((enrollment) => {
        console.log("Enrollment: ", enrollment);
        const course = enrollment.course;
        console.log("Course id: ", course.courseId);
        const progress = enrollment.progress || 0;
        const card = `
              <div class="col-md-4 mb-4">
                  <div class="course-card" data-course-id="${
                    course.courseId
                  }">
                      <h3 class="course-title">${course.title}</h3>
                      <div class="course-info">
                         <span><i></i>${course.description}</span>
                      </div>
                      <div class="progress-section">
                          <div class="progress-label">
                              <span>Progress</span>
                              <span>${progress}%</span>
                          </div>
                          <div class="progress">
                              <div class="progress-bar" style="width: ${progress}%"></div>
                          </div>
                      </div>
                      <div class="course-actions">
                          <a href="../courses.html" class="btn btn-outline-primary" target="_blank">Explore Course</a>
                      </div>
                  </div>
              </div>
          `;
        container.append(card);
      });
    },
    error: function () {
      $(".row").html(
        '<div class="col-12"><p class="text-danger">Failed to load enrolled courses.</p></div>'
      );
    },
  });
});