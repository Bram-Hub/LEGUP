package edu.rpi.legup.puzzle.shorttruthtable;

import edu.rpi.legup.model.PuzzleExporter;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import org.w3c.dom.Document;

public class ShortTruthTableExporter extends PuzzleExporter {

    public ShortTruthTableExporter(ShortTruthTable stt) {
        super(stt);
    }

    @Override
    protected org.w3c.dom.Element createBoardElement(Document newDocument) {
        ShortTruthTableBoard board = (ShortTruthTableBoard) puzzle.getTree().getRootNode().getBoard();
        org.w3c.dom.Element boardElement = newDocument.createElement("board");

        org.w3c.dom.Element dataElement = newDocument.createElement("data");

        ShortTruthTableStatement[] statements = board.getStatements();
        for (int i = 0; i < statements.length; i++) {
            org.w3c.dom.Element statementElement = newDocument.createElement("statement");
            System.out.println(statements[i].toString());
            statementElement.setAttribute("representation", statements[i].getStringRep());
            statementElement.setAttribute("row_index", String.valueOf(i));
            dataElement.appendChild(statementElement);
        }


        for (PuzzleElement puzzleElement : board.getPuzzleElements()) {
            ShortTruthTableCell cell = board.getCellFromElement(puzzleElement);
            if (!cell.getType().isTrueOrFalse()) continue;

            org.w3c.dom.Element cellElement = puzzle.getFactory().exportCell(newDocument, puzzleElement);
            dataElement.appendChild(cellElement);
        }

        boardElement.appendChild(dataElement);
        return boardElement;
    }
}
