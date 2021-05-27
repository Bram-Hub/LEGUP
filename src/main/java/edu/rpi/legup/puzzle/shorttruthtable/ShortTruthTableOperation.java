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


    public static String getLogicSymbol(char c){
        switch(c){
            case AND: return "\u2227";
            case OR: return "\u2228";
            case NOT: return "\u00AC";
            case CONDITIONAL: return "\u2192";
            case BICONDITIONAL: return "\u2194";
        }
        return "" + c;
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
