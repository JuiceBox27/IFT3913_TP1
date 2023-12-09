package test;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;


import currencyConverter.Currency;


public class CurrencyTest {
    ArrayList<Currency> currencies;

    Currency testCadCurrency;
    List<String> testCurrencies;
    List<String> acceptedCurrencies;
    List<String> unacceptedCurrencies;
    // A black box domain test and limit values test.
    @Test
    public void testConvertBlackBox() {
        testCurrencies = List.of("USD", "EUR", "GBP", "CHF", "CNY", "JPY", "CAD");
        acceptedCurrencies = List.of("USD", "CAD", "GBP", "EUR", "CHF", "AUD");
        unacceptedCurrencies = List.of("JPY", "CNY");
        double[] amounts = {-1000000, -1, 0, 1, 500000, 1000000, 1000001, 2000000};

        assertAll("Currency.convert domain and limit values tests.",
            () -> Arrays.stream(amounts)
                        .forEach(a -> assertEquals(a * 2.0d, Currency.convert(a, 2.0d), 0.01d))
        );
    }

    // A white box test of the only path on the flux diagram.
    @Test
    public void testConvertWhiteBox() {
        assertEquals(Currency.convert(100.00d, 2.0d), 100.00d * 2.0d, 0.01d);
    }
  
    @Test
    public void testAcceptedCurrencies() {
        assertAll("Testing accepted currencies",
            () -> currencies.forEach(currency ->
                acceptedCurrencies.forEach(ac ->
                    assertNotNull(currency.getExchangeValues().get(ac), 
                                  "Currency not supported: " + ac)
                )
            )
        );
    }

    @Test
    public void testUnacceptedCurrencies() {
        assertAll("Testing unaccepted currencies",
            () -> currencies.forEach(currency ->
                unacceptedCurrencies.forEach(uc ->
                    assertNull("Currency should not be supported: " + uc,
                               currency.getExchangeValues().get(uc))
                )
            )
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