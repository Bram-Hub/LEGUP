package puzzle.lightup;

import model.PuzzleExporter;
import model.gameboard.ElementData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class LightUpExporter extends PuzzleExporter
{

    public LightUpExporter(LightUp lightUp)
    {
        super(lightUp);
    }

    @Override
    protected Element createBoardElement(Document newDocument)
    {
        LightUpBoard board = (LightUpBoard) puzzle.getTree().getRootNode().getBoard();

        Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("width", String.valueOf(board.getWidth()));
        boardElement.setAttribute("height", String.valueOf(board.getHeight()));

        Element cellsElement = newDocument.createElement("cells");
        for(ElementData data : board.getElementData())
        {
            if(data.getValueInt() != -2)
            {
                Element cellElement = puzzle.getFactory().exportCell(newDocument, data);
                cellsElement.appendChild(cellElement);
            }
        }

        boardElement.appendChild(cellsElement);
        return boardElement;
    }
}
