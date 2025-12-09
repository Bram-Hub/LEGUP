package edu.rpi.legup.app;

import java.io.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The {@code Config} class manages the configuration for puzzles by loading configuration data from
 * an XML file. It provides methods to access puzzle class names, display names, and their file
 * creation statuses
 */
public class Config {
    private static final Logger LOGGER = LogManager.getLogger(Config.class.getName());

    private Map<String, String> puzzles;
    private Map<String, Boolean> fileCreationDisabledStatuses;
    private static final String CONFIG_LOCATION = "edu/rpi/legup/legup/config";

    /**
     * Config Constructor for logic puzzles
     *
     * @throws InvalidConfigException if configuration is invalid
     */
    public Config() throws InvalidConfigException {
        this.puzzles = new Hashtable<>();
        this.fileCreationDisabledStatuses = new Hashtable<>();
        loadConfig(this.getClass().getClassLoader().getResourceAsStream(CONFIG_LOCATION));
    }

    /**
     * Gets a list of all available Puzzle names
     *
     * @return Vector of Puzzle names which are Strings
     */
    public List<String> getPuzzleClassNames() {
        return new LinkedList<>(puzzles.keySet());
    }

    /**
     * Returns a list of the names of the puzzles which can have puzzles created and edited within
     * the proof editor.
     *
     * @return the aforementioned list of Strings
     */
    public List<String> getFileCreationEnabledPuzzles() {
        LinkedList<String> puzzles = new LinkedList<String>();
        for (String puzzle : this.getPuzzleClassNames()) {
            if (!this.fileCreationDisabledStatuses.get(puzzle)) {
                puzzles.add(puzzle);
            }
        }
        return puzzles;
    }

    /**
     * Converts the class name of the puzzles to their display names. Some examples of the
     * conversion: convertClassNameToDisplayName("TreeTent") will return "Tree Tent"
     * convertClassNameToDisplayName("Nurikabe") will return "Nurikabe"
     *
     * @param className the name of the class
     * @return displayName the name of the puzzle
     */
    public static String convertClassNameToDisplayName(String className) {
        String displayName = "";
        for (int i = 0; i < className.length(); i++) {
            if (Character.isUpperCase(className.charAt(i)) && i != 0) {
                displayName += " ";
            }
            displayName += className.charAt(i);
        }
        return displayName;
    }

    /**
     * Converts the display name of the puzzle back to its corresponding class name. For example:
     * convertDisplayNameToClassName("Tree Tent") returns "TreeTent"
     *
     * @param displayName the display name of the puzzle
     * @return the class name of the puzzle as a String
     */
    public static String convertDisplayNameToClassName(String displayName) {
        String className = "";
        for (int i = 0; i < displayName.length(); i++) {
            if (displayName.charAt(i) != ' ') {
                className += displayName.charAt(i);
            }
        }
        return className;
    }

    /**
     * Gets a list of all available puzzle display names
     *
     * @return a List of puzzle display names as Strings
     */
    public List<String> getPuzzleNames() {
        List<String> names = new LinkedList<String>();
        for (String puzzle : this.getPuzzleClassNames()) {
            names.add(Config.convertClassNameToDisplayName(puzzle));
        }
        return names;
    }

    /**
     * Returns a list of the display names of the puzzles that can have files created and edited
     * within the proof editor
     *
     * @return a List of puzzle display names as Strings with file creation enabled
     */
    public List<String> getFileCreationEnabledPuzzleNames() {
        List<String> names = new LinkedList<String>();
        for (String puzzle : this.getFileCreationEnabledPuzzles()) {
            names.add(Config.convertClassNameToDisplayName(puzzle));
        }
        return names;
    }

    /**
     * Gets a {@link edu.rpi.legup.model.Puzzle} class for a puzzle name
     *
     * @param puzzleName puzzle name of the class
     * @return class name for the puzzle name
     */
    public String getPuzzleClassForName(String puzzleName) {
        if (puzzles.containsKey(puzzleName)) {
            return puzzles.get(puzzleName);
        }
        return null;
    }

    /**
     * Loads the config object from the config xml file
     *
     * @param stream file stream for the config xml file
     */
    private void loadConfig(InputStream stream) throws InvalidConfigException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(stream);
            Element configNode = document.getDocumentElement();

            if (!configNode.getNodeName().equalsIgnoreCase("Legup")) {
                throw new InvalidConfigException("Config is not formatted correctly");
            }

            Element puzzleList = (Element) configNode.getElementsByTagName("puzzles").item(0);
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug(puzzleList);
            }
            NodeList puzzleNodes = puzzleList.getElementsByTagName("puzzle");

            for (int i = 0; i < puzzleNodes.getLength(); i++) {
                Element puzzle = (Element) puzzleNodes.item(i);
                String name = puzzle.getAttribute("name");
                String className = puzzle.getAttribute("qualifiedClassName");
                boolean status =
                        Boolean.parseBoolean(
                                puzzle.getAttribute("fileCreationDisabled").toLowerCase());
                if(LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Class Name: {}", className);
                }
                this.puzzles.put(name, className);
                this.fileCreationDisabledStatuses.put(name, Boolean.valueOf(status));
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new InvalidConfigException(e.getMessage());
        }
    }
}
