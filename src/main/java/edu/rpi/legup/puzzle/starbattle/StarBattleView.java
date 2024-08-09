package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.ElementView;
import edu.rpi.legup.ui.boardview.GridBoardView;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class StarBattleView extends GridBoardView {
    static Image STAR;
    private ArrayList<ArrayList<Boolean>> horizontalBorders;    //board.size * board.size+1     left-right up-down
    private ArrayList<ArrayList<Boolean>> verticalBorders;      //board.size+1 * board.size     left-right up-down

    static {
        try {
            STAR =
                    ImageIO.read(
                            ClassLoader.getSystemClassLoader()
                                    .getResource("edu/rpi/legup/images/starbattle/star.gif"));
        } catch (IOException e) {
            // pass
        }
    }

    public StarBattleView(StarBattleBoard board) {
        super(new BoardController(), new StarBattleController(), board.getDimension());
        this.horizontalBorders = new ArrayList<>();
        this.verticalBorders = new ArrayList<>();

        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            StarBattleCell cell = (StarBattleCell) puzzleElement;
            Point loc = cell.getLocation();
            StarBattleElementView elementView = new StarBattleElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(
                    new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(elementView);
        }

        //initialize horizontal borders, the ones that are between two cells along the y-axis, and look like -- not |
        for(int i = 0; i < board.getWidth(); i++){
            ArrayList<Boolean> temp = new ArrayList<>();
            for(int j = 0; j < board.getHeight() + 1; j++){     //+1 to account for sides of board
                if(j == 0 || j == board.getHeight()){       //set borders at the ends of the board
                    temp.add(Boolean.TRUE);
                }
                else if(board.getCell(i, j-1).getGroupIndex() != board.getCell(i, j).getGroupIndex()){       //general case
                    temp.add(Boolean.TRUE);         //adds border when two adjacent cells aren't from the same region
                }
                else{
                    temp.add(Boolean.FALSE);
                }
            }
            horizontalBorders.add(temp);
        }
        //initialize vertical borders, the ones that are between two cells along the x-axis, and look like | not --
        //largely the same code as horizontal border adder but i and j are flipped and general case checks cells adjacent
        //along i (x) instead of j (y)
        for(int j = 0; j < board.getHeight(); j++){         //initialize j (y) first since we're checking the opposite axis
            ArrayList<Boolean> temp = new ArrayList<>();
            for(int i = 0; i < board.getHeight() + 1; i++){     //+1 to account for sides of board
                if(i == 0 || i == board.getWidth()){       //set borders at the ends of the board
                    temp.add(Boolean.TRUE);
                }
                else if(board.getCell(i-1, j).getGroupIndex() != board.getCell(i, j).getGroupIndex()){       //general case
                    temp.add(Boolean.TRUE);         //adds border when two adjacent cells aren't from the same region
                }
                else{
                    temp.add(Boolean.FALSE);
                }
            }
            verticalBorders.add(temp);
        }
    }
}
