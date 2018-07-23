package puzzle.battleship;

import controller.BoardController;
import model.gameboard.Board;
import model.gameboard.Element;
import ui.boardview.ElementView;
import ui.boardview.GridBoardView;

import java.awt.*;

public class BattleShipView extends GridBoardView {
    public BattleShipView(BattleShipBoard board) {
        super(new BoardController(), new BattleShipCellController(), board.getDimension());

        for(Element element : board.getElementData())
        {
            BattleShipCell cell = (BattleShipCell)element;
            Point loc = cell.getLocation();
            BattleShipElementView elementView = new BattleShipElementView(cell);
            elementView.setIndex(cell.getIndex());
            elementView.setSize(elementSize);
            elementView.setLocation(new Point(loc.x * elementSize.width, loc.y * elementSize.height));
            elementViews.add(elementView);
        }
    }

    @Override
    public void onBoardChanged(Board board) {
        this.board = board;
        BattleShipBoard battleShipBoard = (BattleShipBoard) board;
        for (ElementView element: elementViews) {
            element.setElement(battleShipBoard.getElementData(element.getElement()));
        }
        repaint();
    }
}