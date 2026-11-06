package testsuites.ui.navigation;

import assertions.PageAssertions;
import base.BaseTest;
import framework.pages.HomePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("ui")
@Tag("navigation")
public class ScrollNavigationTest extends BaseTest {

    private HomePage homePage;

    @BeforeEach
    void setUpHomePage() {
        homePage = new HomePage(browserSession.getPage());
        homePage.open();

        PageAssertions.assertPageReady(
                browserSession.getPage(), homePage
        );
    }


    @Test
    @DisplayName("Scroll Up button returns the page to the top")
    void shouldReturnToTopUsingScrollUpButton() {
        homePage.scrollToBottom();

        PageAssertions.assertElementInViewport(
                homePage.footer().getHeader()
        );

        double initialScrollY = getScrollY();

        assertTrue(
                initialScrollY > 0,
                "Expected the page to be scrolled down"
        );

        homePage.clickScrollUp();

        double finalScrollY = getScrollY();

        assertTrue(
                Math.abs(finalScrollY) <= 1,
                "Expected the page at the top, actual scrollY: " + finalScrollY
        );
    }

    @Test
    @DisplayName("Scrolling directly returns the page to the top")
    void shouldReturnToTopWithoutUsingScrollUpButton() {
        homePage.scrollToBottom();

        PageAssertions.assertElementInViewport(
                homePage.footer().getHeader()
        );

        double initialScrollY = getScrollY();

        assertTrue(
                initialScrollY > 0,
                "Expected the page to be scrolled down"
        );

        homePage.scrollToTop();

        double finalScrollY = getScrollY();

        assertTrue(
                Math.abs(finalScrollY) <= 1,
                "Expected the page at the top, actual scrollY: " + finalScrollY
        );
    }


//----Private helpers----

    private double getScrollY() {
        return ((Number) Objects.requireNonNull(
                browserSession.getPage().evaluate("window.scrollY"),
                "Expected window.scrollY to return a number"
        )).doubleValue();
    }

}
