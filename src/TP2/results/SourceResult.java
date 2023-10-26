package TP2.results;

import java.util.Map;

public class SourceResult implements Result {
    public static final String CSV_HEADER = "type, loc, qtMethods, codeSize/testsSize";
    public static final String TYPE_PROJECT = "Project";
    public static final String TYPE_FUNCTIONAL = "Functional";
    public static final String TYPE_TESTS = "Tests";

    String type;
    int loc;
    int qtMethods;
    // int 

    public SourceResult(Map<String, ClassResult> results) {
        this(TYPE_FUNCTIONAL, results);
    }

    public SourceResult(String type, Map<String, ClassResult> results) {
        this.type = type;
        this.loc = totalLoc(results);
        this.qtMethods = totalMethods(results);
    }

//#region totals

    public int totalLoc(Map<String, ClassResult> results) {
		return results.values().stream().filter(t -> t.numberOfTestMethods() == 0).mapToInt(t -> t.getCkClassResult().getLoc()).sum();
	}

    public int totalMethods(Map<String, ClassResult> results) {
		return results.values().stream().mapToInt(t -> t.numberOfFunctionalMethods()).sum();
	}

//#endregion

    public int getLoc() {
        return loc;
    }

    @Override
    public String toCsv() {
        return  type
            + ", " + loc
            + ", " + qtMethods
            + ", " + "N/A"
        ;
    }

    @Override
    public String getCsvHeader() {
        return CSV_HEADER;
    }

    @Override
    public String toString() {
        return  "-----\n";
    }
}
