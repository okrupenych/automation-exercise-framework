package testsuites.ui.subscription;

import assertions.PageAssertions;
import base.BaseTest;
import framework.components.Footer;
import framework.pages.HomePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("ui")
@Tag("subscription")
public class SubscriptionTest extends BaseTest {

    private Footer footer;

    @BeforeEach
    void setUpSubscriptionForm() {
        HomePage homePage = new HomePage(browserSession.getPage());
        homePage.open();

        PageAssertions.assertPageReady(
                browserSession.getPage(), homePage
        );

        homePage.scrollToBottom();
        footer = homePage.footer();

        PageAssertions.assertPageElementsVisible(
                footer.getRequiredElements()
        );
    }


    @Test
    @DisplayName("Subscription with a valid email shows a success confirmation")
    void shouldConfirmSubscriptionWithValidEmail() {
        footer.fillEmail("subscriber@example.com");
        footer.clickSubscribe();

        PageAssertions.assertElementVisibleWithText(
                footer.getSubscriptionMessage(),
                "You have been successfully subscribed!"
        );
    }

    @Test
    @DisplayName("Subscription requires an email")
    @Tag("negative")
    void shouldRejectSubscriptionWithoutEmail() {
        footer.fillEmail("");
        footer.clickSubscribe();

        PageAssertions.assertFieldValidationError(
                footer.getEmail(),
                PageAssertions.ValidationError.VALUE_MISSING
        );
    }

    @Test
    @DisplayName("Subscription rejects an invalid email format")
    @Tag("negative")
    void shouldRejectSubscriptionWithInvalidEmail() {
        footer.fillEmail("invalid-email");
        footer.clickSubscribe();

        PageAssertions.assertFieldValidationError(
                footer.getEmail(),
                PageAssertions.ValidationError.TYPE_MISMATCH
        );
    }

}