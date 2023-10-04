package testFiles;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Scanner;


public class SimpleExampleTests {
    @Test
    public void TestAsserts() {
        Object obj = new Object();
        assertTrue(true);
        assertFalse(false);
        assertEquals(obj, obj);
        assertNotEquals(new Object(), new Object());
        assertSame(obj, obj);
        assertNotSame(new Object(), new Object());
        assertArrayEquals(new int[]{}, new int[]{});
        assertNull(null);
        assertNotNull(obj);
        assertThrows(Exception.class, () -> new Scanner("").nextLine());
    }
}
