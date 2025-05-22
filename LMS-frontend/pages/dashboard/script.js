const toggleBtn = document.querySelector(".toggle-sidebar");
const sidebar = document.querySelector(".sidebar");
const mainContent = document.querySelector(".main-content");

toggleBtn.addEventListener("click", () => {
  sidebar.classList.toggle("collapsed");
  mainContent.classList.toggle("expanded");
});

const ctx = document.getElementById("completionChart").getContext("2d");
const completionChart = new Chart(ctx, {
  type: "pie",
  data: {
    labels: ["Completed", "Not Started"],
    datasets: [
      {
        data: [0, 0],
        backgroundColor: [
          "rgba(40, 167, 69, 0.7)", // green (Completed)
          "rgba(255, 193, 7, 0.7)", // yellow (Not Started)
        ],
        borderColor: ["rgba(40, 167, 69, 1)", "rgba(255, 193, 7, 1)"],
        borderWidth: 1,
      },
    ],
  },
  options: {
    responsive: true,
    plugins: {
      legend: {
        position: "bottom",
        labels: {
          generateLabels: function (chart) {
            const data = chart.data.datasets[0].data;
            return chart.data.labels.map(function (label, i) {
              return {
                text: label + ": " + data[i] + "%",
                fillStyle: chart.data.datasets[0].backgroundColor[i],
                strokeStyle: chart.data.datasets[0].borderColor[i],
                lineWidth: 1,
                hidden: isNaN(data[i]) || data[i] === null,
                index: i,
              };
            });
          },
        },
      },
      tooltip: {
        callbacks: {
          label: function (context) {
            let label = context.label || "";
            let value = context.parsed || 0;
            return label + ": " + value + "%";
          },
        },
      },
      title: {
        display: false,
      },
    },
  },
});
$(document).ready(function () {
  $.ajax({
    url: "http://localhost:2025/api/enrollments/leaderboard",
    type: "GET",
    dataType: "json",
    success: function (response) {
      const leaderboard = response.details || [];
      const $list = $(".leaderboard-list");
      $list.empty();

      if (leaderboard.length === 0) {
        $list.append(
          '<li class="list-group-item">No leaderboard data available.</li>'
        );
        return;
      }

      leaderboard.forEach(function (entry) {
        $list.append(`
          <li class="list-group-item">
            <span class="leaderboard-name">${entry.userName}</span>
            <span class="badge badge-pill leaderboard-badge">${entry.averageProgress}%</span>
          </li>
        `);
      });
    },
    error: function () {
      $(".leaderboard-list").html(
        '<li class="list-group-item text-danger">Failed to load leaderboard.</li>'
      );
    },
  });
  const userId = localStorage.getItem("userId");
  $.ajax({
    url: `http://localhost:2025/api/enrollments/user/${userId}`,
    type: "GET",
    dataType: "json",
    success: function (response) {
      const enrollments = response.details || [];
      $("#enrolledCoursesCount").text(enrollments.length);
      const quizCount = enrollments.filter(
        (e) =>
          e.course && e.course.quizIds && String(e.course.quizIds).trim() !== ""
      ).length;
      $("#quizCount").text(quizCount);
      if (enrollments.length === 0) {
        // No enrollments, set chart to 0
        completionChart.data.datasets[0].data = [0, 100];
        completionChart.update();
        $("#progress").text(0);
        return;
      }
      let totalProgress = 0;
      enrollments.forEach((e) => {
        totalProgress += e.progress;
      });
      const avgProgress = totalProgress / enrollments.length;
      $("#progress").text(Math.round(avgProgress) + "%");
      console.log(avgProgress);
      completionChart.data.datasets[0].data = [
        Math.round(avgProgress),
        100 - Math.round(avgProgress),
      ];
      completionChart.update();
    },
    error: function () {
      // On error, show 0 completed
      completionChart.data.datasets[0].data = [0, 100];
      completionChart.update();
    },
  });

  $.ajax({
    url: "http://localhost:2025/api/courses/allCourses",
    type: "GET",
    dataType: "json",
    success: function (response) {
      // Assuming response.details is an array of courses
      const courses = response.details || response; // fallback if details is not present
      $("#allCoursesCount").text(courses.length);
    },
    error: function () {
      $("#allCoursesCount").text("0");
    },
  });
});
