package com.software.teamfive.sitepackageapi.util;

public class ApiResult<T> {

    private String message;
    private T payload;

    public ApiResult(String message, T payload) {
        this.message = message;
        this.payload = payload;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
