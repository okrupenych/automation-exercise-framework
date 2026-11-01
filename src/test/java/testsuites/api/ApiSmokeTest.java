package testsuites.api;

import base.BaseApiTest;
import data.AccountData;
import data.Credentials;
import framework.api.ApiResult;
import framework.api.models.AccountResponse;
import framework.api.models.BrandsResponse;
import framework.api.models.MessageResponse;
import framework.api.models.ProductsResponse;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Tag("api")
public class ApiSmokeTest extends BaseApiTest {

    @Test
    @Tag("smoke")
    void searchProducts() {
        ApiResult result = api.searchProducts("shirt");

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

    @Test
    @Tag("negative")
    void verifyLoginWithoutEmail() {
        ApiResult result = api.verifyLogin(
                new Credentials(null, "test-password")
        );

        assertEquals(200, result.httpStatus());

        MessageResponse response =
                result.as(MessageResponse.class);

        assertNotNull(response);
        assertEquals(Integer.valueOf(400), response.responseCode());
        assertEquals(
                "Bad request, email or password parameter is missing in POST request.",
                response.message()
        );
    }

    @Test
    void accountLifecycle() {
        AccountData account = new AccountData(
                "Mr",
                "Fake", "User",
                "api-" + UUID.randomUUID() + "@test.com",
                "test-password",
                "1", "January", "2000",
                "Fake inc.",
                "Fake address 1", null,
                "United States",
                "Fake State", "Fake City",
                "30",
                "+3030",
                null
        );

        Throwable scenarioFailure = null;
        try {
            ApiResult result = api.createAccount(account);

            assertEquals(200, result.httpStatus());

            MessageResponse created = result.as(MessageResponse.class);

            assertNotNull(created);
            assertEquals(Integer.valueOf(201), created.responseCode());
            assertEquals("User created!", created.message());

            ApiResult details = api.getAccountByEmail(account.email());

            assertEquals(200, details.httpStatus());

            AccountResponse response = details.as(AccountResponse.class);

            assertNotNull(response);
            assertEquals(Integer.valueOf(200), response.responseCode());
            assertNotNull(response.user());

            AccountResponse.User user = response.user();

            assertNotNull(user.id());
            assertEquals(account.email(), user.email());
            assertEquals(account.fullName(), user.name());
            assertEquals(account.title(), user.title());
            assertEquals(account.firstName(), user.firstName());
            assertEquals(account.lastName(), user.lastName());
            assertEquals(account.birthDay(), user.birthDay());
            assertEquals(account.birthMonth(), user.birthMonth());
            assertEquals(account.birthYear(), user.birthYear());
            assertEquals(account.company(), user.company());
            assertEquals(account.address1(), user.address1());
            assertEquals("", user.address2());
            assertEquals(account.country(), user.country());
            assertEquals(account.state(), user.state());
            assertEquals(account.city(), user.city());
            assertEquals(account.zipcode(), user.zipcode());

            ApiResult login = api.verifyLogin(account.credentials());

            assertEquals(200, login.httpStatus());

            MessageResponse verified = login.as(MessageResponse.class);

            assertNotNull(verified);
            assertEquals(Integer.valueOf(200), verified.responseCode());
            assertEquals("User exists!", verified.message());

            AccountData updatedAccount = new AccountData(
                    account.title(),
                    account.firstName(), account.lastName(),
                    account.email(), account.password(),
                    account.birthDay(), account.birthMonth(), account.birthYear(),
                    "Updated company",
                    account.address1(), account.address2(),
                    account.country(),
                    account.state(), "Updated city",
                    account.zipcode(),
                    account.mobileNumber(),
                    account.payment()
            );

            ApiResult update = api.updateAccount(updatedAccount);

            assertEquals(200, update.httpStatus());

            MessageResponse updated = update.as(MessageResponse.class);

            assertNotNull(updated);
            assertEquals(Integer.valueOf(200), updated.responseCode());
            assertEquals("User updated!", updated.message());

            ApiResult updatedDetails = api.getAccountByEmail(account.email());

            assertEquals(200, updatedDetails.httpStatus());

            AccountResponse afterUpdate =
                    updatedDetails.as(AccountResponse.class);

            assertNotNull(afterUpdate);
            assertEquals(Integer.valueOf(200), afterUpdate.responseCode());
            assertNotNull(afterUpdate.user());
            assertEquals(updatedAccount.company(), afterUpdate.user().company());
            assertEquals(updatedAccount.city(), afterUpdate.user().city());
            assertEquals(user.id(), afterUpdate.user().id());
        } catch (RuntimeException | Error failure) {
            scenarioFailure = failure;
            throw failure;
        } finally {
            try {
                ApiResult cleanup = api.deleteAccount(
                        account.credentials()
                );

                assertEquals(200, cleanup.httpStatus());

                MessageResponse deleted = cleanup.as(MessageResponse.class);

                assertNotNull(deleted);
                assertEquals(Integer.valueOf(200), deleted.responseCode());
                assertEquals("Account deleted!", deleted.message());

                ApiResult loginAfterDelete =
                        api.verifyLogin(account.credentials());

                assertEquals(200, loginAfterDelete.httpStatus());

                MessageResponse missing =
                        loginAfterDelete.as(MessageResponse.class);

                assertNotNull(missing);
                assertEquals(Integer.valueOf(404), missing.responseCode());
                assertEquals("User not found!", missing.message());
            } catch (RuntimeException | Error cleanupFailure) {
                if (scenarioFailure != null) {
                    scenarioFailure.addSuppressed(cleanupFailure);
                } else {
                    throw cleanupFailure;
                }
            }
        }
    }

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

}
