package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import framework.components.Breadcrumbs;
import framework.components.Cart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CheckoutPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(CheckoutPage.class);

    private static final String URL = BASE_URL + "/checkout";

    private final Breadcrumbs cmpBreadcrumbs;
    private final Cart cmpCart;

    private final Locator lblHeaderAddress;
    private final Locator rootDeliveryAddress;
    private final Locator rootBillingAddress;
    private final Locator lblHeaderReview;
    private final Locator inptOrderComment;
    private final Locator btnSubmit;

    public CheckoutPage(Page page) {
        super(page);

        Locator rootCheckout = page.locator("section#cart_items");

        cmpBreadcrumbs = new Breadcrumbs(page);

        lblHeaderAddress = rootCheckout.locator("div.step-one h2.heading").nth(0);
        lblHeaderReview = rootCheckout.locator("div.step-one h2.heading").nth(1);

        Locator rootAddressInfo = rootCheckout.locator("div.checkout-information");
        rootDeliveryAddress = rootAddressInfo.locator("ul#address_delivery");
        rootBillingAddress = rootAddressInfo.locator("ul#address_invoice");

        cmpCart = new Cart(rootCheckout.locator("div#cart_info"));

        inptOrderComment = rootCheckout.locator("textarea[name='message']");
        btnSubmit = rootCheckout.locator("a[href='/payment']");
    }


    //----Public interface----

    @Override
    public List<Locator> getRequiredElements() {
        List<Locator> elements = new ArrayList<>(cmpBreadcrumbs.getRequiredElements());

        elements.addAll(List.of(
                lblHeaderAddress,
                lblHeaderReview,
                rootDeliveryAddress,
                rootBillingAddress,
                inptOrderComment,
                btnSubmit
        ));

        elements.addAll(cmpCart.getRequiredElements());

        return elements;
    }

    @Override
    public String getTargetUrl() {
        return URL;
    }


    // Page actions
    public void fillOrderComment(String value) {
        log.info("Filling order comment textarea");
        inptOrderComment.fill(value);
    }

    public void clickPlaceOrder() {
        log.info("Clicking \"Place order\" button");
        btnSubmit.click();
    }


    // Components
    public Breadcrumbs breadcrumbs() {
        return cmpBreadcrumbs;
    }

    public Cart cart() {
        return cmpCart;
    }


    // Delivery address info
    public Locator getDeliveryAddressHeader() {
        return rootDeliveryAddress.locator("li.address_title h3");
    }

    public Locator getDeliveryAddressName() {
        return rootDeliveryAddress.locator("li.address_firstname");
    }

    public Locator getDeliveryAddressLines() {
        return rootDeliveryAddress.locator("li.address_address1");
    }

    public Locator getDeliveryAddressStateAndCity() {
        return rootDeliveryAddress.locator("li.address_city");
    }

    public Locator getDeliveryAddressCountry() {
        return rootDeliveryAddress.locator("li.address_country_name");
    }

    public Locator getDeliveryAddressPhone() {
        return rootDeliveryAddress.locator("li.address_phone");
    }


    // Billing address info
    public Locator getBillingAddressHeader() {
        return rootBillingAddress.locator("li.address_title h3");
    }

    public Locator getBillingAddressName() {
        return rootBillingAddress.locator("li.address_firstname");
    }

    public Locator getBillingAddressLines() {
        return rootBillingAddress.locator("li.address_address1");
    }

    public Locator getBillingAddressStateAndCity() {
        return rootBillingAddress.locator("li.address_city");
    }

    public Locator getBillingAddressCountry() {
        return rootBillingAddress.locator("li.address_country_name");
    }

    public Locator getBillingAddressPhone() {
        return rootBillingAddress.locator("li.address_phone");
    }


    // Other elements
    public Locator getAddressSectionHeader() {
        return lblHeaderAddress;
    }

    public Locator getReviewSectionHeader() {
        return lblHeaderReview;
    }
}