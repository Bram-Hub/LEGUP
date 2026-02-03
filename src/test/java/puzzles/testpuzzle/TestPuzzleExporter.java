package puzzles.testpuzzle;

import edu.rpi.legup.model.PuzzleExporter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TestPuzzleExporter extends PuzzleExporter {
    public TestPuzzleExporter(TestPuzzle puzzle) {super(puzzle);}

    @Override
    protected Element createBoardElement(Document newDocument) {
        return null;
    }
}
