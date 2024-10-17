package puzzles.fillapix.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.fillapix.Fillapix;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;
import edu.rpi.legup.puzzle.fillapix.rules.TouchingCornersDirectRule;
import edu.rpi.legup.puzzle.fillapix.Fillapix;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TouchingCornersDirectRuleTest{
    private static final TouchingCornersDirectRule RULE =
            new TouchingCornersDirectRule();
    private static Fillapix fillapix;

    @BeforeClass
    public static void setUp() {
        fillapix = new Fillapix();
    }
}
