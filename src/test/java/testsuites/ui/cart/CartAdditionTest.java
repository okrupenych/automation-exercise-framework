package testsuites.ui.cart;

import assertions.PageAssertions;
import assertions.PageAssertions.ExpectedCartItem;
import base.BaseTest;
import framework.pages.CartPage;
import framework.pages.ProductDetailsPage;
import framework.pages.ProductsPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

@Tag("ui")
@Tag("cart")
public class CartAdditionTest extends BaseTest {

    private ProductsPage productsPage;
    private ProductDetailsPage productDetailsPage;
    private CartPage cartPage;

    @BeforeEach
    void setUpPages() {
        productsPage = new ProductsPage(browserSession.getPage());
        productDetailsPage = new ProductDetailsPage(browserSession.getPage());
        cartPage = new CartPage(browserSession.getPage());
    }


    @Test
    @DisplayName("A product can be added to the cart from the catalog")
    @Tag("smoke")
    void shouldAddProductFromCatalog() {
        openCatalog();

        productsPage.productGrid()
                .itemByName("Blue Top").clickAddToCart();

        productsPage.modal().waitUntilVisible();
        productsPage.modal().clickProceedLink();

        assertCartContents(
                new ExpectedCartItem("Blue Top", "Rs. 500", 1, "Rs. 500")
        );
    }

    @Test
    @DisplayName("Different products appear as separate cart items")
    void shouldAddDifferentProducts() {
        openCatalog();

        productsPage.addProductsToCart(
                List.of("Blue Top", "Sleeveless Dress")
        );
        productsPage.navigationBar().clickCart();

        assertCartContents(
                new ExpectedCartItem("Blue Top", "Rs. 500", 1, "Rs. 500"),
                new ExpectedCartItem(
                        "Sleeveless Dress", "Rs. 1000", 1, "Rs. 1000"
                )
        );
    }

    @Test
    @DisplayName("Adding the same product again increases its quantity")
    void shouldIncreaseQuantityWhenAddingSameProductAgain() {
        openCatalog();

        productsPage.addProductsToCart(
                List.of("Blue Top", "Blue Top")
        );
        productsPage.navigationBar().clickCart();

        assertCartContents(
                new ExpectedCartItem("Blue Top", "Rs. 500", 2, "Rs. 1000")
        );
    }

    @Test
    @DisplayName("The cart receives the quantity selected on the product page")
    void shouldAddSelectedQuantityFromProductDetails() {
        productDetailsPage.open(1);
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                productDetailsPage,
                productDetailsPage.getTargetUrl() + 1
        );

        productDetailsPage.addToCart(3);
        productDetailsPage.modal().waitUntilVisible();
        productDetailsPage.modal().clickProceedLink();

        assertCartContents(
                new ExpectedCartItem("Blue Top", "Rs. 500", 3, "Rs. 1500")
        );
    }


//----Private helpers----

    private void openCatalog() {
        productsPage.open();
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                productsPage
        );
    }

    private void assertCartContents(ExpectedCartItem... expectedItems) {
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                cartPage
        );

        for (ExpectedCartItem expected : expectedItems) {
            PageAssertions.assertElementVisible(
                    cartPage.cart().itemByName(expected.name()).getName()
            );
        }

        PageAssertions.assertCartContents(
                cartPage.cart(),
                List.of(expectedItems)
        );
    }

}