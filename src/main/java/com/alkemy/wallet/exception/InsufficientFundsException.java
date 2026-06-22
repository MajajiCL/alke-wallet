package com.alkemy.wallet.exception;

/**
 * Exception thrown when a withdrawal is requested with insufficient balance.
 */
public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
