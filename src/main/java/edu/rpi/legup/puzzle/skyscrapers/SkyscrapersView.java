package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.tree.TreeElement;
import edu.rpi.legup.ui.puzzleeditorui.boardview.ElementView;
import edu.rpi.legup.ui.puzzleeditorui.boardview.GridBoardView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class SkyscrapersView extends GridBoardView {
    static Image TREE, GRASS, TENT;

    static {
        try {
            TREE = ImageIO.read(ClassLoader.getSystemResourceAsStream("edu/rpi/legup/images/treetent/tree.png"));
            GRASS = ImageIO.read(ClassLoader.getSystemResourceAsStream("edu/rpi/legup/images/treetent/grass.png"));
            TENT = ImageIO.read(ClassLoader.getSystemResourceAsStream("edu/rpi/legup/images/treetent/tent.png"));
        } catch (IOException e) {

        }
    }

    private ArrayList<SkyscrapersLineView> lineViews;

    private ArrayList<SkyscrapersClueView> northClues;
    private ArrayList<SkyscrapersClueView> eastClues;
    private ArrayList<SkyscrapersClueView> southClues;
    private ArrayList<SkyscrapersClueView> westClues;

    public SkyscrapersView(SkyscrapersBoard board) {
        super(new BoardController(), new SkyscrapersController(), board.getDimension());

        this.lineViews = new ArrayList<>();

        this.northClues = new ArrayList<>();
        this.eastClues = new ArrayList<>();
        this.southClues = new ArrayList<>();
        this.westClues = new ArrayList<>();

        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
        	SkyscrapersCell cell = (SkyscrapersCell) puzzleElement;
            Point loc = cell.getLocation();
            SkyscrapersElementView elementView = new SkyscrapersElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(new Point((loc.x + 1) * elementSize.width, (loc.y + 1) * elementSize.height));
            elementViews.add(elementView);
        }

        for (SkyscrapersLine line : board.getLines()) {
        	SkyscrapersLineView lineView = new SkyscrapersLineView(line);
            lineView.setSize(elementSize);
            lineViews.add(lineView);
        }

        for (int i = 0; i < gridSize.height; i++) {
        	//SkyscrapersClueView row = new SkyscrapersClueView(new SkyscrapersClue(i, i, SkyscrapersType.CLUE_WEST));
        	SkyscrapersClueView row = new SkyscrapersClueView(board.getRow().get(i));
            row.setLocation(new Point(0, (i + 1) * elementSize.height));
            row.setSize(elementSize);

            SkyscrapersClueView clue = new SkyscrapersClueView(board.getRowClues().get(i));
            clue.setLocation(new Point((gridSize.height + 1) * elementSize.height, (i + 1) * elementSize.height));
            clue.setSize(elementSize);

            westClues.add(row);
            eastClues.add(clue);
        }

        for (int i = 0; i < gridSize.width; i++) {
        	//SkyscrapersClueView col = new SkyscrapersClueView(new SkyscrapersClue(i, i, SkyscrapersType.CLUE_NORTH));
        	SkyscrapersClueView col = new SkyscrapersClueView(board.getCol().get(i));
            col.setLocation(new Point((i + 1) * elementSize.width, 0));
            col.setSize(elementSize);

            SkyscrapersClueView clue = new SkyscrapersClueView(board.getColClues().get(i));
            clue.setLocation(new Point((i + 1) * elementSize.width, (gridSize.width + 1) * elementSize.width));
            clue.setSize(elementSize);

            northClues.add(col);
            southClues.add(clue);
        }
    }

    /**
     * Gets the ElementView from the location specified or
     * null if one does not exists at that location
     *
     * @param point location on the viewport
     * @return ElementView at the specified location
     */
    @Override
    public ElementView getElement(Point point) {
        Point scaledPoint = new Point((int) Math.round(point.x / getScale()), (int) Math.round(point.y / getScale()));
        for (ElementView element : elementViews) {
            if (element.isWithinBounds(scaledPoint)) {
                return element;
            }
        }
        for (SkyscrapersClueView clueView : northClues) {
            if (clueView.isWithinBounds(scaledPoint)) {
                return clueView;
            }
        }
        for (SkyscrapersClueView clueView : eastClues) {
            if (clueView.isWithinBounds(scaledPoint)) {
                return clueView;
            }
        }
        for (SkyscrapersClueView clueView : southClues) {
            if (clueView.isWithinBounds(scaledPoint)) {
                return clueView;
            }
        }
        for (SkyscrapersClueView clueView : westClues) {
            if (clueView.isWithinBounds(scaledPoint)) {
                return clueView;
            }
        }
        return null;
    }

    public ArrayList<SkyscrapersLineView> getLineViews() {
        return lineViews;
    }

    public ArrayList<SkyscrapersClueView> getNorthClues() {
        return northClues;
    }

    public ArrayList<SkyscrapersClueView> getEastClues() {
        return eastClues;
    }

    public ArrayList<SkyscrapersClueView> getSouthClues() {
        return southClues;
    }

    public ArrayList<SkyscrapersClueView> getWestClues() {
        return westClues;
    }

    @Override
    protected Dimension getProperSize() {
        Dimension boardViewSize = new Dimension();
        boardViewSize.width = (gridSize.width + 2) * elementSize.width;
        boardViewSize.height = (gridSize.height + 2) * elementSize.height;
        return boardViewSize;
    }

    /**
     * Called when the tree element has changed.
     *
     * @param treeElement tree element
     */
    @Override
    public void onTreeElementChanged(TreeElement treeElement) {
        super.onTreeElementChanged(treeElement);
        SkyscrapersBoard treeTentBoard;
        if (board instanceof CaseBoard) {
            treeTentBoard = (SkyscrapersBoard) ((CaseBoard) board).getBaseBoard();
        } else {
            treeTentBoard = (SkyscrapersBoard) board;
        }

        lineViews.clear();
        for (SkyscrapersLine line : treeTentBoard.getLines()) {
        	SkyscrapersLineView lineView = new SkyscrapersLineView(line);
            lineView.setSize(elementSize);
            lineViews.add(lineView);
        }
    }

    @Override
    public void drawBoard(Graphics2D graphics2D) {
        super.drawBoard(graphics2D);

        for (SkyscrapersLineView view : lineViews) {
            view.draw(graphics2D);
        }

        for (SkyscrapersClueView clueView : northClues) {
            clueView.draw(graphics2D);
        }

        for (SkyscrapersClueView clueView : eastClues) {
            clueView.draw(graphics2D);
        }

        for (SkyscrapersClueView clueView : southClues) {
            clueView.draw(graphics2D);
        }

        for (SkyscrapersClueView clueView : westClues) {
            clueView.draw(graphics2D);
        }
    }
}
