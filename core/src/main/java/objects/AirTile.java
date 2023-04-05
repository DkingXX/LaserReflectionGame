package objects;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import game.TiledMapStage;
import util.Direction;

public class AirTile extends TransparentTile {
    public AirTile(TiledMapTile copy) {
        super(copy);
    }

    @Override
    public void clicked(InputEvent evt, float x, float y) {
        int mirrorsLeft = getStage().getMirrorsLeft();

        if (evt.getButton() == Input.Buttons.LEFT && mirrorsLeft > 0) {
            this.getCell().setTile(Material.MIRROR);
            getStage().update(TiledMapStage.StageUpdateType.LASERPATH);
            getStage().setMirrorsLeft(mirrorsLeft - 1);
        }
    }
}