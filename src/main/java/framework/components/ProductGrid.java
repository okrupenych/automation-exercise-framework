package framework.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import java.util.ArrayList;
import java.util.List;

public class ProductGrid {

    private final Locator rootHeader;
    private final Locator rootProductItems;

    public ProductGrid(Page page) {
        Locator rootProductGrid = page.locator("div.features_items");
        rootHeader = rootProductGrid.locator("h2.title");
        rootProductItems = rootProductGrid.locator("div.col-sm-4");
    }


//----Public interface----

    public List<Locator> getRequiredElements() {
        List<Locator> elements = new ArrayList<>(List.of(
                rootHeader
        ));

        elements.addAll(itemByIndex(0).getRequiredElements());

        return elements;
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

    public Locator getHeader() {
        return rootHeader;
    }

}
