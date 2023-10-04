import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class tloc {
    public static void main(String[] args) {
        tloc myTloc = new tloc();
        
        LinkedList<String> lines = myTloc.GetLinesInFile("SimpleExampleFile.java");


        System.out.println("--------Lines of Code--------");
        for (String line : lines) {
            System.out.println(line);
        }
        System.out.println("Lines of code (LOCs): " + lines.size());
    }

    /**
     * The function `parseLoc` removes comments from a given line of code, taking into account both
     * single-line and multi-line comments.
     * 
     * @param line The `line` parameter is a string representing a line of code or a comment.
     * @param lines The `lines` parameter is a `LinkedList` of strings.
     * @return The method is returning a modified version of the input line.
     */
    public static String parseLoc(String line, LinkedList<String> lines) {
        if (line.matches(".*//.*")) {
            line = line.split("//")[0];
        }

        if (line.matches(".*/\\*.*\\\\*/.*")) {
            String [] lineParts = line.split("/\\*.*\\\\*/");

            line = (lineParts.length == 0) ? line = "" : lineParts[0] + ((lineParts.length == 1) ? "" : lineParts[1]);
        }

        if (line.matches(".*/\\*.*"))
            line = line.split("/\\*")[0];

        if (line.matches(".*\\*/.*")) {
            String [] lineParts = line.split("\\*/");

            line = lines.pollLast();

            if (lineParts.length > 1)
                line += lineParts[1];
        }

        return line;
    }

    /**
     * The function `GetLinesInFile` reads a file line by line and returns a linked list containing
     * only the lines that meet the conditions to be considered a line of code.
     * 
     * @param filePath The `filePath` parameter is a `String` that represents the path to the file from
     * which we want to read the lines.
     * @return The method is returning a LinkedList of Strings.
     */
    LinkedList<String> GetLinesInFile(String filePath) {
        FileReader fileReader;
        BufferedReader reader = null;
        LinkedList<String> lines = new LinkedList<String>();

        try {
            String line;

            fileReader = new FileReader(filePath);
            reader = new BufferedReader(fileReader);

            while ((line = reader.readLine()) != null) {
                line = parseLoc(line, lines);

                if (!line.isBlank()) {
                    lines.add(line);
                }
            }

            reader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }
}