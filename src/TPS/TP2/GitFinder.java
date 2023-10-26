package TPS.TP2;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpResponse.BodyHandlers;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class GitFinder {
    private static final String GIT_CMD_LAST_COMMIT = "git log -1 --pretty=\"format:%ct\"";
    private static final String GIT_CMD_COMMITS = "git log filePath | grep ^commit | wc -l";

    String urlString;

    HttpClient client;
    HttpRequest rootRequest;

    public GitFinder(String urlString) {
        this.urlString = urlString;
        
        client = createClient();
        
        rootRequest = createRootRequest(urlString);
    }

    public String gitCmd(String filePath) {
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

    public int gitFileCommits(String filePath) {
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

    public void sendRequest(String urlSubPath) {
        try {
            HttpRequest request = HttpRequest.newBuilder(rootRequest, (h, v) -> true).build();

            client.sendAsync(request, BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static HttpClient createClient() {
        return HttpClient.newBuilder()
                .version(Version.HTTP_2)
                .followRedirects(Redirect.NORMAL)
                .build();
    }

    private static HttpRequest createRootRequest(String urlString) {
        try {
            return HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .GET()
                // .header("Host", "github.com")
                .timeout(Duration.ofSeconds(5))
                .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }
}
