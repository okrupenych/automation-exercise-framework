package framework.browser;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.options.ColorScheme;
import com.microsoft.playwright.Page;
import framework.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowserSession {

    private static final Logger log = LoggerFactory.getLogger(BrowserSession.class);

    private final BrowserContext context;
    private final Page page;
    private final OverlayHandler overlayHandler;

    public BrowserSession(Browser browser) {
        Browser.NewContextOptions options = new Browser.NewContextOptions()
                .setViewportSize(Config.getViewportWidth(), Config.getViewportHeight())
                .setLocale(Config.getLocale())
                .setTimezoneId(Config.getTimezone())
                .setColorScheme(switch (Config.getColorScheme()) {
                    case "dark" -> ColorScheme.DARK;
                    case "no-preference" -> ColorScheme.NO_PREFERENCE;
                    default -> ColorScheme.LIGHT;
                });
        int timeout = Config.getBrowserTimeout();
        int navigationTimeout = Config.getNavigationTimeout();
        context = browser.newContext(options);
        try {
            context.setDefaultTimeout(timeout);
            context.setDefaultNavigationTimeout(navigationTimeout);
            page = context.newPage();
            overlayHandler = new OverlayHandler(page);
            overlayHandler.registerHandlers();
        } catch (RuntimeException e) {
            context.close();
            throw e;
        }

        log.debug("Browser page initialized");
    }

    public OverlayHandler getOverlayHandler() { return overlayHandler; }

    public Page getPage() {
        return page;
    }

    public void close() {
        if (context != null) {
            log.debug("Closing browser context");
            context.close();
            log.trace("Browser context closed");
        }
    }

}
