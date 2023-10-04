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

    /**
     * The function checks if a given string contains any assertion methods commonly used in JUnit
     * testing.
     * 
     * @param next The "next" parameter is a string that represents the next line of code in a program.
     * @return The method is returning a boolean value.
     */
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

    /**
     * The function `GetAssertionsInFile` reads a file line by line and returns a linked list
     * containing all the assertions found in the file.
     * 
     * @param filePath The `filePath` parameter is a string that represents the path to the file from
     * which we want to extract assertions.
     * @return The method is returning a LinkedList of Strings.
     */
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