package testsuites.ui.contact;

import assertions.PageAssertions;
import base.BaseTest;
import framework.pages.ContactUsPage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

import com.microsoft.playwright.Dialog;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Tag("ui")
@Tag("contact")
public class ContactUsTest extends BaseTest {

    private ContactUsPage contactUsPage;

    @BeforeEach
    void setUpContactPage() {
        contactUsPage = new ContactUsPage(browserSession.getPage());
        contactUsPage.open();

        PageAssertions.assertPageReady(
                browserSession.getPage(), contactUsPage
        );
    }

    @Test
    @DisplayName("Contact form submission without an attachment shows success")
    void shouldConfirmSubmissionWithoutAttachment() {
        contactUsPage.fillContactForm(
                "Test User",
                "contact@example.com",
                "Product availability",
                "Please tell me when this product will be available."
        );

        browserSession.getPage().onceDialog(Dialog::accept);
        contactUsPage.clickSubmit();

        PageAssertions.assertElementVisibleWithText(
                contactUsPage.getSuccessMessage(),
                "Success! Your details have been submitted successfully."
        );
    }

    @Test
    @DisplayName("Contact form submission requires an email")
    @Tag("negative")
    void shouldRejectSubmissionWithoutEmail() {
        contactUsPage.fillContactForm(
                "Test User",
                "",
                "Product availability",
                "Please tell me when this product will be available."
        );
        contactUsPage.clickSubmit();

        PageAssertions.assertFieldValidationError(
                contactUsPage.getEmail(),
                PageAssertions.ValidationError.VALUE_MISSING
        );
    }

    @Test
    @DisplayName("Contact form submission rejects an invalid email format")
    @Tag("negative")
    void shouldRejectSubmissionWithInvalidEmail() {
        contactUsPage.fillContactForm(
                "Test User",
                "invalid-email",
                "Product availability",
                "Please tell me when this product will be available."
        );
        contactUsPage.clickSubmit();

        PageAssertions.assertFieldValidationError(
                contactUsPage.getEmail(),
                PageAssertions.ValidationError.TYPE_MISMATCH
        );
    }

    @Test
    @DisplayName("Contact form submission with an attachment shows success")
    void shouldConfirmSubmissionWithAttachment(@TempDir Path tempDir)
            throws IOException {
        Path attachment = Files.writeString(
                tempDir.resolve("contact-details.txt"),
                "Additional details for the test contact request."
        );

        contactUsPage.fillContactForm(
                "Test User",
                "contact@example.com",
                "Product availability",
                "Please see the attached file."
        );
        contactUsPage.chooseFile(attachment.toString());

        browserSession.getPage().onceDialog(Dialog::accept);
        contactUsPage.clickSubmit();

        PageAssertions.assertElementVisibleWithText(
                contactUsPage.getSuccessMessage(),
                "Success! Your details have been submitted successfully."
        );
    }

    @Test
    @DisplayName("Cancelling contact submission preserves the entered data")
    void shouldPreserveFormDataWhenSubmissionIsCancelled() {
        String name = "Test User";
        String email = "contact@example.com";
        String subject = "Product availability";
        String message = "Please tell me when this product will be available.";

        contactUsPage.fillContactForm(name, email, subject, message);

        AtomicBoolean confirmationDismissed = new AtomicBoolean(false);

        browserSession.getPage().onceDialog(dialog -> {
            dialog.dismiss();
            confirmationDismissed.set(true);
        });
        contactUsPage.clickSubmit();

        assertTrue(
                confirmationDismissed.get(),
                "Expected the confirmation dialog to appear and be dismissed"
        );

        PageAssertions.assertPageReady(
                browserSession.getPage(), contactUsPage
        );

        assertThat(contactUsPage.getName()).hasValue(name);
        assertThat(contactUsPage.getEmail()).hasValue(email);
        assertThat(contactUsPage.getSubject()).hasValue(subject);
        assertThat(contactUsPage.getMessage()).hasValue(message);
        assertThat(contactUsPage.getSuccessMessage()).isHidden();
    }

}