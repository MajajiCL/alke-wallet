package com.alkemy.wallet.service;

import com.alkemy.wallet.model.Account;
import com.alkemy.wallet.model.Currency;
import com.alkemy.wallet.model.User;
import com.alkemy.wallet.service.impl.WalletServiceImpl;
import com.alkemy.wallet.service.interfaces.WalletService;
import com.alkemy.wallet.exception.InsufficientFundsException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for WalletService and WalletServiceImpl.
 */
public class WalletServiceTest {

    private WalletService walletService;
    private User testUser;
    private Account testAccount;

    @BeforeEach
    public void setUp() {
        walletService = new WalletServiceImpl();
        testUser = new User(1, "Test User", "test@example.com", "password");
        // createAccount defaults to USD
        testAccount = walletService.createAccount(testUser);
    }

    @Test
    public void testCreateAccount() {
        assertNotNull(testAccount);
        assertEquals(testUser, testAccount.getUser());
        assertEquals(0.0, testAccount.getBalance(), 0.001);
        assertEquals(Currency.USD, testAccount.getCurrency());
        assertTrue(testAccount.getAccountId() > 0);
    }

    @Test
    public void testCreateAccountCustomCurrency() {
        WalletServiceImpl serviceImpl = (WalletServiceImpl) walletService;
        Account clpAccount = serviceImpl.createAccount(testUser, Currency.CLP);
        assertNotNull(clpAccount);
        assertEquals(Currency.CLP, clpAccount.getCurrency());
        assertEquals(0.0, clpAccount.getBalance(), 0.001);
    }

    @Test
    public void testDepositSuccess() {
        walletService.deposit(testAccount, 150.0);
        assertEquals(150.0, walletService.getBalance(testAccount), 0.001);
    }

    @Test
    public void testDepositNegativeAmountThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            walletService.deposit(testAccount, -50.0);
        });
    }

    @Test
    public void testDepositZeroAmountThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            walletService.deposit(testAccount, 0.0);
        });
    }

    @Test
    public void testWithdrawSuccess() throws InsufficientFundsException {
        walletService.deposit(testAccount, 200.0);
        walletService.withdraw(testAccount, 80.0);
        assertEquals(120.0, walletService.getBalance(testAccount), 0.001);
    }

    @Test
    public void testWithdrawInsufficientFundsThrowsException() {
        walletService.deposit(testAccount, 50.0);
        assertThrows(InsufficientFundsException.class, () -> {
            walletService.withdraw(testAccount, 100.0);
        });
    }

    @Test
    public void testWithdrawNegativeAmountThrowsException() {
        walletService.deposit(testAccount, 100.0);
        assertThrows(IllegalArgumentException.class, () -> {
            walletService.withdraw(testAccount, -10.0);
        });
    }

    @Test
    public void testWithdrawZeroAmountThrowsException() {
        walletService.deposit(testAccount, 100.0);
        assertThrows(IllegalArgumentException.class, () -> {
            walletService.withdraw(testAccount, 0.0);
        });
    }
}
