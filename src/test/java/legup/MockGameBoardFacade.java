package legup;

import edu.rpi.legup.app.Config;
import edu.rpi.legup.app.GameBoardFacade;
import edu.rpi.legup.app.InvalidConfigException;
import edu.rpi.legup.model.Puzzle;

public class MockGameBoardFacade extends GameBoardFacade {
  protected MockGameBoardFacade() {
    super();
    Config config = null;
    try {
      config = new Config();
    } catch (InvalidConfigException e) {
      System.exit(1);
    }
    setConfig(config);
  }

  /**
   * Gets the singleton instance of GameBoardFacade
   *
   * @return single instance of GameBoardFacade
   */
  public static synchronized GameBoardFacade getInstance() {
    if (instance == null) {
      instance = new MockGameBoardFacade();
    }
    return instance;
  }

  @Override
  public void initializeUI() {}

  @Override
  public void setPuzzle(Puzzle puzzle) {
    this.puzzle = puzzle;
  }

  @Override
  public void setWindowTitle(String puzzleName, String fileName) {}
}
