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
    private ArrayList<StarBattleBorderView>
            horizontalBorders; // board.size * board.size+1     left-right up-down
    private ArrayList<StarBattleBorderView>
            verticalBorders; // board.size+1 * board.size     left-right up-down

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

        // Make borders by just making a list of border objects and saving their locations as they
        // come
        // then just draw all of them one at a time

        // initialize horizontal borders, the ones that are between two cells along the y-axis, and
        // look like -- not |
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight() + 1; j++) { // +1 to account for sides of board
                StarBattleBorderView temp =
                        new StarBattleBorderView(
                                new StarBattleBorder(StarBattleCellType.HORIZ_BORDER));
                temp.setSize(elementSize);
                if (j == board.getHeight()) { // set borders at the ends of the board
                    Point cellLoc = board.getCell(i, j - 1).getLocation();
                    temp.setLocation(new Point(
                            cellLoc.x * elementSize.width, (cellLoc.y + 1) * elementSize.height));
                    horizontalBorders.add(temp);
                } else if (j == 0 ||
                        board.getCell(i, j - 1).getGroupIndex() != board.getCell(i, j).getGroupIndex()) {
                    // general case: adds border when two adjacent cells aren't from the same region
                    Point cellLoc = board.getCell(i, j).getLocation();
                    temp.setLocation(new Point(
                            cellLoc.x * elementSize.width, cellLoc.y * elementSize.height));
                    horizontalBorders.add(temp);
                }
                // no else statement. If none of these ifs are met, then just don't add it to the
                // list
            }
        }
        // initialize vertical borders, the ones that are between two cells along the x-axis, and
        // look like | not --
        // largely the same code as horizontal border adder but i and j are flipped and general case
        // checks cells adjacent
        // along i (x) instead of j (y)
        for (int j = 0;
                j < board.getHeight();
                j++) { // initialize j (y) first since we're checking the opposite axis
            for (int i = 0; i < board.getHeight() + 1; i++) { // +1 to account for sides of board
                StarBattleBorderView temp =
                        new StarBattleBorderView(
                                new StarBattleBorder(StarBattleCellType.VERT_BORDER));
                temp.setSize(elementSize);
                if (i == board.getWidth()) { // set borders at the ends of the board
                    Point cellLoc = board.getCell(i - 1, j).getLocation();
                    temp.setLocation(new Point(
                            (cellLoc.x + 1) * elementSize.width, cellLoc.y * elementSize.height));
                    horizontalBorders.add(temp);
                } else if (i == 0 ||
                        board.getCell(i - 1, j).getGroupIndex() != board.getCell(i, j).getGroupIndex()) {
                    // general case: adds border when two adjacent cells aren't from the same region
                    Point cellLoc = board.getCell(i, j).getLocation();
                    temp.setLocation(new Point(
                            cellLoc.x * elementSize.width, cellLoc.y * elementSize.height));
                    horizontalBorders.add(temp);
                }
            }
        }
    }

    // Makes border visual overflow tiles
    @Override
    protected Dimension getProperSize() {
        Dimension boardViewSize = new Dimension();
        boardViewSize.width = (gridSize.width + 2) * elementSize.width;
        boardViewSize.height = (gridSize.height + 2) * elementSize.height;
        return boardViewSize;
    }


    @Override
    public void drawBoard(Graphics2D graphics2D) {
        super.drawBoard(graphics2D);

        for (StarBattleBorderView border : horizontalBorders) {
            // draw a horizontal line
            border.draw(graphics2D);
        }

        for (StarBattleBorderView border : verticalBorders) {
            // draw a vertical line
            border.draw(graphics2D);
        }
        // testing how to draw things off the board
        // StarBattleCell test = new StarBattleCell(0, new Point(-10,-10), -1, 30);
    }
}
