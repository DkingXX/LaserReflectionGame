package objects;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import util.Direction;

/**
 * Defines general behavior of a transparent tile which the laser can pass through.
 */
public abstract class TransparentTile extends Tile {
    public TransparentTile(TiledMapTile copy) {
        super(copy);
    }

    @Override
    public Direction onLaserHit(Direction prevDirection) {
        return prevDirection;
    }
}
