package edu.rpi.legup.ui;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.app.LegupPreferences;
import edu.rpi.legup.controller.CursorController;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.io.FileWriter;
import java.net.URI;
import java.net.URL;
import java.util.Objects;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The {@code HomePanel} class represents the home panel of the LEGUP application. This panel
 * provides buttons for functionalities of opening the proof editor, opening the puzzle editor, and
 * performing batch grading. It also includes a menu bar with options for preferences.
 */
public class HomePanel extends LegupPanel {
    private static final Logger LOGGER = LogManager.getLogger(HomePanel.class.getName());
    private LegupUI legupUI;
    private JFrame frame;
    private JButton[] buttons;
    private JLabel[] text;
    private JMenuBar menuBar;
    private JFileChooser folderBrowser;

    private final int buttonSize = 100;

    /** Initialize the proof solver to an empty panel with no puzzle */
    private ActionListener openProofListener =
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    legupUI.getProofEditor().loadPuzzle("", null);
                }
            };

    /**
     * Constructs a {@code HomePanel} with the specified {@code JFrame} and {@code LegupUI}.
     *
     * @param frame the main application frame
     * @param legupUI the LEGUP user interface
     */
    public HomePanel(JFrame frame, LegupUI legupUI) {
        this.legupUI = legupUI;
        this.frame = frame;
        setLayout(new GridLayout(1, 2));
        setPreferredSize(new Dimension(440, 250));
        initText();
        initButtons();
    }

    /**
     * Creates and returns the menu bar for this panel
     *
     * @return the menu bar
     */
    public JMenuBar getMenuBar() {
        this.menuBar = new JMenuBar();
        JMenu settings = new JMenu("Settings");
        menuBar.add(settings);
        JMenuItem preferences = new JMenuItem("Preferences");
        preferences.addActionListener(
                a -> {
                    PreferencesDialog preferencesDialog = new PreferencesDialog(this.frame);
                    System.out.println("Preferences clicked");
                });
        settings.addSeparator();
        settings.add(preferences);

        JMenuItem contribute = new JMenuItem("Contribute to Legup");
        contribute.addActionListener(
                l -> {
                    try {
                        java.awt.Desktop.getDesktop()
                                .browse(URI.create("https://github.com/Bram-Hub/Legup"));
                    } catch (IOException e) {
                        LOGGER.error("Can't open web page");
                    }
                });
        settings.add(contribute);

        return this.menuBar;
    }

    /** Makes the panel visible and sets the menu bar of the frame */
    @Override
    public void makeVisible() {
        render();
        frame.setJMenuBar(this.getMenuBar());
    }

    /**
     * Resizes the provided icon to the specified width and height
     *
     * @param icon the icon to resize
     * @param width the target width
     * @param height the target height
     * @return the resized icon
     */
    private static ImageIcon resizeButtonIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    /** Initializes the buttons for this panel */
    private void initButtons() {
        this.buttons = new JButton[3];

        this.buttons[0] =
                new JButton("Puzzle Solver") {
                    {
                        setSize(buttonSize, buttonSize);
                        setMaximumSize(getSize());
                    }
                };

        URL button0IconLocation =
                ClassLoader.getSystemClassLoader()
                        .getResource("edu/rpi/legup/images/Legup/homepanel/proof_file.png");
        ImageIcon button0Icon = new ImageIcon(button0IconLocation);
        this.buttons[0].setFocusPainted(false);
        this.buttons[0].setIcon(resizeButtonIcon(button0Icon, this.buttonSize, this.buttonSize));
        this.buttons[0].setHorizontalTextPosition(AbstractButton.CENTER);
        this.buttons[0].setVerticalTextPosition(AbstractButton.BOTTOM);
        this.buttons[0].addActionListener(CursorController.createListener(this, openProofListener));

        this.buttons[1] =
                new JButton("Puzzle Editor") {
                    {
                        setSize(buttonSize, buttonSize);
                        setMaximumSize(getSize());
                    }
                };
        URL button1IconLocation =
                ClassLoader.getSystemClassLoader()
                        .getResource("edu/rpi/legup/images/Legup/homepanel/new_puzzle_file.png");
        ImageIcon button1Icon = new ImageIcon(button1IconLocation);
        this.buttons[1].setFocusPainted(false);
        this.buttons[1].setIcon(resizeButtonIcon(button1Icon, this.buttonSize, this.buttonSize));
        this.buttons[1].setHorizontalTextPosition(AbstractButton.CENTER);
        this.buttons[1].setVerticalTextPosition(AbstractButton.BOTTOM);
        this.buttons[1].addActionListener(l -> this.openPuzzleEditorDialog());

        for (int i = 0; i < this.buttons.length - 1; i++) { // -1 to avoid the batch grader button
            this.buttons[i].setBounds(200, 200, 700, 700);
        }
        this.buttons[2] = new JButton("Batch Grader");
        this.buttons[2].setFocusPainted(false);
        this.buttons[2].setHorizontalTextPosition(AbstractButton.CENTER);
        this.buttons[2].setVerticalTextPosition(AbstractButton.BOTTOM);

        this.buttons[2].addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            use_xml_to_check();
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                        System.out.println("finished checking the folder");
                    }
                });
    }

    /**
     * Opens a folder chooser dialog and grades puzzles in the selected folder. The results are
     * written to a CSV file.
     */
    public void checkFolder() {
        GameBoardFacade facade = GameBoardFacade.getInstance();
        /*
         * Select dir to grade; recursively grade sub-dirs using traverseDir()
         * Selected dir must have sub-dirs for each student:
         * GradeThis
         *    |
         *    | -> Student 1
         *    |       |
         *    |       | -> Proofs
         */

        JFileChooser folderBrowser = new JFileChooser();

        folderBrowser.setCurrentDirectory(new File(LegupPreferences.WORK_DIRECTORY));
        folderBrowser.setDialogTitle("Select Directory");
        folderBrowser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        folderBrowser.setAcceptAllFileFilterUsed(false);
        folderBrowser.showOpenDialog(this);
        folderBrowser.setVisible(true);
        File folder = folderBrowser.getSelectedFile();

        File resultFile = new File(folder.getAbsolutePath() + File.separator + "result.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile))) {
            writer.append("Name");
            writer.append(",");
            writer.append("File Name");
            writer.append(",");
            writer.append("Solved?");
            writer.append("\n");

            for (final File folderEntry : folder.listFiles(File::isDirectory)) {
                writer.append(folderEntry.getName());
                writer.append(",");
                int count1 = 0;
                for (final File fileEntry : folderEntry.listFiles()) {
                    if (fileEntry.getName().charAt(0) == '.') {
                        continue;
                    }
                    count1++;
                    if (count1 > 1) {
                        writer.append(folderEntry.getName());
                        writer.append(",");
                    }
                    writer.append(fileEntry.getName());
                    writer.append(",");
                    String fileName =
                            folderEntry.getAbsolutePath() + File.separator + fileEntry.getName();
                    System.out.println("This is path " + fileName);
                    File puzzleFile = new File(fileName);
                    if (puzzleFile != null && puzzleFile.exists()) {
                        try {
                            legupUI.displayPanel(1);
                            legupUI.getProofEditor();
                            GameBoardFacade.getInstance().loadPuzzle(fileName);
                            String puzzleName =
                                    GameBoardFacade.getInstance().getPuzzleModule().getName();
                            legupUI.setTitle(puzzleName + " - " + puzzleFile.getName());
                            facade = GameBoardFacade.getInstance();
                            Puzzle puzzle = facade.getPuzzleModule();
                            if (puzzle.isPuzzleComplete()) {
                                writer.append("Solved");
                                System.out.println(fileEntry.getName() + "  solved");
                            } else {
                                writer.append("Not Solved");
                                System.out.println(fileEntry.getName() + "  not solved");
                            }
                            writer.append("\n");
                        } catch (InvalidFileFormatException e) {
                            LOGGER.error(e.getMessage());
                        }
                    }
                }
                if (count1 == 0) {
                    writer.append("No file");
                    writer.append("\n");
                }
            }
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
            this.buttons[2].addActionListener((ActionEvent e) -> use_xml_to_check());
        }
    }

    /**
     * Processes XML files within a selected directory and generates a CSV report on their "solved?"
     * status. The method allows the user to select a directory, and evaluates each XML file for a
     * "solved?" status. Results are saved in a "result.csv" file.
     *
     * @effect Selects a directory, processes each XML file to check for "solved?" status, and
     *     writes results to "result.csv". Opens the CSV file upon completion.
     */
    private void use_xml_to_check() {
        /* Select a folder, go through each .xml file in the subfolders, look for "isSolved" flag */
        JFileChooser folderBrowser = new JFileChooser();
        folderBrowser.setCurrentDirectory(new File(LegupPreferences.WORK_DIRECTORY));
        folderBrowser.setDialogTitle("Select Directory");
        folderBrowser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        folderBrowser.setAcceptAllFileFilterUsed(false);
        folderBrowser.setSelectedFile(null);
        folderBrowser.showOpenDialog(this);
        folderBrowser.setVisible(true);
        File folder = folderBrowser.getSelectedFile();

        File resultFile = new File(folder.getAbsolutePath() + File.separator + "result.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile))) {
            writer.append("Name,File Name,Puzzle Type,Solved?,Last Saved\n");
            // Go through student folders, recurse for inner folders
            for (final File folderEntry :
                    Objects.requireNonNull(folder.listFiles(File::isDirectory))) {
                String path = folderEntry.getName();
                // use this helper function to write to the .csv file
                recursive_parser(folderEntry, writer, path, path);
            }
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
        if (resultFile.exists()) {
            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(resultFile);
            } catch (IOException ex) {
                LOGGER.error(ex.getMessage());
            }
        }
        JOptionPane.showMessageDialog(null, "Batch grading complete.");
    }

    /**
     * @param file - the input file
     * @return true if it is a .xml file, else return false
     */
    public boolean isxmlfile(File file) {
        boolean flag = true;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.parse(file);
            flag = true;
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * @param folder - the input folder
     * @param writer - write to .csv
     * @param path - the current path
     * @param name - student's name (the first subfolders of the main folder)
     * @throws IOException
     */
    private void recursive_parser(File folder, BufferedWriter writer, String path, String name)
            throws IOException {
        // Empty folder
        if (Objects.requireNonNull(folder.listFiles()).length == 0) {
            writer.append(path).append(",Empty folder,Ungradeable\n");
            return;
        }
        // Go through every other file in the folder
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser saxParser = spf.newSAXParser();
            for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
                if (fileEntry.getName().equals("result.csv")) {
                    continue;
                }
                // Recurse if it is a subfolder
                if (fileEntry.isDirectory()) {
                    recursive_parser(fileEntry, writer, path + "/" + fileEntry.getName(), name);
                    continue;
                }
                // Set path name
                String fName = fileEntry.getName();
                String fPath = fileEntry.getAbsolutePath();
                if (fileEntry.getName().charAt(0) == '.') {
                    continue;
                }
                // write data
                writer.write(name);
                writer.write(",");
                writer.write(fName);
                writer.write(",");
                path = folder.getAbsolutePath() + File.separator + fileEntry.getName();
                System.out.println(path);
                if (isxmlfile(fileEntry)) {
                    saxParser.parse(
                            path,
                            new DefaultHandler() {
                                @Override
                                public void startDocument() throws SAXException {}

                                boolean solvedFlagExists = false;
                                boolean puzzleTypeExists = false;

                                @Override
                                public void startElement(
                                        String uri,
                                        String localName,
                                        String qName,
                                        Attributes attributes)
                                        throws SAXException {
                                    // append file type to the writer
                                    // return if not designated puzzle ID
                                    if (qName.equals("puzzle")
                                            && attributes.getQName(0) == "name"
                                            && !puzzleTypeExists) {
                                        try {
                                            writer.write(attributes.getValue(0));
                                            writer.write(",");
                                            puzzleTypeExists = true;
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                    // append the "solved?" status of the proof to the writer
                                    else if (qName.equals("solved") && !solvedFlagExists) {
                                        String isSolved = attributes.getValue(0);
                                        String lastSaved = attributes.getValue(1);

                                        int solvedHash;
                                        try {
                                            solvedHash = Integer.parseInt(isSolved);
                                        } catch (NumberFormatException e) {
                                            solvedHash = -1;
                                        }
                                        Boolean solvedState = PuzzleExporter.inverseHash(solvedHash, lastSaved);

                                        if (solvedState == null) {
                                            try {
                                                writer.write("Error");
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                        } else if (solvedState) {
                                            try {
                                                writer.write("Solved");
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                        } else {
                                            try {
                                                writer.write("Not Solved");
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }

                                        // append when is this proof last saved
                                        if (lastSaved != null) {
                                            try {
                                                writer.write(",");
                                                writer.write(lastSaved);
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                        solvedFlagExists = true;
                                    }
                                }

                                @Override
                                public void characters(char[] ch, int start, int length)
                                        throws SAXException {}

                                @Override
                                public void endElement(String uri, String localName, String qName)
                                        throws SAXException {}

                                @Override
                                public void endDocument() throws SAXException {
                                    if (!puzzleTypeExists) {
                                        try {
                                            writer.write("not a LEGUP puzzle!");
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    } else if (!solvedFlagExists) {
                                        try {
                                            writer.write("missing flag!");
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                            });
                }
                // If wrong file type, ungradeable
                else {
                    writer.write("not a \".xml\" file!");
                }
                writer.write("\n");
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Initializes the text labels for the user interface. Sets up labels for welcome message, led
     * by Bram, and version information.
     */
    private void initText() {
        // TODO: add version text after auto-changing version label is implemented. (text[2] =
        // version)
        this.text = new JLabel[2];

        JLabel welcome = new JLabel("Welcome to LEGUP");
        welcome.setFont(new Font("Roboto", Font.BOLD, 23));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel credits = new JLabel("A project by Dr. Bram van Heuveln");
        credits.setFont(new Font("Roboto", Font.PLAIN, 12));
        credits.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel version = new JLabel("Version 5.1.0"); // This should be autochanged in the future
        version.setFont(new Font("Roboto", Font.ITALIC, 10));
        version.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.text[0] = welcome;
        this.text[1] = credits;
    }

    /** Renders the user interface components */
    private void render() {
        this.removeAll();

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.legupUI.setTitle("LEGUP: A Better Way To Learn Formal Logic");

        JPanel buttons = new JPanel();
        buttons.add(Box.createRigidArea(new Dimension(5, 0)));
        buttons.add(this.buttons[0]);
        buttons.add(Box.createRigidArea(new Dimension(5, 0)));
        buttons.add(this.buttons[1]);
        buttons.add(Box.createRigidArea(new Dimension(5, 0)));
        JPanel batchGraderButton = new JPanel();
        batchGraderButton.add(this.buttons[2]);
        batchGraderButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.add(Box.createRigidArea(new Dimension(0, 5)));
        for (int i = 0; i < this.text.length; i++) {
            this.add(this.text[i]);
        }
        this.add(buttons);
        this.add(batchGraderButton);
        this.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    /**
     * Opens the puzzle editor dialog with no selected puzzle, leaving a blank panel
     *
     * @throws IllegalArgumentException if the configuration parameters are invalid (should never
     *     happen)
     */
    private void openPuzzleEditorDialog() {
        String game = "";
        int r = 0;
        int c = 0;

        try {
            this.openEditorWithNewPuzzle(game, r, c);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to open editor with new puzzle");
            e.printStackTrace(System.out);
        }
    }

    /**
     * Opens a dialog to select a directory, recursively processes the directory to grade puzzles,
     * and generates a CSV report of the grading results.
     */
    private void checkProofAll() {
        /*
         * Select dir to grade; recursively grade sub-dirs using traverseDir()
         * Selected dir must have sub-dirs for each student:
         * GradeThis
         *    |
         *    | -> Student 1
         *    |       |
         *    |       | -> Proofs
         */

        LegupPreferences preferences = LegupPreferences.getInstance();
        File preferredDirectory =
                new File(preferences.getUserPref(LegupPreferences.WORK_DIRECTORY));
        folderBrowser = new JFileChooser(preferredDirectory);

        folderBrowser.showOpenDialog(this);
        folderBrowser.setVisible(true);
        folderBrowser.setCurrentDirectory(new File(LegupPreferences.WORK_DIRECTORY));
        folderBrowser.setDialogTitle("Select Directory");
        folderBrowser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        folderBrowser.setAcceptAllFileFilterUsed(false);

        File folder = folderBrowser.getSelectedFile();

        // Write csv file (Path,File-Name,Puzzle-Type,Score,Solved?)
        File resultFile = new File(folder.getAbsolutePath() + File.separator + "result.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile))) {
            writer.append("Name,File Name,Puzzle Type,Score,Solved?\n");

            // Go through student folders
            for (final File folderEntry :
                    Objects.requireNonNull(folder.listFiles(File::isDirectory))) {
                // Write path
                String path = folderEntry.getName();
                traverseDir(folderEntry, writer, path);
            }
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
        JOptionPane.showMessageDialog(null, "Batch grading complete.");
    }

    /**
     * Recursively traverses directories to grade puzzles and writes results to a CSV file
     *
     * @param folder the folder to traverse
     * @param writer the BufferedWriter to write results to the CSV file
     * @param path the current path within the directory structure
     * @throws IOException if an I/O error occurs while writing to the CSV file
     */
    private void traverseDir(File folder, BufferedWriter writer, String path) throws IOException {
        // Recursively traverse directory
        GameBoardFacade facade = GameBoardFacade.getInstance();
        // Folder is empty
        if (Objects.requireNonNull(folder.listFiles()).length == 0) {
            writer.append(path).append(",Empty folder,Ungradeable\n");
            return;
        }
        // Travese directory, recurse if sub-directory found
        // If ungradeable, do not leave a score (0, 1)
        for (final File f : Objects.requireNonNull(folder.listFiles())) {
            // Recurse
            if (f.isDirectory()) {
                traverseDir(f, writer, path + "/" + f.getName());
                continue;
            }

            // Set path name
            writer.append(path).append(",");

            // Load puzzle, run checker
            // If wrong file type, ungradeable
            String fName = f.getName();
            String fPath = f.getAbsolutePath();
            File puzzleFile = new File(fPath);
            if (puzzleFile.exists()) {
                // Try to load file. If invalid, note in csv
                try {
                    // Load puzzle, run checker
                    GameBoardFacade.getInstance().loadPuzzle(fPath);
                    String puzzleName = GameBoardFacade.getInstance().getPuzzleModule().getName();
                    frame.setTitle(puzzleName + " - " + puzzleFile.getName());
                    facade = GameBoardFacade.getInstance();
                    Puzzle puzzle = facade.getPuzzleModule();

                    // Write data
                    writer.append(fName).append(",");
                    writer.append(puzzle.getName()).append(",");
                    if (puzzle.isPuzzleComplete()) {
                        writer.append("Solved\n");
                    } else {
                        writer.append("Unsolved\n");
                    }
                } catch (InvalidFileFormatException e) {
                    writer.append(fName).append("InvalidFile,Ungradeable\n");
                }
            } else {
                LOGGER.debug("Failed to run sim");
            }
        }
    }

    /**
     * Opens the puzzle editor for the specified puzzle with the specified dimensions
     *
     * @param game the name of the game
     * @param rows the number of rows in the puzzle
     * @param columns the number of columns in the puzzle
     * @throws IllegalArgumentException if the dimensions are invalid
     */
    public void openEditorWithNewPuzzle(String game, int rows, int columns)
            throws IllegalArgumentException {
        if (game.isEmpty()) {
            this.legupUI.displayPanel(2);
            this.legupUI.getPuzzleEditor().loadPuzzleFromHome(game, rows, columns);
        } else {
            // Validate the dimensions
            GameBoardFacade facade = GameBoardFacade.getInstance();
            boolean isValidDimensions = facade.validateDimensions(game, rows, columns);
            if (!isValidDimensions) {
                JOptionPane.showMessageDialog(
                        null,
                        "The dimensions you entered are invalid. Please double check \n"
                                + "the number of rows and columns and try again.",
                        "ERROR: Invalid Dimensions",
                        JOptionPane.ERROR_MESSAGE);
                throw new IllegalArgumentException("ERROR: Invalid dimensions given");
            }

            if (this.legupUI == null) {
                System.err.println("Error: legupUI is null in HomePanel");
                return;
            }

            // Set game type on the puzzle editor
            this.legupUI.displayPanel(2);
            this.legupUI.getPuzzleEditor().loadPuzzleFromHome(game, rows, columns);
        }
    }

    /**
     * Opens the puzzle editor for the specified puzzle with the given statements
     *
     * @param game a String containing the name of the game
     * @param statements an array of statements
     */
    public void openEditorWithNewPuzzle(String game, String[] statements) {
        // Validate the text input
        GameBoardFacade facade = GameBoardFacade.getInstance();
        boolean isValidTextInput = facade.validateTextInput(game, statements);
        if (!isValidTextInput) {
            JOptionPane.showMessageDialog(
                    null,
                    "The input you entered is invalid. Please double check \n"
                            + "your statements and try again.",
                    "ERROR: Invalid Text Input",
                    JOptionPane.ERROR_MESSAGE);
            throw new IllegalArgumentException("ERROR: Invalid dimensions given");
        }

        // Set game type on the puzzle editor
        this.legupUI.displayPanel(2);
        this.legupUI.getPuzzleEditor().loadPuzzleFromHome(game, statements);
    }
}
