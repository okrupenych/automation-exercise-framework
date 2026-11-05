package testsuites.ui.account;

import assertions.PageAssertions;
import base.BaseTest;
import data.AccountData;
import data.Credentials;
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
public class LoginTest extends BaseTest {

    private SignupLoginPage signupLoginPage;
    private AccountFlows accountFlows;
    private AccountData account;

    private boolean accountCreationAttempted;
    private boolean accountCreated;

    @BeforeEach
    void setUpLogin() {
        accountCreationAttempted = false;
        accountCreated = false;

        account = new AccountData(
                "Mr",
                "Test",
                "User",
                "login-" + UUID.randomUUID() + "@example.com",
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

        try (ApiSession session = new ApiSession()) {
            AutomationExerciseApi api = new AutomationExerciseApi(
                    new ApiClient(session.getRequestContext())
            );

            accountCreationAttempted = true;
            var result = api.createAccount(account);

            assertEquals(
                    200,
                    result.httpStatus(),
                    "Unexpected setup HTTP status: " + result.body()
            );
            assertEquals(
                    Integer.valueOf(201),
                    result.as(MessageResponse.class).responseCode(),
                    "Failed to create the test account: " + result.body()
            );

            accountCreated = true;
        }

        signupLoginPage = new SignupLoginPage(browserSession.getPage());
        accountFlows = new AccountFlows(browserSession.getPage());

        signupLoginPage.open();
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                signupLoginPage
        );
    }

    @AfterEach
    void deleteTestAccount() {
        if (!accountCreationAttempted) {
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
                    "Unexpected cleanup HTTP status: " + result.body()
            );

            Integer responseCode =
                    result.as(MessageResponse.class).responseCode();

            if (accountCreated) {
                assertEquals(
                        Integer.valueOf(200),
                        responseCode,
                        "Failed to delete the test account: " + result.body()
                );
            } else {
                assertTrue(
                        Integer.valueOf(200).equals(responseCode)
                                || Integer.valueOf(404).equals(responseCode),
                        "Unexpected cleanup response: " + result.body()
                );
            }
        }
    }


    @Test
    @DisplayName("An existing user can log in with valid credentials")
    @Tag("smoke")
    void shouldLoginWithValidCredentials() {
        accountFlows.login(account.credentials(), account.fullName());
    }

    @Test
    @DisplayName("Login rejects an incorrect password")
    @Tag("negative")
    void shouldRejectIncorrectPassword() {
        signupLoginPage.submitLogin(
                new Credentials(account.email(), "WrongPassword123!")
        );

        assertLoginRejected();
    }

    @Test
    @DisplayName("Login rejects an unregistered email")
    @Tag("negative")
    void shouldRejectUnregisteredEmail() {
        signupLoginPage.submitLogin(
                new Credentials(
                        "unknown-" + UUID.randomUUID() + "@example.com",
                        account.password()
                )
        );

        assertLoginRejected();
    }

    @Test
    @DisplayName("Login requires an email")
    @Tag("negative")
    void shouldRejectLoginWithoutEmail() {
        signupLoginPage.submitLogin(
                new Credentials("", account.password())
        );

        PageAssertions.assertFieldValidationError(
                signupLoginPage.getLoginEmail(),
                PageAssertions.ValidationError.VALUE_MISSING
        );
        assertLoginPageRetained();
    }

    @Test
    @DisplayName("Login requires a password")
    @Tag("negative")
    void shouldRejectLoginWithoutPassword() {
        signupLoginPage.submitLogin(
                new Credentials(account.email(), "")
        );

        PageAssertions.assertFieldValidationError(
                signupLoginPage.getLoginPassword(),
                PageAssertions.ValidationError.VALUE_MISSING
        );
        assertLoginPageRetained();
    }

    @Test
    @DisplayName("Login rejects an invalid email format")
    @Tag("negative")
    void shouldRejectLoginWithInvalidEmail() {
        signupLoginPage.submitLogin(
                new Credentials("invalid-email", account.password())
        );

        PageAssertions.assertFieldValidationError(
                signupLoginPage.getLoginEmail(),
                PageAssertions.ValidationError.TYPE_MISMATCH
        );
        assertLoginPageRetained();
    }


//----Private helpers----

    private void assertLoginRejected() {
        PageAssertions.assertElementVisibleWithText(
                signupLoginPage.getLoginError(),
                "Your email or password is incorrect!"
        );
        assertLoginPageRetained();
    }

    private void assertLoginPageRetained() {
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                signupLoginPage
        );
        PageAssertions.assertElementAbsent(
                signupLoginPage.navigationBar().getLoggedInAs()
        );
    }

}