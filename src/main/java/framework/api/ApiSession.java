package framework.api;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.APIRequest;
import framework.config.Config;

public class ApiSession implements AutoCloseable {

    private final Playwright playwright;
    private final APIRequestContext requestContext;

    public ApiSession() {
        String baseUrl = Config.getApiBaseUrl();
        int timeout = Config.getApiTimeout();

        playwright = Playwright.create();

        try {
            requestContext = playwright.request().newContext(
                    new APIRequest.NewContextOptions()
                            .setBaseURL(baseUrl)
                            .setTimeout(timeout)
            );
        } catch (RuntimeException e) {
            playwright.close();
            throw e;
        }
    }


    public APIRequestContext getRequestContext() {
        return requestContext;
    }

    @Override
    public void close() {
        try {
            requestContext.dispose();
        } finally {
            playwright.close();
        }
    }


}