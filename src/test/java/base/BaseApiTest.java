package base;

import framework.api.ApiClient;
import framework.api.ApiSession;
import framework.api.AutomationExerciseApi;
import framework.extensions.LogArchiveExtension;
import framework.extensions.TestLoggerExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({
        LogArchiveExtension.class,
        TestLoggerExtension.class
})
public abstract class BaseApiTest {

    private ApiSession session;
    protected AutomationExerciseApi api;

    @BeforeEach
    protected void setUpApi() {
        session = new ApiSession();
        try {
            api = new AutomationExerciseApi(new ApiClient(session.getRequestContext()));
        } catch (RuntimeException | Error failure) {
            try {
                tearDownApi();
            } catch (RuntimeException | Error cleanupFailure) {
                failure.addSuppressed(cleanupFailure);
            }
            throw failure;
        }
    }

    @AfterEach
    protected void tearDownApi() {
        ApiSession sessionToClose = session;
        session = null;
        api = null;
        if (sessionToClose != null) {
            sessionToClose.close();
        }
    }
}
