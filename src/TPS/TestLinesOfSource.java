package TPS;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.stream.Stream;

public class TestLinesOfSource {
    public static void main(String[] args) {
        List<TestFile> tlsFiles = execute(args);
        System.out.println("Number of test files: " + tlsFiles.size());

        writeToCmd(tlsFiles); //Output to command line.

        if (args.length == 3) {
            createCSV(tlsFiles, args[1]);
        }
    }

    public static List<TestFile> execute(String[] userInputs) {
        // Verify that the user has inputted the appropriate amount of arguments.
        if (userInputs.length != 1 && userInputs.length !=3)// || (userInputs.length != 3 && userInputs[0] != "-o"))
            return null;

        List<TestFile> tlsFiles = new ArrayList<TestFile>();
        String inputPath = userInputs.length == 1 ? userInputs[0] : userInputs[2];
        File file = new File(inputPath);
            
        tlsFiles = tlsFilesInDirectory(file, tlsFiles);

        return tlsFiles;
    }

    public static void writeToCmd(List<TestFile> tlsFiles) {
        System.out.println("path, package, class, tLOCs, assertions, tcmp");
        for (TestFile testFile : tlsFiles) {
            System.out.println(testFile.toString());
        }
    }

    /**
     * The function `tlsFilesInDirectory` recursively searches for Java files in a directory and adds
     * them to a list of `TestFile` objects.
     * 
     * @param file The "file" parameter is the directory or file from which you want to retrieve the
     * TLS files.
     * @param testFiles A list of TestFile objects that will be populated with the TLS files found in
     * the directory.
     * @return The method `tlsFilesInDirectory` returns a `List<TestFile>`.
     */
    public static List<TestFile> tlsFilesInDirectory(File file, List<TestFile> testFiles) {
        FileFilter filter = new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.toString().endsWith("java") | pathname.isDirectory();
                }
            };

        if (file.isDirectory()) {
            for (File subFile : file.listFiles(filter)) {
                // System.out.println(subFile);

                if (subFile.isDirectory()) {
                    Stream.concat(testFiles.stream(), tlsFilesInDirectory(subFile, testFiles).stream()).toList();
                } else {
                    testFiles.add(tlsFile(subFile));
                }
            }

        }
        else {
            testFiles.add(tlsFile(file));
        }

        return testFiles;
    } 

    /**
     * The function "tlsFile" creates a new TestFile object with the path, package name, class name,
     * lines of code, and assertions of a given test file.
     * 
     * @param testFile The testFile parameter is a File object that represents a test file.
     * @return The method is returning a TestFile object.
     */
    public static TestFile tlsFile(File testFile) {
        return new TestFile(testFile.getPath(), parsePackageName(testFile), parseClassName(testFile), TestLinesOfCode.getLOCsInFile(testFile), TestAssertions.getAssertionsInFile(testFile));
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
            String className = scanner.findAll("class\s+.*(\\{|\n.*)")
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

    /**
     * The function creates a CSV file and writes the contents of a list of TestFile objects to it.
     * 
     * @param testFiles A list of TestFile objects.
     * @return The method is returning a File object.
     */
    public static File createCSV(List<TestFile> testFiles, String csvPath) {
        File outputFile = new File(csvPath);

        try {
            FileWriter fileWriter;

            outputFile.createNewFile();
            fileWriter = new FileWriter(outputFile);

            fileWriter.append("path, package, class, tLOCs, assertions, tcmp\n");

            for (TestFile testFile : testFiles) {
                fileWriter.append(testFile.toString() + "\n");
            }

            fileWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return outputFile;
    }
}
