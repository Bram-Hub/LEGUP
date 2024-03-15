package edu.rpi.legup.puzzle.thermometer.rules;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.rules.ContradictionRule;
import edu.rpi.legup.puzzle.thermometer.ThermometerBoard;
import edu.rpi.legup.puzzle.thermometer.ThermometerCell;
import edu.rpi.legup.puzzle.thermometer.ThermometerVial;

import java.util.ArrayList;

public class DiscontinuousMercuryContradictionRule extends ContradictionRule{

    private final String NO_CONTRADICTION_MESSAGE = "Does not contain a contradiction at this index";
    private final String INVALID_USE_MESSAGE = "Contradiction must be a vial";

    public DiscontinuousMercuryContradictionRule() {
        super("THERM-CONT-0003",
                "Discontinuous Mercury",
                "A vial has a filled cell after an empty or blocked cell",
                "edu/rpi/legup/images/thermometer/MercuryInBody.png");
    }


    //User can click on any cell in a vial with a discontinuous flow
    @Override
    public String checkContradictionAt(Board board, PuzzleElement puzzleElement) {
        //useful variables
        ThermometerBoard thermometerBoard = (ThermometerBoard) board;

        ThermometerCell cell = (ThermometerCell) thermometerBoard.getPuzzleElement(puzzleElement);

        ArrayList<ThermometerVial> thermometerVials =  thermometerBoard.getVials();

        //finding out which vial contains the specified cell
        for (int i = 0; i < thermometerVials.size(); i++) {
            ThermometerVial thermometerVial = thermometerVials.get(i);
            //if a vial contains the clicked on cell
            //checking if the vial has a break in the flow
            if(thermometerVial.containsCell(cell)){
                if(thermometerVial.continuousFlow()){
                    return super.getNoContradictionMessage() + ": " + this.NO_CONTRADICTION_MESSAGE;
                }
                else{
                    return null;
                }
            }
        }

        //if none of the vials contain the clicked on cell yell at user
        return super.getInvalidUseOfRuleMessage() + ": " + this.INVALID_USE_MESSAGE;
    }
}
