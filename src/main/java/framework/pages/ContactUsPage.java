package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.List;

public class ContactUsPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(ContactUsPage.class);

    private static final String URL = BASE_URL + "/contact_us";

    private final Locator lblHeaderContactUs;
    private final Locator lblHeaderGetInTouch;
    private final Locator inptName;
    private final Locator inptEmail;
    private final Locator inptSubject;
    private final Locator inptMessage;
    private final Locator inptFilePicker;
    private final Locator btnSubmit;
    private final Locator lblSuccessMessage;
    private final Locator btnHome;

    public ContactUsPage(Page page) {
        super(page);
        Locator rootContactUsForm = page.locator("form#contact-us-form");

        lblHeaderContactUs = page.locator("div#contact-page h2");
        lblHeaderGetInTouch = page.locator("div.contact-form > h2");

        inptName = rootContactUsForm.locator("input[name='name']");
        inptEmail = rootContactUsForm.locator("input[name='email']");
        inptSubject = rootContactUsForm.locator("input[name='subject']");
        inptMessage = rootContactUsForm.locator("textarea[name='message']");
        inptFilePicker = rootContactUsForm.locator("input[name='upload_file']");
        btnSubmit = rootContactUsForm.locator("input[name='submit']");

        lblSuccessMessage = page.locator("div.status.alert.alert-success");
        btnHome = page.locator("div#form-section a.btn-success");

    }


//----Public interface----

    @Override
    public List<Locator> getRequiredElements() {
        return List.of(
                lblHeaderContactUs,
                lblHeaderGetInTouch,
                inptName,
                inptEmail,
                inptSubject,
                inptMessage,
                inptFilePicker,
                btnSubmit
        );
    }

    @Override
    public String getTargetUrl() {
        return URL;
    }


    // Page actions
    public Locator getContactUsHeader() {
        return lblHeaderContactUs;
    }

    public Locator getGetInTouchHeader() {
        return lblHeaderGetInTouch;
    }

    public Locator getSuccessMessage() {
        return lblSuccessMessage;
    }

    public void fillName(String value) {
        log.info("Filling name");
        inptName.fill(value);
    }

    public void fillEmail(String value) {
        log.info("Filling email");
        inptEmail.fill(value);
    }

    public void fillSubject(String value) {
        log.info("Filling subject");
        inptSubject.fill(value);
    }

    public void fillMessage(String value) {
        log.info("Filling message");
        inptMessage.fill(value);
    }

    public void chooseFile(String value) {
        log.info("Choosing upload file");
        inptFilePicker.setInputFiles(Paths.get(value));
    }

    public void clickSubmit() {
        log.info("Clicking \"Submit\" button");
        btnSubmit.click();
    }

    public void clickHome() {
        log.info("Clicking \"Home\" button");
        btnHome.click();
    }

}
