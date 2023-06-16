package legup;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.Tree;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import puzzles.battleship.rules.AdjacentShipsContradictionRuleTest;
import puzzles.battleship.rules.FinishWithShipsDirectRuleTests;

public final class TestUtilities {
    public static void importTestBoard(String fileName, Puzzle puzzle) throws InvalidFileFormatException {
        puzzle.importPuzzle(ClassLoader.getSystemResourceAsStream(fileName));
        Tree tree = puzzle.getTree();
        TreeNode rootNode = tree.getRootNode();
        Board board = rootNode.getBoard().copy();
        TreeTransition transition = new TreeTransition(rootNode, board);
        rootNode.getChildren().add(transition);
    }


}


