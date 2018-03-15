package save;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.InputStream;

public class SavableBoard
{
    private static final String LEGUP_HEADER = "Legup";

    private String filePath;
    private InputStream inputStream;

    public SavableBoard(String filePath) throws Exception
    {
        this.filePath = filePath;
        this.inputStream = new FileInputStream(filePath);


    }
}
