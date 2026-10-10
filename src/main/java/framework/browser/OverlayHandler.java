package framework.browser;

import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverlayHandler {

    private static final Logger log = LoggerFactory.getLogger(OverlayHandler.class);

    private final Page page;

    public OverlayHandler(Page page) {
        this.page = page;
    }

    public void registerHandlers() {
        log.trace("Registering overlay locator handlers");

        // AD popup autoclose
        page.addLocatorHandler(
                page.locator("ins[data-vignette-loaded='true'] iframe")
                        .contentFrame()
                        .locator("#dismiss-button"),
                dismissButton -> {
                    log.trace("AD popup detected");
                    dismissButton.click();
                    log.trace("AD popup dismissed");
                }
        );

        // AD panels
        page.addLocatorHandler(
                page.locator("ins.adsbygoogle[data-anchor-status='displayed'][data-anchor-shown='true']"),
                ad -> {
                    log.trace("AD anchor overlay detected");

                    // The ad overlay requires two separate clicks to close, even manually.
                    page.locator(".grippy-host").evaluate("el => el.click()");
                    page.locator(".grippy-host").evaluate("el => el.click()");

                    log.trace("AD anchor overlay dismiss clicks completed");
                },
                new Page.AddLocatorHandlerOptions().setNoWaitAfter(true)
        );

        page.addLocatorHandler(
                page.locator(".left-side-rail-dismiss-btn"),
                dismissButton -> {
                    log.trace("Left AD rail detected");
                    dismissButton.evaluate("el => el.click()");
                    log.trace("Left AD rail dismiss click completed");
                },
                new Page.AddLocatorHandlerOptions().setNoWaitAfter(true)
        );

        page.addLocatorHandler(
                page.locator(".right-side-rail-dismiss-btn"),
                dismissButton -> {
                    log.trace("Right AD rail detected");
                    dismissButton.evaluate("el => el.click()");
                    log.trace("Right AD rail dismiss click completed");
                },
                new Page.AddLocatorHandlerOptions().setNoWaitAfter(true)
        );

        log.debug("Overlay handlers registered");
    }

}
