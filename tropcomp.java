import java.util.ArrayList;
import java.util.List;

public class tropcomp {
    public static void main(String[] args) {
        List<TestFile> tlsFiles = new ArrayList<TestFile>();
        
        Double limit = Double.parseDouble(args[3]);

        tlsFiles = tls.execute(new String[] {args[2]});

        tlsFiles.removeIf(t -> t.tcmp < limit);

        System.out.println("path, package, class, tLOCs, assertions, tcmp");
        for (TestFile testFile : tlsFiles) {
            System.out.println(testFile.toString());
        }

        tls.createCSV(tlsFiles, args[1]);

        System.out.println("Number of test files: " + tlsFiles.size());
    }
}
