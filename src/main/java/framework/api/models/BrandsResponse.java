package framework.api.models;

import java.util.List;

public record BrandsResponse(
        Integer responseCode,
        List<Brand> brands
) {
    public record Brand(Integer id, String brand) {
    }
}