package puzzle.sudoku;

import model.PuzzleExporter;
import model.gameboard.Element;
import org.w3c.dom.Document;

public class SudokuExporter extends PuzzleExporter
{

    public SudokuExporter(Sudoku sudoku)
    {
        super(sudoku);
    }

    @Override
    protected org.w3c.dom.Element createBoardElement(Document newDocument)
    {
        SudokuBoard board = (SudokuBoard) puzzle.getTree().getRootNode().getBoard();

        org.w3c.dom.Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("size", String.valueOf(board.getSize()));

        org.w3c.dom.Element cellsElement = newDocument.createElement("cells");
        for(Element data : board.getElementData())
        {
            if(data.getValueInt() != 0)
            {
                org.w3c.dom.Element cellElement = puzzle.getFactory().exportCell(newDocument, data);
                cellsElement.appendChild(cellElement);
            }
        }

        boardElement.appendChild(cellsElement);
        return boardElement;
    }
}
