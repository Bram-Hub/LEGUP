package edu.rpi.legup.history;

/**
 * The CommandError enum represents various error conditions that can occur when executing or
 * validating commands related to tree elements in the proof tree.
 * Each error condition is associated with a descriptive message.
 */
public enum CommandError {
    NO_SELECTED_VIEWS("The selection does not have any tree elements."),
    ONE_SELECTED_VIEW("The selection must have exactly one tree element."),
    UNMODIFIABLE_BOARD("The selection contains a board which is not modifiable."),
    UNMODIFIABLE_DATA("The selection contains a board where the data element is not modifiable."),
    UNMODIFIABLE_DATA_CASE_RULE("The proof tree contains a future case rule, causing previous transitions to become unmodifiable."),
    CONTAINS_ROOT("The selection contains the root tree node."),
    ONE_CHILD("The selection contains a tree node that does not have exactly one child."),
    ADD_WITH_CHILD("The selection contains a tree transition that already has a child tree node."),
    TWO_TO_MERGE("The selection must have at least two tree nodes to merge."),
    CONTAINS_MERGE("The selection contains a merging transition."),
    ADD_TO_MERGE("The selection contains a tree node that merges."),
    NO_CHILDREN("The selection contains a tree node that has children."),
    SELECTION_CONTAINS_NODE("The selection contains a tree node."),
    SELECTION_CONTAINS_TRANSITION("The selection contains a tree transition."),
    DEFAULT_APPLICATION("[Apply Default Rule Application]");

    private String value;

    /**
     * Constructs a CommandError with the specified error message
     *
     * @param value The error message associated with the command error
     */
    CommandError(String value) {
        this.value = value;
    }

    /**
     * Returns the error message associated with this CommandError
     *
     * @return The error message
     */
    @Override
    public String toString() {
        return value;
    }
}
