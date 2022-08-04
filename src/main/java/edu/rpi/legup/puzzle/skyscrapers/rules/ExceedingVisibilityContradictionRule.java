package edu.rpi.legup.puzzle.skyscrapers.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersBoard;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersCell;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersType;

import java.awt.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ExceedingVisibilityContradictionRule extends ContradictionRule {

    public ExceedingVisibilityContradictionRule() {
        super("SKYS-CONT-0002", "Exceeding Visibility",
                "More skyscrapers are visible than there should be.",
                "edu/rpi/legup/images/skyscrapers/contradictions/ExceedingVisibility.png");
    }

    /**
     * Checks whether the transition has a contradiction at the specific puzzleElement index using this rule
     *
     * @param board         board to check contradiction
     * @param puzzleElement equivalent puzzleElement
     * @return null if the transition contains a contradiction at the specified puzzleElement,
     * otherwise error message
     */
    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
		//why is this called for every cell? maybe:override checkcontradiction
    	SkyscrapersCell cell = (SkyscrapersCell) puzzleElement;
        SkyscrapersBoard skyscrapersboard = (SkyscrapersBoard) board;
        Point loc = cell.getLocation();
        
        //get borders
        int west  = skyscrapersboard.getRow().get(loc.y).getData();
    	int east  = skyscrapersboard.getRowClues().get(loc.y).getData();
    	int north  = skyscrapersboard.getCol().get(loc.x).getData();
    	int south  = skyscrapersboard.getColClues().get(loc.x).getData();

		//check row
    	int max = 0;
    	int count = 0;
		List<SkyscrapersCell> row = skyscrapersboard.getRowCol(loc.y,SkyscrapersType.Number,true);
		if(row.size()==skyscrapersboard.getWidth()){
			//from west border
			for(SkyscrapersCell c : row){
				if (c.getData() > max) {
					System.out.print(c.getData());
					//System.out.println(cell.getData());
					max = c.getData();
					count++;
				}
			}
			if (count > west) {
				return null;
			}

			max = 0;
			count = 0;
			//from east border
			Collections.reverse(row);
			for(SkyscrapersCell c : row){
				if (c.getData() > max) {
					System.out.print(c.getData());
					//System.out.println(cell.getData());
					max = c.getData();
					count++;
				}
			}
			if (count > east) {
				return null;
			}
		}
        
        //check column
		List<SkyscrapersCell> col = skyscrapersboard.getRowCol(loc.x,SkyscrapersType.Number,false);
		if(col.size()==skyscrapersboard.getHeight()){
			//from north border
			max = 0;
			count = 0;
			for(SkyscrapersCell c : col){
				System.out.println(c.getData());
				if (c.getData() > max) {

					//System.out.println(cell.getData());
					max = c.getData();
					count++;
				}
			}
			if (count > north) {
				return null;
			}

			//from south border
			max = 0;
			count = 0;
			Collections.reverse(col);
			for(SkyscrapersCell c : col){
				System.out.println(c.getData());
				if (c.getData() > max) {

					//System.out.println(cell.getData());
					max = c.getData();
					count++;
				}
			}
			if (count > south) {
				return null;
			}
		}
        
        //System.out.print("Does not contain a contradiction at this index");
        return super.getNoContradictionMessage();
    }
}
