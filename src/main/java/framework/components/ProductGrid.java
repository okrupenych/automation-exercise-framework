package framework.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.ArrayList;
import java.util.List;

public class ProductGrid {

    private final Locator rootProductItems;

    public ProductGrid(Page page) {
        Locator rootProductGrid = page.locator("div.features_items");
        rootProductItems = rootProductGrid.locator("div.col-sm-4");
    }


//----Public Interface----

    public List<Locator> getRequiredElements() {
        return new ArrayList<>(itemByIndex(0).getRequiredElements());
    }

    public List<ProductItem> getAllItems() {
        List<ProductItem> items = new ArrayList<>();

        for (int i = 0; i < rootProductItems.count(); i++) {
            items.add(new ProductItem(rootProductItems.nth(i)));
        }

        return items;
    }

    public ProductItem itemByIndex(int index) {
        return new ProductItem(rootProductItems.nth(index));
    }

    public ProductItem itemByName(String productName) {
        return new ProductItem(
                rootProductItems.filter(
                        new Locator.FilterOptions().setHasText(productName)
                )
        );
    }

    public ProductItem itemByExactName(String productName) {
        // This page is owned by the caller and must not be closed here.
        @SuppressWarnings("resource")
        Page page = rootProductItems.page();
        Locator matches = rootProductItems.filter(new Locator.FilterOptions().setHas(
                page.locator("div.productinfo p").and(page.getByText(
                        productName, new Page.GetByTextOptions().setExact(true)))));
        int count = matches.count();
        if (count != 1) {
            throw new IllegalStateException("Expected one card for product \"" + productName
                    + "\", found " + count);
        }
        return new ProductItem(matches);
    }

}
