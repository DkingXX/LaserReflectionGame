package objects;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import game.GridCell;
import org.mockito.Mockito;

/**
 * Helper class mocking a TileFactory.
 */
public class TileTestHelper {

    private transient TileFactory tileFactory;
    private transient TiledMapTileSets tiledMapTileSetsMock;

    /**
     * Initialize the mocks required for the factory.
     */
    public TileTestHelper() {
        tiledMapTileSetsMock = Mockito.mock(TiledMapTileSets.class);

        for (Material mat : Material.values()) {
            TiledMapTile tile = Mockito.mock(TiledMapTile.class);
            Mockito.when(tile.getId()).thenReturn(mat.getId());
            Mockito.when(tiledMapTileSetsMock.getTile(mat.getId())).thenReturn(tile);
        }

        tileFactory = new TileFactory(tiledMapTileSetsMock);
    }

    /**
     * Helper method that will generate mocks to test a grid cell containing a certain tile.
     * @return grid cell containing the tile.
     */
    public GridCell setupCell(Material tileMaterial) {
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(tiledMapTileSetsMock.getTile(tileMaterial.getId()));
        return new GridCell(cell, tileFactory);
    }
}
