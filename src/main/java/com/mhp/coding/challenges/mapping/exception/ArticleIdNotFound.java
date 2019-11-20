package com.mhp.coding.challenges.mapping.exception;

/**
 * @author Manpreet Kaur
 */
public class ArticleIdNotFound extends RuntimeException {

    public ArticleIdNotFound(String message) {
        super(message);
    }
}
