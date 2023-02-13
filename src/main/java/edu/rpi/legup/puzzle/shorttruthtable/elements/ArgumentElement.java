package edu.rpi.legup.puzzle.shorttruthtable.elements;

import edu.rpi.legup.model.elements.NonPlaceableElement;

public class ArgumentElement extends NonPlaceableElement
{
    private char letter = 'A';
    public ArgumentElement() {
        super("STTT-UNPL-0004", "Argument Element", "Argument of logic statement element", imageName);
    }

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }
}
