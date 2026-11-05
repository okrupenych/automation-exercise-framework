package testsuites.ui.cart;

import assertions.PageAssertions;
import assertions.PageAssertions.ExpectedCartItem;
import base.BaseTest;
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

    private final List<ExpectedCartItem> expectedItems = List.of(
            new ExpectedCartItem(
                    "Blue Top", "Rs. 500", 2, "Rs. 1000"
            ),
            new ExpectedCartItem(
                    "Sleeveless Dress", "Rs. 1000", 1, "Rs. 1000"
            )
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

        productsPage.addProductsToCart(
                List.of("Blue Top", "Blue Top", "Sleeveless Dress")
        );
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
        cartPage.cart().itemByName("Blue Top").delete();

        PageAssertions.assertElementAbsent(
                cartPage.cart().itemByName("Blue Top").getName()
        );

        List<ExpectedCartItem> remainingItems = List.of(
                new ExpectedCartItem(
                        "Sleeveless Dress", "Rs. 1000", 1, "Rs. 1000"
                )
        );
        assertCartContents(remainingItems);

        browserSession.getPage().reload();

        assertCartContents(remainingItems);
        PageAssertions.assertElementAbsent(
                cartPage.cart().itemByName("Blue Top").getName()
        );
    }


//----Private helpers----

    private void assertCartContents(List<ExpectedCartItem> items) {
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                cartPage
        );

        for (ExpectedCartItem item : items) {
            PageAssertions.assertElementVisible(
                    cartPage.cart().itemByName(item.name()).getName()
            );
        }

        PageAssertions.assertCartContents(cartPage.cart(), items);
    }

}