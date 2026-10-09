package framework.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Footer {

    private static final Logger log = LoggerFactory.getLogger(Footer.class);

    private final Locator inptEmail;
    private final Locator btnSubscribe;
    private final Locator lblSubscriptionMessage;
    private final Locator lblInfo;

    public Footer(Page page) {
        Locator rootFooter = page.locator("footer#footer");

        inptEmail = rootFooter.locator("input#susbscribe_email");
        btnSubscribe = rootFooter.locator("button#subscribe");
        lblSubscriptionMessage = rootFooter.locator("div#success-subscribe div");
        lblInfo = rootFooter.locator("form.searchform p");
    }


//----Public interface----

    public void fillEmail(String value) {
        log.info("Filling email {}", value);
        inptEmail.fill(value);
    }

    public void clickSubscribe() {
        log.info("Clicking \"Subscribe\" button");
        btnSubscribe.click();
    }

    public Locator getSubscriptionMessage() {
        return lblSubscriptionMessage;
    }

    public Locator getInfo() {
        return lblInfo;
    }

}
