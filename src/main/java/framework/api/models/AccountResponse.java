package framework.api.models;

import com.google.gson.annotations.SerializedName;

public record AccountResponse(
        Integer responseCode,
        User user
) {
    public record User(
            Integer id,
            String name,
            String email,
            String title,
            @SerializedName("birth_day") String birthDay,
            @SerializedName("birth_month") String birthMonth,
            @SerializedName("birth_year") String birthYear,
            @SerializedName("first_name") String firstName,
            @SerializedName("last_name") String lastName,
            String company,
            String address1,
            String address2,
            String country,
            String state,
            String city,
            String zipcode
    ) {
    }
}