package framework.api;

import com.google.gson.Gson;

public record ApiResult(
        int httpStatus,
        String body
) {
    private static final Gson GSON = new Gson();

    public <T> T as(Class<T> responseType) {
        return GSON.fromJson(body, responseType);
    }

}
