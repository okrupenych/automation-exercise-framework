package testsuites.ui.checkout;

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
import framework.pages.CheckoutPage;
import framework.pages.OrderPlacedPage;
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
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("ui")
@Tag("checkout")
public class RegistrationCheckoutTest extends BaseTest {

    private ProductsPage productsPage;
    private CartPage cartPage;
    private SignupLoginPage signupLoginPage;
    private CheckoutPage checkoutPage;
    private PaymentPage paymentPage;
    private OrderPlacedPage orderPlacedPage;
    private AccountFlows accountFlows;

    private AccountData account;
    private boolean registrationAttempted;
    private boolean registrationCompleted;

    private final List<CartItemData> expectedItems = List.of(
            new CartItemData(1, "Blue Top", "Rs. 500", 1)
    );

    @BeforeEach
    void setUpRegistrationCheckout() {
        registrationAttempted = false;
        registrationCompleted = false;

        account = new AccountData(
                "Mr",
                "Test",
                "User",
                "registration-checkout-" + UUID.randomUUID() + "@example.com",
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
                new AccountData.PaymentDetails(
                        "Test User",
                        "4111111111111111",
                        "123",
                        "12",
                        "2030"
                )
        );

        var page = browserSession.getPage();
        productsPage = new ProductsPage(page);
        cartPage = new CartPage(page);
        signupLoginPage = new SignupLoginPage(page);
        checkoutPage = new CheckoutPage(page);
        paymentPage = new PaymentPage(page);
        orderPlacedPage = new OrderPlacedPage(page);
        accountFlows = new AccountFlows(page);
    }

    @AfterEach
    void deleteTestAccount() {
        if (!registrationAttempted) {
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
                    "Unexpected HTTP status during account cleanup: " + result.body()
            );

            MessageResponse response = result.as(MessageResponse.class);

            if (registrationCompleted) {
                assertEquals(
                        Integer.valueOf(200),
                        response.responseCode(),
                        "Failed to delete the registered account: " + result.body()
                );
            } else {
                assertTrue(
                        Integer.valueOf(200).equals(response.responseCode())
                                || Integer.valueOf(404).equals(response.responseCode()),
                        "Unexpected account cleanup response: " + result.body()
                );
            }
        }
    }


    @Test
    @DisplayName("A guest can register during checkout and complete a purchase")
    void shouldPurchaseAfterRegisteringDuringCheckout() {
        productsPage.open();
        PageAssertions.assertPageReady(browserSession.getPage(), productsPage);

        productsPage.addCartItems(expectedItems);
        productsPage.navigationBar().clickCart();

        assertCartContents();
        PageAssertions.assertElementAbsent(cartPage.navigationBar().getLoggedInAs());

        cartPage.clickProceedToCheckout();
        cartPage.modal().waitUntilVisible();

        PageAssertions.assertPageElementsVisible(cartPage.modal().getRequiredElements());
        PageAssertions.assertPageUrl(browserSession.getPage(), cartPage.getTargetUrl());
        PageAssertions.assertElementVisibleWithText(
                cartPage.modal().getProceedLink(),
                "Register / Login"
        );
        cartPage.modal().clickProceedLink();

        PageAssertions.assertPageReady(browserSession.getPage(), signupLoginPage);

        registrationAttempted = true;
        accountFlows.register(account, false, false);
        registrationCompleted = true;

        signupLoginPage.navigationBar().clickCart();
        assertCartContents();

        completePurchase();
    }

    @Test
    @DisplayName("A new user can register before checkout and complete a purchase")
    void shouldPurchaseAfterRegisteringBeforeCheckout() {
        signupLoginPage.open();
        PageAssertions.assertPageReady(browserSession.getPage(), signupLoginPage);

        registrationAttempted = true;
        accountFlows.register(account, false, false);
        registrationCompleted = true;

        signupLoginPage.navigationBar().clickProducts();
        PageAssertions.assertPageReady(browserSession.getPage(), productsPage);

        productsPage.addCartItems(expectedItems);
        productsPage.navigationBar().clickCart();

        assertCartContents();

        completePurchase();
    }


//----Private helpers----

    private void assertCartContents() {
        PageAssertions.assertPageReady(browserSession.getPage(), cartPage);
        PageAssertions.assertCartContents(cartPage.cart(), expectedItems);
    }

    private void completePurchase() {
        cartPage.clickProceedToCheckout();
        PageAssertions.assertPageReady(browserSession.getPage(), checkoutPage);

        PageAssertions.assertDeliveryAddress(checkoutPage, account);
        PageAssertions.assertBillingAddress(checkoutPage, account);
        PageAssertions.assertCartContents(checkoutPage.cart(), expectedItems);

        checkoutPage.clickPlaceOrder();
        PageAssertions.assertPageReady(browserSession.getPage(), paymentPage);

        paymentPage.fillPaymentDetails(account.payment());
        paymentPage.clickPayAndConfirmOrder();

        PageAssertions.assertPageReady(
                browserSession.getPage(),
                orderPlacedPage,
                Pattern.compile(".*/payment_done/\\d+/?$")
        );
        PageAssertions.assertElementVisibleWithText(
                orderPlacedPage.getHeader(),
                "Order Placed!"
        );
    }

}
