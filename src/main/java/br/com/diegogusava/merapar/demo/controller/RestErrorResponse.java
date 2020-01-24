package br.com.diegogusava.merapar.demo.controller;

import java.time.LocalDateTime;

public class RestErrorResponse {

    private String message;
    private LocalDateTime date = LocalDateTime.now();

    public RestErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
