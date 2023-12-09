package test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import currencyConverter.Currency;

public class CurrencyTest {

    ArrayList<Currency> currencies;

    Currency testCadCurrency;
    List<String> testCurrencies;
    List<String> acceptedCurrencies;
    List<String> unacceptedCurrencies;

    @BeforeEach
    void setUp() {
        // testCurrencies = List.of("USD", "EUR", "GBP", "CHF", "CNY", "JPY");
        testCurrencies = List.of("USD", "EUR", "GBP", "CHF", "CNY", "JPY", "CAD");
        acceptedCurrencies = List.of("USD", "CAD", "GBP", "EUR", "CHF", "AUD");
        unacceptedCurrencies = List.of("JPY", "CNY");
        testCadCurrency = new Currency("CA Dollar", "CAD");

        testCadCurrency.setExchangeValues("USD", 1.00);
        testCadCurrency.setExchangeValues("EUR", 0.93);
        testCadCurrency.setExchangeValues("GBP", 0.66);
        testCadCurrency.setExchangeValues("CHF", 1.01);
        testCadCurrency.setExchangeValues("CNY", 6.36);
        testCadCurrency.setExchangeValues("JPY", 123.54);

        currencies = Currency.init();
        currencies.add(testCadCurrency);
    }

    // A black box domain test and limit values.
    @Test
    public void testConvert() {
        double[] amounts = {-1000000, -1, 0, 1, 500000, 1000000, 1000001, 2000000};

        // Verify the conversion is accurate and precise.
        assertAll(
            () -> Arrays.stream(amounts)
                        .forEach(a -> assertEquals(Currency.convert(a, 2.0d), a * 2.0d))
        );
    }

        @Test
    void testConvertNormalCase() { // Cas normal
        double result = Currency.convert(100.0, 1.5);
        assertEquals(150.0, result, 0.01);
    }

    @Test
    void testConvertZeroExchangeValue() { // Cas avec un taux d'échange nul
        double result = Currency.convert(100.0, 0.0);
        assertEquals(0.0, result, 0.01);
    }

    @Test
    void testConvertZeroAmount() { // Cas avec un montant nul
        double result = Currency.convert(0.0, 1.5);
        assertEquals(0.0, result, 0.01);
    }

    @Test
    void testConvertBothZeroAmounts() { // Cas avec les deux montants nuls
        double result = Currency.convert(0.0, 0.0);
        assertEquals(0.0, result, 0.01);
    }

    @Test
    void testConvertNegativeAmount() { // Cas avec un montant négatif
        double result = Currency.convert(-100.0, 1.5);
        assertEquals(-150.0, result, 0.01);
    }

    @Test
    void testConvertNegativeExchangeValue() { // Cas avec un taux d'échange négatif
        double result = Currency.convert(100.0, -1.5);
        assertEquals(-150.0, result, 0.01);
    }
}