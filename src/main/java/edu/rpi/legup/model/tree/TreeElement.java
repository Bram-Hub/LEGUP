package edu.rpi.legup.model.tree;

import edu.rpi.legup.model.gameboard.Board;

public abstract class TreeElement
{
    protected TreeElementType type;
    protected Board board;

    /**
     * TreeElement Constructor - creates a tree element that is part of a tree
     *
     * @param type type of tree element (NODE or TRANSITION)
     */
    public TreeElement(TreeElementType type)
    {
        this.type = type;
    }

    /**
     * Determines if this tree node leads to a contradiction. Every path from this tree node
     * must lead to a contradiction including all of its children
     *
     * @return true if this tree node leads to a contradiction, false otherwise
     */
    public abstract boolean isContradictoryBranch();

    /**
     * Recursively determines if the sub tree rooted at this tree element is valid by checking
     * whether this tree element and all descendants of this tree element is justified
     * and justified correctly
     *
     * @return true if this tree element and all descendants of this tree element is valid,
     * false otherwise
     */
    public abstract boolean isValid();

    /**
     * Gets the type of tree element
     *
     * @return NODE if this tree element is a tree node, TRANSITION, if this tree element is a transition
     */
    public TreeElementType getType()
    {
        return type;
    }

    /**
     * Gets the board at this TreeElement
     *
     * @return the board at this TreeElement
     */
    public Board getBoard()
    {
        return board;
    }

    /**
     * Sets the board at this TreeElement
     *
     * @param board the board at this TreeElement
     */
    public void setBoard(Board board)
    {
        this.board = board;
    }
}
