package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class ConfirmationPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(ConfirmationPage.class);

    private final Locator lblHeader;
    private final Locator lblMessage;
    private final Locator btnContinue;

    public ConfirmationPage(Page page) {
        super(page);

        btnContinue = page.locator("a[data-qa='continue-button']");
        lblHeader = page.locator("div.col-sm-9 h2");
        lblMessage = page.locator("div.col-sm-9 p");
    }


//----Public interface----

    @Override
    public List<Locator> getRequiredElements() {
        List<Locator> elements = new ArrayList<>(List.of(
                lblHeader,
                btnContinue
        ));

        elements.addAll(lblMessage.all());

        return elements;
    }

    // Page actions
    public void clickContinueButton() {
        log.info("Clicking \"Continue\" button");
        btnContinue.click();
    }

    public Locator getHeader() {
        return lblHeader;
    }

    public List<Locator> getMessage() {
        return lblMessage.all();
    }

}
