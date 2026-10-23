package framework.browser;

import com.google.gson.JsonObject;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.CDPSession;
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
        context = browser.newContext();
        log.debug("Browser context created");

        page = context.newPage();

        int timeout = Config.getBrowserTimeout();
        page.setDefaultTimeout(timeout);
        log.debug("Default page timeout set to {} ms", timeout);
        if (timeout == 0) {
            log.warn("Default page timeout is disabled; operations may wait indefinitely");
        }

        overlayHandler = new OverlayHandler(page);
        overlayHandler.registerHandlers();

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

    public void maximize() {
        log.debug("Maximizing browser window through CDP");
        CDPSession cdp = context.newCDPSession(page);

        // Getting Chromium window ID
        JsonObject windowInfo = cdp.send("Browser.getWindowForTarget");

        int windowId = windowInfo
                .get("windowId")
                .getAsInt();
        log.trace("Resolved browser window ID {}", windowId);

        // Maximizing native window
        JsonObject bounds = new JsonObject();
        bounds.addProperty("windowState", "maximized");

        JsonObject params = new JsonObject();
        params.addProperty("windowId", windowId);
        params.add("bounds", bounds);

        cdp.send("Browser.setWindowBounds", params);

        // Getting real size of maximized window
        JsonObject windowParams = new JsonObject();
        windowParams.addProperty("windowId", windowId);

        JsonObject windowBounds = cdp.send("Browser.getWindowBounds", windowParams);
        JsonObject actualBounds = windowBounds.getAsJsonObject("bounds");

        int width = actualBounds.get("width").getAsInt();
        int height = actualBounds.get("height").getAsInt();

        // Setting viewport of current Page
        page.setViewportSize(width, height);

        // Maximizing browser window
        cdp.send("Browser.setWindowBounds", params);
        log.debug("Browser window maximized with viewport {} x {}", width, height);
    }

}
