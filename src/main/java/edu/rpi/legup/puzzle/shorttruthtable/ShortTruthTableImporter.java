package edu.rpi.legup.puzzle.shorttruthtable;

import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

import java.awt.*;

import java.util.List;
import java.util.ArrayList;

class ShortTruthTableImporter extends PuzzleImporter{

    public ShortTruthTableImporter(ShortTruthTable stt) {
        super(stt);
    }




    private List<ShortTruthTableCell> getCells(String statement, int rowIndex){
        List<ShortTruthTableCell> cells = new ArrayList<ShortTruthTableCell>();
        for(int i = 0; i<statement.length(); i++){
            char c = statement.charAt(i);
            ShortTruthTableCell cell = new ShortTruthTableCell(c, ShortTruthTableCellType.getDefaultType(c), new Point(i, rowIndex));
            cell.setModifiable(statement.charAt(i)!='(' && statement.charAt(i)!=')');//TODO - cell type = unkonwn
            cells.add(cell);
        }
        return cells;
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
            //store the max length
            int maxStatementLength = 0;

            //get the elements from the file
            Element dataElement = (Element) boardElement.getElementsByTagName("data").item(0);
            NodeList statementData = dataElement.getElementsByTagName("statement");
            NodeList cellData = dataElement.getElementsByTagName("cell");

            //get a 2D arraylist of all the given cells
            for (int i = 0; i < statementData.getLength(); i++) {

                //Get the atributes from the statement i in the file
                NamedNodeMap attributeList = statementData.item(i).getAttributes();
                String statementRep = attributeList.getNamedItem("representation").getNodeValue();
                int rowIndex = Integer.valueOf(attributeList.getNamedItem("row_index").getNodeValue());

                //get the cells for the statement
                List<ShortTruthTableCell> rowOfCells = getCells(statementRep, rowIndex*2);
                allCells.add(rowOfCells);
                statements.add(new ShortTruthTableStatement(statementRep, rowOfCells));

                //keep track of the length of the longest statement
                maxStatementLength = Math.max(maxStatementLength, statementRep.length());

            }

            //go through the given cells
            for(int i = 0; i<cellData.getLength(); i++){

                //get the atributes for the cell
                NamedNodeMap attributeList = cellData.item(i).getAttributes();
                int rowIndex = Integer.valueOf(attributeList.getNamedItem("row_index").getNodeValue());
                int charIndex = Integer.valueOf(attributeList.getNamedItem("char_index").getNodeValue());
                String cellType = attributeList.getNamedItem("type").getNodeValue();

                //modify the appropriet cell
                ShortTruthTableCell cell = allCells.get(rowIndex).get(charIndex);
                cell.setData(ShortTruthTableCellType.valueOf(cellType));
                cell.setModifiable(false);
                cell.setGiven(true);

            }


            if(dataElement.getAttribute("normal").equalsIgnoreCase("true")){
                for(int i = 0; i<statements.size()-1; i++)
                    statements.get(i).getCell().setGiven(ShortTruthTableCellType.TRUE);
                statements.get(statements.size()-1).getCell().setGiven(ShortTruthTableCellType.FALSE);
            }


            //calculate the width and height for the board
            int width = maxStatementLength;
            int height = statements.size()*2-1;

            System.out.println("Board dimentions "+width+", "+height);

            //instantiate the board with the correct width and height
            ShortTruthTableBoard sttBoard = new ShortTruthTableBoard(width, height, statements.toArray(new ShortTruthTableStatement[statements.size()]));

            //set the cells in the board. create not_in_play cells where needed
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {

                    //get the statement index for this row of the table
                    int statementIndex = y/2;

                    //get the cell at this location; or create a not_in_play one if necessary
                    ShortTruthTableCell cell = null;

                    //for a cell to exist at (x, y), it must be a valid row and within the statment length
                    if(y%2==0 && x < statements.get(statementIndex).getLength()) {
                        cell = allCells.get(statementIndex).get(x);
                        System.out.println("Importer: check cell statement ref: "+cell.getStatementRefference());
                    }else{
                        //if it is not a valid cell space, add a NOT_IN_PLAY cell
                        cell = new ShortTruthTableCell(' ', ShortTruthTableCellType.NOT_IN_PLAY, new Point(x, y));
                        cell.setModifiable(false);
                    }

                    //add the cell to the table
                    cell.setIndex(y * width + x);
                    sttBoard.setCell(x, y, cell);
                }
            }

            //debug print
//            System.out.println("Imprter");
//            for (int y = 0; y < height; y+=2)
//                for (int x = 0; x < width; x++){
//                    System.out.println("Cell  "+sttBoard.getCell(x, y));
//                    System.out.println("State "+((ShortTruthTableCell)sttBoard.getCell(x, y)).getStatementRefference());
//                    System.out.println();
//                }
//            System.out.println("\n\n\n\n");

            puzzle.setCurrentBoard(sttBoard);

        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException("short truth table Importer: unknown value where integer expected");
        }


    }




}