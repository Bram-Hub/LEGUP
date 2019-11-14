package edu.rpi.legup.puzzle.shorttruthtable;

public enum ShortTruthTableCellType{
	FALSE(0), TRUE(1), UNKNOWN(-1), NON_VAR(-2);


    public int value;

    ShortTruthTableCellType(int value) {
        this.value = value;
    }

    /**
     * Returns true if this cell holds the value either TRUE or FALSE
     *
     * @return
     */
    public boolean isTrueOrFalse(){
        return value==0 || value==1;
    }

    /**
    * 
    * @param symbol the symbol in the statement
    * @return UNKNOWN or NON_VAR
    */
    public static ShortTruthTableCellType getType(char symbol){
    	if(symbol=='(' || symbol==')')
    		return NON_VAR;
    	return UNKNOWN;
    }

}