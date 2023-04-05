package game;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import objects.Clickable;
import objects.Material;
import objects.Tile;
import objects.TileFactory;

/**
 * Object representing a cell in the 2D grid.
 */
public class GridCell extends Cell implements Clickable {
    private Tile tile;
    private transient TileFactory tileFactory;

    /**
     * Constructor from a scene2d cell to out custom cell.
     * @param cell the scene2d cell to convert.
     * @param tileFactory tile factory creating tiles.
     */
    public GridCell(Cell cell, TileFactory tileFactory) {
        this.tileFactory = tileFactory;
        this.setTile(this.tileFactory.getTile(cell.getTile()));
        this.getTile().setCell(this);
    }

    public Tile getTile() {
        return this.tile;
    }

    /**
     * Sets the tile contained in the cell to a different tile iff it is a valid tile
     * implementation.
     * @param tile tile to set in this cell.
     * @return the updated cell.
     */
    public Cell setTile(TiledMapTile tile) {
        if (tile instanceof Tile) {
            if (this.tile != null) {
                ((Tile) tile).setStage(this.tile.getStage());
            }

            this.tile = (Tile) tile;
            this.tile.setCell(this);
        }

        return this;
    }

    public void setTile(Material material) {
        this.setTile(this.tileFactory.getTile(material));
    }

    /**
     * State design pattern. Functionality of clicking changes based on what tile is currently in
     * the GridCell.
     * @param evt Clicking InputEvent.
     * @param x x coordinate clicked.
     * @param y y coordinate clicked.
     */
    @Override
    public void clicked(InputEvent evt, float x, float y) {
        this.tile.clicked(evt, x, y);
    }
}
