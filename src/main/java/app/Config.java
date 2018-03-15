package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Config
{
    private final static Logger LOGGER = Logger.getLogger(Config.class.getName());

    // TODO: Make this data structure more robust, rather than
    //	   depending on type erasure and casting
    private Hashtable<String, Hashtable<String, Object>> puzzles;

    /**
     * Constructor
     */
    public Config()
    {
        puzzles = new Hashtable<>();
    }

    /**
     * Constructor
     *
     * @param configFile location of configuration file to open for reading.
     */
    public Config(InputStream configFile)
    {
        super();
        loadConfig(configFile);
    }

    /**
     * Config Constructor - create the configuration info from the config.xml
     *
     * @param configFileName name of the config file
     */
    public Config(String configFileName)
    {
        super();
        loadConfig(getResource(configFileName));
    }

    /**
     * Gets the config file as an InputStream
     *
     * @param fileName name of the config file
     *
     * @return input stream for the file
     */
    private static InputStream getResource(String fileName)
    {
        InputStream inputStream;
        try
        {
            inputStream = new FileInputStream(new File(fileName));
        }
        catch(IOException e)
        {
            LOGGER.log(Level.SEVERE, e.toString());
            throw new RuntimeException(e);
        }
        return inputStream;
    }

    /**
     * Gets a Vector of Board names (Strings) for a specific puzzle
     *
     * @param puzzleName Puzzle NAME to look for
     *
     * @return Vector of Board names which are Strings
     */
    @SuppressWarnings("unchecked")
    public Vector<String> getBoardsForPuzzle(String puzzleName)
    {
        Vector<String> boards = (Vector<String>) puzzles.get(puzzleName).get("puzzlefiles");
        if(boards == null)
        {
            boards = new Vector<String>();
            boards.add(new String("Unknown Puzzle"));
        }
        return boards;
    }

    /**
     * Gets a list of all available Puzzle names
     *
     * @return Vector of Puzzle names which are Strings
     */
    public Vector<String> getPuzzleList()
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
        Hashtable<String, Object> tmp = puzzles.get(puzzleName);
        if(tmp == null)
        {
            return null;
        }
        return (String) tmp.get("class");
    }

    /**
     * Loads the Config object from the config XML file
     *
     * @param configFile Location of the config XML file
     */
    protected void loadConfig(InputStream configFile)
    {
        // System.out.println("Loading config file: "+configFile);

        Element configNode = null;

        try
        {
            // create the DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // create the Documentbuilder
            DocumentBuilder builder = factory.newDocumentBuilder();

            // obtain the document object from the XML document
            Document document = builder.parse(configFile);

            // get the root node
            configNode = document.getDocumentElement();

            if(configNode.getNodeName() != "legupconfig")
            {
                System.out.println("config is not formatted correctly!");
                System.out.println("ERROR: No legupconfig entry");
                return;
            }

            if(configNode.getElementsByTagName("puzzles").getLength() != 1)
            {
                System.out.println("config is not formatted correctly!");
                System.out.println("ERROR: No puzzles entry");
                return;
            }

            // Load the puzzles
            Hashtable<String, Hashtable<String, Object>> puzzles = new Hashtable<String, Hashtable<String, Object>>();
            NodeList puzzlesList = ((Element) (configNode.getElementsByTagName("puzzles").item(0))).getElementsByTagName("puzzle");
            for(int i = 0; i < puzzlesList.getLength(); i++)
            {
                Hashtable<String, Object> puzzle = new Hashtable<String, Object>();

                if(((Element) (puzzlesList.item(i))).getElementsByTagName("name").getLength() != 1)
                {
                    System.out.println("config is not formatted correctly!");
                    System.out.println("ERROR: Missing NAME entry");
                    return;
                }
                String name = ((Element) (((Element) (puzzlesList.item(i))).getElementsByTagName("name").item(0))).getFirstChild().getNodeValue();

                if(((Element) (puzzlesList.item(i))).getElementsByTagName("class").getLength() != 1)
                {
                    System.out.println("config is not formatted correctly!");
                    System.out.println("ERROR: Missing class entry");
                    return;
                }
                puzzle.put("class", ((Element) (((Element) (puzzlesList.item(i))).getElementsByTagName("class").item(0))).getFirstChild().getNodeValue());

                Vector<String> boards = new Vector<String>();

                // grab all puzzlefiles from directory
                File f = new File("puzzlefiles" + File.separator + name.toLowerCase() + File.separator);
                if(f.exists() && f.isDirectory())
                {
                    File[] files = f.listFiles();

                    for(int x = 0; x < files.length; ++x)
                    {
                        String path = files[x].getPath();

                        if(files[x].isFile() && path.toLowerCase().endsWith(".xml"))
                        {
                            boards.add(path);
                        }
                    }
                }

                puzzle.put("puzzlefiles", boards);

                puzzles.put(name, puzzle);
            }

            this.puzzles = puzzles;

        }
        catch(ParserConfigurationException parserError)
        {
            System.out.println("File (" + configFile + ") is not formatted correctly!");
            System.out.println("ERROR: Parser Error creating the Document Builder");
            return;
        }
        catch(IOException fileException)
        {
            System.out.println("File (" + configFile + ") is not formatted correctly!");
            System.out.println("ERROR: File IO Error");
            return;
        }
        catch(SAXException parseException)
        {
            System.out.println("ERROR: Parsing Error (File=" + configFile + ")");
            System.out.println(parseException.toString());
            return;
        }
    }

    public boolean checkPermission(int a)
    {
        return true;
    }

    //TODO:implement
    public boolean allowDefaultApplication()
    {
        return true;
    }
}

