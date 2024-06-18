package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.ui.boardview.GridBoardView;
import java.awt.*;

public class ThermometerView extends GridBoardView {

    public ThermometerView(ThermometerBoard board) {
        super(new BoardController(), new ThermometerController(), board.getDimension());

        // loop for displaying the vial cells
        // stolen largely from dev guide
        for (ThermometerVial vial : board.getVials()) {
            for (ThermometerCell cell : vial.getCells()) {
                Point loc = cell.getLocation();
                ThermometerElementView elementView = new ThermometerElementView(cell);
                elementView.setIndex(cell.getIndex());
                elementView.setSize(elementSize);
                elementView.setLocation(
                        new Point(loc.x * elementSize.width, loc.y * elementSize.height));
                elementViews.add(elementView);
            }
        }

        // loop for displaying row numbers, same as above
        for (ThermometerCell rowNum : board.getRowNumbers()) {
            Point loc = rowNum.getLocation();
            ThermometerNumberView numberView = new ThermometerNumberView(rowNum);
            numberView.setIndex(rowNum.getIndex());
            numberView.setSize(elementSize);
            numberView.setLocation(
                    new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(numberView);
        }

        // loop for displaying col numbers, also same as above
        for (ThermometerCell colNum : board.getColNumbers()) {
            Point loc = colNum.getLocation();
            ThermometerNumberView numberView = new ThermometerNumberView(colNum);
            numberView.setIndex(colNum.getIndex());
            numberView.setSize(elementSize);
            numberView.setLocation(
                    new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(numberView);
        }
    }
}
