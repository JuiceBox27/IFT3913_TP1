package TPS.TP2;

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

import TPS.results.ClassResult;
import TPS.results.ProjectResult;
import TPS.results.Result;
import TPS.results.SourceResult;
import TPS.results.TestsResult;

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

	public static void execute(String[] args) throws IOException {
		Map<String, CKClassResult> results = createCKClassResultsMap(args, false);
        Map<String, ClassResult> myResults = new HashMap<String, ClassResult>();

        results.forEach((k, v) -> {
			ClassResult cr = new ClassResult(v, "day/month/year", 0);
			myResults.put(k, cr);
			System.out.println(cr.toString());
		});
		
		List<SourceResult> sourceResults = new ArrayList<SourceResult>();
		sourceResults.add(new ProjectResult(myResults));
		sourceResults.add(new SourceResult(myResults));
		sourceResults.add(new TestsResult(myResults));
		
		System.out.println("+-+-+-+-+-+-+-+");
		
		createCSV(myResults.values().stream().map(t -> (Result)t).toList(), "./etude-jfreechart/TP2/ClassResults.csv");
		createCSV(sourceResults.stream().map(t -> (Result)t).toList(), "./etude-jfreechart/TP2/SourceResults.csv");
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

    private static void runnerPrintResults(Map<String, CKClassResult> results, String outputDir, boolean variablesAndFields) throws IOException {
        ResultWriter writer = new ResultWriter(outputDir + "class.csv", outputDir + "method.csv", outputDir + "variable.csv", outputDir + "field.csv", variablesAndFields);

        // // Write the metrics value of each component in the csv files
		for(Map.Entry<String, CKClassResult> entry : results.entrySet()){
			writer.printResult(entry.getValue());
		}
		
		// writer.flushAndClose();
    }

	private static void debugExecTime(long startTime) {
		long totTime = System.currentTimeMillis() - startTime;

		String execTime = String.format("%d min, %d sec",
			TimeUnit.MILLISECONDS.toMinutes(totTime),
			TimeUnit.MILLISECONDS.toSeconds(totTime));

		System.out.println(totTime);
		System.out.println(execTime);
	}
}