package objects;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import game.TiledMapStage;
import util.Direction;

public class BombTile extends OpaqueStaticTile {
    public BombTile(TiledMapTile copy) {
        super(copy);
    }

    @Override
    public Direction onLaserHit(Direction prevDirection) {
        getStage().update(TiledMapStage.StageUpdateType.LOSE);

        return super.onLaserHit(prevDirection);
    }
}
