package puzzle.treetent;

import model.Puzzle;
import model.gameboard.Board;
import model.gameboard.ElementData;
import model.observer.IBoardListener;
import model.observer.ITreeListener;
import model.tree.Tree;
import model.tree.TreeElementType;
import model.tree.TreeNode;
import model.tree.TreeTransition;
import ui.boardview.BoardView;
import ui.boardview.PuzzleElement;
import ui.treeview.*;
import history.PuzzleCommand;

import java.awt.event.MouseEvent;

import static app.GameBoardFacade.getInstance;

public class EditLineCommand extends PuzzleCommand
{
    private TreeTransition transition;
    private ElementData oldData;
    private ElementData newData;

    private PuzzleElement elementView;
    private TreeElementView selectedView;
    private TreeElementView newSelectedView;
    private MouseEvent event;

    private TreeTransitionView transitionView;

    public EditLineCommand(PuzzleElement elementView, TreeElementView selectedView, MouseEvent event, TreeTentLine line)
    {
        this.elementView = elementView;
        this.selectedView = selectedView;
        this.event = event;
        this.newData = line;
        this.oldData = newData.copy();
        this.transition = null;
    }

    /**
     * Executes a command
     */
    @Override
    public void execute()
    {
        Tree tree = getInstance().getTree();
        TreeView treeView = getInstance().getLegupUI().getTreePanel().getTreeView();
        TreeSelection selection = treeView.getTreeSelection();
        BoardView boardView = getInstance().getLegupUI().getBoardView();
        Puzzle puzzle = getInstance().getPuzzleModule();

        TreeTentBoard board = (TreeTentBoard) selectedView.getTreeElement().getBoard();
        int index = elementView.getIndex();

        if(selectedView.getType() == TreeElementType.NODE)
        {
            TreeNodeView nodeView = (TreeNodeView) selectedView;
            TreeNode treeNode = (TreeNode) selectedView.getTreeElement();

            if(transition == null)
            {
                transition = new TreeTransition(treeNode, treeNode.getBoard().copy());
            }

            treeNode.getChildren().add(transition);
            puzzle.notifyTreeListeners((ITreeListener listener) -> listener.onTreeElementAdded(transition));
            transitionView = (TreeTransitionView) treeView.getElementView(transition);

            selection.newSelection(transitionView);
            puzzle.notifyTreeListeners((ITreeListener listener) -> listener.onTreeSelectionChanged(selection));

            getInstance().getLegupUI().repaintTree();
            board = (TreeTentBoard) transition.getBoard();
            getInstance().getPuzzleModule().setCurrentBoard(board);
            oldData = newData.copy();
        }
        else
        {
            transitionView = (TreeTransitionView) selectedView;
            transition = transitionView.getTreeElement();
        }
        newSelectedView = transitionView;
        ElementData dup_line = null;
        boolean mod_contains = false;
        boolean contains = false;
        final TreeTentBoard editBoard = board;
        System.out.println("Size: " + board.getModifiedData().size());
        for(int i = 0; i < board.getModifiedData().size(); i++)
        {
            if(board.getModifiedData().get(i).getValueString() == "LINE")
            {
                if(((TreeTentLine) newData).compare((TreeTentLine) board.getModifiedData().get(i)))
                {
                    System.out.println("contains");
                    dup_line = board.getModifiedData().get(i);
                    mod_contains = true;
                }
            }
        }
        for(int i = 0; i < board.getLines().size(); i++)
        {
            if(board.getLines().get(i).compare((TreeTentLine) newData))
            {
                contains = true;
            }
        }
        if(contains || mod_contains)
        {
            System.out.println("delete");
            board.getModifiedData().remove(dup_line);
            board.getLines().remove(dup_line);
            puzzle.notifyBoardListeners((IBoardListener listener) -> listener.onBoardChanged(editBoard));
        }
        else
        {
            System.out.println("adding");
            board.getModifiedData().add(newData);
            board.getLines().add((TreeTentLine) newData);
            puzzle.notifyBoardListeners((IBoardListener listener) -> listener.onBoardChanged(editBoard));
        }

        transition.propagateChanges(newData);

        getInstance().getLegupUI().repaintBoard();
    }

    /**
     * Determines whether this command can be executed
     */
    @Override
    public boolean canExecute()
    {
        Board board = selectedView.getTreeElement().getBoard();
        int index = elementView.getIndex();
        if(!board.isModifiable())
        {
            return false;
        }
        else if(!board.getElementData(index).isModifiable())
        {
            return false;
        }
        return true;
    }

    /**
     * Gets the reason why the command cannot be executed
     *
     * @return if command cannot be executed, returns reason for why the command cannot be executed,
     * otherwise null if command can be executed
     */
    @Override
    public String getExecutionError()
    {
        Board board = selectedView.getTreeElement().getBoard();
        int index = elementView.getIndex();
        if(!board.isModifiable())
        {
            return "Board is not modifiable";
        }
        else if(!board.getElementData(index).isModifiable())
        {
            return "Data is not modifiable";
        }
        return null;
    }

    /**
     * Undoes an command
     */
    @Override
    public void undo()
    {
        Tree tree = getInstance().getTree();
        TreeView treeView = getInstance().getLegupUI().getTreePanel().getTreeView();
        TreeSelection selection = treeView.getTreeSelection();
        BoardView boardView = getInstance().getLegupUI().getBoardView();

        Board board = transition.getBoard();
        int index = elementView.getIndex();

        if(selectedView.getType() == TreeElementType.NODE)
        {
            TreeNode treeNode = (TreeNode) selectedView.getTreeElement();

            tree.removeTreeElement(transition);
            treeView.removeTreeElement(newSelectedView);

            selection.newSelection(selectedView);

            getInstance().getLegupUI().repaintTree();
            getInstance().getPuzzleModule().setCurrentBoard(treeNode.getBoard());
        }

        Board prevBoard =null;// transition.getParentNode().getBoard();

        newData.setValueInt(oldData.getValueInt());
        board.notifyChange(newData);

        //System.err.println(newData.getValueInt() + " : " + oldData.getValueInt());

        if(prevBoard.getElementData(index).equals(newData))
        {
            board.removeModifiedData(newData);
        }
        else
        {
            board.addModifiedData(newData);
        }
        transition.propagateChanges(newData);
        getInstance().getLegupUI().repaintBoard();
    }
}
