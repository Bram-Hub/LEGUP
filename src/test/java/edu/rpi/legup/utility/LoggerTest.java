package edu.rpi.legup.utility;

import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class LoggerTest {

    @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void initLoggerCreatesLegupHomeDirectory() throws Exception {
        String originalUserHome = System.getProperty("user.home");
        Path userHome = temporaryFolder.newFolder("home").toPath();
        System.setProperty("user.home", userHome.toString());

        try {
            Logger.initLogger();

            assertTrue(Files.isDirectory(userHome.resolve(".legup")));
        } finally {
            System.setProperty("user.home", originalUserHome);
        }
    }
}
