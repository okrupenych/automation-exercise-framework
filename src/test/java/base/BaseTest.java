package base;

import framework.browser.BrowserManager;
import framework.browser.BrowserSession;
import framework.extensions.TestLoggerExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(TestLoggerExtension.class)
public abstract class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    protected BrowserManager browserManager;
    protected BrowserSession browserSession;

    @BeforeAll
    protected void setUpBrowser() {
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
    }

    @AfterEach
    protected void tearDownSession() {
        if (browserSession != null) {
            browserSession.close();
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

}
