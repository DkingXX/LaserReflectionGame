package screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import game.LaserReflection;
import game.Player;

public class SettingsMenuScreen extends Table {
    /**
     * This class sets up a menu. It has
     * a resume settings and exit button.
     * @param game the current game instance.
     * @param player the player currently logged in.
     */
    public SettingsMenuScreen(LaserReflection game, Player player) {
        setFillParent(true);
        setUpButtons(game, player);
        setVisible(false);
    }

    private void setUpButtons(LaserReflection game, Player player) {
        Skin skin = new Skin(Gdx.files.internal("skin/quantum-horizon-ui.json"));
        TextButton resumeButton = new TextButton("Resume", skin);
        TextButton exitButton = new TextButton("Exit", skin);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setVisible(false);
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LevelSelectScreen(game, player));
            }
        });

        add(resumeButton);
        row();
        add(exitButton);
        center();
    }

    /**
     * This method creates a processor to check if the escape key is pressed.
     * When the escape key is pressed, our menu will hide and show.
     * @param stage the stage to add the menu to.
     */
    public void setupInputProcessor(Stage stage) {
        Gdx.input.setInputProcessor(new InputMultiplexer(stage, new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {

                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK)) {
                    if (isVisible()) {
                        setVisible(false);
                    } else {
                        setVisible(true);
                    }

                }
                return false;
            }
        }));
        stage.addActor(this);
    }
}
