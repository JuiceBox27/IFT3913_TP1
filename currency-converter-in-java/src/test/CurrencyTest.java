package test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import currencyConverter.Currency;

public class CurrencyTest {

    // A black box domain test and limit values test.
    @Test
    public void testConvertBlackBox() {
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
}