package game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * This class is a ClickListener that will be mapped to a GridCell.
 * The TiledMapActor covers a cell.
 * Hence you could say that this class registers input for that cell.
 */
public class TiledMapInputListener extends ClickListener {
    private transient GridCell cell;

    /**
     * Initialises a TiledMapInputListener.
     * @param cell the GridCell for which this class will register inputs.
     */
    public TiledMapInputListener(GridCell cell) {
        this.cell = cell;
        this.setButton(-1);
    }

    /**
     * Will register when the user clicks the TiledMapActor which contains a cell.
     * @param evt the click event.
     * @param x the x cords relative to the tiled map.
     * @param y the y cords relative to the tiled map.
     */
    @Override
    public void clicked(InputEvent evt, float x, float y) {
        this.cell.clicked(evt, x, y);
    }

    /**
     * Gets the cell it listens input for.
     * @return the cell
     */
    public GridCell getCell() {
        return cell;
    }

}
