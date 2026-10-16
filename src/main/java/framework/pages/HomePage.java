package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import framework.components.*;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends BasePage {

    private static final String URL = BASE_URL + "/";

    private final Carousel cmpCarousel;
    private final ProductFilterBar cmpProductFilterBar;
    private final ProductGrid cmpProductGrid;
    private final Modal cmpModal;

    public HomePage(Page page) {
        super(page);
        cmpCarousel = new Carousel(page);
        cmpProductFilterBar = new ProductFilterBar(page);
        cmpProductGrid = new ProductGrid(page);
        cmpModal = new Modal(page);
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

    public Modal modal() {
        return cmpModal;
    }

}
