package framework.pages;

import com.microsoft.playwright.Page;

public class AccountCreatedPage extends ConfirmationPage {

    private static final String URL = BASE_URL + "/account_created";

    public AccountCreatedPage(Page page) {
        super(page);
    }


//----Public interface----

    @Override
    public String getTargetUrl() {
        return URL;
    }

}
