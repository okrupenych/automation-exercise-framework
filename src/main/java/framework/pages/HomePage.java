package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import framework.components.Carousel;
import framework.components.CartModal;
import framework.components.ProductFilterBar;
import framework.components.ProductGrid;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends BasePage {

    private static final String URL = BASE_URL;

    private final Carousel cmpCarousel;
    private final ProductFilterBar cmpProductFilterBar;
    private final ProductGrid cmpProductGrid;
    private final CartModal cmpCartModal;

    public HomePage(Page page) {
        super(page);
        cmpCarousel = new Carousel(page);
        cmpProductFilterBar = new ProductFilterBar(page);
        cmpProductGrid = new ProductGrid(page);
        cmpCartModal = new CartModal(page);
    }

//----Public interface----

    @Override
    public List<Locator> getRequiredElements() {
        List<Locator> elements = new ArrayList<>();

        elements.addAll(cmpProductFilterBar.getRequiredElements());
        elements.addAll(cmpProductGrid.getRequiredElements());

        return elements;
    }

    @Override
    public String getTargetUrl() {
        return URL;
    }


    // Built-in components
    public Carousel carousel() {
        return cmpCarousel;
    }

    public ProductFilterBar productsFilterBar() {
        return cmpProductFilterBar;
    }

    public ProductGrid productGrid() {
        return cmpProductGrid;
    }

    public CartModal cartModal() {
        return cmpCartModal;
    }

}
