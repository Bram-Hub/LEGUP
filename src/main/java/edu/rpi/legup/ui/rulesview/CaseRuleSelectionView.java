package edu.rpi.legup.ui.rulesview;

import edu.rpi.legup.ui.WrapLayout;

import javax.swing.*;
import java.awt.*;

public class CaseRuleSelectionView extends JPanel {

    public CaseRuleSelectionView(RuleButton ruleButton) {
        setLayout(new WrapLayout());
        Button button1 = new Button(ruleButton.getX() + ", " + ruleButton.getY());
        button1.setSize(50, 50);
        Button button2 = new Button("How");
        button2.setSize(50, 50);

        add(button1);
        add(button2);

        revalidate();
    }
}
