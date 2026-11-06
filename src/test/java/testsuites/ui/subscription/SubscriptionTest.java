package testsuites.ui.subscription;

import assertions.PageAssertions;
import base.BaseTest;
import data.AccountData;
import flows.AccountFlows;
import framework.api.ApiClient;
import framework.api.ApiSession;
import framework.api.AutomationExerciseApi;
import framework.api.models.MessageResponse;
import framework.pages.ApiTestingPage;
import framework.pages.BasePage;
import framework.pages.CartPage;
import framework.pages.ContactUsPage;
import framework.pages.HomePage;
import framework.pages.ProductsPage;
import framework.pages.SignupLoginPage;
import framework.pages.TestCasesPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("ui")
@Tag("subscription")
public class SubscriptionTest extends BaseTest {

    private HomePage homePage;
    private ProductsPage productsPage;
    private CartPage cartPage;
    private SignupLoginPage signupLoginPage;
    private TestCasesPage testCasesPage;
    private ApiTestingPage apiTestingPage;
    private ContactUsPage contactUsPage;
    private AccountFlows accountFlows;
    private AccountData account;
    private boolean accountCreationAttempted;
    private boolean accountCreated;

    @BeforeEach
    void setUpPages() {
        accountCreationAttempted = false;
        accountCreated = false;
        homePage = new HomePage(browserSession.getPage());
        productsPage = new ProductsPage(browserSession.getPage());
        cartPage = new CartPage(browserSession.getPage());
        signupLoginPage = new SignupLoginPage(browserSession.getPage());
        testCasesPage = new TestCasesPage(browserSession.getPage());
        apiTestingPage = new ApiTestingPage(browserSession.getPage());
        contactUsPage = new ContactUsPage(browserSession.getPage());
        accountFlows = new AccountFlows(browserSession.getPage());
    }

    @AfterEach
    void deleteTestAccount() {
        if (!accountCreationAttempted) {
            return;
        }

        try (ApiSession session = new ApiSession()) {
            AutomationExerciseApi api = new AutomationExerciseApi(
                    new ApiClient(session.getRequestContext())
            );

            var result = api.deleteAccount(account.credentials());

            assertEquals(
                    200,
                    result.httpStatus(),
                    "Unexpected cleanup HTTP status: " + result.body()
            );

            Integer responseCode =
                    result.as(MessageResponse.class).responseCode();

            if (accountCreated) {
                assertEquals(
                        Integer.valueOf(200),
                        responseCode,
                        "Failed to delete the test account: " + result.body()
                );
            } else {
                assertTrue(
                        Integer.valueOf(200).equals(responseCode)
                                || Integer.valueOf(404).equals(responseCode),
                        "Unexpected cleanup response: " + result.body()
                );
            }
        }
    }


    @Test
    @DisplayName("Footer subscription on the home page shows a success confirmation")
    void shouldConfirmSubscriptionOnHomePage() {
        homePage.open();
        PageAssertions.assertPageReady(browserSession.getPage(), homePage);

        assertSuccessfulSubscription(homePage);
    }

    @Test
    @DisplayName("Footer subscription on the products page shows a success confirmation")
    void shouldConfirmSubscriptionOnProductsPage() {
        productsPage.open();
        PageAssertions.assertPageReady(browserSession.getPage(), productsPage);

        assertSuccessfulSubscription(productsPage);
    }

    @Test
    @DisplayName("Footer subscription on the cart page shows a success confirmation")
    void shouldConfirmSubscriptionOnCartPage() {
        cartPage.open();
        PageAssertions.assertPageReady(browserSession.getPage(), cartPage);

        assertSuccessfulSubscription(cartPage);
    }

    @Test
    @DisplayName("Footer subscription on the signup / login page shows a success confirmation")
    void shouldConfirmSubscriptionOnSignupLoginPage() {
        signupLoginPage.open();
        PageAssertions.assertPageReady(browserSession.getPage(), signupLoginPage);

        assertSuccessfulSubscription(signupLoginPage);
    }

    @Test
    @DisplayName("Footer subscription on the test cases page shows a success confirmation")
    void shouldConfirmSubscriptionOnTestCasesPage() {
        testCasesPage.open();
        PageAssertions.assertPageReady(browserSession.getPage(), testCasesPage);

        assertSuccessfulSubscription(testCasesPage);
    }

    @Test
    @DisplayName("Footer subscription on the API testing page shows a success confirmation")
    void shouldConfirmSubscriptionOnApiTestingPage() {
        apiTestingPage.open();
        PageAssertions.assertPageReady(browserSession.getPage(), apiTestingPage);

        assertSuccessfulSubscription(apiTestingPage);
    }

    @Test
    @DisplayName("Footer subscription on the contact us page shows a success confirmation")
    void shouldConfirmSubscriptionOnContactUsPage() {
        contactUsPage.open();
        PageAssertions.assertPageReady(browserSession.getPage(), contactUsPage);

        assertSuccessfulSubscription(contactUsPage);
    }

    @Test
    @DisplayName("Footer subscription on the home page succeeds while logged in")
    void shouldConfirmSubscriptionWhileLoggedIn() {
        createTestAccount();
        signupLoginPage.open();
        PageAssertions.assertPageReady(browserSession.getPage(), signupLoginPage);

        accountFlows.login(account.credentials(), account.fullName());

        homePage.open();
        PageAssertions.assertPageReady(browserSession.getPage(), homePage);
        PageAssertions.assertElementVisibleWithText(
                homePage.navigationBar().getLoggedInAs(),
                "Logged in as " + account.fullName()
        );

        assertSuccessfulSubscription(homePage);
    }

    @Test
    @DisplayName("Subscription requires an email")
    @Tag("negative")
    void shouldRejectSubscriptionWithoutEmail() {
        homePage.open();
        PageAssertions.assertPageReady(browserSession.getPage(), homePage);
        homePage.scrollToBottom();

        homePage.footer().fillEmail("");
        homePage.footer().clickSubscribe();

        PageAssertions.assertFieldValidationError(
                homePage.footer().getEmail(),
                PageAssertions.ValidationError.VALUE_MISSING
        );
    }

    @Test
    @DisplayName("Subscription rejects an invalid email format")
    @Tag("negative")
    void shouldRejectSubscriptionWithInvalidEmail() {
        homePage.open();
        PageAssertions.assertPageReady(browserSession.getPage(), homePage);
        homePage.scrollToBottom();

        homePage.footer().fillEmail("invalid-email");
        homePage.footer().clickSubscribe();

        PageAssertions.assertFieldValidationError(
                homePage.footer().getEmail(),
                PageAssertions.ValidationError.TYPE_MISMATCH
        );
    }


//----Private helpers----

    private void assertSuccessfulSubscription(BasePage targetPage) {
        targetPage.scrollToBottom();
        targetPage.footer().fillEmail("subscriber-" + UUID.randomUUID() + "@example.com");
        targetPage.footer().clickSubscribe();

        PageAssertions.assertElementVisibleWithText(
                targetPage.footer().getSubscriptionMessage(),
                "You have been successfully subscribed!"
        );
    }

    private void createTestAccount() {
        account = new AccountData(
                "Mr", "Test", "User",
                "subscription-login-" + UUID.randomUUID() + "@example.com",
                "TestPassword123!",
                "15", "6", "1990",
                "Test Company",
                "123 Test Street", "Apartment 4",
                "United States", "California", "Los Angeles",
                "90001", "2025550100", null
        );

        try (ApiSession session = new ApiSession()) {
            AutomationExerciseApi api = new AutomationExerciseApi(
                    new ApiClient(session.getRequestContext())
            );

            accountCreationAttempted = true;
            var result = api.createAccount(account);

            assertEquals(
                    200,
                    result.httpStatus(),
                    "Unexpected setup HTTP status: " + result.body()
            );
            assertEquals(
                    Integer.valueOf(201),
                    result.as(MessageResponse.class).responseCode(),
                    "Failed to create the test account: " + result.body()
            );

            accountCreated = true;
        }
    }

}
