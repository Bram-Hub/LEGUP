package edu.rpi.legup.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.ImageIcon;


public class ZoomWidget extends JLabel {
    private ScrollView parent;
    private PopupSlider palette = new PopupSlider();
    private MouseAdapter open = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
            palette.slider.setValue(parent.getZoom());
            palette.show(e.getComponent(), 0, 0);//e.getX(), e.getY() );
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
     *
     */
        private class PopupSlider extends JPopupMenu implements ChangeListener {
            private static final long serialVersionUID = 8225019381200459814L;

            private JSlider slider;

            public PopupSlider() {
                slider = new JSlider(SwingConstants.VERTICAL, 0, 400, 200);
                slider.setMajorTickSpacing(25);
                slider.setPaintTicks(true);

                add(slider);
                slider.addChangeListener(this);
            }

            public void stateChanged(ChangeEvent e) {
                if (slider.getValueIsAdjusting()) {
                    parent.zoomTo((double) slider.getValue() / 100.0);
                }
            }
        }
}
