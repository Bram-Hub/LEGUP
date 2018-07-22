package ui.boardview;

import model.gameboard.Element;

import javax.swing.*;

public class SelectionItemView extends JMenuItem
{
    private Element data;

    public SelectionItemView(Element data, Icon icon)
    {
        super(icon);
        this.data = data;
    }

    public SelectionItemView(Element data, String display)
    {
        super(display);
        this.data = data;
    }

    public SelectionItemView(Element data, int display)
    {
        super(String.valueOf(display));
        this.data = data;
    }

    public SelectionItemView(Element data)
    {
        this(data, data.getValueInt());
    }

    public Element getData()
    {
        return data;
    }
}
