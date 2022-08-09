package edu.rpi.legup.ui;

import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
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

public class DynamicView extends JPanel {

    private ScrollView scrollView;
    private JPanel zoomWrapper;
    private JPanel zoomer;
    private JLabel status;

    private static final Font ERROR_FONT = MaterialFonts.ITALIC;
    private static final Color ERROR_COLOR = MaterialColors.RED_700;

    private static final Font INFO_FONT = MaterialFonts.REGULAR;
    private static final Color INFO_COLOR = MaterialColors.GRAY_900;

    public DynamicView(ScrollView scrollView, DynamicViewType type) {
        this.scrollView = scrollView;

        setLayout(new BorderLayout());

        add(scrollView, CENTER);
        add(setUpZoomer(type), SOUTH);
    }

    /**
     * Sets up the zoomer for the given DynamicViewType
     * @param type The DynamicView that we are setting up the zoomer for (so
     *             the zoomer for the board view or the zoomer for the proof
     *             tree view)
     * @return A JPanel containing the zoomer
     */
    private JPanel setUpZoomer(DynamicViewType type) {
        if (type == DynamicViewType.BOARD)
            return setUpBoardZoomer();
        else if (type == DynamicViewType.PROOF_TREE)
            return setUpProofTreeZoomer();

        // Should never reach here; if you reach here, that's a problem!
        return null;
    }

    /**
     * Sets up the zoomer for the board view
     * @return A JPanel containing the zoomer
     */
    private JPanel setUpBoardZoomer() {
        final String label = "Resize Board";
        ActionListener listener = (ActionListener) -> this.fitBoardViewToScreen();
        return this.setUpZoomerHelper(label, listener);
    }

    /**
     * Sets up the zoomer for the proof tree view
     * @return A JPanel containing the zoomer
     */
    private JPanel setUpProofTreeZoomer() {
        final String label = "Resize Proof";
        ActionListener listener = (ActionListener) -> GameBoardFacade.getInstance().getLegupUI().getProofEditor().fitTreeViewToScreen();
        return this.setUpZoomerHelper(label, listener);
    }

    /**
     * Creates the zoomer
     * @param label A string containing the label to be displayed
     *              on the fit to screen button
     * @param listener A listener that determines what the resize
     *                 button will do
     * @return A JPanel containing the zoomer
     */
    private JPanel setUpZoomerHelper(final String label, ActionListener listener) {
        zoomWrapper = new JPanel();
        try {
            zoomer = new JPanel();

            // Create and add the resize button to the zoomer
            JButton resizeButton = new JButton(label);
            resizeButton.setEnabled(true);
            resizeButton.setSize(100, 50);
            resizeButton.addActionListener(listener);
            zoomer.add(resizeButton);

            JLabel zoomLabel = new JLabel("100%");
            zoomLabel.setFont(MaterialFonts.getRegularFont(16f));

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

    public void reset() {
        // System.out.println("get into the reset");
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        Board board1 = GameBoardFacade.getInstance().getBoard();
        board1.setModifiable(true);
        Dimension bi = new Dimension(1200, 900);
        this.getScrollView().zoomFit();
    }

    protected void fitBoardViewToScreen() {
        scrollView.zoomFit();
    }
}
