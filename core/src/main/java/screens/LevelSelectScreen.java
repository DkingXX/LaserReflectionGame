package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import database.UserDao;
import game.LaserReflection;
import game.Player;

/**
 * Represent the screen for the level selection.
 */
public class LevelSelectScreen implements Screen {

    private transient Player player;
    private transient LaserReflection game;

    protected transient Stage stage;
    private transient Viewport viewport;
    private transient OrthographicCamera camera;
    protected transient Skin skin;
    private transient int levelsUnlocked;

    /**
     * Constructor for the LevelSelectScreen.
     *
     * @param game class constructor takes the game variable as a
     *             parameter and stores it in a global variable
     * @param player the player currently logged in.
     */
    public LevelSelectScreen(LaserReflection game, Player player) {
        this.game = game;
        this.player = player;

        skin = new Skin(Gdx.files.internal("skin/quantum-horizon-ui.json"));

        camera = new OrthographicCamera();
        viewport = new FitViewport(LaserReflection.width, LaserReflection.height, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport);

        this.levelsUnlocked = (new UserDao()).getPlayerHighscore(player);
    }


    @Override
    public void show() {
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);

        //Create Table for the levels.
        Table mainTable = new Table();
        //Create Table for the back button.
        Table mainTable2 = new Table();

        //Set table to fill stage
        mainTable.setFillParent(true);

        //Set alignment of contents in the tables.
        mainTable.center();
        mainTable2.bottom().left();

        for (int level = 0; level < LaserReflection.LEVELS.length; level++) {
            TextButton levelButton;

            if (level > this.levelsUnlocked) {
                // every level not unlocked yet will have this unclickable button.
                levelButton = new TextButton("?", skin);
                levelButton.setDisabled(true);
            } else {
                levelButton = new TextButton(Integer.toString(level + 1), skin);

                // this copy is necessary to pass the variable to a class.
                final int curLevel = level;
                levelButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        game.setScreen(new PlayScreen(game, player, curLevel));
                    }
                });
            }

            mainTable.add(levelButton);

            if (level % 2 != 0) {
                mainTable.row();
            }
        }

        TextButton backButton = new TextButton("Back", skin);
        mainTable2.add(backButton);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game, player));
            }
        });

        //Add table to stage
        stage.addActor(mainTable);
        stage.addActor(mainTable2);


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(LaserReflection.red, LaserReflection.green,
                LaserReflection.blue, LaserReflection.alpha);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
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
        skin.dispose();
        stage.dispose();

    }
}
