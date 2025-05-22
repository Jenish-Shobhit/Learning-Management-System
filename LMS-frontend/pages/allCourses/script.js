const toggleBtn = document.querySelector(".toggle-sidebar");
      const sidebar = document.querySelector(".sidebar");
      const mainContent = document.querySelector(".main-content");

      toggleBtn.addEventListener("click", () => {
        sidebar.classList.toggle("collapsed");
        mainContent.classList.toggle("expanded");
      });

      $(document).on("click", ".enroll-btn", function (e) {
        e.preventDefault();

        const button = $(this);
        const courseCard = button.closest(".course-card");
        const courseId = courseCard.data("course-id");
        const userId = localStorage.getItem("userId"); // Replace with actual logged-in user ID in a real app

        // Prepare the enrollment data
        const enrollmentData = {
          enrollmentDate: new Date().toISOString().split("T")[0], // Today's date in YYYY-MM-DD
          progress: 0,
          courseId: courseId,
          userId: userId,
        };

        $.ajax({
          url: "http://localhost:2025/api/enrollments",
          type: "POST",
          contentType: "application/json",
          data: JSON.stringify(enrollmentData),
          success: function (response) {
            button
              .text("Enrolled")
              .removeClass("btn-primary")
              .addClass("btn-outline-primary")
              .prop("disabled", true);
            alert("Enrolled successfully!");
          },
          error: function (xhr, status, error) {
            alert("Failed to enroll. Please try again.");
          },
        });
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
      $(document).ready(function () {
        const userId = localStorage.getItem("userId"); // Replace with actual logged-in user ID

        $.ajax({
          url: `http://localhost:2025/api/enrollments/user/${userId}`,
          type: "GET",
          dataType: "json",
          success: function (enrollResponse) {
            // Get all courseIds the user is enrolled in
            const enrolledCourseIds = (enrollResponse.details || []).map(
              (enrollment) => enrollment.course.courseId
            );

            $.ajax({
              url: "http://localhost:2025/api/courses/allCourses",
              type: "GET",
              dataType: "json",
              success: function (response) {
                const courses = response.details || [];
                const container = $("#coursesContainer");
                container.empty();

                if (courses.length === 0) {
                  container.append(
                    '<div class="col-12"><p>No courses found.</p></div>'
                  );
                  return;
                }

                courses.forEach((course) => {
                  const isEnrolled = enrolledCourseIds.includes(
                    course.courseId
                  );
                  const buttonHtml = isEnrolled
                    ? `<button class="btn btn-outline-primary enroll-btn" disabled>Enrolled</button>`
                    : `<button class="btn btn-primary enroll-btn">Enroll</button>`;

                  const card = `
              <div class="col-md-4 mb-4">
                <div class="course-card" data-course-id="${course.courseId}">
                  <h3 class="course-title">${course.title}</h3>
                  <div class="course-info">
                    <span><i></i>${course.description}</span>
                    
                  </div>
                  <div class="course-actions">
                    ${buttonHtml}
                  </div>
                </div>
              </div>
            `;
                  container.append(card);
                });
              },
              error: function () {
                $("#coursesContainer").html(
                  '<div class="col-12"><p class="text-danger">Failed to load courses.</p></div>'
                );
              },
            });
          },
          error: function () {
            $("#coursesContainer").html(
              '<div class="col-12"><p class="text-danger">Failed to load enrollments.</p></div>'
            );
          },
        });
      });