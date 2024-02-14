package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.ui.boardview.GridElementView;

import java.awt.*;

public class ThermometerElementView extends GridElementView {

    private static final Font FONT = new Font("TimesRoman", Font.BOLD, 16);
    private static final Color FONT_COLOR = Color.BLACK;

    public ThermometerElementView(ThermometerCell cell) {
        super(cell);
    }

    @Override
    public ThermometerCell getPuzzleElement() {
        return (ThermometerCell) super.getPuzzleElement();
    }

    @Override
    public void drawElement(Graphics2D graphics2D) {
        ThermometerCell cell = (ThermometerCell) puzzleElement;
        ThermometerType type = cell.getType();
        ThermometerFill fill = cell.getFill();
        if (type == ThermometerType.HEAD) {
            if(fill == ThermometerFill.FILLED){
                graphics2D.setStroke(new BasicStroke(1));
                graphics2D.setColor(Color.BLACK);
                graphics2D.fillRect(location.x, location.y, size.width, size.height);
            }
            else if (fill == ThermometerFill.EMPTY) {
                graphics2D.setStroke(new BasicStroke(1));
                graphics2D.setColor(Color.WHITE);
                graphics2D.fillRect(location.x, location.y, size.width, size.height);
            }
            else if (fill == ThermometerFill.BLOCKED){
                graphics2D.setStroke(new BasicStroke(1));
                graphics2D.setColor(Color.BLUE);
                graphics2D.fillRect(location.x, location.y, size.width, size.height);
            }
            else{
                System.out.println("ZEV IS BAD AT DEV");
            }

        }
        else if (type == ThermometerType.SHAFT) {
            if(fill == ThermometerFill.FILLED){
                graphics2D.setStroke(new BasicStroke(1));
                graphics2D.setColor(Color.BLACK);
                graphics2D.fillRect(location.x, location.y, size.width, size.height);
            }
            else if (fill == ThermometerFill.EMPTY) {
                graphics2D.setStroke(new BasicStroke(1));
                graphics2D.setColor(Color.WHITE);
                graphics2D.fillRect(location.x, location.y, size.width, size.height);
            }
            else if (fill == ThermometerFill.BLOCKED){
                graphics2D.setStroke(new BasicStroke(1));
                graphics2D.setColor(Color.BLUE);
                graphics2D.fillRect(location.x, location.y, size.width, size.height);
            }
            else{
                System.out.println("ZEV IS BAD AT DEV");
            }
        }
        else if (type == ThermometerType.TIP) {
            if(fill == ThermometerFill.FILLED){
                graphics2D.setStroke(new BasicStroke(1));
                graphics2D.setColor(Color.BLACK);
                graphics2D.fillRect(location.x, location.y, size.width, size.height);
            }
            else if (fill == ThermometerFill.EMPTY) {
                graphics2D.setStroke(new BasicStroke(1));
                graphics2D.setColor(Color.WHITE);
                graphics2D.fillRect(location.x, location.y, size.width, size.height);
            }
            else if (fill == ThermometerFill.BLOCKED){
                graphics2D.setStroke(new BasicStroke(1));
                graphics2D.setColor(Color.BLUE);
                graphics2D.fillRect(location.x, location.y, size.width, size.height);
            }
            else{
                System.out.println("ZEV IS BAD AT DEV");
            }
        }
        else{
            System.out.println("ZEV IS DOUBLE BAD AT DEV");
        }
    }
}
