package edu.rpi.legup.model;

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
import edu.rpi.legup.utility.LegupUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import edu.rpi.legup.save.InvalidFileFormatException;
import edu.rpi.legup.ui.boardview.BoardView;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Puzzle implements IBoardSubject, ITreeSubject {
    private static final Logger LOGGER = LogManager.getLogger(Puzzle.class.getName());

    protected String name;
    protected Board currentBoard;
    protected Tree tree;
    protected BoardView boardView;
    protected PuzzleImporter importer;
    protected PuzzleExporter exporter;
    protected ElementFactory factory;

    private List<IBoardListener> boardListeners;
    private List<ITreeListener> treeListeners;

    protected List<BasicRule> basicRules;
    protected List<ContradictionRule> contradictionRules;
    protected List<CaseRule> caseRules;

    /**
     * Puzzle Constructor - creates a new Puzzle
     */
    public Puzzle() {
        this.boardListeners = new ArrayList<>();
        this.treeListeners = new ArrayList<>();

        this.basicRules = new ArrayList<>();
        this.contradictionRules = new ArrayList<>();
        this.caseRules = new ArrayList<>();

        registerRules();
    }

    private void registerRules() {
        String packageName = this.getClass().getPackage().toString().replace("package ", "");

        try {
            Class[] possRules = LegupUtils.getClasses(packageName);

            for (Class c : possRules) {

                System.out.println("possible rule: " + c.getName());

                //check that the rule is not abstract
                if (Modifier.isAbstract(c.getModifiers())) continue;

                for (Annotation a : c.getAnnotations()) {
                    if (a.annotationType() == RegisterRule.class) {
                        RegisterRule registerRule = (RegisterRule) a;
                        Constructor<?> cons = c.getConstructor();
                        try {
                            Rule rule = (Rule) cons.newInstance();

                            switch (rule.getRuleType()) {
                                case BASIC:
                                    this.addBasicRule((BasicRule) rule);
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
                            System.out.println("    Failed ");
                            e.getTargetException().printStackTrace();
                        }
                    }
                }
            }

//        } catch (IOException | ClassNotFoundException | NoSuchMethodException |
//                InstantiationException | IllegalAccessException | InvocationTargetException e) {
//            LOGGER.error("Unable to find rules for " + this.getClass().getSimpleName(), e);
//        }
        }catch(Exception e){
            LOGGER.error("Unable to find rules for " + this.getClass().getSimpleName(), e);
        }
    }

    /**
     * Initializes the view. Called by the invoker of the class
     */
    public abstract void initializeView();

    /**
     * Generates a random edu.rpi.legup.puzzle based on the difficulty
     *
     * @param difficulty level of difficulty (1-10)
     * @return board of the random edu.rpi.legup.puzzle
     */
    public abstract Board generatePuzzle(int difficulty);

    /**
     * Determines if the edu.rpi.legup.puzzle was solves correctly
     *
     * @return true if the board was solved correctly, false otherwise
     */
    public boolean isPuzzleComplete() {
        boolean isComplete = tree.isValid();
        if (isComplete) {
            for (TreeElement leaf : tree.getLeafTreeElements()) {
                if(leaf.getType() == TreeElementType.NODE) {
                    TreeNode node = (TreeNode)leaf;
                    if (!node.isRoot()) {
                        isComplete &= node.getParent().isContradictoryBranch() || isBoardComplete(node.getBoard());
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
     * @param fileName
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
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
     * @param inputStream
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
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

        Element rootNode = document.getDocumentElement();
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
     * Gets the list of basic rules
     *
     * @return list of basic rules
     */
    public List<BasicRule> getBasicRules() {
        return basicRules;
    }

    /**
     * Sets the list of basic rules
     *
     * @param basicRules list of basic rules
     */
    public void setBasicRules(List<BasicRule> basicRules) {
        this.basicRules = basicRules;
    }

    /**
     * Adds a basic rule to this Puzzle
     *
     * @param rule basic rule to add
     */
    public void addBasicRule(BasicRule rule) {
        basicRules.add(rule);
    }

    /**
     * Remove a basic rule from this Puzzle
     *
     * @param rule basic rule to remove
     */
    public void removeBasicRule(BasicRule rule) {
        basicRules.remove(rule);
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
        for (Rule rule : basicRules) {
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
     * Adds a board listener
     *
     * @param listener listener to add
     */
    @Override
    public void addBoardListener(IBoardListener listener) {
        boardListeners.add(listener);
    }

    /**
     * Removes a board listener
     *
     * @param listener listener to remove
     */
    @Override
    public void removeBoardListener(IBoardListener listener) {
        boardListeners.remove(listener);
    }

    /**
     * Notifies listeners
     *
     * @param algorithm algorithm to notify the listeners with
     */
    @Override
    public void notifyBoardListeners(Consumer<? super IBoardListener> algorithm) {
        boardListeners.forEach(algorithm);
    }

    /**
     * Adds a board listener
     *
     * @param listener listener to add
     */
    @Override
    public void addTreeListener(ITreeListener listener) {
        treeListeners.add(listener);
    }

    /**
     * Removes a tree listener
     *
     * @param listener listener to remove
     */
    @Override
    public void removeTreeListener(ITreeListener listener) {
        treeListeners.remove(listener);
    }

    /**
     * Notifies listeners
     *
     * @param algorithm algorithm to notify the listeners with
     */
    @Override
    public void notifyTreeListeners(Consumer<? super ITreeListener> algorithm) {
        treeListeners.forEach(algorithm);
    }
}