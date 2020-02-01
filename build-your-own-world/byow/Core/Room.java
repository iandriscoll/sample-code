package byow.Core;

import java.awt.Point;

public class Room {

    private Point tl;
    private Point br;

    public Point getTopLeft() {
        return tl;
    }

    public Point getBottomRight() {
        return br;
    }

    public Room() {
        int height = RandomUtils.uniform(WorldGenerator.getRandom(), 4, 6);
        int width = RandomUtils.uniform(WorldGenerator.getRandom(), 4, 11);
        int leftX = RandomUtils.uniform(WorldGenerator.getRandom(), Engine.WIDTH - width);
        int topY = RandomUtils.uniform(WorldGenerator.getRandom(), height, Engine.HEIGHT - height);
        tl = new Point(leftX, topY);
        br = new Point(leftX + width, topY - height);

    }

    public Room(Hallway h, int height, int width) {
        switch (h.getDirection()) {
            case 0:
                int leftX = h.getTopLeft().x;
                int rightX = leftX + width;
                int bottomY = h.getTopLeft().y;
                int topY = bottomY + height;
                tl = new Point(leftX, topY);
                br = new Point(rightX, bottomY);
                break;
            case 1:
                leftX = h.getTopLeft().x;
                rightX = leftX + width;
                bottomY = h.getBottomRight().y - height;
                topY = h.getBottomRight().y;
                tl = new Point(leftX, topY);
                br = new Point(rightX, bottomY);
                break;
            case 2:
                leftX = h.getTopLeft().x - width;
                rightX = leftX + width;
                topY = h.getTopLeft().y;
                bottomY = topY - height;
                tl = new Point(leftX, topY);
                br = new Point(rightX, bottomY);
                break;
            case 3:
                leftX = h.getBottomRight().x;
                rightX = leftX + width;
                bottomY = h.getBottomRight().y;
                topY = bottomY + height;
                tl = new Point(leftX, topY);
                br = new Point(rightX, bottomY);
                break;
            default: break;
        }

    }

}
