package TP2.results;

import java.util.Map;

public class ProjectResult extends SourceResult {

    double codeTestsLocRatio;

    public ProjectResult(Map<String, ClassResult> results, int testsLoc) {
        super(TYPE_PROJECT, results);
        
        this.codeTestsLocRatio = codeTestsLocRatio(results, testsLoc);
    }

    /**
     * The function calculates the ratio of lines of code to lines of test code.
     * 
     * @param results The parameter "results" is a map where the keys are strings representing class
     * names and the values are objects of type ClassResult.
     * @param testsLoc The parameter "testsLoc" represents the total lines of code in the tests.
     * @return The method is returning a double value.
     */
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
            + ", " + locPerMethod
        ;
    }
}
