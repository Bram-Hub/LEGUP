package puzzle.masyu;

import model.PuzzleImporter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import save.InvalidFileFormatException;

import java.awt.*;

public class MasyuImporter extends PuzzleImporter
{
    public MasyuImporter(Masyu masyu)
    {
        super(masyu);
    }

    /**
     * Creates the board for building
     *
     * @param node xml document node
     * @throws InvalidFileFormatException
     */
    @Override
    public void initializeBoard(Node node) throws InvalidFileFormatException
    {
        try
        {
            if(!node.getNodeName().equalsIgnoreCase("board"))
            {
                throw new InvalidFileFormatException("Masyu Importer: cannot find board element");
            }
            Element boardElement = (Element)node;
            if(boardElement.getElementsByTagName("cells").getLength() == 0)
            {
                throw new InvalidFileFormatException("Masyu Importer: no data found for board");
            }
            Element dataElement = (Element)boardElement.getElementsByTagName("cells").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("cell");

            MasyuBoard masyuBoard = null;
            if(!boardElement.getAttribute("size").isEmpty())
            {
                int size = Integer.valueOf(boardElement.getAttribute("size"));
                masyuBoard = new MasyuBoard(size);
            }
            else if(!boardElement.getAttribute("width").isEmpty() && !boardElement.getAttribute("height").isEmpty())
            {
                int width = Integer.valueOf(boardElement.getAttribute("width"));
                int height = Integer.valueOf(boardElement.getAttribute("height"));
                masyuBoard = new MasyuBoard(width, height);
            }

            if(masyuBoard == null)
            {
                throw new InvalidFileFormatException("Masyu Importer: invalid board dimensions");
            }

            int width = masyuBoard.getWidth();
            int height = masyuBoard.getHeight();

            for(int i = 0; i < elementDataList.getLength(); i++)
            {
                MasyuCell cell = (MasyuCell)puzzle.getFactory().importCell(elementDataList.item(i), masyuBoard);
                Point loc = cell.getLocation();
                if(cell.getValueInt() != 0)
                {
                    cell.setModifiable(false);
                    cell.setGiven(true);
                }
                masyuBoard.setCell(loc.x, loc.y, cell);
            }

            for(int y = 0; y < height; y++)
            {
                for(int x = 0; x < width; x++)
                {
                    if(masyuBoard.getCell(x, y) == null)
                    {
                        MasyuCell cell = new MasyuCell(0, new Point(x, y));
                        cell.setIndex(y * height + x);
                        cell.setModifiable(true);
                        masyuBoard.setCell(x, y, cell);
                    }
                }
            }
            puzzle.setCurrentBoard(masyuBoard);
        }
        catch(NumberFormatException e)
        {
            throw new InvalidFileFormatException("Masyu Importer: unknown value where integer expected");
        }
    }
}
