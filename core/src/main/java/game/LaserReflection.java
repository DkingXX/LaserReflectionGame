package game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import screens.AuthenticationScreen;

/**
 * The game class which contains the render method and the sprite batch.
 */
public class LaserReflection extends Game {
    private transient SpriteBatch batch;

    public static final float red = .1f;
    public static final float green = 0.12f;
    public static final float blue = 0.16f;
    public static final float alpha = 1;

    public static final int width = 700;
    public static final int height = 700;

    public static final String[] LEVELS = {"test1.tmx", "test2.tmx", "test3.tmx", "test4.tmx",
        "test5.tmx", "test6.tmx", "test7.tmx"};

    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new AuthenticationScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    /**
     * Gets the sprite batch.
     * @return the sprite batch
     */
    public SpriteBatch getBatch() {
        return batch;
    }

}
