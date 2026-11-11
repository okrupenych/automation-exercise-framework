package assertions;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Download;
import com.microsoft.playwright.Page;
import framework.components.CartItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Pattern;
import java.util.Locale;
import java.util.Objects;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import framework.components.ProductGrid;
import framework.pages.BasePage;
import framework.components.Cart;
import framework.pages.CheckoutPage;
import data.AccountData;
import data.CartItemData;

public final class PageAssertions {


//----Fields and types----

    private static final Logger log = LoggerFactory.getLogger(PageAssertions.class);

    public enum ValidationError {
        VALUE_MISSING("valueMissing"),
        TYPE_MISMATCH("typeMismatch");

        private final String property;

        ValidationError(String property) {
            this.property = property;
        }
    }


//----Construction----

    private PageAssertions() {
        // No need to create an object, all methods are static
    }


//----Internal implementation----

    private static void assertCartItemDetails(CartItem item, CartItemData expected) {
        assertElementVisibleWithText(item.getName(), expected.name());
        assertElementText(item.getPrice(), expected.unitPrice());
        assertElementText(
                item.getQuantity(),
                Integer.toString(expected.quantity())
        );
        assertElementText(item.getTotal(), expected.total());
    }

    private static void assertAddress(
            Locator name,
            Locator addressLines,
            Locator stateAndCity,
            Locator country,
            Locator phone,
            AccountData expected
    ) {
        assertElementText(name, expected.title() + ". " + expected.fullName());

        assertElementTexts(
                addressLines,
                List.of(
                        Objects.toString(expected.company(), ""),
                        Objects.toString(expected.address1(), ""),
                        Objects.toString(expected.address2(), "")
                )
        );

        assertElementText(
                stateAndCity,
                expected.city() + " " + expected.state() + " " + expected.zipcode()
        );
        assertElementText(country, expected.country());
        assertElementText(phone, expected.mobileNumber());
    }


//----Public Interface----

    public static void assertElementVisible(Locator element) {
        log.info("Verifying that required element is visible");
        assertThat(element).isVisible();
    }

    public static void assertPageUrl(Page page, String expectedUrl) {
        log.info("Verifying that page URL is '{}'", expectedUrl);
        assertThat(page).hasURL(expectedUrl);
    }

    public static void assertPageUrl(Page page, Pattern expectedUrl) {
        log.info("Verifying page URL matches '{}'", expectedUrl);
        assertThat(page).hasURL(expectedUrl);
    }

    public static void assertElementText(Locator locator, String expected) {
        log.info("Verifying element text: '{}'", expected);
        assertThat(locator).hasText(expected);
    }

    public static void assertElementTexts(Locator locator, List<String> expected) {
        log.info("Verifying element texts: {}", expected);
        assertEquals(expected, locator.allTextContents());
    }

    public static void assertItemsCount(List<?> items, int expectedCount) {
        log.info("Verifying items count: expected {}", expectedCount);
        assertEquals(expectedCount, items.size());
    }

    public static void assertElementAbsent(Locator element) {
        log.info("Verifying that element is absent");
        assertThat(element).hasCount(0);
    }


//----Composite assertions----

    public static void assertElementInViewport(Locator element) {
        log.info("Verifying that element is in the viewport");
        assertThat(element).isVisible();
        assertThat(element).isInViewport();
    }

    public static void assertDownloadSuccessful(Download download) {
        log.info("Verifying that file download completed successfully");
        assertNull(download.failure(), "Download should complete without errors");
        assertTrue(java.nio.file.Files.isRegularFile(download.path()),
                "Downloaded file should exist");
    }

    public static void assertDownloadSuccessful(Download download, String expectedFilename) {
        assertDownloadSuccessful(download);
        assertEquals(
                expectedFilename,
                download.suggestedFilename(),
                "Downloaded filename should match"
        );
    }

    public static void assertPageElementsVisible(List<Locator> elements) {
        log.info("Verifying that required page elements are visible");
        for (Locator element : elements) {
            assertThat(element).isVisible();
        }
    }

    public static void assertPageReady(Page page, BasePage targetPage, String expectedUrl) {
        assertPageUrl(page, expectedUrl);
        assertPageElementsVisible(targetPage.getRequiredElements());
    }

    public static void assertPageReady(Page page, BasePage targetPage, Pattern expectedUrl) {
        assertPageUrl(page, expectedUrl);
        assertPageElementsVisible(targetPage.getRequiredElements());
    }

    public static void assertPageReady(Page page, BasePage targetPage) {
        assertPageReady(page, targetPage, targetPage.getTargetUrl());
    }

    public static void assertElementVisibleWithText(Locator element, String expectedText) {
        assertElementVisible(element);
        assertElementText(element, expectedText);
    }

    public static void assertElementInViewportWithText(Locator element, String expectedText) {
        assertElementInViewport(element);
        assertElementText(element, expectedText);
    }

    public static void assertCartContents(Cart cart, List<CartItemData> expectedItems) {
        assertItemsCount(cart.getAllItems(), expectedItems.size());

        for (CartItemData expected : expectedItems) {
            assertCartItemDetails(
                    cart.itemByProductId(expected.productId()),
                    expected
            );
        }
    }

    public static void assertSearchResults(ProductGrid productGrid, String searchQuery) {
        var products = productGrid.getAllItems();
        assertFalse(products.isEmpty(), "Search should return products");

        String expected = searchQuery.toLowerCase(Locale.ROOT);

        for (var product : products) {
            assertElementVisible(product.getName());

            String nameText = product.getName().textContent();
            assertNotNull(nameText, "Product name text should not be null");
            String actualName = nameText.trim();
            assertTrue(
                    actualName.toLowerCase(Locale.ROOT).contains(expected),
                    "Product should match search '" + searchQuery + "': " + actualName
            );
        }
    }

    public static void assertDeliveryAddress(CheckoutPage checkoutPage, AccountData expected) {
        assertAddress(
                checkoutPage.getDeliveryAddressName(),
                checkoutPage.getDeliveryAddressLines(),
                checkoutPage.getDeliveryAddressStateAndCity(),
                checkoutPage.getDeliveryAddressCountry(),
                checkoutPage.getDeliveryAddressPhone(),
                expected
        );
    }

    public static void assertBillingAddress(CheckoutPage checkoutPage, AccountData expected) {
        assertAddress(
                checkoutPage.getBillingAddressName(),
                checkoutPage.getBillingAddressLines(),
                checkoutPage.getBillingAddressStateAndCity(),
                checkoutPage.getBillingAddressCountry(),
                checkoutPage.getBillingAddressPhone(),
                expected
        );
    }

    public static void assertFieldValidationError(Locator field, ValidationError expectedError) {
        log.info("Verifying field validation error: {}", expectedError);

        assertEquals(
                Boolean.TRUE,
                field.evaluate(
                        "(el, property) => el.validity[property]",
                        expectedError.property
                ),
                "Expected validation error: " + expectedError
        );

        assertEquals(
                Boolean.TRUE,
                field.evaluate(
                        "el => el.validationMessage.length > 0"
                ),
                "Expected a non-empty validation message"
        );

        assertThat(field).isFocused();
    }

}
