package objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

import game.GridCell;
import org.junit.jupiter.api.Test;

class TileFactoryTest {

    private transient TileTestHelper tileTestHelper = new TileTestHelper();

    @Test
    void lampTile() {
        GridCell gridCell = tileTestHelper.setupCell(Material.LAMP);
        assertTrue(gridCell.getTile() instanceof LampTile);
    }

    @Test
    void mirrorTile() {
        GridCell gridCell = tileTestHelper.setupCell(Material.MIRROR);
        assertTrue(gridCell.getTile() instanceof MirrorTile);
    }

    @Test
    void airTile() {
        GridCell gridCell = tileTestHelper.setupCell(Material.AIR);
        assertTrue(gridCell.getTile() instanceof AirTile);
    }

    @Test
    void bombTile() {
        GridCell gridCell = tileTestHelper.setupCell(Material.BOMB);
        assertTrue(gridCell.getTile() instanceof BombTile);
    }

    @Test
    void opaqueStaticTile() {
        GridCell gridCellBrick = tileTestHelper.setupCell(Material.BRICK);
        GridCell gridCellLaser = tileTestHelper.setupCell(Material.LASER);

        assertTrue(gridCellBrick.getTile() instanceof OpaqueStaticTile);
        assertTrue(gridCellLaser.getTile() instanceof OpaqueStaticTile);
    }
}