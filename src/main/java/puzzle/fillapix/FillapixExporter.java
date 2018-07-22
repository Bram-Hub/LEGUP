package puzzle.fillapix;

import model.PuzzleExporter;
import model.gameboard.Element;
import org.w3c.dom.Document;

public class FillapixExporter extends PuzzleExporter
{

    public FillapixExporter(Fillapix fillapix)
    {
        super(fillapix);
    }

    @Override
    protected org.w3c.dom.Element createBoardElement(Document newDocument)
    {
        FillapixBoard board = (FillapixBoard) puzzle.getTree().getRootNode().getBoard();

        org.w3c.dom.Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("width", String.valueOf(board.getWidth()));
        boardElement.setAttribute("height", String.valueOf(board.getHeight()));

        org.w3c.dom.Element cellsElement = newDocument.createElement("cells");
        for(Element data : board.getElementData())
        {
            if(data.getValueInt() != -50)
            {
                org.w3c.dom.Element cellElement = puzzle.getFactory().exportCell(newDocument, data);
                cellsElement.appendChild(cellElement);
            }
        }

        boardElement.appendChild(cellsElement);
        return boardElement;
    }
}
