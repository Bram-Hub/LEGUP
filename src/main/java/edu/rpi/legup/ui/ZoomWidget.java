package edu.rpi.legup.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The {@code ZoomWidget} displays a zoom icon that, when clicked, shows a popup slider to adjust
 * the zoom level of the associated {@code ScrollView}.
 */
public class ZoomWidget extends JLabel {
    private ScrollView parent;
    private PopupSlider palette = new PopupSlider();
    private MouseAdapter open =
            new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    palette.slider.setValue(parent.getZoom());
                    palette.show(e.getComponent(), 0, 0);
                }
            };

    /**
     * ZoomWidget Constructor creates a zoom widget for a ScrollView object
     *
     * @param parent dynamicView to which to the ZoomWidget is applied to
     */
    public ZoomWidget(ScrollView parent) {
        super(new ImageIcon("zoom.png"));
        this.parent = parent;
        addMouseListener(open);
    }

    /**
     * A {@code JPopupMenu} subclass that contains a vertical slider for adjusting zoom level.
     */
    private class PopupSlider extends JPopupMenu implements ChangeListener {
        private static final long serialVersionUID = 8225019381200459814L;

        private JSlider slider;

        /**
         * Constructs a {@code PopupSlider} with a vertical slider
         */
        public PopupSlider() {
            slider = new JSlider(SwingConstants.VERTICAL, 0, 400, 200);
            slider.setMajorTickSpacing(25);
            slider.setPaintTicks(true);

            add(slider);
            slider.addChangeListener(this);
        }

        /**
         * Handles state changes in the slider by adjusting the zoom level of the {@code ScrollView}
         *
         * @param e the {@code ChangeEvent} indicating that the slider's state has changed
         */
        public void stateChanged(ChangeEvent e) {
            if (slider.getValueIsAdjusting()) {
                parent.zoomTo((double) slider.getValue() / 100.0);
            }
        }
    }
}
