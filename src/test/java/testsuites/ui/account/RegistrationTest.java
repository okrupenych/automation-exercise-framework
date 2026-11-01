package testsuites.ui.account;

import assertions.PageAssertions;
import base.BaseTest;
import data.AccountData;
import flows.AccountFlows;
import framework.api.ApiClient;
import framework.api.ApiSession;
import framework.api.AutomationExerciseApi;
import framework.api.models.MessageResponse;
import framework.pages.SignupLoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("ui")
@Tag("account")
public class RegistrationTest extends BaseTest {

    private SignupLoginPage signupLoginPage;
    private AccountFlows accountFlows;

    private AccountData account;
    private boolean registrationAttempted;
    private boolean registrationCompleted;

    @BeforeEach
    void setUpRegistration() {
        registrationAttempted = false;
        registrationCompleted = false;

        account = new AccountData(
                "Mr",
                "Test",
                "User",
                "registration-" + UUID.randomUUID() + "@example.com",
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

        signupLoginPage = new SignupLoginPage(browserSession.getPage());
        accountFlows = new AccountFlows(browserSession.getPage());

        signupLoginPage.open();
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                signupLoginPage
        );
    }

    @Test
    @DisplayName("A new user can register with valid account details")
    @Tag("smoke")
    void shouldRegisterWithValidDetails() {
        registrationAttempted = true;

        accountFlows.register(account, false, false);

        registrationCompleted = true;
    }

    @Test
    @DisplayName("Signup requires a name")
    @Tag("negative")
    void shouldRejectSignupWithoutName() {
        signupLoginPage.submitSignup("", account.email());

        PageAssertions.assertFieldValidationError(
                signupLoginPage.getSignupName(),
                PageAssertions.ValidationError.VALUE_MISSING
        );
        PageAssertions.assertPageUrl(
                browserSession.getPage(),
                signupLoginPage.getTargetUrl()
        );
    }

    @Test
    @DisplayName("Signup requires an email")
    @Tag("negative")
    void shouldRejectSignupWithoutEmail() {
        signupLoginPage.submitSignup(account.fullName(), "");

        PageAssertions.assertFieldValidationError(
                signupLoginPage.getSignupEmail(),
                PageAssertions.ValidationError.VALUE_MISSING
        );
        PageAssertions.assertPageUrl(
                browserSession.getPage(),
                signupLoginPage.getTargetUrl()
        );
    }

    @Test
    @DisplayName("Signup rejects an invalid email format")
    @Tag("negative")
    void shouldRejectSignupWithInvalidEmail() {
        signupLoginPage.submitSignup(account.fullName(), "invalid-email");

        PageAssertions.assertFieldValidationError(
                signupLoginPage.getSignupEmail(),
                PageAssertions.ValidationError.TYPE_MISMATCH
        );
        PageAssertions.assertPageUrl(
                browserSession.getPage(),
                signupLoginPage.getTargetUrl()
        );
    }

    @Test
    @DisplayName("Signup rejects an already registered email")
    @Tag("negative")
    void shouldRejectSignupWithExistingEmail() {
        try (ApiSession session = new ApiSession()) {
            AutomationExerciseApi api = new AutomationExerciseApi(
                    new ApiClient(session.getRequestContext())
            );

            registrationAttempted = true;

            var result = api.createAccount(account);

            assertEquals(
                    200,
                    result.httpStatus(),
                    "Unexpected HTTP status during account setup: " + result.body()
            );

            MessageResponse response = result.as(MessageResponse.class);

            assertEquals(
                    Integer.valueOf(201),
                    response.responseCode(),
                    "Failed to create the prerequisite account: " + result.body()
            );

            registrationCompleted = true;
        }

        signupLoginPage.submitSignup("Another User", account.email());

        PageAssertions.assertElementVisibleWithText(
                signupLoginPage.getSignupError(),
                "Email Address already exist!"
        );
        PageAssertions.assertPageElementsVisible(
                signupLoginPage.getRequiredElements()
        );
    }

    @AfterEach
    void deleteTestAccount() {
        if (!registrationAttempted) {
            return;
        }

        try (ApiSession session = new ApiSession()) {
            AutomationExerciseApi api = new AutomationExerciseApi(
                    new ApiClient(session.getRequestContext())
            );

            var result = api.deleteAccount(account.credentials());

            assertEquals(
                    200,
                    result.httpStatus(),
                    "Unexpected HTTP status during account cleanup: " + result.body()
            );

            MessageResponse response = result.as(MessageResponse.class);

            if (registrationCompleted) {
                assertEquals(
                        Integer.valueOf(200),
                        response.responseCode(),
                        "Failed to delete the registered account: " + result.body()
                );
            } else {
                assertTrue(
                        Integer.valueOf(200).equals(response.responseCode())
                                || Integer.valueOf(404).equals(response.responseCode()),
                        "Unexpected account cleanup response: " + result.body()
                );
            }
        }
    }

}