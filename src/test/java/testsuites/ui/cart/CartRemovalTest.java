package testsuites.ui.cart;

import assertions.PageAssertions;
import base.BaseTest;
import data.CartItemData;
import framework.pages.CartPage;
import framework.pages.ProductsPage;
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
        List<CartItemData> items = List.of(
                new CartItemData(1, "Blue Top", "Rs. 500", 1),
                new CartItemData(3, "Sleeveless Dress", "Rs. 1000", 1)
        );

        addProductsAndOpenCart(items);

        PageAssertions.assertCartContents(cartPage.cart(), items);

        cartPage.cart().itemByProductId(items.get(0).productId()).delete();

        assertProductAbsent(items.get(0));
        PageAssertions.assertCartContents(cartPage.cart(), List.of(items.get(1)));
    }

    @Test
    @DisplayName("Removing the last product leaves the cart empty")
    void shouldEmptyCartWhenLastProductIsRemoved() {
        List<CartItemData> items = List.of(
                new CartItemData(1, "Blue Top", "Rs. 500", 1)
        );

        addProductsAndOpenCart(items);

        PageAssertions.assertCartContents(cartPage.cart(), items);

        cartPage.cart().itemByProductId(items.getFirst().productId()).delete();

        assertProductAbsent(items.getFirst());
        PageAssertions.assertCartContents(cartPage.cart(), List.of());
    }

    @Test
    @DisplayName("Removing a product deletes all units of that product")
    void shouldRemoveEntireProductLineRegardlessOfQuantity() {
        List<CartItemData> items = List.of(
                new CartItemData(1, "Blue Top", "Rs. 500", 2),
                new CartItemData(3, "Sleeveless Dress", "Rs. 1000", 1)
        );

        addProductsAndOpenCart(items);

        PageAssertions.assertCartContents(cartPage.cart(), items);

        cartPage.cart().itemByProductId(items.getFirst().productId()).delete();

        assertProductAbsent(items.getFirst());
        PageAssertions.assertCartContents(cartPage.cart(), List.of(items.get(1)));
    }


//----Private helpers----

    private void addProductsAndOpenCart(List<CartItemData> items) {
        productsPage.addCartItems(items);
        productsPage.navigationBar().clickCart();

        PageAssertions.assertPageReady(
                browserSession.getPage(),
                cartPage
        );
    }

    private void assertProductAbsent(CartItemData item) {
        PageAssertions.assertElementAbsent(
                cartPage.cart().itemByProductId(item.productId()).getName()
        );
    }

}