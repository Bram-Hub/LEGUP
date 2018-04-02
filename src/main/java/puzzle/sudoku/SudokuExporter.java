package puzzle.sudoku;

import model.PuzzleExporter;
import model.gameboard.ElementData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import puzzle.lightup.LightUp;
import puzzle.lightup.LightUpBoard;

public class SudokuExporter extends PuzzleExporter
{

    public SudokuExporter(Sudoku sudoku)
    {
        super(sudoku);
    }

    @Override
    protected Element createBoardElement(Document newDocument)
    {
        SudokuBoard board = (SudokuBoard) puzzle.getTree().getRootNode().getBoard();

        Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("size", String.valueOf(board.getSize()));

        Element cellsElement = newDocument.createElement("cells");
        for(ElementData data : board.getElementData())
        {
            if(data.getValueInt() != 0)
            {
                Element cellElement = puzzle.getFactory().exportCell(newDocument, data);
                cellsElement.appendChild(cellElement);
            }
        }

        boardElement.appendChild(cellsElement);
        return boardElement;
    }
}
