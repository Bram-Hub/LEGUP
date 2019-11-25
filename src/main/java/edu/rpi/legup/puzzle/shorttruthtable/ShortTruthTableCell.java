package edu.rpi.legup.puzzle.shorttruthtable;

import edu.rpi.legup.model.gameboard.GridCell;

import java.awt.Point;

public class ShortTruthTableCell extends GridCell<ShortTruthTableCellType>{
	
	private final char symbol;

	//Constructors

	public ShortTruthTableCell(char symbol, ShortTruthTableCellType cellType, Point location){
		super(cellType, location);
		this.symbol = symbol;
	}


    /**
     * Constructs a new Cell and calculates the cell type based off of 'symbol'.
     * The cell type will be either UNKNOWN  or NOT_IN_PLAY
     *
     * @param symbol
     * @param location
     */
    public ShortTruthTableCell(char symbol, Point location){
        this(symbol, ShortTruthTableCellType.getType(symbol), location);
    }

    public ShortTruthTableCell(char symbol){
        this(symbol, null);
    }

    /**
     * Constructs a new NOT_IN_PLAY Cell
     *
     * @param location the location of this cell on the board
     */
    public ShortTruthTableCell(Point location){
	    this(' ', ShortTruthTableCellType.NOT_IN_PLAY, location);
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




    //Setters







    //Modifiers

    public void cycleTypeForward(){
        switch(data){
            case UNKNOWN: data = ShortTruthTableCellType.TRUE; break;
            case TRUE: data = ShortTruthTableCellType.FALSE; break;
            case FALSE: data = ShortTruthTableCellType.UNKNOWN; break;
            default: break;
        }
    }

    public void cycleTypeBackward(){
        cycleTypeForward();
        cycleTypeForward();
    }





    //TO STRING

    @Override
    public String toString(){
        return "STTCell: "+symbol+" "+location;
    }






    //Copy function

    @Override
    public ShortTruthTableCell copy() {
        ShortTruthTableCell copy = new ShortTruthTableCell(symbol, data, (Point) location.clone());
        copy.setIndex(index);
        copy.setModifiable(isModifiable);
        copy.setGiven(isGiven);
        return copy;
    }

}