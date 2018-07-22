package puzzle.battleship;

import controller.BoardController;
import model.gameboard.Board;
import ui.boardview.ElementView;
import ui.boardview.GridBoardView;

import java.awt.*;

public class BattleShipView extends GridBoardView {
    public BattleShipView(Dimension gridSize) {
        super(new BoardController(), new BattleShipCellController(), gridSize);

        for (int i = 0; i < gridSize.height; i++) {
            for (int j = 0; j < gridSize.width; j++) {
                Point location = new Point(j*elementSize.width, i*elementSize.height);
                BattleShipElementView element = new BattleShipElementView(new BattleShipCell(0, null));
                element.setIndex(i*gridSize.width+j);
                element.setSize(elementSize);
                element.setLocation(location);
                elementViews.add(element);
            }
        }
    }

    @Override
    public void onBoardChanged(Board board) {
        this.board = board;
        BattleShipBoard battleShipBoard = (BattleShipBoard) board;
        for (ElementView element: elementViews) {
            element.setElement(battleShipBoard.getElementData(element.getIndex()));
        }
        repaint();
    }
}