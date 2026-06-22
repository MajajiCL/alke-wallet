package com.alkemy.wallet.model;

/**
 * Represents the currencies supported by the Alke Wallet.
 * Each currency has an exchange rate relative to USD (base currency)
 * and a symbol for display purposes.
 */
public enum Currency {
    USD(1.0, "US$"),
    EUR(1.08, "€"),
    CLP(0.0011, "CLP$");

    private final double rateToUSD; // Conversion factor: 1 Unit = X USD
    private final String symbol;

    Currency(double rateToUSD, String symbol) {
        this.rateToUSD = rateToUSD;
        this.symbol = symbol;
    }

    public double getRateToUSD() {
        return rateToUSD;
    }

    public String getSymbol() {
        return symbol;
    }
}
