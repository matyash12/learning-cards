package com.example.backend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponse {
    private String message;
    private HttpStatus statusCode; // HTTP status code
    private Object data;
    // Constructors, getters, and setters

    public ApiResponse(Object data, String message, HttpStatus statusCode) {
        this.data = data;
        this.statusCode = statusCode;
        this.message = message;
    }

    public ResponseEntity<ApiResponse> toResponseEntity() {
        return new ResponseEntity<>(this, statusCode);
    }

    public int getCode() {
        return statusCode.value();
    }

    public String getStatus() {
        if (statusCode.value() >= 200 && statusCode.value() < 300) {
            return "success";
        } else if (statusCode.value() >= 400 && statusCode.value() < 500) {
            return "error";
        } else {
            return "unknown"; // You can customize this based on your needs
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // public HttpStatus getStatusCode() {
    // return statusCode;
    // }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
