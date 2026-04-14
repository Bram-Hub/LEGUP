package edu.rpi.legup.save;

import java.io.FileInputStream;
import java.io.InputStream;

public class SavableBoard {
    private static final String LEGUP_HEADER = "edu.rpi.legup.Legup";

    private String filePath;
    private InputStream inputStream;

    public SavableBoard(String filePath) throws Exception {
        this.filePath = filePath;
        this.inputStream = new FileInputStream(filePath);
    }
}
