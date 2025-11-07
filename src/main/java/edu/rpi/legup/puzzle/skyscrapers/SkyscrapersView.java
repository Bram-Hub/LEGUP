package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.ui.boardview.ElementView;
import edu.rpi.legup.ui.boardview.GridBoardView;
import java.awt.*;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SkyscrapersView extends GridBoardView {
    private static final Logger LOGGER = LogManager.getLogger(SkyscrapersView.class.getName());

    private ArrayList<SkyscrapersClueView> northClues;
    private ArrayList<SkyscrapersClueView> eastClues;
    private ArrayList<SkyscrapersClueView> southClues;
    private ArrayList<SkyscrapersClueView> westClues;

    public SkyscrapersView(SkyscrapersBoard board) {
        super(new BoardController(), new SkyscrapersController(), board.getDimension());

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
            elementView.setLocation(
                    new Point((loc.x + 1) * elementSize.width, (loc.y + 1) * elementSize.height));
            elementViews.add(elementView);
        }

        for (int i = 0; i < gridSize.height; i++) {
            SkyscrapersClueView row = new SkyscrapersClueView(board.getWestClues().get(i));
            row.setLocation(new Point(0, (i + 1) * elementSize.height));
            row.setSize(elementSize);

            SkyscrapersClueView clue = new SkyscrapersClueView(board.getEastClues().get(i));
            clue.setLocation(
                    new Point(
                            (gridSize.height + 1) * elementSize.height,
                            (i + 1) * elementSize.height));
            clue.setSize(elementSize);

            westClues.add(row);
            eastClues.add(clue);
        }

        for (int i = 0; i < gridSize.width; i++) {
            SkyscrapersClueView col = new SkyscrapersClueView(board.getNorthClues().get(i));
            col.setLocation(new Point((i + 1) * elementSize.width, 0));
            col.setSize(elementSize);

            SkyscrapersClueView clue = new SkyscrapersClueView(board.getSouthClues().get(i));
            clue.setLocation(
                    new Point(
                            (i + 1) * elementSize.width, (gridSize.width + 1) * elementSize.width));
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

    public ArrayList<SkyscrapersClueView> getNorthClues() {
        return northClues;
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
     * Sets the board associated with this view
     *
     * @param board board
     */
    @Override
    public void setBoard(Board board) {
        if (this.board != board) {
            this.board = board;

            if (board instanceof CaseBoard) {
                setCasePickable();
            } else {
                for (ElementView elementView : elementViews) {
                    elementView.setPuzzleElement(
                            board.getPuzzleElement(elementView.getPuzzleElement()));
                    elementView.setShowCasePicker(false);
                }
                for (SkyscrapersClueView clueView : northClues) {
                    clueView.setPuzzleElement(board.getPuzzleElement(clueView.getPuzzleElement()));
                    clueView.setShowCasePicker(false);
                }
                for (SkyscrapersClueView clueView : westClues) {
                    clueView.setPuzzleElement(board.getPuzzleElement(clueView.getPuzzleElement()));
                    clueView.setShowCasePicker(false);
                }
            }
        }
    }

    @Override
    protected void setCasePickable() {
        CaseBoard caseBoard = (CaseBoard) board;
        Board baseBoard = caseBoard.getBaseBoard();

        for (ElementView elementView : elementViews) {
            PuzzleElement puzzleElement =
                    baseBoard.getPuzzleElement(elementView.getPuzzleElement());
            elementView.setPuzzleElement(puzzleElement);
            elementView.setShowCasePicker(true);
            elementView.setCaseRulePickable(caseBoard.isPickable(puzzleElement, null));
        }
        for (SkyscrapersClueView clueView : northClues) {
            PuzzleElement puzzleElement = baseBoard.getPuzzleElement(clueView.getPuzzleElement());
            clueView.setPuzzleElement(puzzleElement);
            clueView.setShowCasePicker(true);
            clueView.setCaseRulePickable(caseBoard.isPickable(puzzleElement, null));
        }
        for (SkyscrapersClueView clueView : westClues) {
            PuzzleElement puzzleElement = baseBoard.getPuzzleElement(clueView.getPuzzleElement());
            clueView.setPuzzleElement(puzzleElement);
            clueView.setShowCasePicker(true);
            clueView.setCaseRulePickable(caseBoard.isPickable(puzzleElement, null));
        }
    }

    @Override
    public void drawBoard(Graphics2D graphics2D) {
        super.drawBoard(graphics2D);

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
