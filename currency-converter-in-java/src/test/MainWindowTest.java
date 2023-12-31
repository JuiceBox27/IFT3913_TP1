package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;

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
        double testAmount = 500000;

        // Domain tests for the default currencies of the app and the currencies that do not exist in this default configuration.
        assertEquals((testAmount * 0.93d), MainWindow.convert("US Dollar", "Euro", appCurrencies, testAmount), 0.01d);
        assertEquals(0, MainWindow.convert("US Dollar", "AU Dollar", appCurrencies, testAmount), 0.01d);
        assertEquals(0, MainWindow.convert("CA Dollar", "Euro", appCurrencies, testAmount), 0.01d);
        assertEquals(0, MainWindow.convert("CA Dollar", "Euro", appCurrencies, testAmount), 0.01d);

        // Domain tests for all the currencies.
        assertEquals((testAmount * 0.93d), MainWindow.convert("US Dollar", "Euro", allCurrencies, testAmount), 0.01d);
        assertEquals((testAmount * 88.00d), MainWindow.convert("US Dollar", "AU Dollar", allCurrencies, testAmount), 0.01d);
        assertEquals((testAmount * 22.00d), MainWindow.convert("CA Dollar", "Euro", allCurrencies, testAmount), 0.01d);
        assertEquals((testAmount * 33.00d), MainWindow.convert("CA Dollar", "AU Dollar", allCurrencies, testAmount), 0.01d);

        // Domain and limit values tests of the amount.
        assertEquals((-1000000.00 * 0.93), MainWindow.convert("US Dollar", "Euro", appCurrencies, -1000000.00), 0.01d);
        assertEquals((-1.00 * 0.93), MainWindow.convert("US Dollar", "Euro", appCurrencies, -1.00), 0.01d);
        assertEquals(0, MainWindow.convert("US Dollar", "Euro", appCurrencies, 0.00), 0.01d);
        assertEquals((500000.00 * 0.93), MainWindow.convert("US Dollar", "Euro", appCurrencies, 500000.00), 0.01d);
        assertEquals((1000000.00 * 0.93), MainWindow.convert("US Dollar", "Euro", appCurrencies, 1000000.00), 0.01d);
        assertEquals((1000001.00 * 0.93), MainWindow.convert("US Dollar", "Euro", appCurrencies, 1000001.00), 0.01d);
        assertEquals((2000000.00 * 0.93), MainWindow.convert("US Dollar", "Euro", appCurrencies, 2000000.00), 0.01d);
    }

    @Test
    void testConvertWhiteBox() {
        // Test the path with the loops.
        assertEquals((500000.00 * 0.93), MainWindow.convert("US Dollar", "Euro", appCurrencies, 500000.00), 0.01d);

        // Test the path without the loops with an empty currencies list.
        assertNotEquals((500000.00 * 0.93), MainWindow.convert("US Dollar", "Euro", new ArrayList<Currency>(), 500000.00), 0.01d);
    }
    @Test
    void testConvertValidAmountInvalidCurrency() {
        // Montants valides avec une devise invalide
        double[] validAmounts = {0, 1, 500000, 1000000};
        String invalidCurrency = "Nonexistent Currency";
        
        Arrays.stream(validAmounts).forEach(amount -> {
            double result = MainWindow.convert("CA Dollar", invalidCurrency, allCurrencies, amount);
            assertEquals(0.0, result, 0.01);
        });
    }
    
    @Test
    void testConvertValidAmountValidCurrency() {
        // Montants valides avec une devise valide
        double[] validAmounts = {0, 1, 500000, 1000000};
        String validCurrency = "AUD";

        Arrays.stream(validAmounts).forEach(amount -> {
            double result = MainWindow.convert("CA Dollar", validCurrency, allCurrencies, amount);
            assertEquals(amount * 11.0, result, 0.01);
        });
    }

    @Test
    void testConvertInvalidAmountInvalidCurrency() {
        // Montants invalides avec une devise invalide
        double[] invalidAmounts = {-1000000, -2, -1.01, 1000001, 2000000};
        String invalidCurrency = "Nonexistent Currency";

        Arrays.stream(invalidAmounts).forEach(amount -> {
            double result = MainWindow.convert("CA Dollar", invalidCurrency, allCurrencies, amount);
            assertEquals(0.0, result, 0.01);
        });
    }

    @Test
    void testConvertInvalidAmountValidCurrency() {
        // Montants invalides avec une devise valide
        double[] invalidAmounts = {-1000000, -2, -1.01, 1000001, 2000000};
        String validCurrency = "USD";

        Arrays.stream(invalidAmounts).forEach(amount -> {
            double result = MainWindow.convert("CA Dollar", validCurrency, allCurrencies, amount);
            assertEquals(amount * 11.0, result, 0.01);
        });
    }

    @Test
    void testConvertCalculation() {
        double[] validAmounts = {0, 1, 500000, 1000000};
        String sourceCurrency = "AUD";
        String targetCurrency = "CAD";        

        Arrays.stream(validAmounts).forEach(amount -> {
            double result = MainWindow.convert(sourceCurrency, targetCurrency, allCurrencies, amount);
            assertEquals(amount * 33.0, result, 0.01);
        });
    }

    @Test
    void testConvertNormalCase() { // Normal Case
        double result = MainWindow.convert("CA Dollar", "AU Dollar", allCurrencies, testAmount);
        assertEquals(3302.31, result, 0.01);
    }

    @Test
    void testConvertZeroAmount() { // Zero Amount
        double result = MainWindow.convert("CA Dollar", "AU Dollar", allCurrencies, 0.0);
        assertEquals(0.0, result, 0.01);
    }

    @Test
    void testConvertEmptyCurrencyList() { // Empty Currency List
        double result = MainWindow.convert("CA Dollar", "AU Dollar", new ArrayList<Currency>(), testAmount);
        assertEquals(0.0, result, 0.01);
    }

    @Test
    void testConvertNoCurrency1() { // Non-existent Source Currency
        double result = MainWindow.convert("Nonexistent Currency", "AU Dollar", allCurrencies, testAmount);
        assertEquals(0.0, result, 0.01);
    }

    @Test
    void testConvertNoCurrency2() { // Non-existent Target Currency also test shortNameCurrency2 null
        double result = MainWindow.convert("CA Dollar", "Nonexistent Currency", allCurrencies, testAmount);
        assertEquals(0.0, result, 0.01);
    }

    @Test
    void testConvertNoCurrencies() { // Non-existent Target Currency also test shortNameCurrency2 null
        double result = MainWindow.convert("Nonexistent Currency", "Nonexistent Currency", allCurrencies, testAmount);
        assertEquals(0.0, result, 0.01);
    }
    // Create the currencies list from the app.
    // Add the 
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