import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 Skeleton of a ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.
 */
public class ContinuousIntegrationServer extends AbstractHandler
{
    @Override
    public void handle(
            String target,
            Request baseRequest,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(javax.servlet.http.HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        // see https://stackoverflow.com/questions/8100634/get-the-post-request-body-from-httpservletrequest
        String reqString = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        Payload payload = Payload.parse(reqString);

        File dir = Files.createTempDirectory("temp").toFile();
        try {
            // Clones repo
            File repo = new RepoSnapshot(payload).cloneFiles(dir);
            // Executes gradle build
            Report buildReport = GradleHandler.build(repo);
            // Sends mail
            Mailserver mailserver = new Mailserver();
            mailserver.useGmailSMTP();
            SendMail sendMail = new SendMail();
            sendMail.sendMail(buildReport, payload, mailserver, payload.getPusherEmail(), "Hello");

        } catch (Exception e) {
            System.out.println("Failed to process repo: " + e.getMessage());
        }

        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        // 2nd compile the code

        response.getWriter().println("CI job done");
    }

    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        DocumentBuilder db = new DocumentBuilder();
        db.writeDoc("1", "2", "3", "4", "5");
        Server server = new Server(8080);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();
        server.join();
    }
}