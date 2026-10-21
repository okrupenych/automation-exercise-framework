package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import framework.components.Modal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ProductsPage extends ShopPage {

    private static final Logger log = LoggerFactory.getLogger(ProductsPage.class);

    private static final String URL = BASE_URL + "/products";

    private final Modal cmpModal;

    private final Locator inptSearchProduct;
    private final Locator btnSubmitSearchProduct;

    public ProductsPage(Page page) {
        super(page);
        cmpModal = new Modal(page);

        Locator rootAdvertisementSection = page.locator("section#advertisement");

        inptSearchProduct = rootAdvertisementSection.locator("input#search_product");
        btnSubmitSearchProduct = rootAdvertisementSection.locator("button#submit_search");
    }


//----Public interface----

    @Override
    public List<Locator> getRequiredElements() {
        List<Locator> elements = new ArrayList<>(List.of(
                inptSearchProduct,
                btnSubmitSearchProduct
        ));

        elements.addAll(super.getRequiredElements());

        return elements;
    }

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
    public Modal modal() {
        return cmpModal;
    }

}
