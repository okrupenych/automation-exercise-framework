package framework.components;

import com.microsoft.playwright.Locator;

public class CartItem {

    private final Locator imgPhoto;
    private final Locator lblName;
    private final Locator lblCategory;
    private final Locator lblPrice;
    private final Locator lblQuantity;
    private final Locator lblTotal;
    private final Locator btnDelete;

    public CartItem(Locator rootItem) {
        imgPhoto = rootItem.locator("td.cart_product img");
        lblName = rootItem.locator("td.cart_description h4 a");
        lblCategory = rootItem.locator("td.cart_description p");
        lblPrice = rootItem.locator("td.cart_price p");
        lblQuantity = rootItem.locator("td.cart_quantity button");
        lblTotal = rootItem.locator("td.cart_total p");
        btnDelete = rootItem.locator("td.cart_delete a.cart_quantity_delete");
    }


//----Public interface----

    public Locator getPhoto() {
        return imgPhoto;
    }

    public Locator getName() {
        return lblName;
    }

    public Locator getCategory() {
        return lblCategory;
    }

    public Locator getPrice() {
        return lblPrice;
    }

    public Locator getQuantity() {
        return lblQuantity;
    }

    public Locator getTotal() {
        return lblTotal;
    }

    public void clickName() {
        lblName.click();
    }

    public void delete() {
        btnDelete.click();
    }

}