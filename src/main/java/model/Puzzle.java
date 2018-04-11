package model;

import model.gameboard.Board;
import model.gameboard.ElementFactory;
import model.rules.*;
import model.tree.Tree;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import org.xml.sax.SAXException;
import ui.Selection;
import ui.boardview.BoardView;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Puzzle
{
    protected String name;
    protected Board currentBoard;
    protected Tree tree;
    protected BoardView boardView;
    protected PuzzleImporter importer;
    protected PuzzleExporter exporter;
    protected ElementFactory factory;

    protected ArrayList<BasicRule> basicRules;
    protected ArrayList<ContradictionRule> contradictionRules;
    protected ArrayList<CaseRule> caseRules;

    /**
     * Puzzle Constructor - creates a new Puzzle
     */
    public Puzzle()
    {
        this.basicRules = new ArrayList<>();
        this.contradictionRules = new ArrayList<>();
        this.caseRules = new ArrayList<>();
    }

    /**
     * Initializes the view. Called by the invoker of the class
     */
    public abstract void initializeView();

    /**
     * Generates a random puzzle based on the difficulty
     *
     * @param difficulty level of difficulty (1-10)
     * @return board of the random puzzle
     */
    public abstract Board generatePuzzle(int difficulty);

    /**
     * Determines if the puzzle was solves correctly
     *
     * @return true if the board was solved correctly, false otherwise
     */
    public boolean isPuzzleComplete()
    {
        boolean isComplete = tree.isValid();
        if(isComplete)
        {
            for(TreeNode leaf : tree.getLeafNodes())
            {
                if(!leaf.isRoot())
                {
                    isComplete &= leaf.getParents().get(0).leadsToContradiction() || isBoardComplete(leaf.getBoard());
                }
                else
                {
                    isComplete &= isBoardComplete(leaf.getBoard());
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
     * Callback for when the board data changes
     *
     * @param board the board that has changed
     */
    public abstract void onBoardChange(Board board);

    /**
     * Callback for when the tree selection changes
     *
     * @param newSelection
     */
    public abstract void onTreeSelectionChange(ArrayList<Selection> newSelection);

    /**
     * Imports the board using the file stream
     *
     * @param fileName
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public abstract void importPuzzle(String fileName) throws IOException, ParserConfigurationException, SAXException;

    /**
     * Imports a proof file
     *
     * @param fileName
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public void importProof(String fileName) throws IOException, ParserConfigurationException, SAXException
    {
        importPuzzle(fileName);


    }

    public PuzzleImporter getImporter()
    {
        return importer;
    }

    public PuzzleExporter getExporter()
    {
        return exporter;
    }

    /**
     * Gets the name of the puzzle
     *
     * @return name of the puzzle
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the list of basic rules
     *
     * @return list of basic rules
     */
    public ArrayList<BasicRule> getBasicRules()
    {
        return basicRules;
    }

    /**
     * Sets the list of basic rules
     *
     * @param basicRules list of basic rules
     */
    public void setBasicRules(ArrayList<BasicRule> basicRules)
    {
        this.basicRules = basicRules;
    }

    /**
     * Adds a basic rule to this Puzzle
     *
     * @param rule basic rule to add
     */
    public void addBasicRule(BasicRule rule)
    {
        basicRules.add(rule);
    }

    /**
     * Remove a basic rule from this Puzzle
     *
     * @param rule basic rule to remove
     */
    public void removeBasicRule(BasicRule rule)
    {
        basicRules.remove(rule);
    }

    /**
     * Gets the list of contradiction rules
     *
     * @return list of contradiction rules
     */
    public ArrayList<ContradictionRule> getContradictionRules()
    {
        return contradictionRules;
    }

    /**
     * Sets the list of contradiction rules
     *
     * @param contradictionRules list of contradiction rules
     */
    public void setContradictionRules(ArrayList<ContradictionRule> contradictionRules)
    {
        this.contradictionRules = contradictionRules;
    }

    /**
     * Adds a contradiction rule to this Puzzle
     *
     * @param rule contradiction rule to add
     */
    public void addContradictionRule(ContradictionRule rule)
    {
        contradictionRules.add(rule);
    }

    /**
     * Remove a contradiction rule from this Puzzle
     *
     * @param rule contradiction rule to remove
     */
    public void removeContradictionRule(ContradictionRule rule)
    {
        contradictionRules.remove(rule);
    }

    /**
     * Gets the list of case rules
     *
     * @return list of case rules
     */
    public ArrayList<CaseRule> getCaseRules()
    {
        return caseRules;
    }

    /**
     * Sets the list of case rules
     *
     * @param caseRules list of case rules
     */
    public void setCaseRules(ArrayList<CaseRule> caseRules)
    {
        this.caseRules = caseRules;
    }

    /**
     * Adds a case rule to this Puzzle
     *
     * @param rule case rule to add
     */
    public void addCaseRule(CaseRule rule)
    {
        caseRules.add(rule);
    }

    /**
     * Removes a case rule from this Puzzle
     *
     * @param rule case rule to remove
     */
    public void removeCaseRule(CaseRule rule)
    {
        caseRules.remove(rule);
    }

    /**
     * Gets the rule using the specfied name
     *
     * @param name name of the rule
     * @return Rule
     */
    public Rule getRuleByName(String name)
    {
        for(Rule rule : basicRules)
        {
            if(rule.getRuleName().equals(name))
            {
                return rule;
            }
        }
        for(Rule rule : contradictionRules)
        {
            if(rule.getRuleName().equals(name))
            {
                return rule;
            }
        }
        for(Rule rule : caseRules)
        {
            if(rule.getRuleName().equals(name))
            {
                return rule;
            }
        }
        Rule mergeRule = new MergeRule();
        if(mergeRule.getRuleName().equals(name))
        {
            return mergeRule;
        }
        return null;
    }

    /**
     * Gets the current board
     *
     * @return current board
     */
    public Board getCurrentBoard()
    {
        return currentBoard;
    }

    /**
     * Sets the current board
     *
     * @param currentBoard the current board
     */
    public void setCurrentBoard(Board currentBoard)
    {
        this.currentBoard = currentBoard;
    }

    /**
     * Gets the Tree for keeping the board states
     *
     * @return Tree
     */
    public Tree getTree()
    {
        return tree;
    }

    /**
     * Sets the Tree for keeping the board states
     *
     * @param tree
     */
    public void setTree(Tree tree)
    {
        this.tree = tree;
    }

    public BoardView getBoardView()
    {
        return boardView;
    }

    public void setBoardView(BoardView boardView)
    {
        this.boardView = boardView;
    }

    /**
     * Gets the ElementFactory associated with this puzzle
     *
     * @return ElementFactory associated with this puzzle
     */
    public ElementFactory getFactory()
    {
        return factory;
    }

    /**
     * Sets the ElementFactory associated with this puzzle
     *
     * @param factory ElementFactory associated with this puzzle
     */
    public void setFactory(ElementFactory factory)
    {
        this.factory = factory;
    }
}
