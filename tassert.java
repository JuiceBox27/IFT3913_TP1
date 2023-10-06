import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class tassert {
    File file;

    public static void main(String[] args) {
        LinkedList<String> assertions = tassert.getAssertionsInFile(args[0]);

        // debug(assertions);

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

    public static int getAssertionsInFile(File file) {
        return getAssertionsInFile(file.getPath()).size();
    }

    /**
     * The function `GetAssertionsInFile` reads a file line by line and returns a linked list
     * containing all the assertions found in the file.
     * 
     * @param filePath The `filePath` parameter is a string that represents the path to the file from
     * which we want to extract assertions.
     * @return The method is returning a LinkedList of Strings.
     */
    public static LinkedList<String> getAssertionsInFile(String filePath) {
        LinkedList<String> assertions = new LinkedList<String>();

        try {
            Scanner reader = new Scanner(new File(filePath));
            String assertion;

            while (reader.hasNext()) {
                assertion = reader.next();
                
                if (isAssertion(assertion)) {
                    assertions.add(assertion);
                }
                // System.out.println(assertion);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return assertions;
    }

    /**
     * The debug function prints out a list of assertions.
     * 
     * @param assertions A list of strings representing the assertions that need to be debugged.
     */
    private static void debug(List<String> assertions) {
        System.out.println("--------Assertions--------");
        for (String assertion : assertions) {
            System.out.println(assertion);
        }
    }
}