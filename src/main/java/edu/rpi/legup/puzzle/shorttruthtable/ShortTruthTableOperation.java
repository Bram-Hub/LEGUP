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


    private static final Map<Character, String> logicSymbolMap;
    static {
        Map<Character, String> aMap = new TreeMap();
        aMap.put(NOT, "¬");
        aMap.put(AND, "∧");
        aMap.put(OR, "∨");
        aMap.put(CONDITIONAL, "→");
        aMap.put(BICONDITIONAL, "↔");
        logicSymbolMap = Collections.unmodifiableMap(aMap);
    }


    public static String getLogicSymbol(char c){
        String s = logicSymbolMap.get(c);
        if(s == null) return String.valueOf(c);
        return s;
    }

    public static boolean isOperation(char c){
        return c == AND ||
                c == OR ||
                c == NOT ||
                c == CONDITIONAL ||
                c == BICONDITIONAL;
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

}