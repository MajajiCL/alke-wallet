package com.alkemy.wallet.service.interfaces;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.exception.InsufficientFundsException;

/**
 * Service interface defining wallet operations.
 */
public interface WalletService {
    
    /**
     * Creates a new wallet account for a user with a default currency (USD).
     * @param user the owner of the account.
     * @return the newly created Account.
     */
    Account createAccount(User user);

    /**
     * Deposits an amount of money into the account.
     * @param account the target account.
     * @param amount the amount to deposit, must be positive.
     */
    void deposit(Account account, double amount);

    /**
     * Withdraws an amount of money from the account.
     * @param account the target account.
     * @param amount the amount to withdraw, must be positive.
     * @throws InsufficientFundsException if the account has insufficient balance.
     */
    void withdraw(Account account, double amount) throws InsufficientFundsException;

    /**
     * Retrieves the current balance of the account.
     * @param account the target account.
     * @return the current balance.
     */
    double getBalance(Account account);
}
