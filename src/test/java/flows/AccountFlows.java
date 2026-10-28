package flows;

import assertions.PageAssertions;
import com.microsoft.playwright.Page;

import data.AccountData;
import data.Credentials;

import framework.pages.AccountCreatedPage;
import framework.pages.SignupLoginPage;
import framework.pages.SignupPage;
import framework.pages.AccountDeletedPage;


public final class AccountFlows {

    private final Page page;

    private final SignupLoginPage signupLoginPage;
    private final SignupPage signupPage;
    private final AccountCreatedPage accountCreatedPage;
    private final AccountDeletedPage accountDeletedPage;

    public AccountFlows(Page page) {
        this.page = page;

        signupLoginPage = new SignupLoginPage(page);
        signupPage = new SignupPage(page);
        accountCreatedPage = new AccountCreatedPage(page);
        accountDeletedPage = new AccountDeletedPage(page);
    }


//----Public interface----

    // Precondition: SignupLoginPage is open and ready for signup.
    public void register(AccountData account, boolean newsletter, boolean specialOffers) {
        signupLoginPage.submitSignup(account.fullName(), account.email());

        PageAssertions.assertPageReady(page, signupPage);
        PageAssertions.assertElementVisibleWithText(
                signupPage.getAccountHeader(),
                "Enter Account Information"
        );

        signupPage.fillRegistration(account, newsletter, specialOffers);
        signupPage.clickCreateAccount();

        PageAssertions.assertPageReady(page, accountCreatedPage);
        PageAssertions.assertElementVisibleWithText(
                accountCreatedPage.getHeader(),
                "Account Created!"
        );

        accountCreatedPage.clickContinueButton();

        PageAssertions.assertElementVisibleWithText(
                accountCreatedPage.navigationBar().getLoggedInAs(),
                "Logged in as " + account.fullName()
        );
    }

    // Precondition: SignupLoginPage is open and ready for login.
    public void login(Credentials credentials, String expectedName) {
        signupLoginPage.submitLogin(credentials);

        PageAssertions.assertElementVisibleWithText(
                signupLoginPage.navigationBar().getLoggedInAs(),
                "Logged in as " + expectedName
        );
    }

    // Precondition: user is logged in and the navigation bar is available.
    public void deleteAccount() {
        accountDeletedPage.navigationBar().clickDeleteAccount();

        PageAssertions.assertPageReady(page, accountDeletedPage);
        PageAssertions.assertElementVisibleWithText(
                accountDeletedPage.getHeader(),
                "Account Deleted!"
        );

        accountDeletedPage.clickContinueButton();
    }

}