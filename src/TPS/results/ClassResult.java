package TPS.results;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.mauricioaniche.ck.CKClassResult;
import com.github.mauricioaniche.ck.CKMethodResult;

public class ClassResult {

    private static final String[] ASSERTIONS = {"assertTrue", "assertFalse", 
                                                "assertEquals", "assertNotEquals",
                                                "assertSame", "assertNotSame",
                                                "assertNull", "assertNotNull",
                                                "assertArrayEquals", "assertThrows",
                                            };
    private CKClassResult ckClassResult;

    public ClassResult(CKClassResult result) {
        this.ckClassResult = result;
    }

    public CKClassResult getCkClassResult() {
        return ckClassResult;
    }

    public int numberOfMethods() {
        return ckClassResult.getMethods().size();
    }

    public int numberOfFunctionalMethods() {
        return numberOfMethods() - numberOfTestMethods();
    }

    public int numberOfTestMethods() {
        return getTestMethods(ckClassResult).size();
    }

    public int testMethodsLoc() {
        return getTestMethods(ckClassResult).keySet().stream()
            .mapToInt(k -> k.getLoc()).sum();
    }

    public int cloc() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(ckClassResult.getFile())));
    
            return content.lines().filter(l -> !l.isBlank()).toList().size() - ckClassResult.getLoc();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public double commentsDensity() {
        double cloc = cloc();
        double ncloc = ckClassResult.getLoc();

        return cloc / (ncloc + cloc);
    }

    // public int functionalMethodsLoc() {
    //     return getFunctionalMethods(ckClassResult).values().stream()
    //         .mapToInt(k -> k.getLoc()).sum();
    // }

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
		return results.values().stream().mapToInt(t -> t.testMethodsLoc()).sum();        
    }

//#endregion

//#region private methods for lambda exps. (helper methods)

    private static Map<String, CKMethodResult> getFunctionalMethods(CKClassResult ckClassResult) {
        Map<String, CKMethodResult> methodsWithoutAssertions = new HashMap<String, CKMethodResult>();

        ckClassResult.getMethods().stream()
        .filter(m -> isFunctionalMethod(m)).forEach(m ->
            methodsWithoutAssertions.put(m.getMethodName(), m)
        );

        return methodsWithoutAssertions;
    }

    private static boolean isFunctionalMethod(CKMethodResult m) {
        return m.getMethodInvocations().stream().anyMatch(i -> !isInvocationAssertion(i));
    }

    private static Map<CKMethodResult, Set<String>> getTestMethods(CKClassResult ckClassResult) {
        Map<CKMethodResult, Set<String>> methodsWithAssertions = new HashMap<CKMethodResult, Set<String>>();

        ckClassResult.getMethods().stream()
        .filter(m -> getAssertionsFromMethodInvocations(m).size() > 0)
        .forEach(m ->
                methodsWithAssertions.put(m, getAssertionsFromMethodInvocations(m))
        );

        return methodsWithAssertions;
    }

    private static Set<String> getAssertionsFromMethodInvocations(CKMethodResult m) {
        return m.getMethodInvocations().stream()
                .filter(i -> isInvocationAssertion(i))
                .collect(Collectors.toSet());
    }

    private static boolean isInvocationAssertion(String invocation) {
        return Arrays.stream(ASSERTIONS).anyMatch(invocation::contains);
    }

//#endregion

    public String toCsv() {
        return ckClassResult.getClassName() 
            + ", " + ckClassResult.getLoc() 
            + ", " + cloc()
            + ", " + commentsDensity()
            + ", " + testMethodsLoc()
            + ", " + numberOfMethods()
            + ", " + numberOfTestMethods()
            + ", " + numberOfFunctionalMethods()
            + ", " + ckClassResult.getRfc()
            + ", " + ckClassResult.getWmc()
        ;
    }

    @Override
    public String toString() {
        return  "-----\n" + ckClassResult.getClassName() 
            + "\nncloc: " + ckClassResult.getLoc() 
            + "\ncloc: " + cloc()
            + "\nDC: " + commentsDensity()
            + "\ntestsLoc: " + testMethodsLoc()
            + "\n#methods: " + numberOfMethods() 
            + "\n#testMethods: " + numberOfTestMethods()
            + "\n#FunctionalMethods: " + numberOfFunctionalMethods()
            + "\nrfc: " + ckClassResult.getRfc()
            + "\nwmc: " + ckClassResult.getWmc()
        ;
                // + "\nfunctionalMethodsLoc: " + functionalMethodsLoc();
    }
} 