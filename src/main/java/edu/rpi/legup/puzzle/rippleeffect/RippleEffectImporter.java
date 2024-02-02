package edu.rpi.legup.puzzle.rippleeffect;

import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RippleEffectImporter extends PuzzleImporter {
    public RippleEffectImporter(RippleEffect rippleEffect) {
        super(rippleEffect);
    }

    
    /**
     * Puzzle setting to support row and column inputs
     */
    @Override
    public boolean acceptsRowsAndColumnsInput() {
        return true;
    }

    /**
     * Puzzle setting to disable support for text input
     */
    @Override
    public boolean acceptsTextInput() {
        return false;
    }


    /**
     * Constructs empty RippleEffect gameboard as per the provided dimensions
     * @param rows number of rows for the gameboard
     * @param columns number of columns for the gameboard
     */
    @Override
    public void initializeBoard(int rows, int columns) {
        // Womp
    }



    /**
     * Constructs RippleEffect gameboard
     * @param node xml document node
     * @throws InvalidFileFormatException if file is invalid
     */
    @Override
    public void initializeBoard(Node node) throws InvalidFileFormatException {
        if (node == null) throw new InvalidFileFormatException("Invalid format");
    }



    /**
     * Initialize board via string of statements.
     * @throws UnsupportedOperationException since RippleEffect does not support text input
     */
    @Override
    public void initializeBoard(String[] statements) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Ripple Effect does not accept text input");
    }
}
