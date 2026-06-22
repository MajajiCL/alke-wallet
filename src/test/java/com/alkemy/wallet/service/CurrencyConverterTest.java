package com.alkemy.wallet.service;

import com.alkemy.wallet.model.Currency;
import com.alkemy.wallet.service.impl.CurrencyConverterImpl;
import com.alkemy.wallet.service.interfaces.CurrencyConverter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CurrencyConverter and CurrencyConverterImpl.
 */
public class CurrencyConverterTest {

    private CurrencyConverter currencyConverter;

    @BeforeEach
    public void setUp() {
        currencyConverter = new CurrencyConverterImpl();
    }

    @Test
    public void testConvertCLPtoUSD() {
        // CLP rate: 0.0011, USD rate: 1.0
        // 1000 CLP = 1000 * 0.0011 / 1.0 = 1.1 USD
        double result = currencyConverter.convert(1000.0, Currency.CLP, Currency.USD);
        assertEquals(1.1, result, 0.0001);
    }

    @Test
    public void testConvertUSDtoEUR() {
        // USD rate: 1.0, EUR rate: 1.08
        // 100 USD = 100 * 1.0 / 1.08 = 92.592592... EUR
        double result = currencyConverter.convert(100.0, Currency.USD, Currency.EUR);
        assertEquals(100.0 / 1.08, result, 0.0001);
    }

    @Test
    public void testConvertEURtoCLP() {
        // EUR rate: 1.08, CLP rate: 0.0011
        // 100 EUR = 100 * 1.08 / 0.0011 = 98181.818... CLP
        double result = currencyConverter.convert(100.0, Currency.EUR, Currency.CLP);
        assertEquals(108.0 / 0.0011, result, 0.0001);
    }

    @Test
    public void testConvertSameCurrency() {
        double result = currencyConverter.convert(250.0, Currency.USD, Currency.USD);
        assertEquals(250.0, result, 0.0001);
    }

    @Test
    public void testConvertNegativeAmountThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            currencyConverter.convert(-100.0, Currency.USD, Currency.EUR);
        });
    }

    @Test
    public void testConvertNullCurrencyThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            currencyConverter.convert(100.0, null, Currency.EUR);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            currencyConverter.convert(100.0, Currency.USD, null);
        });
    }
}
