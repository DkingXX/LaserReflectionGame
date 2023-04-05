package objects;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import util.Direction;

public class TileFactory {

    private transient TiledMapTileSets tiledMapTileSets;

    public TileFactory(TiledMapTileSets tiledMapTileSets) {
        this.tiledMapTileSets = tiledMapTileSets;
    }

    private Tile getTile(TiledMapTile tile, Material material) {
        switch (material) {
            case LAMP:
                return new LampTile(tile);
            case MIRROR:
                return new MirrorTile(tile);
            case AIR:
                return new AirTile(tile);
            case BOMB:
                return new BombTile(tile);
            default:
                return new OpaqueStaticTile(tile) {
                };
        }
    }

    /**
     * Converts a scene2d tiledMapTile to the custom implementation.
     *
     * @param tile the scene2d tiledMapTile to convert.
     * @return the converted tile.
     */
    public Tile getTile(TiledMapTile tile) {
        Material material = Material.getMaterial(tile.getId());
        return getTile(tile, material);
    }

    /**
     * Creates a custom tile given its material.
     *
     * @param material material of the tile to create.
     * @return the converted tile.
     */
    public Tile getTile(Material material) {
        return getTile(this.tiledMapTileSets.getTile(material.getId()), material);
    }
}
