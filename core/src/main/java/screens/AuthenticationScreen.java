package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import database.UserDao;
import game.LaserReflection;
import game.Player;

public class AuthenticationScreen implements Screen {

    private transient LaserReflection game;
    protected transient Stage stage;
    private transient Viewport viewport;
    private transient OrthographicCamera camera;
    protected transient Skin skin;
    private transient Texture img;
    private transient Label label;

    /**
     * Login Screen of the game.
     *
     * @param game the game itself
     */
    public AuthenticationScreen(LaserReflection game) {
        this.game = game;

        img = new Texture("title.png");
        skin = new Skin(Gdx.files.internal("skin/quantum-horizon-ui.json"));

        camera = new OrthographicCamera();
        viewport = new FitViewport(LaserReflection.width, LaserReflection.height, camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        label = new Label("Welcome! Please login or register", skin);

        stage = new Stage(viewport);
    }

    @Override
    public void show() {
        //Stage should controll input:
        Gdx.input.setInputProcessor(stage);

        //Create Table
        Table mainTable = new Table();
        //Set table to fill stage
        mainTable.setFillParent(true);
        //Set alignment of contents in the table.
        mainTable.center();

        final TextField usernameTextField = new TextField("", skin);
        usernameTextField.setMessageText("Username");
        final TextField passwordTextField = new TextField("", skin);
        passwordTextField.setMessageText("Password");
        passwordTextField.setPasswordMode(true);
        passwordTextField.setPasswordCharacter('*');

        //Create buttons
        TextButton loginButton = new TextButton("Login", skin, "default");
        loginButton.setTransform(true);
        //loginButton.scaleBy(0.5f);

        TextButton registerButton = new TextButton("Register", skin, "default");
        registerButton.setTransform(true);
        //registerButton.scaleBy(0.5f);

        //Add listeners to buttons
        loginButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = usernameTextField.getText();
                String password = passwordTextField.getText();

                if (username == null || password == null
                        || username.trim().isEmpty() || password.trim().isEmpty()) {
                    label.setText("Please enter username and password");
                } else {
                    Player player = new Player(username, password);
                    UserDao userDao = new UserDao();
                    try {
                        if (userDao.login(player)) {
                            ((LaserReflection)Gdx.app.getApplicationListener())
                                    .setScreen(new MenuScreen(game, player));
                            usernameTextField.setText("");
                            passwordTextField.setText("");
                        } else {
                            label.setText("Login not successful! Check your password");
                            passwordTextField.setText("");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        registerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = usernameTextField.getText();
                String password = passwordTextField.getText();

                if (username == null || password == null
                        || username.trim().isEmpty() || password.trim().isEmpty()) {
                    label.setText("Please enter username and password");
                } else {
                    Player player = new Player(username, password);
                    UserDao userDao = new UserDao();
                    try {
                        if (userDao.register(player)) {
                            label.setText("Registration successful");
                            passwordTextField.setText("");
                        } else {
                            label.setText("Registration not successful");
                            usernameTextField.setText("");
                            passwordTextField.setText("");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mainTable.add(label).spaceBottom(25);
        mainTable.row();
        mainTable.add(usernameTextField);
        mainTable.row();
        mainTable.add(passwordTextField).spaceBottom(25);
        mainTable.row();
        mainTable.add(loginButton).height(70).width(175);
        mainTable.row();
        mainTable.add(registerButton).height(70).width(175);

        //Add table to stage
        stage.addActor(mainTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(LaserReflection.red, LaserReflection.green,
                LaserReflection.blue, LaserReflection.alpha);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().begin();
        game.getBatch().draw(img, 35, 350);
        game.getBatch().end();

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
        img.dispose();
        skin.dispose();
        stage.dispose();
    }
}


