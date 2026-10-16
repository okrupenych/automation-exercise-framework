package framework.components;

import com.microsoft.playwright.Locator;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private final Locator rootCart;

    public Cart(Locator rootCart) {
        this.rootCart = rootCart;
    }


    //----Public interface----

    public List<Locator> getRequiredElements() {
        return List.of(rootCart);
    }


    // Products in the cart
    public CartItem itemByIndex(int index) {
        return new CartItem(rootCart.locator("tr[id^='product-']").nth(index));
    }

    public CartItem itemByProductId(int productId) {
        return new CartItem(rootCart.locator("tr#product-" + productId));
    }

    public CartItem itemByName(String productName) {
        return new CartItem(rootCart.locator("tr").filter(
                new Locator.FilterOptions().setHasText(productName)
        ));
    }

    public List<CartItem> getAllItems() {
        Locator rows = rootCart.locator("tr[id^='product-']");
        List<CartItem> items = new ArrayList<>();

        for (int i = 0; i < rows.count(); i++) {
            items.add(new CartItem(rows.nth(i)));
        }

        return items;
    }
}