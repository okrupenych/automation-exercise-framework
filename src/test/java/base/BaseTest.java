package base;

import framework.browser.BrowserManager;
import framework.browser.BrowserSession;
import framework.extensions.LogArchiveExtension;
import framework.extensions.TestLoggerExtension;
import framework.extensions.UiAllureExtension;
import io.qameta.allure.playwright.AllurePlaywright;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({
        io.qameta.allure.jupiter.AllureJupiter.class,
        LogArchiveExtension.class,
        TestLoggerExtension.class,
        UiAllureExtension.class
})
public abstract class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    protected BrowserManager browserManager;
    protected BrowserSession browserSession;

    @BeforeAll
    protected void setUpBrowser() {
        com.microsoft.playwright.assertions.PlaywrightAssertions.setDefaultAssertionTimeout(
                framework.config.Config.getAssertionTimeout());
        browserManager = new BrowserManager();
        try {
            browserManager.startBrowser();
        } catch (RuntimeException e) {
            log.error("Failed to start browser", e);
            throw e;
        }
    }

    @BeforeEach
    protected void setUpSession() {
        browserSession = new BrowserSession(browserManager.getBrowser());
        try {
            AllurePlaywright.register(browserSession.getPage());
            AllurePlaywright.startTracing(browserSession.getPage().context());
        } catch (RuntimeException diagnosticFailure) {
            log.warn("Could not start Allure Playwright diagnostics", diagnosticFailure);
        }
    }

    @AfterEach
    protected void tearDownSession() {
        if (browserSession != null) {
            try {
                // Allure's close interceptor saves trace and page logs before context disposal.
                browserSession.close();
            } finally {
                browserSession = null;
            }
        }
    }

    @AfterAll
    protected void tearDownBrowser() {
        try {
            browserManager.stopBrowser();
        } catch (RuntimeException e) {
            log.error("Failed to stop browser", e);
            throw e;
        }
    }


    public BrowserSession getBrowserSession() {
        return browserSession;
    }

}
