package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.GridBoardView;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class StarBattleView extends GridBoardView {
    static Image STAR;
    private ArrayList<StarBattleBorderView> horizontalBorders;    //board.size * board.size+1     left-right up-down
    private ArrayList<StarBattleBorderView> verticalBorders;      //board.size+1 * board.size     left-right up-down

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

        //Make borders by just making a list of border objects and saving their locations as they come
        //then just draw all of them one at a time

        //initialize horizontal borders, the ones that are between two cells along the y-axis, and look like -- not |
        for(int i = 0; i < board.getWidth(); i++){
            for(int j = 0; j < board.getHeight() + 1; j++){     //+1 to account for sides of board
                StarBattleBorderView temp = new StarBattleBorderView(new StarBattleBorder(StarBattleCellType.HORIZ_BORDER));
                temp.setSize(elementSize);
                if(j == 0){       //set borders at the ends of the board
                    //set on top of cell
                    temp.setLocation(endCell(board.getCell(i,0), 8, elementSize));
                    horizontalBorders.add(temp);
                }
                else if(j == board.getHeight()){
                    temp.setLocation(endCell(board.getCell(i,board.getHeight()-1), 2, elementSize));
                    horizontalBorders.add(temp);
                }
                else if(board.getCell(i, j-1).getGroupIndex() != board.getCell(i, j).getGroupIndex()){       //general case
                             //adds border when two adjacent cells aren't from the same region
                    temp.setLocation(endCell(board.getCell(i,j), 8, elementSize));
                    horizontalBorders.add(temp);
                }
                //no else statement. If none of these ifs are met, then just don't add it to the list
            }
        }
        //initialize vertical borders, the ones that are between two cells along the x-axis, and look like | not --
        //largely the same code as horizontal border adder but i and j are flipped and general case checks cells adjacent
        //along i (x) instead of j (y)
        for(int j = 0; j < board.getHeight(); j++){         //initialize j (y) first since we're checking the opposite axis
            for(int i = 0; i < board.getHeight() + 1; i++){     //+1 to account for sides of board
                StarBattleBorderView temp = new StarBattleBorderView(new StarBattleBorder(StarBattleCellType.VERT_BORDER));
                temp.setSize(elementSize);
                if(i == 0){       //set borders at the ends of the board
                    temp.setLocation(endCell(board.getCell(0,j), 4, elementSize));
                    verticalBorders.add(temp);
                }
                else if(i == board.getWidth()){
                    temp.setLocation(endCell(board.getCell(board.getWidth() - 1,j), 6, elementSize));
                    verticalBorders.add(temp);
                }
                else if(board.getCell(i-1, j).getGroupIndex() != board.getCell(i, j).getGroupIndex()){       //general case
                    //adds border when two adjacent cells aren't from the same region
                    temp.setLocation(endCell(board.getCell(i,j), 4, elementSize));
                    verticalBorders.add(temp);
                }
            }
        }
    }

    //Direction means which side of the cell in question should have a border on it
    //Numpad rules. 2 is down, 4 is left, 6 is right, 8 is up (based on visuals not coordinates, since y is from up to down)
    public Point endCell(StarBattleCell one, int direction, Dimension elementSize){
        Point temp = new Point(
                one.getLocation().x * elementSize.width,
                one.getLocation().y * elementSize.height
        );//dump this
        //System.out.println("point is " + temp.x + "," + temp.y + "\n");
        if(direction == 2){
            temp.y += elementSize.height * 15/16; //multiply  so it doesn't go off screen
        }
        if(direction == 4){
            temp.x -= elementSize.width / 16;   //divide  so it doesn't go off screen
        }
        if(direction == 6){
            temp.x += elementSize.width * 15/16; //multiply  so it doesn't go off screen
        }if(direction == 8){
            temp.y -= elementSize.height / 16;   //multiply  so it doesn't go off screen
        }
        //System.out.println("point is now " + temp.x + "," + temp.y + "\n");
        return temp;
    }
    /*  shelved for redundancy
    //finds average point location between two cells
    public Point betweenCells(StarBattleCell one, StarBattleCell two, Dimension elementSize){
        //dump this
        System.out.println("point between is " + Math.max(one.getLocation().x,two.getLocation().x) + "," + Math.max(one.getLocation().y,two.getLocation().y) + "\n");
        if(one.getLocation().x < two.getLocation().x){
            return endCell(one, 6, elementSize);
        }
        else if(one.getLocation().y < two.getLocation().y){
            return endCell(one, 6, elementSize);
        }
    }   */

    @Override
    public void drawBoard(Graphics2D graphics2D){
        super.drawBoard(graphics2D);

        for(StarBattleBorderView border: horizontalBorders){
            //draw a horizontal line
            border.draw(graphics2D);
        }

        for(StarBattleBorderView border: verticalBorders){
            //draw a vertical line
            border.draw(graphics2D);
        }
    }
}
