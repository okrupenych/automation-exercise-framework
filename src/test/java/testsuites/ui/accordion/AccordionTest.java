package testsuites.ui.accordion;

import assertions.PageAssertions;
import base.BaseTest;
import com.microsoft.playwright.Page;
import framework.components.Accordion;
import framework.pages.ApiTestingPage;
import framework.pages.TestCasesPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("ui")
@Tag("accordion")
public class AccordionTest extends BaseTest {

    private Page page;
    private TestCasesPage testCasesPage;
    private ApiTestingPage apiTestingPage;


    @BeforeEach
    void setUpPages() {
        page = browserSession.getPage();
        testCasesPage = new TestCasesPage(page);
        apiTestingPage = new ApiTestingPage(page);
    }


    @Test
    @DisplayName("Test Cases accordion opens and closes every section")
    void shouldOpenAndCloseAllTestCases() {
        testCasesPage.open();
        PageAssertions.assertPageReady(page, testCasesPage);

        Accordion accordion = testCasesPage.accordion();
        int itemCount = accordion.getItemCount();
        assertTrue(itemCount > 0, "Test Cases accordion should contain sections");

        accordion.openAll();

        for (int i = 0; i < itemCount; i++) {
            PageAssertions.assertElementVisible(accordion.getItemContent(i));
            assertThat(accordion.getItemContent(i)).hasClass(Pattern.compile(".*\\bin\\b.*"));
        }

        accordion.closeAll();

        for (int i = 0; i < itemCount; i++) {
            assertThat(accordion.getItemContent(i)).isHidden();
        }
    }

    @Test
    @DisplayName("Test Cases accordion opens and closes a section by title")
    void shouldOpenAndCloseTestCaseByTitle() {
        testCasesPage.open();
        PageAssertions.assertPageReady(page, testCasesPage);

        Accordion accordion = testCasesPage.accordion();
        String title = "Test Case 2: Login User with correct email and password";

        accordion.openItem(title);

        PageAssertions.assertElementVisible(accordion.getItemContent(title));
        assertThat(accordion.getItemContent(title)).hasClass(Pattern.compile(".*\\bin\\b.*"));

        accordion.closeItem(title);

        assertThat(accordion.getItemContent(title)).isHidden();
    }


    @Test
    @DisplayName("API Testing accordion opens and closes every section")
    void shouldOpenAndCloseAllApiSections() {
        apiTestingPage.open();
        PageAssertions.assertPageReady(page, apiTestingPage);

        Accordion accordion = apiTestingPage.accordion();
        int itemCount = accordion.getItemCount();
        assertTrue(itemCount > 0, "API Testing accordion should contain sections");

        accordion.openAll();

        for (int i = 0; i < itemCount; i++) {
            PageAssertions.assertElementVisible(accordion.getItemContent(i));
            assertThat(accordion.getItemContent(i)).hasClass(Pattern.compile(".*\\bin\\b.*"));
        }

        accordion.closeAll();

        for (int i = 0; i < itemCount; i++) {
            assertThat(accordion.getItemContent(i)).isHidden();
        }
    }

    @Test
    @DisplayName("API Testing accordion opens and closes a section by title")
    void shouldOpenAndCloseApiSectionByTitle() {
        apiTestingPage.open();
        PageAssertions.assertPageReady(page, apiTestingPage);

        Accordion accordion = apiTestingPage.accordion();
        String title = "API 1: Get All Products List";

        accordion.openItem(title);

        PageAssertions.assertElementVisible(accordion.getItemContent(title));
        assertThat(accordion.getItemContent(title)).hasClass(Pattern.compile(".*\\bin\\b.*"));

        accordion.closeItem(title);

        assertThat(accordion.getItemContent(title)).isHidden();
    }

}
