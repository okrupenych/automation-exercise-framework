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


//----Public interface----

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

}
