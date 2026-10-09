package framework.pages;

import com.microsoft.playwright.Page;
import framework.components.Footer;
import framework.components.NavigationBar;
import framework.config.Config;

public abstract class BasePage {

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

    public void open() {
        page.navigate(getTargetUrl());
    }


    // Built-in components
    public NavigationBar navigationBar() {
        return cmpNavigationBar;
    }

    public Footer footer() {
        return cmpFooter;
    }

}
