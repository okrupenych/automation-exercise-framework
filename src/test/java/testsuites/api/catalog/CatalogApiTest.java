package testsuites.api.catalog;

import base.BaseApiTest;
import framework.api.ApiResult;
import framework.api.models.BrandsResponse;
import framework.api.models.MessageResponse;
import framework.api.models.ProductsResponse;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("api")
@Tag("catalog")
public class CatalogApiTest extends BaseApiTest {

    @Test
    @Tag("smoke")
    void getProducts() {
        ApiResult result = api.getProducts();

        assertEquals(200, result.httpStatus());

        ProductsResponse response =
                result.as(ProductsResponse.class);

        assertNotNull(response);
        assertEquals(Integer.valueOf(200), response.responseCode());
        assertNotNull(response.products());
        assertFalse(response.products().isEmpty());
    }

    @Test
    @Tag("smoke")
    void getBrands() {
        ApiResult result = api.getBrands();

        assertEquals(200, result.httpStatus());

        BrandsResponse response =
                result.as(BrandsResponse.class);

        assertNotNull(response);
        assertEquals(Integer.valueOf(200), response.responseCode());
        assertNotNull(response.brands());
        assertFalse(response.brands().isEmpty());
    }

    @Test
    @Tag("smoke")
    void searchProducts() {
        ApiResult result = api.searchProducts("Blue");

        assertEquals(200, result.httpStatus());

        ProductsResponse response =
                result.as(ProductsResponse.class);

        assertNotNull(response);
        assertEquals(Integer.valueOf(200), response.responseCode());
        assertNotNull(response.products());
        assertFalse(response.products().isEmpty());
        var productNames = response.products().stream()
                .map(ProductsResponse.Product::name)
                .toList();
        assertTrue(productNames.contains("Blue Top"), "Blue search should include Blue Top: " + productNames);
        assertFalse(productNames.contains("Sleeveless Dress"),
                "Blue search should exclude Sleeveless Dress: " + productNames);
    }

    @Test
    @Tag("negative")
    void searchProductsWithoutQuery() {
        ApiResult result = api.searchProducts(null);

        assertEquals(200, result.httpStatus());

        MessageResponse response =
                result.as(MessageResponse.class);

        assertNotNull(response);
        assertEquals(Integer.valueOf(400), response.responseCode());
        assertEquals(
                "Bad request, search_product parameter is missing in POST request.",
                response.message()
        );
    }

}
