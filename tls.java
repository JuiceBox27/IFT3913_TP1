import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;

public class tls {
    public static void main(String[] args) {
        File file = new File("testFiles");
        List<TestFile> tlsFiles = tlsFilesInDirectory(file);


        System.out.println("path, package, class, tLOCs, assertions, tcmp");
        for (TestFile testFile : tlsFiles) {
            System.out.println(testFile.toString());
        }
    }

    public static List<TestFile> tlsFilesInDirectory(File file) {
        List<TestFile> testFiles = new ArrayList<TestFile>();
        
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File testFile : files) {
                System.out.println(testFile.getPath());
                testFiles.add(tlsFile(testFile));
            }
        }

        return testFiles;
    }

    public static TestFile tlsFile(File testFile) {
        return new TestFile(testFile.getPath(), parsePackageName(testFile), parseClassName(testFile), tloc.getLOCsInFile(testFile), tassert.getAssertionsInFile(testFile));
    }

    /**
     * The function `parsePackageName` takes a `File` object as input and returns the package name
     * declared in the file.
     * 
     * @param testFile The testFile parameter is a File object that represents the file from which you
     * want to parse the package name.
     * @return The method is returning a String representing the package name extracted from the given
     * testFile.
     */
    public static String parsePackageName(File testFile) {
        try {
            Scanner scanner = new Scanner(testFile);
            String packageName = scanner.findAll(".*package\s*.*\\;")
                                    .map(MatchResult::group).findFirst().get()
                                    .split("\s*package\s+")[1].split("\\;.*")[0];

            scanner.close();
            return packageName;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * The function `parseClassName` takes a `File` object as input, reads the contents of the file,
     * and extracts the name of the class defined in the file.
     * 
     * @param testFile The testFile parameter is a File object that represents the file from which you
     * want to parse the class name.
     * @return The method is returning a String, which is the name of the class parsed from the given
     * testFile.
     */
    public static String parseClassName(File testFile) {
        try {
            Scanner scanner = new Scanner(testFile);
            String className = scanner.findAll("class\s*.*(\\{|\n)")
                                    .map(MatchResult::group).findFirst().get()
                                    .split(".*class\s*")[1].split("\s*\\{.*")[0];

            scanner.close();
            
            return className;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * The function calculates the Test Code to Main Code Proportion (TCMP) by dividing the total lines
     * of code (LOCs) by the total number of assertions.
     * 
     * @param totalLOCs The total number of lines of code in a program.
     * @param totalAssertions The total number of assertions in the code.
     * @return The method is returning a double value.
     */
    public static double calculateTCMP(int totalLOCs, int totalAssertions) {
        return (double)totalLOCs / (double)totalAssertions;
    }
}

class TestFile {
    String filePath;
    String packageName;
    String className;
    int totalLOCs;
    int totalAssertions;

    private double tcmp;

    public TestFile(String filePath, String packageName, String className, int totalLOCs, int totalAssertions) {
        this.filePath = filePath;
        this.packageName = packageName;
        this.className = className;
        this.totalLOCs = totalLOCs;
        this.totalAssertions = totalAssertions;

        tcmp = tls.calculateTCMP(totalLOCs, totalAssertions);
    }

    @Override
    public String toString() {
        return filePath + ", " + packageName + ", " + className + ", " + totalLOCs + ", " + totalAssertions + ", " + tcmp;
    }
}
