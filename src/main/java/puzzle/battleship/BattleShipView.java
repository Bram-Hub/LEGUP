package puzzle.battleship;

import controller.BoardController;
import model.gameboard.Board;
import ui.boardview.GridBoardView;
import ui.boardview.PuzzleElement;

import java.awt.*;

public class BattleShipView extends GridBoardView {
    public BattleShipView(Dimension gridSize) {
        super(new BoardController(), new BattleShipCellController(), gridSize);

        for (int i = 0; i < gridSize.height; i++) {
            for (int j = 0; j < gridSize.width; j++) {
                Point location = new Point(j*elementSize.width, i*elementSize.height);
                BattleShipElement element = new BattleShipElement(new BattleShipCell(0, null));
                element.setIndex(i*gridSize.width+j);
                element.setSize(elementSize);
                element.setLocation(location);
                puzzleElements.add(element);
            }
        }
    }

    @Override
    public void updateBoard(Board board) {
        BattleShipBoard battleShipBoard = (BattleShipBoard) board;
        for (PuzzleElement element: puzzleElements) {
            element.setData(battleShipBoard.getElementData(element.getIndex()));
        }
        repaint();
    }
}