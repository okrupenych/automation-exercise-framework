package framework.api;

import com.microsoft.playwright.options.FormData;
import data.Credentials;
import data.AccountData;

public final class AccountFormMapper {

    private AccountFormMapper() {
    }

    public static FormData toForm(Credentials credentials) {
        FormData form = FormData.create();

        setIfPresent(form, "email", credentials.email());
        setIfPresent(form, "password", credentials.password());

        return form;
    }

    public static FormData toForm(AccountData account) {
        FormData form = toForm(account.credentials());

        setIfPresent(form, "name", account.fullName());
        setIfPresent(form, "title", account.title());
        setIfPresent(form, "birth_date", account.birthDay());
        setIfPresent(form, "birth_month", account.birthMonth());
        setIfPresent(form, "birth_year", account.birthYear());
        setIfPresent(form, "firstname", account.firstName());
        setIfPresent(form, "lastname", account.lastName());
        setIfPresent(form, "company", account.company());
        setIfPresent(form, "address1", account.address1());
        setIfPresent(form, "address2", account.address2());
        setIfPresent(form, "country", account.country());
        setIfPresent(form, "zipcode", account.zipcode());
        setIfPresent(form, "state", account.state());
        setIfPresent(form, "city", account.city());
        setIfPresent(form, "mobile_number", account.mobileNumber());

        return form;
    }

    private static void setIfPresent(FormData form, String name, String value) {
        if (value != null) {
            form.set(name, value);
        }
    }
}