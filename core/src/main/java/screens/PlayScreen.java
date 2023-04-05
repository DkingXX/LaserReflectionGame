package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import database.UserDao;
import game.LaserReflection;
import game.Level;
import game.Player;
import game.TiledMapStage;
import java.util.ArrayList;

public class PlayScreen implements Screen {

    private transient OrthographicCamera camera;
    private transient Viewport viewport;
    private transient MapRenderer mapRenderer;

    private transient TiledMapStage stage;
    private transient Level currentLevel;
    private transient SettingsMenuScreen settingsMenuScreen;

    private transient Player player;
    private transient LaserReflection game;

    /**
     * Creates a new instance of the PlayScreen.
     *
     * @param game the game class.
     */
    public PlayScreen(LaserReflection game, Player player, int level) {
        this.player = player;
        this.game = game;
        this.camera = new OrthographicCamera();
        this.settingsMenuScreen = new SettingsMenuScreen(game, player);

        loadLevel(level);
    }

    @Override
    public void show() {

    }

    /**
     * loads the next level or returns the player to the level select screen.
     */
    public void nextLevel() {
        this.dispose();
        int levelID = currentLevel.getID() + 1;

        if (levelID >= (new UserDao()).getPlayerHighscore(player) + 1) {
            // only increment the player's score if the level won is higher than their current
            // highest.
            (new UserDao()).updateScore(player);
        }

        if (levelID >= LaserReflection.LEVELS.length) {
            game.setScreen(new LevelSelectScreen(game, player));
            return;
        }

        loadLevel(levelID);
    }

    /**
     * Resets the current level.
     */
    public void resetLevel() {
        this.dispose();
        loadLevel(currentLevel.getID());
    }

    /**
     * Loads a level.
     *
     * @param levelID the level ID of the level to load
     */
    private void loadLevel(int levelID) {
        // construct new level
        this.currentLevel = new Level(LaserReflection.LEVELS[levelID], levelID, this, player);
        setupLevelRendering();
        this.stage = new TiledMapStage(currentLevel.getTiledMap(), viewport, game.getBatch());
        currentLevel.startObserving(stage);
        // makes sure there is a laser before user input
        currentLevel.getLaser().update();

        settingsMenuScreen.setupInputProcessor(stage);
    }

    private void setupLevelRendering() {
        MapProperties properties = currentLevel.getTiledMap().getProperties();

        float tilewidth = properties.get("tilewidth", Integer.class);
        float worldWidth = tilewidth * properties.get("width", Integer.class);
        float worldHeight = tilewidth * properties.get("height", Integer.class);

        this.viewport = new FitViewport(worldWidth, worldHeight, camera);
        this.camera.setToOrtho(false, worldWidth, worldHeight);

        this.mapRenderer = new OrthogonalTiledMapRenderer(currentLevel.getTiledMap());
    }


    @Override
    public void render(float delta) {
        mapRenderer.setView(camera);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRenderer.render();
        currentLevel.getLaser().drawLaser(camera, new ShapeRenderer());
        stage.act();
        stage.draw();
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        this.dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        currentLevel.getTiledMap().dispose();
    }
}
