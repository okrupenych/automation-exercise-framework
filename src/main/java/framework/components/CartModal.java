package framework.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CartModal {

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

    public Locator getHeader() {
        return lblHeader;
    }

    public Locator getMessage() {
        return lblMessage;
    }

    public void clickViewCart() {
        linkViewCart.click();
    }

    public void clickContinueShopping() {
        btnContinue.click();
    }

}