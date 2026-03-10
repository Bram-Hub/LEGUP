package edu.rpi.legup.model.rules;

import static edu.rpi.legup.model.rules.RuleType.CASE;

import edu.rpi.legup.model.gameboard.Board;
import edu.rpi.legup.model.gameboard.CaseBoard;
import edu.rpi.legup.model.gameboard.PuzzleElement;
import edu.rpi.legup.model.tree.TreeNode;
import edu.rpi.legup.model.tree.TreeTransition;

import java.util.*;

/**
 * CaseRule is an abstract class representing a rule that can be applied with multiple cases in a
 * puzzle board. It defines methods for applying and checking the rule, as well as retrieving the
 * necessary elements.
 */
public abstract class CaseRule extends Rule {

    private final String INVALID_USE_MESSAGE;
    public int MAX_CASES;
    public int MIN_CASES;

    /**
     * CaseRule Constructor creates a new case rule.
     *
     * @param ruleID ID of the rule
     * @param ruleName name of the rule
     * @param description description of the rule
     * @param imageName file name of the image
     */
    public CaseRule(String ruleID, String ruleName, String description, String imageName) {
        super(ruleID, ruleName, description, imageName);
        this.ruleType = CASE;
        this.INVALID_USE_MESSAGE = "Invalid use of the case rule " + this.ruleName;
        this.MAX_CASES = 10;
        this.MIN_CASES = 1; // By default, this will not actually have any effect
    }

    /**
     * Gets the case board that indicates where this case rule can be applied on the given {@link
     * Board}.
     *
     * @param board board to find locations where this case rule can be applied
     * @return a case board
     */
    public abstract CaseBoard getCaseBoard(Board board);

    /**
     * Gets the possible cases for this {@link Board} at a specific {@link PuzzleElement} based on
     * this case rule.
     *
     * @param board the current board state
     * @param puzzleElement equivalent puzzleElement
     * @return a list of elements the specified could be
     */
    public abstract ArrayList<Board> getCases(Board board, PuzzleElement puzzleElement);

    /**
     * Checks whether the {@link TreeTransition} logically follows from the parent node using this
     * rule. In the case rule implementation, sibling transitions will also be updated.
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRule(TreeTransition transition) {
        List<TreeNode> parentNodes = transition.getParents();
        if (parentNodes.size() != 1) {
            return "Must not have multiple parent nodes";
        }

        for (TreeTransition childTrans : parentNodes.getFirst().getChildren()) {
            if (childTrans.getRule() == null
                    || !childTrans.getRule().getClass().equals(this.getClass())) {
                return "All children nodes must be justified with the same case rule.";
            }
        }
        String check = checkRuleRaw(transition);

        // Mark transition and new data as valid or not
        boolean isCorrect = (check == null);
        for (TreeTransition childTrans : parentNodes.getFirst().getChildren()) {
            childTrans.setCorrect(isCorrect);
            for (PuzzleElement element : childTrans.getBoard().getModifiedData()) {
                element.setValid(isCorrect);
            }
        }

        return check;
    }

    /**
     * Checks whether the {@link TreeTransition} logically follows from the parent node using this
     * rule. This method should be overridden in child classes iff there is a systematic way to check
     * the validity of a case rule, such as in an atomic case.
     *
     * @param transition transition to check
     * @return null if the child node logically follow from the parent node, otherwise error message
     */
    @Override
    public String checkRuleRaw(TreeTransition transition){
        // The default functionality of checkRuleRaw should work as follows:
        //  Generate all possible iterations of boards using getCases
        //  Attempt to match the set of boards with the children found in the tre
        //  Return true if one such match exists, false otherwise
        TreeNode parent = transition.getParents().getFirst();

        // If there are no modified cells, then return true if this is the only
        // child. If it has siblings, then it should not be true
        if (transition.getBoard().getModifiedData().isEmpty()) {
            return parent.getChildren().size() == 1 ? null : INVALID_USE_MESSAGE;
        }


        Board board = parent.getBoard();
        List<TreeTransition> childTransitions = parent.getChildren();
        List<Board> childBoards = new ArrayList<>();
        childTransitions.forEach(t -> childBoards.add(t.getBoard()));

        CaseBoard possibleCasesBoard = getCaseBoard(parent.getBoard());

        // Locate a cell where a case rule can be applied
        Set<PuzzleElement<?>> applicableLocations = new HashSet<>();
        for (PuzzleElement<?> element : possibleCasesBoard.getBaseBoard().getPuzzleElements()) {
            if (possibleCasesBoard.isPickable(element, null)) {
                applicableLocations.add(element);
            }
        }

        // For each cell where a case rule can be applied, generate the cases
        for (PuzzleElement<?> element : applicableLocations) {
            ArrayList<Board> boards = getCases(board, element);
            if (boards.size() != childBoards.size()) {
                continue;
            }

            // For each board in the case, map the current boards to
            // respective cases.
            Map<Integer, Integer> indexMap = new HashMap<>();
            int n = boards.size();
            for (int i = 0; i < n; i++) {
                Board caseBoard = boards.get(i);

                // Attempt to match some board i with the caseBoard j
                for (int j = 0; j < n; j++) {
                    Board childBoard = childBoards.get(j);

                    // If they match, and another child is already matching,
                    // the case rule was applied incorrectly. Otherwise,
                    // move to the next caseBoard/childBoard pairing
                    if (caseBoard.equalsBoard(childBoard)) {
                        if (indexMap.containsKey(j)) {
                            break;
                        }
                        indexMap.put(j, i);
                        break;
                    }
                }

                // If we couldn't find a match for board i, then this doesn't work
                if (indexMap.size() <= i) {
                    break;
                }
            }

            // If we have found a mapping that maps the n boards to each other,
            // this is a valid application of the case rule
            if (indexMap.size() == n) {
                return null;
            }
        }
        return INVALID_USE_MESSAGE;
    }

    /**
     * Checks whether the child node logically follows from the parent node at the specific
     * puzzleElement index using this rule.
     *
     * @param transition transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified
     *     puzzleElement, otherwise error message
     */
    @Override
    public String checkRuleAt(TreeTransition transition, PuzzleElement puzzleElement) {
        return this.INVALID_USE_MESSAGE;
    }

    /**
     * Checks whether the child node logically follows from the parent node at the specific
     * puzzleElement index using this rule. This method is the one that should overridden in child
     * classes.
     *
     * @param transition transition to check
     * @param puzzleElement equivalent puzzleElement
     * @return null if the child node logically follow from the parent node at the specified
     *     puzzleElement, otherwise error message
     */
    @Override
    public abstract String checkRuleRawAt(TreeTransition transition, PuzzleElement puzzleElement);

    /**
     * Returns the elements necessary for the cases returned by getCases(board,puzzleElement) to be
     * valid Overridden by case rules dependent on more than just the modified data
     *
     * @param board board state at application
     * @param puzzleElement selected puzzleElement
     * @return List of puzzle elements (typically cells) this application of the case rule depends
     *     upon. Defaults to any element modified by any case
     */
    public List<PuzzleElement> dependentElements(Board board, PuzzleElement puzzleElement) {
        List<PuzzleElement> elements = new ArrayList<>();

        List<Board> cases = getCases(board, puzzleElement);
        for (Board caseBoard : cases) {
            Set<PuzzleElement> data = caseBoard.getModifiedData();
            for (PuzzleElement element : data) {
                if (!elements.contains(board.getPuzzleElement(element))) {
                    elements.add(board.getPuzzleElement(element));
                }
            }
        }

        return elements;
    }
}
