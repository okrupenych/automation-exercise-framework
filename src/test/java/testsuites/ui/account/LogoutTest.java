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
public class LogoutTest extends BaseTest {

    private SignupLoginPage signupLoginPage;
    private AccountFlows accountFlows;
    private AccountData account;

    private boolean accountCreationAttempted;
    private boolean accountCreated;

    @BeforeEach
    void setUpLoggedInUser() {
        accountCreationAttempted = false;
        accountCreated = false;

        account = new AccountData(
                "Mr",
                "Test",
                "User",
                "logout-" + UUID.randomUUID() + "@example.com",
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

        accountFlows.login(account.credentials(), account.fullName());
    }

    @Test
    @DisplayName("Logout returns the user to the login page and survives reload")
    void shouldRemainLoggedOutAfterReload() {
        signupLoginPage.navigationBar().clickLogout();

        assertLoggedOut();

        browserSession.getPage().reload();

        assertLoggedOut();
    }

    @Test
    @DisplayName("A user can log in again after logging out")
    void shouldLoginAgainAfterLogout() {
        signupLoginPage.navigationBar().clickLogout();

        assertLoggedOut();

        accountFlows.login(account.credentials(), account.fullName());
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

    private void assertLoggedOut() {
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                signupLoginPage
        );
        PageAssertions.assertElementAbsent(
                signupLoginPage.navigationBar().getLoggedInAs()
        );
    }

}