package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SignupLoginPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(SignupLoginPage.class);

    private static final String URL = BASE_URL + "/login";

    private final Locator lblHeaderLogin;
    private final Locator inptLoginEmail;
    private final Locator inptLoginPassword;
    private final Locator lblLoginError;
    private final Locator btnLoginSubmit;

    private final Locator lblHeaderSignup;
    private final Locator inptSignupName;
    private final Locator inptSignupEmail;
    private final Locator lblSignupError;
    private final Locator btnSignupSubmit;

    public SignupLoginPage(Page page) {
        super(page);
        Locator rootLoginForm = page.locator("div.login-form");
        Locator rootSignupForm = page.locator("div.signup-form");

        lblHeaderLogin = rootLoginForm.locator("h2");
        inptLoginEmail = rootLoginForm.locator("input[data-qa='login-email']");
        inptLoginPassword = rootLoginForm.locator("input[data-qa='login-password']");
        lblLoginError = rootLoginForm.locator("form p");
        btnLoginSubmit = rootLoginForm.locator("button[data-qa='login-button']");

        lblHeaderSignup = rootSignupForm.locator("h2");
        inptSignupName = rootSignupForm.locator("input[data-qa='signup-name']");
        inptSignupEmail = rootSignupForm.locator("input[data-qa='signup-email']");
        lblSignupError = rootSignupForm.locator("form p");
        btnSignupSubmit = rootSignupForm.locator("button[data-qa='signup-button']");
    }


//----Public interface----

    @Override
    public List<Locator> getRequiredElements() {
        return List.of(
                lblHeaderLogin,
                inptLoginEmail,
                inptLoginPassword,
                btnLoginSubmit,
                lblHeaderSignup,
                inptSignupName,
                inptSignupEmail,
                btnSignupSubmit
        );
    }

    @Override
    public String getTargetUrl() {
        return URL;
    }


    // Login form
    public Locator getLoginHeader() {
        return lblHeaderLogin;
    }

    public void fillLoginEmail(String value) {
        log.info("Filling login email");
        inptLoginEmail.fill(value);
    }

    public void fillLoginPassword(String value) {
        log.info("Filling login password");
        inptLoginPassword.fill(value);
    }

    public void clickLoginSubmit() {
        log.info("Clicking \"Login\" button");
        btnLoginSubmit.click();
    }

    public Locator getLoginError() {
        return lblLoginError;
    }


    // Sign Up form
    public Locator getSignupHeader() {
        return lblHeaderSignup;
    }

    public void fillSignupName(String value) {
        log.info("Filling signup name");
        inptSignupName.fill(value);
    }

    public void fillSignupEmail(String value) {
        log.info("Filling signup email");
        inptSignupEmail.fill(value);
    }

    public void clickSignupSubmit() {
        log.info("Clicking \"Signup\" button");
        btnSignupSubmit.click();
    }

    public Locator getSignupError() {
        return lblSignupError;
    }

}
