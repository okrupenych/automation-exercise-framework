package assertions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class PageAssertions {

    private static final Logger log = LoggerFactory.getLogger(PageAssertions.class);

    private PageAssertions() {
        // No need to create an object, all methods are static
    }

    public static void assertPageElementsVisible(List<Locator> elements) {
        log.info("Verifying that required page elements are visible");
        for (Locator element : elements) {
            assertThat(element).isVisible();
        }
    }

    public static void assertPageUrl(Page page, String expectedUrl) {
        log.info("Verifying that page URL is '{}'", expectedUrl);
        assertThat(page).hasURL(expectedUrl);
    }

    public static void assertPageUrl(Page page, Pattern expectedUrl) {
        log.info("Verifying page URL matches '{}'", expectedUrl);
        assertThat(page).hasURL(expectedUrl);
    }

    public static void assertElementText(Locator locator, String expected) {
        log.info("Verifying element text: '{}'", expected);
        assertThat(locator).hasText(expected);
    }

    public static void assertElementTexts(Locator locator, List<String> expected) {
        log.info("Verifying element texts: {}", expected);
        assertEquals(expected, locator.allTextContents());
    }

    public static void assertItemsCount(List<?> items, int expectedCount) {
        log.info("Verifying items count: expected {}", expectedCount);
        assertEquals(expectedCount, items.size());
    }

}
