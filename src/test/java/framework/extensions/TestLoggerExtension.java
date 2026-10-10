package framework.extensions;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class TestLoggerExtension implements
        BeforeAllCallback,
        AfterAllCallback,
        BeforeTestExecutionCallback,
        TestWatcher {



    private static final Logger log = LoggerFactory.getLogger(TestLoggerExtension.class);

    @Override
    public void beforeAll(ExtensionContext context) {
        log.info("===== SUITE START: {} =====", context.getRequiredTestClass().getSimpleName());
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        log.info("----- START: {} -----", context.getRequiredTestMethod().getName());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        log.info("===== SUITE END: {} =====", context.getRequiredTestClass().getSimpleName());
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        log.info("PASSED: {}", context.getRequiredTestMethod().getName());
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        log.error("FAILED: {}", context.getRequiredTestMethod().getName(), cause);
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        log.warn("ABORTED: {}", context.getRequiredTestMethod().getName(), cause);
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        log.info("DISABLED: {}. Reason: {}",
                context.getDisplayName(),
                reason.orElse("Not specified"));
    }
}