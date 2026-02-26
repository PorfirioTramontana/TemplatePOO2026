# Progetto ST: pipeline di analisi del codice

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=PorfirioTramontana_TemplatePOO2026&metric=alert_status)](https://sonarcloud.io/project/configuration?id=PorfirioTramontana_TemplatePOO2026)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=PorfirioTramontana_TemplatePOO2026&metric=coverage)](https://sonarcloud.io/project/configuration?id=PorfirioTramontana_TemplatePOO2026)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=PorfirioTramontana_TemplatePOO2026&metric=bugs)](https://sonarcloud.io/project/configuration?id=PorfirioTramontana_TemplatePOO2026)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=PorfirioTramontana_TemplatePOO2026&metric=vulnerabilities)](https://sonarcloud.io/project/configuration?id=PorfirioTramontana_TemplatePOO2026)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=PorfirioTramontana_TemplatePOO2026&metric=code_smells)](https://sonarcloud.io/project/configuration?id=PorfirioTramontana_TemplatePOO2026)

[![AI Code Assurance](https://sonarcloud.io/api/project_badges/measure?project=PorfirioTramontana_TemplatePOO2026&metric=alert_status)](https://sonarcloud.io/project/configuration?id=PorfirioTramontana_TemplatePOO2026)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=PorfirioTramontana_TemplatePOO2026&metric=alert_status)](https://sonarcloud.io/project/configuration?id=PorfirioTramontana_TemplatePOO2026)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=PorfirioTramontana_TemplatePOO2026&metric=alert_status)](https://sonarcloud.io/project/configuration?id=PorfirioTramontana_TemplatePOO2026)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=PorfirioTramontana_TemplatePOO2026&metric=alert_status)](https://sonarcloud.io/project/configuration?id=PorfirioTramontana_TemplatePOO2026)

[![Duplicated Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=PorfirioTramontana_TemplatePOO2026&metric=alert_status)](https://sonarcloud.io/project/configuration?id=PorfirioTramontana_TemplatePOO2026)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=PorfirioTramontana_TemplatePOO2026&metric=alert_status)](https://sonarcloud.io/project/configuration?id=PorfirioTramontana_TemplatePOO2026)
[![Security Hotspots](https://sonarcloud.io/api/project_badges/measure?project=PorfirioTramontana_TemplatePOO2026&metric=alert_status)](https://sonarcloud.io/project/configuration?id=PorfirioTramontana_TemplatePOO2026)


> **Important:** After forking this repository, you **must** edit this `README.md` file. Replace all instances of `Redy1908_Applicativo` in the above links with  `YourGithHubUsername_Applicativo`. Once you have done this, and followed the setup instructions below, you can delete this note and the instructions.

---

This repository serves as a base template for programming projects. It comes pre-configured with:
*   Java 21
*   Maven for dependency and build management
*   JUnit 5 for unit testing
*   A Continuous Integration (CI) workflow with GitHub Actions that automates builds, tests, and code quality analysis.
*   Integration with SonarCloud for static code analysis.

---

## Initial Setup

Fork this repository.

Create your own organization on SonarCloud, linked to your GitHub account.

1.  **Log in to SonarCloud**: Go to [SonarCloud.io](https://sonarcloud.io/) and log in with your GitHub account.
2.  **Create a New Organization**:
    *   Click the `+` icon in the top-right corner and select **Create new organization**.
    *   SonarCloud will ask you to choose a GitHub account to link. Select your personal GitHub account and grant permissions.
    *   You will be prompted for an **Organization Key**. This is a critical step. By default, SonarCloud will suggest your GitHub username in lowercase, you can use that or customize it. Just make sure to edit the `pom.xml` file in this repository to replace `unina` with the exact key you choose here.
    *   Choose the **Free plan** and click "Create Organization".
3.  **Add Your Project to the New Organization**:
    *   After creating the organization, you will be prompted to import a project. If not, click the `+` icon again and select **Analyze new project**.
    *   Make sure you are in the correct organization (the one you just created).
    *   Select your project repository from the list and click **Set Up**.
4.  **Disable Automatic Analysis**:
    *   Once the project is set up, you will be on the project's main page.
    *   Go to **Administration** > **Analysis Method**.
    *   Turn **OFF** the "SonarCloud Automatic Analysis" toggle. Our custom GitHub Actions workflow will handle the analysis.

### 3. Generate a SonarCloud Access Token

The GitHub Actions workflow needs a token to authenticate and send data to your new SonarCloud organization.

1.  On SonarCloud, click your profile icon in the top right corner and go to **My Account**.
2.  Select the **Security** tab.
3.  Under "Generate Tokens", enter a name for your token (e.g., `github-token`) and click **Generate**.
4.  **Copy the generated token and save it temporarily.** You will not be able to see it again.

### 4. Add the Token to GitHub Secrets

Now you need to securely add the token to your GitHub repository.

1.  Go to your GitHub repository (the one forked from this repository).
2.  Click on **Settings** > **Secrets and variables** > **Actions**.
3.  Click the **New repository secret** button.
4.  Fill in the fields:
    *   **Name**: `SONAR_TOKEN` (the name must match).
    *   **Secret**: Paste the token you copied from SonarCloud.
5.  Click **Add secret**.

**Done!** Your project is now fully configured.

## Optional: Changing the Java Version
This template is configured to use Java 21 by default. If you need to use a different version, you must update it in two places to ensure both your local build and the CI workflow are aligned.

### 1. Maven Configuration

In the `pom.xml` file, update the `maven.compiler.source` and `maven.compiler.target` properties to your desired Java version.

### 2. GitHub Actions Workflow

In the `.github/workflows/pipeline.yaml` file, update the `java-version` in the steps named "Set up JDK" step to match the version you set in your `pom.xml`.

---

## How It Works

From now on, the process is automatic: 

*   **On every `push` to the `main` branch**: GitHub Actions will trigger the workflow, compile the code, run the tests, and send a full analysis to SonarCloud, updating the project's dashboard.
*   **When you open a `Pull Request`**: The workflow will run, and SonarCloud will post a **summary comment** directly on the Pull Request page, showing you the impact of your changes on code quality before you even merge them.

You can monitor the status of each run in the **"Actions"** tab of your repository.
