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
import framework.pages.HomePage;
import framework.pages.OrderPlacedPage;
import framework.pages.PaymentPage;
import framework.pages.ProductsPage;
import framework.pages.SignupLoginPage;
import framework.utils.PriceUtils;
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
public class PaymentTest extends BaseTest {

    private PaymentPage paymentPage;
    private OrderPlacedPage orderPlacedPage;
    private AccountData account;

    private boolean accountCreationAttempted;
    private boolean accountCreated;

    @BeforeEach
    void setUpPayment() {
        accountCreationAttempted = false;
        accountCreated = false;

        account = new AccountData(
                "Mr",
                "Test",
                "User",
                "payment-" + UUID.randomUUID() + "@example.com",
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
        CheckoutPage checkoutPage = new CheckoutPage(page);
        paymentPage = new PaymentPage(page);
        orderPlacedPage = new OrderPlacedPage(page);

        signupLoginPage.open();
        PageAssertions.assertPageReady(page, signupLoginPage);

        new AccountFlows(page).login(
                account.credentials(),
                account.fullName()
        );

        signupLoginPage.navigationBar().clickProducts();
        PageAssertions.assertPageReady(page, productsPage);

        productsPage.addProductsToCart(List.of("Blue Top"));
        productsPage.navigationBar().clickCart();

        PageAssertions.assertPageReady(page, cartPage);
        PageAssertions.assertElementVisible(
                cartPage.cart().itemByName("Blue Top").getName()
        );
        PageAssertions.assertCartContents(
                cartPage.cart(),
                List.of(
                        new ExpectedCartItem(
                                "Blue Top", "Rs. 500", 1, PriceUtils.multiply("Rs. 500", 1)
                        )
                )
        );

        cartPage.clickProceedToCheckout();
        PageAssertions.assertPageReady(page, checkoutPage);

        checkoutPage.clickPlaceOrder();
        PageAssertions.assertPageReady(page, paymentPage);
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
    @DisplayName("Payment confirms the order and allows returning home")
    @Tag("smoke")
    void shouldConfirmOrderAfterPayment() {
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

        var invoice = orderPlacedPage.downloadInvoice();
        PageAssertions.assertDownloadSuccessful(invoice);

        orderPlacedPage.clickContinueButton();

        HomePage homePage = new HomePage(browserSession.getPage());
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                homePage
        );
        PageAssertions.assertElementVisibleWithText(
                homePage.navigationBar().getLoggedInAs(),
                "Logged in as " + account.fullName()
        );
    }

}
