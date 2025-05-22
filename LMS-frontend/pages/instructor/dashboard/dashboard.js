const toggleBtn = document.querySelector(".toggle-sidebar");
const sidebar = document.querySelector(".sidebar");
const mainContent = document.querySelector(".main-content");

toggleBtn.addEventListener("click", () => {
  sidebar.classList.toggle("collapsed");
  mainContent.classList.toggle("expanded");
});


$(document).ready(function () {
  $.ajax({
    url: "http://localhost:2025/api/enrollments/progressTracking",
    type: "GET",
    dataType: "json",
    success: function (response) {
      if (
        response.details &&
        Array.isArray(response.details) &&
        response.details.length > 0
      ) {
        $("#studentCount").text(response.details.length);
      } else {
            $("#studentCount").text(0);

      }
    },
    error: function () {
      $("#studentCount").text("Failed to load data.");
    },
  });
  
  $.ajax({
    url: "http://localhost:2025/api/courses/allCourses",
    type: "GET",
    dataType: "json",
    success: function (response) {
      if (
        response.details &&
        Array.isArray(response.details) &&
        response.details.length > 0
      ) {
        $("#courseCount").text(response.details.length);
      } else {
          $("#courseCount").text(0);
      }
    },
    error: function () {
      $("#courseCount").text("Failed to load data.");
    },
  });

  $.ajax({
    url: "http://localhost:2025/api/quizes",
    type: "GET",
    dataType: "json",
    success: function (response) {
      if (
        response.details &&
        Array.isArray(response.details) &&
        response.details.length > 0
      ) {
        $("#quizCount").text(response.details.length);
      } else {
          $("#quizCount").text(0);
      }
    },
    error: function () {
      $("#quizCount").text("Failed to load data.");
    },
  });

});

// Chart.js Pie Chart
const ctx = document.getElementById("completionChart").getContext("2d");
const completionChart = new Chart(ctx, {
  type: "pie",
  data: {
    labels: ["Completed", "In Progress", "Not Started"],
    datasets: [
      {
        label: "Course Completion",
        data: [60, 25],
        backgroundColor: [
          "rgba(40, 167, 69, 0.7)", // green
          "rgba(255, 193, 7, 0.7)", // yellow
          "rgba(220, 53, 69, 0.7)", // red
        ],
        borderColor: [
          "rgba(40, 167, 69, 1)",
          "rgba(255, 193, 7, 1)",
          "rgba(220, 53, 69, 1)",
        ],
        borderWidth: 1,
      },
    ],
  },
  options: {
    responsive: true,
    plugins: {
      legend: {
        position: "bottom",
      },
      title: {
        display: false,
      },
    },
  },
});

// Chart.js Bar Chart for Quiz Completion
const ctxBar = document
  .getElementById("quizCompletionChart")
  .getContext("2d");
const quizCompletionChart = new Chart(ctxBar, {
  type: "bar",
  data: {
    labels: Array.from({ length: 30 }, (_, i) => `Day ${i + 1}`),
    datasets: [
      {
        label: "Quizzes Completed",
        data: [
          2, 3, 1, 4, 5, 3, 2, 4, 6, 5, 3, 4, 2, 3, 5, 6, 4, 3, 2, 5, 6,
          4, 3, 2, 4, 5, 3, 2, 4, 5,
        ],
        backgroundColor: "rgba(54, 162, 235, 0.7)",
        borderColor: "rgba(54, 162, 235, 1)",
        borderWidth: 1,
      },
    ],
  },
  options: {
    responsive: true,
    scales: {
      x: {
        title: {
          display: true,
          text: "Days in Month",
        },
      },
      y: {
        beginAtZero: true,
        title: {
          display: true,
          text: "Number of Quizzes Completed",
        },
      },
    },
    plugins: {
      legend: {
        position: "top",
      },
      title: {
        display: false,
      },
    },
  },
});