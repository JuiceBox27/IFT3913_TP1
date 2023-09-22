import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class tloc {
    public static void main(String[] args) {
        tloc myTloc = new tloc();
        
        LinkedList<String> lines = myTloc.GetLinesInFile("SimpleExampleFile.java");

        for (String line : lines) {
            System.out.println(line);
        }
        System.out.println(lines.size());
    }

    // Verify whether the line is a line of code and return as boolean.
    // Return false when: blank, single line comment, (multiline comment opening, body or closing).
    boolean IsLoc(String line) {
        if (line.isBlank())
            return false;
        
        return true;

        // Trying to match the regex for single comments
        // return !(line.matches("(//"));

        // The next idea in determining whether a line is a line of code:
        // - Have a boolean 'isMultiline' that is set 'true' when an multiline comment opening '/*' is matched
        // - Toggle the boolean to 'false' when the multiline comment closing '*/' but still return false
        // Return false if the boolean 'isMultiline' is true.
        // An issue that will occur is the multiline comment possibly being fitted in code 
        //      (ex.: in the declaration of a variable: int /* comment in declaration */ myInt = 0;
        //      This should return a valid LOC.
    }

    // Get the lines of code from the file.
    // The blank lines and lines that are uniquely comments are removed.
    LinkedList<String> GetLinesInFile(String filePath) {
        FileReader fileReader;
        BufferedReader reader = null;
        LinkedList<String> lines = new LinkedList<String>();

        try {
            String line;

            fileReader = new FileReader(filePath);
            reader = new BufferedReader(fileReader);

            while ((line = reader.readLine()) != null) {
                if (IsLoc(line)) {
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