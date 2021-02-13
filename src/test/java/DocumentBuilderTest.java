import org.junit.jupiter.api.Test;
import org.w3c.tidy.Tidy;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentBuilderTest {

    DocumentBuilder db = new DocumentBuilder();

    /**
     * Test for the DocumentBuilder class.
     * The first part of the test asserts that the returned HTML string from the method db.writeDoc contains
     * the commit hash for the commits in the list.
     *
     * The second part of the test uses Jtidy to parse the string containing the HTML and check for errors.
     * The line tidy.getParseErrors() == 0 will be true if getParseErrors() returns 0, meaning that no errors
     * where found when parsing the HTML string. If getParseErrors() returns any number higher than 0 the validation
     * has failed and there is at least one error in the HTML. Warnings are allowed and will not give any errors.
     */
    @Test
    public void testWriteDoc(){

        List<CommitStructure> commits = new ArrayList<>();
        commits.add(new CommitStructure("23424324432424", "11/02-20",
                "logloglogsfsssfsfsfsfsffs", true));
        commits.add(new CommitStructure("79979779977979", "11/02-20",
                "logloglogsfsssf645646465466sfsffs", true));

        //set system out to an outputstream so it does not print anything when
        //running tidy.parse(System.in, System.out)
        System.setOut(new PrintStream(new OutputStream() {
            public void write(int b) {}}));

        String html = db.writeDoc(commits);
        assertTrue(html.contains("23424324432424"));
        assertTrue(html.contains("79979779977979"));

        //https://stackoverflow.com/questions/26608365/how-to-validate-html-using-jtidy
        //http://jtidy.sourceforge.net/apidocs/index.html
        Tidy tidy = new Tidy();
        InputStream stream = new ByteArrayInputStream(html.getBytes());
        tidy.parse(stream, System.out); //parse the html string
        assertTrue(tidy.getParseErrors() == 0); //the number of errors that occurred in the most recent parse operation.
        assertTrue(tidy.getParseWarnings() == 0); //the number of warnings that occurred in the most recent parse operation.
    }

    /**
     * Test for the DocumentBuilder class.
     * The first part of the test asserts that the returned HTML string from the method db.writeBuildDetails contains
     * the build details from the commit passed to the method.
     *
     * The second part of the test uses Jtidy to parse the string containing the HTML and check for errors.
     * The line tidy.getParseErrors() == 0 will be true if getParseErrors() returns 0, meaning that no errors
     * where found when parsing the HTML string. If getParseErrors() returns any number higher than 0 the validation
     * has failed and there is at least one error in the HTML. Warnings are allowed and will not give any errors.
     */
    @Test
    public void testWriteBuilDetails(){

        CommitStructure commit = new CommitStructure("23424324432424", "11/02-20",
                "logloglogsfsssfsfsfsfsffs", true);


        String html = db.writeBuildDetails(commit);
        assertTrue(html.contains("logloglogsfsssfsfsfsfsffs"));

        //https://stackoverflow.com/questions/26608365/how-to-validate-html-using-jtidy
        //http://jtidy.sourceforge.net/apidocs/index.html
        Tidy tidy = new Tidy();
        InputStream stream = new ByteArrayInputStream(html.getBytes());
        tidy.parse(stream, System.out); //parse the html string
        assertTrue(tidy.getParseErrors() == 0); //the number of errors that occurred in the most recent parse operation.
        assertTrue(tidy.getParseWarnings() == 0); //the number of warnings that occurred in the most recent parse operation.

    }
}
