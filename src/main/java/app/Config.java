package app;

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import model.Puzzle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import save.ExportFileException;

public class Config
{
    private final static Logger LOGGER = Logger.getLogger(Config.class.getName());

    private Hashtable<String, String> puzzles;
    private static final String CONFIG_LOCATION = "resources/legup/config";

    /**
     * Constructor
     */
    public Config() throws InvalidConfigException
    {
        puzzles = new Hashtable<>();
        loadConfig(this.getClass().getResourceAsStream("/legup/config"));
    }

    /**
     * Gets a list of all available Puzzle names
     *
     * @return Vector of Puzzle names which are Strings
     */
    public Vector<String> getPuzzleNames()
    {
        Vector<String> puzzleList = new Vector<String>();
        for(Enumeration<String> e = puzzles.keys(); e.hasMoreElements(); )
        {
            puzzleList.add(e.nextElement());
        }
        return puzzleList;
    }

    /**
     * Gets a puzzle class for a Puzzle NAME
     *
     * @param puzzleName Puzzle NAME to get a Class Name of
     *
     * @return Class Name for the Puzzle Name
     */
    public String getPuzzleClassForName(String puzzleName)
    {
        if(puzzles.containsKey(puzzleName))
        {
            return puzzles.get(puzzleName);
        }
        return null;
    }

    /**
     * Loads the Config object from the config xml file
     *
     * @param stream file stream for the config xml file
     */
    private void loadConfig(InputStream stream) throws InvalidConfigException
    {
        try
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(stream);
            Element configNode = document.getDocumentElement();

            if(!configNode.getNodeName().equals("Legup"))
            {
                throw new InvalidConfigException("Config is not formatted correctly");
            }

            Element puzzleList = (Element)configNode.getElementsByTagName("puzzles").item(0);
            NodeList puzzleNodes = puzzleList.getElementsByTagName("puzzle");

            for(int i = 0; i < puzzleNodes.getLength(); i++)
            {
                Element puzzle = (Element)puzzleNodes.item(i);
                String name = puzzle.getAttribute("name");
                String className = puzzle.getAttribute("qualifiedClassName");
                puzzles.put(name, className);
            }
        }
        catch(ParserConfigurationException | SAXException | IOException e)
        {
            throw new InvalidConfigException(e.getMessage());
        }
    }
}

