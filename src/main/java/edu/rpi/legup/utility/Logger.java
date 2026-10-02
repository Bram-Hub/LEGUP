package edu.rpi.legup.utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

public class Logger {

    private static final String LOG_PATTERN =
            "[%-5level] %d{yyyy-MM-dd HH:mm:ss.SSS} [%t] %c{1} - %msg%n";

    public static void initLogger() {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();
        if (config.getAppender("fileLogger") != null) {
            System.setProperty("sun.java2d.noddraw", Boolean.TRUE.toString());
            return;
        }

        ConsoleAppender consoleAppender = config.getAppender("console");
        String logPattern = LOG_PATTERN;
        if (consoleAppender != null && consoleAppender.getLayout() instanceof PatternLayout) {
            logPattern = ((PatternLayout) consoleAppender.getLayout()).getConversionPattern();
        }
        TimeBasedTriggeringPolicy triggeringPolicy =
                TimeBasedTriggeringPolicy.newBuilder().withInterval(1).withModulate(true).build();
        PatternLayout patternLayout = PatternLayout.newBuilder().withPattern(logPattern).build();

        try {
            Path legupHome = getLegupHome();
            Files.createDirectories(legupHome);
            RollingFileAppender rollingFileAppender =
                    RollingFileAppender.newBuilder()
                            .setName("fileLogger")
                            .withFileName(legupHome.resolve("legup.log").toString())
                            .withFilePattern(
                                    legupHome.resolve("legup-%d{yyyy-MM-dd}.log.gz").toString())
                            .withPolicy(triggeringPolicy)
                            .setLayout(patternLayout)
                            .setConfiguration(config)
                            .build();
            rollingFileAppender.start();
            config.addAppender(rollingFileAppender);
            LoggerConfig rootLogger = config.getRootLogger();
            rootLogger.addAppender(config.getAppender("fileLogger"), null, null);
            context.updateLoggers();
        } catch (IOException | RuntimeException e) {
            System.err.println("Unable to initialize LEGUP file logging: " + e.getMessage());
        }

        System.setProperty("sun.java2d.noddraw", Boolean.TRUE.toString());
    }

    private static Path getLegupHome() {
        return Paths.get(System.getProperty("user.home"), ".legup");
    }
}
