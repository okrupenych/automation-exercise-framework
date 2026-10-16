package data;

public record AccountData(
        String firstName,
        String lastName,
        String email,
        String password,
        String birthDay,
        String birthMonth,
        String birthYear,
        String company,
        String address1,
        String address2,
        String country,
        String state,
        String city,
        String zipcode,
        String mobileNumber,
        PaymentDetails payment
) {
    public String fullName() {
        if (firstName == null && lastName == null) {
            return null;
        }

        return String.join(" ",
                firstName == null ? "" : firstName,
                lastName == null ? "" : lastName
        ).trim();
    }

    public record PaymentDetails(
            String nameOnCard,
            String cardNumber,
            String cvc,
            String expirationMonth,
            String expirationYear
    ) {
    }
}
