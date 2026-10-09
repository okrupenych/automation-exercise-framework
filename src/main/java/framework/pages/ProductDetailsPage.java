package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import framework.components.CartModal;
import framework.components.ProductFilterBar;

public class ProductDetailsPage extends BasePage {

    private static final String URL = BASE_URL + "/product_details/";

    private final ProductFilterBar cmpProductFilterBar;
    private final CartModal cmpCartModal;

    // Product Details block
    private final Locator imgProductPhoto;
    private final Locator lblProductName;
    private final Locator lblProductCategory;
    private final Locator lblProductPrice;
    private final Locator inptQuantity;
    private final Locator btnAddToCart;
    private final Locator lblAvailability;
    private final Locator lblCondition;
    private final Locator lblBrand;

    // "Write Your Review" block
    private final Locator inptName;
    private final Locator inptEmail;
    private final Locator inptReviewText;
    private final Locator btnSubmit;
    private final Locator lblSuccessMessage;

    public ProductDetailsPage(Page page) {
        super(page);

        cmpProductFilterBar = new ProductFilterBar(page);
        cmpCartModal = new CartModal(page);

        Locator rootProductDetails = page.locator("div.product-details");
        Locator rootProductInformation = rootProductDetails.locator("div.product-information");

        imgProductPhoto = rootProductDetails.locator("div.view-product img");
        lblProductName = rootProductInformation.locator("h2");
        lblProductCategory = rootProductInformation.locator("p").nth(0);
        lblProductPrice = rootProductInformation.locator("span > span");
        inptQuantity = rootProductInformation.locator("input#quantity");
        btnAddToCart = rootProductInformation.locator("button.cart");
        lblAvailability = rootProductInformation.locator("p:has(b:text('Availability:'))");
        lblCondition = rootProductInformation.locator("p:has(b:text('Condition:'))");
        lblBrand = rootProductInformation.locator("p:has(b:text('Brand:'))");

        Locator rootReviewSection = page.locator("div#reviews");

        inptName = rootReviewSection.locator("input#name");
        inptEmail = rootReviewSection.locator("input#email");
        inptReviewText = rootReviewSection.locator("textarea#review");
        btnSubmit = rootReviewSection.locator("button#button-review");
        lblSuccessMessage = rootReviewSection.locator("div.alert-success");
    }


//----Public interface----

    @Override
    public String getTargetUrl() {
        return URL;
    }

    @Override
    public void open() {
        throw new UnsupportedOperationException(
                "ProductDetailsPage requires productId"
        );
    }

    public void open(int productId) {
        page.navigate(getTargetUrl() + productId);
    }


    // Product details section
    public Locator getProductPhoto() {
        return imgProductPhoto;
    }

    public Locator getProductName() {
        return lblProductName;
    }

    public Locator getProductCategory() {
        return lblProductCategory;
    }

    public Locator getProductPrice() {
        return lblProductPrice;
    }

    public Locator getAvailability() {
        return lblAvailability;
    }

    public Locator getCondition() {
        return lblCondition;
    }

    public Locator getBrand() {
        return lblBrand;
    }

    public void fillQuantity(int quantity) {
        inptQuantity.fill(String.valueOf(quantity));
    }

    public void clickAddToCart() {
        btnAddToCart.click();
    }


    // "Write your review" form
    public Locator getReviewSuccessMessage() {
        return lblSuccessMessage;
    }

    public void fillName(String name) {
        inptName.fill(name);
    }

    public void fillEmail(String email) {
        inptEmail.fill(email);
    }

    public void fillReviewText(String text) {
        inptReviewText.fill(text);
    }

    public void fillReviewForm(String name, String email, String reviewText) {
        fillName(name);
        fillEmail(email);
        fillReviewText(reviewText);
    }

    public void clickSubmitReview() {
        btnSubmit.click();
    }


    // Built-in components
    public ProductFilterBar productFilterBar() {
        return cmpProductFilterBar;
    }

    public CartModal cartModal() {
        return cmpCartModal;
    }

}
