package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import framework.browser.OverlayHandler;
import framework.components.ProductFilterBar;
import framework.components.ProductGrid;

import java.util.ArrayList;
import java.util.List;

public abstract class ShopPage extends BasePage {

    private final ProductFilterBar cmpProductFilterBar;
    private final ProductGrid cmpProductGrid;

    private final Locator lblHeader;

    protected ShopPage(Page page) {
        this(page, new OverlayHandler(page));
    }

    protected ShopPage(Page page, OverlayHandler overlayHandler) {
        super(page, overlayHandler);
        cmpProductFilterBar = new ProductFilterBar(page);
        cmpProductGrid = new ProductGrid(page);
        lblHeader = page.locator("div.features_items").locator("h2.title");
    }

//----Public interface----

    @Override
    public List<Locator> getRequiredElements() {
        List<Locator> elements = new ArrayList<>();

        elements.add(lblHeader);
        elements.addAll(cmpProductFilterBar.getRequiredElements());
        elements.addAll(cmpProductGrid.getRequiredElements());

        return elements;
    }

    public Locator getHeader() {
        return lblHeader;
    }


    // Built-in components
    public ProductFilterBar productsFilterBar() {
        return cmpProductFilterBar;
    }

    public ProductGrid productGrid() {
        return cmpProductGrid;
    }

}
