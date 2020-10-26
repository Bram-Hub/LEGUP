package edu.rpi.legup.puzzle.sudoku;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.controller.ElementController;
import edu.rpi.legup.ui.boardview.DataSelectionView;
import edu.rpi.legup.ui.boardview.ElementView;
import edu.rpi.legup.ui.boardview.GridBoardView;
import edu.rpi.legup.ui.boardview.SelectionItemView;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class SudokuView extends GridBoardView {
    private static final Color STROKE_COLOR = new Color(0, 0, 0);
    private static final Stroke MINOR_STOKE = new BasicStroke(1);
    private static final Stroke MAJOR_STOKE = new BasicStroke(4);

    public SudokuView(SudokuBoard board) {
        super(new BoardController(), new ElementController(), board.getDimension());

        int minorSize = (int) Math.sqrt(gridSize.width);
        for (int i = 0; i < gridSize.height; i++) {
            for (int k = 0; k < gridSize.width; k++) {
                Point location = new Point(k * elementSize.width + (k / minorSize) * 4 + 5,
                        i * elementSize.height + (i / minorSize) * 4 + 5);
                SudokuElementView element = new SudokuElementView(board.getCell(k, i));
                element.setIndex(i * gridSize.width + k);
                element.setSize(elementSize);
                element.setLocation(location);
                elementViews.add(element);
            }
        }

    }

    /**
     * Gets the SudokuElementView from the puzzleElement index or
     * null if out of bounds
     *
     * @param index index of the ElementView
     * @return SudokuElementView at the specified index
     */
    @Override
    public SudokuElementView getElement(int index) {
        return (SudokuElementView) super.getElement(index);
    }

    public void drawGrid(Graphics2D graphics2D) {
        int minorSize = (int) Math.sqrt(gridSize.width);
        graphics2D.setColor(STROKE_COLOR);
        graphics2D.setStroke(MAJOR_STOKE);
        graphics2D.drawRect(3, 3,
                gridSize.width * (elementSize.width + 1) + 3,
                gridSize.height * (elementSize.height + 1) + 3);

        graphics2D.setColor(STROKE_COLOR);
        graphics2D.setStroke(MAJOR_STOKE);
        for (int i = 1; i < minorSize; i++) {
            int x = i * minorSize * elementSize.width + i * ((minorSize + 1)) + 3;
            graphics2D.drawLine(x, 3, x, gridSize.height * (elementSize.height + 1) + 6);
        }
        for (int i = 1; i < minorSize; i++) {
            int y = i * minorSize * elementSize.height + i * ((minorSize + 1)) + 3;
            graphics2D.drawLine(3, y, gridSize.width * (elementSize.width + 1) + 6, y);
        }
    }

    @Override
    public void drawBoard(Graphics2D graphics2D) {
        drawGrid(graphics2D);

        if (board instanceof PossibleNumberCaseBoard) {
            drawCaseBoard(graphics2D);
            return;
        }
        graphics2D.setColor(STROKE_COLOR);
        graphics2D.setStroke(MINOR_STOKE);
        ElementView hover = null;
        for (int i = 0; i < gridSize.height; i++) {
            for (int k = 0; k < gridSize.width; k++) {
                ElementView element = elementViews.get(i * gridSize.height + k);
                if (!element.isHover())
                    element.draw(graphics2D);
                else
                    hover = element;
            }
        }

        if (hover != null)
            hover.draw(graphics2D);
    }

    public void drawCaseBoard(Graphics2D graphics2D) {
        drawGrid(graphics2D);

        PossibleNumberCaseBoard caseBoard = (PossibleNumberCaseBoard) board;
        SudokuBoard sudokuBoard = (SudokuBoard) caseBoard.getBaseBoard();

        graphics2D.setColor(STROKE_COLOR);
        graphics2D.setStroke(MINOR_STOKE);
        ElementView hover = null;
        for (int i = 0; i < gridSize.height; i++) {
            for (int k = 0; k < gridSize.width; k++) {
                ElementView element = elementViews.get(i * gridSize.height + k);
                if (!element.isHover())
                    element.draw(graphics2D);
                else
                    hover = element;
            }
        }

        graphics2D.setColor(new Color(0x1A, 0x23, 0x7E, 200));
        for (int r : caseBoard.getPickableRegions()) {
            Set<SudokuCell> region = sudokuBoard.getRegion(r);
            int x = Integer.MAX_VALUE, y = Integer.MAX_VALUE, w = Integer.MIN_VALUE, h = Integer.MIN_VALUE;
            for (SudokuCell c : region) {
                x = Math.min(x, c.getLocation().x);
                y = Math.min(y, c.getLocation().y);
                w = Math.max(w, c.getLocation().x);
                h = Math.max(h, c.getLocation().y);
            }

            SudokuElementView firstElement = getElement(y * gridSize.width + x);
            SudokuElementView lastElement = getElement(h * gridSize.width + w);
            x = firstElement.getLocation().x;
            y = firstElement.getLocation().y;
            w = (lastElement.getLocation().x + elementSize.width) - x;
            h = (lastElement.getLocation().y + elementSize.height) - y;

            graphics2D.fillRect(x + 4, y + 4, w - 8, h - 8);
        }

//        if(hover != null)
//            hover.draw(graphics2D);
    }

    protected Dimension getProperSize() {
        Dimension boardViewSize = new Dimension();
        boardViewSize.width = gridSize.width * (elementSize.width + 1) + 9;
        boardViewSize.height = gridSize.height * (elementSize.height + 1) + 9;
        return boardViewSize;
    }

    public DataSelectionView getSelectionPopupMenu() {
        DataSelectionView selectionView = new DataSelectionView(elementController);
        GridLayout layout = new GridLayout(3, 3);
        selectionView.setLayout(layout);
        for (int r = 1; r <= 3; r++) {
            for (int c = 1; c <= 3; c++) {
                SudokuElementView element = new SudokuElementView(new SudokuCell((r - 1) * 3 + c, null, 0));
                element.setSize(new Dimension(32, 32));
                element.setLocation(new Point(0, 0));
                SelectionItemView item = new SelectionItemView(element.getPuzzleElement(), new ImageIcon(element.getImage()));
                item.addActionListener(elementController);
                item.setHorizontalTextPosition(SwingConstants.CENTER);
                selectionView.add(item);
            }
        }
        return selectionView;
    }
}
