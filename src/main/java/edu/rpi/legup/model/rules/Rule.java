package edu.rpi.legup.model.rules;

import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.tree.TreeTransition;

import javax.swing.ImageIcon;

@RegisterRule
public abstract class Rule
{
    protected String ruleName;
    protected String description;
    protected String imageName;
    protected ImageIcon image;
    protected RuleType ruleType;

    /**
     * Rule Constructor - creates a new rule
     *
     * @param ruleName name of the rule
     * @param description description of the rule
     * @param imageName file name of the image
     */
    public Rule(String ruleName, String description, String imageName)
    {
        this.imageName = imageName;
        this.ruleName = ruleName;
        this.description = description;
        loadImage();
    }

    /**
     * Checks whether the transition logically follows from the parent node using this rule
     *
     * @param transition transition to check
     *
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    public abstract String checkRule(TreeTransition transition);

    /**
     * Checks whether the transition logically follows from the parent node using this rule.
     * This method is the one that should overridden in child classes
     *
     * @param transition transition to check
     *
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    protected abstract String checkRuleRaw(TreeTransition transition);

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     *
     * @param transition transition to check
     * @param puzzleElement equivalent puzzleElement
     *
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    public abstract String checkRuleAt(TreeTransition transition, PuzzleElement puzzleElement);

    /**
     * Checks whether the child node logically follows from the parent node
     * at the specific puzzleElement index using this rule
     * This method is the one that should overridden in child classes
     *
     * @param transition transition to check
     * @param puzzleElement equivalent puzzleElement
     *
     * @return null if the child node logically follow from the parent node at the specified puzzleElement,
     * otherwise error message
     */
    protected abstract String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement);

    /**
     * Loads the image file
     */
    private void loadImage()
    {
        if(imageName != null)
        {
            image = new ImageIcon(ClassLoader.getSystemResource(imageName));
        }
    }

    /**
     * Gets the name of the rule
     *
     * @return name of the rule
     */
    public String getRuleName()
    {
        return ruleName;
    }

    /**
     * Sets the rule name
     *
     * @param ruleName new name of the rule
     */
    public void setRuleName(String ruleName)
    {
        this.ruleName = ruleName;
    }

    /**
     * Gets the description of the rule
     *
     * @return the description of the rule
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Gets the image icon of the rule
     *
     * @return image icon of the rule
     */
    public ImageIcon getImageIcon()
    {
        return image;
    }

    /**
     * Gets the rule type
     *
     * @return rule type
     */
    public RuleType getRuleType()
    {
        return ruleType;
    }
}