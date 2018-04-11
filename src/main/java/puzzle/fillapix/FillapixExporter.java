package puzzle.fillapix;

import model.PuzzleExporter;
import model.gameboard.ElementData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import puzzle.lightup.LightUp;
import puzzle.lightup.LightUpBoard;

public class FillapixExporter extends PuzzleExporter
{

    public FillapixExporter(Fillapix fillapix)
    {
        super(fillapix);
    }

    @Override
    protected Element createBoardElement(Document newDocument)
    {
        FillapixBoard board = (FillapixBoard) puzzle.getTree().getRootNode().getBoard();

        Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("width", String.valueOf(board.getWidth()));
        boardElement.setAttribute("height", String.valueOf(board.getHeight()));

        Element cellsElement = newDocument.createElement("cells");
        for(ElementData data : board.getElementData())
        {
            if(data.getValueInt() != -50)
            {
                Element cellElement = puzzle.getFactory().exportCell(newDocument, data);
                cellsElement.appendChild(cellElement);
            }
        }

        boardElement.appendChild(cellsElement);
        return boardElement;
    }
}
