package com.alkemy.wallet.model;

import com.alkemy.wallet.exception.InsufficientFundsException;

/**
 * Represents a user's wallet account in Alke Wallet.
 * Handles depositing and withdrawing funds with validation.
 */
public class Account {
    private int accountId;
    private User user;
    private double balance;
    private Currency currency;

    public Account(int accountId, User user, double balance, Currency currency) {
        if (balance < 0) {
            throw new IllegalArgumentException("El saldo inicial no puede ser negativo.");
        }
        this.accountId = accountId;
        this.user = user;
        this.balance = balance;
        this.currency = currency;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     * Deposits money into the account.
     * @param amount the amount to deposit, must be positive.
     */
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto a depositar debe ser mayor que cero.");
        }
        this.balance += amount;
    }

    /**
     * Withdraws money from the account.
     * @param amount the amount to withdraw, must be positive.
     * @throws InsufficientFundsException if the amount exceeds the current balance.
     */
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser mayor que cero.");
        }
        if (amount > this.balance) {
            throw new InsufficientFundsException(
                String.format("Fondos insuficientes. Saldo actual: %.2f, Monto solicitado: %.2f", this.balance, amount)
            );
        }
        this.balance -= amount;
    }
}
