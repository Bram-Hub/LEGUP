package edu.rpi.legup.puzzle.shorttruthtable;

import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class ShortTruthTableImporter extends PuzzleImporter {

    public ShortTruthTableImporter(ShortTruthTable stt) {
        super(stt);
    }


    /**
     * Parse a string into all the cells, the y position of the statement is passed so the y position can be set
     *
     * @param statement
     * @param y
     * @return
     */
    private List<ShortTruthTableCell> getCells(String statement, int y) {
        List<ShortTruthTableCell> cells = new ArrayList<ShortTruthTableCell>();
        //go through each char in the statement and make a cell for it
        for (int i = 0; i < statement.length(); i++) {
            char c = statement.charAt(i);
            ShortTruthTableCell cell = new ShortTruthTableCell(c, ShortTruthTableCellType.getDefaultType(c), new Point(i, y));
            //it is modifiable if the type is unknown
            cell.setModifiable(cell.getType() == ShortTruthTableCellType.UNKNOWN);
            cells.add(cell);
        }
        return cells;
    }


    /**
     * Parses the statementData into all the cells (with symbols) and statements for the
     * puzzle. All cells are set to UNKNWON, it their value is given, it will be set later
     * in the import process.
     * <p>
     * Both allCells and statements act as returns, They should be passed as empty arrays
     *
     * @param statementData The data to be imported
     * @param allCells      returns all the cells as a jagged 2d array
     * @param statements    returns all the statements
     * @return the length, in chars, of the longest statement
     */
    private int parseAllStatementsAndCells(final NodeList statementData,
                                           List<List<ShortTruthTableCell>> allCells,
                                           List<ShortTruthTableStatement> statements) throws InvalidFileFormatException {

        int maxStatementLength = 0;

        //get a 2D arraylist of all the cells
        for (int i = 0; i < statementData.getLength(); i++) {

            //Get the atributes from the statement i in the file
            NamedNodeMap attributeList = statementData.item(i).getAttributes();

            String statementRep = attributeList.getNamedItem("representation").getNodeValue();
            System.out.println("STATEMENT REP: " + statementRep);
            System.out.println("ROW INDEX: " + attributeList.getNamedItem("row_index").getNodeValue());
            //parser time (on statementRep)
            //if (!validGrammar(statementRep)) throw some error
            if (!validGrammar(statementRep)) {
                JOptionPane.showMessageDialog(null, "ERROR: Invalid file syntax");
                throw new InvalidFileFormatException("shorttruthtable importer: invalid sentence syntax");
            }
            int rowIndex = Integer.valueOf(attributeList.getNamedItem("row_index").getNodeValue());

            //get the cells for the statement
            List<ShortTruthTableCell> rowOfCells = getCells(statementRep, rowIndex * 2);
            allCells.add(rowOfCells);
            statements.add(new ShortTruthTableStatement(statementRep, rowOfCells));

            //keep track of the length of the longest statement
            maxStatementLength = Math.max(maxStatementLength, statementRep.length());

        }

        return maxStatementLength;
    }

    private int parseAllStatementsAndCells(String[] statementData,
                                           List<List<ShortTruthTableCell>> allCells,
                                           List<ShortTruthTableStatement> statements) throws IllegalArgumentException {
        int maxStatementLength = 0;

        for (int i = 0; i < statementData.length; i++) {
            if (!validGrammar(statementData[i])) {
                JOptionPane.showMessageDialog(null, "ERROR: Invalid file syntax");
                throw new IllegalArgumentException("shorttruthtable importer: invalid sentence syntax");
            }

            //get the cells for the statement
            List<ShortTruthTableCell> rowOfCells = getCells(statementData[i], i * 2);
            allCells.add(rowOfCells);
            statements.add(new ShortTruthTableStatement(statementData[i], rowOfCells));

            //keep track of the length of the longest statement
            maxStatementLength = Math.max(maxStatementLength, statementData[i].length());
        }

        return maxStatementLength;
    }

    protected boolean validGrammar(String sentence) {
        int open = 0;
        int close = 0;
        char[] valid_characters = new char[]{'^', 'v', '!', '>', '-', '&', '|', '~', '$', '%'};
        for (int i = 0; i < sentence.length(); i++) {
            char s = sentence.charAt(i);
            if (s == '(' || s == ')') {
                switch (s) {
                    case ')':
                        close++;
                        break;
                    case '(':
                        open++;
                        break;
                }
                continue;
            }
            if (!Character.isLetter(s)) {
                boolean valid = false;
                for (char c : valid_characters) {
                    if (c == s) {
                        valid = true;
                        break;
                    }
                }
                if (!valid) {
                    System.out.println("Invalid character");
                    System.out.println(s);
                    return false;
                }
                if (i != sentence.length() - 1) {
                    char next = sentence.charAt(i + 1);
                    if (next != '!' && next != '~') {
                        for (char c : valid_characters) {
                            if (c == next) {
                                System.out.println("Invalid next character");
                                System.out.println(s);
                                System.out.println(next);
                                return false;
                            }
                        }
                    }
                }
            } else {
                if (i != sentence.length() - 1) {
                    if (Character.isLetter(sentence.charAt(i + 1))) {
                        System.out.println("Invalid next character");
                        System.out.println(s);
                        System.out.println(sentence.charAt(i + 1));
                        return false;
                    }
                }
            }
        }
        return open == close;
    }

    private ShortTruthTableBoard generateBoard(List<List<ShortTruthTableCell>> allCells,
                                               List<ShortTruthTableStatement> statements,
                                               int width) {

        //calculate the height for the board
        int height = statements.size() * 2 - 1;

        //instantiate the board with the correct width and height
        ShortTruthTableBoard sttBoard = new ShortTruthTableBoard(width, height,
                statements.toArray(new ShortTruthTableStatement[statements.size()]));

        //set the cells in the board. create not_in_play cells where needed
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                //get the statement index for this row of the table
                int statementIndex = y / 2;

                //get the cell at this location; or create a not_in_play one if necessary
                ShortTruthTableCell cell = null;

                //for a cell to exist at (x, y), it must be a valid row and within the statement length
                if (y % 2 == 0 && x < statements.get(statementIndex).getLength()) {
                    cell = allCells.get(statementIndex).get(x);
                    System.out.println("Importer: check cell statement ref: " + cell.getStatementReference());
                } else {
                    //if it is not a valid cell space, add a NOT_IN_PLAY cell
                    cell = new ShortTruthTableCell(' ', ShortTruthTableCellType.NOT_IN_PLAY, new Point(x, y));
                    cell.setModifiable(false);
                }

                //add the cell to the table
                cell.setIndex(y * width + x);
                sttBoard.setCell(x, y, cell);
            }
        }

        return sttBoard;

    }


    private void setGivenCells(ShortTruthTableBoard sttBoard,
                               Element dataElement,
                               NodeList cellData,
                               List<ShortTruthTableStatement> statements) throws InvalidFileFormatException {


        //if it is normal, set all predicates to true and the conclusion to false
        if (dataElement.getAttribute("normal").equalsIgnoreCase("true")) {
            //set all predicates to true (all but the last one)
            for (int i = 0; i < statements.size() - 1; i++) {
                statements.get(i).getCell().setGiven(ShortTruthTableCellType.TRUE);
            }
            //set the conclusion to false (the last one)
            statements.get(statements.size() - 1).getCell().setGiven(ShortTruthTableCellType.FALSE);
        }

        //set the given cell values
        for (int i = 0; i < cellData.getLength(); i++) {
            //set the value with the factory importer
            ShortTruthTableCell cell = (ShortTruthTableCell) puzzle.getFactory().importCell(cellData.item(i), sttBoard);
            //set the modifiable and given flags
            cell.setModifiable(false);
            cell.setGiven(true);
        }


    }

    @Override
    public boolean acceptsRowsAndColumnsInput() {
        return false;
    }

    @Override
    public boolean acceptsTextInput() {
        return true;
    }

    /**
     * Creates an empty board for building
     *
     * @param rows    the number of rows on the board
     * @param columns the number of columns on the board
     * @throws RuntimeException
     */
    @Override
    public void initializeBoard(int rows, int columns) {

    }

    //STATEMENT IMPORTER

    /**
     * Creates the board for building
     *
     * @param node xml document node
     * @throws InvalidFileFormatException
     */
    @Override
    public void initializeBoard(Node node) throws InvalidFileFormatException {

        try {

            //Check File formatting
            if (!node.getNodeName().equalsIgnoreCase("board")) {
                throw new InvalidFileFormatException("short truth table Importer: cannot find board puzzleElement");
            }
            Element boardElement = (Element) node;
            if (boardElement.getElementsByTagName("data").getLength() == 0) {
                throw new InvalidFileFormatException("short truth table Importer: no statements found for board");
            }


            //get all the cells in a 2D arraylist
            List<List<ShortTruthTableCell>> allCells = new ArrayList<List<ShortTruthTableCell>>();
            //store the statement data structors
            List<ShortTruthTableStatement> statements = new ArrayList<ShortTruthTableStatement>();

            //get the elements from the file
            Element dataElement = (Element) boardElement.getElementsByTagName("data").item(0);
            NodeList statementData = dataElement.getElementsByTagName("statement");
            NodeList cellData = dataElement.getElementsByTagName("cell");


            //Parse the data
            int maxStatementLength = parseAllStatementsAndCells(statementData, allCells, statements);

            //generate the board
            ShortTruthTableBoard sttBoard = generateBoard(allCells, statements, maxStatementLength);

            //set the given cell values
            setGivenCells(sttBoard, dataElement, cellData, statements);

            puzzle.setCurrentBoard(sttBoard);

        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException("short truth table Importer: unknown value where integer expected");
        }
    }

    /**
     * Creates the board for building using statements
     *
     * @param statementInput
     * @throws UnsupportedOperationException
     * @throws IllegalArgumentException
     */
    public void initializeBoard(String[] statementInput) throws UnsupportedOperationException, IllegalArgumentException {
        List<String> statementsList = new LinkedList<>();
        for (String s : statementInput) {
            if (s.strip().length() > 0) {
                statementsList.add(s);
            }
        }
        String[] statementData = statementsList.toArray(new String[statementsList.size()]);

        if (statementData.length == 0) {
            throw new IllegalArgumentException("short truth table Importer: no statements found for board");
        }

        // Store all cells and statements
        List<List<ShortTruthTableCell>> allCells = new ArrayList<>();
        List<ShortTruthTableStatement> statements = new ArrayList<>();

        // Parse the data
        int maxStatementLength = parseAllStatementsAndCells(statementData, allCells, statements);

        // Generate and set the board - don't set given cell values since none are given
        ShortTruthTableBoard sttBoard = generateBoard(allCells, statements, maxStatementLength);
        puzzle.setCurrentBoard(sttBoard);
    }
}
