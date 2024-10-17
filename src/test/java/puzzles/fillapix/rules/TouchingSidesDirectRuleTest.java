package puzzles.fillapix.rules;

import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;
import edu.rpi.legup.puzzle.fillapix.Fillapix;
import edu.rpi.legup.puzzle.fillapix.FillapixBoard;
import edu.rpi.legup.puzzle.fillapix.rules.TouchingSidesDirectRule;
import edu.rpi.legup.puzzle.lightup.LightUp;
import edu.rpi.legup.save.InvalidFileFormatException;
import java.awt.*;
import legup.MockGameBoardFacade;
import legup.TestUtilities;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TouchingSidesDirectRuleTest {
    private static final TouchingSidesDirectRule RULE =
            new TouchingSidesDirectRule();
    private static Fillapix fillapix;

    @BeforeClass
    public static void setUp() {
        fillapix = new Fillapix();
    }
}
