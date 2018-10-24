package edu.rpi.legup.puzzle.treetent.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.CaseRule;
import edu.rpi.legup.model.rules.RegisterRule;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.treetent.TreeTentBoard;
import edu.rpi.legup.puzzle.treetent.TreeTentCell;
import edu.rpi.legup.puzzle.treetent.TreeTentLine;
import edu.rpi.legup.puzzle.treetent.TreeTentType;

import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

public class LinkTentCaseRule extends CaseRule
{

    public LinkTentCaseRule()
    {
        super("Links from tent",
                "A tent must link to exactly one adjacent tree.",
                "edu/rpi/legup/images/treetent/caseLinkTent.png");
    }

    @Override
    public CaseBoard getCaseBoard(Board board)
    {
        TreeTentBoard treeTentBoard = ((TreeTentBoard) board).copy();
        treeTentBoard.setModifiable(false);
        CaseBoard caseBoard = new CaseBoard(treeTentBoard, this);
        //caseBoard.setModifiable(false);
        for(PuzzleElement element: treeTentBoard.getPuzzleElements()){
            TreeTentCell cell = (TreeTentCell) element;
            ArrayList<PuzzleElement> branches = branchable(treeTentBoard,cell.getLocation());
            if(cell.getType() == TreeTentType.TENT && !cell.isLinked() && branches.size() > 0){
                caseBoard.addPickableElement(element);
            }
        }
        return caseBoard;

    }

    public ArrayList<PuzzleElement> branchable(TreeTentBoard board, Point loc){
        ArrayList<TreeTentCell> adjacent = new ArrayList<>();
        ArrayList<PuzzleElement> branchableCell = new ArrayList<>();
        adjacent.add(board.getCell(loc.x+1,loc.y));
        adjacent.add(board.getCell(loc.x-1,loc.y));
        adjacent.add(board.getCell(loc.x,loc.y+1));
        adjacent.add(board.getCell(loc.x,loc.y-1));
        for(TreeTentCell cell: adjacent){
            if(cell != null && (cell.getType() == TreeTentType.TREE && !cell.isLinked())){
                branchableCell.add(cell);
            }
        }
        return branchableCell;

    }

    /**
     * Gets the possible cases at a specific location based on this case rule
     *
     * @param board        the current board state
     * @param puzzleElement equivalent puzzleElement
     *
     * @return a list of elements the specified could be
     */
    @Override
    public ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement)
    {
        ArrayList<Board> cases = new ArrayList<>();
        //ArrayList<Integer> branches = branchable((TreeTentBoard) board,((TreeTentCell) board.getElementData(elementIndex)).getLocation());
        TreeTentBoard treeTentBoard = (TreeTentBoard) board;

        List<PuzzleElement> branches = branchable(treeTentBoard, ((TreeTentCell) puzzleElement).getLocation());

        for(PuzzleElement c: branches){
            TreeTentBoard branchCase = (TreeTentBoard) board.copy();
            TreeTentCell cell1 = (TreeTentCell) branchCase.getPuzzleElement(puzzleElement);
            TreeTentCell cell2 = (TreeTentCell) branchCase.getPuzzleElement(c);
            TreeTentLine line = new TreeTentLine(cell1,cell2);
            cell1.setLink(true);
            cell2.setLink(true);
            branchCase.getLines().add(line);
            branchCase.addModifiedData(line);
            cases.add(branchCase);
        }
        return cases;
    }


    /**
     * Checks whether the transition logically follows from the parent node using this rule
     *
     * @param transition transition to check
     *
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRuleRaw(TreeTransition transition)
    {
        return null;
    }

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition   transition to check
     * @param puzzleElement equivalent puzzleElement
     *
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement)
    {
        return null;
    }
}
