package objects;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import org.lwjgl.Sys;
import util.Direction;

public class LampTile extends TransparentTile {
    private transient boolean isOn;

    public LampTile(TiledMapTile copy) {
        super(copy);
        this.isOn = false;
    }

    @Override
    public Direction onLaserHit(Direction prevDirection) {
        if (!isOn) {
            this.isOn = true;
            getStage().decrementLampsLeft();
        }
        return super.onLaserHit(prevDirection);
    }

    /**
     * Turns the lamp off if it is currently on and updates the lamp progression tracked by the
     * stage.
     */
    public void turnOff() {
        if (isOn) {
            this.isOn = false;
            getStage().incrementLampsLeft();
        }
    }
}
