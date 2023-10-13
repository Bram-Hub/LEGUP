package puzzles.shorttruthtable.rules;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTable;
import edu.rpi.legup.puzzle.shorttruthtable.rules.basic.DirectRuleAtomic;
import legup.MockGameBoardFacade;
import org.junit.BeforeClass;

class AtomicDirectRuleTest {
    private static final DirectRuleAtomic RULE = new DirectRuleAtomic();
    private static ShortTruthTable stt;

    @BeforeClass
    public static void setup()
    {
        MockGameBoardFacade.getInstance();
        stt = new ShortTruthTable();
    }
}