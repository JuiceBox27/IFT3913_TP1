package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import currencyConverter.Currency;
import currencyConverter.MainWindow;

public class MainWindowTest {

    ArrayList<Currency> appCurrencies;
    ArrayList<Currency> allCurrencies;

    Currency testCadCurrency;
    Currency testAudCurrency;

    double testAmount = 100.07d;

    @BeforeEach
    void setUp() {
        setUpCurrencies();
    }

    
    @Test
    void testConvertBlackBox() {
        assertEquals((testAmount * 0.93d), MainWindow.convert("US Dollar", "Euro", appCurrencies, testAmount), 0.01d);
        assertEquals(0, MainWindow.convert("US Dollar", "AU Dollar", appCurrencies, testAmount), 0.01d);
        assertEquals(0, MainWindow.convert("CA Dollar", "Euro", appCurrencies, testAmount), 0.01d);
        assertEquals(0, MainWindow.convert("CA Dollar", "Euro", appCurrencies, testAmount), 0.01d);

        assertEquals((testAmount * 0.93d), MainWindow.convert("US Dollar", "Euro", allCurrencies, testAmount), 0.01d);
        assertEquals((testAmount * 88.00d), MainWindow.convert("US Dollar", "AU Dollar", allCurrencies, testAmount), 0.01d);
        assertEquals((testAmount * 22.00d), MainWindow.convert("CA Dollar", "Euro", allCurrencies, testAmount), 0.01d);
        assertEquals((testAmount * 33.00d), MainWindow.convert("CA Dollar", "AU Dollar", allCurrencies, testAmount), 0.01d);

        
    }

    @Test
    void testConvertWhiteBox() {
        MainWindow.convert("USD", "EUR", appCurrencies, 100.0d);
    }

    private void setUpCurrencies() {
        testCadCurrency = new Currency("CA Dollar", "CAD");
        testAudCurrency = new Currency("AU Dollar", "AUD");

        testCadCurrency.setExchangeValues("USD", 11.00);
        testCadCurrency.setExchangeValues("EUR", 22.00);
        testCadCurrency.setExchangeValues("GBP", 44.00);
        testCadCurrency.setExchangeValues("CHF", 44.00);
        testCadCurrency.setExchangeValues("CNY", 44.00);
        testCadCurrency.setExchangeValues("JPY", 44.00);
        testCadCurrency.setExchangeValues("AUD", 33.00);

        testAudCurrency.setExchangeValues("USD", 11.00);
        testAudCurrency.setExchangeValues("EUR", 22.00);
        testAudCurrency.setExchangeValues("GBP", 44.00);
        testAudCurrency.setExchangeValues("CHF", 44.00);
        testAudCurrency.setExchangeValues("CNY", 44.00);
        testAudCurrency.setExchangeValues("JPY", 44.00);
        testAudCurrency.setExchangeValues("CAD", 33.00);

        allCurrencies = Currency.init();
        appCurrencies = Currency.init();

        allCurrencies.forEach(
            c -> {
                c.setExchangeValues("CAD", 99.00);
                c.setExchangeValues("AUD", 88.00);
            });

        allCurrencies.add(testCadCurrency);
        allCurrencies.add(testAudCurrency);
    }
}