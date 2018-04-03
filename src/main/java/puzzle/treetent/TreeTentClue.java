package puzzle.treetent;

import model.gameboard.ElementData;

public class TreeTentClue extends ElementData
{

    private TreeTentType type;

    public TreeTentClue(int value, int index, TreeTentType type)
    {
        super(value);
        this.index = index;
        this.type = type;
    }

    public static String colNumToString(int col)
    {
        final StringBuilder sb = new StringBuilder();
        col--;
        while(col >= 0)
        {
            int numChar = (col % 26) + 65;
            sb.append((char) numChar);
            col = (col / 26) - 1;
        }
        return sb.reverse().toString();
    }

    public static int colStringToColNum(String col)
    {
        int result = 0;
        for(int i = 0; i < col.length(); i++)
        {
            result *= 26;
            result += col.charAt(i) - 'A' + 1;
        }
        return result;
    }

    public TreeTentType getType()
    {
        return type;
    }

    public void setType(TreeTentType type)
    {
        this.type = type;
    }

    public TreeTentClue copy()
    {
        return null;
    }
}
