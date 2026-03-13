package edu.rpi.legup.puzzle.starbattle.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.starbattle.StarBattleBoard;
import edu.rpi.legup.puzzle.starbattle.StarBattleCell;
import edu.rpi.legup.puzzle.starbattle.StarBattleCellType;
import java.util.ArrayList;
import java.util.List;

public class StarOrEmptyCaseRule extends CaseRule {

    public StarOrEmptyCaseRule() {
        super(
                "STBL-CASE-0002",
                "Star or Empty",
                "Each unknown space is either a star or empty.",
                "edu/rpi/legup/images/starbattle/cases/StarOrEmptyCaseRule.png");
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        StarBattleBoard starBattleBoard = (StarBattleBoard) board.copy();
        CaseBoard caseBoard = new CaseBoard(starBattleBoard, this);
        starBattleBoard.setModifiable(false);
        for (PuzzleElement element : starBattleBoard.getPuzzleElements()) {
            if (((StarBattleCell) element).getType() == StarBattleCellType.UNKNOWN) {
                caseBoard.addPickableElement(element);
            }
        }
        return caseBoard;
    }

    /**
     * Gets the possible cases at a specific location based on this case rule
     *
     * @param board the current board state
     * @param puzzleElement equivalent puzzleElement
     * @return a list of elements the specified could be
     */
    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases = new ArrayList<>();
        if (puzzleElement == null) {
            return cases;
        }

        Board case1 = board.copy();
        PuzzleElement data1 = case1.getPuzzleElement(puzzleElement);
        data1.setData(StarBattleCellType.STAR.value);
        case1.addModifiedData(data1);
        cases.add(case1);

        Board case2 = board.copy();
        PuzzleElement data2 = case2.getPuzzleElement(puzzleElement);
        data2.setData(StarBattleCellType.BLACK.value);
        case2.addModifiedData(data2);
        cases.add(case2);

        return cases;
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return checkRuleRaw(transition);
    }
}
