# Contributing

Thanks for helping improve the LMS. The current application is a prototype; please read the [README](README.md), especially its security limitations, before proposing changes.

1. Open an issue for a substantial change so the scope and expected behavior are clear. For a bug, include reproduction steps and remove private data from logs or screenshots.
2. Create a focused branch and keep each pull request about one change.
3. For backend changes, use JDK 21 and run `cd LMS-backend && ./mvnw verify` with a local MySQL database configured as described in the README.
4. For frontend JavaScript changes, run `node --check` on the files you changed and verify the affected pages through a local HTTP server.
5. Explain what changed, how you checked it, and any limitations in the pull request. Update documentation when behavior or setup changes.

Report possible vulnerabilities through [SECURITY.md](SECURITY.md), not a public issue.
