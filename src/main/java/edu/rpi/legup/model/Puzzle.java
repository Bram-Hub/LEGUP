package edu.rpi.legup.model;

import edu.rpi.legup.model.elements.*;
import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.ElementFactory;
import edu.rpi.legup.model.observer.IBoardListener;
import edu.rpi.legup.model.observer.IBoardSubject;
import edu.rpi.legup.model.observer.ITreeListener;
import edu.rpi.legup.model.observer.ITreeSubject;
import edu.rpi.legup.model.rules.*;
import edu.rpi.legup.model.tree.Tree;
import edu.rpi.legup.model.tree.TreeElement;
import edu.rpi.legup.model.tree.TreeElementType;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.save.InvalidFileFormatException;
import edu.rpi.legup.ui.boardview.BoardView;
import edu.rpi.legup.utility.LegupUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Abstract class representing a puzzle. The Puzzle class manages the core components of a puzzle
 * game, including the board, rules, and elements. It also handles importing and exporting puzzle
 * configurations and notifies listeners about changes.
 */
public abstract class Puzzle implements IBoardSubject, ITreeSubject {
    private static final Logger LOGGER = LogManager.getLogger(Puzzle.class.getName());

    protected String name;
    protected String tag = "";
    protected Board currentBoard;
    protected Tree tree;
    protected BoardView boardView;
    protected PuzzleImporter importer;
    protected PuzzleExporter exporter;
    protected ElementFactory factory;

    private List<IBoardListener> boardListeners;
    private List<ITreeListener> treeListeners;

    protected List<DirectRule> directRules;
    protected List<ContradictionRule> contradictionRules;
    protected List<CaseRule> caseRules;
    protected List<PlaceableElement> placeableElements;

    /** Puzzle Constructor - creates a new Puzzle */
    public Puzzle() {
        this.boardListeners = new ArrayList<>();
        this.treeListeners = new ArrayList<>();

        this.directRules = new ArrayList<>();
        this.contradictionRules = new ArrayList<>();
        this.caseRules = new ArrayList<>();

        this.placeableElements = new ArrayList<>();

        registerRules();
        registerPuzzleElements();
    }

    /**
     * Registers puzzle elements from the package of the derived class. Scans for classes annotated
     * with {@link RegisterElement} and initializes them.
     */
    private void registerPuzzleElements() {
        String packageName = this.getClass().getPackage().toString().replace("package ", "");

        try {
            Class[] possElements = LegupUtils.getClasses(packageName);

            for (Class c : possElements) {

                String classPackageName = c.getPackage().getName();
                if (!classPackageName.startsWith("edu.rpi.legup.puzzle.")
                        || !classPackageName.endsWith(".elements")) {
                    continue;
                }
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("possible element: {}", c.getName());
                }

                // check that the element is not abstract
                if (Modifier.isAbstract(c.getModifiers())) continue;

                for (Annotation a : c.getAnnotations()) {
                    if (a.annotationType() == RegisterElement.class) {
                        RegisterElement registerElement = (RegisterElement) a;
                        Constructor<?> cons = c.getConstructor();
                        try {
                            Element element = (Element) cons.newInstance();

                            switch (element.getElementType()) {
                                case PLACEABLE:
                                    this.addPlaceableElement((PlaceableElement) element);
                                    break;
                                default:
                                    break;
                            }
                        } catch (InvocationTargetException e) {
                            LOGGER.error("    Failed ");
                            e.getTargetException().printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Unable to find elements for " + this.getClass().getSimpleName(), e);
        }
    }

    /**
     * Registers rules from the package of the derived class. Scans for classes annotated with
     * {@link RegisterRule} and initializes them.
     */
    private void registerRules() {
        String packageName = this.getClass().getPackage().toString().replace("package ", "");

        try {
            Class[] possRules = LegupUtils.getClasses(packageName);

            for (Class c : possRules) {

                String classPackageName = c.getPackage().getName();
                if (!classPackageName.contains(".rules")) {
                    continue;
                }
                LOGGER.info("possible rule: {}", c.getName());

                // check that the rule is not abstract
                if (Modifier.isAbstract(c.getModifiers())) continue;

                for (Annotation a : c.getAnnotations()) {
                    if (a.annotationType() == RegisterRule.class) {
                        RegisterRule registerRule = (RegisterRule) a;
                        Constructor<?> cons = c.getConstructor();
                        try {
                            Rule rule = (Rule) cons.newInstance();

                            switch (rule.getRuleType()) {
                                case BASIC:
                                    this.addDirectRule((DirectRule) rule);
                                    break;
                                case CASE:
                                    this.addCaseRule((CaseRule) rule);
                                    break;
                                case CONTRADICTION:
                                    this.addContradictionRule((ContradictionRule) rule);
                                    break;
                                case MERGE:
                                    break;
                                default:
                                    break;
                            }
                        } catch (InvocationTargetException e) {
                            LOGGER.error("    Failed ");
                            e.getTargetException().printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Unable to find rules for {}", this.getClass().getSimpleName(), e);
        }
    }

    /** Initializes the view. Called by the invoker of the class */
    public abstract void initializeView();

    /**
     * Generates a random edu.rpi.legup.puzzle based on the difficulty
     *
     * @param difficulty level of difficulty (1-10)
     * @return board of the random edu.rpi.legup.puzzle
     */
    public abstract Board generatePuzzle(int difficulty);

    /**
     * Checks if the given height and width are valid board dimensions for the given puzzle
     *
     * @param rows the number of rows on the board
     * @param columns the number of columns on the board
     * @return true if the given dimensions are valid for the given puzzle, false otherwise
     */
    public boolean isValidDimensions(int rows, int columns) {
        return rows > 0 && columns > 0;
    }

    /**
     * Checks if the provided text input is valid for the puzzle.
     *
     * @param statements array of statements to check
     * @return true if input is valid, false otherwise
     */
    public boolean isValidTextInput(String[] statements) {
        return statements.length > 0;
    }

    /**
     * Determines if the edu.rpi.legup.puzzle was solves correctly
     *
     * @return true if the board was solved correctly, false otherwise
     */
    public boolean isPuzzleComplete() {
        if (tree == null) {
            return false;
        }

        boolean isComplete = tree.isValid();
        if (isComplete) {
            for (TreeElement leaf : tree.getLeafTreeElements()) {
                if (leaf.getType() == TreeElementType.NODE) {
                    TreeNode node = (TreeNode) leaf;
                    if (!node.isRoot()) {
                        isComplete &=
                                node.getParent().isContradictoryBranch()
                                        || isBoardComplete(node.getBoard());
                    } else {
                        isComplete &= isBoardComplete(node.getBoard());
                    }
                } else {
                    isComplete = false;
                }
            }
        }
        return isComplete;
    }

    /**
     * Determines if the current board is a valid state
     *
     * @param board board to check for validity
     * @return true if board is valid, false otherwise
     */
    public abstract boolean isBoardComplete(Board board);

    /**
     * Callback for when the board puzzleElement changes
     *
     * @param board the board that has changed
     */
    public abstract void onBoardChange(Board board);

    /**
     * Imports the board using the file stream
     *
     * @param fileName the file that is imported
     * @throws InvalidFileFormatException if file is invalid
     */
    public void importPuzzle(String fileName) throws InvalidFileFormatException {
        try {
            importPuzzle(new FileInputStream(fileName));
        } catch (IOException e) {
            LOGGER.error("Importing puzzle error", e);
            throw new InvalidFileFormatException("Could not find file");
        }
    }

    /**
     * Imports the board using the file stream
     *
     * @param inputStream the file stream that is imported
     * @throws InvalidFileFormatException if file stream is invalid
     */
    public void importPuzzle(InputStream inputStream) throws InvalidFileFormatException {
        Document document;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(inputStream);
        } catch (IOException | SAXException | ParserConfigurationException e) {
            LOGGER.error("Importing puzzle error", e);
            throw new InvalidFileFormatException("Could not find file");
        }

        org.w3c.dom.Element rootNode = document.getDocumentElement();
        if (rootNode.getTagName().equals("Legup")) {
            Node node = rootNode.getElementsByTagName("puzzle").item(0);
            if (importer == null) {
                throw new InvalidFileFormatException("Puzzle importer null");
            }
            importer.initializePuzzle(node);
        } else {
            LOGGER.error("Invalid file");
            throw new InvalidFileFormatException("Invalid file: must be a Legup file");
        }
    }

    /**
     * Gets the edu.rpi.legup.puzzle importer for importing edu.rpi.legup.puzzle files
     *
     * @return edu.rpi.legup.puzzle importer
     */
    public PuzzleImporter getImporter() {
        return importer;
    }

    /**
     * Gets the edu.rpi.legup.puzzle exporter for exporting edu.rpi.legup.puzzle files
     *
     * @return edu.rpi.legup.puzzle exporter
     */
    public PuzzleExporter getExporter() {
        return exporter;
    }

    /**
     * Gets the name of the edu.rpi.legup.puzzle
     *
     * @return name of the edu.rpi.legup.puzzle
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the list of direct rules
     *
     * @return list of basic rules
     */
    public List<DirectRule> getDirectRules() {
        return directRules;
    }

    /**
     * Gets the list of placeable elements.
     *
     * @return list of PlaceableElement instances
     */
    public List<PlaceableElement> getPlaceableElements() {
        return placeableElements;
    }

    /**
     * Sets the list of direct rules
     *
     * @param directRules list of basic rules
     */
    public void setDirectRules(List<DirectRule> directRules) {
        this.directRules = directRules;
    }

    /**
     * Adds a basic rule to this Puzzle
     *
     * @param rule basic rule to add
     */
    public void addDirectRule(DirectRule rule) {
        directRules.add(rule);
    }

    /**
     * Adds a placeable element to this puzzle.
     *
     * @param element PlaceableElement to add
     */
    public void addPlaceableElement(PlaceableElement element) {
        placeableElements.add(element);
    }

    /**
     * Remove a basic rule from this Puzzle
     *
     * @param rule basic rule to remove
     */
    public void removeDirectRule(DirectRule rule) {
        directRules.remove(rule);
    }

    /**
     * Accessor method for the puzzle UUID
     *
     * @return returns the puzzle UUID tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * Modifier method to override the puzzle persistent UUID
     *
     * @param tag String to overwrite the current puzzle UUID
     */
    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Gets the list of contradiction rules
     *
     * @return list of contradiction rules
     */
    public List<ContradictionRule> getContradictionRules() {
        return contradictionRules;
    }

    /**
     * Sets the list of contradiction rules
     *
     * @param contradictionRules list of contradiction rules
     */
    public void setContradictionRules(List<ContradictionRule> contradictionRules) {
        this.contradictionRules = contradictionRules;
    }

    /**
     * Adds a contradiction rule to this Puzzle
     *
     * @param rule contradiction rule to add
     */
    public void addContradictionRule(ContradictionRule rule) {
        contradictionRules.add(rule);
    }

    /**
     * Remove a contradiction rule from this Puzzle
     *
     * @param rule contradiction rule to remove
     */
    public void removeContradictionRule(ContradictionRule rule) {
        contradictionRules.remove(rule);
    }

    /**
     * Gets the list of case rules
     *
     * @return list of case rules
     */
    public List<CaseRule> getCaseRules() {
        return caseRules;
    }

    /**
     * Sets the list of case rules
     *
     * @param caseRules list of case rules
     */
    public void setCaseRules(List<CaseRule> caseRules) {
        this.caseRules = caseRules;
    }

    /**
     * Adds a case rule to this Puzzle
     *
     * @param rule case rule to add
     */
    public void addCaseRule(CaseRule rule) {
        caseRules.add(rule);
    }

    /**
     * Removes a case rule from this Puzzle
     *
     * @param rule case rule to remove
     */
    public void removeCaseRule(CaseRule rule) {
        caseRules.remove(rule);
    }

    /**
     * Gets the rule using the specified name
     *
     * @param name name of the rule
     * @return Rule
     */
    public Rule getRuleByName(String name) {
        for (Rule rule : directRules) {
            if (rule.getRuleName().equals(name)) {
                return rule;
            }
        }
        for (Rule rule : contradictionRules) {
            if (rule.getRuleName().equals(name)) {
                return rule;
            }
        }
        for (Rule rule : caseRules) {
            if (rule.getRuleName().equals(name)) {
                return rule;
            }
        }
        Rule mergeRule = new MergeRule();
        if (mergeRule.getRuleName().equals(name)) {
            return mergeRule;
        }
        return null;
    }

    /**
     * Gets the rule using the specified name
     *
     * @param id name of the rule
     * @return Rule
     */
    public Rule getRuleByID(String id) {
        for (Rule rule : directRules) {
            if (rule.getRuleID().equals(id)) {
                return rule;
            }
        }
        for (Rule rule : contradictionRules) {
            if (rule.getRuleID().equals(id)) {
                return rule;
            }
        }
        for (Rule rule : caseRules) {
            if (rule.getRuleID().equals(id)) {
                return rule;
            }
        }
        Rule mergeRule = new MergeRule();
        if (mergeRule.getRuleID().equals(id)) {
            return mergeRule;
        }
        return null;
    }

    /**
     * Gets the current board
     *
     * @return current board
     */
    public Board getCurrentBoard() {
        return currentBoard;
    }

    /**
     * Sets the current board
     *
     * @param currentBoard the current board
     */
    public void setCurrentBoard(Board currentBoard) {
        this.currentBoard = currentBoard;
    }

    /**
     * Gets the Tree for keeping the board states
     *
     * @return Tree
     */
    public Tree getTree() {
        return tree;
    }

    /**
     * Sets the Tree for keeping the board states
     *
     * @param tree tree of board states
     */
    public void setTree(Tree tree) {
        this.tree = tree;
    }

    /**
     * Gets the board view that displays the board
     *
     * @return board view
     */
    public BoardView getBoardView() {
        return boardView;
    }

    /**
     * Sets the board view that displays the board
     *
     * @param boardView board view
     */
    public void setBoardView(BoardView boardView) {
        this.boardView = boardView;
    }

    /**
     * Gets the ElementFactory associated with this edu.rpi.legup.puzzle
     *
     * @return ElementFactory associated with this edu.rpi.legup.puzzle
     */
    public ElementFactory getFactory() {
        return factory;
    }

    /**
     * Sets the ElementFactory associated with this edu.rpi.legup.puzzle
     *
     * @param factory ElementFactory associated with this edu.rpi.legup.puzzle
     */
    public void setFactory(ElementFactory factory) {
        this.factory = factory;
    }

    /**
     * Adds a board listener to the list of listeners. This allows the puzzle to notify the listener
     * about changes to the board.
     *
     * @param listener The IBoardListener to be added to the list of listeners.
     */
    @Override
    public void addBoardListener(IBoardListener listener) {
        boardListeners.add(listener);
    }

    /**
     * Removes a board listener from the list of listeners. This prevents the puzzle from notifying
     * the listener about future changes to the board.
     *
     * @param listener The IBoardListener to be removed from the list of listeners.
     */
    @Override
    public void removeBoardListener(IBoardListener listener) {
        boardListeners.remove(listener);
    }

    /**
     * Notifies all registered board listeners about changes. The provided algorithm is applied to
     * each listener to process the notification.
     *
     * @param algorithm A Consumer function that takes an IBoardListener and performs operations to
     *     notify the listener.
     */
    @Override
    public void notifyBoardListeners(Consumer<? super IBoardListener> algorithm) {
        boardListeners.forEach(algorithm);
    }

    /**
     * Adds a tree listener to the list of listeners. This allows the puzzle to notify the listener
     * about changes to the tree.
     *
     * @param listener The ITreeListener to be added to the list of listeners.
     */
    @Override
    public void addTreeListener(ITreeListener listener) {
        treeListeners.add(listener);
    }

    /**
     * Removes a tree listener from the list of listeners. This prevents the puzzle from notifying
     * the listener about future changes to the tree.
     *
     * @param listener The ITreeListener to be removed from the list of listeners.
     */
    @Override
    public void removeTreeListener(ITreeListener listener) {
        treeListeners.remove(listener);
    }

    /**
     * Notifies all registered tree listeners about changes. The provided algorithm is applied to
     * each listener to process the notification.
     *
     * @param algorithm A Consumer function that takes an ITreeListener and performs operations to
     *     notify the listener.
     */
    @Override
    public void notifyTreeListeners(Consumer<? super ITreeListener> algorithm) {
        treeListeners.forEach(algorithm);
    }

    /**
     * Checks if the puzzle is valid. The implementation of this method can vary based on the
     * specific criteria for puzzle validity.
     *
     * @return true if the puzzle is valid, false otherwise.
     */
    public boolean checkValidity() {
        return true;
    }
}
