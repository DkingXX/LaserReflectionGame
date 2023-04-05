package util;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import org.junit.jupiter.api.extension.Extension;
import org.mockito.Mockito;

public class HeadLessTestExtension implements Extension, ApplicationListener {

    /**
     * Starts up a headless LibGDX application for testing purposes.
     */
    public HeadLessTestExtension() {
        Gdx.gl = Mockito.mock(GL20.class);
        Gdx.gl20 = Mockito.mock(GL20.class);

        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        new HeadlessApplication(this, config);
    }

    /**
     * Called when the {@link Application} is first created.
     */
    @Override
    public void create() {

    }

    /**
     * Called when the {@link Application} is resized. This can happen at any point during a
     * non-paused state but will never happen
     * before a call to {@link #create()}.
     *
     * @param width  the new width in pixels
     * @param height the new height in pixels
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     * Called when the {@link Application} should render itself.
     */
    @Override
    public void render() {

    }

    /**
     * Called when the {@link Application} is paused, usually when it's not active or visible
     * on-screen. An Application is also
     * paused before it is destroyed.
     */
    @Override
    public void pause() {

    }

    /**
     * Called when the {@link Application} is resumed from a paused state, usually when it
     * regains focus.
     */
    @Override
    public void resume() {

    }

    /**
     * Called when the {@link Application} is destroyed. Preceded by a call to {@link #pause()}.
     */
    @Override
    public void dispose() {

    }
}
