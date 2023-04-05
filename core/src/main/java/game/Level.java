package game;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import database.UserDao;
import objects.Laser;
import objects.Material;
import screens.PlayScreen;
import util.Direction;

/**
 * The level class.
 */
public class Level implements StageObserver {

    private transient Laser laser;
    private transient TiledMap tiledMap;
    private transient PlayScreen playScreen;

    private transient int levelID;

    /**
     * Initializes a Level instance.
     *
     * @param fileName  the filename of the tmxMap
     * @param levelID   the id of the current level
     * @param mapLoader TMXMapLoader object
     */
    public Level(String fileName, int levelID, PlayScreen playScreen,
                 TmxMapLoader mapLoader) {
        this.levelID = levelID;
        this.playScreen = playScreen;

        tiledMap = mapLoader.load(fileName);

        TiledMapTileLayer.Cell laserCell = ((TiledMapTileLayer) tiledMap.getLayers().get("level"))
            .getCell(2, 2);
        laserCell.setTile(tiledMap.getTileSets().getTile(
            Material.LASER.getId())); // laser placeholder
        laser = new Laser(tiledMap, 2, 2, Direction.RIGHT);
    }

    /**
     * Initializes a Level instance.
     *
     * @param fileName the filename of the tmxMap
     * @param levelID  the id of the current level
     * @param player   the currently logged in player
     */
    public Level(String fileName, int levelID, PlayScreen playScreen, Player player) {
        this(fileName, levelID, playScreen, new TmxMapLoader());
    }

    /**
     * Finishes the level.
     */
    public void finishLevel() {
        playScreen.nextLevel();
    }

    /**
     * Gets the tiled map.
     *
     * @return the tiled map
     */
    public TiledMap getTiledMap() {
        return tiledMap;
    }

    /**
     * Gets the laser.
     *
     * @return the laser
     */
    public Laser getLaser() {
        return laser;
    }

    /**
     * Gets the level id.
     *
     * @return the id
     */
    public int getID() {
        return levelID;
    }

    /**
     * Register this level and its laser to the stage.
     * @param stage the stage to register to.
     */
    public void startObserving(TiledMapStage stage) {
        stage.register(laser);
        stage.register(this);
    }

    @Override
    public void update(TiledMapStage.StageUpdateType event) {
        if (event == TiledMapStage.StageUpdateType.WIN) {
            finishLevel();
        } else if (event == TiledMapStage.StageUpdateType.LOSE) {
            playScreen.resetLevel();
        }
    }
}
