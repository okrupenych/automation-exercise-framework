package framework.browser;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import framework.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverlayHandler {

    private static final Logger log = LoggerFactory.getLogger(OverlayHandler.class);

    private final Page page;

    public OverlayHandler(Page page) {
        this.page = page;
    }

    private boolean dismissingPopup;
    private long operationDeadline = Long.MAX_VALUE;
    private long dismissalCount;
    private String lastDismissed = "none";

    public long getDismissalCount() { return dismissalCount; }
    public String getLastDismissed() { return lastDismissed; }

    public void withDeadline(long deadline, Runnable action) {
        long previous = operationDeadline;
        operationDeadline = deadline;
        try {
            action.run();
        } finally {
            operationDeadline = previous;
        }
    }

    private void recordDismissal(String name) {
        dismissalCount++;
        lastDismissed = name;
        log.debug("{} dismissed", name);
    }

    public void dismissIfPresent(double timeoutMs) {
        var popup = page.locator("ins[data-vignette-loaded='true'] iframe")
                .contentFrame().locator("#dismiss-button");
        if (popup.isVisible()) {
            dismissPopup(popup, timeoutMs);
        }
        var anchor = page.locator("ins.adsbygoogle[data-anchor-status='displayed'][data-anchor-shown='true']");
        if (anchor.isVisible()) {
            dismissAnchor();
        }
        for (String side : new String[]{"left", "right"}) {
            var rail = page.locator("." + side + "-side-rail-dismiss-btn");
            if (rail.isVisible()) {
                dismissRail(rail, side);
            }
        }
    }

    private void dismissPopup(Locator button, double timeoutMs) {
        timeoutMs = Math.clamp(
                (operationDeadline - System.nanoTime()) / 1_000_000.0,
                Math.min(1, timeoutMs), timeoutMs);
        long deadline = System.nanoTime() + (long) (timeoutMs * 1_000_000);
        if (dismissingPopup) {
            return;
        }
        dismissingPopup = true;
        try {
            button.click(new Locator.ClickOptions().setTimeout(timeoutMs));
            while (button.isVisible()) {
                double remaining = (deadline - System.nanoTime()) / 1_000_000.0;
                if (remaining <= 0) {
                    throw new com.microsoft.playwright.TimeoutError("AD popup did not close");
                }
                page.waitForTimeout(Math.min(50, remaining));
            }
        } finally {
            dismissingPopup = false;
        }
        recordDismissal("AD popup");
    }

    private void dismissAnchor() {
        // This overlay requires two clicks even when dismissed manually.
        page.locator(".grippy-host").evaluate("el => el.click()");
        page.locator(".grippy-host").evaluate("el => el.click()");
        recordDismissal("AD anchor");
    }

    private void dismissRail(Locator button, String side) {
        button.evaluate("el => el.click()");
        recordDismissal(side + " AD rail");
    }

    public void registerHandlers() {
        log.trace("Registering overlay locator handlers");

        // AD popup autoclose
        page.addLocatorHandler(
                page.locator("ins[data-vignette-loaded='true'] iframe")
                        .contentFrame()
                        .locator("#dismiss-button"),
                dismissButton -> {
                    if (dismissingPopup) {
                        return;
                    }
                    log.trace("AD popup detected");
                    dismissPopup(dismissButton, Config.getBrowserTimeout() == 0
                            ? 15000 : Config.getBrowserTimeout());
                    log.trace("AD popup dismissed");
                },
                new Page.AddLocatorHandlerOptions().setNoWaitAfter(true)
        );

        // AD panels
        page.addLocatorHandler(
                page.locator("ins.adsbygoogle[data-anchor-status='displayed'][data-anchor-shown='true']"),
                ad -> {
                    log.trace("AD anchor overlay detected");

                    // The ad overlay requires two separate clicks to close, even manually.
                    dismissAnchor();

                    log.trace("AD anchor overlay dismiss clicks completed");
                },
                new Page.AddLocatorHandlerOptions().setNoWaitAfter(true)
        );

        page.addLocatorHandler(
                page.locator(".left-side-rail-dismiss-btn"),
                dismissButton -> {
                    log.trace("Left AD rail detected");
                    dismissRail(dismissButton, "left");
                    log.trace("Left AD rail dismiss click completed");
                },
                new Page.AddLocatorHandlerOptions().setNoWaitAfter(true)
        );

        page.addLocatorHandler(
                page.locator(".right-side-rail-dismiss-btn"),
                dismissButton -> {
                    log.trace("Right AD rail detected");
                    dismissRail(dismissButton, "right");
                    log.trace("Right AD rail dismiss click completed");
                },
                new Page.AddLocatorHandlerOptions().setNoWaitAfter(true)
        );

        log.debug("Overlay handlers registered");
    }

}
