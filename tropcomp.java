import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class tropcomp {
    public static void main(String[] args) {
        File file = new File("../jfreechart/src/test");
        // File file = new File("args");
        List<TestFile> tlsFiles = new ArrayList<TestFile>();
        
        tlsFiles = tls.tlsFilesInDirectory(file, tlsFiles);

        System.out.println("path, package, class, tLOCs, assertions, tcmp");
        for (TestFile testFile : tlsFiles) {
            if (testFile.tcmp < 1)
                System.out.println(testFile.toString());
        }

        tls.createCSV(tlsFiles, "test.csv");

        System.out.println("Number of test files: " + tlsFiles.size());
    }

    public static void execute(String[] userInputs) {
    }
}
