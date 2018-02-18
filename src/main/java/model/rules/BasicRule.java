package model.rules;

import static model.rules.RuleType.BASIC;

public abstract class BasicRule extends Rule
{
    /**
     * BasicRule Constructor - creates a new basic rule
     *
     * @param ruleName name of the rule
     * @param description description of the rule
     * @param imageName file name of the image
     */
    public BasicRule(String ruleName, String description, String imageName)
    {
        super(ruleName, description, imageName);
        ruleType = BASIC;
    }
}
