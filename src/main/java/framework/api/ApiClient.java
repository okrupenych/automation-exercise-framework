package framework.api;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import com.microsoft.playwright.PlaywrightException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ApiClient {

    private static final Logger log = LoggerFactory.getLogger(ApiClient.class);

    private final APIRequestContext requestContext;

    public ApiClient(APIRequestContext requestContext) {
        this.requestContext = requestContext;
    }

    public ApiResult send(String method, String path, RequestOptions options) {
        long startedAt = System.nanoTime();

        log.info("API request: {} {}", method, path);

        try {
            APIResponse response = requestContext.fetch(
                    path,
                    options.setMethod(method)
                            .setFailOnStatusCode(false)
            );

            long elapsedMs = (System.nanoTime() - startedAt) / 1_000_000;

            log.info("API response: {} {} → HTTP {} ({} ms)",
                    method, path, response.status(), elapsedMs);

            try {
                return new ApiResult(
                        response.status(),
                        response.text()
                );
            } finally {
                response.dispose();
            }
        } catch (PlaywrightException e) {
            long elapsedMs = (System.nanoTime() - startedAt) / 1_000_000;

            log.error("API request failed: {} {} ({} ms)",
                    method, path, elapsedMs);

            throw e;
        }
    }

}
