package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PaymentPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(PaymentPage.class);

    private static final String URL = BASE_URL + "/payment";

    private final Locator lblHeader;
    private final Locator inptNameOnCard;
    private final Locator inptCardNumber;
    private final Locator inptCVC;
    private final Locator inptExpirationMonth;
    private final Locator inptExpirationYear;
    private final Locator btnPayAndConfirmOrder;
    private final Locator lblMessageSuccess;

    public PaymentPage(Page page) {
        super(page);

        lblHeader = page.locator("div.step-one h2.heading");

        Locator rootForm = page.locator("form#payment-form");
        inptNameOnCard = rootForm.locator("input[name='name_on_card']");
        inptCardNumber = rootForm.locator("input[name='card_number']");
        inptCVC = rootForm.locator("input[name='cvc']");
        inptExpirationMonth = rootForm.locator("input[name='expiry_month']");
        inptExpirationYear = rootForm.locator("input[name='expiry_year']");
        btnPayAndConfirmOrder = rootForm.locator("button#submit");

        lblMessageSuccess = page.locator("div#success_message div.alert-success");
    }


//----Public interface----

    @Override
    public List<Locator> getRequiredElements() {
        return List.of(
                lblHeader,
                inptNameOnCard,
                inptCardNumber,
                inptCVC,
                inptExpirationMonth,
                inptExpirationYear,
                btnPayAndConfirmOrder
        );
    }

    @Override
    public String getTargetUrl() {
        return URL;
    }


    // Page actions
    public Locator getHeader() {
        return lblHeader;
    }

    public Locator getSuccessMessage() {
        return lblMessageSuccess;
    }

    public void fillNameOnCard(String value) {
        log.info("Filling name on card");
        inptNameOnCard.fill(value);
    }

    public void fillCardNumber(String value) {
        log.info("Filling card number");
        inptCardNumber.fill(value);
    }

    public void fillCVC(String value) {
        log.info("Filling cvc");
        inptCVC.fill(value);
    }

    public void fillExpirationMonth(String value) {
        log.info("Filling expiration month");
        inptExpirationMonth.fill(value);
    }

    public void fillExpirationYear(String value) {
        log.info("Filling expiration year");
        inptExpirationYear.fill(value);
    }

    public void clickPayAndConfirmOrder() {
        log.info("Clicking \"Pay and Confirm Order\" button");
        btnPayAndConfirmOrder.click(new Locator.ClickOptions().setNoWaitAfter(true));
    }

}
