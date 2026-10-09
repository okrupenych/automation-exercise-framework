package framework.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class NavigationBar {

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
        linkLogo.click();
    }

    public void openHome() {
        linkHome.click();
    }

    public void openProducts() {
        linkProducts.click();
    }

    public void openCart() {
        linkCart.click();
    }

    public void openSignupLogin() {
        linkSignupLogin.click();
    }

    public void openTestCases() {
        linkTestCases.click();
    }

    public void openApiTesting() {
        linkApiTesting.click();
    }

    public void openVideoTutorials() {
        linkVideoTutorials.click();
    }

    public void openContactUs() {
        linkContactUs.click();
    }
}
