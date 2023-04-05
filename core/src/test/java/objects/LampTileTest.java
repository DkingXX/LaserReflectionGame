package objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import game.GridCell;
import game.TiledMapStage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import util.Direction;

class LampTileTest {

    private transient TileTestHelper tileTestHelper = new TileTestHelper();
    private transient GridCell gridCell;
    private transient Tile tile;
    private transient TiledMapStage stageMock;

    @BeforeEach
    void init() {
        gridCell = tileTestHelper.setupCell(Material.LAMP);
        tile = gridCell.getTile();

        stageMock = Mockito.mock(TiledMapStage.class);
        tile.setStage(stageMock);
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void onLaserHit(Direction prevDirection) {
        // every incoming direction gets returned.
        assertEquals(prevDirection, tile.onLaserHit(prevDirection));
    }

    @Test
    void lampTurnsOn() {
        // calling twice but only turns on once.
        tile.onLaserHit(Direction.UP);
        tile.onLaserHit(Direction.UP);

        Mockito.verify(stageMock, Mockito.times(1)).decrementLampsLeft();
    }

    @Test
    void lampTurnsOff() {
        // turn on.
        tile.onLaserHit(Direction.UP);

        // calling twice but only turns off once.
        ((LampTile) tile).turnOff();
        ((LampTile) tile).turnOff();

        Mockito.verify(stageMock, Mockito.times(1)).incrementLampsLeft();
    }
}