package framework.pages;

import com.microsoft.playwright.Page;
import data.CartItemData;
import framework.browser.OverlayHandler;
import framework.components.Carousel;
import framework.components.RecommendedItems;

import java.util.List;

public class HomePage extends ShopPage {

    private static final String URL = BASE_URL + "/";

    private final Carousel cmpCarousel;
    private final RecommendedItems cmpRecommendedItems;

    public HomePage(Page page) {
        this(page, new OverlayHandler(page));
    }

    public HomePage(Page page, OverlayHandler overlayHandler) {
        super(page, overlayHandler);
        cmpCarousel = new Carousel(page);
        cmpRecommendedItems = new RecommendedItems(page);
    }


//----Public Interface----

    @Override
    public String getTargetUrl() {
        return URL;
    }


    // Composite actions
    public void addRecommendedCartItems(List<CartItemData> items) {
        cmpRecommendedItems.scrollIntoView();
        cmpRecommendedItems.showFirstSlide();
        for (CartItemData item : items) {
            for (int i = 0; i < item.quantity(); i++) {
                cmpRecommendedItems.itemByProductId(item.productId()).clickAddToCart();
                modal().waitUntilVisible();
                modal().clickDismissButton();
                modal().waitUntilHidden();
            }
        }
    }


    // Built-in components
    public Carousel carousel() {
        return cmpCarousel;
    }

    public RecommendedItems recommendedItems() {
        return cmpRecommendedItems;
    }

}
