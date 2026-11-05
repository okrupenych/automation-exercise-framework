package testsuites.ui.reviews;

import assertions.PageAssertions;
import base.BaseTest;
import framework.pages.ProductDetailsPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("ui")
@Tag("reviews")
public class ProductReviewTest extends BaseTest {

    private static final int PRODUCT_ID = 1;

    private ProductDetailsPage productDetailsPage;

    @BeforeEach
    void setUpProductPage() {
        productDetailsPage = new ProductDetailsPage(
                browserSession.getPage()
        );

        productDetailsPage.open(PRODUCT_ID);

        PageAssertions.assertPageReady(
                browserSession.getPage(),
                productDetailsPage,
                productDetailsPage.getTargetUrl() + PRODUCT_ID
        );
    }


    @Test
    @DisplayName("Review submission shows a success confirmation")
    void shouldConfirmReviewSubmissionWithValidData() {
        PageAssertions.assertElementVisibleWithText(
                productDetailsPage.getReviewHeader(),
                "Write Your Review"
        );

        productDetailsPage.fillReviewForm(
                "Test Reviewer",
                "reviewer@example.com",
                "Comfortable fit and good quality."
        );

        productDetailsPage.clickSubmitReview();

        PageAssertions.assertElementVisibleWithText(
                productDetailsPage.getReviewSuccessMessage(),
                "Thank you for your review."
        );
    }

    @Test
    @DisplayName("Review submission requires a name")
    @Tag("negative")
    void shouldRejectReviewWithoutName() {
        productDetailsPage.fillReviewForm(
                "",
                "reviewer@example.com",
                "Comfortable fit and good quality."
        );
        productDetailsPage.clickSubmitReview();

        PageAssertions.assertFieldValidationError(
                productDetailsPage.getReviewName(),
                PageAssertions.ValidationError.VALUE_MISSING
        );
    }

    @Test
    @DisplayName("Review submission requires an email")
    @Tag("negative")
    void shouldRejectReviewWithoutEmail() {
        productDetailsPage.fillReviewForm(
                "Test Reviewer",
                "",
                "Comfortable fit and good quality."
        );
        productDetailsPage.clickSubmitReview();

        PageAssertions.assertFieldValidationError(
                productDetailsPage.getReviewEmail(),
                PageAssertions.ValidationError.VALUE_MISSING
        );
    }

    @Test
    @DisplayName("Review submission rejects an invalid email format")
    @Tag("negative")
    void shouldRejectReviewWithInvalidEmail() {
        productDetailsPage.fillReviewForm(
                "Test Reviewer",
                "invalid-email",
                "Comfortable fit and good quality."
        );
        productDetailsPage.clickSubmitReview();

        PageAssertions.assertFieldValidationError(
                productDetailsPage.getReviewEmail(),
                PageAssertions.ValidationError.TYPE_MISMATCH
        );
    }

    @Test
    @DisplayName("Review submission requires review text")
    @Tag("negative")
    void shouldRejectReviewWithoutText() {
        productDetailsPage.fillReviewForm(
                "Test Reviewer",
                "reviewer@example.com",
                ""
        );
        productDetailsPage.clickSubmitReview();

        PageAssertions.assertFieldValidationError(
                productDetailsPage.getReviewText(),
                PageAssertions.ValidationError.VALUE_MISSING
        );
    }

}