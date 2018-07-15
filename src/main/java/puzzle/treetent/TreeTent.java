package puzzle.treetent;

import model.Puzzle;
import model.gameboard.Board;
import model.gameboard.ElementData;
import model.tree.Tree;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import puzzle.treetent.rules.*;
import ui.boardview.PuzzleElement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TreeTent extends Puzzle
{

    public TreeTent()
    {
        super();

        this.name = "TreeTent";

        this.importer = new TreeTentImporter(this);
        this.exporter = new TreeTentExporter(this);

        this.factory = new TreeTentCellFactory();

        this.basicRules.add(new EmptyFieldBasicRule());
        this.basicRules.add(new FinishWithGrassBasicRule());
        this.basicRules.add(new FinishWithTentsBasicRule());
        this.basicRules.add(new LastCampingSpotBasicRule());
        this.basicRules.add(new TentForTreeBasicRule());
        this.basicRules.add(new TreeForTentBasicRule());

        this.contradictionRules.add(new NoTentForTreeContradictionRule());
        this.contradictionRules.add(new NoTreeForTentContradictionRule());
        this.contradictionRules.add(new TooFewTentsContradictionRule());
        this.contradictionRules.add(new TooManyTentsContradictionRule());
        this.contradictionRules.add(new TouchingTentsContradictionRule());

        this.caseRules.add(new FillinRowCaseRule());
        this.caseRules.add(new LinkTentCaseRule());
        this.caseRules.add(new LinkTreeCaseRule());
        this.caseRules.add(new TentOrGrassCaseRule());
    }

    /**
     * Initializes the game board. Called by the invoker of the class
     */
    @Override
    public void initializeView()
    {
        TreeTentBoard board = (TreeTentBoard)currentBoard;
        TreeTentView view = new TreeTentView(board.getDimension());
        boardView = view;
        for(PuzzleElement element: boardView.getPuzzleElements())
        {
            int index = element.getIndex();
            TreeTentCell cell = (TreeTentCell)board.getElementData(index);

            cell.setIndex(index);
            element.setData(cell);
        }

        for(TreeTentLine line : board.getLines())
        {
            TreeTentLineView lineView = new TreeTentLineView(line);
            lineView.setSize(view.getSize());
            view.getLineViews().add(lineView);
        }

        for(int i = 0; i < board.getHeight(); i++)
        {
            TreeTentClueView row = view.getWestClues().get(i);
            TreeTentClueView clue = view.getEastClues().get(i);

            row.setData(new TreeTentClue(i, i, TreeTentType.CLUE_WEST));
            clue.setData(board.getEast().get(i));
        }

        for(int i = 0; i < board.getWidth(); i++)
        {
            TreeTentClueView col = view.getNorthClues().get(i);
            TreeTentClueView clue = view.getSouthClues().get(i);

            col.setData(new TreeTentClue(i, i, TreeTentType.CLUE_NORTH));
            clue.setData(board.getSouth().get(i));
        }
    }

    /**
     * Generates a random puzzle based on the difficulty
     *
     * @param difficulty level of difficulty (1-10)
     *
     * @return board of the random puzzle
     */
    @Override
    public Board generatePuzzle(int difficulty)
    {
        return null;
    }

    /**
     * Determines if the current board is a valid state
     *
     * @param board board to check for validity
     *
     * @return true if board is valid, false otherwise
     */
    @Override
    public boolean isBoardComplete(Board board)
    {
        return false;
    }

    /**
     * Callback for when the board data changes
     *
     * @param board the board that has changed
     */
    @Override
    public void onBoardChange(Board board)
    {

    }
}
