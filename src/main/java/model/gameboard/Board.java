package model.gameboard;

import model.rules.CaseRule;
import java.util.ArrayList;
import java.util.List;

public abstract class Board
{
    protected List<ElementData> elementData;
    protected List<ElementData> modifiedData;
    protected boolean isModifiable;
    protected CaseRule caseRule;

    /**
     * Board Constructor - creates an empty board
     */
    public Board()
    {
        this.elementData = new ArrayList<>();
        this.modifiedData = new ArrayList<>();
        this.isModifiable = true;
        this.caseRule = null;
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
     * Gets a specific Element on the board
     *
     * @param index index of the element
     * @return ElementData at the specified index
     */
    public ElementData getElementData(int index)
    {
        return index < elementData.size() ? elementData.get(index) : null;
    }

    /**
     * Sets a specific Element on the board
     *
     * @param index index of the element
     * @param data new element at the index
     */
    public void setElementData(int index, ElementData data)
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
    public List<ElementData> getElementData()
    {
        return elementData;
    }

    /**
     * Sets the elements on the board
     *
     * @param elementData elements on the board
     */
    public void setElementData(ArrayList<ElementData> elementData)
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
    public List<ElementData> getModifiedData()
    {
        return modifiedData;
    }

    /**
     * Adds a element that has been modified to the list
     *
     * @param data element that has been modified
     */
    public void addModifiedData(ElementData data)
    {
        if(!modifiedData.contains(data))
        {
            modifiedData.add(data);
            data.setModified(true);
        }
    }

    /**
     * Removes a element that is no longer modified
     *
     * @param data element that is no longer modified
     */
    public void removeModifiedData(ElementData data)
    {
        modifiedData.remove(data);
        data.setModified(false);
    }

    public void notifyChange(ElementData data)
    {
        elementData.get(data.getIndex()).setValueInt(data.getValueInt());
    }

    public Board mergedBoard(Board lca, ArrayList<Board> boards)
    {
        if(lca == null || boards.isEmpty())
        {
            return null;
        }

        Board mergedBoard = lca.copy();

        Board firstBoard = boards.get(0);
        for(ElementData lcaData : lca.getElementData())
        {
            ElementData mData = firstBoard.getElementData(lcaData.getIndex());

            boolean isSame = true;
            for(Board board : boards)
            {
                isSame &= mData.equals(board.getElementData(lcaData.getIndex()));
            }

            if(isSame && !lcaData.equals(mData))
            {
                ElementData mergedData = mergedBoard.getElementData(lcaData.getIndex());
                mergedData.setValueInt(mData.getValueInt());
                mergedBoard.addModifiedData(mergedData);
            }
        }

        return mergedBoard;
    }

    /**
     *
     *
     * @return
     */
    public CaseRule getCaseRule()
    {
        return caseRule;
    }

    /**
     *
     *
     * @param caseRule
     */
    public void setCaseRule(CaseRule caseRule)
    {
        this.caseRule = caseRule;
    }

    /**
     * Performs a deep copy of the Board
     *
     * @return a new copy of the board that is independent of this one
     */
    public abstract Board copy();
}
