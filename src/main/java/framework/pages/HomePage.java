package framework.pages;

import com.microsoft.playwright.Page;
import framework.components.Carousel;
import framework.components.Modal;
import framework.components.RecommendedItems;

public class HomePage extends ShopPage {

    private static final String URL = BASE_URL + "/";

    private final Carousel cmpCarousel;
    private final Modal cmpModal;
    private final RecommendedItems cmpRecommendedItems;

    public HomePage(Page page) {
        super(page);
        cmpCarousel = new Carousel(page);
        cmpModal = new Modal(page);
        cmpRecommendedItems = new RecommendedItems(page);
    }

//----Public interface----

    @Override
    public String getTargetUrl() {
        return URL;
    }


    // Built-in components
    public Carousel carousel() {
        return cmpCarousel;
    }

    public Modal modal() {
        return cmpModal;
    }

    public RecommendedItems recommendedItems() {
        return cmpRecommendedItems;
    }

}
