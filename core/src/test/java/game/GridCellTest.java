package game;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import objects.AirTile;
import objects.BombTile;
import objects.Material;
import objects.TileFactory;
import objects.TileTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GridCellTest {

    private transient TileTestHelper tileTestHelper = new TileTestHelper();
    private transient GridCell gridCell;

    @BeforeEach
    void setUp() {
        gridCell = tileTestHelper.setupCell(Material.AIR);
    }

    @Test
    void getTile() {
        assertTrue(gridCell.getTile() instanceof AirTile);
    }

    @Test
    void setCorrectTile() {
        gridCell.setTile(Material.BOMB);

        assertTrue(gridCell.getTile() instanceof BombTile);
    }

    @Test
    void setIncorrectTile() {
        TiledMapTile incorrectTile = Mockito.mock(TiledMapTile.class);
        gridCell.setTile(incorrectTile);

        // tile did not change
        assertTrue(gridCell.getTile() instanceof AirTile);
    }
}