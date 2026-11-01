package testsuites.ui.catalog;

import assertions.PageAssertions;
import base.BaseTest;
import framework.pages.CategoryPage;
import framework.pages.ProductsPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Tag("ui")
@Tag("catalog")
public class CategoryFilterTest extends BaseTest {

    private ProductsPage productsPage;
    private CategoryPage categoryPage;

    @BeforeEach
    void setUpPages() {
        productsPage = new ProductsPage(browserSession.getPage());
        categoryPage = new CategoryPage(browserSession.getPage());

        productsPage.open();
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                productsPage
        );
    }

    @Test
    @DisplayName("Women Dress category shows women's dresses")
    void shouldFilterByWomenDress() {
        productsPage.productsFilterBar()
                .openCategoryLink("Women", "Dress");

        assertCategoryReady(1, "Women - Dress Products");
        assertProductVisible("Sleeveless Dress");

        assertThat(
                categoryPage.productGrid()
                        .itemByName("Men Tshirt").getName()
        ).hasCount(0);
    }

    @Test
    @DisplayName("Men Tshirts category shows men's T-shirts")
    void shouldFilterByMenTshirts() {
        productsPage.productsFilterBar()
                .openCategoryLink("Men", "Tshirts");

        assertCategoryReady(3, "Men - Tshirts Products");
        assertProductVisible("Men Tshirt");

        assertThat(
                categoryPage.productGrid()
                        .itemByName("Sleeveless Dress").getName()
        ).hasCount(0);
    }

    @Test
    @DisplayName("Kids Dress category shows children's clothing")
    void shouldFilterByKidsDress() {
        productsPage.productsFilterBar()
                .openCategoryLink("Kids", "Dress");

        assertCategoryReady(4, "Kids - Dress Products");
        assertProductVisible("Blue Cotton Indie Mickey Dress");

        assertThat(
                categoryPage.productGrid()
                        .itemByName("Sleeveless Dress").getName()
        ).hasCount(0);
    }

    @Test
    @DisplayName("Switching category replaces the previous results")
    void shouldReplaceResultsWhenSwitchingCategory() {
        productsPage.productsFilterBar()
                .openCategoryLink("Women", "Dress");

        assertCategoryReady(1, "Women - Dress Products");
        assertProductVisible("Sleeveless Dress");

        categoryPage.productsFilterBar()
                .openCategoryLink("Men", "Tshirts");

        assertCategoryReady(3, "Men - Tshirts Products");
        assertProductVisible("Men Tshirt");

        assertThat(
                categoryPage.productGrid()
                        .itemByName("Sleeveless Dress").getName()
        ).hasCount(0);
    }

    private void assertCategoryReady(int categoryId, String expectedHeader) {
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                categoryPage,
                categoryPage.getTargetUrl() + categoryId
        );
        PageAssertions.assertElementVisibleWithText(
                categoryPage.getHeader(),
                expectedHeader
        );
    }

    private void assertProductVisible(String productName) {
        PageAssertions.assertElementVisibleWithText(
                categoryPage.productGrid()
                        .itemByName(productName).getName(),
                productName
        );
    }
}