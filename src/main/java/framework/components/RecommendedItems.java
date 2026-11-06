package framework.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.List;
import java.util.Objects;

public class RecommendedItems {

    private final Locator rootRecommendedItems;
    private final Locator lblHeader;

    public RecommendedItems(Page page) {
        rootRecommendedItems = page.locator("div.recommended_items");
        lblHeader = rootRecommendedItems.locator("h2.title");
    }


//----Public Interface----

    public List<Locator> getRequiredElements() {
        return List.of(lblHeader);
    }

    public Locator getHeader() {
        return lblHeader;
    }

    public void scrollIntoView() {
        lblHeader.scrollIntoViewIfNeeded();
    }

    public void showFirstSlide() {
        rootRecommendedItems.evaluate(
                "el => window.jQuery(el).find('#recommended-item-carousel').carousel('pause').carousel(0)"
        );
        rootRecommendedItems.locator("#recommended-item-carousel .item:first-child.active .col-sm-4")
                .first().waitFor();
    }

    public ProductItem itemByIndex(int index) {
        Locator activeItem = rootRecommendedItems
                .locator("#recommended-item-carousel .item.active .col-sm-4").nth(index);
        int productId = Integer.parseInt(Objects.requireNonNull(
                activeItem.locator("a.add-to-cart").getAttribute("data-product-id"),
                "Recommended product is missing data-product-id"));

        // Keep the selected product when the carousel advances to another slide.
        return new ProductItem(rootRecommendedItems.locator(
                ".col-sm-4:has(a[data-product-id='" + productId + "'])"));
    }

}
