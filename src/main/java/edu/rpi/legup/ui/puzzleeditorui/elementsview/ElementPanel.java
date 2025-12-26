package edu.rpi.legup.ui.puzzleeditorui.elementsview;

import edu.rpi.legup.model.elements.Element;
import edu.rpi.legup.ui.WrapLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ElementPanel extends JPanel {
    private static final Logger LOGGER = LogManager.getLogger(ElementPanel.class.getName());

    protected ImageIcon icon;
    protected String name;
    protected String toolTip;
    protected ElementButton[] elementButtons;
    protected ElementFrame elementFrame;
    protected List<? extends Element> elements;

    public ElementPanel(ElementFrame eFrame) {
        this.elementFrame = eFrame;
        this.elements = new ArrayList<>();
        setLayout(new WrapLayout());
    }

    public int setElements(List<? extends Element> elements) {
        this.elements = elements;
        clearButtons();

        elementButtons = new ElementButton[elements.size()];
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("adding {} elements to panel", elements.size());
        }
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            elementButtons[i] = new ElementButton(element);
            elementFrame.getButtonGroup().add(elementButtons[i]);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("added button: {}, element {}\n", i, element.getElementName());
            }

            elementButtons[i].setToolTipText(
                    element.getElementName() + ": " + element.getDescription());
            elementButtons[i].addActionListener(elementFrame.getController());
            add(elementButtons[i]);
        }
        revalidate();
        return elements.size();
    }

    protected void clearButtons() {
        if (elementButtons != null) {
            removeAll();
            for (int x = 0; x < elementButtons.length; ++x) {
                elementButtons[x].removeActionListener(elementFrame.getController());
            }
        }
    }

    public ElementButton[] getElementButtons() {
        return elementButtons;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public String getToolTip() {
        return toolTip;
    }

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }
}
