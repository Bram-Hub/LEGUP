package ui.boardview;

import model.gameboard.ElementData;

import javax.swing.*;

public class SelectionItemView extends JMenuItem
{
    private ElementData data;

    public SelectionItemView(ElementData data, Icon icon)
    {
        super(icon);
        this.data = data;
    }

    public SelectionItemView(ElementData data, String display)
    {
        super(display);
        this.data = data;
    }

    public SelectionItemView(ElementData data, int display)
    {
        super(String.valueOf(display));
        this.data = data;
    }

    public SelectionItemView(ElementData data)
    {
        this(data, data.getValueInt());
    }

    public ElementData getData()
    {
        return data;
    }
}
