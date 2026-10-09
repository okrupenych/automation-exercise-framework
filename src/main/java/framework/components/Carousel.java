package framework.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.util.regex.Pattern;

public class Carousel {

    private final Page page;

    private final Locator btnNext;
    private final Locator btnPrevious;
    private final Locator btnTestCases;
    private final Locator btnAPIList;
    private final Locator btnIndicator;

    public Carousel(Page page) {
        this.page = page;

        Locator rootCarousel = page.locator("#slider-carousel");
        Locator rootActiveSlide = page.locator(".carousel-inner .item.active");

        btnNext = rootCarousel.locator("[data-slide='next']");
        btnPrevious = rootCarousel.locator("[data-slide='prev']");

        btnTestCases = rootActiveSlide.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Test Cases")).first();
        btnAPIList = rootActiveSlide.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("APIs list for practice")).first();

        btnIndicator = page.getByRole(AriaRole.LISTITEM).filter(new Locator.FilterOptions().setHasText(Pattern.compile("^$")));
    }


//----Public interface----

    public void clickPrevious() {
        btnPrevious.click();
    }

    public void clickNext() {
        btnNext.click();
    }

    public void clickTestCases() {
        btnTestCases.click();
    }

    public void clickAPIList() {
        btnAPIList.click();
    }

    public void clickIndicator(int i) {
        btnIndicator.nth(i).click();

        page.waitForFunction("""
            () => !document.querySelector(
                '#slider-carousel .item.left, #slider-carousel .item.right'
            )
        """);
    }

}
