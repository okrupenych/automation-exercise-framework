package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import framework.browser.OverlayHandler;
import framework.components.Footer;
import framework.components.NavigationBar;
import framework.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class BasePage {

    private static final Logger log = LoggerFactory.getLogger(BasePage.class);

    protected static final String BASE_URL = Config.getBaseUrl();

    protected final Page page;
    private final OverlayHandler overlayHandler;

    private final NavigationBar cmpNavigationBar;
    private final Footer cmpFooter;
    private final Locator btnScrollUp;

    protected BasePage(Page page) {
        this(page, new OverlayHandler(page));
    }

    protected BasePage(Page page, OverlayHandler overlayHandler) {
        this.page = page;
        this.overlayHandler = overlayHandler;
        cmpNavigationBar = new NavigationBar(page);
        cmpFooter = new Footer(page);
        btnScrollUp = page.locator("#scrollUp");
    }


//----Internal implementation----

    private void performScrollUp(long deadline) {
        int retries = 0;
        overlayHandler.dismissIfPresent(remainingMs(deadline));
        long observedDismissals = overlayHandler.getDismissalCount();
        btnScrollUp.click(new Locator.ClickOptions().setTimeout(remainingMs(deadline)));
        while (System.nanoTime() < deadline) {
            overlayHandler.dismissIfPresent(remainingMs(deadline));
            double scrollY = ((Number) page.evaluate("window.scrollY")).doubleValue();
            if (Math.abs(scrollY) <= 1) {
                return;
            }
            long dismissals = overlayHandler.getDismissalCount();
            if (dismissals > observedDismissals && retries < 2) {
                log.info("Retrying scroll up after {} (retry {})",
                        overlayHandler.getLastDismissed(), ++retries);
                btnScrollUp.click(new Locator.ClickOptions().setTimeout(remainingMs(deadline)));
                observedDismissals = dismissals;
            }
            // Return control regularly to check ads while the scroll animation runs.
            if (System.nanoTime() < deadline) {
                page.waitForTimeout(Math.min(100, remainingMs(deadline)));
            }
        }
        throw new com.microsoft.playwright.TimeoutError("Scroll up failed: scrollY="
                + page.evaluate("window.scrollY") + ", retries=" + retries
                + ", last overlay=" + overlayHandler.getLastDismissed());
    }

    private static double remainingMs(long deadline) {
        double remaining = (deadline - System.nanoTime()) / 1_000_000.0;
        if (remaining <= 0) {
            throw new com.microsoft.playwright.TimeoutError("Scroll up recovery deadline exceeded");
        }
        return remaining;
    }


//----Public interface----

    public abstract String getTargetUrl();

    public abstract List<Locator> getRequiredElements();


    // Common page actions
    public void open() {
        String url = getTargetUrl();

        log.info("Opening page: {}", url);
        page.navigate(url);
        log.debug("Page navigation completed: {}", url);
    }

    public void scrollToBottom() {
        log.info("Scrolling to bottom of page");
        page.evaluate("window.scrollTo({top: document.documentElement.scrollHeight, behavior: 'instant'})");
    }

    public void scrollToTop() {
        log.info("Scrolling to top of page");
        page.evaluate("window.scrollTo({top: 0, behavior: 'instant'})");
    }

    public Locator getScrollUpButton() {
        return btnScrollUp;
    }

    public void clickScrollUp() {
        log.info("Clicking scroll up button");
        int timeout = Config.getBrowserTimeout() == 0 ? 15000 : Config.getBrowserTimeout();
        long deadline = System.nanoTime() + timeout * 1_000_000L;
        try {
            overlayHandler.withDeadline(deadline, () -> performScrollUp(deadline));
        } catch (com.microsoft.playwright.TimeoutError error) {
            log.error("Scroll up timed out: last overlay={}, dismissals={}",
                    overlayHandler.getLastDismissed(), overlayHandler.getDismissalCount());
            throw error;
        }
    }


    // Built-in components
    public NavigationBar navigationBar() {
        return cmpNavigationBar;
    }

    public Footer footer() {
        return cmpFooter;
    }

}
