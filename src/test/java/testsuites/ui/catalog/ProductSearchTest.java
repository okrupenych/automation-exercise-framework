package testsuites.ui.catalog;

import assertions.PageAssertions;
import base.BaseTest;
import framework.pages.ProductsPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("ui")
@Tag("catalog")
public class ProductSearchTest extends BaseTest {

    private ProductsPage productsPage;

    @BeforeEach
    void setUpProductsPage() {
        productsPage = new ProductsPage(browserSession.getPage());
        productsPage.open();

        PageAssertions.assertPageReady(
                browserSession.getPage(),
                productsPage
        );
    }

    @Test
    @DisplayName("Search finds a product by its full name")
    void shouldFindProductByFullName() {
        productsPage.searchProducts("Blue Top");

        PageAssertions.assertElementVisibleWithText(
                productsPage.getHeader(),
                "Searched Products"
        );
        PageAssertions.assertElementVisibleWithText(
                productsPage.productGrid().itemByIndex(0).getName(),
                "Blue Top"
        );
        PageAssertions.assertItemsCount(
                productsPage.productGrid().getAllItems(),
                1
        );
    }

    @Disabled("Search matching rules need clarification: results may match category as well as product name")
    @Test
    @DisplayName("Search finds matching products by part of their name")
    void shouldFindProductsByPartialName() {
        productsPage.searchProducts("Top");

        PageAssertions.assertElementVisibleWithText(
                productsPage.getHeader(),
                "Searched Products"
        );
        PageAssertions.assertElementVisibleWithText(
                productsPage.productGrid().itemByName("Blue Top").getName(),
                "Blue Top"
        );
        PageAssertions.assertSearchResults(
                productsPage.productGrid(),
                "Top"
        );
    }

    @Test
    @DisplayName("Search returns no products for an unknown name")
    @Tag("negative")
    void shouldReturnNoProductsForUnknownName() {
        productsPage.searchProducts("nonexistent-product-7f93c2");

        PageAssertions.assertElementVisibleWithText(
                productsPage.getHeader(),
                "Searched Products"
        );
        PageAssertions.assertItemsCount(
                productsPage.productGrid().getAllItems(),
                0
        );
    }

    @Test
    @DisplayName("A new search replaces the previous results")
    void shouldReplaceResultsWhenSearchingAgain() {
        productsPage.searchProducts("Blue Top");

        PageAssertions.assertElementVisibleWithText(
                productsPage.getHeader(),
                "Searched Products"
        );
        PageAssertions.assertElementVisibleWithText(
                productsPage.productGrid().itemByIndex(0).getName(),
                "Blue Top"
        );
        PageAssertions.assertItemsCount(
                productsPage.productGrid().getAllItems(),
                1
        );

        productsPage.searchProducts("Sleeveless Dress");

        PageAssertions.assertElementVisibleWithText(
                productsPage.getHeader(),
                "Searched Products"
        );
        PageAssertions.assertElementVisibleWithText(
                productsPage.productGrid().itemByIndex(0).getName(),
                "Sleeveless Dress"
        );
        PageAssertions.assertItemsCount(
                productsPage.productGrid().getAllItems(),
                1
        );
    }

}