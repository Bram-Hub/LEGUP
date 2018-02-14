package model.rules;

import model.gameboard.Board;

import javax.swing.ImageIcon;

import java.net.URL;

public abstract class Rule
{
    protected String ruleName;
    protected String description;
    protected String imageName;
    protected ImageIcon image;
    protected RuleType ruleType;
    protected boolean isAiUsable;

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
        isAiUsable = true;
        loadImage();
    }

    /**
     * Checks whether the finalBoard logically follows from the initializeBoard using this rule
     *
     * @param initialBoard initial state of the board
     * @param finalBoard final state of the board
     * @return null if the finalBoard logically follow from the initializeBoard, otherwise error message
     */
    public abstract String checkRule(Board initialBoard, Board finalBoard);

    /**
     * Checks whether the finalBoard logically follows from the initializeBoard
     * at the specific element index using this rule
     *
     * @param initialBoard initial state of the board
     * @param finalBoard final state of the board
     * @param elementIndex index of the element
     * @return null if the finalBoard logically follow from the initializeBoard at the specified element,
     * otherwise error message
     */
    public abstract String checkRuleAt(Board initialBoard, Board finalBoard, int elementIndex);

    /**
     * Checks whether the finalBoard logically follows from the initializeBoard using this rule
     * and if so will perform the default application of the rule
     *
     * @param initialBoard initial state of the board
     * @param finalBoard final state of the board
     * @return true if the finalBoard logically follow from the initializeBoard and accepts the changes
     * to the board, otherwise false
     */
    public abstract boolean doDefaultApplication(Board initialBoard, Board finalBoard);

    /**
     * Checks whether the finalBoard logically follows from the initializeBoard at the
     * specific element index using this rule and if so will perform the default application of the rule
     *
     * @param initialBoard initial state of the board
     * @param finalBoard final state of the board
     * @return true if the finalBoard logically follow from the initializeBoard and accepts the changes
     * to the board, otherwise false
     */
    public abstract boolean doDefaultApplicationAt(Board initialBoard, Board finalBoard, int elementIndex);

    /**
     * Loads the image file
     */
    public void loadImage()
    {
        if(imageName != null)
        {
            URL res = ClassLoader.getSystemResource(imageName);
            if(res == null)
            {
                //throw new Error(String.format("Image \"%s\" does not exist (needed for \"%s\")", imageName, this.getClass()));
            }
            image = new ImageIcon(imageName);
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