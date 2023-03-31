package edu.rpi.legup.utility;

import edu.rpi.legup.Legup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.File;

public class Logger {

    private static final String LEGUP_HOME = System.getProperty("user.home") + File.separator + ".legup" + File.separator;

    public static void initLogger() {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();
        ConsoleAppender consoleAppender = config.getAppender("console");
        PatternLayout consolePattern = (PatternLayout) consoleAppender.getLayout();
        TimeBasedTriggeringPolicy triggeringPolicy = TimeBasedTriggeringPolicy.newBuilder().withInterval(1).withModulate(true).build();
        PatternLayout patternLayout = PatternLayout.newBuilder().withPattern(consolePattern.getConversionPattern()).build();
        RollingFileAppender rollingFileAppender = RollingFileAppender.newBuilder()
                .setName("fileLogger")
                .withFileName(LEGUP_HOME + "legup.log")
                .withFilePattern(LEGUP_HOME + "legup-%d{yyyy-MM-dd}.log.gz")
                .withPolicy(triggeringPolicy)
                .setLayout(patternLayout)
                .setConfiguration(config)
                .build();
        rollingFileAppender.start();
        config.addAppender(rollingFileAppender);
        LoggerConfig rootLogger = config.getRootLogger();
        rootLogger.addAppender(config.getAppender("fileLogger"), null, null);
        context.updateLoggers();

        System.setProperty("sun.java2d.noddraw", Boolean.TRUE.toString());
    }

}
