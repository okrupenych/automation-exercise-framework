# Automation Exercise Test Framework

A learning and portfolio project demonstrating a reusable test framework with Playwright and Java, using AutomationExercise for UI and API testing. This is an independent project, unaffiliated with AutomationExercise, and is not a production framework.

## Stack and design

- Java 21, Maven Wrapper (Apache Maven 3.10.0), JUnit 5, and Maven Surefire.
- Playwright for Chromium, Firefox and WebKit UI automation and HTTP requests through `APIRequestContext`.
- Page Objects and reusable UI components, with shared account flows and page assertions.
- An API layer separating session management, HTTP transport, endpoint methods, and Gson response models.
- SLF4J and Logback logging, with JUnit extensions for test lifecycle logging and log archive maintenance.
- Allure JUnit Jupiter and Playwright Java reporting, with AspectJ steps and browser diagnostics.

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
| `browser.headless` | `false` | Browser headless mode |
| `browser.timeout` | `15000` | Default page operation timeout, ms |
| `browser.recovery.timeout` | `15000` | Finite scroll/overlay recovery timeout when browser.timeout is 0, ms |
| `browser.transient.text.timeout` | `5000` | Transient message capture timeout, ms |
| `browser.log.name.timeout` | `500` | Product-name lookup timeout for logging, ms |
| `logs.archive.retention.days` | `30` | Log archive retention, days |
| `logs.archive.max.size.mb` | `500` | Total log archive size limit, MiB |
| `api.base.url` | `https://automationexercise.com/api/` | API base URL |
| `api.timeout` | `30000` | API request timeout, ms |

Settings use **JVM properties > environment variables > config.properties**. Environment names replace dots with underscores and uppercase the key: `browser.type` ? `BROWSER_TYPE`. Empty overrides are rejected. Values are validated when consumed by their component; API-only runs do not require browser binaries.

Additional settings:

| Property | Default | Purpose |
| --- | --- | --- |
| `browser.type` | `chromium` | `chromium`, `firefox`, `webkit` |
| `browser.launch.timeout` | `180000` | Browser launch timeout, ms; retains Playwright default |
| `browser.navigation.timeout` | `15000` | Context navigation timeout, ms |
| `browser.assertion.timeout` | `5000` | Playwright web assertion timeout, ms |
| `browser.slow.mo` | `0` | Delay between Playwright operations, ms |
| `browser.viewport.width` / `browser.viewport.height` | `1920` / `1080` | Fixed viewport, CSS pixels, inherited by new pages |
| `browser.locale` | `en-US` | BCP 47 locale |
| `browser.timezone` | `UTC` | Timezone identifier, validated by Java and browser |
| `browser.color.scheme` | `light` | `light`, `dark`, `no-preference` |
| `log.level` | `INFO` | OFF, ERROR, WARN, INFO, DEBUG, TRACE, ALL |
| `logs.directory` | `logs` | Shared Logback/archive root; relative to working directory or absolute |

Launch, action, navigation and API timeouts accept zero to disable the deadline. Assertion, recovery, transient text and diagnostic timeouts, viewport dimensions and archive limits must be positive integers. Slow motion is nonnegative. Boolean values are strict. URLs must be absolute HTTP(S), without credentials, queries or fragments; `api.base.url` requires a trailing slash. Locale and timezone are explicit for reproducible runs.

Examples (Windows; use `./mvnw` on macOS/Linux):

```cmd
mvnw.cmd test -Dgroups=ui -Dbrowser.type=firefox -Dbrowser.headless=true
mvnw.cmd test -Dbrowser.viewport.width=1440 -Dbrowser.viewport.height=900
mvnw.cmd test -Dgroups=api -Dapi.base.url=https://example.org/api/ -Dlog.level=DEBUG
```

PowerShell: `$env:BROWSER_TYPE = "webkit"`. `LOG_LEVEL` remains the environment counterpart of `log.level`; JVM overrides use `-Dlog.level=DEBUG`.

Install each selected Playwright-managed engine with the CLI command above using `install firefox` or `install webkit`. The framework uses no CDP, native window maximization, browser channels, custom executables or engine-specific launch arguments. Viewport size controls page layout, not native window bounds. Bundled Firefox/WebKit are separate engines, not installed Firefox/Safari; rendering and OS support differ. Unsupported engine names fail explicitly. There are no exposed Chromium-only settings.

Surefire/JUnit settings use native JVM/Maven options, separate from the application loader: `groups`, `excludedGroups`, `test`; `forkCount=1`, `reuseForks=true`, `surefire.reportsDirectory=target/surefire-reports` by default. Example: `mvnw.cmd test -Dgroups=api -DexcludedGroups=negative -Dsurefire.reportsDirectory=target/api-results`. JUnit parallel execution defaults to false; enabling it is rejected because shared class state and Playwright objects require serial execution within a JVM. Maven forks provide process isolation, but concurrent archive maintenance is not coordinated: keep one fork for normal runs. Native build options do not use the application environment-name mapping.

Current API tests need no environment credentials: scenarios generate fixture accounts. Keep real passwords/tokens outside tracked configuration and fixtures; supply future authenticated integrations through environment variables or JVM properties, without logging them. Test data and expected results remain unchanged.

## Repository layout

```text
src/main/java/
  data/                   Account and credential records
  framework/
    api/                  API sessions, client, endpoints, response models
    browser/              Cross-engine browser lifecycle, sessions, overlay handling
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

Maven XML/text results go to `target/surefire-reports`; Allure JSON and attachments go separately to `target/allure-results`. Console and timestamped `framework-*.log` files keep the configured log level and directory. Older daily logs are archived under `<logs.directory>/archive/`; retention and size limits apply to ZIP archives, not active logs.

## Allure reports

Reporting uses Allure Java 2.35.5 (`allure-jupiter`, the successor to `allure-junit5`, and `allure-playwright`), AspectJ 1.9.25.1, Maven plugin 2.16.1 and Allure Report 2.36.0. Java 21 needs no `--add-opens` with this weaver. Surefire 3.6.0 loads the agent through a quoted local-repository path in `argLine`; the plugin dependency downloads the agent. Existing `argLine` additions are preserved through late replacement. Keep `forkCount` positive: an in-process run does not load the agent. The JUnit BOM aligns Jupiter and Platform, including the launcher's transitive dependency from Allure.

Reports include test names, tags, statuses, failure stack traces, setup/cleanup fixtures, nested Page Object/component/flow/assertion steps and Playwright actions and web assertions. API calls appear as `API METHOD path` steps with HTTP status and duration; request options and response bodies are omitted. An HTTP error status alone does not fail the step: existing test assertions determine success. Method arguments are omitted from framework steps, and Playwright input values are redacted.

On UI failures, an invocation interceptor collects screenshot, page HTML, URL and title before cleanup, including failures in `@BeforeEach` and `@AfterEach` while a page is available. The adapter deduplicates screenshot/HTML/trace collection. Each test context records a trace with screenshots and DOM snapshots: it is attached immediately on failure or before context close on success. Browser console messages and uncaught JavaScript errors are attached on context close when present (Playwright retains a bounded recent history). Popups are registered by the Playwright adapter. Already closed or crashed pages may have incomplete diagnostics; collection failures are logged without replacing the original test failure. Browser lifetime remains per class, context lifetime per test; API-only tests launch no browser. Video is disabled. Existing framework logs remain in `logs/`.

Run from the repository root on Windows (PowerShell: use `.\mvnw.cmd`):

```cmd
mvnw.cmd clean test
mvnw.cmd test -Dgroups=ui
mvnw.cmd test -Dgroups=api
mvnw.cmd allure:report
mvnw.cmd allure:serve
```

On macOS/Linux:

```sh
./mvnw clean test
./mvnw test -Dgroups=ui
./mvnw test -Dgroups=api
./mvnw allure:report
./mvnw allure:serve
```

`allure:report` generates `target/site/allure-maven/index.html`. `allure:serve` generates a temporary report from the same results and opens it through a local web server; stop it with Ctrl+C. These report goals do not run tests. For an already generated HTML report, an installed Allure 2 CLI can serve it with `allure open target/site/allure-maven`. The Maven plugin downloads its own report distribution into `.allure/`, so no global CLI is needed for the Maven commands.

Use `clean test` for a fresh run: repeated `test` commands accumulate Allure results, which is useful when combining UI and API runs. Do not run `clean` between a test run and report generation. `target/`, `.allure/`, and root-level fallback Allure output directories are ignored by Git. Traces, HTML and browser logs can contain page/session data; input redaction applies to step labels only.

Allure-specific options live in `src/test/resources/allure.properties`, separately from application `config.properties`. Maven passes the absolute results path to the fork; a custom `-Dallure.results.directory=...` must remain different from the Surefire directory and should point inside `target/`. IDE JUnit runs use the relative fallback `target/allure-results`; for full Playwright/framework steps, add the same AspectJ `-javaagent` VM option from `pom.xml` to the IDE's JUnit configuration. Merely adding dependencies does not activate weaving in the IDE.

Configuration references: [JUnit Jupiter](https://allurereport.org/docs/junit-jupiter/), [Playwright Java](https://allurereport.org/docs/playwright-java/), [AspectJ Java 21 support](https://eclipse.dev/aspectj/doc/latest/release/README-1.9.21.html).
