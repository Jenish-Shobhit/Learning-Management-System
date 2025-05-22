const password = document.getElementById("password");
const confirmPassword = document.getElementById("confirmPassword");
const requirements = {
  length: /.{8,}/,
  uppercase: /[A-Z]/,
  lowercase: /[a-z]/,
  number: /[0-9]/,
  special: /[!@#$%^&*(),.?":{}|<>]/,
};

function updatePasswordRequirements() {
  const value = password.value;
  for (const [requirement, regex] of Object.entries(requirements)) {
    const element = document.getElementById(requirement);
    const isValid = regex.test(value);
    element.classList.toggle("valid", isValid);
    element.classList.toggle("invalid", !isValid && value.length > 0);
    element.querySelector("i").className = isValid
      ? "fas fa-check-circle"
      : "fas fa-circle";
  }
}

password.addEventListener("input", updatePasswordRequirements);

document.querySelectorAll(".toggle-password").forEach((toggle) => {
  toggle.addEventListener("click", function () {
    const input = this.parentElement.parentElement.querySelector("input");
    const icon = this.querySelector("i");

    if (input.type === "password") {
      input.type = "text";
      icon.classList.remove("fa-eye");
      icon.classList.add("fa-eye-slash");
    } else {
      input.type = "password";
      icon.classList.remove("fa-eye-slash");
      icon.classList.add("fa-eye");
    }
  });
});

document.getElementById("signupForm").addEventListener("submit", function (e) {
  e.preventDefault();

  const firstName = document
    .querySelector("[placeholder='Enter first name']")
    .value.trim();
  const lastName = document
    .querySelector("[placeholder='Enter last name']")
    .value.trim();
  const email = document
    .querySelector("[placeholder='Enter your email']")
    .value.trim();
  const password = document.getElementById("password").value.trim();
  const role = document.querySelector('input[name="role"]:checked').value;
  const username = firstName + " " + lastName;

  function isValidEmail(email) {
    return /^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$/.test(email);
  }
  if (!isValidEmail(email)) {
    alert("Please enter a valid email address.");
    return;
  }

  if (
    password.length < 8 ||
    !/[A-Z]/.test(password) ||
    !/[a-z]/.test(password) ||
    !/[0-9]/.test(password) ||
    !/[!@#$%^&*(),.?":{}|<>]/.test(password)
  ) {
    alert("Your password does not meet the requirements.");
    return;
  }

  var xhr = new XMLHttpRequest();
  xhr.open("POST", "http://localhost:2025/register", true);
  xhr.setRequestHeader("Content-Type", "application/json");

  const userData = {
    name: username,
    email: email,
    password: password,
    role: role,
  };

  xhr.onload = function () {
    if (xhr.status === 200) {
      alert("Signup Successful!");
      window.location.href = "../../auth/login/login.html";
    } else {
      alert("Signup failed. Please try again.");
    }
  };

  xhr.onerror = function () {
    alert("An error occurred during the signup process. Please try again.");
  };

  xhr.send(JSON.stringify(userData));
});
