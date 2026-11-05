package testsuites.ui.checkout;

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
import framework.pages.CheckoutPage;
import framework.pages.PaymentPage;
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
@Tag("checkout")
public class CheckoutTest extends BaseTest {

    private CheckoutPage checkoutPage;
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
    void setUpCheckout() {
        accountCreationAttempted = false;
        accountCreated = false;

        account = new AccountData(
                "Mr",
                "Test",
                "User",
                "checkout-" + UUID.randomUUID() + "@example.com",
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

        var page = browserSession.getPage();
        SignupLoginPage signupLoginPage = new SignupLoginPage(page);
        ProductsPage productsPage = new ProductsPage(page);
        CartPage cartPage = new CartPage(page);
        checkoutPage = new CheckoutPage(page);

        signupLoginPage.open();
        PageAssertions.assertPageReady(page, signupLoginPage);

        new AccountFlows(page).login(
                account.credentials(),
                account.fullName()
        );

        signupLoginPage.navigationBar().clickProducts();
        PageAssertions.assertPageReady(page, productsPage);

        productsPage.addProductsToCart(
                List.of("Blue Top", "Blue Top", "Sleeveless Dress")
        );
        productsPage.navigationBar().clickCart();

        PageAssertions.assertPageReady(page, cartPage);

        for (ExpectedCartItem item : expectedItems) {
            PageAssertions.assertElementVisible(
                    cartPage.cart().itemByName(item.name()).getName()
            );
        }
        PageAssertions.assertCartContents(cartPage.cart(), expectedItems);

        cartPage.clickProceedToCheckout();

        PageAssertions.assertPageReady(page, checkoutPage);
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
    @DisplayName("Checkout displays the account's delivery address")
    void shouldDisplayDeliveryAddress() {
        PageAssertions.assertDeliveryAddress(checkoutPage, account);
    }

    @Test
    @DisplayName("Checkout displays the account's billing address")
    void shouldDisplayBillingAddress() {
        PageAssertions.assertBillingAddress(checkoutPage, account);
    }

    @Test
    @DisplayName("Order review preserves cart products, quantities and prices")
    void shouldDisplayCartContentsInOrderReview() {
        for (ExpectedCartItem item : expectedItems) {
            PageAssertions.assertElementVisible(
                    checkoutPage.cart().itemByName(item.name()).getName()
            );
        }

        PageAssertions.assertCartContents(
                checkoutPage.cart(),
                expectedItems
        );
    }

    @Test
    @DisplayName("An order can proceed to payment without a comment")
    void shouldProceedToPaymentWithoutComment() {
        checkoutPage.fillOrderComment("");
        checkoutPage.clickPlaceOrder();

        PaymentPage paymentPage =
                new PaymentPage(browserSession.getPage());

        PageAssertions.assertPageReady(
                browserSession.getPage(),
                paymentPage
        );
    }

}