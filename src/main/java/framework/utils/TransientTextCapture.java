package framework.utils;

import com.microsoft.playwright.Page;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class TransientTextCapture {

    private TransientTextCapture() {
    }

    public static String capture(Page page, String selector, Runnable action) {
        CompletableFuture<String> capturedText = new CompletableFuture<>();
        String bindingName = "captureText_" + UUID.randomUUID().toString().replace("-", "");

        page.exposeBinding(bindingName, (source, args) -> {
            capturedText.complete((String) args[0]);
            return "";
        });

        page.evaluate("""
                ([selector, bindingName]) => {
                    const capture = window[bindingName];
                    const target = document.querySelector(selector);

                    const observer = new MutationObserver(() => {
                        const element = document.querySelector(selector);

                        if (element &&
                            element.getClientRects().length > 0 &&
                            element.innerText.trim()) {

                            capture(element.innerText.trim());
                            observer.disconnect();
                        }
                    });

                    observer.observe(document.documentElement, {
                        subtree: true,
                        childList: true,
                        attributes: true,
                        characterData: true
                    });
                }
                """, new Object[]{selector, bindingName});

        action.run();

        page.waitForCondition(
                capturedText::isDone,
                new Page.WaitForConditionOptions().setTimeout(5000)
        );

        return capturedText.join();
    }
}
