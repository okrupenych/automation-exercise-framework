package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import framework.components.Breadcrumbs;
import framework.components.Cart;
import framework.components.Modal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(CartPage.class);

    private static final String URL = BASE_URL + "/view_cart";

    private final Breadcrumbs cmpBreadcrumbs;
    private final Cart cmpCart;
    private final Modal cmpModal;

    private final Locator btnProceedToCheckout;

    public CartPage(Page page) {
        super(page);

        cmpBreadcrumbs = new Breadcrumbs(page);
        cmpCart = new Cart(page.locator("div#cart_info"));
        cmpModal = new Modal(page);

        btnProceedToCheckout = page.locator("section#do_action a.check_out");
    }


    //----Public interface----

    @Override
    public List<Locator> getRequiredElements() {
        List<Locator> elements = new ArrayList<>();

        elements.addAll(cmpBreadcrumbs.getRequiredElements());
        elements.addAll(cmpCart.getRequiredElements());

        return elements;
    }

    @Override
    public String getTargetUrl() {
        return URL;
    }


    // Page actions
    public void clickProceedToCheckout() {
        log.info("Clicking \"Proceed To Checkout\" button");
        btnProceedToCheckout.click();
    }


//----Built-in components----

    public Breadcrumbs breadcrumbs() {
        return cmpBreadcrumbs;
    }

    public Cart cart() {
        return cmpCart;
    }

    public Modal modal() {
        return cmpModal;
    }
}