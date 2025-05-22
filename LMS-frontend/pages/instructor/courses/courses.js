let editingCard = null;

      // Sidebar toggle
      $(".toggle-sidebar").on("click", function () {
        $(".sidebar").toggleClass("collapsed");
        $(".main-content").toggleClass("expanded");
      });

      // Mobile sidebar toggle
      $(".navbar-toggler").on("click", function () {
        $(".sidebar").toggleClass("active");
      });

      // Close sidebar when clicking outside on mobile
      $(document).on("click", function (e) {
        if (
          !$(e.target).closest(".sidebar").length &&
          !$(e.target).closest(".navbar-toggler").length
        ) {
          $(".sidebar").removeClass("active");
        }
      });

      $("#courseSearch").on("input", function () {
        const searchTerm = $(this).val().trim().toLowerCase();

        if (searchTerm.length === 0) {
          $(".course-card").show(); // Show all courses if search is empty
          return;
        }

        $.ajax({
          url: `http://localhost:2025/api/courses/${encodeURIComponent(
            searchTerm
          )}`,
          type: "GET",
          dataType: "json",
          success: function (response) {
            console.log("Course search results:", response);

            const courseListContainer = $(".row"); // Adjust selector based on your course list container
            courseListContainer.empty(); // Clear previous results

            if (!response.details) {
              courseListContainer.append(
                "<p class='no-courses-message'>No matching courses found.</p>"
              );
              return;
            }

            const course = response.details; // Since backend wraps data inside `StandardResponse`
            const courseCardHtml = `
                      <div class="col-md-4 mb-4">
                          <div class="course-card" data-course-id="${course.courseId}">
                              <h3 class="course-title">${course.title}</h3>
                              <div class="course-info">
                                  <span><i class="fas fa-user"></i> Instructor Name</span>
                                  <span><i class="fas fa-clock"></i> Duration</span>
                              </div>
                              <div class="course-actions">
                                  <a href="#" class="btn btn-primary edit-course-btn">Edit</a>
                                  <a href="#" class="btn btn-outline-primary delete-course-btn">Delete</a>
                              </div>
                          </div>
                      </div>
                  `;
            courseListContainer.append(courseCardHtml);
          },
          error: function (xhr, status, error) {
            console.error("Error searching course:", error);
            $(".row").html(
              "<p class='no-courses-message'>No matching courses found.</p>"
            );
          },
        });
      });
      $("#addCourseModal").on("show.bs.modal", function () {
        // Fetch quizzes from the API
        $.ajax({
          url: "http://localhost:2025/api/quizes",
          type: "GET",
          success: function (response) {
            const quizSelect = $("#courseQuiz");
            quizSelect.empty(); // Clear existing options

            // Add default option
            quizSelect.append('<option value="">Select a quiz</option>');

            // Add quizzes from API response
            response.details.forEach((quiz) => {
              quizSelect.append(
                `<option value="${quiz.quizId}">${quiz.quizName} (Marks: ${quiz.marks}, Pass Score: ${quiz.minPassScore})</option>`
              );
            });
          },
          error: function (xhr, status, error) {
            console.error("Error loading quizzes:", error);
            alert("Failed to load quizzes. Please try again.");
          },
        });
      });

      // Add or update course form submission
      $("#addCourseForm").on("submit", function (e) {
        e.preventDefault();

        const courseName = $("#courseName").val();
        const courseDescription = $("#courseDescription").val();
        const coursePrerequisites = $("#coursePrerequisites").val();
        const courseVideo = $("#courseVideo").val();
        const courseQuiz = $("#courseQuiz").val();

        $("#addCourseModal").modal("hide");
        this.reset();

        const courseData = {
          title: courseName,
          description: courseDescription,
          prerequisites: coursePrerequisites,
          videoUrl: courseVideo,
          quizIds: courseQuiz ? courseQuiz : null, // Ensure it's an array
        };
        const apiUrl = "http://localhost:2025/api/courses/add"; // Your Spring Boot API endpoint for adding courses

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "http://localhost:2025/api/courses/add", true);
        xhr.setRequestHeader("Content-Type", "application/json");

        xhr.onreadystatechange = function () {
          if (xhr.readyState === 4) {
            if (xhr.status === 201) {
              alert("Course added Successfully!");
            } else if (xhr.status === 400) {
              alert("Signup failed: Invalid input. Please check your details.");
            } else if (xhr.status === 409) {
              alert("Signup failed: Email already exists.");
            } else {
              alert("Course creation failed to server error.");
            }
          }
        };

        xhr.send(JSON.stringify(courseData));
      });

      // Reset modal on close
      $("#addCourseModal").on("hidden.bs.modal", function () {
        editingCard = null;
        $("#addCourseModalLabel").text("Add New Course");
        $(".modal-footer .btn-primary[type=submit]").text("Save Course");
        $("#addCourseForm")[0].reset();
      });

      // Delete course functionality
      $(document).on("click", ".delete-course-btn", function (e) {
        e.preventDefault();
        const courseId = $(this).closest(".course-card").data("course-id");
        console.log("Course ID to delete:", courseId);
        const apiUrl = `http://localhost:2025/api/courses/${encodeURIComponent(
          courseId
        )}`;
        if (confirm("Are you sure you want to delete this course?")) {
          $(this).closest(".col-md-4").remove();
          $.ajax({
            url: apiUrl,
            type: "DELETE",
            success: function (response) {
              console.log("Course deleted successfully:", response);
              alert("Course deleted successfully!");
            },
            error: function (xhr, status, error) {
              console.error("Error deleting course:", error);
              alert("Failed to delete course. Please try again.");
            },
          });
        }
      });

      // Event listener for the "Edit Course" form submission (if you implement editing)
      //edit course details using PUT method
      $("#editCourseForm").on("submit", function (e) {
        e.preventDefault();
        const courseName = $("#editCourseName").val().trim();
        const courseDescription = $("#editCourseDescription").val().trim();
        const coursePrerequisites = $("#editCoursePrerequisites").val().trim();
        const courseVideo = $("#editCourseVideo").val().trim();
        const courseQuiz = $("#editCourseQuiz").val();
        console.log("Course Quiz ID:", courseQuiz);

        const courseData = {
          title: courseName,
          description: courseDescription,
          prerequisites: coursePrerequisites,
          videoUrl: courseVideo,
          quizIds: courseQuiz,
        };
        const courseId = $("#editCourseForm").data("course-id");
        console.log("Course ID to update:", courseId);
        const apiUrl = `http://localhost:2025/api/courses/${courseId}`; 

        $.ajax({
          url: apiUrl,
          type: "PUT",
          contentType: "application/json",
          data: JSON.stringify(courseData),
          success: function (response) {
            console.log("Course updated successfully:", response);
            alert("Course updated successfully!");
            $("#editCourseModal").modal("hide");
            $("#editCourseForm")[0].reset();
          },
          error: function (xhr, status, error) {
            console.error("Error updating course:", error);
            alert("Failed to update course. Please try again.");
          },
        });
      });

      $(document).on("click", ".delete-course-btn", function () {});

      $(document).on("click", ".edit-course-btn", function () {
        const courseId = $(this).closest(".course-card").data("course-id");
        const apiUrl = `http://localhost:2025/api/courses/${courseId}`;

        $.ajax({
          url: apiUrl,
          type: "GET",
          dataType: "json",
          success: function (data) {
            const course = data.details;
            $("#editCourseName").val(course.title);
            $("#editCourseDescription").val(course.description);
            $("#editCoursePrerequisites").val(course.prerequisites);
            $("#editCourseVideo").val(course.videoUrl ? course.videoUrl : "");
            $("#editCourseForm").data("course-id", courseId);

            $.ajax({
              url: "http://localhost:2025/api/quizes",
              type: "GET",
              success: function (response) {
                const quizSelect = $("#editCourseQuiz");
                quizSelect.empty();
                quizSelect.append('<option value="">Select a quiz</option>');
                response.details.forEach((quiz) => {
                  quizSelect.append(
                    `<option value="${quiz.quizId}">${quiz.quizName} (Marks: ${quiz.marks}, Pass Score: ${quiz.minPassScore})</option>`
                  );
                });
                quizSelect.val(course.quizId || "");
              },
              error: function () {
                alert("Failed to load quizzes for editing.");
              },
            });

            $("#editCourseModal").modal("show");
          },
          error: function () {
            alert("Failed to fetch course details for editing.");
          },
        });
      });

      function loadCourses() {
        $.ajax({
          url: "http://localhost:2025/api/courses/allCourses",
          type: "GET",
          dataType: "json",
          success: function (data) {
            console.log("Initial course data:", data);
            const courses = data.details;

            const courseListContainer = $(".row"); 
            courseListContainer.empty();

            $.each(courses, function (index, course) {
              const courseCardHtml = `
                          <div class="col-md-4 mb-4">
                              <div class="course-card" data-course-id="${course.courseId}">
                                  <h3 class="course-title" id="courseNameInput">${course.title}</h3>
                                  <div class="course-info">
                                      <span><i></i>${course.description}</span>
                                  </div>
                                  <div class="course-actions">
                                      <a href="#" class="btn btn-primary edit-course-btn">Edit</a>
                                      <a href="#" class="btn btn-outline-primary delete-course-btn">Delete</a>
                                  </div>
                              </div>
                          </div>
                      `;
              courseListContainer.append(courseCardHtml);
            });
          },
          error: function (xhr, status, error) {
            console.error("Error loading courses:", error);
            $(".row").html("<p>Failed to load courses.</p>");
          },
        });
      }

      loadCourses();