package objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import game.GridCell;
import game.StageObserver;
import game.TiledMapStage;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import util.Direction;


/*
    This suppression is due to the fact that pmd hates variables initialized in loops,
    I will ask the TA about it.
 */
@SuppressWarnings({"PMD.DataflowAnomalyAnalysis", "PMD.AvoidLiteralsInIfCondition"})
public class Laser implements StageObserver {
    protected transient Direction startDir;
    protected transient HashSet<LampTile> lampsHit;
    protected transient ArrayList<Point.Float> laserPath;

    private transient TiledMapTileLayer layer;
    private transient Point start;
    private transient float tileWidth;


    /**
     * Initialises a new laser.
     *
     * @param level     the tiled map where the laser is located.
     * @param locationX the x location of the laser
     * @param locationY the y location of the laser
     * @param startDir  the starting direction of the laser
     */
    public Laser(TiledMap level, int locationX, int locationY, Direction startDir) {
        this.start = new Point(locationX, locationY);
        this.startDir = startDir;
        this.layer = (TiledMapTileLayer) level.getLayers().get("level");
        this.tileWidth = level.getProperties().get("tilewidth", Integer.class);

        this.lampsHit = new HashSet<>();
        this.laserPath = new ArrayList<>();
    }

    /**
     * Add a point to the laser path at the center of the given point.
     * @param point point to add;
     */
    protected void addLaserPoint(Point point) {
        laserPath.add(new Point.Float(point.x + 0.5f, point.y + 0.5f));
    }

    /**
     * Updates the laser path only when necessary.
     * @param type type of event occurring.
     */
    @Override
    public void update(TiledMapStage.StageUpdateType type) {
        if (type == TiledMapStage.StageUpdateType.LASERPATH) {
            update();
        }
    }

    /**
     * Reset laser path and recalculate it.
     */
    public void update() {
        Direction dir = startDir;
        // Direction.NONE can be used to turn the laser off.
        if (dir == Direction.NONE) {
            return;
        }

        laserPath = new ArrayList<>();
        Point cur = new Point();
        cur.move(start.x, start.y);
        addLaserPoint(cur);

        // reset all lamps hit
        for (LampTile lamp : lampsHit) {
            lamp.turnOff();
        }
        lampsHit.clear();

        // this loop runs until the laser has been blocked or left the map.
        do {
            // find the coordinates of the next cell to visit based on the laser direction.
            cur.translate(dir.getX(), dir.getY());

            // check if laser is out of bounds.
            if (cur.x < 0 || cur.x >= layer.getWidth() || cur.y < 0 || cur.y >= layer.getHeight()) {
                addLaserPoint(cur);
                break;
            }

            Tile tile = ((GridCell) layer.getCell(cur.x, cur.y)).getTile();

            if (tile instanceof LampTile) {
                lampsHit.add((LampTile) tile);
            }

            // get next direction from hitting tile.
            Direction prevDir = dir;
            dir = tile.onLaserHit(dir);

            // check if laser is blocked.
            if (dir == Direction.NONE) {
                // we want to add a new point not at the center of the tile blocking the laser,
                // but at the edge of the tile.
                laserPath.add(new Point.Float(cur.x + (1 - prevDir.getX()) * 0.5f,
                    cur.y + (1 - prevDir.getY()) * 0.5f));
                break;
            }

            // only add a new point at places where the laser changes direction (corners in path).
            if (prevDir != dir) {
                addLaserPoint(cur);
            }
        } while (true);
    }

    /**
     * Draws the laser path as a red line.
     * @param camera the view camera.
     */
    public void drawLaser(OrthographicCamera camera, ShapeRenderer sr) {
        if (laserPath.size() <= 1) {
            return;
        }

        sr.setColor(Color.RED);
        sr.setProjectionMatrix(camera.combined);
        sr.begin(ShapeRenderer.ShapeType.Line);

        for (int i = 0; i < laserPath.size() - 1; i++) {
            Point.Float p1 = laserPath.get(i);
            Point.Float p2 = laserPath.get(i + 1);
            sr.line(p1.x * tileWidth, p1.y * tileWidth, p2.x * tileWidth, p2.y * tileWidth);
        }

        sr.end();
    }

}
