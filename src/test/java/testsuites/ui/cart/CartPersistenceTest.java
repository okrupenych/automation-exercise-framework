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
public class CartPersistenceTest extends BaseTest {

    private ProductsPage productsPage;
    private CartPage cartPage;

    private final List<CartItemData> expectedItems = List.of(
            new CartItemData(1, "Blue Top", "Rs. 500", 2),
            new CartItemData(3, "Sleeveless Dress", "Rs. 1000", 1)
    );

    @BeforeEach
    void setUpCart() {
        productsPage = new ProductsPage(browserSession.getPage());
        cartPage = new CartPage(browserSession.getPage());

        productsPage.open();
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                productsPage
        );

        productsPage.addCartItems(expectedItems);
        productsPage.navigationBar().clickCart();

        assertCartContents(expectedItems);
    }


    @Test
    @DisplayName("Cart contents survive a page reload")
    void shouldPreserveCartAfterReload() {
        browserSession.getPage().reload();

        assertCartContents(expectedItems);
    }

    @Test
    @DisplayName("Cart contents survive navigation to the catalog and back")
    void shouldPreserveCartAfterNavigation() {
        cartPage.navigationBar().clickProducts();

        PageAssertions.assertPageReady(
                browserSession.getPage(),
                productsPage
        );

        productsPage.navigationBar().clickCart();

        assertCartContents(expectedItems);
    }

    @Test
    @DisplayName("A removed product does not return after a page reload")
    void shouldPreserveRemovalAfterReload() {
        cartPage.cart().itemByProductId(expectedItems.get(0).productId()).delete();

        PageAssertions.assertElementAbsent(
                cartPage.cart().itemByProductId(expectedItems.get(0).productId()).getName()
        );

        List<CartItemData> remainingItems = List.of(expectedItems.get(1));
        assertCartContents(remainingItems);

        browserSession.getPage().reload();

        assertCartContents(remainingItems);
        PageAssertions.assertElementAbsent(
                cartPage.cart().itemByProductId(expectedItems.getFirst().productId()).getName()
        );
    }


//----Private helpers----

    private void assertCartContents(List<CartItemData> items) {
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                cartPage
        );

        PageAssertions.assertCartContents(cartPage.cart(), items);
    }

}