package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

public class ContactUsPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(ContactUsPage.class);

    private static final String URL = BASE_URL + "/contact_us";

    private final Locator inptName;
    private final Locator inptEmail;
    private final Locator inptSubject;
    private final Locator inptMessage;
    private final Locator inptFilePicker;
    private final Locator btnSubmit;

    public ContactUsPage(Page page) {
        super(page);
        Locator rootContactUsForm = page.locator("form#contact-us-form");

        inptName = rootContactUsForm.locator("input[name='name']");
        inptEmail = rootContactUsForm.locator("input[name='email']");
        inptSubject = rootContactUsForm.locator("input[name='subject']");
        inptMessage = rootContactUsForm.locator("textarea[name='message']");
        inptFilePicker = rootContactUsForm.locator("input[name='upload_file']");
        btnSubmit = rootContactUsForm.locator("input[name='submit']");
    }


//----Public interface----

    @Override
    public String getTargetUrl() {
        return URL;
    }


    // Page actions
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

}
