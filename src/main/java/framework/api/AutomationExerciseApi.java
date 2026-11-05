package framework.api;

import com.microsoft.playwright.options.FormData;
import com.microsoft.playwright.options.RequestOptions;
import data.AccountData;
import data.Credentials;

public class AutomationExerciseApi {

    private final ApiClient client;

    public AutomationExerciseApi(ApiClient client) {
        this.client = client;
    }

    public ApiResult getProducts() {
        return client.send("GET", "productsList", RequestOptions.create());
    }

    public ApiResult getBrands() {
        return client.send("GET", "brandsList", RequestOptions.create());
    }

    public ApiResult searchProducts(String query) {
        FormData form = FormData.create();
        if (query != null) {
            form.set("search_product", query);
        }

        return client.send("POST", "searchProduct",
                RequestOptions.create().setMultipart(form));
    }

    public ApiResult verifyLogin(Credentials credentials) {
        return client.send("POST", "verifyLogin",
                RequestOptions.create().setMultipart(AccountFormMapper.toForm(credentials)));
    }

    public ApiResult createAccount(AccountData account) {
        return client.send("POST", "createAccount",
                RequestOptions.create().setMultipart(AccountFormMapper.toForm(account)));
    }

    public ApiResult deleteAccount(Credentials credentials) {
        return client.send("DELETE", "deleteAccount",
                RequestOptions.create().setMultipart(AccountFormMapper.toForm(credentials)));
    }

    public ApiResult updateAccount(AccountData account) {
        return client.send("PUT", "updateAccount",
                RequestOptions.create().setMultipart(AccountFormMapper.toForm(account)));
    }

    public ApiResult getAccountByEmail(String email) {
        RequestOptions options = RequestOptions.create();
        if (email != null) {
            options.setQueryParam("email", email);
        }

        return client.send("GET", "getUserDetailByEmail", options);
    }

}
