package framework.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Breadcrumbs {

    private static final Logger log = LoggerFactory.getLogger(Breadcrumbs.class);

    private final Locator linkHome;
    private final Locator lblCurrentPage;

    public Breadcrumbs(Page page) {
        Locator rootBreadcrumbs = page.locator("div.breadcrumbs");

        linkHome = rootBreadcrumbs.locator("a[href='/']");
        lblCurrentPage = rootBreadcrumbs.locator("li.active");
    }

    //----Public interface----

    public List<Locator> getRequiredElements() {
        return List.of(
                linkHome,
                lblCurrentPage
        );
    }

    public void clickHome() {
        log.info("Clicking \"Home\" breadcrumb link");
        linkHome.click();
    }

    public Locator getCurrentPage() {
        return lblCurrentPage;
    }

    public Locator getHomeLink() {
        return linkHome;
    }
}