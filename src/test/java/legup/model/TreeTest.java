package legup.model;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.GridBoard;
import edu.rpi.legup.model.tree.Tree;
import org.junit.BeforeClass;
import org.junit.Test;

public class TreeTest {
    public static Tree tree;

    @BeforeClass
    public static void setUp() {
        Board board = new GridBoard(5, 5);
        tree = new Tree(board);
    }

    @Test
    public void testTree() {}
}
