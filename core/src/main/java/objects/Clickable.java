package objects;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

public interface Clickable {
    void clicked(InputEvent evt, float x, float y);
}
