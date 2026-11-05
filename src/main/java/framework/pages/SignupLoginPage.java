package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import data.Credentials;
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


//----Public Interface----

    @Override
    public String getTargetUrl() {
        return URL;
    }

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


    // Login form
    public Locator getLoginHeader() {
        return lblHeaderLogin;
    }

    public Locator getLoginError() {
        return lblLoginError;
    }

    public Locator getLoginEmail() {
        return inptLoginEmail;
    }

    public Locator getLoginPassword() {
        return inptLoginPassword;
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


    // Sign Up form
    public Locator getSignupHeader() {
        return lblHeaderSignup;
    }

    public Locator getSignupError() {
        return lblSignupError;
    }

    public Locator getSignupName() {
        return inptSignupName;
    }

    public Locator getSignupEmail() {
        return inptSignupEmail;
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


    // Composite actions
    public void submitLogin(Credentials credentials) {
        fillLoginEmail(credentials.email());
        fillLoginPassword(credentials.password());
        clickLoginSubmit();
    }

    public void submitSignup(String name, String email) {
        fillSignupName(name);
        fillSignupEmail(email);
        clickSignupSubmit();
    }

}
