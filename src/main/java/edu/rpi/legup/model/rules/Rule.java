package edu.rpi.legup.model.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.tree.TreeTransition;

import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.Graphics2D;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RegisterRule
public abstract class Rule {
    protected String ruleID;
    protected String ruleName;
    protected String description;
    protected String imageName;
    protected ImageIcon image;
    protected RuleType ruleType;

    private final String INVALID_USE_MESSAGE;

    /**
     * Rule Constructor creates a new rule
     *
     * @param ruleID      ID of the rule
     * @param ruleName    name of the rule
     * @param description description of the rule
     * @param imageName   file name of the image
     */
    public Rule(String ruleID, String ruleName, String description, String imageName) {
        this.ruleID = ruleID;
        this.ruleName = ruleName;
        this.description = description;
        this.imageName = imageName;
        this.INVALID_USE_MESSAGE = "Invalid use of the rule " + this.ruleName;
        loadImage();
    }

    /**
     * Checks whether the transition logically follows from the parent node using this rule
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    public abstract String checkRule(TreeTransition transition);

    /**
     * Checks whether the transition logically follows from the parent node using this rule.
     * This method is the one that should overridden in child classes
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    protected abstract String checkRuleRaw(TreeTransition transition);

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition    transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    public abstract String checkRuleAt(TreeTransition transition, PuzzleElement puzzleElement);

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     * This method is the one that should overridden in child classes
     *
     * @param transition    transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    protected abstract String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement);

    /**
     * Loads the image file
     */
    private void loadImage() {
        if (imageName != null) {
            this.image = new ImageIcon(ClassLoader.getSystemResource(imageName));
            //Resize images to be 100px wide
            Image image = this.image.getImage();
            if (this.image.getIconWidth() < 120) return;
            int height = (int) (100 * ((double) this.image.getIconHeight() / this.image.getIconWidth()));
            if (height == 0) {
                System.out.println("height is 0 error");
                System.out.println("height: " + this.image.getIconHeight());
                System.out.println("width:  " + this.image.getIconWidth());
                return;
            }
            BufferedImage bimage = new BufferedImage(100, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bimage.createGraphics();
            g.drawImage(image, 0, 0, 100, height, null);
            this.image = new ImageIcon(bimage);
        }
    }

    /**
     * Gets the name of the rule
     *
     * @return name of the rule
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * Gets the name of the rule
     *
     * @return name of the rule
     */
    public String getRuleID() {
        return ruleID;
    }

    /**
     * Sets the rule name
     *
     * @param ruleName new name of the rule
     */
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    /**
     * Gets the description of the rule
     *
     * @return the description of the rule
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the image icon of the rule
     *
     * @return image icon of the rule
     */
    public ImageIcon getImageIcon() {
        return image;
    }

    /**
     * Gets the rule type
     *
     * @return rule type
     */
    public RuleType getRuleType() {
        return ruleType;
    }

    public String getInvalidUseOfRuleMessage() {
        return this.INVALID_USE_MESSAGE;
    }
}