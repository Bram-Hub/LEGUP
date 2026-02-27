package edu.rpi.legup.utility.svg;

record Token(int type, String text) {}

class SVGTokenizer {
    public static final int IDENT = 0;
    public static final int POUND = 1;
    public static final int NUMBER = 2;
    public static final int PERCENT = 3;
    public static final int LPAREN = 4;
    public static final int RPAREN = 5;
    public static final int COMMA = 6;
    public static final int SLASH = 7;
    public static final int EOF = 8;

    private final String input;
    private int pos = 0;

    SVGTokenizer(String input) {
        this.input = input;
    }

    Token next() {
        skipWhitespace();
        if (pos >= input.length()) {
            return new Token(EOF, "");
        }
        char c = input.charAt(pos);
        switch (c) {
            case '#' -> {
                pos++;
                return new Token(POUND, "#");
            }
            case '(' -> {
                pos++;
                return new Token(LPAREN, "(");
            }
            case ')' -> {
                pos++;
                return new Token(RPAREN, ")");
            }
            case ',' -> {
                pos++;
                return new Token(COMMA, ",");
            }
            case '/' -> {
                pos++;
                return new Token(SLASH, "/");
            }
        }
        // Identifier
        if (Character.isLetter(c)) {
            int start = pos;
            while (pos < input.length() && Character.isLetter(input.charAt(pos))) {
                pos++;
            }
            return new Token(IDENT, input.substring(start, pos));
        }
        // Number or percent
        if (c == '+' || c == '-' || Character.isDigit(c) || c == '.') {
            int start = pos;
            do {
                pos++;
            } while (pos < input.length()
                    && (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '.'));
            if (pos < input.length() && input.charAt(pos) == '%') {
                pos++;
                return new Token(PERCENT, input.substring(start, pos));
            }
            return new Token(NUMBER, input.substring(start, pos));
        }
        throw new IllegalArgumentException("Unexpected character: " + c);
    }

    private void skipWhitespace() {
        while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) {
            pos++;
        }
    }
}
