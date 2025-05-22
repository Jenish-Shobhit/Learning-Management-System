document.querySelectorAll(".custom-checkbox").forEach((checkbox, idx, arr) => {
  checkbox.addEventListener("change", function () {
    if (this.checked && idx > 0) {
      if (!arr[idx - 1].checked) {
        this.checked = false;
        alert("Please complete the previous step first.");
        return;
      }
    }
    if (!this.checked) {
      for (let i = idx + 1; i < arr.length; i++) {
        arr[i].checked = false;
        const parent = arr[i].closest(
          ".video-content, .quiz-section, .content-item"
        );
        const completedText = parent && parent.querySelector(".completed-text");
        if (completedText) completedText.style.display = "none";
      }
    }

    const parent = this.closest(".video-content, .quiz-section, .content-item");
    const completedText = parent && parent.querySelector(".completed-text");
    if (this.checked) {
      if (completedText) completedText.style.display = "inline";
    } else {
      if (completedText) completedText.style.display = "none";
    }

    const totalCheckboxes =
      document.querySelectorAll(".custom-checkbox").length;
    const checkedCheckboxes = document.querySelectorAll(
      ".custom-checkbox:checked"
    ).length;
    const progress = Math.round((checkedCheckboxes / totalCheckboxes) * 100);

    const userId = localStorage.getItem("userId");
    const courseId = localStorage.getItem("selectedCourseId");

    if (userId && courseId) {
      $.ajax({
        url: `http://localhost:2025/api/enrollments/progress?userId=${userId}&courseId=${courseId}&progress=${progress}`,
        type: "PUT",
        success: function () {
          console.log("Progress updated:", progress + "%");
        },
        error: function () {
          console.error("Failed to update progress.");
        },
      });
    }
  });
});

$(document).ready(function () {
  const courseId = localStorage.getItem("selectedCourseId");
  console.log("Selected Course ID:", courseId); // Debugging line
  if (!courseId) {
    $(".main-content").html(
      '<p class="text-danger">Invalid course selection.</p>'
    );
    return;
  }

  $.ajax({
    url: `http://localhost:2025/api/courses/${courseId}`,
    type: "GET",
    dataType: "json",
    success: function (data) {
      const course = data.details;
      console.log("Course Data:", course); // Debugging line
      $(".page-course-title").html(
        `<i class="fas fa-laptop-code mr-2" style="color:#4a90e2;"></i> ${course.title}`
      );
      $("#description").text(course.description);
      $("#prerequisites").html(course.prerequisites);
      $(".video-container iframe").attr("src", course.videoUrl || "");
      const quizIds = course.quizIds || [];
      localStorage.setItem("quizIds", JSON.stringify(quizIds));
    },
    error: function (xhr) {
      if (xhr.status === 404) {
        window.location.href = "../error/index.html";
      } else {
        $(".main-content").html(
          '<p class="text-danger">Failed to load course details.</p>'
        );
      }
    },
  });

  const userId = localStorage.getItem("userId");
  if (userId && courseId) {
    $.ajax({
      url: `http://localhost:2025/api/enrollments/progress?userId=${userId}&courseId=${courseId}`,
      type: "GET",
      dataType: "json",
      success: function (data) {
        const progress =
          data.details && data.details.progress ? data.details.progress : 0;
        console.log("Progress Value:", progress); // Debugging line
        const totalCheckboxes = $(".custom-checkbox").length;
        const checkedCount = Math.round((progress / 100) * totalCheckboxes);
        console.log("Progress from server:", progress); // Debugging line
        console.log("Checked Count:", checkedCount); // Debugging line

        $(".custom-checkbox").each(function (i) {
          if (i < checkedCount) {
            $(this).prop("checked", true).trigger("change");
          } else {
            $(this).prop("checked", false).trigger("change");
          }
        });
      },
      error: function () {},
    });
  }
});
