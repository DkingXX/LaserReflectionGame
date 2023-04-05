package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import database.UserDao;
import game.LaserReflection;
import game.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class MenuScreen implements Screen {

    private transient LaserReflection game;
    private transient Player player;

    protected transient Stage stage;
    private transient Viewport viewport;
    private transient OrthographicCamera camera;
    protected transient Skin skin;

    /**
     * A constructor for MenuScreen which declares all the variables.
     *
     * @param game class constructor takes the game variable as a
     *             parameter and stores it in a global variable
     */
    public MenuScreen(LaserReflection game, Player player) {
        this.game = game;
        this.player = player;

        skin = new Skin(Gdx.files.internal("skin/quantum-horizon-ui.json"));

        camera = new OrthographicCamera();
        viewport = new FitViewport(LaserReflection.width, LaserReflection.height, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport);
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();

        TextButton playButton = new TextButton("Play", skin);

        TextButton logoutButton = new TextButton("Logout", skin);

        TextButton leaderboardBotton = new TextButton("Leaderboard", skin);



        logoutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((LaserReflection)Gdx.app.getApplicationListener())
                        .setScreen(new AuthenticationScreen(game));
            }
        });

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((LaserReflection) Gdx.app.getApplicationListener())
                        .setScreen(new LevelSelectScreen(game, player));
            }
        });

        leaderboardBotton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Dialog dialog = new Dialog("Leaderboard", skin);
                UserDao userDao = new UserDao();
                LinkedHashMap<String, Integer> scores = userDao.getTop10Players();
                Set set = scores.entrySet();
                Iterator iterator = set.iterator();
                while (iterator.hasNext()) {
                    Map.Entry mentry = (Map.Entry) iterator.next();
                    dialog.getContentTable().add((String)mentry.getKey());
                    Integer score = (Integer)mentry.getValue() * 100;
                    dialog.getContentTable().add(score.toString());
                    dialog.getContentTable().row();
                }
                dialog.button("Ok", true); //sends "true" as the result
                //dialog.key(Keys.Enter, true); //sends "true" when the ENTER key is pressed
                dialog.show(stage);
            }
        });


        mainTable.add(playButton).height(70).width(200);
        mainTable.row();
        mainTable.add(leaderboardBotton).height(70).width(200);
        mainTable.row();
        mainTable.add(logoutButton).height(70).width(200);
        mainTable.row();


        stage.addActor(mainTable);
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

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
