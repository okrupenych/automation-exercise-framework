package testsuites.ui.cart;

import assertions.PageAssertions;
import base.BaseTest;
import data.AccountData;
import data.CartItemData;
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

    private final List<CartItemData> expectedItems = List.of(
            new CartItemData(1, "Blue Top", "Rs. 500", 2),
            new CartItemData(3, "Sleeveless Dress", "Rs. 1000", 1)
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

        productsPage.addCartItems(expectedItems);
        productsPage.navigationBar().clickCart();

        assertExpectedCartContents(expectedItems);

        cartPage.navigationBar().clickSignupLogin();
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                signupLoginPage
        );

        accountFlows.login(account.credentials(), account.fullName());
        signupLoginPage.navigationBar().clickCart();

        assertExpectedCartContents(expectedItems);
        PageAssertions.assertElementVisibleWithText(
                cartPage.navigationBar().getLoggedInAs(),
                "Logged in as " + account.fullName()
        );
    }


    @Test
    @DisplayName("Guest cart contents from search are preserved after login")
    void shouldPreserveSearchedProductInGuestCartAfterLogin() {
        List<CartItemData> searchedItems = List.of(
                new CartItemData(1, "Blue Top", "Rs. 500", 1)
        );

        productsPage.open();
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                productsPage
        );

        productsPage.searchProducts(searchedItems.getFirst().name());

        PageAssertions.assertElementVisibleWithText(
                productsPage.getHeader(),
                "Searched Products"
        );
        PageAssertions.assertElementVisibleWithText(
                productsPage.productGrid().itemByIndex(0).getName(),
                searchedItems.getFirst().name()
        );
        PageAssertions.assertItemsCount(
                productsPage.productGrid().getAllItems(),
                searchedItems.size()
        );

        productsPage.addCartItems(searchedItems);
        productsPage.navigationBar().clickCart();

        assertExpectedCartContents(searchedItems);

        cartPage.navigationBar().clickSignupLogin();
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                signupLoginPage
        );

        accountFlows.login(account.credentials(), account.fullName());
        signupLoginPage.navigationBar().clickCart();

        assertExpectedCartContents(searchedItems);
        PageAssertions.assertElementVisibleWithText(
                cartPage.navigationBar().getLoggedInAs(),
                "Logged in as " + account.fullName()
        );
    }


//----Private helpers----

    private void assertExpectedCartContents(List<CartItemData> expectedItems) {
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                cartPage
        );

        PageAssertions.assertCartContents(cartPage.cart(), expectedItems);
    }

}
