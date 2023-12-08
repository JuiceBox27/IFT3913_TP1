package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import currencyConverter.Currency;
import currencyConverter.MainWindow;

public class MainWindowTest {

    ArrayList<Currency> actualCurrencies;

    Currency testCadCurrency;

    @BeforeEach
    void setUp() {
        testCadCurrency = new Currency("CA Dollar", "CAD");

        testCadCurrency.setExchangeValues("USD", 1.00);
        testCadCurrency.setExchangeValues("EUR", 0.93);
        testCadCurrency.setExchangeValues("GBP", 0.66);
        testCadCurrency.setExchangeValues("CHF", 1.01);
        testCadCurrency.setExchangeValues("CNY", 6.36);
        testCadCurrency.setExchangeValues("JPY", 123.54);

        actualCurrencies = Currency.init();
        actualCurrencies.add(testCadCurrency);
    }

    @Test
    void testConvert() {
        assertNotNull(actualCurrencies);
        assertEquals(7, actualCurrencies.size());


    }

    @Test
    void testConvertBlackBox() {

    }

    @Test
    void testConvertWhiteBox() {
        MainWindow.convert("USD", "EUR", actualCurrencies, 100.0d);
    }
}