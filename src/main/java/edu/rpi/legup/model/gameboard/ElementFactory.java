package edu.rpi.legup.model.gameboard;

import edu.rpi.legup.model.Goal;
import edu.rpi.legup.model.GoalType;
import edu.rpi.legup.save.InvalidFileFormatException;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * ElementFactory is an abstract class for importing and exporting {@link PuzzleElement} instances.
 */
public abstract class ElementFactory {

    /**
     * Creates a {@link PuzzleElement} based on the xml document Node and adds it to the board.
     *
     * @param node node that represents the puzzleElement
     * @param board board to add the newly created cell
     * @return newly created cell from the xml document Node
     * @throws InvalidFileFormatException thrown if the xml node is invalid for the specific puzzle
     *     element
     */
    public abstract @NotNull PuzzleElement importCell(@NotNull Node node, @NotNull Board board)
            throws InvalidFileFormatException;

    /**
     * @param node node that represents the Goal object
     * @param board board to add the goal to
     * @return newly created Goal from the xml document Node
     * @throws InvalidFileFormatException thrown if the xml node is malformed
     */
    public Goal importGoal(Node node, Board board) throws InvalidFileFormatException {
        try {
            if (!node.getNodeName().equalsIgnoreCase("goal")) {
                throw new InvalidFileFormatException(
                        "Factory: unknown puzzleElement puzzleElement");
            }

            NamedNodeMap attributeList = node.getAttributes();
            String goalTypeString = attributeList.getNamedItem("type").getNodeValue();
            GoalType goalType =
                    goalTypeString == null
                            ? GoalType.DEFAULT
                            : GoalType.valueOf(goalTypeString.toUpperCase());

            try {
                String assumeSolution = attributeList.getNamedItem("assumeSolution").getNodeValue();
                if (!(assumeSolution.equalsIgnoreCase("true")
                        || assumeSolution.equalsIgnoreCase("false"))) {
                    throw new InvalidFileFormatException(
                            "Field 'assumeSolution' must be null, true, or false.");
                }
                boolean assume = (assumeSolution.equalsIgnoreCase("true"));
                return new Goal(goalType, assume);
            } catch (NullPointerException e) {
                return new Goal(goalType, false);
            }

        } catch (NumberFormatException e) {
            throw new InvalidFileFormatException("Factory: unknown value where integer expected");
        } catch (NullPointerException e) {
            throw new InvalidFileFormatException("Factory: could not find attribute(s)");
        }
    }

    /**
     * Creates a xml document {@link PuzzleElement} from a cell for exporting.
     *
     * @param document xml document
     * @param puzzleElement PuzzleElement cell
     * @return xml PuzzleElement
     */
    public abstract @NotNull Element exportCell(
            @NotNull Document document, @NotNull PuzzleElement puzzleElement);
}
