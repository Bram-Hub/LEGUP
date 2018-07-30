package edu.rpi.legup.puzzle.lightup;

import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.model.gameboard.Element;
import org.w3c.dom.Document;

public class LightUpExporter extends PuzzleExporter
{

    public LightUpExporter(LightUp lightUp)
    {
        super(lightUp);
    }

    @Override
    protected org.w3c.dom.Element createBoardElement(Document newDocument)
    {
        LightUpBoard board = (LightUpBoard) puzzle.getTree().getRootNode().getBoard();

        org.w3c.dom.Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("width", String.valueOf(board.getWidth()));
        boardElement.setAttribute("height", String.valueOf(board.getHeight()));

        org.w3c.dom.Element cellsElement = newDocument.createElement("cells");
        for(Element element : board.getElementData())
        {
            LightUpCell cell = (LightUpCell)element;
            if(cell.getData() != -2)
            {
                org.w3c.dom.Element cellElement = puzzle.getFactory().exportCell(newDocument, element);
                cellsElement.appendChild(cellElement);
            }
        }

        boardElement.appendChild(cellsElement);
        return boardElement;
    }
}
