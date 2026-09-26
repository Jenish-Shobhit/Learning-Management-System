# Frontend

This directory contains static HTML, CSS, and JavaScript. It has login, signup, password-change, student, and instructor pages. The pages use CDN-hosted Bootstrap 4, jQuery, Font Awesome 5, and Chart.js where referenced by the HTML.

From this directory, run `python3 -m http.server 5500 --bind 127.0.0.1`, then open [http://127.0.0.1:5500/](http://127.0.0.1:5500/). Start the backend on port `2025` first; the frontend scripts call `http://localhost:2025`.

See the [repository README](../README.md) for setup and security limitations.
