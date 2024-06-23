package edu.rpi.legup.puzzle.binary.rules;


import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;

import java.util.ArrayList;

public class EliminateTheImpossibleDirectRule extends DirectRule {
    private final String INVALID_USE_MESSAGE = "Number at cell is incorrect";

    public EliminateTheImpossibleDirectRule() {
        super(
                "BINA-BASC-0004",
                "Eliminate The Impossible",
                "If three adjacent empty cells are open, prevents a trio of numbers to exist",
                "edu/rpi/legup/images/binary/rules/OneTileGapDirectRule.png");
    }

    // Function to generate all binary strings
    static String generateAllBinaryStrings(int n, String arr, int i)
    {
        System.out.println(i);
        if (i == n)
        {
            return arr;
        }

        // First assign "0" at ith position
        // and try for all other permutations
        // for remaining positions
        arr = arr + "0";
        generateAllBinaryStrings(n, arr, i + 1);

        // And then assign "1" at ith position
        // and try for all other permutations
        // for remaining positions
        arr = arr + "1";
        generateAllBinaryStrings(n, arr, i + 1);


        return null;
    }

    public ArrayList<String> binaryCombiniations(int numEmpty) {
        System.out.println("FUNCTION");
        ArrayList<String> possibilities = new ArrayList<>();
        String arr = "";

        generateAllBinaryStrings(numEmpty, arr, 0);
//
//        if (generateAllBinaryStrings(numEmpty, arr, 0) != null) {
//            //System.out.println("T)");
//            possibilities.add(arr);
//        }

        for (int i = 0; i < possibilities.size(); i++) {
            System.out.println(possibilities.get(i));
        }
        return null;
    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        BinaryBoard origBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
        BinaryCell binaryCell = (BinaryCell) puzzleElement;
        System.out.println("HI");
        binaryCombiniations(4);
        return "Grouping of Three Ones or Zeros not found";
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}
