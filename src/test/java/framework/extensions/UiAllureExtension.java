package framework.extensions;

import base.BaseTest;
import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.playwright.AllurePlaywright;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/** Capture failures immediately, before subclass cleanup and the base context close. */
public class UiAllureExtension implements InvocationInterceptor {
    private static final Logger log = LoggerFactory.getLogger(UiAllureExtension.class);

    @Override
    public void interceptTestMethod(Invocation<Void> invocation,
            ReflectiveInvocationContext<Method> method, ExtensionContext context) throws Throwable {
        captureFailure(invocation, context);
    }

    @Override
    public void interceptTestTemplateMethod(Invocation<Void> invocation,
            ReflectiveInvocationContext<Method> method, ExtensionContext context) throws Throwable {
        captureFailure(invocation, context);
    }

    @Override
    public void interceptBeforeEachMethod(Invocation<Void> invocation,
            ReflectiveInvocationContext<Method> method, ExtensionContext context) throws Throwable {
        captureFailure(invocation, context);
    }

    @Override
    public void interceptAfterEachMethod(Invocation<Void> invocation,
            ReflectiveInvocationContext<Method> method, ExtensionContext context) throws Throwable {
        captureFailure(invocation, context);
    }

    private void captureFailure(Invocation<Void> invocation, ExtensionContext context) throws Throwable {
        try {
            invocation.proceed();
        } catch (Throwable originalFailure) {
            try {
                BaseTest test = (BaseTest) context.getRequiredTestInstance();
                if (test.getBrowserSession() != null) {
                    Page page = test.getBrowserSession().getPage();
                    // The adapter deduplicates failure attachments across lifecycle failures.
                    AllurePlaywright.afterTestFailure(originalFailure);
                    if (!page.isClosed()) {
                        Allure.addAttachment("Page URL", "text/plain", page.url(), ".txt");
                        Allure.addAttachment("Page title", "text/plain", page.title(), ".txt");
                    }
                }
            } catch (RuntimeException diagnosticFailure) {
                log.warn("Could not capture UI failure diagnostics", diagnosticFailure);
            }
            throw originalFailure;
        }
    }
}
