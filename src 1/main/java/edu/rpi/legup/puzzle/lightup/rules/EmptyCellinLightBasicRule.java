package edu.rpi.legup.puzzle.lightup.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.BasicRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.lightup.LightUpBoard;
import edu.rpi.legup.puzzle.lightup.LightUpCell;
import edu.rpi.legup.puzzle.lightup.LightUpCellType;

public class EmptyCellinLightBasicRule extends BasicRule {

    public EmptyCellinLightBasicRule() {
        super("Empty Cells in Light",
                "Cells in light must be empty.",
                "edu/rpi/legup/images/lightup/rules/EmptyCellInLight.png");
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition    transition to check
     * @param puzzleElement index of the puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        LightUpBoard initialBoard = (LightUpBoard) transition.getParents().get(0).getBoard();
        initialBoard.fillWithLight();
        LightUpCell initCell = (LightUpCell) initialBoard.getPuzzleElement(puzzleElement);
        LightUpCell finalCell = (LightUpCell) transition.getBoard().getPuzzleElement(puzzleElement);
        if (finalCell.getType() == LightUpCellType.EMPTY && initCell.getType() == LightUpCellType.UNKNOWN && initCell.isLite()) {
            return null;
        }
        return "Cell is not forced to be empty";
    }

    /**
     * Creates a transition {@link Board} that has this rule applied to it using the {@link TreeNode}.
     *
     * @param node tree node used to create default transition board
     * @return default board or null if this rule cannot be applied to this tree node
     */
    @Override
    public Board getDefaultBoard(TreeNode node) {
        LightUpBoard lightUpBoard = (LightUpBoard) node.getBoard().copy();
        for (PuzzleElement element : lightUpBoard.getPuzzleElements()) {
            LightUpCell cell = (LightUpCell) element;
            if (cell.getType() == LightUpCellType.UNKNOWN && cell.isLite()) {
                cell.setData(LightUpCellType.EMPTY.value);
                lightUpBoard.addModifiedData(cell);
            }
        }
        if (lightUpBoard.getModifiedData().isEmpty()) {
            return null;
        } else {
            return lightUpBoard;
        }
    }
}
