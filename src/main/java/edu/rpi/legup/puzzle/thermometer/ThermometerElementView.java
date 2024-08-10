package edu.rpi.legup.puzzle.thermometer;

import edu.rpi.legup.ui.boardview.GridElementView;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ThermometerElementView extends GridElementView {

    // mixture of stuff stolen from tree tent and dev guide
    private static final Font FONT = new Font("TimesRoman", Font.BOLD, 16);
    private static final Color FONT_COLOR = Color.BLACK;

    public ThermometerElementView(@NotNull ThermometerCell cell) {
        super(cell);
    }

    @Override
    public @NotNull ThermometerCell getPuzzleElement() {
        return (ThermometerCell) super.getPuzzleElement();
    }

    // method for drawing a thermometer cell
    // basically copy/pasted from tree tent drawing tent images
    @Override
    @Contract(pure = true)
    public void drawElement(Graphics2D graphics2D) {

        ThermometerCell cell = (ThermometerCell) puzzleElement;
        ThermometerType type = cell.getType();
        ThermometerFill fill = cell.getFill();
        int rotation = cell.getRotation();

        graphics2D.drawImage(
                imageSrc(type, fill, rotation),
                location.x,
                location.y,
                size.width,
                size.height,
                null,
                null);

        graphics2D.setColor(Color.BLACK);
        graphics2D.drawRect(location.x, location.y, size.width, size.height);
    }

    // modified code from tree trent to display images
    private Image imageSrc(ThermometerType t, ThermometerFill f, int r) {

        // will have a 36 switch case at end to determine which image gets opened
        int result = 0;

        // 100 = NORTH, 200 = WEST, 300 = SOUTH, 400 = EAST
        switch (r) {
            case 0 -> result += 100;
            case 90 -> result += 400;
            case 180 -> result += 300;
            case 270 -> result += 200;
            default -> {
                System.out.println("ThermometerElementView: Invalid Rotation");
                return null;
            }
        }

        // 10 = EMPTY, 20 = FILLED, 30 = BLOCKED
        switch (f) {
            case ThermometerFill.EMPTY -> result += 10;
            case ThermometerFill.FILLED -> result += 20;
            case ThermometerFill.BLOCKED -> result += 30;
            default -> {
                System.out.println("ThermometerElementView: Invalid Fill");
                return null;
            }
        }

        // 1 = HEAD, 2 = SHAFT, 3 = TIP
        switch (t) {
            case ThermometerType.HEAD -> result += 1;
            case ThermometerType.SHAFT -> result += 2;
            case ThermometerType.TIP -> result += 3;
            default -> {
                System.out.println("ThermometerElementView: Invalid Type");
                return null;
            }
        }

        try {
            switch (result) {
                case 111 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/HeadEmpN.png"));
                }

                case 112 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/ShaftEmpN.png"));
                }

                case 113 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/TipEmpN.png"));
                }

                case 121 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/HeadFillN.png"));
                }

                case 122 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/ShaftFillN.png"));
                }

                case 123 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/TipFillN.png"));
                }

                case 131 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/HeadBlockN.png"));
                }

                case 132 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/ShaftBlockN.png"));
                }

                case 133 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/TipBlockN.png"));
                }

                case 211 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/HeadEmpE.png"));
                }

                case 212 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/ShaftEmpE.png"));
                }

                case 213 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/TipEmpE.png"));
                }

                case 221 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/HeadFillE.png"));
                }

                case 222 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/ShaftFillE.png"));
                }

                case 223 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/TipFillE.png"));
                }

                case 231 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/HeadBlockE.png"));
                }

                case 232 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/ShaftBlockE.png"));
                }

                case 233 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/TipBlockE.png"));
                }

                case 311 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/HeadEmpS.png"));
                }

                case 312 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/ShaftEmpS.png"));
                }

                case 313 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/TipEmpS.png"));
                }

                case 321 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/HeadFillS.png"));
                }

                case 322 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/ShaftFillS.png"));
                }

                case 323 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/TipFillS.png"));
                }

                case 331 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/HeadBlockS.png"));
                }

                case 332 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/ShaftBlockS.png"));
                }

                case 333 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/TipBlockS.png"));
                }

                case 411 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/HeadEmpW.png"));
                }

                case 412 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/ShaftEmpW.png"));
                }

                case 413 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/TipEmpW.png"));
                }

                case 421 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/HeadFillW.png"));
                }

                case 422 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/ShaftFillW.png"));
                }

                case 423 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/TipFillW.png"));
                }

                case 431 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/HeadBlockW.png"));
                }

                case 432 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/ShaftBlockW.png"));
                }

                case 433 -> {
                    return ImageIO.read(
                            ClassLoader.getSystemResourceAsStream(
                                    "edu/rpi/legup/images/thermometer/Elements/TipBlockW.png"));
                }
            }
        } catch (IOException e) {
            System.out.println("ThermometerElementView: Unexpected Issue");
            return null;
        }

        return null;
    }
}
