package game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.viewport.Viewport;
import objects.Material;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import util.HeadLessTestExtension;

@ExtendWith(HeadLessTestExtension.class)
class TiledMapStageTest {

    private transient TiledMapStage stage;

    @BeforeEach
    void setUp() {
        // constructs a 2x1 map with one brick tile and one lamp
        TiledMapTile tileMock = Mockito.mock(TiledMapTile.class);
        Mockito.when(tileMock.getId()).thenReturn(Material.BRICK.getId());
        TiledMapTileLayer.Cell testCell = new TiledMapTileLayer.Cell();
        testCell.setTile(tileMock);

        TiledMapTile tileMock2 = Mockito.mock(TiledMapTile.class);
        Mockito.when(tileMock2.getId()).thenReturn(Material.LAMP.getId());
        TiledMapTileLayer.Cell testCell2 = new TiledMapTileLayer.Cell();
        testCell2.setTile(tileMock2);

        TiledMapTileLayer layer = new TiledMapTileLayer(2, 1, 1, 1);
        layer.setName("level");
        layer.setCell(0, 0, testCell);
        layer.setCell(1, 0, testCell2);

        MapLayers mapLayers = new MapLayers();
        mapLayers.add(layer);

        MapProperties mapProperties = new MapProperties();
        mapProperties.put("maxMirrors", 3);

        TiledMap mapMock = Mockito.mock(TiledMap.class);
        Mockito.when(mapMock.getLayers()).thenReturn(mapLayers);
        Mockito.when(mapMock.getProperties()).thenReturn(mapProperties);

        // call constructor
        stage = new TiledMapStage(mapMock, Mockito.mock(Viewport.class),
            Mockito.mock(SpriteBatch.class));
    }

    @Test
    void mirrorsLeft() {
        assertEquals(3, stage.getMirrorsLeft());

        stage.setMirrorsLeft(4);

        assertEquals(4, stage.getMirrorsLeft());
    }

    @Test
    void createActors() {
        // test if actor is created correctly
        assertEquals(2, stage.getActors().size);
        assertEquals(0, stage.getActors().get(0).getX());
        assertEquals(0, stage.getActors().get(0).getY());
        assertEquals(1, stage.getActors().get(0).getWidth());
        assertEquals(1, stage.getActors().get(0).getHeight());
        assertTrue(stage.getActors().get(0).getListeners().get(0) instanceof TiledMapInputListener);
    }

    @Test
    void observerUpdate() {
        StageObserver observer1 = Mockito.mock(StageObserver.class);
        StageObserver observer2 = Mockito.mock(StageObserver.class);

        stage.register(observer1);
        stage.register(observer2);

        stage.update(TiledMapStage.StageUpdateType.LASERPATH);

        Mockito.verify(observer1).update(TiledMapStage.StageUpdateType.LASERPATH);
        Mockito.verify(observer2).update(TiledMapStage.StageUpdateType.LASERPATH);
    }

    @Test
    void observerLampsLeft() {
        StageObserver observer = Mockito.mock(StageObserver.class);

        stage.register(observer);

        // map created in setUp already has one lamp
        stage.incrementLampsLeft();

        stage.decrementLampsLeft();
        stage.decrementLampsLeft();

        Mockito.verify(observer, Mockito.times(1)).update(TiledMapStage.StageUpdateType.WIN);
    }
}