package TPS.TP2;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.nio.file.Path;

public class GitFinder {
    private static final String GIT_REPO_PATH = "../../jfreechart/";
    private static final String GIT_CMD_LAST_COMMIT = "git log -1 --pretty=\"format:%ct\" --";
    private static final String GIT_CMD_COMMITS = "git log -- grep ^commit | wc -l --";

    public GitFinder() {
    }

    private static String toGitRelativePath(String filePath) {
        File gitRepo = new File(GIT_REPO_PATH).getAbsoluteFile();
        File file = new File(filePath).getAbsoluteFile();
        return gitRepo.toPath().relativize(file.toPath()).toString().replace('\\', '/');
    }

    public static String gitCmd(String filePath) {
        try {
            String relativePath = toGitRelativePath(filePath);
            String command = GIT_CMD_LAST_COMMIT + " " + relativePath;

            ProcessBuilder builder = new ProcessBuilder(command.split(" "))
                .directory(new File(GIT_REPO_PATH))
                .redirectErrorStream(true);
            
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = r.readLine();

            Date date = new Date(Long.valueOf(line) * 1000);

            SimpleDateFormat sdf = new SimpleDateFormat("MMMM/d/yyyy/h:mm", Locale.ENGLISH);
		    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            
            return sdf.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int gitFileCommits(String filePath) {
        try {
            String relativePath = toGitRelativePath(filePath);
            String command = "git log " + relativePath;
    
            ProcessBuilder builder = new ProcessBuilder(command.split(" "))
                .directory(new File(GIT_REPO_PATH))
                .redirectErrorStream(true);
            
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
    
            String line;
            int count = 0;
            while ((line = r.readLine()) != null) {
                if (line.startsWith("commit")) {
                    count++;
                }
            }
    
            return count;
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return 0;
    }
    
}
