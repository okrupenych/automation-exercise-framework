package testsuites.ui.cart;

import assertions.PageAssertions;
import assertions.PageAssertions.ExpectedCartItem;
import base.BaseTest;
import data.AccountData;
import flows.AccountFlows;
import framework.api.ApiClient;
import framework.api.ApiSession;
import framework.api.AutomationExerciseApi;
import framework.api.models.MessageResponse;
import framework.pages.CartPage;
import framework.pages.ProductsPage;
import framework.pages.SignupLoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("ui")
@Tag("cart")
public class CartLoginTest extends BaseTest {

    private ProductsPage productsPage;
    private CartPage cartPage;
    private SignupLoginPage signupLoginPage;
    private AccountFlows accountFlows;
    private AccountData account;

    private boolean accountCreationAttempted;
    private boolean accountCreated;

    private final List<ExpectedCartItem> expectedItems = List.of(
            new ExpectedCartItem(
                    "Blue Top",
                    "Rs. 500",
                    2,
                    "Rs. 1000"
            ),
            new ExpectedCartItem(
                    "Sleeveless Dress",
                    "Rs. 1000",
                    1,
                    "Rs. 1000"
            )
    );

    @BeforeEach
    void setUpAccountAndPages() {
        accountCreationAttempted = false;
        accountCreated = false;

        account = new AccountData(
                "Mr",
                "Test",
                "User",
                "cart-login-" + UUID.randomUUID() + "@example.com",
                "TestPassword123!",
                "15",
                "6",
                "1990",
                "Test Company",
                "123 Test Street",
                "Apartment 4",
                "United States",
                "California",
                "Los Angeles",
                "90001",
                "2025550100",
                null
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

        productsPage = new ProductsPage(browserSession.getPage());
        cartPage = new CartPage(browserSession.getPage());
        signupLoginPage = new SignupLoginPage(browserSession.getPage());
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
    @DisplayName("Guest cart contents are preserved after login")
    void shouldPreserveGuestCartAfterLogin() {
        productsPage.open();
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                productsPage
        );

        productsPage.addProductsToCart(
                List.of("Blue Top", "Blue Top", "Sleeveless Dress")
        );
        productsPage.navigationBar().clickCart();

        assertExpectedCartContents();

        cartPage.navigationBar().clickSignupLogin();
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                signupLoginPage
        );

        accountFlows.login(account.credentials(), account.fullName());
        signupLoginPage.navigationBar().clickCart();

        assertExpectedCartContents();
        PageAssertions.assertElementVisibleWithText(
                cartPage.navigationBar().getLoggedInAs(),
                "Logged in as " + account.fullName()
        );
    }


//----Private helpers----

    private void assertExpectedCartContents() {
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                cartPage
        );

        for (ExpectedCartItem item : expectedItems) {
            PageAssertions.assertElementVisible(
                    cartPage.cart().itemByName(item.name()).getName()
            );
        }

        PageAssertions.assertCartContents(cartPage.cart(), expectedItems);
    }

}