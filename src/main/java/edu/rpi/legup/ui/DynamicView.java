package edu.rpi.legup.ui;


import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.controller.TreeController;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.tree.Tree;
import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialColors;
import edu.rpi.legup.ui.lookandfeel.materialdesign.MaterialFonts;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Objects;

import static java.awt.BorderLayout.*;

import  edu.rpi.legup.model.tree.*;
import edu.rpi.legup.ui.proofeditorui.treeview.TreePanel;
import edu.rpi.legup.ui.proofeditorui.treeview.TreeView;

public class DynamicView extends JPanel {

    private ScrollView scrollView;
    private JPanel zoomWrapper;
    private JPanel zoomer;

    private TreeView treeview;

    private JLabel status;

    private static final Font ERROR_FONT = MaterialFonts.ITALIC;
    private static final Color ERROR_COLOR = MaterialColors.RED_700;

    private static final Font INFO_FONT = MaterialFonts.REGULAR;
    private static final Color INFO_COLOR = MaterialColors.GRAY_900;

    public DynamicView(ScrollView scrollView) {
        this.scrollView = scrollView;

        setLayout(new BorderLayout());

        add(scrollView, CENTER);
        add(setUpZoomer(), SOUTH);

    }

    private JPanel setUpZoomer() {
        zoomWrapper = new JPanel();
        try {
            zoomer = new JPanel();
            JButton resizeButton1 = new JButton("Resize puzzle");
            JButton resizeButton2 = new JButton("Resize treeview");
            resizeButton1.setEnabled(true);
            resizeButton2.setEnabled(true);
            resizeButton1.setSize(100,50);
            resizeButton1.setSize(100,50);
            JLabel zoomLabel = new JLabel("100%");
            zoomLabel.setFont(MaterialFonts.getRegularFont(16f));
            zoomer.add(resizeButton1);
            zoomer.add(resizeButton2);
            resizeButton2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(true){
                        rest_for_treeview();
                        System.out.println("The lower resize bottom be click");
                    }
                }
            });
            resizeButton1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //System.out.println("The resize bottom be click");
                    if (true){
                        reset_for_puzzle();
                        System.out.println("The upper resize bottom be click");
                    }



                }
            });
            JSlider zoomSlider = new JSlider(25, 400, 100);

            JButton plus = new JButton(new ImageIcon(ImageIO.read(
                    Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(
                            "edu/rpi/legup/imgs/add.png")))));
            plus.setFont(MaterialFonts.getRegularFont(10f));
            plus.setPreferredSize(new Dimension(20, 20));
            plus.addActionListener((ActionEvent e) -> zoomSlider.setValue(zoomSlider.getValue() + 25));


            JButton minus = new JButton(new ImageIcon(ImageIO.read(
                    Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(
                            "edu/rpi/legup/imgs/remove.png")))));
            minus.setPreferredSize(new Dimension(20, 20));
            minus.setFont(MaterialFonts.getRegularFont(10f));
            minus.addActionListener((ActionEvent e) -> zoomSlider.setValue(zoomSlider.getValue() - 25));
            this.scrollView.setWheelScrollingEnabled(true);
            /*this.scrollView.getViewport().addMouseWheelListener(new MouseAdapter() {
                public void mouseWheelMoved(MouseWheelEvent e) {
                    if (e.isControlDown()) {
                        scrollView.zoom(e.getWheelRotation() * 2, e.getPoint());
                    } else {
                        scrollView.zoom(e.getWheelRotation(), e.getPoint());
                    }
                    zoomSlider.setValue((int) (scrollView.getScale() * 100));
                }
            });*/

            zoomSlider.setPreferredSize(new Dimension(160, 30));

            scrollView.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    zoomSlider.setValue(scrollView.getZoom());
                    zoomLabel.setText(zoomSlider.getValue() + "%");
                }
            });

            zoomSlider.addChangeListener((ChangeEvent e) -> {
                scrollView.zoomTo(zoomSlider.getValue() / 100.0);
                zoomLabel.setText(zoomSlider.getValue() + "%");
            });


            zoomSlider.setMajorTickSpacing(100);
            zoomSlider.setMinorTickSpacing(25);
            zoomSlider.setPaintTicks(true);

            Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
            labelTable.put(0, new JLabel("25%"));
            labelTable.put(100, new JLabel("100%"));
            labelTable.put(400, new JLabel("400%"));
            zoomSlider.setLabelTable(labelTable);

            zoomer.setLayout(new FlowLayout());

            zoomer.add(minus);
            zoomer.add(zoomSlider);
            zoomer.add(plus);
            zoomer.add(zoomLabel);

            status = new JLabel();

            zoomWrapper.setLayout(new BorderLayout());
            zoomWrapper.add(status, WEST);
            zoomWrapper.add(zoomer, EAST);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return zoomWrapper;
    }

    public ScrollView getScrollView() {
        return this.scrollView;
    }

    public JPanel getZoomWrapper() {
        return this.zoomWrapper;
    }

    public JPanel getZoomer() {
        return this.zoomer;
    }

    public void updateInfo(String message) {
        status.setFont(INFO_FONT);
        status.setForeground(INFO_COLOR);
        status.setText(message);
    }

    public void updateError(String message) {
        status.setFont(ERROR_FONT);
        status.setForeground(ERROR_COLOR);
        status.setText(message);
    }

    public void resetStatus() {
        status.setText("");
    }

    public void reset_for_puzzle() {
        // System.out.println("get into the reset");
//        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
//        Board board1 = GameBoardFacade.getInstance().getBoard();
//        Tree tree= GameBoardFacade.getInstance().getTree();
//        //System.out.println("tree view "+ treeView.getZoom()+"puzzle view "+this.getScrollView().getZoom());
//        board1.setModifiable(true);
//        Dimension bi = new Dimension(1200,900);
        scrollView.zoomFit();

        // System.out.println("Finish into the reset");
    }
    public void rest_for_treeview(){
        treeview=GameBoardFacade.getInstance().getLegupUI().getTreePanel().returntreeview();
        treeview.zoomFit();
    }
}
