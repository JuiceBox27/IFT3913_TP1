package tp1;

import java.util.ArrayList;
import java.util.List;

public class tropcomp {
    public static void main(String[] args) {
        List<TestFile> tlsFiles = new ArrayList<TestFile>();
        
        Double limit = Double.parseDouble(args[3]);

        tlsFiles = tls.execute(new String[] {args[2]});

        tlsFiles = tropcomp.findSuspiciousFiles(tlsFiles, limit);

        System.out.println("path, package, class, tLOCs, assertions, tcmp");
        for (TestFile testFile : tlsFiles) {
            System.out.println(testFile.toString());
        }

        tls.createCSV(tlsFiles, args[1]);

        // System.out.println("Number of test files: " + tlsFiles.size());
    }

    /**
     * The function `findSuspiciousFiles` takes a list of `TestFile` objects, calculates limits based
     * on a given percentage, and removes files from the list that fall below those limits.
     * 
     * @param tlsFiles A list of TestFile objects. Each TestFile object represents a file and contains
     * information such as total lines of code (totalLOCs) and test coverage (tcmp).
     * @param limit The "limit" parameter is a double value that represents the threshold for
     * determining whether a file is suspicious or not. It is expressed as a percentage, where a value
     * of 0.0 means no files will be considered suspicious, and a value of 100.0 means all files will
     * be considered
     * @return The method is returning a List<TestFile> containing the suspicious files.
     */
    static List<TestFile> findSuspiciousFiles(List<TestFile> tlsFiles, double limit) {

        final double tlocLimit = tlsFiles.stream().mapToDouble(t -> t.totalLOCs).sum() * (1 - limit * 0.01) / tlsFiles.size();
        final double tcmpLimit = tlsFiles.stream().mapToDouble(t -> !Double.isInfinite(t.tcmp) ? t.tcmp : 0).sum() * (1 - limit * 0.01) / tlsFiles.size();

        tlsFiles.removeIf(t -> (t.totalLOCs < tlocLimit && t.tcmp < tcmpLimit));

        return tlsFiles;
    }
}
