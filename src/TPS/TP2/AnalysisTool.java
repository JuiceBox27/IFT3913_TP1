package TPS.TP2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKClassResult;
import com.github.mauricioaniche.ck.CKMethodResult;
import com.github.mauricioaniche.ck.CKNotifier;
import com.github.mauricioaniche.ck.ResultWriter;
import com.github.mauricioaniche.ck.Runner;
import com.github.mauricioaniche.ck.util.FileUtils;

import TPS.results.ClassResult;

public class AnalysisTool {
    public static void main(String[] args) throws IOException {
        Map<String, CKClassResult> results = getCKClassResultsMap(args, false);
        Map<String, ClassResult> myResults = new HashMap<String, ClassResult>();
        
        results.forEach((k, v) -> myResults.put(k, new ClassResult(v)));

		myResults.entrySet().stream()
		// .filter(e -> e.getValue().numberOfNonTestMethods() > 0)
		.forEach((e -> System.out.println(e.getValue().toString())));

		System.out.println("+-+-+-+-+-+-+-+");
		System.out.println("#totLoc: " + ClassResult.totalLoc(myResults));
		System.out.println("#totTestMethodsLoc: " + ClassResult.totalTestsLoc(myResults));
		System.out.println("----------");
		System.out.println("#totMethods: " + ClassResult.totalMethods(myResults));
		System.out.println("#totTestMethods: " + ClassResult.totalFunctionalMethods(myResults));
		System.out.println("#totFunctionalMethods: " + ClassResult.totalTestMethods(myResults));
    }

    private static Map<String, CKClassResult> getCKClassResultsMap(String[] runnerArgs, boolean runnerPrintResults) {
        if (runnerArgs == null || runnerArgs.length < 1) {
			System.out.println("Usage java -jar ck.jar <path to project> <use Jars=true|false> <max files per partition, 0=automatic selection> <print variables and fields metrics? True|False> <path to save the output files>");
			System.exit(1);
		}

		String path = runnerArgs[0];

		// use jars?
		boolean useJars = false;
		if(runnerArgs.length >= 2)
			useJars = Boolean.parseBoolean(runnerArgs[1]);

		// number of files per partition?
		int maxAtOnce = 0;
		if(runnerArgs.length >= 3)
			maxAtOnce = Integer.parseInt(runnerArgs[2]);

		// variables and field results?
		boolean variablesAndFields = true;
		if(runnerArgs.length >= 4)
			variablesAndFields = Boolean.parseBoolean(runnerArgs[3]);
		
		// path where the output csv files will be exported
		String outputDir = "";
		if(runnerArgs.length >= 5)
			outputDir = runnerArgs[4];

		// load possible additional ignored directories
		//noinspection ManualArrayToCollectionCopy
		for (int i = 5; i < runnerArgs.length; i++) {
		FileUtils.IGNORED_DIRECTORIES.add(runnerArgs[i]);
		}

		
		Map<String, CKClassResult> results = new HashMap<>();
		
		new CK(useJars, maxAtOnce, variablesAndFields).calculate(path, new CKNotifier() {
			@Override
			public void notify(CKClassResult result) {
				
				// Store the metrics values from each component of the project in a HashMap
				results.put(result.getClassName(), result);
				
			}

			@Override
			public void notifyError(String sourceFilePath, Exception e) {
				System.err.println("Error in " + sourceFilePath);
				e.printStackTrace(System.err);
			}
		});

        // Print the results using the CK method
        if(runnerPrintResults) {
            try {
                runnerPrintResults(results, outputDir, variablesAndFields);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

		System.out.println("Metrics extracted!!!");

        return results;
    }

    private static void runnerPrintResults(Map<String, CKClassResult> results, String outputDir, boolean variablesAndFields) throws IOException {
        ResultWriter writer = new ResultWriter(outputDir + "class.csv", outputDir + "method.csv", outputDir + "variable.csv", outputDir + "field.csv", variablesAndFields);

        // // Write the metrics value of each component in the csv files
		for(Map.Entry<String, CKClassResult> entry : results.entrySet()){
			writer.printResult(entry.getValue());
		}
		
		// writer.flushAndClose();
    }
}
