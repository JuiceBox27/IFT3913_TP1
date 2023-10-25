package TPS.results;

import java.util.Map;

public class SourceResult implements Result {
    public static final String CSV_HEADER = "type, loc";

    String type;
    int loc;
    int qtMethods;
    // int 

    public SourceResult(int loc, int qtMethods) {
        this.loc = loc;
        this.qtMethods = qtMethods;
    }

//#region totals
    public static int totalLoc(Map<String, ClassResult> results) {
		return results.values().stream().mapToInt(t -> t.getCkClassResult().getLoc()).sum();
	}

    public static int totalMethods(Map<String, ClassResult> results) {
		return results.values().stream().mapToInt(t -> t.numberOfMethods()).sum();
	}

    public static int totalTestMethods(Map<String, ClassResult> results) {
		return results.values().stream().mapToInt(t -> t.numberOfTestMethods()).sum();
	}

    public static int totalFunctionalMethods(Map<String, ClassResult> results) {
		return results.values().stream().mapToInt(t -> t.numberOfFunctionalMethods()).sum();
	}

    public static int totalTestsLoc(Map<String, ClassResult> results) {
		return results.values().stream().filter(t -> t.numberOfTestMethods() > 0).mapToInt(t -> t.getCkClassResult().getLoc()).sum();
    }

//#endregion

    public static double codeTestsLocRatio(Map<String, ClassResult> results) {
		return (double)SourceResult.totalLoc(results) / (double)SourceResult.totalTestsLoc(results);
	}

    @Override
    public String toCsv() {
        return "";
    }
    
    @Override
    public String toString() {
        return  "-----\n";
    }

    @Override
    public String getCsvHeader() {
        return CSV_HEADER;
    }
}
