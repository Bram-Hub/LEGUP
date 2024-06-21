package edu.rpi.legup.puzzle.binary.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.DirectRule;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.binary.BinaryBoard;
import edu.rpi.legup.puzzle.binary.BinaryCell;

import java.util.LinkedList;
import java.util.Queue;
import java.lang.Math.*;
import java.util.ArrayList;

public class EliminateTheImpossibleDirectRule extends DirectRule {
    private final String INVALID_USE_MESSAGE = "Number at cell is incorrect";

    public EliminateTheImpossibleDirectRule() {
        super(
                "BINA-BASC-0003",
                "Eliminate The Impossible",
                "If three adjacent empty cells are open, prevents a trio of numbers to exist",
                "edu/rpi/legup/images/binary/rules/OneTileGapDirectRule.png");
    }

    // Function to generate all binary strings
    void generateAllBinaryStrings(double x, int poss, ArrayList<String> possibilities)
    {
        int count = (int)x;
        int finalLen = poss;
        
        Queue<String> q = new LinkedList<String>();
        q.add("1");
        while (count-- > 0) {
            String s1 = q.peek();
            q.remove();
            
            String newS1 = s1;
            int curLen = newS1.length();
            
            int runFor = poss - curLen;
            if(curLen < finalLen){
                
                
                for(int i = 0; i < runFor; i++){
                    newS1 = "0" + newS1;
                }
                
            }
            
            System.out.println(newS1);
            possibilities.add(newS1);
            String s2 = s1;
            q.add(s1 + "0");
            q.add(s2 + "1");
        }

    }

    @Override
    public String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement) {
        // This function should first check if there are three open spaces, if so, continue, else figure out
        // how many spots are open, all the possible binary combinations that could be put there, and by
        // analyzing the common factors, logically determine which number has a set spot, meaning that we know
        // that a certain spot must be a zero or a one

        BinaryBoard origBoard = (BinaryBoard) transition.getParents().get(0).getBoard();
        BinaryCell binaryCell = (BinaryCell) puzzleElement;

        ArrayList<String> result = new ArrayList<String>();
        generateAllBinaryStrings(10,4,result);

        for(String s : result){
            System.out.println(s);
        }

        return "Grouping of Three Ones or Zeros not found TEST";
    }

    @Override
    public Board getDefaultBoard(TreeNode node) {
        return null;
    }
}
