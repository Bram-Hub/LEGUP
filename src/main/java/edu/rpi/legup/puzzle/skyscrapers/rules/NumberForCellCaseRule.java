package edu.rpi.legup.puzzle.skyscrapers.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersBoard;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersCell;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersType;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NumberForCellCaseRule extends CaseRule {

    public NumberForCellCaseRule() {
        super(
                "SKYS-CASE-0001",
                "Number For Cell",
                "A blank cell must have height of 1 to n.",
                "edu/rpi/legup/images/skyscrapers/cases/NumberForCell.png");
    }

    @Override
    public CaseBoard getCaseBoard(Board board) {
        SkyscrapersBoard lightUpBoard = (SkyscrapersBoard) board.copy();
        lightUpBoard.setModifiable(false);
        CaseBoard caseBoard = new CaseBoard(lightUpBoard, this);
        for (PuzzleElement data : lightUpBoard.getPuzzleElements()) {
            if (((SkyscrapersCell) data).getType() == SkyscrapersType.UNKNOWN) {
                caseBoard.addPickableElement(data);
            }
        }
        return caseBoard;
    }

    /**
     * Gets the possible cases at a specific location based on this case rule
     *
     * @param board the current board state
     * @param puzzleElement puzzleElement to determine the possible cases for
     * @return a list of elements the specified could be
     */
    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement) {
        ArrayList<Board> cases = new ArrayList<>();
        if (puzzleElement == null) {
            return cases;
        }

        SkyscrapersCell cell = (SkyscrapersCell) puzzleElement;
        SkyscrapersBoard skyscrapersboard = (SkyscrapersBoard) board;
        Point loc = cell.getLocation();

        Set<Integer> candidates = new HashSet<Integer>();
        for (int i = 1; i <= skyscrapersboard.getWidth(); i++) {
            Board newCase = board.copy();
            PuzzleElement newCell = newCase.getPuzzleElement(puzzleElement);
            newCell.setData(i);
            newCase.addModifiedData(newCell);

            // if flags
            boolean passed = true;
            if (skyscrapersboard.getDupeFlag()) {
                DuplicateNumberContradictionRule DupeRule = new DuplicateNumberContradictionRule();
                passed = passed && DupeRule.checkContradictionAt(newCase, newCell) != null;
            }
            if (skyscrapersboard.getViewFlag()) {
                PreemptiveVisibilityContradictionRule ViewRule =
                        new PreemptiveVisibilityContradictionRule();
                passed = passed && ViewRule.checkContradictionAt(newCase, newCell) != null;
            }
            // how should unresolved be handled? should it be?
            if (passed) {
                cases.add(newCase);
            }
        }

        return cases;
    }

    /**
     * Checks whether the child node logically follows from the parent node at the specific
     * puzzleElement index using this rule
     *
     * @param transition transition to check
     * @param puzzleElement index of the puzzleElement
     * @return null if the child node logically follow from the parent node at the specified
     *     puzzleElement, otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return checkRuleRaw(transition);
    }

    /**
     * Returns the elements necessary for the cases returned by getCases(board,puzzleElement) to be
     * valid Overridden by case rules dependent on more than just the modified data
     *
     * @param board board state at application
     * @param puzzleElement selected puzzleElement
     * @return List of puzzle elements (typically cells) this application of the case rule depends
     *     upon. Defaults to any element modified by any case
     */
    @Override
    public List<PuzzleElement> dependentElements(Board board, PuzzleElement puzzleElement) {
        List<PuzzleElement> elements = new ArrayList<>();

        SkyscrapersBoard puzzleBoard = (SkyscrapersBoard) board;
        SkyscrapersCell point = (SkyscrapersCell) puzzleBoard.getPuzzleElement(puzzleElement);

        List<SkyscrapersCell> cells = new ArrayList<>(List.of(point));

        // if dependent on row/col
        if (puzzleBoard.getDupeFlag() || puzzleBoard.getViewFlag()) {
            // add all cells in row/col intersecting given point
            cells.addAll(puzzleBoard.getRowCol(point.getLocation().x, SkyscrapersType.ANY, false));
            cells.addAll(puzzleBoard.getRowCol(point.getLocation().y, SkyscrapersType.ANY, true));
        }

        for (SkyscrapersCell cell : cells) {
            if (!elements.contains(board.getPuzzleElement(cell))) {
                elements.add(board.getPuzzleElement(cell));
            }
        }

        return elements;
    }
}
