$(".toggle-sidebar").on("click", function () {
    $(".sidebar").toggleClass("collapsed");
    $(".main-content").toggleClass("expanded");
  });

  $(".navbar-toggler").on("click", function () {
    $(".sidebar").toggleClass("active");
  });

  $(document).on("click", function (e) {
    if (
      !$(e.target).closest(".sidebar").length &&
      !$(e.target).closest(".navbar-toggler").length
    ) {
      $(".sidebar").removeClass("active");
    }
  });

  function createQuizCard(quiz) {
    return `
      <div class="col-md-4" id="quiz-${quiz.quizId}">
        <div class="quiz-card">
          <h3 class="quiz-title">${quiz.quizName}</h3>
          <div class="quiz-info">
            <span><strong>Marks:</strong> ${quiz.marks}</span>
            <span><strong>Minimum Pass Score:</strong> ${quiz.minPassScore}</span>
          </div>
          <button class="btn btn-primary btn-sm update-questions-btn" data-quizid="${quiz.quizId}">Update</button>
          <button class="btn btn-danger btn-sm delete-quiz-btn" data-quizid="${quiz.quizId}">Delete</button>
        </div>
      </div>
    `;
  }

  function loadQuizzes() {
    $.ajax({
      url: "http://localhost:2025/api/quizes",
      method: "GET",
      success: function (response) {
        if (response && response.details) {
          $("#quizGrid").empty();
          response.details.forEach(function (quiz) {
            $("#quizGrid").append(createQuizCard(quiz));
          });
        }
      },
      error: function () {
        alert("Failed to load quizzes.");
      },
    });
  }

  $("#addQuizForm").on("submit", function (e) {
    e.preventDefault();

    const quizName = $("#quizName").val();
    const marks = parseInt($("#marks").val());
    const minPassScore = parseInt($("#minPassScore").val());

    const quizData = {
      quizName: quizName,
      marks: marks,
      minPassScore: minPassScore,
    };

    $.ajax({
      url: "http://localhost:2025/api/quizes",
      method: "POST",
      contentType: "application/json",
      data: JSON.stringify(quizData),
      success: function (response) {
        if (response && response.details) {
          $("#quizGrid").append(createQuizCard(response.details));
          $("#addQuizModal").modal("hide");
          $("#addQuizForm")[0].reset();
        } else {
          alert("Failed to save quiz.");
        }
      },
      error: function () {
        alert("Error occurred while saving quiz.");
      },
    });
  });

  $(document).on("click", ".update-questions-btn", function () {
    const quizId = $(this).data("quizid");
    window.location.href = `../question/question.html?quizId=${quizId}`;
  });

  $(document).on("click", ".delete-quiz-btn", function () {
    const quizId = $(this).data("quizid");
    if (confirm("Are you sure you want to delete this quiz?")) {
      $.ajax({
        url: `http://localhost:2025/api/quizes/${quizId}`,
        method: "DELETE",
        success: function () {
          $(`#quiz-${quizId}`).remove();
        },
        error: function () {
          alert("Failed to delete quiz.");
        },
      });
    }
  });

  $(document).ready(function () {
    loadQuizzes();
  });