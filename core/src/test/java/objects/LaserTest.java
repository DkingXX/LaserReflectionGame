package objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import game.GridCell;
import game.TiledMapStage;
import java.awt.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mockito;
import util.Direction;

class LaserTest {

    private transient Laser laser;
    private transient TiledMapTileLayer layerMock;

    @BeforeEach
    void setUp() {
        MapLayers layers = new MapLayers();
        layerMock = Mockito.mock(TiledMapTileLayer.class);
        Mockito.when(layerMock.getName()).thenReturn("level");
        layers.add(layerMock);

        TiledMap levelMock = Mockito.mock(TiledMap.class);
        Mockito.when(levelMock.getLayers()).thenReturn(layers);

        MapProperties props = new MapProperties();
        props.put("tilewidth", 2);
        Mockito.when(levelMock.getProperties()).thenReturn(props);

        laser = Mockito.spy(new Laser(levelMock, 0, 0, Direction.RIGHT));
    }

    @ParameterizedTest
    @EnumSource(TiledMapStage.StageUpdateType.class)
    void updateTypes(TiledMapStage.StageUpdateType type) {
        laser.update(type);

        if (type == TiledMapStage.StageUpdateType.LASERPATH) {
            Mockito.verify(laser).update();
        } else {
            Mockito.verify(laser, Mockito.never()).update();
        }
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void outOfBounds(Direction laserDirection) {
        laser.startDir = laserDirection;
        Mockito.when(layerMock.getWidth()).thenReturn(1);
        Mockito.when(layerMock.getHeight()).thenReturn(1);

        laser.update();

        Mockito.verify(laser, Mockito.atMost(2))
            .addLaserPoint(Mockito.any(Point.class));
        Mockito.verify(layerMock, Mockito.never()).getCell(Mockito.anyInt(), Mockito.anyInt());

        if (laserDirection == Direction.NONE) {
            Mockito.verify(laser, Mockito.never()).addLaserPoint(Mockito.any(Point.class));
        }
    }

    /**
     * Sets up mocks to mimick certain behaviour on laser incoming on left side.
     */
    void setupTileBehaviour(Direction nextDir) {
        Mockito.when(layerMock.getWidth()).thenReturn(2);
        Mockito.when(layerMock.getHeight()).thenReturn(1);

        Tile tile = Mockito.mock(Tile.class);
        Mockito.when(tile.onLaserHit(Direction.RIGHT)).thenReturn(nextDir);
        GridCell cell = Mockito.mock(GridCell.class);

        Mockito.when(cell.getTile()).thenReturn(tile);
        Mockito.when(layerMock.getCell(1, 0)).thenReturn(cell);
    }

    @Test
    void opaqueTile() {
        setupTileBehaviour(Direction.NONE);

        laser.update();

        assertEquals(1, laser.laserPath.get(1).x);
        assertEquals(0.5f, laser.laserPath.get(1).y);
    }

    @Test
    void transparentTile() {
        setupTileBehaviour(Direction.RIGHT);

        laser.update();

        // blocked at bound of map
        assertEquals(2.5f, laser.laserPath.get(1).x);
        assertEquals(0.5f, laser.laserPath.get(1).y);
    }

    @Test
    void reflectTile() {
        setupTileBehaviour(Direction.UP);

        laser.update();

        // blocked at bound of map
        assertEquals(1.5f, laser.laserPath.get(1).x);
        assertEquals(0.5f, laser.laserPath.get(1).y);
    }

    @Test
    void hitLamp() {
        Mockito.when(layerMock.getWidth()).thenReturn(2);
        Mockito.when(layerMock.getHeight()).thenReturn(1);

        assertTrue(laser.lampsHit.isEmpty());

        LampTile tile = new LampTile(Mockito.mock(TiledMapTile.class));
        tile.setStage(Mockito.mock(TiledMapStage.class));

        GridCell cell = Mockito.mock(GridCell.class);
        Mockito.when(cell.getTile()).thenReturn(tile);
        Mockito.when(layerMock.getCell(1, 0)).thenReturn(cell);

        laser.update();

        assertEquals(1, laser.lampsHit.size());
        assertTrue(laser.lampsHit.contains(tile));

        Mockito.verify(laser, Mockito.atMost(2))
            .addLaserPoint(Mockito.any(Point.class));
    }

    @Test
    void turnOffLamps() {
        LampTile tile = Mockito.mock(LampTile.class);
        tile.setStage(Mockito.mock(TiledMapStage.class));

        laser.lampsHit.add(tile);

        laser.update();

        Mockito.verify(tile).turnOff();
        assertTrue(laser.lampsHit.isEmpty());
    }

    @Test
    void drawEmptyLaser() {
        OrthographicCamera cameraMock = Mockito.mock(OrthographicCamera.class);
        ShapeRenderer srMock = Mockito.mock(ShapeRenderer.class);
        laser.drawLaser(cameraMock, srMock);

        Mockito.verifyNoInteractions(cameraMock);
    }

    @Test
    void drawLaser() {
        laser.laserPath.add(new Point.Float(0f, 0f));
        laser.laserPath.add(new Point.Float(0f, 1f));
        laser.laserPath.add(new Point.Float(1f, 1f));

        OrthographicCamera cameraMock = Mockito.mock(OrthographicCamera.class);
        ShapeRenderer srMock = Mockito.mock(ShapeRenderer.class);
        laser.drawLaser(cameraMock, srMock);

        Mockito.verify(srMock).line(0f, 0f, 0f, 2f);
        Mockito.verify(srMock).line(0f, 2f, 2f, 2f);
    }
}