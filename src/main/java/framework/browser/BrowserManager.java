package framework.browser;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import framework.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowserManager {

    private static final Logger log = LoggerFactory.getLogger(BrowserManager.class);

    private Playwright playwright;
    private Browser browser;

    public void startBrowser() {
        log.info("Starting Chromium browser");

        playwright = Playwright.create();
        log.trace("Playwright runtime created");

        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions().setHeadless(Config.isHeadless())
        );
        log.debug("Chromium browser started");
    }

    public void stopBrowser() {
        log.info("Stopping Chromium browser");

        try {
            if (browser != null) {
                browser.close();
                log.trace("Browser closed");
            }
        } finally {
            if (playwright != null) {
                playwright.close();
                log.trace("Playwright runtime closed");
            }
        }
    }

    public Browser getBrowser() {
        return browser;
    }

}
