package model.rules;

import model.gameboard.Board;

public abstract class TreeElement
{
    protected TreeElementType type;
    protected Board board;

    public TreeElement(TreeElementType type)
    {
        this.type = type;
    }

    /**
     * Gets the type of tree element
     *
     * @return NODE if this tree element is a tree node,
     * TRANSITION, if this tree element is a transition
     */
    public TreeElementType getType()
    {
        return type;
    }

    /**
     * Sets the type of tree element
     *
     * @param type NODE if this tree element is a tree node,
     * TRANSITION, if this tree element is a transition
     */
    public void setType(TreeElementType type)
    {
        this.type = type;
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
