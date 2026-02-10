package edu.rpi.legup.puzzle.shorttruthtable;

import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.GridCell;
import edu.rpi.legup.puzzle.battleship.BattleshipType;

import java.awt.Point;
import java.awt.event.MouseEvent;

public class ShortTruthTableCell extends GridCell<ShortTruthTableCellType> {

    // The symbol on the cell
    private char symbol;

    // This is a reference to the statement that contains this cell
    private ShortTruthTableStatement statement;

    // Constructors

    public ShortTruthTableCell(char symbol, ShortTruthTableCellType cellType, Point location) {
        super(cellType, location);
        this.symbol = symbol;
    }

    /**
     * Constructs a new NOT_IN_PLAY Cell
     *
     * @param location the location of this cell on the board
     */
    public ShortTruthTableCell(Point location) {
        this(' ', ShortTruthTableCellType.NOT_IN_PLAY, location);
    }

    // Getters

    public ShortTruthTableStatement getStatementReference() {
        return statement;
    }

    public ShortTruthTableCellType getType() {
        return data;
    }

    public char getSymbol() {
        return symbol;
    }

    /**
     * Returns true if this cell is a letter; false if it is an operation, paren, unused, etc
     *
     * @return true if cell is a variable; false otherwise
     */
    public boolean isVariable() {
        return Character.isLetter(symbol);
    }

    public int getX() {
        return (int) location.getX();
    }

    public int getY() {
        return (int) location.getY();
    }

    public boolean isAssigned() {
        return getType() == ShortTruthTableCellType.TRUE
                || getType() == ShortTruthTableCellType.FALSE;
    }

    // Setters

    void setStatementReference(ShortTruthTableStatement statement) {
        this.statement = statement;
    }

    public void setType(ShortTruthTableCellType type) {
        data = type;
    }

    public void setGiven(ShortTruthTableCellType type) {
        setType(type);
        setModifiable(false);
        setGiven(true);
    }

    // Modifiers

    public void cycleTypeForward() {
        switch (data) {
            case UNKNOWN:
                data = ShortTruthTableCellType.TRUE;
                break;
            case TRUE:
                data = ShortTruthTableCellType.FALSE;
                break;
            case FALSE:
                data = ShortTruthTableCellType.UNKNOWN;
                break;
            default:
                break;
        }
    }

    public void cycleTypeBackward() {
        cycleTypeForward();
        cycleTypeForward();
    }

    // TO STRING

    @Override
    public String toString() {
        return String.format("STTCell: %c %2d %-11s %s", symbol, index, data, location.toString());
    }

    // Copy function

    @Override
    public ShortTruthTableCell copy() {
        ShortTruthTableCell copy = new ShortTruthTableCell(symbol, data, (Point) location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        copy.setGoal(isGoal);
        return copy;
    }

    /**
     * Sets the type of this ShortTruthTableCell
     *
     * @param e element to set the type of this Short Truth Table cell to
     */
    @Override
    public void setType(Element e, MouseEvent m) {
        // Do not allow odd rows to be modified since they are spacer rows
        if (this.getLocation().getY() % 2 == 1) {
            return;
        }

        // Red Element
        if (e.getElementID().equals("STTT-ELEM-0004")) {
            this.data = ShortTruthTableCellType.FALSE;
        }
        // Green Element
        else {
            if (e.getElementID().equals("STTT-ELEM-0002")) {
                this.data = ShortTruthTableCellType.TRUE;
            }
            // Unknown Element
            else {
                if (e.getElementID().equals("STTT-ELEM-0005")) {
                    this.data = ShortTruthTableCellType.UNKNOWN;
                }
                // Argument Element
                else {
                    if (e.getElementID().equals("STTT-ELEM-0001")) {
                        // Prevents non-argument symbols from being changed
                        if (!(this.symbol >= 'A' && this.symbol <= 'Z')) {
                            return;
                        }

                        if (m.getButton() == MouseEvent.BUTTON1) {
                            this.symbol += 1;
                            if (this.symbol > 'Z') {
                                this.symbol = 'A';
                            }
                        } else {
                            if (m.getButton() == MouseEvent.BUTTON3) {
                                this.symbol -= 1;
                                if (this.symbol < 'A') {
                                    this.symbol = 'Z';
                                }
                            }
                        }
                    }
                    // And/Or Element
                    else {
                        if (e.getElementID().equals("STTT-ELEM-0003")) {
                            if (m.getButton() == MouseEvent.BUTTON1) {
                                if (this.symbol == '^') {
                                    this.symbol = '|';
                                } else {
                                    if (this.symbol == '|') {
                                        this.symbol = '>';
                                    } else {
                                        if (this.symbol == '>') {
                                            this.symbol = '-';
                                        } else {
                                            if (this.symbol == '-') {
                                                this.symbol = '^';
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (m.getButton() == MouseEvent.BUTTON3) {
                                    if (this.symbol == '^') {
                                        this.symbol = '-';
                                    } else {
                                        if (this.symbol == '|') {
                                            this.symbol = '^';
                                        } else {
                                            if (this.symbol == '>') {
                                                this.symbol = '|';
                                            } else {
                                                if (this.symbol == '-') {
                                                    this.symbol = '>';
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isKnown() {return !(data == ShortTruthTableCellType.UNKNOWN);}
}
