package edu.rpi.legup.puzzle.yinyang;

import edu.rpi.legup.model.PuzzleImporter;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class YinYangImporter extends PuzzleImporter {
    public YinYangImporter(YinYang puzzle) {
        super(puzzle);
    }

    @Override
    public void initializeBoard(Element boardElement) {
        int width = Integer.parseInt(boardElement.getAttribute("width"));
        int height = Integer.parseInt(boardElement.getAttribute("height"));
        YinYangBoard board = new YinYangBoard(width, height);

        NodeList cellNodes = boardElement.getElementsByTagName("cell");
        for (int i = 0; i < cellNodes.getLength(); i++) {
            Element cellElement = (Element) cellNodes.item(i);
            int x = Integer.parseInt(cellElement.getAttribute("x"));
            int y = Integer.parseInt(cellElement.getAttribute("y"));
            YinYangType type = YinYangType.valueOf(cellElement.getAttribute("type"));

            YinYangCell cell = board.getCell(x, y);
            cell.setType(type);
        }

        puzzle.setCurrentBoard(board);
    }
}