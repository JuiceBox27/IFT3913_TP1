package TP2.results;

import java.util.Map;

public class ProjectResult extends SourceResult {

    double codeTestsLocRatio;

    public ProjectResult(Map<String, ClassResult> results, int testsLoc) {
        super(TYPE_PROJECT, results);
        
        this.codeTestsLocRatio = codeTestsLocRatio(results, testsLoc);
    }

    private double codeTestsLocRatio(Map<String, ClassResult> results, int testsLoc) {
		return (double)loc / (double)testsLoc;
	}
    
    @Override
    public int totalLoc(Map<String, ClassResult> results) {
		return results.values().stream().mapToInt(t -> t.getCkClassResult().getLoc()).sum();
	}

    @Override
    public int totalMethods(Map<String, ClassResult> results) {
		return results.values().stream().mapToInt(t -> t.numberOfMethods()).sum();
	}
    
    @Override
    public String toCsv() {
        return  type
            + ", " + loc
            + ", " + qtMethods
            + ", " + codeTestsLocRatio
        ;
    }
}
