package ui.treeview;

import app.TreeController;
import model.gameboard.Board;
import model.rules.Rule;
import ui.LegupUI;

import java.awt.BorderLayout;

import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

public class TreePanel extends JPanel
{
    public boolean modifiedSinceSave = false;
    public boolean modifiedSinceUndoPush = false;
    public int updateStatusTimer = 0;
    private TreeView treeView;
    private TreeToolbarPanel toolbar;
    private LegupUI legupUI;

    private JLabel status = new JLabel();
    private Rule curRuleApplied = null;

    public TreePanel(LegupUI legupUI)
    {
        this.legupUI = legupUI;

        JPanel main = new JPanel();

        main.setLayout(new BorderLayout());

        treeView = new TreeView(new TreeController());
        toolbar = new TreeToolbarPanel(this);

        main.add(toolbar, BorderLayout.WEST);
        main.add(treeView, BorderLayout.CENTER);

        //status.setPreferredSize(new Dimension(150,20));
        main.add(status, BorderLayout.SOUTH);

        TitledBorder title = BorderFactory.createTitledBorder("TreePanel");
        title.setTitleJustification(TitledBorder.CENTER);
        main.setBorder(title);

        setLayout(new BorderLayout());
        add(main);

        // listeners
//        JustificationFrame.addJustificationAppliedListener(this);
//        legupUI.legupMain.getSelections().addTreeSelectionListener(this);
//        BoardState.addCellChangeListener(this);

        updateStatusTimer = 0;
    }

//    public static void colorTransitions()
//    {
//        if(Legup.getInstance().getInitialBoardState() == null)
//        {
//            return;
//        }
//        if(Legup.getInstance().getGui().checkImmediateFeedback())
//        {
//            Legup.getInstance().getInitialBoardState().evalDelayStatus();
//        }
//        else
//        {
//            BoardState.removeColorsFromTransitions();
//        }
//        Legup.getInstance().getGui().getTree().treePanel.repaint();
//    }
//
//    public JLabel getStatus()
//    {
//        return status;
//    }
//
//    public void undo()
//    {
//        if(undoStack.size() > 0)
//        {
//            tempSuppressUndoPushing = true;
//            BoardState state = SavableProof.bytesToState(undoStack.peek());
//            redoStack.push(SavableProof.stateToBytes(Legup.getInstance().getInitialBoardState()));
//            redoStackState.push(Legup.getCurrentState().getPathToNode());
//            Legup.getInstance().setInitialBoardState(state);
//            Legup.setCurrentState(BoardState.evaluatePathToNode(undoStackState.peek()));
//            undoStack.pop();
//            undoStackState.pop();
//            tempSuppressUndoPushing = false;
//        }
//        if(undoStack.size() == 0)
//        {
//            if(origInitState != null)
//            {
//                BoardState state = SavableProof.bytesToState(origInitState);
//                Legup.getInstance().setInitialBoardState(state);
//                while(state.getChildren().size() > 0)
//                {
//                    state = state.getChildren().get(state.getChildren().size() - 1);
//                }
//                Legup.setCurrentState(state);
//            }
//        }
//    }
//
//    public void redo()
//    {
//        if(redoStack.size() > 0)
//        {
//            tempSuppressUndoPushing = true;
//            BoardState state = SavableProof.bytesToState(redoStack.peek());
//            undoStack.push(SavableProof.stateToBytes(Legup.getInstance().getInitialBoardState()));
//            undoStackState.push(Legup.getCurrentState().getPathToNode());
//            Legup.getInstance().setInitialBoardState(state);
//            Legup.setCurrentState(BoardState.evaluatePathToNode(redoStackState.peek()));
//            redoStack.pop();
//            redoStackState.pop();
//            tempSuppressUndoPushing = false;
//        }
//    }
//
//    /**
//     * Add a child to the sate that is currently selected
//     */
//    public void addChildAtCurrentState()
//    {
//		/*if (curRuleApplied instanceof CaseRule){
//			toolbar.addChild.setEnabled(true);
//		} else {
//			toolbar.addChild.setEnabled(false);
//		}*/
//        treeView.addChildAtCurrentState(curRuleApplied);
//        curRuleApplied = null;
//    }
//
//    /**
//     * Collapse states in the tree view
//     */
//    public void collapseStates()
//    {
//        treeView.collapseCurrentState();
//    }

    /**
     * Merge the selected states
     */
    public void mergeStates()
    {
        treeView.mergeStates();
    }

    /**
     * Delete the child subtree starting at the current state
     */
    public void delChildAtCurrentState()
    {
        treeView.delChildAtCurrentState();
    }

    /**
     * Delete the current state and reposition the children
     */
    public void delCurrentState()
    {
        treeView.delCurrentState();
    }

//    public void justificationApplied(BoardState state, Justification justification)
//    {
//		/*if (j instanceof CaseRule){
//			toolbar.addChild.setEnabled(true);
//		} else {
//			toolbar.addChild.setEnabled(false);
//		}*/
//        curRuleApplied = justification;
//        justification = null;
//        repaint();
//    }

//    public Justification getCurrentJustificationApplied()
//    {
//        return curRuleApplied;
//    }

    /*
    public void treeSelectionChanged(ArrayList<Selection> newSelectionList)
    {
        //System.out.println("tree select changed");
        BoardState cur = Legup.getCurrentState();
        if(cur.getSingleParentState() != null)
        {
            if(cur.getSingleParentState().getFirstChild() != null)
            {
                if(cur.getSingleParentState().getFirstChild().getCaseRuleJustification() != null)
                {
                    toolbar.addChild.setEnabled(true);
                }
                else
                {
                    toolbar.addChild.setEnabled(false);
                }
            }
            else
            {
                toolbar.addChild.setEnabled(false);
            }
        }
        else
        {
            toolbar.addChild.setEnabled(false);
        }
        if(modifiedSinceUndoPush)
        {
            pushUndo();
        }
        modifiedSinceSave = true;
        updateStatus();
        colorTransitions();
    }*/

    /*
    public void pushUndo()
    {
        if(!tempSuppressUndoPushing)
        {
            boolean pushTwice = (undoStack.size() == 0);
            byte[] bytesOfState = SavableProof.stateToBytes(Legup.getInstance().getInitialBoardState());
            if(undoStack.size() > 0)
            {
                if(bytesOfState.equals(undoStack.peek()))
                {
                    return;
                }
            }
            redoStack.clear();
            redoStackState.clear();
            undoStack.push(bytesOfState);
            undoStackState.push(Legup.getCurrentState().getPathToNode());
            modifiedSinceUndoPush = false;
            if(pushTwice)
            {
                pushUndo();
            }
        }
    }*/

    public void boardDataChanged(Board board)
    {
        modifiedSinceSave = true;
        modifiedSinceUndoPush = true;
        updateStatus();
        //colorTransitions();
    }

    public void updateStatus()
    {
        updateStatusTimer = ((updateStatusTimer - 1) > 0) ? (updateStatusTimer - 1) : 0;
        if(updateStatusTimer > 0)
        {
            return;
        }
        this.status.setText("");
    }
}
