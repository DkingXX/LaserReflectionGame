package game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.HashSet;
import objects.LampTile;
import objects.TileFactory;

/**
 * The TiledMapStage is an instance of Stage.
 * Furthermore this class is used to map each cell of a tiled map to an Actor and track the
 * current level.
 */

@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
public class TiledMapStage extends Stage {

    private transient TiledMap map;
    private transient HashSet<StageObserver> registered;
    private transient TileFactory tileFactory;

    // keeps track of the amount of lamps left before winning
    private transient int lampsLeft;

    // keeps track of maximum mirrors allowed to be placed
    private int mirrorsLeft;

    /**
     * Used for identifying what kind of an update happened to pass to the observers.
     */
    public enum StageUpdateType {
        LASERPATH, WIN, LOSE
    }

    /**
     * Initialises a TiledMapStage.
     * Furthermore this will create a TiledMapActor for each cell of the TiledMap
     * this method gets as parameter.
     * @param map The tiled map for which we want to create the TiledMapActors.
     * @param viewport The viewport to pass to the Stage constructor.
     * @param batch The sprite batch to pass to the Stage constructor.
     */
    public TiledMapStage(TiledMap map, Viewport viewport, SpriteBatch batch) {
        super(viewport, batch);
        this.map = map;
        this.registered = new HashSet<>();
        this.tileFactory = new TileFactory(map.getTileSets());
        this.lampsLeft = 0;
        this.mirrorsLeft = (int) map.getProperties().get("maxMirrors");

        createActors((TiledMapTileLayer) this.map.getLayers().get("level"));
    }

    /**
     * This helper method will go over every cell of the layer and
     * add a new TiledMapActor for that cell.
     * @param layer the layer for which tiledMapActors will be added
     */
    private void createActors(TiledMapTileLayer layer) {
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                createActor(layer, layer.getCell(x, y), x, y);
            }
        }
    }

    /**
     * This helper method will add a new TiledMapActor for a given cell.
     *
     * @param layer the layer for which the tiledMapActor will be added.
     * @param cell the cell for which tiledMapActor will be added.
     */
    private void createActor(TiledMapTileLayer layer, TiledMapTileLayer.Cell cell, int x, int y) {
        GridCell gridCell = new GridCell(cell, this.tileFactory);
        gridCell.getTile().setStage(this);
        layer.setCell(x, y, gridCell);

        if (gridCell.getTile() instanceof LampTile) {
            lampsLeft++;
        }

        Actor actor = new Actor();
        addActorToLayer(actor, layer, x, y);

        actor.addListener(new TiledMapInputListener(gridCell));
    }

    /**
     * This helper method add an actor to the Map layer.
     *
     * @param actor the actor that is distributed the input event by stage.
     * @param layer the layer for which the tiledMapActor will be added.
     */
    private void addActorToLayer(Actor actor, TiledMapTileLayer layer, int x, int y) {
        actor.setBounds((float)x * layer.getTileWidth(), (float)y * layer.getTileHeight(),
                layer.getTileWidth(), layer.getTileHeight());
        this.addActor(actor);
    }

    /**
     * Register a new observer.
     * @param observer the observer to register.
     */
    public void register(StageObserver observer) {
        registered.add(observer);
    }

    /**
     * Update all registered observers.
     * @param type type of update happening.
     */
    public void update(StageUpdateType type) {
        for (StageObserver observer : registered) {
            observer.update(type);
        }
    }

    /**
     * Adds one to amount of lamps left before winning.
     */
    public void incrementLampsLeft() {
        this.lampsLeft++;
    }

    /**
     * Subtracts one to amount of lamps left before winning. And checks if game has been won.
     */
    public void decrementLampsLeft() {
        this.lampsLeft--;

        if (this.lampsLeft <= 0) {
            update(StageUpdateType.WIN);
        }
    }

    public int getMirrorsLeft() {
        return this.mirrorsLeft;
    }

    public void setMirrorsLeft(int maxMirrors) {
        this.mirrorsLeft = maxMirrors;
    }
}
