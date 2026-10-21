package framework.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Pattern;

public class Carousel {

    private static final Logger log = LoggerFactory.getLogger(Carousel.class);

    private final Page page;

    private final Locator btnNext;
    private final Locator btnPrevious;
    private final Locator btnTestCases;
    private final Locator btnAPIList;
    private final Locator btnIndicator;
    private final Locator lblHeading;

    public Carousel(Page page) {
        this.page = page;

        Locator rootCarousel = page.locator("#slider-carousel");
        Locator rootActiveSlide = rootCarousel.locator(".carousel-inner .item.active");
        lblHeading = rootActiveSlide.locator("h2");

        btnNext = rootCarousel.locator("[data-slide='next']");
        btnPrevious = rootCarousel.locator("[data-slide='prev']");

        btnTestCases = rootActiveSlide.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Test Cases")).first();
        btnAPIList = rootActiveSlide.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("APIs list for practice")).first();

        btnIndicator = page.getByRole(AriaRole.LISTITEM).filter(new Locator.FilterOptions().setHasText(Pattern.compile("^$")));
    }


//----Public interface----

    public List<Locator> getRequiredElements() {
        return List.of(); // No stable elements to verify
    }

    public void clickPrevious() {
        log.info("Clicking previous slide");
        btnPrevious.click();
    }

    public Locator getHeading() {
        return lblHeading;
    }

    public void clickNext() {
        log.info("Clicking next slide");
        btnNext.click();
    }

    public void clickTestCases() {
        log.info("Clicking \"Test Cases\" button");
        btnTestCases.click();
    }

    public void clickAPIList() {
        log.info("Clicking \"APIs list for practice\" button");
        btnAPIList.click();
    }

    public void clickIndicator(int i) {
        log.info("Clicking indicator {}", i);
        btnIndicator.nth(i).click();

        log.trace("Waiting for slide transition after selecting indicator {}", i);
        page.waitForFunction("""
            () => !document.querySelector(
                '#slider-carousel .item.left, #slider-carousel .item.right'
            )
        """);
        log.debug("Slide transition completed for indicator {}", i);
    }

}
