const toggleBtn = document.querySelector('.toggle-sidebar');
const sidebar = document.querySelector('.sidebar');
const mainContent = document.querySelector('.main-content');

toggleBtn.addEventListener('click', () => {
    sidebar.classList.toggle('collapsed');
    mainContent.classList.toggle('expanded');
});

// Mobile sidebar toggle
const hamburger = document.querySelector('.navbar-toggler');
if (hamburger) {
    hamburger.addEventListener('click', () => {
        sidebar.classList.toggle('active');
    });
}

// Close sidebar when clicking outside on mobile
document.addEventListener('click', (e) => {
    if (!sidebar.contains(e.target) && (!hamburger || !hamburger.contains(e.target))) {
        sidebar.classList.remove('active');
    }
});

function renderStudents(students) {
const tableBody = document.getElementById('studentTableBody');
tableBody.innerHTML = students.map(student => `
    <tr>
        <td>${student.userName}</td>
        <td>${student.userEmail}</td>
        <td>${student.courseName}</td>
        <td>${student.averageProgress}%</td>
    </tr>
`).join('');
}
$(document).ready(function () {
$.ajax({
    url: 'http://localhost:2025/api/enrollments/progressTracking',
    type: 'GET',
    dataType: 'json',
    success: function (response) {
        if (response.details && Array.isArray(response.details) && response.details.length > 0) {
            renderStudents(response.details);
        } else {
            $('#studentTableBody').html('<tr><td colspan="4">No data found.</td></tr>');
        }
    },
    error: function () {
        $('#studentTableBody').html('<tr><td colspan="4" class="text-danger">Failed to load data.</td></tr>');
    }
});
});