package TPS.TP2;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class GitFinder {
    private static final String GIT_CMD_LAST_COMMIT = "git log -1 --pretty=\"format:%ct\"";
    private static final String GIT_CMD_COMMITS = "git log filePath | grep ^commit | wc -l";

    public GitFinder() {
    }

    public static String gitCmd(String filePath) {
        try {
            ProcessBuilder builder = new ProcessBuilder((GIT_CMD_LAST_COMMIT + "\s" + filePath).split("\s"))
                .directory(new File("../../jfreechart/"))
                .redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            line = r.readLine();

            Date date = new Date(Long.valueOf(line) * 1000);

            SimpleDateFormat sdf = new SimpleDateFormat("MMMM/d/yyyy/h:mm", Locale.ENGLISH);
		    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            String formattedDate = sdf.format(date);

            return formattedDate;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int gitFileCommits(String filePath) {
        try {
            String[] arguments = GIT_CMD_COMMITS.split("\s");
            arguments[2] = filePath.split("..\\..")[1];
            ProcessBuilder builder = new ProcessBuilder(arguments)
                .directory(new File("../../jfreechart/"))
                .redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            int line;
            line = r.read();

            System.out.println(arguments[2]);
            System.out.println(filePath);
            System.out.println(line);

            return line;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
