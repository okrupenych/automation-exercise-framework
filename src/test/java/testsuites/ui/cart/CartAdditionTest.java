package testsuites.ui.cart;

import assertions.PageAssertions;
import base.BaseTest;
import data.CartItemData;
import framework.pages.CartPage;
import framework.pages.HomePage;
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

    private HomePage homePage;
    private ProductsPage productsPage;
    private ProductDetailsPage productDetailsPage;
    private CartPage cartPage;

    @BeforeEach
    void setUpPages() {
        homePage = new HomePage(browserSession.getPage());
        productsPage = new ProductsPage(browserSession.getPage());
        productDetailsPage = new ProductDetailsPage(browserSession.getPage());
        cartPage = new CartPage(browserSession.getPage());
    }


    @Test
    @DisplayName("A product can be added to the cart from the catalog")
    @Tag("smoke")
    void shouldAddProductFromCatalog() {
        List<CartItemData> items = List.of(
                new CartItemData(1, "Blue Top", "Rs. 500", 1)
        );

        openCatalog();

        productsPage.addCartItems(items);
        productsPage.navigationBar().clickCart();

        assertCartContents(items);
    }

    @Test
    @DisplayName("A product can be added to the cart from recommended items on the Home page")
    void shouldAddProductFromRecommendedItems() {
        List<CartItemData> items = List.of(
                new CartItemData(1, "Blue Top", "Rs. 500", 1)
        );

        homePage.open();
        PageAssertions.assertPageReady(browserSession.getPage(), homePage);

        homePage.addRecommendedCartItems(items);
        homePage.navigationBar().clickCart();

        assertCartContents(items);
    }

    @Test
    @DisplayName("Different products appear as separate cart items")
    void shouldAddDifferentProducts() {
        List<CartItemData> items = List.of(
                new CartItemData(1, "Blue Top", "Rs. 500", 1),
                new CartItemData(3, "Sleeveless Dress", "Rs. 1000", 1)
        );

        openCatalog();

        productsPage.addCartItems(items);
        productsPage.navigationBar().clickCart();

        assertCartContents(items);
    }

    @Test
    @DisplayName("Adding the same product again increases its quantity")
    void shouldIncreaseQuantityWhenAddingSameProductAgain() {
        List<CartItemData> items = List.of(
                new CartItemData(1, "Blue Top", "Rs. 500", 2)
        );

        openCatalog();

        productsPage.addCartItems(items);
        productsPage.navigationBar().clickCart();

        assertCartContents(items);
    }

    @Test
    @DisplayName("The cart receives the quantity selected on the product page")
    void shouldAddSelectedQuantityFromProductDetails() {
        List<CartItemData> items = List.of(
                new CartItemData(1, "Blue Top", "Rs. 500", 3)
        );

        productDetailsPage.open(items.getFirst());
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                productDetailsPage,
                productDetailsPage.getTargetUrl() + items.getFirst().productId()
        );

        productDetailsPage.addToCart(items.getFirst());
        productDetailsPage.modal().waitUntilVisible();
        productDetailsPage.modal().clickProceedLink();

        assertCartContents(items);
    }


//----Private helpers----

    private void openCatalog() {
        productsPage.open();
        PageAssertions.assertPageReady(browserSession.getPage(), productsPage);
    }

    private void assertCartContents(List<CartItemData> expectedItems) {
        PageAssertions.assertPageReady(browserSession.getPage(), cartPage);

        PageAssertions.assertCartContents(cartPage.cart(), expectedItems);
    }

}
