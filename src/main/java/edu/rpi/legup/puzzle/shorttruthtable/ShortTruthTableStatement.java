package edu.rpi.legup.puzzle.shorttruthtable;



import edu.rpi.legup.model.gameboard.PuzzleElement;

import java.util.Set;
import java.util.HashSet;
import java.awt.Point;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;


public class ShortTruthTableStatement extends PuzzleElement<String>{

	//the cell that this statement holds
	private final ShortTruthTableCell cell;

	//child nodes of the tree
	private final ShortTruthTableStatement leftStatement;
	private final ShortTruthTableStatement rightStatement;

	//the representation string for this statement
	private final String stringRep;
	private final List<ShortTruthTableCell> cells;

	//each of the logic symbols
	public static final char NOT = '~';
	public static final char AND = '^';
	public static final char OR = '|';
	public static final char CONDITIONAL = '>';
	public static final char BICONDITIONAL = '-';
	private static final Map<Character, String> logicSymbolMap;
	static {
		Map<Character, String> aMap = new TreeMap();
		aMap.put(NOT, "¬");
		aMap.put(AND, "∧");
		aMap.put(OR, "∨");
		aMap.put(CONDITIONAL, "→");
		aMap.put(BICONDITIONAL, "↔");
		logicSymbolMap = Collections.unmodifiableMap(aMap);
	}

	public static String getLogicSymbol(char c){
		String s = logicSymbolMap.get(c);
		if(s == null) return String.valueOf(c);
		return s;
	}


	//recursive constructor; constructs child statement nodes if necessary
	public ShortTruthTableStatement(String statement, List<ShortTruthTableCell> cells){


		//set the string rep to the statement (include parens incase this is a sub statement)
		this.stringRep = statement;
		this.cells = new ArrayList<ShortTruthTableCell>(cells);;

		//remove the parens for parsing the statement
		statement = removeParens(statement);
		removeParens(cells);

		//get the index of the char that this statement represents
		int index = parse(statement);

		//construct the cell for this node in the tree
		cell = cells.get(index);

		//get the strings on either side of this char in the string rep
		String left = statement.substring(0, index);
		String right = statement.substring(index+1);

		List<ShortTruthTableCell> leftCells = new ArrayList<ShortTruthTableCell>(cells.subList(0, index));
		List<ShortTruthTableCell> rightCells = new ArrayList<ShortTruthTableCell>(cells.subList(index+1, cells.size()));

		//cunstruct substatements if necessary
		if(left.length() > 0)
			leftStatement = new ShortTruthTableStatement(left, leftCells);
		else
			leftStatement = null;

		if(right.length() > 0) {
			rightStatement = new ShortTruthTableStatement(right, rightCells);
		}else
			rightStatement = null;

	}



	//parsing for the constructor
	static String removeParens(String statement){

		if(statement.charAt(0) != '(')
			return statement;

		//if the statement does start with a paren, check that it matches with the last paren
		int openParenCount = 1;
		int i = 1;
		while(i < statement.length()-1){
			char c = statement.charAt(i);
			if(c == '(') openParenCount++;
			else if(c == ')') openParenCount--;

			//if the first paren has been closed and it is not the end of the string,
			//then there is no whole statement parens to remove
			if(openParenCount == 0 && i!=statement.length()-1)
				return statement;

			i++;
		}
		//if the while loop made it through the entire statement, there are parens around the whole thing
		return statement.substring(1, statement.length()-1);

	}

	int parse(String statement){


		//Split by and, or, CONDITIONAL, or biconditional
		//keep track of the parens, it must be equal to zero to split
		int openParenCount = 0;
		//index for stepping through the string
		int i = 0;
		//step through each char in the statement
		while(i < statement.length()){
			//get the char
			char c = statement.charAt(i);
			//keep track of the open parens
			if(c == '(') openParenCount++;
			else if(c == ')') openParenCount--;
			//if the char is an operator, and there are no open parens, split the statement here
			else if((c==AND || c==OR || c==CONDITIONAL || c==BICONDITIONAL) && openParenCount==0)
				return i;
			//increment the index
			i++;
		}

		//if it made it through the while loop:
		//this is an atomic statement or a negation
		//either way, the important char is the first character in the string
		return 0;

	}

	static void removeParens(List<ShortTruthTableCell> cells){

		if(cells.get(0).getSymbol() != '(')
			return;

		//if the statement does start with a paren, check that it matches with the last paren
		int openParenCount = 1;
		int i = 1;
		while(i < cells.size()-1){
			char c = cells.get(i).getSymbol();
			if(c == '(') openParenCount++;
			else if(c == ')') openParenCount--;

			//if the first paren has been closed and it is not the end of the string,
			//then there is no whole statement parens to remove
			if(openParenCount == 0 && i!=cells.size()-1)
				return;

			i++;
		}

		//if the while loop made it through the entire statement, there are parens around the whole thing
		cells.remove(cells.size()-1);
		cells.remove(0);

	}




	//Getters


	public ShortTruthTableCell getCell() { return cell; }

	public ShortTruthTableStatement getLeftStatement(){ return leftStatement; }
	public ShortTruthTableStatement getRightStatement(){ return rightStatement; }

	@Override
	public String toString(){
		return stringRep;
	}


	/**
	 * Returns the length of the statement in cells. This includes all cells used for parenthesis
	 *
	 * @return the number of cells contained in this statement
	 */
	public int getLength(){
		return stringRep.length();
	}



	//Getters (recursive)

	//returns all cells in this statement with the symbol 'symbol'; runs recursivly on both sides of the tree
	public Set<ShortTruthTableCell> getCellsWithSymbol(char symbol){
		Set<ShortTruthTableCell> set = new HashSet(getLength());
		if(cell.getSymbol() == symbol) set.add(cell);
		if(leftStatement != null) set.addAll(leftStatement.getCellsWithSymbol(symbol));
		if(rightStatement != null) set.addAll(rightStatement.getCellsWithSymbol(symbol));
		return set;
	}

	public Set<ShortTruthTableCell> getAllCells(){
		Set<ShortTruthTableCell> set = new HashSet(getLength());
		set.add(cell);
		if(leftStatement != null) set.addAll(leftStatement.getAllCells());
		if(rightStatement != null) set.addAll(rightStatement.getAllCells());
		return set;
	}


	//Setters

	private void setCellLocations(int rowIndex, int offset){
		//set the location of this cell
		int xLoc = offset;
		if(leftStatement != null)
			xLoc += leftStatement.getLength();
		cell.setLocation(new Point(xLoc, rowIndex));
		//recurse on both sides of the tree
		if(leftStatement != null)
			leftStatement.setCellLocations(rowIndex, offset);
		if(rightStatement != null)
			rightStatement.setCellLocations(rowIndex, xLoc+1);
	}

	public void setCellLocations(int rowIndex){
		setCellLocations(rowIndex, 0);
	}





	public ShortTruthTableStatement copy(){
		return new ShortTruthTableStatement(stringRep, cells);
	}

}