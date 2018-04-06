package puzzle.treetent;

import model.PuzzleImporter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import save.InvalidFileFormatException;

import java.awt.*;

public class TreeTentImporter extends PuzzleImporter
{
    public TreeTentImporter(TreeTent treeTent)
    {
        super(treeTent);
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
                throw new InvalidFileFormatException("TreeTent Importer: cannot find board element");
            }
            Element boardElement = (Element)node;
            if(boardElement.getElementsByTagName("cells").getLength() == 0)
            {
                throw new InvalidFileFormatException("TreeTent Importer: no data found for board");
            }
            Element dataElement = (Element)boardElement.getElementsByTagName("cells").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("cell");

            TreeTentBoard treeTentBoard = null;
            if(!boardElement.getAttribute("size").isEmpty())
            {
                int size = Integer.valueOf(boardElement.getAttribute("size"));
                treeTentBoard = new TreeTentBoard(size);
            }
            else if(!boardElement.getAttribute("width").isEmpty() && !boardElement.getAttribute("height").isEmpty())
            {
                int width = Integer.valueOf(boardElement.getAttribute("width"));
                int height = Integer.valueOf(boardElement.getAttribute("height"));
                treeTentBoard = new TreeTentBoard(width, height);
            }

            if(treeTentBoard == null)
            {
                throw new InvalidFileFormatException("TreeTent Importer: invalid board dimensions");
            }

            int width = treeTentBoard.getWidth();
            int height = treeTentBoard.getHeight();

            for(int i = 0; i < elementDataList.getLength(); i++)
            {
                TreeTentCell cell = (TreeTentCell)puzzle.getFactory().importCell(elementDataList.item(i), treeTentBoard);
                Point loc = cell.getLocation();
                if(cell.getValueInt() != 0)
                {
                    cell.setModifiable(false);
                    cell.setGiven(true);
                }
                treeTentBoard.setCell(loc.x, loc.y, cell);
            }

            for(int y = 0; y < height; y++)
            {
                for(int x = 0; x < width; x++)
                {
                    if(treeTentBoard.getCell(x, y) == null)
                    {
                        TreeTentCell cell = new TreeTentCell(0, new Point(x, y));
                        cell.setIndex(y * height + x);
                        cell.setModifiable(true);
                        treeTentBoard.setCell(x, y, cell);
                    }
                }
            }

            NodeList axes = boardElement.getElementsByTagName("axis");
            if(axes.getLength() != 2)
            {
                throw new InvalidFileFormatException("TreeTent Importer: cannot find axes");
            }

            Element axis1 = (Element) axes.item(0);
            Element axis2 = (Element) axes.item(1);

            if(!axis1.hasAttribute("side") || !axis1.hasAttribute("side"))
            {
                throw new InvalidFileFormatException("TreeTent Importer: side attribute of axis not specified");
            }
            String side1 = axis1.getAttribute("side");
            String side2 = axis2.getAttribute("side");
            if(side1.equalsIgnoreCase(side2) || !(side1.equalsIgnoreCase("east") || side1.equalsIgnoreCase("south")) ||
                    !(side2.equalsIgnoreCase("east") || side2.equalsIgnoreCase("south")))
            {
                throw new InvalidFileFormatException("TreeTent Importer: axes must be different and be {east | south}");
            }
            NodeList eastClues = side1.equalsIgnoreCase("east") ? axis1.getElementsByTagName("clue") : axis2.getElementsByTagName("clue");
            NodeList southClues = side1.equalsIgnoreCase("south") ? axis1.getElementsByTagName("clue") : axis2.getElementsByTagName("clue");

            if(eastClues.getLength() != treeTentBoard.getHeight() || southClues.getLength() != treeTentBoard.getWidth())
            {
                throw new InvalidFileFormatException("TreeTent Importer: there must be same number of clues as the dimension of the board");
            }

            for(int i = 0; i < eastClues.getLength(); i++)
            {
                Element clue = (Element)eastClues.item(i);
                int value = Integer.valueOf(clue.getAttribute("value"));
                int index = TreeTentClue.colStringToColNum(clue.getAttribute("index"));

                if(index - 1< 0 || index - 1> treeTentBoard.getHeight())
                {
                    throw new InvalidFileFormatException("TreeTent Importer: clue index out of bounds");
                }

                if(treeTentBoard.getEast().get(index - 1) != null)
                {
                    throw new InvalidFileFormatException("TreeTent Importer: duplicate clue index");
                }
                treeTentBoard.getEast().set(index - 1, new TreeTentClue(value, index, TreeTentType.CLUE_EAST));
            }

            for(int i = 0; i < southClues.getLength(); i++)
            {
                Element clue = (Element)southClues.item(i);
                int value = Integer.valueOf(clue.getAttribute("value"));
                int index = Integer.valueOf(clue.getAttribute("index"));

                if(index - 1 < 0 || index - 1> treeTentBoard.getWidth())
                {
                    throw new InvalidFileFormatException("TreeTent Importer: clue index out of bounds");
                }

                if(treeTentBoard.getSouth().get(index - 1) != null)
                {
                    throw new InvalidFileFormatException("TreeTent Importer: duplicate clue index");
                }
                treeTentBoard.getSouth().set(index - 1, new TreeTentClue(value, index, TreeTentType.CLUE_SOUTH));
            }

            if(boardElement.getElementsByTagName("lines").getLength() == 1)
            {
                Element linesElement = (Element)boardElement.getElementsByTagName("lines").item(0);
                NodeList linesList = linesElement.getElementsByTagName("line");
                for(int i = 0; i < linesList.getLength(); i++)
                {
                    treeTentBoard.getLines().add((TreeTentLine)puzzle.getFactory().importCell(linesList.item(i), treeTentBoard));
                }
            }

            puzzle.setCurrentBoard(treeTentBoard);
        }
        catch(NumberFormatException e)
        {
            throw new InvalidFileFormatException("TreeTent Importer: unknown value where integer expected");
        }
    }
}
