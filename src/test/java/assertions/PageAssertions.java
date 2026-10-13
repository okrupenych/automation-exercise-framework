package assertions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public final class PageAssertions {

    private PageAssertions() {
    }

    public static void assertElementsVisible(List<Locator> elements) {
        for (Locator element : elements) {
            assertThat(element).isVisible();
        }
    }

    public static void assertUrl(Page page, String expectedUrl) {
        assertThat(page).hasURL(expectedUrl);
    }

}
