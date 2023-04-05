package game;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import objects.Laser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import screens.PlayScreen;

/**
 * Tests for the level class.
 * mainly integration testing.
 */
public class LevelTest {

    private final transient String fileName = "testlevel.tmx";
    private final transient int levelID = 1;

    // mocks
    private transient PlayScreen playScreenMock;
    private transient TmxMapLoader mapLoaderMock;
    private transient TiledMap tiledMapMock;

    /**
     * Sets up all the mocks.
     */
    @BeforeEach
    public void setupMocks() {
        playScreenMock = Mockito.mock(PlayScreen.class);
        mapLoaderMock = Mockito.mock(TmxMapLoader.class);
        tiledMapMock = Mockito.mock(TiledMap.class);
        TiledMapTileLayer.Cell cellMock = Mockito.mock(TiledMapTileLayer.Cell.class);
        TiledMapTileLayer layerMock = Mockito.mock(TiledMapTileLayer.class);
        MapLayers mapLayersMock = Mockito.mock(MapLayers.class);
        TiledMapTileSets tileSetsMock = Mockito.mock(TiledMapTileSets.class);

        Mockito.when((mapLayersMock.get("level"))).thenReturn(layerMock);
        Mockito.when(layerMock.getCell(2, 2)).thenReturn(cellMock);
        Mockito.when(mapLoaderMock.load(fileName)).thenReturn(tiledMapMock);
        Mockito.when(tiledMapMock.getTileSets()).thenReturn(tileSetsMock);
        Mockito.when(tiledMapMock.getLayers()).thenReturn(mapLayersMock);
        MapProperties props = new MapProperties();
        props.put("tilewidth", 10);
        Mockito.when(tiledMapMock.getProperties()).thenReturn(props);
    }

    /**
     * Tests the constructor.
     */
    @Test
    public void constructorTest() {
        Level level = new Level(fileName, levelID, playScreenMock, mapLoaderMock);
        Assertions.assertEquals(levelID, level.getID());
        Assertions.assertEquals(tiledMapMock, level.getTiledMap());
        Assertions.assertNotNull(level.getLaser());
    }


    /**
     * Test the update with type WIN.
     */
    @Test
    public void updateWinTest() {
        Level level = Mockito.spy(new Level(fileName, levelID, playScreenMock, mapLoaderMock));

        // disable this method to avoid calling the database
        Mockito.doNothing().when(level).finishLevel();

        level.update(TiledMapStage.StageUpdateType.WIN);

        Mockito.verify(level, Mockito.times(1)).finishLevel();
    }


    /**
     * Test the update with type LOSE.
     */
    @Test
    public void updateLoseTest() {
        Level level = new Level(fileName, levelID, playScreenMock, mapLoaderMock);

        level.update(TiledMapStage.StageUpdateType.LOSE);

        Mockito.verify(playScreenMock, Mockito.times(1)).resetLevel();
    }


    /**
     * Tests the update with type LASERPATH.
     */
    @Test
    public void updateLaserTest() {
        Level level = Mockito.spy(new Level(fileName, levelID, playScreenMock, mapLoaderMock));

        level.update(TiledMapStage.StageUpdateType.LASERPATH);

        Mockito.verify(level, Mockito.never()).finishLevel();
    }

    /**
     * Test the startObserving method.
     */
    @Test
    public void startObservingTest() {
        Level level = new Level(fileName, levelID, playScreenMock, mapLoaderMock);
        TiledMapStage stageMock = Mockito.mock(TiledMapStage.class);

        level.startObserving(stageMock);
        Mockito.verify(stageMock, Mockito.times(1)).register(level);
        Mockito.verify(stageMock, Mockito.times(1)).register(Mockito.any(Laser.class));
    }
}
