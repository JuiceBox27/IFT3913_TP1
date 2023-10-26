package TP2.results;

import java.util.Map;

public class TestsResult extends SourceResult {
    double codeTestsLocRatio;

    public TestsResult(Map<String, ClassResult> results) {
        super(TYPE_TESTS, results);

        this.type = TYPE_TESTS;
    }

    @Override
    public int totalLoc(Map<String, ClassResult> results) {
		return results.values().stream().filter(t -> t.numberOfTestMethods() > 0).mapToInt(t -> t.getCkClassResult().getLoc()).sum();
    }

    @Override
    public int totalMethods(Map<String, ClassResult> results) {
		return results.values().stream().mapToInt(t -> t.numberOfTestMethods()).sum();
	}
}
