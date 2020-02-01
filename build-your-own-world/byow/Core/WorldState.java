package byow.Core;

import byow.InputDemo.KeyboardInputSource;
import byow.TileEngine.TETile;

import java.io.Serializable;

public class WorldState implements Serializable {
    private TETile[][] world;
    private TETile[][] visibleWorld;
    private int avaX;
    private int avaY;
    private KeyboardInputSource k;
    private int xMouse;
    private int yMouse;
    private boolean light;

    public boolean isLight() {
        return light;
    }

    public void setLight(boolean l) {
        light = l;
    }

    public void setMouse(int x, int y) {
        xMouse = x;
        yMouse = y;
    }

    public int getYMouse() {
        return yMouse;
    }

    public int getXMouse() {
        return xMouse;
    }

    public void updateAvatar(int x, int y) {
        avaX = x;
        avaY = y;
    }

    public void updateWorld(TETile[][] w) {
        world = w;
    }

    public void updateVisibleWorld(TETile[][] vw) {
        visibleWorld = vw;
    }

    public TETile[][] getWorld() {
        return world;
    }

    public KeyboardInputSource getTyper() {
        return k;
    }

    public int getAvaX() {
        return avaX;
    }

    public int getAvaY() {
        return avaY;
    }

    public void setTyper(KeyboardInputSource key) {
        k = key;
    }

}
