package framework.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.PlaywrightException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CartItem {

    private static final Logger log = LoggerFactory.getLogger(CartItem.class);

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


//----Internal implementation----

    private String resolveNameForLog() {
        try {
            return lblName.textContent(
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
                imgPhoto,
                lblName,
                lblCategory,
                lblPrice,
                lblQuantity,
                lblTotal,
                btnDelete
        );
    }

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
        log.info("Clicking product name \"{}\"", resolveNameForLog());
        lblName.click();
    }

    public void delete() {
        log.info("Deleting cart item \"{}\"", resolveNameForLog());
        btnDelete.click();
    }

}
