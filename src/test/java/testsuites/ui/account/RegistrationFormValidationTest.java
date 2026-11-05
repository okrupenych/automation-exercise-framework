package testsuites.ui.account;

import assertions.PageAssertions;
import base.BaseTest;
import com.microsoft.playwright.Locator;
import data.AccountData;
import framework.pages.SignupLoginPage;
import framework.pages.SignupPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Tag("ui")
@Tag("account")
public class RegistrationFormValidationTest extends BaseTest {

    private SignupPage signupPage;
    private AccountData account;

    @BeforeEach
    void openRegistrationForm() {
        account = new AccountData(
                "Mr",
                "Test",
                "User",
                "validation-" + UUID.randomUUID() + "@example.com",
                "TestPassword123!",
                "15",
                "6",
                "1990",
                "Test Company",
                "123 Test Street",
                "Apartment 4",
                "United States",
                "California",
                "Los Angeles",
                "90001",
                "2025550100",
                null
        );

        SignupLoginPage signupLoginPage =
                new SignupLoginPage(browserSession.getPage());
        signupPage = new SignupPage(browserSession.getPage());

        signupLoginPage.open();
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                signupLoginPage
        );

        signupLoginPage.submitSignup(account.fullName(), account.email());

        PageAssertions.assertPageReady(
                browserSession.getPage(),
                signupPage
        );
    }


    @Test
    @DisplayName("Signup preserves the email and prevents editing it")
    void shouldPreserveEmailAsDisabledField() {
        assertThat(signupPage.getEmail()).hasValue(account.email());
        assertThat(signupPage.getEmail()).isDisabled();
    }

    @Test
    @DisplayName("Registration requires an account name")
    @Tag("negative")
    void shouldRejectRegistrationWithoutName() {
        fillValidRegistration();
        signupPage.fillName("");

        assertRequiredFieldValidation(signupPage.getName());
    }

    @Test
    @DisplayName("Registration requires a password")
    @Tag("negative")
    void shouldRejectRegistrationWithoutPassword() {
        fillValidRegistration();
        signupPage.fillPassword("");

        assertRequiredFieldValidation(signupPage.getPassword());
    }

    @Test
    @DisplayName("Registration requires a first name")
    @Tag("negative")
    void shouldRejectRegistrationWithoutFirstName() {
        fillValidRegistration();
        signupPage.fillFirstName("");

        assertRequiredFieldValidation(signupPage.getFirstName());
    }

    @Test
    @DisplayName("Registration requires a last name")
    @Tag("negative")
    void shouldRejectRegistrationWithoutLastName() {
        fillValidRegistration();
        signupPage.fillLastName("");

        assertRequiredFieldValidation(signupPage.getLastName());
    }

    @Test
    @DisplayName("Registration requires an address")
    @Tag("negative")
    void shouldRejectRegistrationWithoutAddress() {
        fillValidRegistration();
        signupPage.fillAddress("");

        assertRequiredFieldValidation(signupPage.getAddress());
    }

    @Test
    @DisplayName("Registration requires a state")
    @Tag("negative")
    void shouldRejectRegistrationWithoutState() {
        fillValidRegistration();
        signupPage.fillState("");

        assertRequiredFieldValidation(signupPage.getState());
    }

    @Test
    @DisplayName("Registration requires a city")
    @Tag("negative")
    void shouldRejectRegistrationWithoutCity() {
        fillValidRegistration();
        signupPage.fillCity("");

        assertRequiredFieldValidation(signupPage.getCity());
    }

    @Test
    @DisplayName("Registration requires a zipcode")
    @Tag("negative")
    void shouldRejectRegistrationWithoutZipcode() {
        fillValidRegistration();
        signupPage.fillZipcode("");

        assertRequiredFieldValidation(signupPage.getZipcode());
    }

    @Test
    @DisplayName("Registration requires a mobile number")
    @Tag("negative")
    void shouldRejectRegistrationWithoutMobileNumber() {
        fillValidRegistration();
        signupPage.fillMobileNumber("");

        assertRequiredFieldValidation(signupPage.getMobileNumber());
    }


//----Private helpers----

    private void fillValidRegistration() {
        signupPage.fillRegistration(account, false, false);
    }

    private void assertRequiredFieldValidation(Locator field) {
        signupPage.clickCreateAccount();

        PageAssertions.assertFieldValidationError(
                field,
                PageAssertions.ValidationError.VALUE_MISSING
        );
        PageAssertions.assertPageUrl(
                browserSession.getPage(),
                signupPage.getTargetUrl()
        );
    }

}