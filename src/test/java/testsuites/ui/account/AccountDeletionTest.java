package testsuites.ui.account;

import assertions.PageAssertions;
import base.BaseTest;
import data.AccountData;
import flows.AccountFlows;
import framework.api.ApiClient;
import framework.api.ApiSession;
import framework.api.AutomationExerciseApi;
import framework.api.models.MessageResponse;
import framework.pages.HomePage;
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
public class AccountDeletionTest extends BaseTest {

    private SignupLoginPage signupLoginPage;
    private HomePage homePage;
    private AccountFlows accountFlows;
    private AccountData account;

    private boolean accountCreationAttempted;

    @BeforeEach
    void setUpLoggedInUser() {
        accountCreationAttempted = false;

        account = new AccountData(
                "Mr",
                "Test",
                "User",
                "deletion-" + UUID.randomUUID() + "@example.com",
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
        }

        signupLoginPage = new SignupLoginPage(browserSession.getPage());
        homePage = new HomePage(browserSession.getPage());
        accountFlows = new AccountFlows(browserSession.getPage());

        signupLoginPage.open();
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                signupLoginPage
        );

        accountFlows.login(account.credentials(), account.fullName());
    }

    @AfterEach
    void cleanUpTestAccount() {
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

            assertTrue(
                    Integer.valueOf(200).equals(responseCode)
                            || Integer.valueOf(404).equals(responseCode),
                    "Unexpected cleanup response: " + result.body()
            );
        }
    }


    @Test
    @DisplayName("A deleted account can no longer be used to log in")
    @Tag("negative")
    void shouldDeleteAccountAndRejectSubsequentLogin() {
        accountFlows.deleteAccount();

        PageAssertions.assertPageReady(
                browserSession.getPage(),
                homePage
        );
        PageAssertions.assertElementAbsent(
                homePage.navigationBar().getLoggedInAs()
        );

        homePage.navigationBar().clickSignupLogin();

        PageAssertions.assertPageReady(
                browserSession.getPage(),
                signupLoginPage
        );

        signupLoginPage.submitLogin(account.credentials());

        PageAssertions.assertElementVisibleWithText(
                signupLoginPage.getLoginError(),
                "Your email or password is incorrect!"
        );
        PageAssertions.assertPageReady(
                browserSession.getPage(),
                signupLoginPage
        );
        PageAssertions.assertElementAbsent(
                signupLoginPage.navigationBar().getLoggedInAs()
        );
    }

}