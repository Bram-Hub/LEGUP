package edu.rpi.legup.puzzle.sudoku;

import edu.rpi.legup.controller.BoardController;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.ui.boardview.DataSelectionView;
import edu.rpi.legup.ui.boardview.GridBoardView;
import edu.rpi.legup.ui.boardview.SelectionItemView;
import java.awt.*;
import javax.swing.*;

public class SudokuView extends GridBoardView {
    private static final Color STROKE_COLOR = new Color(0, 0, 0);
    private static final Stroke MINOR_STOKE = new BasicStroke(1);
    private static final Stroke MAJOR_STOKE = new BasicStroke(4);

    public SudokuView(SudokuBoard board) {
        super(new BoardController(), new SudokuCellController(), board.getDimension());

        generateElementViews(board);
    }

    /**
     * Gets the SudokuElementView from the puzzleElement index or null if out of bounds
     *
     * @param index index of the ElementView
     * @return SudokuElementView at the specified index
     */
    @Override
    public SudokuElementView getElement(int index) {
        return (SudokuElementView) super.getElement(index);
    }

    @Override
    protected void generateElementViews(Board board) {
        elementViews.clear();

        SudokuBoard sudokuBoard = (SudokuBoard) board;
        int minorSize = (int) Math.sqrt(gridSize.width);
        for (int i = 0; i < gridSize.height; i++) {
            for (int k = 0; k < gridSize.width; k++) {
                Point location =
                        new Point(
                                k * elementSize.width + (k / minorSize) * 4 + 5, //
                                i * elementSize.height + (i / minorSize) * 4 + 5);
                //                Point location =
                //                        new Point(
                //                                k * elementSize.width,
                //                                i * elementSize.height);
                SudokuElementView element = new SudokuElementView(sudokuBoard.getCell(k, i));
                element.setIndex(i * gridSize.width + k);
                element.setIndex(i * gridSize.width);
                element.setSize(elementSize);
                element.setLocation(location);
                elementViews.add(element);
            }
        }
    }

    public void drawGrid(Graphics2D graphics2D) {
        int minorSize = (int) Math.sqrt(gridSize.width);
        graphics2D.setColor(STROKE_COLOR);
        graphics2D.setStroke(MAJOR_STOKE);
        graphics2D.drawRect(
                3,
                3,
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

    //    @Override
    //    public void drawBoard(Graphics2D graphics2D) {
    //        drawGrid(graphics2D);
    //
    //        if (board instanceof PossibleNumberCaseBoard) {
    //            drawCaseBoard(graphics2D);
    //            return;
    //        }
    //        graphics2D.setColor(STROKE_COLOR);
    //        graphics2D.setStroke(MINOR_STOKE);
    //        ElementView hover = null;
    //        for (int i = 0; i < gridSize.height; i++) {
    //            for (int k = 0; k < gridSize.width; k++) {
    //                ElementView element = elementViews.get(i * gridSize.height + k);
    //                if (!element.isHover()) {
    //                    element.draw(graphics2D);
    //                } else {
    //                    hover = element;
    //                }
    //            }
    //        }
    //
    //        if (hover != null) {
    //            hover.draw(graphics2D);
    //        }
    //    }

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
                SudokuElementView element =
                        new SudokuElementView(
                                new SudokuCell((r - 1) * 3 + c, null, 0, gridSize.width));
                element.setSize(new Dimension(32, 32));
                element.setLocation(new Point(0, 0));
                SelectionItemView item =
                        new SelectionItemView(
                                element.getPuzzleElement(), new ImageIcon(element.getImage()));
                item.addActionListener(elementController);
                item.setHorizontalTextPosition(SwingConstants.CENTER);
                selectionView.add(item);
            }
        }
        return selectionView;
    }
}
