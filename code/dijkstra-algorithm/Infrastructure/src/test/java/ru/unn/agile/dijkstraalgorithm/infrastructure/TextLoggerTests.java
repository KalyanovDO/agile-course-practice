package ru.unn.agile.dijkstraalgorithm.infrastructure;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.unn.agile.dijkstraalgorithm.infrastructure.RegExprMatcher.matchesPattern;

public class TextLoggerTests {
    private static final String FILENAME = "./TxtLogger_Tests-lab3-dijkstra-algorithm.log";
    private TextLogger textLogger;

    @Before
    public void setUp() {
        textLogger = new TextLogger(FILENAME);
    }

    @Test
    public void canCreateLoggerWithFileName() {
        assertNotNull(textLogger);
    }

    @Test
    public void canCreateLogFileOnDisk() {
        try {
            new BufferedReader(new FileReader(FILENAME));
        } catch (FileNotFoundException e) {
            fail("File " + FILENAME + " wasn't found.");
        }
    }

    @Test
    public void canWriteLogMessage() {
        String checkMessage = "Test message";

        textLogger.log(checkMessage);

        String message = textLogger.getLog().get(0);
        assertThat(message, matchesPattern(".*" + checkMessage + "$"));
    }

    @Test
    public void canWriteSeveralLogMessages() {
        String[] messageList = {"Test message 1st", "Test message 2nd"};

        textLogger.log(messageList[0]);
        textLogger.log(messageList[1]);

        List<String> actualMessages = textLogger.getLog();
        for (int i = 0; i < actualMessages.size(); i++) {
            assertThat(actualMessages.get(i), matchesPattern(".*" + messageList[i] + "$"));
        }
    }

    @Test
    public void doesLogContainDateAndTime() {
        String checkMessage = "Test message";

        textLogger.log(checkMessage);

        String message = textLogger.getLog().get(0);
        assertThat(message, matchesPattern("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} > .*"));
    }
}
