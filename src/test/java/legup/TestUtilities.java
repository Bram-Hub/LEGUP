package legup;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.Tree;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.save.InvalidFileFormatException;

import java.io.InputStream;

public final class TestUtilities {
    public static void importTestBoard(String fileName, Puzzle puzzle)
            throws InvalidFileFormatException {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream cannot be null. File not found: " + fileName);
        }

        puzzle.importPuzzle(inputStream);
        Tree tree = puzzle.getTree();
        TreeNode rootNode = tree.getRootNode();
        Board board = rootNode.getBoard().copy();
        TreeTransition transition = new TreeTransition(rootNode, board);
        rootNode.getChildren().add(transition);
    }
}
