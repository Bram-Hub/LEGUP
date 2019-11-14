package rpi.legup.puzzle.shorttruthtable;

import edu.rpi.legup.model.gameboard.GridCell;

public class ShortTruthTableCell extends GridCell<ShortTruthTableCellType>{
	
	private final char symbol;

	//Constructors

	public ShortTruthTableCell(char symbol, ShortTruthTableCellType cellType, Point location){
		super(cellType, location);
		this.symbol = symbol;
	}

    public ShortTruthTableCell(char symbol, Point location){
        this(symbol, ShortTruthTableCellType.getType(symbol), location);
    }

    public ShortTruthTableCell(char symbol){
        this(symbol, null);
    }




    //Getters

    public ShortTruthTableCellType getType() {
        return data;
    }

    public char getSymbol(){
    	return symbol;
    }

    /**
     * Returns true if this cell is a letter; false if it is an operation, paren, unused, etc
     *
     * @return true if cell is a variable; false otherwise
     */
    public boolean isVariable(){ return Character.isLetter(symbol); }









    //Modifiers

    public void cycleTypeForward(){
        switch(data){
            case ShortTruthTableCellType.UNKNOWN: data = ShortTruthTableCellType.TRUE; break;
            case ShortTruthTableCellType.TRUE: data = ShortTruthTableCellType.FALSE; break;
            case ShortTruthTableCellType.FALSE: data = ShortTruthTableCellType.UNKNOWN; break;
            default: break;
        }
    }

    public void cycleTypeBackward(){
        cycleTypeForward();
        cycleTypeForward();
    }








    //Copy function

    @Override
    public ShortTruthTableCell copy() {
        ShortTruthTableCell copy = new ShortTruthTableCell(data, (Point) location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }

}