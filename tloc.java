import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class tloc {
    public static void main(String[] args) {
        List<String> lines = getLOCsInFilePath(args[0]);

        // debug(lines);

        System.out.println("Lines of code (LOCs): " + lines.size());
    }


    /**
     * The function returns the number of lines of code in a given file.
     * 
     * @param file The parameter "file" is of type File and represents a file object.
     * @return The number of lines of code (LOCs) in the file.
     */
    public static int getLOCsInFile(File file) {
        return getLOCsInFilePath(file.getPath()).size();
    }

    /**
     * The function `getLOCsInFilePath` reads the content of a file, removes single and multiline
     * comments, and returns a list of non-blank lines.
     * 
     * @param filePath The filePath parameter is a string that represents the path to the file from
     * which you want to extract lines of code (LOCs).
     * @return The method is returning a List of Strings.
     */
    public static List<String> getLOCsInFilePath(String filePath) {
        List<String> lines = new LinkedList<String>();

        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));

            content = content.replaceAll("\\/\\/.*", "");               // Remove single comments
            content = content.replaceAll("(?ms)\\/\\*.*?\\*\\/", "");   // Remove multiline comments (?ms required for m: mulitline, s: . counts for spaces too)
            
            // content.lines().forEach(System.out::println);

            // Add the line if it is not blank
            lines = content.lines().filter(n -> !n.isBlank()).toList();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    /**
     * The debug function prints out each line of code in a given list.
     * 
     * @param lines The parameter "lines" is a List of Strings. It represents a collection of lines of
     * code that will be printed out for debugging purposes.
     */
    private static void debug(List<String> lines) {
        System.out.println("--------Lines of Code--------");
        for (String line : lines) {
            System.out.println(line);
        }
    }
}