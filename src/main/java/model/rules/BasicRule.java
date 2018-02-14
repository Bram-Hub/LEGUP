package model.rules;

import static model.rules.RuleType.BASIC;

public abstract class BasicRule extends Rule
{
    /**
     * BasicRule Constructor - creates a new basic rule
     *
     * @param ruleName name of the rule
     * @param imageName file name of the image
     * @param description description of the rule
     */
    public BasicRule(String ruleName, String imageName, String description)
    {
        super(ruleName, imageName, description);
        ruleType = BASIC;
    }
}
