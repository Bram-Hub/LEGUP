package edu.rpi.legup.puzzle.starbattle;

import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class StarBattle extends Puzzle {
    public StarBattle() {
        super();
        this.name = "StarBattle";

        this.importer = new StarBattleImporter(this);
        this.exporter = new StarBattleExporter(this);

        this.factory = new StarBattleCellFactory();
    }

    @Override
    @Contract(pure = false)
    public void initializeView() {
        boardView = new StarBattleView((StarBattleBoard) currentBoard);
        addBoardListener(boardView);
    }

    @Override
    @Contract("_ -> null")
    public Board generatePuzzle(int difficulty) {
        return null;
    }

    @Override
    public boolean isBoardComplete(Board board) {
        return true;
    }

    @Override
    @Contract(pure = true)
    public void onBoardChange(@NotNull Board board) {}
}
