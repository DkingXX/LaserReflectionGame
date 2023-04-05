package com.group62.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import game.LaserReflection;

/**
 * The launcher class that contains the main method.
 */
public class DesktopLauncher {

    /**
     * The main method that will start the laser game.
     * @param arg arguments
     */
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new LaserReflection(), config);
    }
}
