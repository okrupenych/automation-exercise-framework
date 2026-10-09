package framework.components;

import com.microsoft.playwright.Locator;

public class ProductItem {

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
        btnAddToCart.click();
    }

    public void clickViewProduct() {
        linkViewProduct.click();
    }

}