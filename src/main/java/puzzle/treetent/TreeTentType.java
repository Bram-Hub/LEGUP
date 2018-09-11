package puzzle.treetent;

public enum TreeTentType
{
    UNKNOWN, TREE, GRASS, TENT, CLUE_NORTH, CLUE_EAST, CLUE_SOUTH, CLUE_WEST;
    public int toValue()
    {
        return this.ordinal();
    }
}