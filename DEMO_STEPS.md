# Step-by-step guide for tomorrow's class

This guide is written for someone who does **not** usually configure pipelines.

---

## Part A — What you install before class

### 1. Install Java 17
Check:

```bash
java -version
```

### 2. Install Maven
Check:

```bash
mvn -version
```

### 3. Install Git
Check:

```bash
git --version
```

### 4. Install Docker Desktop
Check:

```bash
docker --version
```

### 5. Install Gitleaks (local secret scanning)
Then check:

```bash
gitleaks version
```

### 6. Optional but recommended: install Semgrep locally
Then check:

```bash
semgrep --version
```

If Semgrep is not installed locally, that is fine because GitHub Actions will run it in CI.

---

## Part B — Open the project and initialize Git

Go inside the project folder:

```bash
cd devsecops-demo
```

Initialize Git:

```bash
git init
```

Install the pre-commit hook:

```bash
bash scripts/install-pre-commit.sh
```

---

## Part C — Demo 1: Pre-commit secret scanning

### Goal
Show students that security can stop a mistake **before** code even enters Git history.

### What to do
Stage everything:

```bash
git add .
```

Try to commit:

```bash
git commit -m "Initial vulnerable demo"
```

### What should happen
The pre-commit hook runs Gitleaks and blocks the commit because of the fake AWS-style key in `application.properties`.

### What you explain
- This is the **pre-commit gate**.
- The code never reaches the remote repository.
- This is cheaper than discovering the secret after push.

### Fix it live
Open `src/main/resources/application.properties` and delete this line:

```properties
demo.aws.access.key=REDACTED_DEMO_KEY
```

Now run again:

```bash
git add .
git commit -m "Remove fake secret so pre-commit passes"
```

If the commit succeeds, your local gate is working.

---

## Part D — Demo 2: Run the app locally

Start the application:

```bash
mvn spring-boot:run
```

Wait until you see that the app is running on port 8080.

Open these URLs in a browser:

1. Normal query:

```text
http://localhost:8080/search?id=1
```

2. SQL injection example:

```text
http://localhost:8080/search?id=1%20OR%201=1
```

3. Normal echo:

```text
http://localhost:8080/echo?msg=hello
```

4. XSS example:

```text
http://localhost:8080/echo?msg=<script>alert(1)</script>
```

### What you explain
- `/search` is vulnerable because user input is concatenated directly into SQL.
- `/echo` is vulnerable because untrusted input is returned directly into HTML.

Stop the app with `Ctrl + C` after this quick live proof.

---

## Part E — Demo 3: Local SAST with Semgrep (optional but powerful)

If Semgrep is installed locally, run:

```bash
semgrep scan --config semgrep-rules --error src
```

### What should happen
The custom rules should flag:
- SQL injection in `search()`
- Reflected XSS in `echo()`

### What you explain
- SAST reads source code **without running the app**.
- It is ideal for catching insecure coding patterns early.

---

## Part F — Put the demo on GitHub

### 1. Create an empty repository on GitHub
Do **not** add a README there because your local project already has files.

### 2. Connect your local repo to GitHub
Replace the URL below with your own repository URL:

```bash
git remote add origin https://github.com/YOUR-USERNAME/devsecops-demo.git
```

Rename the branch to `main`:

```bash
git branch -M main
```

Push:

```bash
git push -u origin main
```

### What happens now
GitHub automatically detects the file `.github/workflows/secure-ci.yml` and starts the pipeline.

---

## Part G — How to read the pipeline in GitHub Actions

Open your GitHub repository.

1. Click **Actions**.
2. Click the workflow run named **Secure CI Demo**.
3. Open the job **secure-pipeline**.
4. Expand the steps one by one.

You will see the stages in order:
- Gitleaks
- Build + lint
- Semgrep
- Dependency-Check
- App startup
- ZAP

### What to tell students
This is a **secure CI/CD pipeline** because security checks are integrated directly into delivery automation.

---

## Part H — Expected classroom storyline

### First push after removing only the fake secret
What should happen:
- Gitleaks passes
- Build/lint should pass
- Semgrep should fail because the code still contains SQL injection and reflected XSS
- SCA should also flag the old Log4j dependency
- ZAP will generate a report for runtime findings

This is perfect pedagogically because students see that one fix is not enough.

---

## Part I — Fix the code live and push again

### 1. Fix SQL injection
Use the secure version from `FIXES.md`.

### 2. Fix reflected XSS
Use the secure version from `FIXES.md`.

### 3. Remove the vulnerable Log4j dependency
Also in `FIXES.md`.

Now run locally:

```bash
mvn clean verify
```

If Semgrep is installed locally, re-run:

```bash
semgrep scan --config semgrep-rules --error src
```

Commit the fixes:

```bash
git add .
git commit -m "Fix SQLi XSS and vulnerable dependency"
git push
```

### What happens
A new pipeline run starts.

This time it should be much cleaner.

---

## Part J — DAST with ZAP locally

Start the application again:

```bash
mvn spring-boot:run
```

In another terminal, run:

```bash
bash scripts/run-local-zap.sh
```

This generates:
- `zap-report.html`
- `zap-report.json`

Open `zap-report.html` in the browser and show the students the findings.

### What you explain
- DAST scans the **running application from the outside**.
- It validates the exposed runtime surface.
- It complements SAST, it does not replace it.

---

## Part K — Very important backup plan

If something goes wrong tomorrow:

1. Still show the browser-based SQLi/XSS behavior.
2. Still show the pre-commit block.
3. Still open the GitHub Actions workflow and show whichever stages completed.
4. Use the files in `FIXES.md` to explain the secure version.

That is enough for a strong classroom demo.

---

## Part L — The shortest version if you are short on time

If you only have 15–20 minutes, do exactly this:

1. Commit blocked by Gitleaks.
2. Remove fake secret and commit again.
3. Run the app and show SQL injection and XSS in browser.
4. Push to GitHub and open the Actions pipeline.
5. Show Semgrep and Dependency-Check findings.
6. Explain that ZAP is the runtime gate.

That short version is already a very good DevSecOps demonstration.
