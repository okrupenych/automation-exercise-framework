package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class ConfirmationPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(ConfirmationPage.class);

    private final Locator lblHeader;
    private final Locator lblMessagePart1;
    private final Locator lblMessagePart2;
    private final Locator btnContinue;

    public ConfirmationPage(Page page) {
        super(page);

        btnContinue = page.locator("a[data-qa='continue-button']");
        lblHeader = page.locator("div.col-sm-9 h2");
        lblMessagePart1 = page.locator("div.col-sm-9 p").nth(0);
        lblMessagePart2 = page.locator("div.col-sm-9 p").nth(1);
    }


//----Public interface----

    @Override
    public List<Locator> getRequiredElements() {
        return List.of(
                lblHeader,
                lblMessagePart1,
                lblMessagePart2,
                btnContinue
        );
    }

    // Page actions
    public void clickContinueButton() {
        log.info("Clicking \"Continue\" button");
        btnContinue.click();
    }

    public Locator getHeader() {
        return lblHeader;
    }

    public Locator getMessagePart1() {
        return lblMessagePart1;
    }

    public Locator getMessagePart2() {
        return lblMessagePart2;
    }

}
