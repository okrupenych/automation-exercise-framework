package testsuites.ui.checkout;

import assertions.PageAssertions;
import base.BaseTest;
import data.CartItemData;
import framework.pages.CartPage;
import framework.pages.ProductsPage;
import framework.pages.SignupLoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

@Tag("ui")
@Tag("checkout")
public class GuestCheckoutTest extends BaseTest {

    private CartPage cartPage;
    private SignupLoginPage signupLoginPage;

    private final List<CartItemData> expectedItems = List.of(
            new CartItemData(1, "Blue Top", "Rs. 500", 1)
    );

    @BeforeEach
    void setUpGuestCart() {
        ProductsPage productsPage =
                new ProductsPage(browserSession.getPage());
        cartPage = new CartPage(browserSession.getPage());
        signupLoginPage = new SignupLoginPage(browserSession.getPage());

        productsPage.open();
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                productsPage
        );

        productsPage.addCartItems(expectedItems);
        productsPage.navigationBar().clickCart();

        assertCartContents();
        PageAssertions.assertElementAbsent(
                cartPage.navigationBar().getLoggedInAs()
        );
    }


    @Test
    @DisplayName("Guest checkout offers navigation to the login page")
    void shouldOfferLoginWhenGuestProceedsToCheckout() {
        cartPage.clickProceedToCheckout();

        assertLoginPromptVisible();

        PageAssertions.assertElementVisibleWithText(
                cartPage.modal().getProceedLink(),
                "Register / Login"
        );
        cartPage.modal().clickProceedLink();

        PageAssertions.assertPageReady(
                browserSession.getPage(),
                signupLoginPage
        );
    }

    @Test
    @DisplayName("Closing the guest checkout prompt preserves the cart")
    void shouldPreserveCartWhenCheckoutPromptIsDismissed() {
        cartPage.clickProceedToCheckout();

        assertLoginPromptVisible();

        cartPage.modal().clickDismissButton();
        cartPage.modal().waitUntilHidden();

        assertCartContents();
        PageAssertions.assertElementAbsent(
                cartPage.navigationBar().getLoggedInAs()
        );
    }


//----Private helpers----

    private void assertLoginPromptVisible() {
        cartPage.modal().waitUntilVisible();

        PageAssertions.assertPageElementsVisible(
                cartPage.modal().getRequiredElements()
        );
        PageAssertions.assertPageUrl(
                browserSession.getPage(),
                cartPage.getTargetUrl()
        );
    }

    private void assertCartContents() {
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                cartPage
        );
        PageAssertions.assertCartContents(cartPage.cart(), expectedItems);
    }

}