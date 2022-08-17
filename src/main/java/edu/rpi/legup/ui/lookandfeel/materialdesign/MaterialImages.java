package edu.rpi.legup.ui.lookandfeel.materialdesign;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class MaterialImages {

    public static final BufferedImage RIGHT_ARROW = loadImg("/edu/rpi/legup/imgs/right_arrow.png");
    public static final BufferedImage DOWN_ARROW = loadImg("/edu/rpi/legup/imgs/down_arrow.png");
    public static final BufferedImage UP_ARROW = loadImg("/edu/rpi/legup/imgs/up_arrow.png");
    public static final BufferedImage PAINTED_CHECKED_BOX = loadImg("/edu/rpi/legup/imgs/painted_checked_box.png");
    public static final BufferedImage OUTLINED_CHECKED_BOX = loadImg("/edu/rpi/legup/imgs/outlined_checked_box.png");
    public static final BufferedImage UNCHECKED_BOX = loadImg("/edu/rpi/legup/imgs/unchecked_box.png");
    public static final BufferedImage RADIO_BUTTON_ON = loadImg("/edu/rpi/legup/imgs/radio_button_on.png");
    public static final BufferedImage RADIO_BUTTON_OFF = loadImg("/edu/rpi/legup/imgs/radio_button_off.png");
    public static final BufferedImage TOGGLE_BUTTON_ON = loadImg("/edu/rpi/legup/imgs/toggle_on.png");
    public static final BufferedImage TOGGLE_BUTTON_OFF = loadImg("/edu/rpi/legup/imgs/toggle_off.png");
    public static final BufferedImage BACK_ARROW = loadImg("/edu/rpi/legup/imgs/back_arrow.png");
    public static final BufferedImage COMPUTER = loadImg("/edu/rpi/legup/imgs/computer.png");
    public static final BufferedImage FILE = loadImg("/edu/rpi/legup/imgs/file.png");
    public static final BufferedImage FLOPPY_DRIVE = loadImg("/edu/rpi/legup/imgs/floppy_drive.png");
    public static final BufferedImage FOLDER = loadImg("/edu/rpi/legup/imgs/folder.png");
    public static final BufferedImage HARD_DRIVE = loadImg("/edu/rpi/legup/imgs/hard_drive.png");
    public static final BufferedImage HOME = loadImg("/edu/rpi/legup/imgs/home.png");
    public static final BufferedImage LIST = loadImg("/edu/rpi/legup/imgs/list.png");
    public static final BufferedImage NEW_FOLDER = loadImg("/edu/rpi/legup/imgs/new_folder.png");
    public static final BufferedImage DETAILS = loadImg("/edu/rpi/legup/imgs/details.png");

    private MaterialImages() {
    }

    private static BufferedImage loadImg(String imgPath) {
        try (InputStream inputStream = MaterialImages.class.getResourceAsStream(imgPath)) {
            return ImageIO.read(inputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Image " + imgPath + " wasn't loaded");
        }
    }
}
