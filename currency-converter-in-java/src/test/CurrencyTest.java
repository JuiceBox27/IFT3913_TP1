package test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Test;

import currencyConverter.Currency;

public class CurrencyTest {
    @Test
    public void testConvert() {
        double[] amounts = {-1000000, -1, 0, 1, 500000, 1000000, 1000001, 2000000};
        // double[] amounts = {0, 1, 500000, 1000000};
        double testValue = Currency.convert(1d, 2d);

        // Verify the conversion is accurate and precise.
        assertAll(
            () -> Arrays.stream(amounts)
                        .forEach(a -> assertEquals(Currency.convert(a, 2.0d), a * 2.0d))
        );
    } 
}
