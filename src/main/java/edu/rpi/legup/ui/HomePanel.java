package edu.rpi.legup.ui;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.app.LegupPreferences;
import edu.rpi.legup.controller.CursorController;
import edu.rpi.legup.model.Puzzle;
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

public class HomePanel extends LegupPanel {
    private static final Logger LOGGER = LogManager.getLogger(HomePanel.class.getName());
    private LegupUI legupUI;
    private JFrame frame;
    private JButton[] buttons;
    private JLabel[] text;
    private JMenuBar menuBar;
    private JFileChooser folderBrowser;

    private final int buttonSize = 100;

    private ActionListener openProofListener =
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Object[] items = legupUI.getProofEditor().promptPuzzle();
                    if (items == null) {
                        // The attempt to prompt a puzzle ended gracefully (cancel)
                        return;
                    }
                    String fileName = (String) items[0];
                    File puzzleFile = (File) items[1];
                    legupUI.getProofEditor().loadPuzzle(fileName, puzzleFile);
                }
            };

    private ActionListener openPuzzleListener =
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Object[] items = legupUI.getPuzzleEditor().promptPuzzle();
                    if (items == null) {
                        // The attempt to prompt a puzzle ended gracefully (cancel)
                        return;
                    }
                    String fileName = (String) items[0];
                    File puzzleFile = (File) items[1];
                    legupUI.getPuzzleEditor().loadPuzzle(fileName, puzzleFile);
                }
            };

    public HomePanel(FileDialog fileDialog, JFrame frame, LegupUI legupUI) {
        this.legupUI = legupUI;
        this.frame = frame;
        setLayout(new GridLayout(1, 2));
        setPreferredSize(new Dimension(440, 250));
        initText();
        initButtons();
    }

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

    @Override
    public void makeVisible() {
        render();
        frame.setJMenuBar(this.getMenuBar());
    }

    private static ImageIcon resizeButtonIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    private void initButtons() {
        this.buttons = new JButton[4];

        this.buttons[0] =
                new JButton("Solve Puzzle") {
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
                new JButton("Create Puzzle") {
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
        this.buttons[1].addActionListener(l -> this.openNewPuzzleDialog());

        this.buttons[2] =
                new JButton("Edit Existing Puzzle") {
                    {
                        setSize(buttonSize, buttonSize);
                        setMaximumSize(getSize());
                    }
                };
        URL button2IconLocation =
                ClassLoader.getSystemClassLoader()
                        .getResource("edu/rpi/legup/images/Legup/homepanel/puzzle_file.png");
        ImageIcon button2Icon = new ImageIcon(button2IconLocation);
        this.buttons[2].setFocusPainted(false);
        this.buttons[2].setIcon(resizeButtonIcon(button2Icon, this.buttonSize, this.buttonSize));
        this.buttons[2].setHorizontalTextPosition(AbstractButton.CENTER);
        this.buttons[2].setVerticalTextPosition(AbstractButton.BOTTOM);
        this.buttons[2].addActionListener(
                CursorController.createListener(this, openPuzzleListener)); // PLACEHOLDER

        this.buttons[2].setVisible(false);

        for (int i = 0; i < this.buttons.length - 1; i++) { // -1 to avoid the batch grader button
            this.buttons[i].setBounds(200, 200, 700, 700);
        }
        this.buttons[3] = new JButton("Batch Grader");
        this.buttons[3].setFocusPainted(false);
        this.buttons[3].setHorizontalTextPosition(AbstractButton.CENTER);
        this.buttons[3].setVerticalTextPosition(AbstractButton.BOTTOM);

        this.buttons[3].addActionListener(
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
            this.buttons[3].addActionListener((ActionEvent e) -> use_xml_to_check());
        }
    }

    /**
     * @effect batch grade using .xml parser - go through a collection of files and report their
     *     "solved?" status
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
                                        if (isSolved != null) {
                                            if (isSolved.equals("true")) {
                                                try {
                                                    writer.write("Solved");
                                                } catch (IOException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            } else if (isSolved.equals("false")) {
                                                try {
                                                    writer.write("Not Solved");
                                                } catch (IOException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            } else {
                                                try {
                                                    writer.write("Error");
                                                } catch (IOException e) {
                                                    throw new RuntimeException(e);
                                                }
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
        buttons.add(this.buttons[2]);
        buttons.add(Box.createRigidArea(new Dimension(5, 0)));

        JPanel batchGraderButton = new JPanel();
        batchGraderButton.add(this.buttons[3]);
        batchGraderButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.add(Box.createRigidArea(new Dimension(0, 5)));
        for (int i = 0; i < this.text.length; i++) {
            this.add(this.text[i]);
        }
        this.add(buttons);
        this.add(batchGraderButton);
        this.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void openNewPuzzleDialog() {
        CreatePuzzleDialog cpd = new CreatePuzzleDialog(this.frame, this);
        cpd.setVisible(true);
    }

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

    public void openEditorWithNewPuzzle(String game, int rows, int columns)
            throws IllegalArgumentException {
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

    /**
     * Opens the puzzle editor for the specified game with the given statements
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
