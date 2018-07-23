package model.gameboard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Board
{
    protected List<Element> elementData;
    protected Set<Element> modifiedData;
    protected boolean isModifiable;

    /**
     * Board Constructor - creates an empty board
     */
    public Board()
    {
        this.elementData = new ArrayList<>();
        this.modifiedData = new HashSet<>();
        this.isModifiable = true;
    }

    /**
     * Board Constructor - creates a board with null element
     *
     * @param size
     */
    public Board(int size)
    {
        this();
        for(int i = 0; i < size; i++)
        {
            elementData.add(null);
        }
    }

    /**
     * Gets a specific Element on this board
     *
     * @param element equivalent element
     * @return equivalent element on this board
     */
    public Element getElementData(Element element)
    {
        int index = element.getIndex();
        return index < elementData.size() ? elementData.get(index) : null;
    }

    /**
     * Sets a specific Element on the board
     *
     * @param index index of the element
     * @param data new element at the index
     */
    public void setElementData(int index, Element data)
    {
        if(index < elementData.size())
        {
            elementData.set(index, data);
        }
    }

    /**
     * Gets the number of elements on the board
     *
     * @return number of elements on the board
     */
    public int getElementCount()
    {
        return elementData.size();
    }

    /**
     * Gets the elements on the board
     *
     * @return elements on the board
     */
    public List<Element> getElementData()
    {
        return elementData;
    }

    /**
     * Sets the elements on the board
     *
     * @param elementData elements on the board
     */
    public void setElementData(ArrayList<Element> elementData)
    {
        this.elementData = elementData;
    }

    /**
     * Gets the modifiable attribute for the board
     *
     * @return true if the board is modifiable, false otherwise
     */
    public boolean isModifiable()
    {
        return isModifiable;
    }

    /**
     * Sets the modifiable attribute for the board
     *
     * @param isModifiable true if the board is modifiable, false otherwise
     */
    public void setModifiable(boolean isModifiable)
    {
        this.isModifiable = isModifiable;
    }

    /**
     * Gets whether the element of this board has been modified by the user
     *
     * @return true if the board has been modified, false otherwise
     */
    public boolean isModified()
    {
        return !modifiedData.isEmpty();
    }

    /**
     * Gets the list of modified element of the board
     *
     * @return list of modified element of the board
     */
    public Set<Element> getModifiedData()
    {
        return modifiedData;
    }

    /**
     * Adds a element that has been modified to the list
     *
     * @param data element that has been modified
     */
    public void addModifiedData(Element data)
    {
        modifiedData.add(data);
        data.setModified(true);
    }

    /**
     * Removes a element that is no longer modified
     *
     * @param data element that is no longer modified
     */
    public void removeModifiedData(Element data)
    {
        modifiedData.remove(data);
        data.setModified(false);
    }

    public void notifyChange(Element data)
    {
        elementData.get(data.getIndex()).setData(data.getData());
    }

    public Board mergedBoard(Board lca, ArrayList<Board> boards)
    {
        if(lca == null || boards.isEmpty())
        {
            return null;
        }

        Board mergedBoard = lca.copy();

        Board firstBoard = boards.get(0);
        for(Element lcaData : lca.getElementData())
        {
            Element mData = firstBoard.getElementData(lcaData);

            boolean isSame = true;
            for(Board board : boards)
            {
                isSame &= mData.equalsData(board.getElementData(lcaData));
            }

            if(isSame && !lcaData.equalsData(mData))
            {
                Element mergedData = mergedBoard.getElementData(lcaData);
                mergedData.setData(mData.getData());
                mergedBoard.addModifiedData(mergedData);
            }
        }

        return mergedBoard;
    }

    /**
     * Performs a deep copy of the Board
     *
     * @return a new copy of the board that is independent of this one
     */
    public abstract Board copy();
}
