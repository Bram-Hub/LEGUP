package puzzle.treetent;

import model.PuzzleExporter;
import model.gameboard.ElementData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TreeTentExporter extends PuzzleExporter
{

    public TreeTentExporter(TreeTent treeTent)
    {
        super(treeTent);
    }

    @Override
    protected Element createBoardElement(Document newDocument)
    {
        TreeTentBoard board = (TreeTentBoard) puzzle.getTree().getRootNode().getBoard();

        Element boardElement = newDocument.createElement("board");
        boardElement.setAttribute("width", String.valueOf(board.getWidth()));
        boardElement.setAttribute("height", String.valueOf(board.getHeight()));

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
