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

        Element axisEast = newDocument.createElement("axis");
        axisEast.setAttribute("side", "east");
        for(TreeTentClue clue : board.getEast())
        {
            Element clueElement = newDocument.createElement("clue");
            clueElement.setAttribute("value", String.valueOf(clue.getValueInt()));
            clueElement.setAttribute("index", TreeTentClue.colNumToString(clue.getIndex()));
            axisEast.appendChild(clueElement);
        }
        boardElement.appendChild(axisEast);

        Element axisSouth = newDocument.createElement("axis");
        axisSouth.setAttribute("side", "south");
        for(TreeTentClue clue : board.getEast())
        {
            Element clueElement = newDocument.createElement("clue");
            clueElement.setAttribute("value", String.valueOf(clue.getValueInt()));
            clueElement.setAttribute("index", String.valueOf(clue.getIndex()));
            axisSouth.appendChild(clueElement);
        }
        boardElement.appendChild(axisSouth);

        if(!board.getLines().isEmpty())
        {
            Element linesElement = newDocument.createElement("lines");
            for(ElementData data : board.getLines())
            {
                Element lineElement = puzzle.getFactory().exportCell(newDocument, data);
                linesElement.appendChild(lineElement);
            }
            boardElement.appendChild(linesElement);
        }
        return boardElement;
    }
}
