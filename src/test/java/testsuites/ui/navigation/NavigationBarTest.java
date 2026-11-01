package testsuites.ui.navigation;

import assertions.PageAssertions;
import base.BaseTest;
import com.microsoft.playwright.Page;
import framework.pages.CartPage;
import framework.pages.ContactUsPage;
import framework.pages.HomePage;
import framework.pages.ProductsPage;
import framework.pages.SignupLoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import framework.pages.TestCasesPage;
import framework.pages.ApiTestingPage;

@Tag("ui")
@Tag("navigation")
public class NavigationBarTest extends BaseTest {

    private Page page;
    private HomePage homePage;
    private ProductsPage productsPage;

    @BeforeEach
    void setUpPages() {
        page = browserSession.getPage();
        homePage = new HomePage(page);
        productsPage = new ProductsPage(page);
    }

    @Test
    @DisplayName("Products link opens the catalog")
    @Tag("smoke")
    void shouldOpenProducts() {
        homePage.open();
        PageAssertions.assertPageReady(page, homePage);

        homePage.navigationBar().clickProducts();

        PageAssertions.assertPageReady(page, productsPage);
    }

    @Test
    @DisplayName("Cart link opens the cart")
    void shouldOpenCart() {
        homePage.open();
        PageAssertions.assertPageReady(page, homePage);

        homePage.navigationBar().clickCart();

        PageAssertions.assertPageReady(page, new CartPage(page));
    }

    @Test
    @DisplayName("Signup / Login link opens the authentication page")
    void shouldOpenSignupLogin() {
        homePage.open();
        PageAssertions.assertPageReady(page, homePage);

        homePage.navigationBar().clickSignupLogin();

        PageAssertions.assertPageReady(page, new SignupLoginPage(page));
    }

    @Test
    @DisplayName("Contact Us link opens the contact page")
    void shouldOpenContactUs() {
        homePage.open();
        PageAssertions.assertPageReady(page, homePage);

        homePage.navigationBar().clickContactUs();

        PageAssertions.assertPageReady(page, new ContactUsPage(page));
    }

    @Test
    @DisplayName("Home link returns from the catalog to the home page")
    void shouldReturnHomeViaHomeLink() {
        productsPage.open();
        PageAssertions.assertPageReady(page, productsPage);

        productsPage.navigationBar().clickHome();

        PageAssertions.assertPageReady(page, homePage);
    }

    @Test
    @DisplayName("Site logo returns from the catalog to the home page")
    void shouldReturnHomeViaLogo() {
        productsPage.open();
        PageAssertions.assertPageReady(page, productsPage);

        productsPage.navigationBar().clickLogo();

        PageAssertions.assertPageReady(page, homePage);
    }

    @Test
    @DisplayName("Test Cases link opens the test cases page")
    void shouldOpenTestCases() {
        homePage.open();
        PageAssertions.assertPageReady(page, homePage);

        homePage.navigationBar().clickTestCases();

        PageAssertions.assertPageReady(page, new TestCasesPage(page));
    }

    @Test
    @DisplayName("API Testing link opens the API documentation page")
    void shouldOpenApiTesting() {
        homePage.open();
        PageAssertions.assertPageReady(page, homePage);

        homePage.navigationBar().clickApiTesting();

        PageAssertions.assertPageReady(page, new ApiTestingPage(page));
    }

    @Test
    @DisplayName("Video Tutorials link opens the AutomationExercise YouTube channel")
    void shouldOpenVideoTutorials() {
        homePage.open();
        PageAssertions.assertPageReady(page, homePage);

        homePage.navigationBar().clickVideoTutorials();

        PageAssertions.assertPageUrl(
                page,
                "https://www.youtube.com/c/AutomationExercise"
        );
    }

}