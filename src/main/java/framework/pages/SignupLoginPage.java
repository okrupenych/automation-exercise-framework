package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class SignupLoginPage extends BasePage {

    private static final String URL = BASE_URL + "/login";

    private final Locator inptLoginEmail;
    private final Locator inptLoginPassword;
    private final Locator lblLoginError;
    private final Locator btnLoginSubmit;

    private final Locator inptSignUpName;
    private final Locator inptSignUpEmail;
    private final Locator lblSignUpError;
    private final Locator btnSignUpSubmit;

    public SignupLoginPage(Page page) {
        super(page);
        Locator rootLoginForm = page.locator("div.login-form");
        Locator rootSignupForm = page.locator("div.signup-form");

        inptLoginEmail = rootLoginForm.locator("input[data-qa='login-email']");
        inptLoginPassword = rootLoginForm.locator("input[data-qa='login-password']");
        lblLoginError = rootLoginForm.locator("form p");
        btnLoginSubmit = rootLoginForm.locator("button[data-qa='login-button']");

        inptSignUpName = rootSignupForm.locator("input[data-qa='signup-name']");
        inptSignUpEmail = rootSignupForm.locator("input[data-qa='signup-email']");
        lblSignUpError = rootSignupForm.locator("form p");
        btnSignUpSubmit = rootSignupForm.locator("button[data-qa='signup-button']");
    }


//----Public interface----

    @Override
    public String getTargetUrl() {
        return URL;
    }

    // Login form
    public void fillLoginEmail(String value) {
        inptLoginEmail.fill(value);
    }

    public void fillLoginPassword(String value) {
        inptLoginPassword.fill(value);
    }

    public void clickLoginSubmit() {
        btnLoginSubmit.click();
    }

    public Locator getLoginError() {
        return lblLoginError;
    }

    // Sign Up form
    public void fillSignupName(String value) {
        inptSignUpName.fill(value);
    }

    public void fillSignupEmail(String value) {
        inptSignUpEmail.fill(value);
    }

    public void clickSignUpSubmit() {
        btnSignUpSubmit.click();
    }

    public Locator getSignupError() {
        return lblSignUpError;
    }

}
