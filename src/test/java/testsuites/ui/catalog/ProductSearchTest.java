package testsuites.ui.catalog;

import assertions.PageAssertions;
import base.BaseTest;
import framework.api.ApiClient;
import framework.api.ApiSession;
import framework.api.AutomationExerciseApi;
import framework.api.models.ProductsResponse;
import framework.pages.ProductsPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Locale;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    @DisplayName("Search finds matching products by part of their name")
    void shouldFindProductsByPartialName() {
        var productsById = getProductsById();
        productsPage.searchProducts("Blue");

        PageAssertions.assertElementVisibleWithText(
                productsPage.getHeader(),
                "Searched Products"
        );
        var results = productsPage.productGrid().getAllItems();
        assertFalse(results.isEmpty(), "Search should return products");
        for (var result : results) {
            int productId = result.getProductId();
            var product = productsById.get(productId);
            assertNotNull(product, "API product list should contain product " + productId);
            assertTrue(product.name().toLowerCase(Locale.ROOT).contains("blue"),
                    "Product " + productId + " should have a name containing Blue: " + product.name());
        }
    }

    @Test
    @DisplayName("Search finds matching products by subcategory")
    void shouldFindProductsBySubcategory() {
        var productsById = getProductsById();
        productsPage.searchProducts("Tops & Shirts");

        PageAssertions.assertElementVisibleWithText(productsPage.getHeader(), "Searched Products");
        var results = productsPage.productGrid().getAllItems();
        assertFalse(results.isEmpty(), "Search should return products");
        for (var result : results) {
            int productId = result.getProductId();
            var product = productsById.get(productId);
            assertNotNull(product, "API product list should contain product " + productId);
            assertNotNull(product.category(), "Product " + productId + " should have a category");
            assertEquals("Tops & Shirts", product.category().category(),
                    "Product " + productId + " should match the searched subcategory");
        }
    }

    @Test
    @DisplayName("Search finds matching products by brand")
    void shouldFindProductsByBrand() {
        var productsById = getProductsById();
        productsPage.searchProducts("Polo");

        PageAssertions.assertElementVisibleWithText(productsPage.getHeader(), "Searched Products");
        var results = productsPage.productGrid().getAllItems();
        assertFalse(results.isEmpty(), "Search should return products");
        for (var result : results) {
            int productId = result.getProductId();
            var product = productsById.get(productId);
            assertNotNull(product, "API product list should contain product " + productId);
            assertEquals("Polo", product.brand(),
                    "Product " + productId + " should match the searched brand");
        }
    }

    @Test
    @Tag("negative")
    @DisplayName("A product found by name is absent from a search for a different brand")
    void shouldExcludeProductWhenSearchingForDifferentBrand() {
        var productsById = getProductsById();
        productsPage.searchProducts("Blue");

        PageAssertions.assertElementVisibleWithText(productsPage.getHeader(), "Searched Products");
        var blueTop = productsPage.productGrid().itemByExactName("Blue Top");
        PageAssertions.assertElementVisibleWithText(blueTop.getName(), "Blue Top");
        int productId = blueTop.getProductId();
        var product = productsById.get(productId);
        assertNotNull(product, "API product list should contain product " + productId);
        assertEquals("Blue Top", product.name());
        assertEquals("Polo", product.brand());

        productsPage.searchProducts("Mast & Harbour");

        PageAssertions.assertElementVisibleWithText(productsPage.getHeader(), "Searched Products");
        for (var result : productsPage.productGrid().getAllItems()) {
            assertNotEquals(productId, result.getProductId(),
                    "Blue Top (product " + productId + ") should be absent from another brand's results");
        }
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


//----Private helpers----

    private Map<Integer, ProductsResponse.Product> getProductsById() {
        try (ApiSession session = new ApiSession()) {
            AutomationExerciseApi api = new AutomationExerciseApi(
                    new ApiClient(session.getRequestContext())
            );
            var result = api.getProducts();
            assertEquals(200, result.httpStatus());
            ProductsResponse response = result.as(ProductsResponse.class);
            assertNotNull(response);
            assertEquals(Integer.valueOf(200), response.responseCode());
            assertNotNull(response.products());
            assertFalse(response.products().isEmpty(), "API product list should not be empty");
            return response.products().stream().collect(Collectors.toMap(
                    ProductsResponse.Product::id, Function.identity()
            ));
        }
    }

}
