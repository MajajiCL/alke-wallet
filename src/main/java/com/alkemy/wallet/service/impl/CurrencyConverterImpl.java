package com.alkemy.wallet.service.impl;

import com.alkemy.wallet.model.Currency;
import com.alkemy.wallet.service.interfaces.CurrencyConverter;

/**
 * Concrete implementation of the CurrencyConverter interface.
 * Uses rate factors relative to USD to convert between different currencies.
 */
public class CurrencyConverterImpl implements CurrencyConverter {

    @Override
    public double convert(double amount, Currency from, Currency to) {
        if (amount < 0) {
            throw new IllegalArgumentException("El monto a convertir no puede ser negativo.");
        }
        if (from == null || to == null) {
            throw new IllegalArgumentException("Las monedas de origen y destino no pueden ser nulas.");
        }
        
        if (from == to) {
            return amount;
        }

        // Convert the amount to the base currency (USD)
        double amountInUSD = amount * from.getRateToUSD();

        // Convert from the base currency (USD) to the target currency
        return amountInUSD / to.getRateToUSD();
    }
}
