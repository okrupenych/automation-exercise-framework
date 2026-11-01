package testsuites.ui.catalog;

import assertions.PageAssertions;
import base.BaseTest;
import framework.pages.ProductDetailsPage;
import framework.pages.ProductsPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("ui")
@Tag("catalog")
public class ProductDetailsTest extends BaseTest {

    private ProductsPage productsPage;
    private ProductDetailsPage productDetailsPage;

    @BeforeEach
    void setUpPages() {
        productsPage = new ProductsPage(browserSession.getPage());
        productDetailsPage = new ProductDetailsPage(browserSession.getPage());
    }

    @Test
    @DisplayName("View Product opens the selected product from the catalog")
    void shouldOpenProductDetailsFromCatalog() {
        productsPage.open();
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                productsPage
        );

        var product = productsPage.productGrid().itemByName("Blue Top");
        PageAssertions.assertElementVisibleWithText(
                product.getName(),
                "Blue Top"
        );
        String expectedPrice = product.getPrice().innerText().trim();

        product.clickViewProduct();

        assertProductDetailsReady(1);
        PageAssertions.assertElementVisibleWithText(
                productDetailsPage.getProductName(),
                "Blue Top"
        );
        PageAssertions.assertElementText(
                productDetailsPage.getProductPrice(),
                expectedPrice
        );
    }

    @Test
    @DisplayName("View Product opens the selected product from search results")
    void shouldOpenProductDetailsFromSearchResults() {
        productsPage.open();
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                productsPage
        );

        productsPage.searchProducts("Blue Top");

        PageAssertions.assertElementVisibleWithText(
                productsPage.getHeader(),
                "Searched Products"
        );

        var product = productsPage.productGrid().itemByName("Blue Top");
        PageAssertions.assertElementVisibleWithText(
                product.getName(),
                "Blue Top"
        );
        String expectedPrice = product.getPrice().innerText().trim();

        product.clickViewProduct();

        assertProductDetailsReady(1);
        PageAssertions.assertElementVisibleWithText(
                productDetailsPage.getProductName(),
                "Blue Top"
        );
        PageAssertions.assertElementText(
                productDetailsPage.getProductPrice(),
                expectedPrice
        );
    }

    @Test
    @DisplayName("Product details display the expected product information")
    void shouldDisplayProductInformation() {
        productDetailsPage.open(1);

        assertProductDetailsReady(1);
        PageAssertions.assertElementText(
                productDetailsPage.getProductName(),
                "Blue Top"
        );
        PageAssertions.assertElementText(
                productDetailsPage.getProductCategory(),
                "Category: Women > Tops"
        );
        PageAssertions.assertElementText(
                productDetailsPage.getProductPrice(),
                "Rs. 500"
        );
        PageAssertions.assertElementText(
                productDetailsPage.getAvailability(),
                "Availability: In Stock"
        );
        PageAssertions.assertElementText(
                productDetailsPage.getCondition(),
                "Condition: New"
        );
        PageAssertions.assertElementText(
                productDetailsPage.getBrand(),
                "Brand: Polo"
        );
    }

    private void assertProductDetailsReady(int productId) {
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                productDetailsPage,
                productDetailsPage.getTargetUrl() + productId
        );
    }
}