# Automation Exercise Test Framework

A learning and portfolio project demonstrating a reusable test framework with Playwright and Java, using AutomationExercise for UI and API testing. This is an independent project, unaffiliated with AutomationExercise, and is not a production framework.

## Stack and design

- Java 21, Maven Wrapper (Apache Maven 3.10.0), JUnit 5, and Maven Surefire.
- Playwright for Chromium UI automation and HTTP requests through `APIRequestContext`.
- Page Objects and reusable UI components, with shared account flows and page assertions.
- An API layer separating session management, HTTP transport, endpoint methods, and Gson response models.
- SLF4J and Logback logging, with JUnit extensions for test lifecycle logging and log archive maintenance.

UI tests share a browser within each test class and receive a fresh browser context and page per test. API tests receive a separate request context per test without launching a browser.

## Test coverage

- **UI:** registration and form validation, login/logout and account deletion, product search and details, category/brand filters, cart operations and persistence, checkout and payment, navigation, contact forms, product reviews, and subscriptions.
- **API:** products, brands, product search, missing-parameter validation, and an account lifecycle covering creation, lookup, login, update, deletion, and login rejection after deletion.

Account scenarios generate unique email addresses and include cleanup logic. API assertions check both HTTP status and the application's JSON `responseCode` where applicable.

One partial-name UI search test is disabled while the expected matching rules are clarified.

Tests depend on an external demonstration site that may be temporarily unavailable.

## Getting started

Install JDK 21. Maven Wrapper downloads and uses Apache Maven 3.10.0 automatically, so a separate Maven installation is not required. Internet access is needed to download dependencies and Playwright browser binaries and to reach the test site. UI tests run in a visible Chromium window by default, so a graphical environment is needed with the default configuration.

Run commands from the repository root.

On Windows:

```cmd
mvnw.cmd test
mvnw.cmd test -Dgroups=api
mvnw.cmd test -Dgroups=ui
mvnw.cmd test -Dgroups=smoke
mvnw.cmd test -Dtest=AccountApiTest
```

On macOS/Linux:

```sh
./mvnw test
./mvnw test -Dgroups=api
./mvnw test -Dgroups=ui
./mvnw test -Dgroups=smoke
./mvnw test -Dtest=AccountApiTest
```

Install the Playwright-managed Chromium browser before the first UI test run.

On Windows:

```cmd
mvnw.cmd exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install chromium"
```

On macOS/Linux:

```sh
./mvnw exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install chromium"
```

Repeat the matching command after changing the Playwright version. Tests can also be run through JUnit in an IDE configured with JDK 21.

Surefire uses JUnit tags for `groups` filtering. Available tags are `ui`, `api`, `smoke`, `negative`, `account`, `catalog`, `cart`, `checkout`, `navigation`, `contact`, `reviews`, and `subscription`. For example, `mvnw.cmd test "-Dgroups=api & catalog"` on Windows or `./mvnw test "-Dgroups=api & catalog"` on macOS/Linux selects API catalog tests.

## Configuration

Edit [`src/main/resources/config.properties`](src/main/resources/config.properties):

| Property | Default | Purpose |
| --- | --- | --- |
| `base.url` | `https://www.automationexercise.com` | UI base URL |
| `browser.headless` | `false` | Chromium headless mode |
| `browser.timeout` | `15000` | Default page operation timeout, ms |
| `api.base.url` | `https://automationexercise.com/api/` | API base URL |
| `api.timeout` | `30000` | API request timeout, ms |

These settings are read from the classpath properties file; JVM `-D` properties do not override them. Set `browser.headless=true` in that file for headless UI runs.

## Repository layout

```text
src/main/java/
  data/                   Account and credential records
  framework/
    api/                  API sessions, client, endpoints, response models
    browser/              Chromium lifecycle, sessions, overlay handling
    components/           Reusable UI components
    config/               Properties loader
    pages/                Page Objects
    utils/                Shared utilities and log archiving
src/main/resources/       Application and logging configuration
src/test/java/
  assertions/             Shared page assertions
  base/                   UI and API test lifecycle
  flows/                  Reusable account workflows
  framework/extensions/   JUnit logging extensions
  testsuites/api/          Account and catalog API tests
  testsuites/ui/           UI tests grouped by feature
```

## Results and logs

Maven test results are written to `target/surefire-reports/`. Logging goes to the console and timestamped `logs/framework-*.log` files. Older logs are archived under `logs/archive/`, with 30-day retention and a 500 MB archive size limit. The default log level is `INFO`; `LOG_LEVEL` controls verbosity in the Logback configuration.
