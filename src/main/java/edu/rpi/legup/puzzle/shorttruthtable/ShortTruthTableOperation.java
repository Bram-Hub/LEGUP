package edu.rpi.legup.puzzle.shorttruthtable;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class ShortTruthTableOperation{

    public static final char AND_1 = '^';
    public static final char OR_1 = 'v';
    public static final char NOT_1 = '!';
    public static final char CONDITIONAL_1 = '>';
    public static final char BICONDITIONAL_1 = '-';
    public static final char AND_2 = '&';
    public static final char OR_2 = '|';
    public static final char NOT_2 = '~';
    public static final char CONDITIONAL_2 = '$';
    public static final char BICONDITIONAL_2 = '%';

    private ShortTruthTableOperation(){}


    public static String getLogicSymbol(char c){
        switch(c){
            case AND_1: return "\u2227";
            case OR_1: return "\u2228";
            case NOT_1: return "\u00AC";
            case CONDITIONAL_1: return "\u2192";
            case BICONDITIONAL_1: return "\u2194";
            case AND_2: return "\u2227";
            case OR_2: return "\u2228";
            case NOT_2: return "\u00AC";
            case CONDITIONAL_2: return "\u2192";
            case BICONDITIONAL_2: return "\u2194";
        }
        return "" + c;
    }

    public static String getRuleName(char operation){
        switch(operation){
            case AND_1: return "And";
            case OR_1: return "Or";
            case NOT_1: return "Not";
            case CONDITIONAL_1: return "Conditional";
            case BICONDITIONAL_1: return "Biconditional";
            case AND_2: return "And";
            case OR_2: return "Or";
            case NOT_2: return "Not";
            case CONDITIONAL_2: return "Conditional";
            case BICONDITIONAL_2: return "Biconditional";
        }
        return null;
    }


    public static boolean isOperation(char c){
        return c == AND_1 ||
                c == OR_1 ||
                c == NOT_1 ||
                c == CONDITIONAL_1 ||
                c == BICONDITIONAL_1 ||
                c == AND_2 ||
                c == OR_2 ||
                c == NOT_2 ||
                c == CONDITIONAL_2 ||
                c == BICONDITIONAL_2;
    }


}
