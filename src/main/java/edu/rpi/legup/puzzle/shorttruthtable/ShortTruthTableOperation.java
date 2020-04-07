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

    private static final Map<Character, ShortTruthTableCellType[][]> contradictionPatterns;
    static {

        ShortTruthTableCellType T = ShortTruthTableCellType.TRUE;
        ShortTruthTableCellType F = ShortTruthTableCellType.FALSE;
        ShortTruthTableCellType n = null;

        ShortTruthTableCellType[][] and = {
                {n, T, F},
                {F, T, n},
                {T, F, T},
        };

        ShortTruthTableCellType[][] or = {
                {n, F, T},
                {T, F, n},
                {F, T, F},
        };

        ShortTruthTableCellType[][] not = {
                {n, T, T},
                {n, F, F}
        };

        ShortTruthTableCellType[][] conditional = {
                {n, F, T},
                {F, F, n},
                {T, T, F},
        };

        ShortTruthTableCellType[][] biconditional = {
                {T, T, F},
                {F, T, T},
                {T, F, T},
                {F, F, F},
        };

        Map<Character, ShortTruthTableCellType[][]> map = new TreeMap();
        map.put(AND, and);
        map.put(OR, or);
        map.put(NOT, not);
        map.put(CONDITIONAL, conditional);
        map.put(BICONDITIONAL, biconditional);
        contradictionPatterns = Collections.unmodifiableMap(map);
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

    public static ShortTruthTableCellType[][] getContradictionPatterns(char symbol){
        return contradictionPatterns.get(symbol);
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