package framework.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.PlaywrightException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProductItem {

    private static final Logger log = LoggerFactory.getLogger(ProductItem.class);

    private final Locator imgProductPhoto;
    private final Locator lblProductName;
    private final Locator lblProductPrice;
    private final Locator btnAddToCart;
    private final Locator linkViewProduct;

    public ProductItem(Locator rootItem) {
        Locator rootProductInfo = rootItem.locator("div.productinfo");

        imgProductPhoto = rootProductInfo.locator("img");
        lblProductName = rootProductInfo.locator("p");
        lblProductPrice = rootProductInfo.locator("h2");
        btnAddToCart = rootProductInfo.locator("a.add-to-cart");
        linkViewProduct = rootItem.locator("div.choose a");
    }


//----Internal implementation----
    private String resolveNameForLog() {
        try {
            return lblProductName.textContent(
                    new Locator.TextContentOptions().setTimeout(500)
            );
        } catch (PlaywrightException e) {
            log.debug("Could not retrieve product name for logging: \"{}\"", e.getMessage());
            return "<unknown>";
        }
    }

//----Public interface----

    public List<Locator> getRequiredElements() {
        return List.of(
                imgProductPhoto,
                lblProductName,
                lblProductPrice,
                btnAddToCart,
                linkViewProduct
        );
    }

    public Locator getPhoto() {
        return imgProductPhoto;
    }

    public Locator getName() {
        return lblProductName;
    }

    public Locator getPrice() {
        return lblProductPrice;
    }

    public void clickAddToCart() {
        log.info("Clicking \"Add to cart\" button for product \"{}\"", resolveNameForLog());
        btnAddToCart.click();
    }

    public void clickViewProduct() {
        log.info("Clicking \"View Product\" link for product \"{}\"", resolveNameForLog());
        linkViewProduct.click();
    }

}
