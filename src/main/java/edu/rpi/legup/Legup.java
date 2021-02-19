package edu.rpi.legup;

import edu.rpi.legup.app.Config;
import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.app.InvalidConfigException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;


public class Legup {
    static {
        String logPath = "";
		try {
			logPath = Paths.get(Legup.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toFile().getParent();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
        logPath += logPath.endsWith(File.separator) ? "" : File.separator;
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration config = context.getConfiguration();
        ConsoleAppender consoleAppender = config.getAppender("console");
        PatternLayout consolePattern = (PatternLayout) consoleAppender.getLayout();
        TimeBasedTriggeringPolicy triggeringPolicy = TimeBasedTriggeringPolicy.newBuilder().withInterval(1).withModulate(true).build();
        PatternLayout patternLayout = PatternLayout.newBuilder().withPattern(consolePattern.getConversionPattern()).build();
        RollingFileAppender rollingFileAppender = RollingFileAppender.newBuilder()
                .withName("fileLogger")
                .withFileName(logPath + "legup.log")
                .withFilePattern(logPath + "legup-%d{yyyy-MM-dd}.log.gz")
                .withPolicy(triggeringPolicy)
                .withLayout(patternLayout)
                .setConfiguration(config)
                .build();
        rollingFileAppender.start();
        config.addAppender(rollingFileAppender);
        LoggerConfig rootLogger = config.getRootLogger();
        rootLogger.addAppender(config.getAppender("fileLogger"), null, null);
        context.updateLoggers();

        System.setProperty("sun.java2d.noddraw", Boolean.TRUE.toString());
    }

    /**
     * Starts the Legup Program
     *
     * @param args arguments to Legup
     */
    public static void main(String[] args) {
        GameBoardFacade.getInstance();
        setConfig();
    }

    private static void setConfig() {
        Config config = null;
        try {
            config = new Config();
        } catch (InvalidConfigException e) {
            System.exit(1);
        }
        GameBoardFacade.getInstance().setConfig(config);
    }
}