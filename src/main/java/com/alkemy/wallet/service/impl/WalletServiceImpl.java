package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Currency;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.model.Transaction;
import com.alkemy.wallet.service.interfaces.WalletService;
import com.alkemy.wallet.exception.InsufficientFundsException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Concrete implementation of the WalletService interface.
 * Implements validation rules, account creation, and transaction history tracking.
 */
public class WalletServiceImpl implements WalletService {
    
    private final AtomicInteger accountIdCounter = new AtomicInteger(1000);
    private final List<Transaction> transactionHistory = new ArrayList<>();

    @Override
    public Account createAccount(User user) {
        if (user == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        // Default currency is USD, initial balance is 0.0
        int newId = accountIdCounter.incrementAndGet();
        return new Account(newId, user, 0.0, Currency.USD);
    }

    /**
     * Overloaded method to allow account creation with a specific currency.
     */
    public Account createAccount(User user, Currency currency) {
        if (user == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        if (currency == null) {
            throw new IllegalArgumentException("La moneda no puede ser nula.");
        }
        int newId = accountIdCounter.incrementAndGet();
        return new Account(newId, user, 0.0, currency);
    }

    @Override
    public void deposit(Account account, double amount) {
        if (account == null) {
            throw new IllegalArgumentException("La cuenta no puede ser nula.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto a depositar debe ser mayor que cero.");
        }
        
        // Deposit into account
        account.deposit(amount);

        // Record transaction
        String txId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Transaction tx = new Transaction(txId, account, amount, "DEPOSIT", new Date());
        transactionHistory.add(tx);
    }

    @Override
    public void withdraw(Account account, double amount) throws InsufficientFundsException {
        if (account == null) {
            throw new IllegalArgumentException("La cuenta no puede ser nula.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto a retirar debe ser mayor que cero.");
        }
        
        // Withdraw from account (throws InsufficientFundsException if balance is insufficient)
        account.withdraw(amount);

        // Record transaction
        String txId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Transaction tx = new Transaction(txId, account, amount, "WITHDRAWAL", new Date());
        transactionHistory.add(tx);
    }

    @Override
    public double getBalance(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("La cuenta no puede ser nula.");
        }
        return account.getBalance();
    }

    /**
     * Returns the global transaction history for all operations.
     */
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    /**
     * Returns the transaction history for a specific account.
     */
    public List<Transaction> getTransactionHistoryForAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("La cuenta no puede ser nula.");
        }
        List<Transaction> accountTxs = new ArrayList<>();
        for (Transaction tx : transactionHistory) {
            if (tx.getAccount().getAccountId() == account.getAccountId()) {
                accountTxs.add(tx);
            }
        }
        return accountTxs;
    }
}
