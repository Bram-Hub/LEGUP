package edu.rpi.legup.ui;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.app.LegupPreferences;
import edu.rpi.legup.app.VersionInfo;
import edu.rpi.legup.controller.CursorController;
import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.utility.SVGImage;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.io.FileWriter;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * The {@code HomePanel} class represents the home panel of the LEGUP application. This panel
 * provides buttons for functionalities of opening the proof editor, opening the puzzle editor, and
 * performing batch grading. It also includes a menu bar with options for preferences.
 */
public class HomePanel extends LegupPanel {
    private static final Logger LOGGER = LogManager.getLogger(HomePanel.class.getName());
    private static final ArrayList<String> _tagsToGrade = new ArrayList<>();
    private static final ArrayList<String> _typesToGrade = new ArrayList<>();
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
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Preferences clicked");
                    }
                });
        settings.addSeparator();
        settings.add(preferences);

        JMenuItem contribute = new JMenuItem("Contribute to LEGUP");
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
                        .getResource("edu/rpi/legup/images/Legup/homepanel/proof_file.svg");
        SVGImage button0Icon = new SVGImage(button0IconLocation);
        this.buttons[0].setFocusPainted(false);
        this.buttons[0].setIcon(button0Icon.getIcon(this.buttonSize, this.buttonSize));
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
                        .getResource("edu/rpi/legup/images/Legup/homepanel/new_puzzle_file.svg");
        SVGImage button1Icon = new SVGImage(button1IconLocation);
        this.buttons[1].setFocusPainted(false);
        this.buttons[1].setIcon(button1Icon.getIcon(this.buttonSize, this.buttonSize));
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
        this.buttons[2].addActionListener(e -> openBatchGraderMenu());
    }

    /** Initializes screen for autograder options */
    public void openBatchGraderMenu() {
        JDialog batchGraderOptions = new JDialog(frame, "Batch Grader Options", true);
        batchGraderOptions.setSize(450, 200);
        batchGraderOptions.setLayout(new BorderLayout());

        // Create a panel for the directory selection part
        JPanel directoryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton browseButton = new JButton("Select Directory");
        JTextField directoryField = new JTextField(10);
        directoryField.setEnabled(false);

        directoryPanel.add(browseButton);
        directoryPanel.add(directoryField);
        batchGraderOptions.add(directoryPanel, BorderLayout.NORTH);

        // Create a panel for the puzzle IDs label, text field, and checkbox
        JPanel puzzleIdPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel puzzleIdLabel = new JLabel("Puzzle IDs:");
        JTextField puzzleIdField = new JTextField(10);
        puzzleIdField.setEnabled(false);
        JCheckBox gradeAllIDsCheckbox = new JCheckBox("Grade All IDs");
        gradeAllIDsCheckbox.setSelected(true);

        puzzleIdPanel.add(puzzleIdLabel);
        puzzleIdPanel.add(puzzleIdField);
        puzzleIdPanel.add(gradeAllIDsCheckbox);

        // Create a panel for the puzzle tags label, text field, and checkbox
        JPanel puzzleTypePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel puzzleTypeLabel = new JLabel("Puzzle Types:");
        JTextField puzzleTypeField = new JTextField(10);
        puzzleTypeField.setEnabled(false);
        JCheckBox gradeAllTagsCheckbox = new JCheckBox("Grade All Types");
        gradeAllTagsCheckbox.setSelected(true);

        puzzleTypePanel.add(puzzleTypeLabel);
        puzzleTypePanel.add(puzzleTypeField);
        puzzleTypePanel.add(gradeAllTagsCheckbox);

        JPanel batchGraderConstraints = new JPanel(new FlowLayout(FlowLayout.CENTER));
        batchGraderConstraints.add(puzzleIdPanel);
        batchGraderConstraints.add(puzzleTypePanel);
        batchGraderOptions.add(batchGraderConstraints, BorderLayout.CENTER);

        // Create a save button at the bottom
        JButton gradeButton = new JButton("Grade");
        JButton updateButton = new JButton("Update");
        JPanel gradePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        gradePanel.add(gradeButton);
        gradePanel.add(updateButton);
        batchGraderOptions.add(gradePanel, BorderLayout.SOUTH);

        // Action listeners for the buttons
        gradeAllIDsCheckbox.addActionListener(
                e -> puzzleIdField.setEnabled(!gradeAllIDsCheckbox.isSelected()));
        gradeAllTagsCheckbox.addActionListener(
                e -> puzzleTypeField.setEnabled(!gradeAllTagsCheckbox.isSelected()));

        browseButton.addActionListener(
                e -> {
                    JFileChooser folderBrowser = new JFileChooser();
                    folderBrowser.setCurrentDirectory(new File(LegupPreferences.WORK_DIRECTORY));
                    folderBrowser.setDialogTitle("Select Directory");
                    folderBrowser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    folderBrowser.setAcceptAllFileFilterUsed(false);
                    folderBrowser.setSelectedFile(null);
                    folderBrowser.setVisible(true);

                    int result = folderBrowser.showOpenDialog(frame);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        directoryField.setText(folderBrowser.getSelectedFile().getAbsolutePath());
                    }
                });

        gradeButton.addActionListener(
                e -> {
                    String directoryPath = directoryField.getText();
                    String puzzleTags = puzzleIdField.getText();
                    String puzzleTypes = puzzleTypeField.getText();

                    _tagsToGrade.clear();
                    if (!puzzleTags.isEmpty()) {
                        Pattern pattern = Pattern.compile("\"(.*?)\"");
                        Matcher matcher = pattern.matcher(puzzleTags);

                        while (matcher.find()) {
                            _tagsToGrade.add(matcher.group(1));
                        }
                    }
                    _typesToGrade.clear();
                    if (!puzzleTypes.isEmpty()) {
                        Pattern pattern = Pattern.compile("\"(.*?)\"");
                        Matcher matcher = pattern.matcher(puzzleTypes);

                        while (matcher.find()) {
                            _typesToGrade.add(matcher.group(1));
                        }
                    }

                    try {
                        File dir = new File(directoryPath);
                        use_xml_to_check(dir);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Finished autograding");
                    }

                    batchGraderOptions.dispose();
                });

        updateButton.addActionListener(
                e -> {
                    recursiveUpdater(new File(directoryField.getText()));
                    JOptionPane.showMessageDialog(null, "Updating complete.");
                    batchGraderOptions.dispose();
                });

        // Center the dialog on the screen
        batchGraderOptions.setLocationRelativeTo(null);
        batchGraderOptions.setVisible(true);
    }

    /**
     * Processes XML files within a selected directory and generates a CSV report on their "solved?"
     * status. The method allows the user to select a directory, and evaluates each XML file for a
     * "solved?" status. Results are saved in a "result.csv" file.
     *
     * @effect Selects a directory, processes each XML file to check for "solved?" status, and
     *     writes results to "result.csv". Opens the CSV file upon completion.
     */
    private void use_xml_to_check(File folder) {
        /* Select a folder, go through each .xml file in the subfolders, look for "isSolved" flag */
        File resultFile = new File(folder.getAbsolutePath() + File.separator + "result.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile))) {
            writer.append("Name,File Name,Puzzle Type,Puzzle Tag,Solved?,Last Saved\n");
            // Go through student folders, recurse for inner folders
            for (final File folderEntry :
                    Objects.requireNonNull(folder.listFiles(File::isDirectory))) {
                String path = folderEntry.getName();
                // use this helper function to write to the .csv file
                recursive_parser(folderEntry, writer, path);
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
        _tagsToGrade.clear();
    }

    /**
     * @param file - the input file
     * @return Parsed document of file if possible, null otherwise
     */
    public Document isxmlfile(File file) {
        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(file);
        } catch (Exception e) {
            LOGGER.error("'{}' is not a valid XML file", file.getPath());
        }
        return doc;
    }

    /**
     * reads the puzzle name and type, and outputs to .csv file
     *
     * @param doc - the parsed file currently being graded
     * @param writer - write to .csv
     * @throws IOException
     */
    private void parsePuzzle(Document doc, BufferedWriter writer) throws IOException {
        NodeList puzzleNodes = doc.getElementsByTagName("puzzle");
        if (puzzleNodes.getLength() <= 0) {
            writer.write("not a LEGUP puzzle!");
            return;
        }

        Element puzzleElement = (Element) puzzleNodes.item(0);
        String puzzleType = puzzleElement.getAttribute("name");
        writer.write(puzzleType.isEmpty() ? "not a LEGUP puzzle!" : puzzleType);
        writer.write(",");

        String puzzleTag = puzzleElement.getAttribute("tag");
        writer.write(puzzleTag.isEmpty() ? "No tag given" : puzzleTag);
    }

    /**
     * Reads the hashed solved state and export timestamp, unhashes information and prints out to
     * csv
     *
     * @param doc - the parsed file currently being graded
     * @param writer - write to .csv
     * @throws IOException
     */
    private void parseSolvedState(Document doc, BufferedWriter writer) throws IOException {
        NodeList solvedNodes = doc.getElementsByTagName("solved");
        if (solvedNodes.getLength() <= 0) {
            writer.write(",missing flag!");
            return;
        }

        Element solvedElement = (Element) solvedNodes.item(0);
        String isSolved = solvedElement.getAttribute("isSolved");
        String lastSaved = solvedElement.getAttribute("lastSaved");

        // unhash solved flag
        writer.write(",");
        try {
            int solvedHash = Integer.parseInt(isSolved);
            Boolean solvedState = PuzzleExporter.inverseHash(solvedHash, lastSaved);

            if (solvedState == null) {
                writer.write("Error");
            } else if (solvedState) {
                writer.write("Solved");
            } else {
                writer.write("Not Solved");
            }
        } catch (NumberFormatException e) {
            writer.write("Error");
            LOGGER.error("Solved state could not be unhashed:\n{}", e.getMessage());
        }

        // Append the lastSaved attribute
        writer.write(",");
        writer.write(!lastSaved.isEmpty() ? lastSaved : "Error");
    }

    /**
     * @param folder - the input folder
     * @param writer - write to .csv
     * @param path - the current path
     * @throws IOException
     */
    private void recursive_parser(File folder, BufferedWriter writer, String path)
            throws IOException {
        // Empty folder
        if (Objects.requireNonNull(folder.listFiles()).length == 0) {
            writer.append(path).append(",Empty folder,Ungradeable\n");
            return;
        }
        // Go through every other file in the folder
        try {
            for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
                if (fileEntry.getName().equals("result.csv")) {
                    continue;
                }
                // Recurse if it is a subfolder
                if (fileEntry.isDirectory()) {
                    recursive_parser(fileEntry, writer, path + "/" + fileEntry.getName());
                    continue;
                }
                // Set path name
                String fName = fileEntry.getName();
                if (fileEntry.getName().charAt(0) == '.') {
                    continue;
                }

                Document doc;
                if ((doc = isxmlfile(fileEntry)) == null) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("{} is not a '.xml' file", fName);
                    }
                    writer.write(fName + ",Not an xml file!\n");
                    continue;
                }

                NodeList puzzleNodes = doc.getElementsByTagName("puzzle");
                Element puzzleElement = (Element) puzzleNodes.item(0);
                String puzzleTag = puzzleElement.getAttribute("tag");
                if (!_tagsToGrade.isEmpty()
                        && _tagsToGrade.stream().noneMatch(puzzleTag::contains)) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(
                                "'{}' is not graded with tag '{}'",
                                puzzleElement.getAttribute("name"),
                                puzzleTag);
                    }
                    continue;
                }
                String puzzleType = puzzleElement.getAttribute("name");
                if (!_typesToGrade.isEmpty()
                        && _typesToGrade.stream().noneMatch(puzzleType::contains)) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(
                                "'{}' is not graded with type '{}'",
                                puzzleElement.getAttribute("name"),
                                puzzleType);
                    }
                    continue;
                }

                // write data
                path = folder.getAbsolutePath();
                writer.write(path.substring(path.lastIndexOf(File.separator) + 1));
                writer.write(",");
                writer.write(fName);
                writer.write(",");

                parsePuzzle(doc, writer);
                parseSolvedState(doc, writer);

                writer.write("\n");
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Updates all old puzzle files to the new tagged variant and hashes all solved states
     *
     * @param folder Folder to update all files, and recurse for all subdirectories
     */
    private void recursiveUpdater(File folder) {
        if (Objects.requireNonNull(folder.listFiles()).length == 0) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Empty directory");
            }
            return;
        }
        for (File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
                recursiveUpdater(fileEntry);
                continue;
            }

            String fName = fileEntry.getName();
            Document doc;
            if ((doc = isxmlfile(fileEntry)) == null) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("{} is not a '.xml' file", fileEntry.getName());
                }
                continue;
            }

            Element puzzleNodes = (Element) doc.getElementsByTagName("puzzle").item(0);
            if (!puzzleNodes.hasAttribute("tag")) {
                puzzleNodes.setAttribute("tag", fName);
            }

            Element solvedNodes = (Element) doc.getElementsByTagName("solved").item(0);
            String time = LocalDateTime.now().format(PuzzleExporter.DATE_FORMAT);
            String unsolved = PuzzleExporter.obfHash(false, time) + "";
            if (solvedNodes != null) {
                solvedNodes.getAttributeNode("isSolved").setValue(unsolved);
                solvedNodes.getAttributeNode("lastSaved").setTextContent(time);
            } else {
                solvedNodes = doc.createElement("solved");
                solvedNodes.setAttribute("isSolved", unsolved);
                solvedNodes.setAttribute("lastSaved", time);
                doc.getElementsByTagName("Legup").item(0).appendChild(solvedNodes);
            }

            try {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.setOutputProperty(OutputKeys.INDENT, "no");
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

                DOMSource source = new DOMSource(doc);
                StreamResult result =
                        new StreamResult(
                                new File(folder.getAbsolutePath() + File.separator + fName));

                transformer.transform(source, result);
            } catch (TransformerException e) {
                LOGGER.error("Unable to update file: {}:\n{}", fName, e.getMessage());
            }
        }
    }

    /**
     * Initializes the text labels for the user interface. Sets up labels for welcome message, led
     * by Bram, and version information.
     */
    private void initText() {
        this.text = new JLabel[3];

        JLabel welcome = new JLabel("Welcome to LEGUP");
        welcome.setFont(new Font("Roboto", Font.BOLD, 23));
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel credits = new JLabel("A project by Dr. Bram van Heuveln");
        credits.setFont(new Font("Roboto", Font.PLAIN, 12));
        credits.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel version = new JLabel("Version " + VersionInfo.getVersion());
        version.setFont(new Font("Roboto", Font.ITALIC, 10));
        version.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.text[0] = welcome;
        this.text[1] = credits;
        this.text[2] = version;
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
            LOGGER.error("Failed to open editor with new puzzle");
            e.printStackTrace(System.out);
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
