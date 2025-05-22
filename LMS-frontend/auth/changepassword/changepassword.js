document
          .getElementById("changePasswordForm")
          .addEventListener("submit", function (e) {
            e.preventDefault();
            const newPassword = document.getElementById("newPassword").value;
            const confirmPassword = document.getElementById("confirmPassword").value;
            const errorMsg = document.getElementById("errorMsg");
      
            if (!newPassword || !confirmPassword) {
              errorMsg.textContent = "Please fill in all fields.";
              errorMsg.style.display = "block";
              return;
            }
      
            if (newPassword !== confirmPassword) {
              errorMsg.textContent = "Passwords do not match.";
              errorMsg.style.display = "block";
              return;
            }
      
            errorMsg.style.display = "none";
      
            // Get userId from localStorage
            const userId = localStorage.getItem("userId");
            console.log("User ID:", userId); // Debugging line
            
            if (!userId) {
              errorMsg.textContent = "User not found. Please try again.";
              errorMsg.style.display = "block";
              return;
            }
      
            // AJAX call to update password
            $.ajax({
              url: "http://localhost:2025/updatePassword",
              type: "PUT",
              contentType: "application/json",
              data: JSON.stringify({
                userId: userId,
                password: newPassword
              }),
              success: function () {
                alert("Password updated successfully!");
                window.location.href = "../login/login.html";
              },
              error: function (xhr) {
                errorMsg.textContent = xhr.responseJSON && xhr.responseJSON.message
                  ? xhr.responseJSON.message
                  : "Failed to update password.";
                errorMsg.style.display = "block";
              }
            });
          });