package objects;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import util.Direction;

/**
 * General tile object for non interactive tiles that block the laser.
 * Currently used for the laser base and bricks.
 */
public abstract class OpaqueStaticTile extends Tile {

    public OpaqueStaticTile(TiledMapTile copy) {
        super(copy);
    }

    /**
     * Method called when laser hits the tile.
     * @param prevDirection previous direction of the laser hitting the tile.
     * @return new direction of the laser. Direction.NONE if laser gets blocked.
     */
    @Override
    public Direction onLaserHit(Direction prevDirection) {
        return Direction.NONE;
    }
}
