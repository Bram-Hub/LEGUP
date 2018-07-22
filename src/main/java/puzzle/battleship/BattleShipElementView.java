package puzzle.battleship;

import ui.boardview.GridElementView;

import java.awt.*;
import java.util.Random;

public class BattleShipElementView extends GridElementView {
    private static final Color WATER_COLOR = Color.BLUE; // CAN CHANGE THIS TO ANY BLUE YOU WANT
    private static final Color SHIP_COLOR = Color.DARK_GRAY;

    public BattleShipElementView(BattleShipCell cell) {super(cell); }

    @Override public void drawElement(Graphics2D graphics2D) {
        BattleShipCell cell = (BattleShipCell) element;
        BattleShipCellType type = cell.getType();

        // graphics2D.setColor(WATER_COLOR);
        // graphics2D.fillRect(location.x, location.y, size.width, size.height);
        colorBackgroundWater(graphics2D);
        if( type == BattleShipCellType.SHIP_SEGMENT) {
            graphics2D.setColor(SHIP_COLOR);
            graphics2D.fillRect(location.x+size.width/4, location.y+size.height/4, size.width/2, size.height/2);
        }

        graphics2D.setColor(Color.BLACK);
        graphics2D.setStroke(new BasicStroke(1));
        graphics2D.drawRect(location.x, location.y, size.width, size.height);
    }

    private void colorBackgroundWater(Graphics2D graphics2D) {
        Color oceanColors[] = new Color[5];
        oceanColors[0] = new Color(47, 86, 233);
        oceanColors[1] = new Color(45, 100, 245);
        oceanColors[2] = new Color(47, 141, 255);
        oceanColors[3] = new Color(51, 171, 249);
        oceanColors[4] = new Color(52, 204, 255);

        graphics2D.setColor(oceanColors[0]);
        graphics2D.fillRect(location.x, location.y, size.width, size.height);

        Random generator = new Random();
        for (int i = location.x; i < location.x+size.width; i = i+4) {
            for (int j = location.y; j < location.y+size.height; j = j+2) {
                int c = generator.nextInt(oceanColors.length);
                graphics2D.setColor(oceanColors[c]);
                if (i+4 < location.x+size.width && j+2 < location.y+size.height) {
                    graphics2D.fillRect(i, j, 4,2);
                } else if (i+4 < location.x+size.width) {
                    graphics2D.fillRect(i, j, 4,(location.y+size.height)%2);
                } else if (j+2 < location.y+size.height) {
                    graphics2D.fillRect(i, j, (location.x+size.width)%4, 2);
                } else {
                    graphics2D.fillRect(i, j, (location.x+size.width)%4, (location.y+size.height)%2);
                }
            }
        }

        /*
        Random generator = new Random();
        for (int i = location.x; i < location.x+size.width; i = i+8) {
            for (int j = location.y; j < location.y+size.height; j = j+2) {
                int c = generator.nextInt(oceanColors.length);
                graphics2D.setColor(oceanColors[c]);
                if (i+8 < location.x+size.width && j+2 < location.y+size.height) {
                    graphics2D.fillRect(i, j, 8,2);
                } else if (i+8 < location.x+size.width) {
                    graphics2D.fillRect(i, j, 8,(location.y+size.height)%2);
                } else if (j+2 < location.y+size.height) {
                    graphics2D.fillRect(i, j, (location.x+size.width)%8, 2);
                } else {
                    graphics2D.fillRect(i, j, (location.x+size.width)%8, (location.y+size.height)%2);
                }
            }
        }
        */
    }
}