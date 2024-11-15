package edu.rpi.legup.puzzle.kakurasu;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.tree.TreeElement;
import edu.rpi.legup.ui.boardview.ElementView;
import edu.rpi.legup.ui.boardview.GridBoardView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class KakurasuView extends GridBoardView {
    private static final Logger LOGGER = LogManager.getLogger(KakurasuView.class.getName());

    static Image FILLED, EMPTY, UNKNOWN;

    static {
        try {
            FILLED =
                    ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/kakurasu/tiles/FilledTile.png"));
            EMPTY =
                    ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/kakurasu/tiles/EmptyTile.png"));
            UNKNOWN =
                    ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/kakurasu/tiles/UnknownTile.png"));
        } catch (IOException e) {
            LOGGER.error("Failed to open Kakurasu images");
        }
    }

    private ArrayList<KakurasuClueView> northClues;
    private ArrayList<KakurasuClueView> eastClues;
    private ArrayList<KakurasuClueView> southClues;
    private ArrayList<KakurasuClueView> westClues;

    public KakurasuView(KakurasuBoard board) {
        super(new BoardController(), new KakurasuController(), board.getDimension());

        this.northClues = new ArrayList<>();
        this.eastClues = new ArrayList<>();
        this.southClues = new ArrayList<>();
        this.westClues = new ArrayList<>();

        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            KakurasuCell cell = (KakurasuCell) puzzleElement;
            Point loc = cell.getLocation();
            KakurasuElementView elementView = new KakurasuElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(
                    new Point((loc.x + 1) * elementSize.width, (loc.y + 1) * elementSize.height));
            elementViews.add(elementView);
        }

        for (int i = 0; i < gridSize.height; i++) {
            KakurasuClueView row =
                    new KakurasuClueView(new KakurasuClue(i, i, KakurasuType.CLUE_WEST));
            row.setLocation(new Point(0, (i + 1) * elementSize.height));
            row.setSize(elementSize);

            KakurasuClueView clue = new KakurasuClueView(board.getRowClues().get(i));
            clue.setLocation(
                    new Point(
                            (gridSize.width + 1) * elementSize.width,
                            (i + 1) * elementSize.height));
            clue.setSize(elementSize);

            westClues.add(row);
            eastClues.add(clue);
        }

        for (int i = 0; i < gridSize.width; i++) {
            KakurasuClueView col =
                    new KakurasuClueView(new KakurasuClue(i, i, KakurasuType.CLUE_NORTH));
            col.setLocation(new Point((i + 1) * elementSize.width, 0));
            col.setSize(elementSize);

            KakurasuClueView clue = new KakurasuClueView(board.getColClues().get(i));
            clue.setLocation(
                    new Point(
                            (i + 1) * elementSize.width,
                            (gridSize.height + 1) * elementSize.height));
            clue.setSize(elementSize);

            northClues.add(col);
            southClues.add(clue);
        }
    }

    /**
     * Gets the ElementView from the location specified or null if one does not exists at that
     * location
     *
     * @param point location on the viewport
     * @return ElementView at the specified location
     */
    @Override
    public ElementView getElement(Point point) {
        Point scaledPoint =
                new Point(
                        (int) Math.round(point.x / getScale()),
                        (int) Math.round(point.y / getScale()));
        for (ElementView element : elementViews) {
            if (element.isWithinBounds(scaledPoint)) {
                return element;
            }
        }
        for (KakurasuClueView clueView : northClues) {
            if (clueView.isWithinBounds(scaledPoint)) {
                return clueView;
            }
        }
        for (KakurasuClueView clueView : eastClues) {
            if (clueView.isWithinBounds(scaledPoint)) {
                return clueView;
            }
        }
        for (KakurasuClueView clueView : southClues) {
            if (clueView.isWithinBounds(scaledPoint)) {
                return clueView;
            }
        }
        for (KakurasuClueView clueView : westClues) {
            if (clueView.isWithinBounds(scaledPoint)) {
                return clueView;
            }
        }
        return null;
    }

    public ArrayList<KakurasuClueView> getNorthClues() {
        return northClues;
    }

    public ArrayList<KakurasuClueView> getEastClues() {
        return eastClues;
    }

    public ArrayList<KakurasuClueView> getSouthClues() {
        return southClues;
    }

    public ArrayList<KakurasuClueView> getWestClues() {
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
        KakurasuBoard kakurasuBoard;
        if (board instanceof CaseBoard) {
            kakurasuBoard = (KakurasuBoard) ((CaseBoard) board).getBaseBoard();
        } else {
            kakurasuBoard = (KakurasuBoard) board;
        }
    }

    @Override
    public void drawBoard(Graphics2D graphics2D) {
        super.drawBoard(graphics2D);

        for (KakurasuClueView clueView : northClues) {
            clueView.draw(graphics2D);
        }

        for (KakurasuClueView clueView : eastClues) {
            clueView.draw(graphics2D);
        }

        for (KakurasuClueView clueView : southClues) {
            clueView.draw(graphics2D);
        }

        for (KakurasuClueView clueView : westClues) {
            clueView.draw(graphics2D);
        }
    }
}
