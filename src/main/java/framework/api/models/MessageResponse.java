package framework.api.models;

public record MessageResponse(
        Integer responseCode,
        String message
) {
}