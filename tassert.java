import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class tassert {
    public static void main(String[] args) {
        tloc myTloc = new tloc();
        tassert myTassert = new tassert();

        LinkedList<String> assertions = myTassert.GetAssertionsInFile("SimpleExampleTests.java");

        System.out.println("--------Assertions--------");
        for (String assertion : assertions) {
            System.out.println(assertion);
        }
        System.out.println("Number of assertions: " + assertions.size());
    }

    static boolean isAssertion(String next) {
        if (next.matches(".*(?<!org.junit.Assert.)"
                        + "(assertTrue|assertFalse"
                        + "|assertEquals|assertNotEquals"
                        + "|assertSame|assertNotSame"
                        + "|assertNull|assertNotNull"
                        + "|assertArrayEquals|assertThrows).*"))
            return true;
            
        return false;
    }

    LinkedList<String> GetAssertionsInFile(String filePath) {
        Scanner reader = null;
        LinkedList<String> assertions = new LinkedList<String>();

        try {
            String assertion;
            reader = new Scanner(new File(filePath));

            while (reader.hasNext()) {
                assertion = reader.next();
                
                if (isAssertion(assertion)) {
                    assertions.add(assertion);
                }
                System.out.println(assertion);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return assertions;
    }
}