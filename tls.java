import java.io.File;

public class tls {
    File file;

    public static void main(String[] args) {
        File file = new File("SimpleExampleTests.java");
        tls myTls = new tls(file);
        TestFile myTestFile = myTls.tlsFile(file);


        System.out.println("path\t\t\t\t\t\t\t\tpackage\t\t\t\tclass\t\t\tLOCs\t\tassertions\t\ttcmp");
        System.out.println(myTestFile.toString());
    }

    public tls (File file) {
        this.file = file;
    }

    public static TestFile tlsFile(File testFile) {
        return new TestFile(testFile.getPath(), null, null, tloc.getLOCsInFile(testFile), tassert.getAssertionsInFile(testFile));
    }

    public static double calculateTCMP(int totalLOCs, int totalAssertions) {
        return (double)(totalLOCs / totalAssertions);
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
        return filePath + "\t\t\t\t\t\t" + packageName + "\t\t\t\t" + className + "\t\t\t" + totalLOCs + "\t\t" + totalAssertions + "\t\t\t" + tcmp;
    }
}
