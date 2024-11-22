package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.model.Puzzle;

public class YinYang extends Puzzle {
    public YinYang() {
        super("Yin Yang");

        // Initialize components for the puzzle
        setBoardFactory(new YinYangCellFactory());
        setImporter(new YinYangImporter(this));
        setExporter(new YinYangExporter(this));
        setUtilities(new YinYangUtilities());
    }
}
