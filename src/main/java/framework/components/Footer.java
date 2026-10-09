package framework.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class Footer {

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
        inptEmail.fill(value);
    }

    public void clickSubscribe() {
        btnSubscribe.click();
    }

    public Locator getSubscriptionMessage() {
        return lblSubscriptionMessage;
    }

    public Locator getInfo() {
        return lblInfo;
    }

}
