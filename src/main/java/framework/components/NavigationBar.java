package framework.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
    private final Locator linkLogout;
    private final Locator linkDeleteAccount;
    private final Locator lblLoggedInAs;

    public NavigationBar(Page page) {
        Locator rootNavigationBar = page.locator("ul.navbar-nav");

        linkLogo = page.locator(".logo a");
        linkHome = rootNavigationBar.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Home"));
        linkProducts = rootNavigationBar.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Products"));
        linkCart = rootNavigationBar.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Cart"));
        linkSignupLogin = rootNavigationBar.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Signup / Login"));
        linkTestCases = rootNavigationBar.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Test Cases"));
        linkApiTesting = rootNavigationBar.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("API Testing"));
        linkVideoTutorials = rootNavigationBar.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Video Tutorials"));
        linkContactUs = rootNavigationBar.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Contact us"));

        linkLogout = rootNavigationBar.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Logout"));
        linkDeleteAccount = rootNavigationBar.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("Delete Account"));
        lblLoggedInAs = rootNavigationBar.locator("a:not([href]):has-text('Logged in as')");
    }


    
//----Public interface----

    public List<Locator> getRequiredElements() {
        return List.of(
                linkLogo,
                linkHome,
                linkProducts,
                linkCart,
                linkTestCases,
                linkApiTesting,
                linkVideoTutorials,
                linkContactUs
        );
    }

    public void clickLogo() {
        log.info("Clicking site logo");
        linkLogo.click();
    }

    public void clickHome() {
        log.info("Clicking \"Home\" link");
        linkHome.click();
    }

    public void clickProducts() {
        log.info("Clicking \"Products\" link");
        linkProducts.click();
    }

    public void clickCart() {
        log.info("Clicking \"Cart\" link");
        linkCart.click();
    }

    public void clickSignupLogin() {
        log.info("Clicking \"Signup/Login\" link");
        linkSignupLogin.click();
    }

    public void clickTestCases() {
        log.info("Clicking \"Test Cases\" link");
        linkTestCases.click();
    }

    public void clickApiTesting() {
        log.info("Clicking \"API Testing\" link");
        linkApiTesting.click();
    }

    public void clickVideoTutorials() {
        log.info("Clicking \"Video Tutorials\" link");
        linkVideoTutorials.click();
    }

    public void clickContactUs() {
        log.info("Clicking \"Contact Us\" link");
        linkContactUs.click();
    }

    public void clickLogout() {
        log.info("Clicking \"Logout\" link");
        linkLogout.click();
    }

    public void clickDeleteAccount() {
        log.info("Clicking \"Delete Account\" link");
        linkDeleteAccount.click();
    }

    public Locator getLoggedInAs() {
        return lblLoggedInAs;
    }

}
