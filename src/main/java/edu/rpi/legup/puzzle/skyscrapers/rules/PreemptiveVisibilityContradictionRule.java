package edu.rpi.legup.puzzle.skyscrapers.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersBoard;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersCell;

import java.awt.*;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;

public class PreemptiveVisibilityContradictionRule extends ContradictionRule {

    public PreemptiveVisibilityContradictionRule() {
        super("SKYS-CONT-0006", "Preemptive Visibility",
                "Visibility constraints are not met given an incomplete row/col",
                "edu/rpi/legup/images/skyscrapers/contradictions/PreemptiveVisibility.png");
    }

    /**
     * Checks whether there is an instance of a visibility contradiction in every possible row/col based on the specific
     * puzzleElement index using this rule
     *
     * @param board         board to check contradiction
     * @param puzzleElement equivalent puzzleElement
     * @return null if the all possible rows/cols contain a contradiction at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        SkyscrapersBoard skyscrapersBoard = (SkyscrapersBoard) board;
        SkyscrapersCell cell = (SkyscrapersCell) puzzleElement;
        Point loc = cell.getLocation();

        //Initialize instances of necessary contradiction and case rules
        InsufficientVisibilityContradictionRule tooFew = new InsufficientVisibilityContradictionRule();
        ExceedingVisibilityContradictionRule tooMany = new ExceedingVisibilityContradictionRule();
        CellForNumberCaseRule caseRule = new CellForNumberCaseRule();

        //Initialize skyscraperBoard queues for rows and cols
        Queue<SkyscrapersBoard> rowQ = new LinkedList<>();
        rowQ.add(skyscrapersBoard);
        Queue<SkyscrapersBoard> colQ = new LinkedList<>();
        colQ.add(skyscrapersBoard);

        // find all cases for the corresponding row and column for each possible skyscraper height

        //Add every possible case for all heights for each corresponding row and column
        for(int i = 0; i < skyscrapersBoard.getWidth(); i++) {
            int num = i + 1;

            //check row west clue
            List<Board> rows;

            for(int j = 0; j < rowQ.size(); j++) {
                SkyscrapersBoard temp = rowQ.poll(); //get row from the top of the stack

                //set flags
                boolean dupeTemp = temp.getDupeFlag();
                boolean viewTemp = temp.getViewFlag();
                temp.setDupeFlag(true);
                temp.setViewFlag(false);

                //get all cases for corresponding row based on west clue
                rows = caseRule.getCasesFor(temp, skyscrapersBoard.getWestClues().get(loc.y), num);

                //reset flags
                temp.setDupeFlag(dupeTemp);
                temp.setViewFlag(viewTemp);

                //add all row cases to row queue
                if (rows.size() == 0)
                    rowQ.add(temp);
                else {
                    for (Board k : rows) {
                        rowQ.add((SkyscrapersBoard) k);
                    }
                }
            }


            //check col north clue
            List<Board> cols;

            for(int j = 0; j < colQ.size(); j++) {
                SkyscrapersBoard temp = colQ.poll(); //get row from the top of the stack

                //set flags
                boolean dupeTemp = temp.getDupeFlag();
                boolean viewTemp = temp.getViewFlag();
                temp.setDupeFlag(true);
                temp.setViewFlag(false);

                //get all cases for corresponding col based on north clue
                cols = caseRule.getCasesFor(temp, skyscrapersBoard.getNorthClues().get(loc.x), num);

                //reset flags
                temp.setDupeFlag(dupeTemp);
                temp.setViewFlag(viewTemp);

                //add all row cases to row queue
                if(cols.size() == 0)
                    colQ.add(temp);
                else {
                    for(Board k : cols)
                        colQ.add((SkyscrapersBoard) k);
                }
            }

        }

        String rowTooFew;
        String rowTooMany;
        boolean rowContradiction = true;
        //check if each case board has a contradiction
        for(int j = 0; j < rowQ.size(); j++) {
            SkyscrapersBoard fullRow = rowQ.poll();
            //checks if there is a contradiction given the row based on the west clue
            rowTooFew = tooFew.checkContradictionAt(fullRow, cell); // is cell the correct puzzle element to check?
            rowTooMany = tooMany.checkContradictionAt(fullRow, cell);
            //boolean that checks if there is a contradiction within all rows
            rowContradiction = rowContradiction && (rowTooFew == null || rowTooMany == null); // !null means there isn't a contradiction, so there must be a valid permutation of the array
        }

        String colTooFew;
        String colTooMany;
        boolean colContradiction = true;
        for(int j = 0; j < colQ.size(); j++) {
            SkyscrapersBoard fullCol = colQ.poll();
            //checks if there is a contradiction given the col baesd on the north clue
            colTooFew = tooFew.checkContradictionAt(fullCol, cell);
            colTooMany = tooMany.checkContradictionAt(fullCol, cell);
            //boolean that checks if there is a contradiction within all the cols
            colContradiction = colContradiction && (colTooFew == null || colTooMany == null);
        }

        //if every possible permutation results in contradictions return null, else no contradiction
        if(rowContradiction || colContradiction)
            return null;
        return super.getNoContradictionMessage();
    }

    /**
     * Checks whether the tree node has a contradiction using this rule
     *
     * @param board board to check contradiction
     * @return null if the tree node contains a contradiction, otherwise error message
     */
    @Override
    public String checkContradiction(Board board) {
        SkyscrapersBoard skyscrapersBoard = (SkyscrapersBoard) board;
        for (int i = 0; i < skyscrapersBoard.getWidth(); i++) {
            //checks the middle diagonal (checkContradictionAt checks row/col off each)
            String checkStr = checkContradictionAt(board, skyscrapersBoard.getCell(i,i));
            if (checkStr == null) {
                return checkStr;
            }
        }
        return "No instance of the contradiction " + this.ruleName + " here";
    }



}
