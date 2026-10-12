package framework.pages;

import com.microsoft.playwright.Page;

public class AccountDeletedPage extends ConfirmationPage {

    private static final String URL = BASE_URL + "/delete_account";

    public AccountDeletedPage(Page page) {
        super(page);
    }


//----Public interface----

    @Override
    public String getTargetUrl() {
        return URL;
    }

}
