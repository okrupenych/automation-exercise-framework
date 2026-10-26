package framework.api.models;

import java.util.List;

public record ProductsResponse(
        Integer responseCode,
        List<Product> products
) {
    public record Product(
            Integer id,
            String name,
            String price,
            String brand,
            Category category
    ) {
    }

    public record Category(
            UserType usertype,
            String category
    ) {
    }

    public record UserType(
            String usertype
    ) {
    }
}