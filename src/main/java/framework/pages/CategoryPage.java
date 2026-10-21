package framework.pages;

import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CategoryPage extends ShopPage {

    private static final Logger log = LoggerFactory.getLogger(CategoryPage.class);

    private static final String URL = BASE_URL + "/category_products/";

    public CategoryPage(Page page) {
        super(page);
    }

//----Public interface----

    @Override
    public String getTargetUrl() {
        return URL;
    }

    @Override
    public void open() {
        throw new UnsupportedOperationException(
                "CategoryPage requires categoryId"
        );
    }

    public void open(int categoryId) {
        log.info("Opening category for category ID \"{}\"", categoryId);
        page.navigate(getTargetUrl() + categoryId);
        log.debug("Navigation completed for category ID \"{}\"", categoryId);
    }

}
