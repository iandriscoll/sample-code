package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;
import java.awt.Point;

/** World Generator class that contains constructor to generate a world with randomly
 * sized and placed rooms and hallways.
 * Rooms are connected by hallways
 * Hallways can be connected by both rooms and other hallways.
 */
public class WorldGenerator {

    private TETile[][] world;
    private static Random RANDOM;

    public static Random getRandom() {
        return RANDOM;
    }


    // WorldGenerator constructor that takes in a seed and generates a random world of TETiles
    public WorldGenerator(long seed) {

        world = new TETile[Engine.WIDTH][Engine.HEIGHT];
        RANDOM = new Random(seed);
        for (int i = 0; i < Engine.HEIGHT; i++) {
            for (int j = 0; j < Engine.WIDTH; j++) {
                world[j][i] = Tileset.NOTHING;
            }
        }
        Room firstRoom = new Room();
        System.out.println(firstRoom.getBottomRight());
        System.out.println(firstRoom.getTopLeft());
        placeRoom(firstRoom);
        for (int j = 0; j < 4; j++) {
            generate(firstRoom, j);
        }

        this.normalizeWorld();
    }

    /** Method that takes in a room as the argument and places the room in the world using
     * its coordinates by placing walls and floors in the appropriate locations.
     */

    private void placeRoom(Room r) {

        for (int a = r.getBottomRight().y; a <= r.getTopLeft().y; a++) {
            if (a >= Engine.HEIGHT
                    || r.getBottomRight().x >= Engine.WIDTH
                    || r.getTopLeft().x < 0
                    || a < 0) {
                break;
            }
            if (!world[r.getTopLeft().x][a].equals(Tileset.FLOOR)) {
                world[r.getTopLeft().x][a] = Tileset.WALL;
            }
            if (!world[r.getBottomRight().x][a].equals(Tileset.FLOOR)) {
                world[r.getBottomRight().x][a] = Tileset.WALL;
            }
        }
        for (int b = r.getTopLeft().x; b <= r.getBottomRight().x; b++) {
            if (b >= Engine.WIDTH || r.getBottomRight().y < 0
                || r.getTopLeft().y >= Engine.HEIGHT) {
                break;
            }
            if (!world[b][r.getTopLeft().y].equals(Tileset.FLOOR)) {
                world[b][r.getTopLeft().y] = Tileset.WALL;
            }
            if (!world[b][r.getBottomRight().y].equals(Tileset.FLOOR)) {
                world[b][r.getBottomRight().y] = Tileset.WALL;
            }
        }

        for (int c = r.getTopLeft().x + 1; c < r.getBottomRight().x; c++) {
            for (int d = r.getBottomRight().y + 1; d < r.getTopLeft().y; d++) {
                if (c >= Engine.WIDTH || d >= Engine.HEIGHT
                        || c < 0
                        || d < 0) {
                    break;
                }
                world[c][d] = Tileset.FLOOR;
            }
        }
    }

    /** Method that takes in the first room in the world and recursively
     * generates and places hallways
     * and rooms across the world.
     * NOTE: currently in room -> hallway -> room -> hallway -> ... order.
     *       Does not generate multiple hallways per room, hallway leading to another hallway,  etc.
     */

    /**private void generate(Room rm) {
        int orientZero = RandomUtils.uniform(RANDOM, 4);
        Hallway h = new Hallway(orientZero);
        if (!checkHallwayDir(rm, h)) {
            System.out.println("one");
            int orientOne = RandomUtils.getRandomWithExclusion(RANDOM, 0, 4, orientZero);
            Hallway h2 = new Hallway(orientOne);
            if (!checkHallwayDir(rm, h2)) {
                System.out.println("two");
                int orientTwo = RandomUtils.getRandomWithExclusion(RANDOM,
     0, 4, orientZero, orientOne);
                Hallway h3 = new Hallway(orientTwo);
                if (!checkHallwayDir(rm, h3)) {
                    System.out.println("three");
                    int orientThree = RandomUtils.getRandomWithExclusion(RANDOM,
     0, 4, orientZero, orientOne, orientTwo);
                    Hallway h4 = new Hallway(orientThree);
                    if (!checkHallwayDir(rm, h4)) {
                        System.out.println("four");
                        return;
                    } else {
                        placeHallway(rm, h4);
                        int height = RandomUtils.uniform(WorldGenerator.RANDOM, 4, 6);
                        int width = RandomUtils.uniform(WorldGenerator.RANDOM, 4, 11);
                        if (checkRoomDir(h4, width, height)) {
                            Room rm4 = new Room(h4, height, width);
                            placeRoom(rm4);
                            generate(rm4);
                        }
                    }
                } else {
                    placeHallway(rm, h3);
                    int height = RandomUtils.uniform(WorldGenerator.RANDOM, 4, 6);
                    int width = RandomUtils.uniform(WorldGenerator.RANDOM, 4, 11);
                    if (checkRoomDir(h3, width, height)) {
                        Room rm3 = new Room(h3, height, width);
                        placeRoom(rm3);
                        generate(rm3);
                    }
                }
            } else {
                placeHallway(rm, h2);
                int height = RandomUtils.uniform(WorldGenerator.RANDOM, 4, 6);
                int width = RandomUtils.uniform(WorldGenerator.RANDOM, 4, 11);
                if (checkRoomDir(h2, width, height)) {
                    Room rm2 = new Room(h2, height, width);
                    placeRoom(rm2);
                    generate(rm2);
                }
            }
        } else {
            placeHallway(rm, h);
            int height = RandomUtils.uniform(WorldGenerator.RANDOM, 4, 6);
            int width = RandomUtils.uniform(WorldGenerator.RANDOM, 4, 11);
            if (checkRoomDir(h, width, height)) {
                Room rm1 = new Room(h, height, width);
                placeRoom(rm1);
                generate(rm1);
            }
        }

    }
     */

    private void generate(Room firstRoom, int orient) {
        Hallway hall = new Hallway(orient);
        if (checkHallwayDir(firstRoom, hall)) {
            placeHallway(firstRoom, hall);
            int height = RandomUtils.uniform(WorldGenerator.RANDOM, 4, 6);
            int width = RandomUtils.uniform(WorldGenerator.RANDOM, 4, 11);
            Room hallRoom = new Room(hall, height, width);
            if (checkRoomDir(hall, width, height)) {
                placeRoom(hallRoom);
                int newOrient = RandomUtils.getRandomWithExclusion(RANDOM,
                        0, 4, hall.getDirection());
                generate(hallRoom, newOrient);
            } else {
                return;
            }
        } else {
            return;
        }

    }


    /**
     * Function that takes in a room and a hallway and places the
     * hallway in the appropriate location.
     */
    private void placeHallway(Room rm, Hallway h) {
        switch (h.getDirection()) {
            case 0:
                int leftSideX = RandomUtils.uniform(RANDOM,
                        rm.getTopLeft().x, rm.getBottomRight().x - 2);
                Point tl = new Point(leftSideX, rm.getTopLeft().y + h.getHeight());
                Point br = new Point(leftSideX + 2, rm.getTopLeft().y);
                h.setCoordinates(tl, br);
                for (int i = 0; i <= h.getHeight(); i++) {
                    if (rm.getTopLeft().y + i >= 40 || leftSideX + 2 >= Engine.WIDTH) {
                        break;
                    }
                    world[leftSideX][rm.getTopLeft().y + i] = Tileset.WALL;
                    world[leftSideX + 1][rm.getTopLeft().y + i] = Tileset.FLOOR;
                    world[leftSideX + 2][rm.getTopLeft().y + i] = Tileset.WALL;
                }
                break;
            case 1:
                leftSideX = RandomUtils.uniform(RANDOM,
                        rm.getTopLeft().x, rm.getBottomRight().x - 2);
                tl = new Point(leftSideX, rm.getBottomRight().y);
                br = new Point(leftSideX + 2, rm.getBottomRight().y - h.getHeight());
                h.setCoordinates(tl, br);
                for (int i = 0; i <= h.getHeight(); i++) {
                    if (rm.getBottomRight().y - i < 0 || leftSideX + 2 >= Engine.WIDTH) {
                        break;
                    }
                    world[leftSideX][rm.getBottomRight().y - i] = Tileset.WALL;
                    world[leftSideX + 1][rm.getBottomRight().y - i] = Tileset.FLOOR;
                    world[leftSideX + 2][rm.getBottomRight().y - i] = Tileset.WALL;
                }
                break;
            case 2:
                int topSideY = RandomUtils.uniform(RANDOM,
                        rm.getBottomRight().y + 2, rm.getTopLeft().y);
                tl = new Point(rm.getTopLeft().x - h.getWidth(), topSideY);
                br = new Point(rm.getTopLeft().x, topSideY - 2);
                h.setCoordinates(tl, br);
                for (int i = 0; i <= h.getWidth(); i++) {
                    if (rm.getTopLeft().x - i < 0 || rm.getTopLeft().x - i >= Engine.WIDTH
                        || topSideY - 2 < 0) {
                        break;
                    }
                    world[rm.getTopLeft().x - i][topSideY] = Tileset.WALL;
                    world[rm.getTopLeft().x - i][topSideY - 1] = Tileset.FLOOR;
                    world[rm.getTopLeft().x - i][topSideY - 2] = Tileset.WALL;
                }
                break;
            case 3:
                topSideY = RandomUtils.uniform(RANDOM,
                        rm.getBottomRight().y + 2, rm.getTopLeft().y);
                tl = new Point(rm.getBottomRight().x, topSideY);
                br = new Point(rm.getBottomRight().x + h.getWidth(), topSideY - 2);
                h.setCoordinates(tl, br);
                for (int i = 0; i <= h.getWidth(); i++) {
                    if (rm.getBottomRight().x + i >= 50 || topSideY - 2 < 0) {
                        break;
                    }
                    world[rm.getBottomRight().x + i][topSideY] = Tileset.WALL;
                    world[rm.getBottomRight().x + i][topSideY - 1] = Tileset.FLOOR;
                    world[rm.getBottomRight().x + i][topSideY - 2] = Tileset.WALL;
                }
                break;
            default: break;
        }
    }

    // Method that returns the world, a 2D array of TETiles
    public TETile[][] getWorld() {
        return world;
    }

    /** Method that takes in a room and a hallway and returns a boolean indicating whether or not
     * a hallway can be added in its designated direction.
     */
    private boolean checkHallwayDir(Room rm, Hallway hway) {
        int dir = hway.getDirection();

        switch (dir) {
            case 0:
                return (rm.getTopLeft().y + hway.getHeight() <= Engine.HEIGHT);
            case 1:
                return (rm.getBottomRight().y - hway.getHeight() > 0);
            case 2:
                return (rm.getTopLeft().x - hway.getWidth() > 0);
            case 3:
                return (rm.getBottomRight().getX() + hway.getWidth() <= Engine.WIDTH);
            default: return false;
        }
    }

    public static boolean checkRoomDir(Hallway hway, int width, int height) {
        int dir = hway.getDirection();

        switch (dir) {
            case 0:
                return (hway.getTopLeft().getY() + height < Engine.HEIGHT);
            case 1:
                return (hway.getBottomRight().getY() - height >= 0);
            case 2:
                return (hway.getTopLeft().x - width >= 0);
            case 3:
                return (hway.getBottomRight().x + width < Engine.WIDTH);
            default:
                return false;
        }
    }

    public void normalizeWorld() {
        for (int w = 1; w < Engine.WIDTH - 1; w++) {
            for (int h = 1; h < Engine.HEIGHT - 1; h++) {
                if (world[w][h].equals(Tileset.WALL) && world[w - 1][h].equals(Tileset.FLOOR)
                    && world[w + 1][h].equals(Tileset.FLOOR)) {
                    world[w][h] = Tileset.FLOOR;
                }
                if (world[w][h].equals(Tileset.WALL) && world[w][h - 1].equals(Tileset.FLOOR)
                        && world[w][h + 1].equals(Tileset.FLOOR)) {
                    world[w][h] = Tileset.FLOOR;
                }
                if (world[w][h].equals(Tileset.FLOOR)
                    &&
                        ((world[w - 1][h].equals(Tileset.NOTHING))
                                || (world[w + 1][h].equals(Tileset.NOTHING))
                                || (world[w][h + 1].equals(Tileset.NOTHING))
                                || (world[w][h - 1].equals(Tileset.NOTHING)))) {
                    world[w][h] = Tileset.WALL;
                }
            }
        }

    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(Engine.WIDTH, Engine.HEIGHT);
        WorldGenerator worldGen = new WorldGenerator(3994);
        ter.renderFrame(worldGen.getWorld());
    }
}
