package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;

public class StarBattle extends Puzzle {
    public StarBattle() {
        super();
        this.name = "StarBattle";

        this.importer = new StarBattleImporter(this);
        this.exporter = new StarBattleExporter(this);

        this.factory = new StarBattleCellFactory();
    }

    @Override
    public void initializeView() {
        boardView = new StarBattleView((StarBattleBoard) currentBoard);
        addBoardListener(boardView);
    }

    @Override
    public Board generatePuzzle(int difficulty) {
        return null;
    }

    @Override
    public boolean isBoardComplete(Board board) {
        return true;
    }

    @Override
    public void onBoardChange(Board board) {}
}
