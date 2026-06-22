package com.alkemy.wallet.model;

import java.util.Date;

/**
 * Represents a transaction record for a specific account.
 */
public class Transaction {
    private String transactionId;
    private Account account;
    private double amount;
    private String type; // e.g. "DEPOSIT" or "WITHDRAWAL"
    private Date date;

    public Transaction(String transactionId, Account account, double amount, String type, Date date) {
        this.transactionId = transactionId;
        this.account = account;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: %.2f %s on %s (ID: %s)",
                date, type, amount, account.getCurrency(), date, transactionId);
    }
}
