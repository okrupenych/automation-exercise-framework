package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import framework.browser.OverlayHandler;
import framework.components.ProductFilterBar;
import framework.components.ProductGrid;
import framework.components.Modal;

import java.util.ArrayList;
import java.util.List;

public abstract class ShopPage extends BasePage {

    private final ProductFilterBar cmpProductFilterBar;
    private final ProductGrid cmpProductGrid;
    private final Modal cmpModal;

    private final Locator lblHeader;

    protected ShopPage(Page page) {
        this(page, new OverlayHandler(page));
    }

    protected ShopPage(Page page, OverlayHandler overlayHandler) {
        super(page, overlayHandler);
        cmpProductFilterBar = new ProductFilterBar(page);
        cmpProductGrid = new ProductGrid(page);
        cmpModal = new Modal(page);
        lblHeader = page.locator("div.features_items").locator("h2.title");
    }


//----Public Interface----

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


    // Composite actions
    public void addProductsToCart(List<String> productNames) {
        if (productNames == null) {
            throw new IllegalArgumentException("Product names must not be null");
        }
        List<String> names = new ArrayList<>(productNames);
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Product name at index " + i + " must not be null or blank");
            }
        }
        for (String name : names) {
            try {
                cmpProductGrid.itemByExactName(name).clickAddToCart();
                cmpModal.waitUntilVisible();
                cmpModal.clickDismissButton();
                cmpModal.waitUntilHidden();
            } catch (RuntimeException e) {
                throw new IllegalStateException("Failed to add product \"" + name + "\" to cart: " + e.getMessage(), e);
            }
        }
    }


    // Built-in components
    public Modal modal() {
        return cmpModal;
    }

    public ProductFilterBar productsFilterBar() {
        return cmpProductFilterBar;
    }

    public ProductGrid productGrid() {
        return cmpProductGrid;
    }

}
