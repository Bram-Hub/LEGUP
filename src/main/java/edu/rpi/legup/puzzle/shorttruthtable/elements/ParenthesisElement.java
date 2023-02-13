package edu.rpi.legup.puzzle.shorttruthtable.elements;

import edu.rpi.legup.model.elements.NonPlaceableElement;

public class ParenthesisElement extends NonPlaceableElement {

    private char parenthesis = '(';
    public ParenthesisElement() {
        super("STTT-UNPL-0007", "Parenthesis Element", "Parenthesis element", imageName);
    }

    public char getParenthesis() {
        return parenthesis;
    }

    public void setOpenParenthesis() {
        this.parenthesis = '(';
    }

    public void setClosedParenthesis() {
        this.parenthesis = ')';
    }
}
