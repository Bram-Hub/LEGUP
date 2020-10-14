package edu.rpi.legup.puzzle.skyscrapers;

import edu.rpi.legup.model.PuzzleImporter;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;

public class SkyscrapersImporter extends PuzzleImporter {
  public SkyscrapersImporter(Skyscrapers skyscrapers) {
    super(skyscrapers);
  }

  /**
   * Creates board for building
   * @param node xml document node
   * @throws InvalidFileFormatException
   */
   @Override
   public void initializeBoard(Node node) throws InvalidFileFormatException {
     try{
       if(!node.getNodeName().equalsIgnoreCase("board")) {
         throw new InvalidFileFormatException("Skyscrapers Imprter: cannot find board puzzleElement");
       }
       Element boardElement = (Element) node;
       //make sure cells are empty
       if(boardElement.getElementsByTagName("cells").getLength() != 0) {
         throw new InvalidFileFormatException("Skyscrapers Importer: cells should be empty");
       }

       //needs implementing
       //SkyscrapersBoard skyscrapersBoard = null;
       //handle using size or width/length
       if(!boardElement.getAttribute("size").isEmpty()) {
         int size = Integer.valueOf(boardElement.getAttribute("size"));
         //skyscrapersBoard = new SkyscrapersBoard(size);
       } else if(!boardElement.getAttribute("width").isEmpty() && !boardElement.getAttribute("height").isEmpty()) {
         int width = Integer.valueOf(boardElement.getAttribute("width"));
         int height = Integer.valueOf(boardElement.getAttribute("height"));
         //skyscrapersBoard = new SkyscrapersBoard(width, height);
       }

       if(treeTentBoard == null) {
         throw new InvalidFileFormatException("Skyscrapers Importer: invalid board dimensions");
       }

       NodeList axes = boardElement.getElementsByTagName("axis");
       if (axes.getLength() != 4) {
         throw new InvalidFileFormatException("Skyscrapers Importer: cannot find axes");
       }

       Element axisN = (Element) axes.item(0);
       Element axisS = (Element) axes.item(1);
       Element axisE = (Element) axes.item(2);
       Element axisW = (Element) axes.item(3);

       if(!axisN.hasAttribute("side")){
         throw new InvalidFileFormatException("Skyscrapers Importer: side attribute of axis not set");
       }

       String sideN = axisN.getAttribute("side");
       String sideS = axisS.getAttribute("side");
       String sideE = axisE.getAttribute("side");
       String sideW = axisW.getAttribute("side");

       //add some way to make sure axes are all on different sides
       //add checks to make sure axes are in the order: North, South, East, then west

       NodeList northClues = sideN.getElementsByTagName("clue");
       NodeList southClues = sideS.getElementsByTagName("clue");
       NodeList eastClues = sideE.getElementsByTagName("clue");
       NodeList westClues = sideW.getElementsByTagName("clue");

       //check sizes of clues
       //int height = skyscrapersBoard.getHeight();
       //int width = skyscrapersBoard.getWidth();
       if(northClues.getLength() != width || southClues.getLength() != width || eastClues.getLength() != height || westClues.getLength() != height) {
         throw new InvalidFileFormatException("Skyscrapers Importer: the number of clues must match the dimensions of the board");
       }

       for(int i = 0; i < height; i++) {
         Element clueE = (Element) eastClues.item(i);
         Element clueW = (Element) westClues.item(i);
         int eValue = Integer.valueOf(clueE.getAttribute("value"));
         int wValue = Integer.valueOf(clueW.getAttribute("value"));

         //need to implement way to set the clue values for a board
         //something like:
         //skyscrapersBoard.getEastClues().set(i, new SkyscrapersClue(value, i+1, SkyscrapersType.CLUE_EAST));
         //skyscrapersBoard.getWestClues().set(i, new SkyscrapersClue(value, i+1, SkyscrapersType.CLUE_WEST));
       }

       //do same thing for North and South
       for(int i = 0; i < width; i++) {
         Element clueN = (Element) northClues.item(i);
         Element clueS = (Element) southClues.item(i);
         int nValue = Integer.valueOf(clueN.getAttribute("value"));
         int sValue = Integer.valueOf(clueS.getAttribute("value"));

         //do same as above loop for north and south
       }

       puzzle.setCurrentBoard(skyscrapersBoard);

     } catch (NumberFormatException e) {
       throw new InvalidFileFormatException("Skyscrapers Importer: unknown value where integer expected");
     }
   }
}
