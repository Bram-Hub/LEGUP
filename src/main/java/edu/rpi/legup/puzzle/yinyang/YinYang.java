package edu.rpi.legup.puzzle.yinyang;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;

public class YinYang extends Puzzle {

    public YinYang() {
        super();
        this.name = "Yin Yang";
        this.importer = new YinYangImporter(this);
        this.exporter = new YinYangExporter(this);
        this.factory = new YinYangCellFactory();
    }

    @Override
    public void initializeView() {
        boardView = new YinYangView((YinYangBoard) currentBoard);
        boardView.setBoard(currentBoard);
        addBoardListener(boardView);
    }

    @Override
    public Board generatePuzzle(int difficulty) {
        // Placeholder: Returns a new empty board of size difficulty x difficulty
        return new YinYangBoard(difficulty, difficulty);
    }

    @Override
    public boolean isValidDimensions(int rows, int columns) {
        // Example validation: Board must be at least 2x2
        return rows >= 2 && columns >= 2;
    }

    @Override
    public boolean isBoardComplete(Board board) {
        YinYangBoard yinYangBoard = (YinYangBoard) board;

        for (var element : yinYangBoard.getPuzzleElements()) {
            YinYangCell cell = (YinYangCell) element;
            if (cell.getType() == YinYangType.UNKNOWN) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onBoardChange(Board board) {
        // No additional action required for now
    }
}
