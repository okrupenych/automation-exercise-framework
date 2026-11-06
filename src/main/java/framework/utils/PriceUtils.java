package framework.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class PriceUtils {

    private static final String CURRENCY_PREFIX = "Rs.";
    private static final int DIVISION_SCALE = 2;

    private PriceUtils() {
    }


    private static BigDecimal parse(String price) {
        Objects.requireNonNull(price, "Price must not be null");

        String value = price.trim();
        if (!value.startsWith(CURRENCY_PREFIX)) {
            throw new IllegalArgumentException(
                    "Price must start with '" + CURRENCY_PREFIX + "': " + price
            );
        }

        String amount = value.substring(CURRENCY_PREFIX.length()).trim();
        if (amount.isEmpty()) {
            throw new IllegalArgumentException("Price amount must not be empty");
        }

        try {
            return new BigDecimal(amount);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid price amount: " + price, exception);
        }
    }

    private static String format(BigDecimal amount) {
        return CURRENCY_PREFIX + " " + amount.stripTrailingZeros().toPlainString();
    }


    public static String add(String... prices) {
        if (prices == null || prices.length == 0) {
            throw new IllegalArgumentException("At least one price is required");
        }

        BigDecimal result = BigDecimal.ZERO;
        for (String price : prices) {
            result = result.add(parse(price));
        }

        return format(result);
    }

    public static String subtract(String minuend, String subtrahend) {
        return format(parse(minuend).subtract(parse(subtrahend)));
    }

    public static String multiply(String price, int multiplier) {
        return format(parse(price).multiply(BigDecimal.valueOf(multiplier)));
    }

    public static String divide(String price, int divisor) {
        if (divisor == 0) {
            throw new IllegalArgumentException("Divisor must not be zero");
        }

        return format(parse(price).divide(
                BigDecimal.valueOf(divisor),
                DIVISION_SCALE,
                RoundingMode.HALF_UP
        ));
    }

}
