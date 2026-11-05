package framework.reporting;

import framework.api.ApiResult;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import io.qameta.allure.util.ResultsUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.UUID;

/** Reporting only: preserve return values and propagate the original throwable. */
@Aspect
public class FrameworkStepsAspect {
    @Around("execution(public * framework.pages..*(..))"
            + " || execution(public * framework.components..*(..))"
            + " || execution(public * flows..*(..))"
            + " || execution(public * assertions..*(..))"
            + " || execution(public * framework.api.ApiClient.send(..))")
    public Object reportStep(ProceedingJoinPoint call) throws Throwable {
        if (Allure.getLifecycle().getCurrentTestCaseOrStep().isEmpty()) {
            return call.proceed();
        }
        String name = call.getSignature().getDeclaringType().getSimpleName()
                + "." + call.getSignature().getName();
        boolean apiRequest = call.getSignature().getDeclaringTypeName().equals("framework.api.ApiClient");
        if (apiRequest) {
            // Deliberately omit request options, credentials and response bodies.
            name = "API " + call.getArgs()[0] + " " + call.getArgs()[1];
        }
        String stepId = UUID.randomUUID().toString();
        Allure.getLifecycle().startStep(stepId, new StepResult().setName(name));
        long started = System.nanoTime();
        try {
            Object result = call.proceed();
            if (apiRequest && result instanceof ApiResult response) {
                Allure.addAttachment("HTTP response summary", "text/plain",
                        "HTTP " + response.httpStatus() + "\nDuration: "
                                + (System.nanoTime() - started) / 1_000_000 + " ms", ".txt");
            }
            Allure.getLifecycle().updateStep(stepId, step -> step.setStatus(Status.PASSED));
            return result;
        } catch (Throwable failure) {
            Allure.getLifecycle().updateStep(stepId, step -> step
                    .setStatus(ResultsUtils.getStatus(failure).orElse(Status.BROKEN))
                    .setStatusDetails(ResultsUtils.getStatusDetails(failure).orElse(null)));
            throw failure;
        } finally {
            Allure.getLifecycle().stopStep(stepId);
        }
    }

}
