package testsuites.ui.catalog;

import assertions.PageAssertions;
import base.BaseTest;
import framework.pages.BrandPage;
import framework.pages.ProductsPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("ui")
@Tag("catalog")
public class BrandFilterTest extends BaseTest {

    private ProductsPage productsPage;
    private BrandPage brandPage;

    @BeforeEach
    void setUpPages() {
        productsPage = new ProductsPage(browserSession.getPage());
        brandPage = new BrandPage(browserSession.getPage());

        productsPage.open();
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                productsPage
        );
    }


    @Test
    @DisplayName("Polo filter shows Polo products")
    void shouldFilterByPolo() {
        productsPage.productsFilterBar().openBrandLink("Polo");

        assertBrandReady("Polo");
        assertProductVisible("Blue Top");
        assertProductAbsent("Sleeveless Dress");
    }

    @Test
    @DisplayName("Madame filter shows Madame products")
    void shouldFilterByMadame() {
        productsPage.productsFilterBar().openBrandLink("Madame");

        assertBrandReady("Madame");
        assertProductVisible("Sleeveless Dress");
        assertProductAbsent("Blue Top");
    }

    @Test
    @DisplayName("Switching brand replaces the previous results")
    void shouldReplaceResultsWhenSwitchingBrand() {
        productsPage.productsFilterBar().openBrandLink("Polo");

        assertBrandReady("Polo");
        assertProductVisible("Blue Top");
        assertProductAbsent("Sleeveless Dress");

        brandPage.productsFilterBar().openBrandLink("Madame");

        assertBrandReady("Madame");
        assertProductVisible("Sleeveless Dress");
        assertProductAbsent("Blue Top");
    }


//----Private helpers----

    private void assertBrandReady(String brandName) {
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                brandPage,
                brandPage.getTargetUrl() + brandName
        );
        PageAssertions.assertElementVisibleWithText(
                brandPage.getHeader(),
                "Brand - " + brandName + " Products"
        );
    }

    private void assertProductVisible(String productName) {
        PageAssertions.assertElementVisibleWithText(
                brandPage.productGrid()
                        .itemByName(productName).getName(),
                productName
        );
    }

    private void assertProductAbsent(String productName) {
        PageAssertions.assertElementAbsent(
                brandPage.productGrid()
                        .itemByName(productName).getName()
        );
    }

}