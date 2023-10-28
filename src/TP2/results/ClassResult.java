package TP2.results;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.mauricioaniche.ck.CKClassResult;
import com.github.mauricioaniche.ck.CKMethodResult;

public class ClassResult implements Result {
    
    public static final String CSV_HEADER = "class, ncloc, cloc, dc, testMethodsLoc, numberOfMethods, numberOfTestMethods, numberOfFunctionalMethods, rfc, wmc, gitFileAge, gitTotCommits";

    CKClassResult ckClassResult;

    String lastCommitDate;
    int commitCount;

    Map<CKMethodResult, Set<String>> testMethods;

    double commentsDensity;
    int cloc;

    public ClassResult(CKClassResult result, String lastCommitDate, int commitCount) {
        this.ckClassResult = result;
        this.lastCommitDate = lastCommitDate;
        this.commitCount = commitCount;

        testMethods = calcTestMethods(result);

        this.cloc = measureCloc();
        this.commentsDensity = calcCommentsDensity();

        // this.lastCommitDate = gitCmd();
    }

//#region getters

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
        return testMethods.size();
    }

    public int testMethodsLoc() {
        return testMethods.keySet().stream()
            .mapToInt(k -> k.getLoc()).sum();
    }
//#endregion


//#region measures and calculations

    /**
     * The function calculates the comments density by dividing the number of lines of comments by the
     * total number of lines of code.
     * 
     * @return The method is returning the comments density, which is calculated by dividing the number
     * of lines of comments (cloc) by the sum of the number of non-comment lines of code (ncloc) and
     * the number of lines of comments (cloc).
     */
    public double calcCommentsDensity() {
        double ncloc = ckClassResult.getLoc();

        return (double)(cloc / (ncloc + cloc));
    }

    /**
     * The function measures the number of lines of code in a file, excluding blank lines and a
     * specified number of lines at the beginning.
     * 
     * @return The method is returning the difference between the number of non-blank lines in the
     * content of a file and the value of `ckClassResult.getLoc()`.
     */
    public int measureCloc() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(ckClassResult.getFile())));
    
            return content.lines().filter(l -> !l.isBlank()).toList().size() - ckClassResult.getLoc();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }


//#endregion

//#region private methods for lambda exps. (helper methods)

    /**
     * The function `getTestMethods` takes a `CKClassResult` object and returns a map of methods with
     * their corresponding assertions.
     * 
     * @param ckClassResult The parameter `ckClassResult` is an object of type `CKClassResult`.
     * @return The method is returning a Map object, where the keys are CKMethodResult objects and the
     * values are Set objects containing strings.
     */
    private static Map<CKMethodResult, Set<String>> calcTestMethods(CKClassResult ckClassResult) {
        Map<CKMethodResult, Set<String>> methodsWithAssertions = new HashMap<CKMethodResult, Set<String>>();

        ckClassResult.getMethods().stream()
        .filter(m -> getAssertionsFromMethodInvocations(m).size() > 0)
        .forEach(m ->
                methodsWithAssertions.put(m, getAssertionsFromMethodInvocations(m))
        );

        return methodsWithAssertions;
    }

    /**
     * The function returns a set of assertions from a given method's invocations.
     * 
     * @param m A CKMethodResult object, which represents the result of a method analysis in the CK
     * code metrics library. It contains information about the method, such as its name, type, and
     * invocations.
     * @return The method is returning a Set of Strings.
     */
    private static Set<String> getAssertionsFromMethodInvocations(CKMethodResult m) {
        return m.getMethodInvocations().stream()
                .filter(i -> isInvocationAssertion(i))
                .collect(Collectors.toSet());
    }

    /**
     * The function checks if a given invocation contains any of the specified assertions.
     * 
     * @param invocation The `invocation` parameter is a string that represents a method invocation.
     * @return The method is returning a boolean value.
     */
    private static boolean isInvocationAssertion(String invocation) {
        return Arrays.stream(ASSERTIONS).anyMatch(invocation::contains);
    }

//#endregion

    public Map<CKMethodResult, Set<String>> getTestMethods() {
        return testMethods;
    }

    public double getCommentsDensity() {
        return commentsDensity;
    }

    public String toCsv() {
        return ckClassResult.getClassName() 
            + ", " + ckClassResult.getLoc() 
            + ", " + cloc
            + ", " + commentsDensity
            + ", " + testMethodsLoc()
            + ", " + numberOfMethods()
            + ", " + numberOfTestMethods()
            + ", " + numberOfFunctionalMethods()
            + ", " + ckClassResult.getRfc()
            + ", " + ckClassResult.getWmc()
            + ", " + lastCommitDate
            + ", " + commitCount
        ;
    }

    public String getCsvHeader() {
        return CSV_HEADER;
    }

    @Override
    public String toString() {
        return  "-----\n" + ckClassResult.getClassName() 
            + "\nncloc: " + ckClassResult.getLoc() 
            + "\ncloc: " + cloc
            + "\nDC: " + commentsDensity
            + "\ntestsLoc: " + testMethodsLoc()
            + "\n#methods: " + numberOfMethods() 
            + "\n#testMethods: " + numberOfTestMethods()
            + "\n#FunctionalMethods: " + numberOfFunctionalMethods()
            + "\nrfc: " + ckClassResult.getRfc()
            + "\nwmc: " + ckClassResult.getWmc()
            + "\nlast commit: " + lastCommitDate
            + "\ncommit count: " + commitCount
        ;
                // + "\nfunctionalMethodsLoc: " + functionalMethodsLoc();
    }
} 