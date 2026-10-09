package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import framework.components.CartItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CartPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(CartPage.class);

    private static final String URL = BASE_URL + "/view_cart";

    private final Locator rootCart;

    private final Locator linkHome;
    private final Locator btnProceedToCheckout;

    public CartPage(Page page) {
        super(page);
        rootCart = page.locator("table#cart_info_table tbody");

        linkHome = page.locator("div.breadcrumbs a[href='/']");
        btnProceedToCheckout = page.locator("section#do_action a.check_out");
    }


//----Public interface----

    @Override
    public String getTargetUrl() {
        return URL;
    }


    // Products in the cart
    public CartItem itemByIndex(int index) {
        return new CartItem(rootCart.locator("tr").nth(index));
    }

    public CartItem itemByProductId(int productId) {
        return new CartItem(rootCart.locator("tr#product-" + productId));
    }

    public CartItem itemByName(String productName) {
        return new CartItem(rootCart.locator("tr").filter(
                new Locator.FilterOptions().setHasText(productName)
        ));
    }


    // Page actions
    public void clickHomeLink() {
        log.info("Clicking \"Home\" link");
        linkHome.click();
    }

    public void clickProceedToCheckout() {
        log.info("Clicking \"Proceed To Checkout\" button");
        btnProceedToCheckout.click();
    }

}
