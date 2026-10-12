package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
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

    private final NavigationBar cmpNavigationBar;
    private final Footer cmpFooter;

    protected BasePage(Page page) {
        this.page = page;
        cmpNavigationBar = new NavigationBar(page);
        cmpFooter = new Footer(page);
    }


//----Public interface----

    public abstract String getTargetUrl();

    public abstract List<Locator> getRequiredElements();

    public void open() {
        String url = getTargetUrl();

        log.info("Opening page: {}", url);
        page.navigate(url);
        log.debug("Page navigation completed: {}", url);
    }


    // Built-in components
    public NavigationBar navigationBar() {
        return cmpNavigationBar;
    }

    public Footer footer() {
        return cmpFooter;
    }

}
