package TP2;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class GitFinder {
    private static final String GIT_CMD_LAST_COMMIT = "git log -1 --pretty=\"format:%ct\" --";
    private static final String GIT_CMD_COMMITS = "git log\s";

    private String gitRepoPath;

    public GitFinder(String gitRepoPath) {
        this.gitRepoPath = gitRepoPath;
    }

    public String gitCmd(String filePath) {
        try {
            String relativePath = toGitRelativePath(filePath);
            String command = GIT_CMD_LAST_COMMIT + "\s" + relativePath;

            ProcessBuilder builder = new ProcessBuilder(command.split("\s"))
                .directory(new File(gitRepoPath))
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

    public int gitFileCommits(String filePath) {
        try {
            String relativePath = toGitRelativePath(filePath);
            String command = GIT_CMD_COMMITS + relativePath;
    
            ProcessBuilder builder = new ProcessBuilder(command.split("\s"))
                .directory(new File(gitRepoPath))
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

    private String toGitRelativePath(String filePath) {
        File gitRepo = new File(gitRepoPath).getAbsoluteFile();
        File file = new File(filePath).getAbsoluteFile();
        return gitRepo.toPath().relativize(file.toPath()).toString().replace('\\', '/');
    }
    
}
