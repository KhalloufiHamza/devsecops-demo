# DevSecOps & Secure CI/CD Demo Kit

This project is intentionally insecure so you can demonstrate multiple security gates during one class.

## What is intentionally broken

1. **Fake hardcoded secret** in `application.properties` for secret scanning.
2. **SQL injection** in `DemoController.search()` for SAST.
3. **Reflected XSS** in `DemoController.echo()` for SAST / DAST discussion.
4. **Old vulnerable dependency** (`log4j-core:2.14.1`) for SCA.

## What is included

- Spring Boot demo application
- Git pre-commit hook
- Gitleaks config
- Checkstyle config
- Custom Semgrep rules
- GitHub Actions pipeline
- Local helper scripts
- Detailed teaching guide in `DEMO_STEPS.md`
- Fix instructions in `FIXES.md`

## Recommended toolchain for class

- Java 17
- Maven 3.9+
- Git
- GitHub account/repository
- Docker Desktop (for ZAP)
- Optional local tools:
  - Gitleaks
  - Semgrep

## Quick start

1. Extract the zip.
2. Read `DEMO_STEPS.md` from top to bottom.
3. Run the local pre-commit part first.
4. Then push to GitHub to show the CI pipeline.
