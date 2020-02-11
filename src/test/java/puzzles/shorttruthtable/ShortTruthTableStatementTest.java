package java.puzzles.shorttruthtable;

import edu.rpi.legup.puzzle.shorttruthtable.ShortTruthTableStatement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShortTruthTableStatementTest{

    @Test
    public void testCreateStatement(){
        ShortTruthTableStatement statement = new ShortTruthTableStatement("a^b");

        assertEquals(statement.getLength(), 3);
    }

}