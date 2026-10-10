package framework.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NavigationBar {

    private static final Logger log = LoggerFactory.getLogger(NavigationBar.class);

    private final Locator linkLogo;
    private final Locator linkHome;
    private final Locator linkProducts;
    private final Locator linkCart;
    private final Locator linkSignupLogin;
    private final Locator linkTestCases;
    private final Locator linkApiTesting;
    private final Locator linkVideoTutorials;
    private final Locator linkContactUs;

    public NavigationBar(Page page) {
        Locator rootNavigationBar = page.locator("header");

        linkLogo = page.locator(".logo a");
        linkHome = rootNavigationBar.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Home"));
        linkProducts = rootNavigationBar.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Products"));
        linkCart = rootNavigationBar.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Cart"));
        linkSignupLogin = rootNavigationBar.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Signup / Login"));
        linkTestCases = rootNavigationBar.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Test Cases"));
        linkApiTesting = rootNavigationBar.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("API Testing"));
        linkVideoTutorials = rootNavigationBar.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Video Tutorials"));
        linkContactUs = rootNavigationBar.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Contact us"));
    }

    
//----Public interface----

    public void clickLogo() {
        log.info("Clicking site logo");
        linkLogo.click();
    }

    public void openHome() {
        log.info("Clicking \"Home\" link");
        linkHome.click();
    }

    public void openProducts() {
        log.info("Clicking \"Products\" link");
        linkProducts.click();
    }

    public void openCart() {
        log.info("Clicking \"Cart\" link");
        linkCart.click();
    }

    public void openSignupLogin() {
        log.info("Clicking \"Signup/Login\" link");
        linkSignupLogin.click();
    }

    public void openTestCases() {
        log.info("Clicking \"Test Cases\" link");
        linkTestCases.click();
    }

    public void openApiTesting() {
        log.info("Clicking \"API Testing\" link");
        linkApiTesting.click();
    }

    public void openVideoTutorials() {
        log.info("Clicking \"Video Tutorials\" link");
        linkVideoTutorials.click();
    }

    public void openContactUs() {
        log.info("Clicking \"Contact Us\" link");
        linkContactUs.click();
    }
}
