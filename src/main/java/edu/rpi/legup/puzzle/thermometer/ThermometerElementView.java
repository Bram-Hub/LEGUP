package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.ui.boardview.GridElementView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;

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
        int rotation = cell.getRotation();
        if (type == ThermometerType.HEAD) {
            if(fill == ThermometerFill.FILLED){
                graphics2D.drawImage(
                        ThermometerView.FILLEDHEAD,
                        location.x,
                        location.y,
                        size.width,
                        size.height,
                        null,
                        null);
                System.out.println(cell.getLocation().toString());
                graphics2D.rotate(Math.toRadians(rotation), location.x, location.y);
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawRect(location.x, location.y, size.width, size.height);
            }
            else if (fill == ThermometerFill.EMPTY) {
                graphics2D.drawImage(
                        ThermometerView.EMPTYHEAD,
                        location.x,
                        location.y,
                        size.width,
                        size.height,
                        null,
                        null);
                System.out.println(cell.getLocation().toString());
                graphics2D.rotate(Math.toRadians(rotation), location.x, location.y);
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawRect(location.x, location.y, size.width, size.height);
            }
            else if (fill == ThermometerFill.BLOCKED){
                    graphics2D.drawImage(
                            ThermometerView.BLOCKEDHEAD,
                            location.x,
                            location.y,
                            size.width,
                            size.height,
                            null,
                            null);
                    System.out.println(cell.getLocation().toString());
                    graphics2D.rotate(Math.toRadians(rotation), location.x, location.y);
                    graphics2D.setColor(Color.BLACK);
                    graphics2D.drawRect(location.x, location.y, size.width, size.height);
            }
            else{
                System.out.println("missing fill type");
            }

        }
        else if (type == ThermometerType.SHAFT) {
            if(fill == ThermometerFill.FILLED){
                graphics2D.drawImage(
                        ThermometerView.FILLEDSHAFT,
                        location.x,
                        location.y,
                        size.width,
                        size.height,
                        null,
                        null);
                System.out.println(cell.getLocation().toString());
                graphics2D.rotate(Math.toRadians(rotation), location.x, location.y);
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawRect(location.x, location.y, size.width, size.height);
            }
            else if (fill == ThermometerFill.EMPTY) {
                graphics2D.drawImage(
                        ThermometerView.EMPTYSHAFT,
                        location.x,
                        location.y,
                        size.width,
                        size.height,
                        null,
                        null);
                System.out.println(cell.getLocation().toString());
                graphics2D.rotate(Math.toRadians(rotation), location.x, location.y);
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawRect(location.x, location.y, size.width, size.height);
            }
            else if (fill == ThermometerFill.BLOCKED){
                    graphics2D.drawImage(
                            ThermometerView.BLOCKEDSHAFT,
                            location.x,
                            location.y,
                            size.width,
                            size.height,
                            null,
                            null);
                    System.out.println(cell.getLocation().toString());
                    graphics2D.rotate(Math.toRadians(rotation), location.x, location.y);
                    graphics2D.setColor(Color.BLACK);
                    graphics2D.drawRect(location.x, location.y, size.width, size.height);
            }
            else{
                System.out.println("missing fill type");
            }
        }
        else if (type == ThermometerType.TIP) {
            if(fill == ThermometerFill.FILLED){
                graphics2D.drawImage(
                        ThermometerView.FILLEDTIP,
                        location.x,
                        location.y,
                        size.width,
                        size.height,
                        null,
                        null);
                System.out.println(cell.getLocation().toString());
                graphics2D.rotate(Math.toRadians(rotation), location.x, location.y);
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawRect(location.x, location.y, size.width, size.height);
            }
            else if (fill == ThermometerFill.EMPTY) {
                graphics2D.drawImage(
                        ThermometerView.EMPTYTIP,
                        location.x,
                        location.y,
                        size.width,
                        size.height,
                        null,
                        null);
                System.out.println(cell.getLocation().toString());
                graphics2D.rotate(Math.toRadians(rotation), location.x, location.y);
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawRect(location.x, location.y, size.width, size.height);
            }
            else if (fill == ThermometerFill.BLOCKED){
                    graphics2D.drawImage(
                            ThermometerView.BLOCKEDTIP,
                            location.x,
                            location.y,
                            size.width,
                            size.height,
                            null,
                            null);
                    System.out.println(cell.getLocation().toString());
                    graphics2D.rotate(Math.toRadians(rotation), location.x, location.y);
                    graphics2D.setColor(Color.BLACK);
                    graphics2D.drawRect(location.x, location.y, size.width, size.height);
            }
            else{
                System.out.println("missing fill type");
            }
        }
        else{
            System.out.println("missing type type");
        }
    }
}
