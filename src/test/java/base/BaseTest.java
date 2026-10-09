package base;

import framework.browser.BrowserManager;
import framework.browser.BrowserSession;

import org.junit.jupiter.api.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {
    protected BrowserManager browserManager;
    protected BrowserSession browserSession;

    @BeforeAll
    protected void setUpBrowser() {
        browserManager = new BrowserManager();
        browserManager.startBrowser();
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
        browserManager.stopBrowser();
    }
}
