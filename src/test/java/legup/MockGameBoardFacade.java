package legup;

import app.Config;
import app.GameBoardFacade;
import app.InvalidConfigException;
import model.Puzzle;
import org.junit.BeforeClass;

import javax.annotation.Resource;
import java.util.logging.Level;

public class MockGameBoardFacade extends GameBoardFacade
{
    protected MockGameBoardFacade()
    {
        super();
        Config config = null;
        try
        {
            config = new Config();
        }
        catch(InvalidConfigException e)
        {
            System.exit(1);
        }
        setConfig(config);
    }

    /**
     * Gets the singleton instance of GameBoardFacade
     *
     * @return single instance of GameBoardFacade
     */
    public synchronized static GameBoardFacade getInstance()
    {
        if(instance == null)
        {
            instance = new MockGameBoardFacade();
        }
        return instance;
    }

    @Override
    public void initializeUI()
    {

    }

    @Override
    public void setPuzzle(Puzzle puzzle)
    {
        this.puzzle = puzzle;
    }

    @Override
    public void setWindowTitle(String puzzleName, String fileName)
    {

    }
}
