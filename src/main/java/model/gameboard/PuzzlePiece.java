package model.gameboard;

import java.util.ArrayList;

public class PuzzlePiece
{
    private ArrayList<ElementData> pieces;

    public PuzzlePiece()
    {
        pieces = new ArrayList<>();
    }

    public ArrayList<ElementData> getPieces()
    {
        return pieces;
    }

    public void setPieces(ArrayList<ElementData> pieces)
    {
        this.pieces = pieces;
    }

    public void addElementData(ElementData data)
    {
        pieces.add(data);
    }

    public void removeElementData(ElementData data)
    {
        pieces.remove(data);
    }
}
