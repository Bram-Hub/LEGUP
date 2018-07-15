package legup;

import model.Puzzle;
import model.gameboard.Board;
import model.rules.Rule;
import model.tree.Tree;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import save.InvalidFileFormatException;

public final class TestUtilities
{
    public static void importTestBoard(String fileName, Puzzle puzzle) throws InvalidFileFormatException
    {
        puzzle.importPuzzle(ClassLoader.getSystemResourceAsStream(fileName));
        Tree tree = puzzle.getTree();
        TreeNode rootNode = tree.getRootNode();
        Board board = rootNode.getBoard().copy();
        TreeTransition transition = new TreeTransition(rootNode, board);
        rootNode.getChildren().add(transition);
    }
}
