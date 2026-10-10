package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import framework.components.CartModal;
import framework.components.ProductFilterBar;
import framework.components.ProductGrid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductsPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(ProductsPage.class);

    private static final String URL = BASE_URL + "/products";

    private final ProductFilterBar cmpProductFilterBar;
    private final ProductGrid cmpProductGrid;
    private final CartModal cmpCartModal;

    private final Locator inptSearchProduct;
    private final Locator btnSubmitSearchProduct;

    public ProductsPage(Page page) {
        super(page);
        cmpProductFilterBar = new ProductFilterBar(page);
        cmpProductGrid = new ProductGrid(page);
        cmpCartModal = new CartModal(page);

        Locator rootAdvertisementSection = page.locator("section#advertisement");

        inptSearchProduct = rootAdvertisementSection.locator("input#search_product");
        btnSubmitSearchProduct = rootAdvertisementSection.locator("button#submit_search");
    }


//----Public interface----

    @Override
    public String getTargetUrl() {
        return URL;
    }


    // Page actions
    public void fillSearchProduct(String value) {
        log.info("Filling product search");
        inptSearchProduct.fill(value);
    }

    public void clickSubmitSearch() {
        log.info("Clicking submit search button");
        btnSubmitSearchProduct.click();
    }


    // Built-in components
    public ProductFilterBar productsFilterBar() {
        return cmpProductFilterBar;
    }

    public ProductGrid productGrid() {
        return cmpProductGrid;
    }

    public CartModal cartModal() {
        return cmpCartModal;
    }

}
