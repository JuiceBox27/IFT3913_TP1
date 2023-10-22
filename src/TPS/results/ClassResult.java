package TPS.results;

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
    CKClassResult ckClassResult;
    int testsInClass;
    
    public ClassResult(CKClassResult result) {
        this.ckClassResult = result;

        Map<String, Set<String>> assertions = new HashMap<String, Set<String>>();


        result.getMethods().stream()
        .filter(m -> getAssertionsFromMethodInvocations(m).size() > 0)
        .forEach(m ->
                assertions.put(m.getMethodName(), getAssertionsFromMethodInvocations(m))
        );

        this.testsInClass = assertions.size();

        // result.getMethods().forEach(m ->

        // );
    }

    public String getClassName() {
        return ckClassResult.getClassName();
    }

    public int getLinesOfCode() {
        return ckClassResult.getLoc();
    }

    public int numberOfMethods() {
        return ckClassResult.getMethods().size();
    }

    public int numberOfTestMethods() {
        return testsInClass;
    }

    public int numberOfFunctionalMethods() {
        return numberOfMethods() - numberOfTestMethods();
    }
    
    private boolean isTestMethod(CKMethodResult m) {
        return m.getMethodInvocations().stream().anyMatch(i -> isInvocationAssertion(i));
    }

    private static Set<String> getAssertionsFromMethodInvocations(CKMethodResult m) {
        return m.getMethodInvocations().stream()
                .filter(i -> isInvocationAssertion(i))
                .collect(Collectors.toSet());
    }

    private static boolean isInvocationAssertion(String invocation) {
        return Arrays.stream(ASSERTIONS).anyMatch(invocation::contains);
    }

    // private int getAssertionsInClass() {

    // }


    public static int totalMethods(Map<String, ClassResult> myResults) {
		return myResults.values().stream().mapToInt(t -> t.numberOfMethods()).sum();
	}

    public static int totalTestMethods(Map<String, ClassResult> myResults) {
		return myResults.values().stream().mapToInt(t -> t.numberOfTestMethods()).sum();
	}

    public static int totalFunctionalMethods(Map<String, ClassResult> myResults) {
		return myResults.values().stream().mapToInt(t -> t.numberOfFunctionalMethods()).sum();
	}

    @Override
    public String toString() {
        return  "-----\n" + getClassName() 
                + "\nloc: " + getLinesOfCode() 
                + "\n#methods: " + numberOfMethods() 
                + "\n#testMethods: " + numberOfTestMethods()
                + "\n#FunctionalMethods: " + numberOfFunctionalMethods();
    }
}
