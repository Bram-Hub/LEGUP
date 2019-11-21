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
            if (boardElement.getElementsByTagName("statements").getLength() == 0) {
                throw new InvalidFileFormatException("short truth table Importer: no statements found for board");
            }


            //store all the statements in a list
            List<ShortTruthTableStatement> statements = new ArrayList<ShortTruthTableStatement>();

            //get the elements from the file
            Element dataElement = (Element) boardElement.getElementsByTagName("cells").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("cell");

            for (int i = 0; i < elementDataList.getLength(); i++) {

                //Get the atributes from the statement i in the file
                NamedNodeMap attributeList = elementDataList.item(i).getAttributes();
                String statementRep = attributeList.getNamedItem("representation").getNodeValue();
                int rowIndex = Integer.valueOf(attributeList.getNamedItem("row_index").getNodeValue());
                ShortTruthTableCellType cellType = ShortTruthTableCellType.valueOf(attributeList.getNamedItem("cell_type").getNodeValue());

                //construct the statement
                ShortTruthTableStatement statement = new ShortTruthTableStatement(statementRep);
                statement.setCellLocations(rowIndex);
                statement.getCell().setData(cellType);

                //make the cell for the statement modifiable or not, based on the cell type
                if (cellType != ShortTruthTableCellType.UNKNOWN) {
                    statement.setModifiable(false);
                    statement.setGiven(true);
                }

                //add statement i to the list of statements
                statements.add(statement);
            }


            //calculate the width and height for the board
            int width = 0;
            for(ShortTruthTableStatement statement : statements)
                width = Math.max(width, statement.getLength());
            int height = statements.size();

            //instantiate the board with the correct width and height
            ShortTruthTableBoard sttBoard = new ShortTruthTableBoard(width, height, statements);

//            sttBoard.setCell(loc.x, loc.y, cell);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (sttBoard.getCell(x, y) == null) {
                        ShortTruthTableCell cell = new ShortTruthTableCell(ShortTruthTableCellType.OUT_OF_PLAY, new Point(x, y));
                        cell.setIndex(y * height + x);
                        cell.setModifiable(true);
                        sttBoard.setCell(x, y, cell);
                    }
                }
            }

            puzzle.setCurrentBoard(nurikabeBoard);

        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException("short truth table Importer: unknown value where integer expected");
        }


    }





    //CELL IMPORTER OLD

    /*
    /**
     * Creates the board for building
     *
     * @param node xml document node
     * @throws InvalidFileFormatException
     */
    /*
    @Override
    public void initializeBoard(Node node) throws InvalidFileFormatException {

        try {

            //Check File formatting
            if (!node.getNodeName().equalsIgnoreCase("board")) {
                throw new InvalidFileFormatException("short truth table Importer: cannot find board puzzleElement");
            }
            Element boardElement = (Element) node;
            if (boardElement.getElementsByTagName("cells").getLength() == 0) {
                throw new InvalidFileFormatException("short truth table Importer: no puzzleElement found for board");
            }


            //Get the width and the height of the board
            int width = 0;
            int height = 0;
            if (!boardElement.getAttribute("size").isEmpty()) {
                width = height = Integer.valueOf(boardElement.getAttribute("size"));
            } else if (!boardElement.getAttribute("width").isEmpty() && !boardElement.getAttribute("height").isEmpty()) {
                width = Integer.valueOf(boardElement.getAttribute("width"));
                height = Integer.valueOf(boardElement.getAttribute("height"));
            }else{
                throw new InvalidFileFormatException("short truth table Importer: invalid board dimensions");
            }

            //instantiate the board with the correct width and height
            ShortTruthTableBoard sttBoard = new ShortTruthTableBoard(width, height);


            Element dataElement = (Element) boardElement.getElementsByTagName("cells").item(0);
            NodeList elementDataList = dataElement.getElementsByTagName("cell");

            for (int i = 0; i < elementDataList.getLength(); i++) {
                ShortTruthTableCell cell = (ShortTruthTableCell) puzzle.getFactory().importCell(elementDataList.item(i), sttBoard);
                Point loc = cell.getLocation();

                if (cell.getData() != ShortTruthTableCellType.UNKNOWN) {
                    cell.setModifiable(false);
                    cell.setGiven(true);
                }
                sttBoard.setCell(loc.x, loc.y, cell);
            }

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (sttBoard.getCell(x, y) == null) {
                        ShortTruthTableCell cell = new ShortTruthTableCell(ShortTruthTableCellType.OUT_OF_PLAY, new Point(x, y));
                        cell.setIndex(y * height + x);
                        cell.setModifiable(true);
                        sttBoard.setCell(x, y, cell);
                    }
                }
            }

            puzzle.setCurrentBoard(nurikabeBoard);

        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException("short truth table Importer: unknown value where integer expected");
        }


    }*/




}