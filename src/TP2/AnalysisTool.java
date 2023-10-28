package TP2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKClassResult;
import com.github.mauricioaniche.ck.CKNotifier;
import com.github.mauricioaniche.ck.ResultWriter;
import com.github.mauricioaniche.ck.util.FileUtils;

import TP2.results.ClassResult;
import TP2.results.ProjectResult;
import TP2.results.Result;
import TP2.results.SourceResult;
import TP2.results.TestsResult;

public class AnalysisTool {
    public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		try {
			execute(args);
		} catch (IOException e) {
			e.printStackTrace();
		}

		debugExecTime(startTime);
	}

	/**
	 * The `execute` function takes an array of arguments, creates CKClassResults and ClassResults maps,
	 * retrieves Git information for each class, prints the ClassResults, creates SourceResults, and
	 * finally creates CSV files for the ClassResults and SourceResults.
	 */
	public static void execute(String[] args) throws IOException {
		Map<String, CKClassResult> results = createCKClassResultsMap(new String[] {args[0], "True", "0", "True", args[1]}, false);
        Map<String, ClassResult> myResults = new HashMap<String, ClassResult>();

		GitFinder gitFinder = new GitFinder(args[0]);

        results.forEach((k, v) -> {
			ClassResult cr = new ClassResult(v, gitFinder.gitCmd(v.getFile()), gitFinder.gitFileCommits(v.getFile()));
			myResults.put(k, cr);
			System.out.println(cr.toString());
		});
		
		List<SourceResult> sourceResults = createSourceResults(myResults);
		
		System.out.println("+-+-+-+-+-+-+-+");

		createTestsClassResultFilteredCsv(myResults, args[1]);

		createCSV(myResults.values().stream().map(t -> (Result)t).toList(), args[1] + "/ClassResults.csv");
		createCSV(sourceResults.stream().map(t -> (Result)t).toList(), args[1] + "/SourceResults.csv");
	}

	/**
	 * The function creates a list of SourceResult objects based on the input map of ClassResult objects.
	 * 
	 * @param results A map where the keys are strings representing source file names and the values are
	 * ClassResult objects representing the results of analyzing each source file.
	 * @return The method is returning a List of SourceResult objects.
	 */
	public static List<SourceResult> createSourceResults(Map<String, ClassResult> results) {
		List<SourceResult> sourceResults = new ArrayList<SourceResult>();
		sourceResults.add(new SourceResult(results));
		sourceResults.add(new TestsResult(results));
		// Create the project results obj. using the loc of tests for "ratio taille code / taille tests"
		sourceResults.add(new ProjectResult(results, sourceResults.get(1).getLoc()));

		return sourceResults;
	}

	public static void createTestsClassResultFilteredCsv(Map<String, ClassResult> results, String filePath) {
		createCSV(
			results.values()
			.stream()
			.filter(t -> (
				t.numberOfTestMethods() > 0 
				&& t.getCommentsDensity() > 0.6
				&& (double)t.getCkClassResult().getRfc() <  (double)(t.numberOfTestMethods() * (double)(2 * (double)(1 + t.getCommentsDensity())))
				&& t.getCkClassResult().getWmc() <= t.numberOfTestMethods()
				))
			.map(t -> (Result)t)
			.toList(), filePath + "/TestClassResults.csv");
	}

	/**
     * The function creates a CSV file and writes the contents of a list of TestFile objects to it.
     * 
     * @param testFiles A list of TestFile objects.
     * @return The method is returning a File object.
     */
    public static File createCSV(List<Result> result, String csvPath) {
        File outputFile = new File(csvPath);

        try {
            FileWriter fileWriter;

            outputFile.createNewFile();
            fileWriter = new FileWriter(outputFile);

			fileWriter.append(result.get(0).getCsvHeader() + "\n");


            for (Result testFile : result) {
                fileWriter.append(testFile.toCsv() + "\n");
            }

            fileWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return outputFile;
    }

    /**
	 * The function `createCKClassResultsMap` takes in command line arguments, calculates CK metrics
	 * for a given project, stores the metrics in a map, and optionally prints the results.
	 * 
	 * @param runnerArgs An array of command line arguments passed to the program.
	 * @param runnerPrintResults A boolean flag indicating whether to print the results or not. If set
	 * to true, the results will be printed using the `runnerPrintResults` method. If set to false, the
	 * results will not be printed.
	 * @return The method is returning a Map<String, CKClassResult> object.
	 */
	private static Map<String, CKClassResult> createCKClassResultsMap(String[] runnerArgs, boolean runnerPrintResults) {
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

    /**
	 * The function takes a map of CKClassResult objects, an output directory, and a boolean flag as
	 * parameters, and writes the metrics values of each component in the map to separate CSV files in
	 * the specified output directory.
	 * 
	 * @param results A map containing the results of the code analysis. The keys are the names of the
	 * classes, and the values are CKClassResult objects that contain the metrics for each class.
	 * @param outputDir The output directory where the CSV files will be written to.
	 * @param variablesAndFields A boolean value indicating whether to include variables and fields in
	 * the output files. If set to true, the metrics for variables and fields will be included in the
	 * "variable.csv" and "field.csv" files respectively. If set to false, these metrics will not be
	 * included.
	 */
	private static void runnerPrintResults(Map<String, CKClassResult> results, String outputDir, boolean variablesAndFields) throws IOException {
        ResultWriter writer = new ResultWriter(outputDir + "class.csv", outputDir + "method.csv", outputDir + "variable.csv", outputDir + "field.csv", variablesAndFields);

        // // Write the metrics value of each component in the csv files
		for(Map.Entry<String, CKClassResult> entry : results.entrySet()){
			writer.printResult(entry.getValue());
		}
		
		// writer.flushAndClose();
    }

	/**
	 * The function calculates and prints the execution time of a code block in seconds.
	 * 
	 * @param startTime The startTime parameter is the starting time of the execution of a certain code
	 * block or method. It is typically obtained using the System.currentTimeMillis() method.
	 */
	private static void debugExecTime(long startTime) {
		long totTime = System.currentTimeMillis() - startTime;

		String execTime = String.format("%d secondes",
			TimeUnit.MILLISECONDS.toSeconds(totTime));

		System.out.println(totTime);
		System.out.println(execTime);
	}
}