package TPS.results;

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
    
    public static final String CSV_HEADER = "class, ncloc, cloc, dc, testMethodsLoc, numberOfMethods, numberOfTestMethods, numberOfFunctionalMethods, rfc, wmc";

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

        testMethods = getTestMethods(result);

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

    public double calcCommentsDensity() {
        double ncloc = ckClassResult.getLoc();

        return (double)(cloc / (ncloc + cloc));
    }

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