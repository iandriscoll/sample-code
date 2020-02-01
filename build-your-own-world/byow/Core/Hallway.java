package byow.Core;

import java.awt.Point;

public class Hallway {

    // direction key: 0 is top, 1 is bottom, 2 is left, 3 is right
    private int direction;
    private int height;
    private int width;
    private Point tl;
    private Point br;

    public Hallway(int dir) {
        direction = dir;
        if (direction == 0 || direction == 1) {
            height = RandomUtils.uniform(WorldGenerator.getRandom(), 2, 6);
            width = 1;
        } else if (direction == 2 || direction == 3) {
            height = 1;
            width = RandomUtils.uniform(WorldGenerator.getRandom(), 2, 6);
        }
    }

    public int getDirection() {
        return direction;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Point getTopLeft() {
        return tl;
    }

    public Point getBottomRight() {
        return br;
    }

    public void setCoordinates(Point topLeft, Point bottomRight) {
        tl = topLeft;
        br = bottomRight;
    }
}
