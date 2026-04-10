package edu.rpi.legup.ui;

import static java.awt.BorderLayout.*;

import com.formdev.flatlaf.FlatClientProperties;
import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.model.Puzzle;
import edu.rpi.legup.model.gameboard.Board;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;

/**
 * A JPanel that provides a dynamic view with zooming capabilities for different types of content.
 * This class supports views such as game boards or proof trees, allowing users to zoom in and out.
 */
public class DynamicView extends JPanel {

    private final ScrollView scrollView;
    private JPanel zoomWrapper;
    private JPanel zoomer;
    private JLabel status;

    private static final float SLIDER_PRECISION = 0.1f;

    /**
     * Constructs a new DynamicView with the specified ScrollView and view type
     *
     * @param scrollView the ScrollView that provides the content to be displayed and zoomed
     * @param type the type of dynamic view to set up (e.g., BOARD or PROOF_TREE)
     */
    public DynamicView(ScrollView scrollView, DynamicViewType type) {
        this.scrollView = scrollView;

        setLayout(new BorderLayout());

        add(scrollView, CENTER);
        add(setUpZoomer(type), SOUTH);
    }

    /**
     * Sets up the zoomer for the given DynamicViewType
     *
     * @param type The DynamicView that we are setting up the zoomer for (so the zoomer for the
     *     board view or the zoomer for the proof tree view)
     * @return A JPanel containing the zoomer
     */
    private JPanel setUpZoomer(DynamicViewType type) {
        if (type == DynamicViewType.BOARD) {
            return setUpBoardZoomer();
        } else {
            if (type == DynamicViewType.PROOF_TREE) {
                return setUpProofTreeZoomer();
            }
        }

        // Should never reach here; if you reach here, that's a problem!
        return null;
    }

    /**
     * Sets up the zoomer for the board view
     *
     * @return A JPanel containing the zoomer
     */
    private JPanel setUpBoardZoomer() {
        final String label = "Resize Board";
        ActionListener listener = (ActionListener) -> this.fitBoardViewToScreen();
        return this.setUpZoomerHelper(label, listener);
    }

    /**
     * Sets up the zoomer for the proof tree view
     *
     * @return A JPanel containing the zoomer
     */
    private JPanel setUpProofTreeZoomer() {
        final String label = "Resize Proof";
        ActionListener listener =
                (ActionListener) ->
                        GameBoardFacade.getInstance()
                                .getLegupUI()
                                .getProofEditor()
                                .fitTreeViewToScreen();
        return this.setUpZoomerHelper(label, listener);
    }

    /**
     * Creates the zoomer
     *
     * @param label A string containing the label to be displayed on the fit to screen button
     * @param listener A listener that determines what the resize button will do
     * @return A JPanel containing the zoomer
     */
    private JPanel setUpZoomerHelper(final String label, ActionListener listener) {
        zoomWrapper = new JPanel();
        try {
            zoomer = new JPanel();

            // Create and add the resize button to the zoomer
            JButton resizeButton = new JButton(label);
            resizeButton.setFocusPainted(false);
            resizeButton.setFocusPainted(false);
            resizeButton.setEnabled(true);
            resizeButton.setSize(100, 50);
            resizeButton.addActionListener(listener);
            zoomer.add(resizeButton);

            JLabel zoomLabel = new JLabel("100%");
            zoomLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "zoomer");
            zoomLabel.setPreferredSize(new Dimension(40, 50));
            zoomLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            JSlider zoomSlider =
                    new JSlider(
                            (int) (25f / SLIDER_PRECISION),
                            (int) (400f / SLIDER_PRECISION),
                            (int) (100f / SLIDER_PRECISION));

            JButton plus =
                    new JButton(
                            new ImageIcon(
                                    ImageIO.read(
                                            Objects.requireNonNull(
                                                    ClassLoader.getSystemClassLoader()
                                                            .getResource(
                                                                    "edu/rpi/legup/imgs/add.png")))));
            plus.setFocusPainted(false);
            plus.setPreferredSize(new Dimension(20, 20));
            plus.addActionListener(
                    (ActionEvent e) ->
                            zoomSlider.setValue(
                                    zoomSlider.getValue() + (int) (25f / SLIDER_PRECISION)));

            JButton minus =
                    new JButton(
                            new ImageIcon(
                                    ImageIO.read(
                                            Objects.requireNonNull(
                                                    ClassLoader.getSystemClassLoader()
                                                            .getResource(
                                                                    "edu/rpi/legup/imgs/remove.png")))));
            minus.setFocusPainted(false);
            minus.setPreferredSize(new Dimension(20, 20));
            minus.addActionListener(
                    (ActionEvent e) ->
                            zoomSlider.setValue(
                                    zoomSlider.getValue() - (int) (25f / SLIDER_PRECISION)));
            this.scrollView.setWheelScrollingEnabled(true);

            zoomSlider.setPreferredSize(new Dimension(160, 30));

            scrollView.addComponentListener(
                    new ComponentAdapter() {
                        @Override
                        public void componentResized(ComponentEvent e) {
                            zoomSlider.setValue((int) (scrollView.getZoom() / SLIDER_PRECISION));
                            zoomLabel.setText(
                                    (int) (zoomSlider.getValue() * SLIDER_PRECISION + 0.5) + "%");
                        }
                    });

            zoomSlider.addChangeListener(
                    (ChangeEvent e) -> {
                        scrollView.zoomTo(zoomSlider.getValue() * SLIDER_PRECISION / 100.0);
                        zoomLabel.setText(
                                (int) (zoomSlider.getValue() * SLIDER_PRECISION + 0.5) + "%");
                    });

            zoomSlider.setMajorTickSpacing((int) (100f / SLIDER_PRECISION));
            zoomSlider.setMinorTickSpacing((int) (25f / SLIDER_PRECISION));
            zoomSlider.setPaintTicks(true);

            // Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
            // labelTable.put((int)(25f/SLIDER_PRECISION), new JLabel("25%"));
            // labelTable.put((int)(100f/SLIDER_PRECISION), new JLabel("100%"));
            // labelTable.put((int)(400f/SLIDER_PRECISION), new JLabel("400%"));
            // zoomSlider.setLabelTable(labelTable);
            // zoomSlider.setPaintLabels(true);

            zoomer.setLayout(new FlowLayout());

            zoomer.add(minus);
            zoomer.add(zoomSlider);
            zoomer.add(plus);
            zoomer.add(zoomLabel);

            status = new JLabel();

            zoomWrapper.setLayout(new BorderLayout());
            zoomWrapper.add(status, WEST);
            zoomWrapper.add(zoomer, EAST);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zoomWrapper;
    }

    /**
     * Gets the ScrollView component associated with this DynamicView
     *
     * @return the ScrollView component
     */
    public ScrollView getScrollView() {
        return this.scrollView;
    }

    /**
     * Gets the zoom wrapper that contains the zooming controls
     *
     * @return the zoom wrapper with zooming controls
     */
    public JPanel getZoomWrapper() {
        return this.zoomWrapper;
    }

    /**
     * Gets the zoomer that contains the zoomer component
     *
     * @return the zoomer with the zoomer component
     */
    public JPanel getZoomer() {
        return this.zoomer;
    }

    /**
     * Updates the status label with an informational message
     *
     * @param message the informational message to display
     */
    public void updateInfo(String message) {
        status.putClientProperty(FlatClientProperties.STYLE_CLASS, "info");
        status.setText(message);
    }

    /**
     * Updates the status label with an error message
     *
     * @param message the error message to display
     */
    public void updateError(String message) {
        status.putClientProperty(FlatClientProperties.STYLE_CLASS, "error");
        status.setText(message);
    }

    /** Clears the status label */
    public void resetStatus() {
        status.putClientProperty(FlatClientProperties.STYLE_CLASS, null);
        status.setText("");
    }

    /** Resets the view to its default state and zooms the content to fit the screen */
    public void reset() {
        Puzzle puzzle = GameBoardFacade.getInstance().getPuzzleModule();
        Board board1 = GameBoardFacade.getInstance().getBoard();
        board1.setModifiable(true);
        Dimension bi = new Dimension(1200, 900);
        this.getScrollView().zoomFit();
    }

    /** Fits the board view to the screen */
    protected void fitBoardViewToScreen() {
        scrollView.zoomFit();
    }
}
