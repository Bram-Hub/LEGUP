package ui;

import model.gameboard.Board;

public class Selection
{
    private Board state;
    private boolean isTransition;

    /**
     * Create a new selection
     *
     * @param state the state of the board, or parent state of the isTransition
     * @param isTransition true iff this is a isTransition
     */
    public Selection(Board state, boolean isTransition)
    {
        this.state = state;
        this.isTransition = isTransition;
    }

    /**
     * Retrieves the associated BoardState
     *
     * @return BoardState of the state, or parent state of the isTransition
     */
    public Board getState()
    {
        return state;
    }

    /**
     * Determines if the Selection is a state or a isTransition
     *
     * @return True is the Selection is a state, false if it is a isTransition
     */
    public boolean isState()
    {
        return !isTransition;
    }

    /**
     * Determines if the selection is a state or a isTransition
     *
     * @return True is the selection is a state, false if it is a isTransition
     */
    public boolean isTransition()
    {
        return isTransition;
    }
}

