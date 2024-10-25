//package edu.rpi.legup.puzzle.binary.rules;
//
//import edu.rpi.legup.model.gameboard.Board;
//import edu.rpi.legup.model.gameboard.PuzzleElement;
//import edu.rpi.legup.model.rules.DirectRule;
//import edu.rpi.legup.model.tree.TreeNode;
//import edu.rpi.legup.model.tree.TreeTransition;
//import edu.rpi.legup.puzzle.binary.BinaryBoard;
//import edu.rpi.legup.puzzle.binary.BinaryCell;
//import edu.rpi.legup.puzzle.binary.BinaryType;
//
//import java.util.LinkedList;
//import java.util.Queue;
//import java.lang.Math.*;
//import java.lang.reflect.Array;
//import java.util.ArrayList;
//
//public class EliminateTheImpossibleDirectRule extends DirectRule {
//    private final String INVALID_USE_MESSAGE = "Number at cell is incorrect";
//
//    public EliminateTheImpossibleDirectRule() {
//        super(
//                "BINA-BASC-0004",
//                "Eliminate The Impossible",
//                "Out of the remaining empty cells in this row or column, this digit must go here, otherwise there will be a future contradiction",
//                "edu/rpi/legup/images/binary/rules/EliminateTheImpossibleDirectRule.png");
//    }
//
//    // Function to generate all binary strings
//    void generatePossibilitites(int spots, ArrayList<String> possibilities, int zeroCount, int oneCount)
//    // This function generates all the possible combinations of 0s and 1s for a
//    // certain size, it does this
//    // by basically just counting from 0 to the number - 1, so if you want all the
//    // possible combinations for 3
//    // spots, you can just count in binary from 0 to 7 (taking 3 spots, so from 000
//    // to 111). To be practical,
//    // the function does not return an array with all the possibilities as an array,
//    // but populates the
//    // arraylist you pass in (possibilities)
//    {
//        if (zeroCount + oneCount != spots) {
//            System.out.println("INVALID INPUT");
//            return;
//        }
//
//        if (zeroCount == spots) {
//            String zero = "";
//            for (int i = 0; i < spots; i++) {
//                zero = zero + "0";
//            }
//            possibilities.add(zero);
//
//        }
//        int count = (int) Math.pow(2, spots) - 1;
//        int finalLen = spots;
//        Queue<String> q = new LinkedList<String>();
//        q.add("1");
//
//        while (count-- > 0) {
//            String s1 = q.peek();
//            q.remove();
//
//            String newS1 = s1;
//            int curLen = newS1.length();
//            int runFor = spots - curLen;
//            if (curLen < finalLen) {
//                for (int i = 0; i < runFor; i++) {
//                    newS1 = "0" + newS1;
//                }
//            }
//            int curZeros = 0;
//            int curOnes = 0;
//
//            for (int i = 0; i < spots; i++) {
//                if (newS1.charAt(i) == '0') {
//                    curZeros++;
//                }
//                if (newS1.charAt(i) == '1') {
//                    curOnes++;
//                }
//            }
//
//            if (zeroCount == curZeros && oneCount == curOnes) {
//                possibilities.add(newS1);
//            }
//            String s2 = s1;
//            q.add(s1 + "0");
//            q.add(s2 + "1");
//        }
//    }
//
//    @Override
//    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
//        // This function should first check if there are three open spaces, if so,
//        // continue, else figure out
//        // how many spots are open, all the possible binary combinations that could be
//        // put there, and by
//        // analyzing the common factors, logically determine which number has a set
//        // spot, meaning that we know
//        // that a certain spot must be a zero or a one
//
//        BinaryBoard origBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
//        BinaryCell binaryCell = (BinaryCell) puzzleElement;
//
//        //Getting the row where the user clicked
//        ArrayList<BinaryCell> row = origBoard.listRowCells(binaryCell.getLocation().y);
//        int size = row.size();
//        int rowNumZeros = 0;
//        int rowNumOnes = 0;
//
//        for (BinaryCell item : row) {
//            if (item.getType() == BinaryType.ZERO) {
//                rowNumZeros++;
//            } else if (item.getType() == BinaryType.ONE) {
//                rowNumOnes++;
//            }
//        }
//
//        ArrayList<String> rowResult = new ArrayList<String>();
//
//        // To call generatePossibilitites(), you must call it and pass in the amount of
//        // unknown spots left,
//        // an ArrayList that will be populated with the possible results (in String
//        // form), the amount of zeros left and ones left
//        generatePossibilitites((size - rowNumZeros - rowNumOnes), rowResult, size / 2 - rowNumZeros, size / 2 - rowNumOnes);
//
//        // Create deep copies of each row
//        ArrayList<ArrayList<BinaryCell>> rowCopies = new ArrayList<>();
//        for (int i = 0; i < rowResult.size(); i++) {
//            ArrayList<BinaryCell> newRow = new ArrayList<>();
//            for (BinaryCell cell : row) {
//                newRow.add(cell.copy());
//            }
//            rowCopies.add(newRow);
//        }
//
//        System.out.println("Number of possible binary combinations: " + rowCopies.size());
//
//        ArrayList<ArrayList<BinaryCell>> nonContraRows = new ArrayList<>();
//        int rowIdx = 0;
//        for(ArrayList<BinaryCell> curRow : rowCopies){
//            int charIdx = 0;
//            System.out.println(rowResult.get(rowIdx));
//            for(int i = 0; i < curRow.size(); i++) {
//                if (curRow.get(i).getData() == 2) {
//                    if (rowResult.get(rowIdx).charAt(charIdx) == '0') {
//                        curRow.get(i).setData(0);
//                    }
//                    else if (rowResult.get(rowIdx).charAt(charIdx) == '1') {
//                        curRow.get(i).setData(1);
//                    }
//                    charIdx++;
//                }
//                System.out.print(curRow.get(i).getData() + " ");
//            }
//
//            boolean threeAdjacent = false;
//            int count = 1;
//            for(int i = 1; i < curRow.size(); i++) {
//                if (curRow.get(i).getData() == curRow.get(i-1).getData()) {
//                    count++;
//                    if (count == 3) {
//                        threeAdjacent = true;
//                        break;
//                    }
//                } else {
//                    count = 1;
//                }
//            }
//
//            if (!threeAdjacent) {
//                nonContraRows.add(curRow);
//            }
//
//            rowIdx++;
//            System.out.println();
//        }
//
//        System.out.println("Number of non contradiction rows: " + nonContraRows.size());
//        int colNum = binaryCell.getLocation().x;
//        boolean invalid = false;
//        for(int i = 0; i < nonContraRows.size(); i++) {
//            if (nonContraRows.get(i).get(colNum).getData() != binaryCell.getData()) {
//                invalid = true;
//                break;
//            }
//        }
//
//        if (!invalid) {
//            return null;
//        }
//
//        return "This cell can either be a 0 or a 1";
//
//    }
//
//    @Override
//    public Board getDefaultBoard(TreeNode node) {
//        return null;
//    }
//}
