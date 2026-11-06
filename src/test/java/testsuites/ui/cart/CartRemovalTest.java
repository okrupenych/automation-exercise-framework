package testsuites.ui.cart;

import assertions.PageAssertions;
import assertions.PageAssertions.ExpectedCartItem;
import base.BaseTest;
import framework.pages.CartPage;
import framework.pages.ProductsPage;
import framework.utils.PriceUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

@Tag("ui")
@Tag("cart")
public class CartRemovalTest extends BaseTest {

    private ProductsPage productsPage;
    private CartPage cartPage;

    @BeforeEach
    void setUpPages() {
        productsPage = new ProductsPage(browserSession.getPage());
        cartPage = new CartPage(browserSession.getPage());

        productsPage.open();
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                productsPage
        );
    }


    @Test
    @DisplayName("Removing a product preserves the other cart items")
    void shouldRemoveProductWithoutChangingOtherItems() {
        addProductsAndOpenCart("Blue Top", "Sleeveless Dress");

        PageAssertions.assertCartContents(
                cartPage.cart(),
                List.of(
                        new ExpectedCartItem(
                                "Blue Top", "Rs. 500", 1, PriceUtils.multiply("Rs. 500", 1)
                        ),
                        new ExpectedCartItem(
                                "Sleeveless Dress", "Rs. 1000", 1, PriceUtils.multiply("Rs. 1000", 1)
                        )
                )
        );

        cartPage.cart().itemByName("Blue Top").delete();

        assertProductAbsent("Blue Top");
        PageAssertions.assertCartContents(
                cartPage.cart(),
                List.of(
                        new ExpectedCartItem(
                                "Sleeveless Dress", "Rs. 1000", 1, PriceUtils.multiply("Rs. 1000", 1)
                        )
                )
        );
    }

    @Test
    @DisplayName("Removing the last product leaves the cart empty")
    void shouldEmptyCartWhenLastProductIsRemoved() {
        addProductsAndOpenCart("Blue Top");

        PageAssertions.assertCartContents(
                cartPage.cart(),
                List.of(
                        new ExpectedCartItem(
                                "Blue Top", "Rs. 500", 1, PriceUtils.multiply("Rs. 500", 1)
                        )
                )
        );

        cartPage.cart().itemByName("Blue Top").delete();

        assertProductAbsent("Blue Top");
        PageAssertions.assertItemsCount(cartPage.cart().getAllItems(), 0);
    }

    @Test
    @DisplayName("Removing a product deletes all units of that product")
    void shouldRemoveEntireProductLineRegardlessOfQuantity() {
        addProductsAndOpenCart("Blue Top", "Blue Top", "Sleeveless Dress");

        PageAssertions.assertCartContents(
                cartPage.cart(),
                List.of(
                        new ExpectedCartItem(
                                "Blue Top", "Rs. 500", 2, PriceUtils.multiply("Rs. 500", 2)
                        ),
                        new ExpectedCartItem(
                                "Sleeveless Dress", "Rs. 1000", 1, PriceUtils.multiply("Rs. 1000", 1)
                        )
                )
        );

        cartPage.cart().itemByName("Blue Top").delete();

        assertProductAbsent("Blue Top");
        PageAssertions.assertCartContents(
                cartPage.cart(),
                List.of(
                        new ExpectedCartItem(
                                "Sleeveless Dress", "Rs. 1000", 1, PriceUtils.multiply("Rs. 1000", 1)
                        )
                )
        );
    }


//----Private helpers----

    private void addProductsAndOpenCart(String... productNames) {
        productsPage.addProductsToCart(List.of(productNames));
        productsPage.navigationBar().clickCart();

        PageAssertions.assertPageReady(
                browserSession.getPage(),
                cartPage
        );

        for (String productName : productNames) {
            PageAssertions.assertElementVisible(
                    cartPage.cart().itemByName(productName).getName()
            );
        }
    }

    private void assertProductAbsent(String productName) {
        PageAssertions.assertElementAbsent(
                cartPage.cart().itemByName(productName).getName()
        );
    }

}