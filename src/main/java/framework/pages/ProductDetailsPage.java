package framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import framework.components.CartModal;
import framework.components.ProductFilterBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(ProductDetailsPage.class);

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
    public List<Locator> getRequiredElements() {
         List<Locator> elements = new ArrayList<>(List.of(
                imgProductPhoto,
                lblProductName,
                lblProductCategory,
                lblProductPrice,
                inptQuantity,
                btnAddToCart,
                lblAvailability,
                lblCondition,
                lblBrand,
                inptName,
                inptEmail,
                inptReviewText,
                btnSubmit
         ));

         elements.addAll(cmpProductFilterBar.getRequiredElements());

         return elements;
    }

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
        log.info("Opening product details for product ID \"{}\"", productId);
        page.navigate(getTargetUrl() + productId);
        log.debug("Navigation completed for product ID \"{}\"", productId);
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

    public void fillQuantity(int value) {
        log.info("Filling quantity \"{}\"", value);
        inptQuantity.fill(String.valueOf(value));
    }

    public void clickAddToCart() {
        log.info("Clicking \"Add to cart\" button");
        btnAddToCart.click();
    }


    // "Write your review" form
    public Locator getReviewSuccessMessage() {
        return lblSuccessMessage;
    }

    public void fillName(String value) {
        log.info("Filling review name");
        inptName.fill(value);
    }

    public void fillEmail(String value) {
        log.info("Filling review email");
        inptEmail.fill(value);
    }

    public void fillReviewText(String value) {
        log.info("Filling review text");
        inptReviewText.fill(value);
    }

    public void fillReviewForm(String name, String email, String reviewText) {
        fillName(name);
        fillEmail(email);
        fillReviewText(reviewText);
    }

    public void clickSubmitReview() {
        log.info("Clicking review \"Submit\" button");
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
