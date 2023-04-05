package objects;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import game.GridCell;
import game.TiledMapStage;
import util.Direction;

/**
 * Custom scene2d tile implementation.
 */
public abstract class Tile extends StaticTiledMapTile implements Clickable {
    private GridCell cell;

    private TiledMapStage stage;

    public Tile(TiledMapTile copy) {
        super(copy.getTextureRegion());
        this.setId(copy.getId());
    }

    /**
     * Override this function if the tile should do something when clicked.
     * @param evt click event object.
     * @param x x coordinate clicked.
     * @param y y coordinate clicked.
     */
    @Override
    public void clicked(InputEvent evt, float x, float y) {

    }

    /**
     * Method called when laser hits the tile.
     * @param prevDirection previous direction of the laser hitting the tile.
     * @return new direction of the laser. Direction.NONE if laser gets blocked.
     */
    public abstract Direction onLaserHit(Direction prevDirection);

    public GridCell getCell() {
        return this.cell;
    }

    public void setCell(GridCell cell) {
        this.cell = cell;
    }

    public TiledMapStage getStage() {
        return stage;
    }

    public void setStage(TiledMapStage stage) {
        this.stage = stage;
    }
}