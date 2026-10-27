package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import data.AccountData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

public class SignupPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(SignupPage.class);

    private static final String URL = BASE_URL + "/signup";

    private final Locator lblHeaderAccount;
    private final Locator rbtnMr;
    private final Locator rbtnMrs;
    private final Locator inptName;
    private final Locator inptEmail;
    private final Locator inptPassword;
    private final Locator slctDay;
    private final Locator slctMonth;
    private final Locator slctYear;
    private final Locator chkNewsletter;
    private final Locator chkSpecialOffers;

    private final Locator lblHeaderAddress;
    private final Locator inptFirstName;
    private final Locator inptLastName;
    private final Locator inptCompany;
    private final Locator inptAddress;
    private final Locator inptAddress2;
    private final Locator slctCountry;
    private final Locator inptState;
    private final Locator inptCity;
    private final Locator inptZipcode;
    private final Locator inptMobileNumber;

    private final Locator btnCreateAccount;

    public SignupPage(Page page) {
        super(page);

        Locator rootForm = page.locator("div.login-form");

        lblHeaderAccount = rootForm.locator("> h2.title");
        rbtnMr = rootForm.locator("#id_gender1");
        rbtnMrs = rootForm.locator("#id_gender2");
        inptName = rootForm.locator("#name");
        inptEmail = rootForm.locator("#email");
        inptPassword = rootForm.locator("#password");
        slctDay = rootForm.locator("#days");
        slctMonth = rootForm.locator("#months");
        slctYear = rootForm.locator("#years");
        chkNewsletter = rootForm.locator("#newsletter");
        chkSpecialOffers = rootForm.locator("#optin");

        lblHeaderAddress = rootForm.locator("form > h2.title");
        inptFirstName = rootForm.locator("#first_name");
        inptLastName = rootForm.locator("#last_name");
        inptCompany = rootForm.locator("#company");
        inptAddress = rootForm.locator("#address1");
        inptAddress2 = rootForm.locator("#address2");
        slctCountry = rootForm.locator("#country");
        inptState = rootForm.locator("#state");
        inptCity = rootForm.locator("#city");
        inptZipcode = rootForm.locator("#zipcode");
        inptMobileNumber = rootForm.locator("#mobile_number");

        btnCreateAccount = rootForm.locator("button[data-qa='create-account']");
    }


//----Internal implementation----
    private void fillIfPresent(String value, Consumer<String> action) {
        if (value != null) {
            action.accept(value);
        }
    }


//----Public interface----

    @Override
    public List<Locator> getRequiredElements() {
        return List.of(
                lblHeaderAccount,
                rbtnMr,
                rbtnMrs,
                inptName,
                inptEmail,
                inptPassword,
                slctDay,
                slctMonth,
                slctYear,
                chkNewsletter,
                chkSpecialOffers,
                lblHeaderAddress,
                inptFirstName,
                inptLastName,
                inptCompany,
                inptAddress,
                inptAddress2,
                slctCountry,
                inptState,
                inptCity,
                inptZipcode,
                inptMobileNumber,
                btnCreateAccount
        );
    }

    @Override
    public String getTargetUrl() {
        return URL;
    }


    // Account info section
    public Locator getAccountHeader() {
        return lblHeaderAccount;
    }

    public Locator getEmail() {
        return inptEmail;
    }

    public void selectTitle(String title) {
        log.info("Selecting title: " + title);
        switch (title) {
            case "Mr" -> selectMr();
            case "Mrs" -> selectMrs();
            default -> throw new IllegalArgumentException("Unsupported title: " + title);
        }
    }

    public void selectMr() {
        log.info("Selecting title: Mr.");
        rbtnMr.check();
    }

    public void selectMrs() {
        log.info("Selecting title: Mrs.");
        rbtnMrs.check();
    }

    public void fillName(String value) {
        log.info("Filling account name");
        inptName.fill(value);
    }

    public void fillPassword(String value) {
        log.info("Filling account password");
        inptPassword.fill(value);
    }

    public void selectDateOfBirth(String day, String month, String year) {
        log.info("Selecting date of birth");

        slctDay.selectOption(day);
        slctMonth.selectOption(month);
        slctYear.selectOption(year);
    }

    public void setNewsletter(boolean value) {
        log.info("Setting newsletter subscription: {}", value);
        chkNewsletter.setChecked(value);
    }

    public void setSpecialOffers(boolean value) {
        log.info("Setting special offers subscription: {}", value);
        chkSpecialOffers.setChecked(value);
    }


    // Address info section
    public Locator getAddressHeader() {
        return lblHeaderAddress;
    }

    public void fillFirstName(String value) {
        log.info("Filling first name");
        inptFirstName.fill(value);
    }

    public void fillLastName(String value) {
        log.info("Filling last name");
        inptLastName.fill(value);
    }

    public void fillCompany(String value) {
        log.info("Filling company");
        inptCompany.fill(value);
    }

    public void fillAddress(String value) {
        log.info("Filling address");
        inptAddress.fill(value);
    }

    public void fillAddress2(String value) {
        log.info("Filling address2");
        inptAddress2.fill(value);
    }

    public void selectCountry(String value) {
        log.info("Selecting country");
        slctCountry.selectOption(value);
    }

    public void fillState(String value) {
        log.info("Filling state");
        inptState.fill(value);
    }

    public void fillCity(String value) {
        log.info("Filling city");
        inptCity.fill(value);
    }

    public void fillZipcode(String value) {
        log.info("Filling zipcode");
        inptZipcode.fill(value);
    }

    public void fillMobileNumber(String value) {
        log.info("Filling Mobile number");
        inptMobileNumber.fill(value);
    }


    // Submit button
    public void clickCreateAccount() {
        log.info("Clicking on \"Create account\" button");
        btnCreateAccount.click();
    }


    // Composite actions
    public void fillRegistration(AccountData account, boolean newsletter, boolean specialOffers) {
        fillIfPresent(account.title(), this::selectTitle);
        fillIfPresent(account.fullName(), this::fillName);
        fillIfPresent(account.password(), this::fillPassword);

        if (account.birthDay() != null
                && account.birthMonth() != null
                && account.birthYear() != null) {
            selectDateOfBirth(
                    account.birthDay(),
                    account.birthMonth(),
                    account.birthYear()
            );
        }

        setNewsletter(newsletter);
        setSpecialOffers(specialOffers);

        fillIfPresent(account.firstName(), this::fillFirstName);
        fillIfPresent(account.lastName(), this::fillLastName);
        fillIfPresent(account.company(), this::fillCompany);
        fillIfPresent(account.address1(), this::fillAddress);
        fillIfPresent(account.address2(), this::fillAddress2);
        fillIfPresent(account.country(), this::selectCountry);
        fillIfPresent(account.state(), this::fillState);
        fillIfPresent(account.city(), this::fillCity);
        fillIfPresent(account.zipcode(), this::fillZipcode);
        fillIfPresent(account.mobileNumber(), this::fillMobileNumber);
    }

}
