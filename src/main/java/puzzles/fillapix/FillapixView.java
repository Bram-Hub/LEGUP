package puzzles.fillapix;

import app.BoardController;
import ui.boardview.GridBoardView;

import java.awt.*;

public class FillapixView extends GridBoardView{
    // private FillapixCellController fillapixCellController;
    // CAN TAKE THESE AND PUT IT INTO A DIFFERENT FOLDER AND GRAB FROM THERE LATER
    private static final Color STROKE_COLOR = new Color(0,0,0);
    private static final Stroke MINOR_STOKE = new BasicStroke(1);
    private static final Stroke MAJOR_STOKE = new BasicStroke(3);

    public FillapixView(BoardController boardController, Dimension gridSize, Dimension elementSize) {
        super(boardController, gridSize, elementSize);
        addMouseListener(elementController);
        addMouseMotionListener(elementController);
        int minorSize = (int)Math.sqrt(gridSize.width);
        for (int i = 0; i < gridSize.height; i++) {
            for (int j = 0; j < gridSize.width; j++) {
                Point location = new Point(j * elementSize.width + (j / minorSize + 1),
                        i * elementSize.height + (i / minorSize + 1));

                // FillapixElement element = new FillapixElement(new FillapixCell(0, null));
                // element.setIndex(i*gridSize.width + j);
                // element.setSize(elementSize);
                // element.setLocation(location);

                // puzzleElements.add(element);
            }
        }
    }

    @Override
    public void draw(Graphics2D graphics2D)
    {
        graphics2D.setColor(STROKE_COLOR);
        graphics2D.setStroke(MINOR_STOKE);
        // PuzzleElement hover = null;
        int minorSize = (int)Math.sqrt(gridSize.width);
        for(int i = 0; i < gridSize.height; i++)
        {
            for(int k = 0; k < gridSize.width; k++)
            {
                // PuzzleElement element = puzzleElements.get(i * gridSize.height + k);
                // if(!element.isHover())
                //    element.draw(graphics2D);
                // else
                //    hover = element;
            }
        }

        graphics2D.setColor(STROKE_COLOR);
        graphics2D.setStroke(MAJOR_STOKE);
        graphics2D.drawRect(3,3,
                gridSize.width * elementSize.width,
                gridSize.height * elementSize.height);

//        graphics2D.setStroke(new BasicStroke(4));
//        graphics2D.drawRect(50, 50, 100,100);
//        graphics2D.setColor(Color.GREEN);
//        graphics2D.setStroke(new BasicStroke(0.5f));
//        graphics2D.drawRect(48, 48,4,4);

        int regions = (int)Math.sqrt(gridSize.width);
        for(int i = 0; i < regions; i++)
        {
            for(int k = 0; k < regions; k++)
            {
                graphics2D.drawRect(i * regions * elementSize.width + (i / minorSize + 1) * 3,
                        k * regions * elementSize.height + (k / minorSize + 1) * 3,
                        regions * elementSize.width, regions * elementSize.height);
            }
        }

        // if(hover != null)
        //    hover.draw(graphics2D);

        //System.out.println("Canvas: " + canvas.getBounds());
    }
}