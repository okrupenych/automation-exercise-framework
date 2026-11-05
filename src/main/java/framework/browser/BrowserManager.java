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
        String engine = Config.getBrowserType();
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(Config.isHeadless())
                .setTimeout(Config.getLaunchTimeout())
                .setSlowMo(Config.getSlowMo());
        log.info("Starting {} browser", engine);
        playwright = Playwright.create();
        try {
            BrowserType type = switch (engine) {
                case "firefox" -> playwright.firefox();
                case "webkit" -> playwright.webkit();
                default -> playwright.chromium();
            };
            browser = type.launch(options);
        } catch (RuntimeException e) {
            playwright.close();
            playwright = null;
            throw e;
        }
    }

    public void stopBrowser() {
        log.info("Stopping browser");

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
