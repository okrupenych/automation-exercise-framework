package framework.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Modal {

    private static final Logger log = LoggerFactory.getLogger(Modal.class);

    private final Locator lblHeader;
    private final Locator lblMessage;
    private final Locator linkProceed;
    private final Locator btnDismiss;

    public Modal(Page page) {
        Locator rootModal = page.locator("div.modal-content");

        lblHeader = rootModal.locator("h4.modal-title");
        lblMessage = rootModal.locator("div.modal-body p.text-center").nth(0);
        linkProceed = rootModal.locator("div.modal-body p.text-center").nth(1).locator("a");
        btnDismiss = rootModal.locator("div.modal-footer button[data-dismiss='modal']");
    }

//----Public interface----

    public List<Locator> getRequiredElements() {
        return List.of(
                lblHeader,
                lblMessage,
                linkProceed,
                btnDismiss
        );
    }

    public Locator getHeader() {
        return lblHeader;
    }

    public Locator getMessage() {
        return lblMessage;
    }

    public Locator getProceedLink() {
        return linkProceed;
    }

    public Locator getDismissLink() {
        return btnDismiss;
    }

    public void clickProceedLink() {
        log.info("Clicking proceed link");
        linkProceed.click();
    }

    public void clickDismissButton() {
        log.info("Clicking dismiss button");
        btnDismiss.click();
    }

}
