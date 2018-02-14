package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.io.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;

import javax.swing.SwingConstants;

import javax.swing.JToolBar;

import app.BoardController;
import app.GameBoardFacade;
import puzzles.sudoku.SudokuView;
import ui.boardview.BoardView;
import ui.boardview.GridBoardView;
import ui.rulesview.RuleFrame;
import ui.treeview.TreePanel;

import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;


public class GridUI extends LegupUI
{
    private RuleFrame justificationFrame;
    private TreePanel treePanel;
    private TitledBorder boardBorder;
    private JSplitPane topHalfPanel, mainPanel;
    private LinkedList<BoardView> boardStack = new LinkedList<>();

    public GridUI()
    {
        super();
        setupContent();
    }

    public void repaintBoard()
    {
        if(boardView != null)
        {
            boardView.updateBoard(GameBoardFacade.getInstance().getPuzzleModule().getCurrentBoard());
        }
    }

    public void pushBoard(BoardView b)
    {
        b.setBorder(boardBorder);
        boardStack.push(boardView);
        int z1 = boardView.getZoom();
        double z2 = ((double) z1) / 100;
        b.zoomTo(z2);
        b.getViewport().setViewPosition(boardView.getViewport().getViewPosition());
        boardView = b;
        topHalfPanel.setRightComponent(b);
    }

    public void popBoard()
    {
        BoardView b = boardStack.pop();
        int z1 = boardView.getZoom();
        double z2 = ((double) z1) / 100;
        b.zoomTo(z2);
        b.getViewport().setViewPosition(boardView.getViewport().getViewPosition());

        boardView = b;
        topHalfPanel.setRightComponent(b);
    }

    /**
     * Sets the main content for the user interface
     */
    @Override
    protected void setupContent()
    {

        JPanel consoleBox = new JPanel(new BorderLayout());
        JPanel treeBox = new JPanel(new BorderLayout());
        JPanel ruleBox = new JPanel(new BorderLayout());

        //console = new Console();

        justificationFrame = new RuleFrame(null);
        //ruleBox.add( justificationFrame, BorderLayout.WEST );

        boardView = new SudokuView(new BoardController(), new Dimension(9,9),new Dimension(9,9));
        boardView.setPreferredSize(new Dimension(600, 400));
        //boardView.updateBoard(GameBoardFacade.getInstance().getPuzzleModule().getCurrentBoard());

        treePanel = new TreePanel(this);

        JPanel boardPanel = new JPanel(new BorderLayout());
        topHalfPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, justificationFrame, boardView);
        mainPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, topHalfPanel, treePanel);
        topHalfPanel.setPreferredSize(new Dimension(600, 400));
        mainPanel.setPreferredSize(new Dimension(600, 600));
        boardPanel.add(mainPanel);
        boardBorder = BorderFactory.createTitledBorder("Board");
        boardBorder.setTitleJustification(TitledBorder.CENTER);
        boardView.setBorder(boardBorder);

        ruleBox.add(boardPanel);
        treeBox.add(ruleBox);
        consoleBox.add(treeBox);
        add(consoleBox);

        mainPanel.setDividerLocation(mainPanel.getMaximumDividerLocation() + 100);
        pack();
        invalidate();
    }
}