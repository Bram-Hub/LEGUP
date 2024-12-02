package edu.rpi.legup.ui.proofeditorui.rulesview;

import edu.rpi.legup.model.rules.Rule;
import javax.swing.*;

/**
 * The {@code RuleButton} class is a custom button that represents a rule in the user interface. It
 * extends {@link JButton} and is designed to display a rule's name and icon. The button is
 * initialized with a {@link Rule} object, which provides the name and icon for the button.
 */
public class RuleButton extends JButton {
    private Rule rule;

    /**
     * RuleButton Constructor - creates a button for a rule
     *
     * @param rule rule to create the button
     */
    RuleButton(Rule rule) {
        super(
                rule.getRuleName(),
                rule.getImageIcon()); // display rules' name under rule when load the icon
        this.rule = rule;
        this.setFocusPainted(false);
    }

    /**
     * Gets the Rule from this button
     *
     * @return Rule from this button
     */
    public Rule getRule() {
        return rule;
    }

    /**
     * Sets the Rule for this button
     *
     * @param rule Rule for this button
     */
    void setRule(Rule rule) {
        this.rule = rule;
    }
}
