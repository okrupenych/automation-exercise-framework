package framework.browser;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import framework.config.Config;

public class BrowserManager {

    private Playwright playwright;
    private Browser browser;

    public void startBrowser() {
        playwright = Playwright.create();

        browser = playwright.chromium().launch(
            new BrowserType.LaunchOptions().setHeadless(Config.isHeadless())
        );
    }

    public void stopBrowser() {
        if (browser != null) {
            browser.close();
        }

        if (playwright != null) {
            playwright.close();
        }
    }

    public Browser getBrowser() {
        return browser;
    }

}
