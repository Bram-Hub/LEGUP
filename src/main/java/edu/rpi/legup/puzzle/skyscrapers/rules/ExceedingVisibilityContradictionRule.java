package edu.rpi.legup.puzzle.skyscrapers.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersBoard;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersCell;
import edu.rpi.legup.puzzle.skyscrapers.SkyscrapersType;

import java.awt.*;
import java.util.HashSet;
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
		//why is this called for every cell?
    	SkyscrapersCell cell = (SkyscrapersCell) puzzleElement;
        SkyscrapersBoard skyscrapersboard = (SkyscrapersBoard) board;
        Point loc = cell.getLocation();
        
        //get borders
        int west  = skyscrapersboard.getRow().get(loc.y).getData();
    	int east  = skyscrapersboard.getRowClues().get(loc.y).getData();
    	int north  = skyscrapersboard.getCol().get(loc.x).getData();
    	int south  = skyscrapersboard.getColClues().get(loc.x).getData();

		//check row
		//from west border
    	int max = 0;
    	int count = 0;
    	boolean complete = true;
    	for (int i = 0; i < skyscrapersboard.getWidth(); i++) {
        	SkyscrapersCell c = skyscrapersboard.getCell(i, loc.y);
            if (c.getType() == SkyscrapersType.Number && c.getData() > max) {
            	//System.out.print(c.getData());
            	//System.out.println(cell.getData());
                max = c.getData();
                count++;
            }
            if (c.getType() == SkyscrapersType.UNKNOWN) {
            	complete = false;
				break;
            }
        }
    	if (count > west && complete) {
    		return null;
    	}

		if(complete) {
			//from east border
			max = 0;
			count = 0;
			for (int i = skyscrapersboard.getWidth() - 1; i >= 0; i--) {
				SkyscrapersCell c = skyscrapersboard.getCell(i, loc.y);
				if (c.getType() == SkyscrapersType.Number && c.getData() > max) {
					//System.out.print(c.getData());
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
		//from north border
    	max = 0;
    	count = 0;
    	complete = true;
        for (int i = 0; i < skyscrapersboard.getHeight(); i++) {
        	SkyscrapersCell c = skyscrapersboard.getCell(loc.x, i);
        	if (c.getType() == SkyscrapersType.Number && c.getData() > max) {
        		//System.out.print(c.getData());
            	//System.out.println(cell.getData());
        		max = c.getData();
                count++;
            }
        	if (c.getType() == SkyscrapersType.UNKNOWN) {
            	complete = false;
				break;
            }
        }
        if (count > north && complete) {
    		return null;
    	}

		if(complete) {
			//from south border
			max = 0;
			count = 0;
			for (int i = skyscrapersboard.getHeight() - 1; i >= 0; i--) {
				SkyscrapersCell c = skyscrapersboard.getCell(loc.x, i);
				if (c.getType() == SkyscrapersType.Number && c.getData() > max) {
					//System.out.print(c.getData());
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
