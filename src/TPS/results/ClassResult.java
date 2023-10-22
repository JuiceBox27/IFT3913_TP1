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

    String className;
    int linesOfCode;
    int testsInClass;
    int numberOfMethods;
    
    public ClassResult(CKClassResult result) {
        this.className = result.getClassName();
        this.linesOfCode = result.getLoc();
        this.numberOfMethods = result.getMethods().size();

        Map<String, Set<String>> assertions = new HashMap<String, Set<String>>();


        result.getMethods().forEach(m ->
                assertions.put(m.getMethodName(), getAssertionsFromMethodInvocations(m))
        );

        this.testsInClass = assertions.size();
    }

    public String getClassName() {
        return className;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    public int getNumberOfMethods() {
        return numberOfMethods;
    }

    public int getTestsInClass() {
        return testsInClass;
    }

    private static Set<String> getAssertionsFromMethodInvocations(CKMethodResult m) {
        return m.getMethodInvocations().stream()
                // .filter(i -> i.contains("assert"))
                .filter(i -> isInvocationAssertion(i))
                .collect(Collectors.toSet());
    }

    private static boolean isInvocationAssertion(String invocation) {
        return Arrays.stream(ASSERTIONS).anyMatch(invocation::contains);
    }

    // private int getAssertionsInClass() {

    // }

    @Override
    public String toString() {
        return getClassName() + ", " + getLinesOfCode() + ", " + getNumberOfMethods() + ", " + getTestsInClass();
    }
}
