package test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import currencyConverter.Currency;

public class CurrencyTest {

    ArrayList<Currency> currencies;

    Currency testCadCurrency;

    List<String> testCurrencies;


    @BeforeEach
    void setUp() {
        // testCurrencies = List.of("USD", "EUR", "GBP", "CHF", "CNY", "JPY");
        testCurrencies = List.of("USD", "EUR", "GBP", "CHF", "CNY", "JPY", "CAD");

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
    
    // A black box test of the types of currencies accepted.
    // @Test
    // public void testCurrencies() {

    //     assertAll(
    //         () -> testCurrencies.stream().forEach(tc -> 
    //                 assertTrue(currencies.stream().map(c -> 
    //                     c.getShortName()).collect(Collectors.toList()).contains(tc)))
    //     );
    // }
}