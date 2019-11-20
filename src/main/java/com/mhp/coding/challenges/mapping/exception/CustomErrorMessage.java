package com.mhp.coding.challenges.mapping.exception;

/**
 * @author Manpreet Kaur
 */
public class CustomErrorMessage {

    private String message;

    public CustomErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
