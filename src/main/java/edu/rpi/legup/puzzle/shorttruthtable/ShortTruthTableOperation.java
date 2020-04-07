package edu.rpi.legup.puzzle.shorttruthtable;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class ShortTruthTableOperation{

    public static final char AND = '^';
    public static final char OR = '|';
    public static final char NOT = '~';
    public static final char CONDITIONAL = '>';
    public static final char BICONDITIONAL = '-';

    private ShortTruthTableOperation(){}


    public static String getLogicSymbol(char operation){
        switch(operation){
            case AND: return "∧";
            case OR: return "∨";
            case NOT: return "¬";
            case CONDITIONAL: return "→";
            case BICONDITIONAL: return "↔";
        }
        return null;
    }

    public static String getRuleName(char operation){
        switch(operation){
            case AND: return "And";
            case OR: return "Or";
            case NOT: return "Not";
            case CONDITIONAL: return "Conditional";
            case BICONDITIONAL: return "Biconditional";
        }
        return null;
    }


    public static boolean isOperation(char c){
        return c == AND ||
                c == OR ||
                c == NOT ||
                c == CONDITIONAL ||
                c == BICONDITIONAL;
    }


}