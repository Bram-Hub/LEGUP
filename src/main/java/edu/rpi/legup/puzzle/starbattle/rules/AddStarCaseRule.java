package edu.rpi.legup.puzzle.starbattle.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.starbattle.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class AddStarCaseRule extends CaseRule {
    public AddStarCaseRule() {
        super("STBL-CASE-0001",
                "Add Star",
                "Different ways a region's star number can be satisfied",
                "");
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        StarBattleBoard starbattleBoard = (StarBattleBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(starbattleBoard, this);

        return caseBoard;
    }

    /**
     * Gets the possible cases at a specific location based on this case rule
     *
     * @param board         the current board state
     * @param puzzleElement the cell to determine the possible cases for
     * @return a list of elements the specified spot could be
     */
    @Override
    public List<Board> getCases(Board board, PuzzleElement puzzleElement) {
        StarBattleBoard starbattleBoard = (StarBattleBoard) board;
        // take selected spot, check if there are any stars in that row, column, and region

        // do we want all possible cases, that seems like a lot
        return null;
    }

    @Override
    public String checkRuleRaw(TreeTransition transition) {
        return null;
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return null;
    }
}