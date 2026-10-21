package framework.pages;

import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrandPage extends ShopPage {

    private static final Logger log = LoggerFactory.getLogger(BrandPage.class);

    private static final String URL = BASE_URL + "/brand_products/";

    public BrandPage(Page page) {
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
                "BrandPage requires brandName"
        );
    }

    public void open(String brandName) {
        log.info("Opening products for brand \"{}\"", brandName);
        page.navigate(getTargetUrl() + brandName);
        log.debug("Navigation completed for brand \"{}\"", brandName);
    }

}
