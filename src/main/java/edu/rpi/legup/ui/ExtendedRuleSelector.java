package edu.rpi.legup.ui;

import edu.rpi.legup.model.rules.Rule;

import javax.swing.*;
import java.awt.*;

public class ExtendedRuleSelector extends JPanel {
    public ExtendedRuleSelector(Rule rule) {
        setLayout(new GridLayout(3,3));
        setPreferredSize(new Dimension(100, 100));
    }
}