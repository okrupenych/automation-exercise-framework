package framework.components;

import com.microsoft.playwright.Locator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


//----Public interface----

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
        log.info("Clicking \"Add to cart\" button for product \"{}\"", lblProductName.textContent());
        btnAddToCart.click();
    }

    public void clickViewProduct() {
        log.info("Clicking \"View Product\" link for product \"{}\"", lblProductName.textContent());
        linkViewProduct.click();
    }

}
