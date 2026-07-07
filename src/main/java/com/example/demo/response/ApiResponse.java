package com.example.demo.response;

import java.time.LocalDateTime;

// SUCCESS RESPONSE
//{
//        "success": true,
//        "message": "Category created",
//        "data": {...},
//        "errors": null,
//        "timestamp": "..."
//}

// ERROR RESPONSE
//{
//        "success": false,
//        "message": "Validation failed",
//        "data": null,
//        "errors": {
//        "name": "required"
//        },
//        "timestamp": "..."
//}

public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final Object errors;
    private final LocalDateTime timestamp;

    private ApiResponse(boolean success, String message, T data, Object errors) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    // ✅ SUCCESS RESPONSE (with data)
    public static <T> ApiResponse<T> success(String message, T data){
        return new ApiResponse<>(true, message, data, null);
    }

    // ✅ SUCCESS RESPONSE (without data)
    public static <T> ApiResponse<T> success(String message){
        return new ApiResponse<>(true, message, null, null);
    }

    // ❌ ERROR RESPONSE (simple)
    public static <T> ApiResponse<T> error(String message){
        return new ApiResponse<>(false, message, null, null);
    }

    // ❌ ERROR RESPONSE (with details)
    public static <T> ApiResponse<T> error(String message, Object errors){
        return new ApiResponse<>(false, message, null, errors);
    }

    // 🔹 GETTERS
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public Object getErrors() {
        return errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}