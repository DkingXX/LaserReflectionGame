package objects;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import game.GridCell;
import game.TiledMapStage;
import util.Direction;

public class MirrorTile extends Tile {
    public MirrorTile(TiledMapTile copy) {
        super(copy);
    }

    @Override
    public Direction onLaserHit(Direction prevDirection) {
        // test for each rotation
        switch (getRotation()) {
            // default is 0 rotation.
            default:
                switch (prevDirection) {
                    case RIGHT:
                        return Direction.DOWN;
                    case UP:
                        return Direction.LEFT;
                    default:
                        // laser hits back
                        return Direction.NONE;
                }
            case GridCell.ROTATE_90:
                switch (prevDirection) {
                    case LEFT:
                        return Direction.DOWN;
                    case UP:
                        return Direction.RIGHT;
                    default:
                        // laser hits back
                        return Direction.NONE;
                }
            case GridCell.ROTATE_180:
                switch (prevDirection) {
                    case LEFT:
                        return Direction.UP;
                    case DOWN:
                        return Direction.RIGHT;
                    default:
                        // laser hits back
                        return Direction.NONE;
                }
            case GridCell.ROTATE_270:
                switch (prevDirection) {
                    case RIGHT:
                        return Direction.UP;
                    case DOWN:
                        return Direction.LEFT;
                    default:
                        // laser hits back
                        return Direction.NONE;
                }
        }
    }

    @Override
    public void clicked(InputEvent evt, float x, float y) {
        if (evt.getButton() == Input.Buttons.LEFT) {
            // rotate clockwise
            int rot = this.getCell().getRotation() + 3;
            this.getCell().setRotation(rot % 4);
        } else {
            // change mirror to air
            this.getCell().setTile(Material.AIR);
            getStage().setMirrorsLeft(getStage().getMirrorsLeft() + 1);
        }

        getStage().update(TiledMapStage.StageUpdateType.LASERPATH);
    }

    /**
     * Get the mirror's rotation.
     * @return rotation of the mirror.
     */
    public int getRotation() {
        return this.getCell().getRotation();
    }
}
