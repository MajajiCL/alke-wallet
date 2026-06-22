package com.alkemy.wallet.service.interfaces;

import com.alkemy.wallet.model.Currency;

/**
 * Service interface defining currency conversion behavior.
 */
public interface CurrencyConverter {

    /**
     * Converts a given amount from one currency to another.
     * @param amount the quantity to convert, must be non-negative.
     * @param from the source currency.
     * @param to the target currency.
     * @return the converted amount.
     */
    double convert(double amount, Currency from, Currency to);
}
