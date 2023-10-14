package TPS;

class TestFile {
    String filePath;
    String packageName;
    String className;
    int totalLOCs;
    int totalAssertions;

    double tcmp;

    public TestFile(String filePath, String packageName, String className, int totalLOCs, int totalAssertions) {
        this.filePath = filePath;
        this.packageName = packageName;
        this.className = className;
        this.totalLOCs = totalLOCs;
        this.totalAssertions = totalAssertions;

        tcmp = TestLinesOfSource.calculateTCMP(totalLOCs, totalAssertions);
    }

    @Override
    public String toString() {
        return filePath + ", " + packageName + ", " + className + ", " + totalLOCs + ", " + totalAssertions + ", " + tcmp;
    }
}

