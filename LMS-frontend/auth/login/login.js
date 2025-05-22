// Toggle password visibility
document
.querySelector(".toggle-password")
.addEventListener("click", function () {
  const passwordInput =
    this.parentElement.parentElement.querySelector("input");
  const icon = this.querySelector("i");

  if (passwordInput.type === "password") {
    passwordInput.type = "text";
    icon.classList.remove("fa-eye");
    icon.classList.add("fa-eye-slash");
  } else {
    passwordInput.type = "password";
    icon.classList.remove("fa-eye-slash");
    icon.classList.add("fa-eye");
  }
});

// Show forgot password modal
function showForgotPassword() {
$("#forgotPasswordModal").modal("show");
}

// Handle password reset
function resetPassword() {
const form = document.getElementById("forgotPasswordForm");
if (form.checkValidity()) {
  // Get the email value from the form
  const email = form.querySelector('input[type="email"]').value;
  $.ajax({
    url: `http://localhost:2025/emailexists?useremail=${encodeURIComponent(
      email
    )}`,
    type: "GET",
    success: function (response) {
      // Assuming response contains userId
      if (response.userId) {
        localStorage.setItem("userId", response.userId);
      }
      // Redirect to changepassword.html
      window.location.href = "../changepassword/changepassword.html";
    },
    error: function (xhr) {
      if (xhr.status === 400) {
        alert("Email not found. Please enter a registered email.");
      } else {
        alert("Something went wrong. Please try again.");
      }
    },
  });
} else {
  form.reportValidity();
}
}

// Handle login form submission
document
.getElementById("loginForm")
.addEventListener("submit", function (e) {
  e.preventDefault();
  // Removed the old redirect logic here to avoid conflict with AJAX success redirect
});

$("#loginButton").click(function (event) {
event.preventDefault(); // Prevent the default form submission

const useremail = $("#useremail").val();
const password = $("#password").val();

const data = JSON.stringify({
  useremail: useremail,
  password: password,
});

$.ajax({
  url: "http://localhost:2025/login",
  type: "POST",
  contentType: "application/json",
  data: data,
  success: function (response) {
    console.log("Login successful:", response.userId);
    localStorage.setItem("userId", response.userId);
    console.log(
      "User ID stored in localStorage:",
      localStorage.getItem("userId")
    );
    const role = response.role;
    if (role === "Student") {
      window.location.href = "../../pages/dashboard/index.html";
    } else if (role === "Instructor") {
      window.location.href = "../../pages/instructor/dashboard/dashboard.html";
    } else {
      window.location.href = "../../pages/dashboard/index.html";
    }
  },
  error: function (xhr, status, error) {
    console.error("Login failed:", xhr, status, error);
    if (
      xhr.responseJSON &&
      xhr.responseJSON.message == "Invalid username."
    ) {
      window.alert("Invalid Email");
      console.log("Failed to Login:", xhr.responseJSON.message);
    } else if (
      xhr.responseJSON &&
      xhr.responseJSON.message == "Invalid password."
    ) {
      window.alert("Invalid Password");
      console.log("Failed to Login: Invalid credentials.");
    } else if (xhr.responseText) {
      console.log("Failed to Login: Invalid credentials.");
    } else {
      console.log("Failed to Login: Invalid credentials.");
    }
  },
});
});