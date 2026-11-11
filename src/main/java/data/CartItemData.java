package data;

import framework.utils.PriceUtils;

public record CartItemData(
        int productId,
        String name,
        String unitPrice,
        int quantity
) {
    public String total() {
        return PriceUtils.multiply(unitPrice, quantity);
    }

}
