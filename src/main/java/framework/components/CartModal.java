package framework.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CartModal {

    private static final Logger log = LoggerFactory.getLogger(CartModal.class);

    private final Locator lblHeader;
    private final Locator lblMessage;
    private final Locator linkViewCart;
    private final Locator btnContinue;

    public CartModal(Page page) {
        Locator rootCartModal = page.locator("div#cartModal");

        lblHeader = rootCartModal.locator("h4.modal-title");
        lblMessage = rootCartModal.locator("div.modal-body p").first();
        linkViewCart = rootCartModal.locator("a[href='/view_cart']");
        btnContinue = rootCartModal.locator("button[data-dismiss='modal']");
    }

//----Public interface----

    public List<Locator> getRequiredElements() {
        return List.of(
                lblHeader,
                lblMessage,
                linkViewCart,
                btnContinue
        );
    }

    public Locator getHeader() {
        return lblHeader;
    }

    public Locator getMessage() {
        return lblMessage;
    }

    public void clickViewCart() {
        log.info("Clicking \"View Cart\" link");
        linkViewCart.click();
    }

    public void clickContinueShopping() {
        log.info("Clicking \"Continue Shopping\" button");
        btnContinue.click();
    }

}
