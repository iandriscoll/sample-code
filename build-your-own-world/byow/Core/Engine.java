package byow.Core;

import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 50;
    public static final int HEIGHT = 40;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {

        Font font = new Font("Arial", Font.BOLD, 36);
        StdDraw.setFont(font);
        StdDraw.text(0.5, 0.85, "Build Your Own World");
        font = new Font("Arial", Font.BOLD, 20);
        StdDraw.setFont(font);
        StdDraw.text(0.5, 0.6, "New World (N)");
        StdDraw.text(0.5, 0.55, "Load World (L)");
        StdDraw.text(0.5, 0.5, "Quit World (:Q)");

        KeyboardInputSource typer = new KeyboardInputSource();
        char c = typer.getNextKey();
        while (true) {
            if (c == 'N') {
                StdDraw.clear();
                StdDraw.text(0.5, 0.85, "Enter a random seed (numbers only)");
                StdDraw.text(0.5, 0.55, "When done, press S");
                String seed = "";
                c = typer.getNextKey();
                while (Character.isDigit(c)) {
                    seed += c;
                    StdDraw.clear();
                    StdDraw.text(0.5, 0.85, "Enter a random seed");
                    StdDraw.text(0.5, 0.55, "When done, press S");
                    StdDraw.text(0.5, 0.8, seed);
                    c = typer.getNextKey();
                }
                if (c == 'S') {
                    WorldState thisWorld = new WorldState();
                    TETile[][] ourWorld = new WorldGenerator(Long.parseLong(seed)).getWorld();
                    thisWorld.updateWorld(ourWorld);
                    ter.initialize(Engine.WIDTH, Engine.HEIGHT);
                    ter.renderFrame(ourWorld);
                    System.out.println(seed);

                    Random rand = new Random();
                    int avatarX = RandomUtils.uniform(rand, Engine.WIDTH);
                    int avatarY = RandomUtils.uniform(rand, Engine.HEIGHT);
                    while (!(ourWorld[avatarX][avatarY].equals(Tileset.FLOOR))) {
                        avatarX = RandomUtils.uniform(rand, Engine.WIDTH);
                        avatarY = RandomUtils.uniform(rand, Engine.HEIGHT);
                    }
                    ourWorld[avatarX][avatarY] = Tileset.AVATAR;
                    thisWorld.updateAvatar(avatarX, avatarY);
                    thisWorld.updateWorld(ourWorld);
                    TETile[][] vis = ableToSee(thisWorld);
                    ter.renderFrame(vis);
                    moveInteract(thisWorld, ourWorld, thisWorld.getAvaX(), thisWorld.getAvaY());

                }
                /**TERenderer ter = new TERenderer();
                 ter.initialize(Engine.WIDTH, Engine.HEIGHT);


                 TETile[][] myWorld = interactWithInputString(seed);


                 ter.renderFrame(myWorld);*/
            } else if (c == 'L') {
                WorldState w = loadWorld(false);
                moveInteract(w, w.getWorld(), w.getAvaX(), w.getAvaY());
            } else if (c == ':') {
                c = typer.getNextKey();
                if (c == 'Q') {
                    System.exit(0);
                }
            }

        }

    }

    private TETile[][] ableToSee(WorldState w) {
        if (w.isLight()) {
            return w.getWorld();
        }
        TETile[][] x = new TETile[WIDTH][HEIGHT];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                x[j][i] = Tileset.NOTHING;
            }
        }
        TETile[][] fullWorld = w.getWorld();
        x[w.getAvaX()][w.getAvaY()] = Tileset.AVATAR;
        for (int k = 0; k <= 3; k++) {
            for (int j = 0; j <= 3; j++) {
                if (!indexOutOfBounds(w.getAvaX() + k, w.getAvaY() + j)) {
                    x[w.getAvaX() + k][w.getAvaY() + j] = fullWorld[w.getAvaX() + k][w.getAvaY() + j];
                }
                if (!indexOutOfBounds(w.getAvaX() - k, w.getAvaY() + j)) {
                    x[w.getAvaX() - k][w.getAvaY() + j] = fullWorld[w.getAvaX() - k][w.getAvaY() + j];
                }
                if (!indexOutOfBounds(w.getAvaX() + k, w.getAvaY() - j)) {
                    x[w.getAvaX() + k][w.getAvaY() - j] = fullWorld[w.getAvaX() + k][w.getAvaY() - j];
                }
                if (!indexOutOfBounds(w.getAvaX() - k, w.getAvaY() - j)) {
                    x[w.getAvaX() - k][w.getAvaY() - j] = fullWorld[w.getAvaX() - k][w.getAvaY() - j];
                }
            }

        }
        return x;
    }

    private WorldState loadWorld(boolean txt) {
        File f;
        if (!txt) {
            f = new File("./save_data");
        } else {
            f = new File("./save_data.txt");
        }
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                return (WorldState) os.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }
        return new WorldState();
    }

    private void mouseHover(WorldState w) {
        int xSpot = (int) StdDraw.mouseX();
        int ySpot = (int) StdDraw.mouseY();
        w.setMouse(xSpot, ySpot);
        if (w.getWorld()[xSpot][ySpot].equals(Tileset.FLOOR)) {
            w.setMouse(xSpot, ySpot);
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(2, Engine.HEIGHT - 2, "floor");
        } else if (w.getWorld()[xSpot][ySpot].equals(Tileset.WALL)) {
            w.setMouse(xSpot, ySpot);
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(2, Engine.HEIGHT - 2, "wall");
        } else if (w.getWorld()[xSpot][ySpot].equals(Tileset.AVATAR)) {
            w.setMouse(xSpot, ySpot);
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(2, Engine.HEIGHT - 2, "you");
        } else if (w.getWorld()[xSpot][ySpot].equals(Tileset.NOTHING)) {
            w.setMouse(xSpot, ySpot);
            StdDraw.enableDoubleBuffering();
            StdDraw.setPenColor(Color.white);
            StdDraw.text(2, Engine.HEIGHT - 2, "nothing");
        }
        StdDraw.text(Engine.WIDTH - 8, Engine.HEIGHT - 2, "Press T to turn on/off light");
        StdDraw.show();
    }



    private void moveInteract(WorldState a, TETile[][] ourWorld, int avatarX, int avatarY) {
        ter.initialize(Engine.WIDTH, Engine.HEIGHT);
        TETile[][] vis = ableToSee(a);
        ter.renderFrame(vis);
        KeyboardInputSource newTyper = new KeyboardInputSource();
        char c = newTyper.getNextKey();

        while (true) {
            if (c == 'W') {
                if (ourWorld[avatarX][avatarY + 1].equals(Tileset.FLOOR)) {
                    ourWorld[avatarX][avatarY] = Tileset.FLOOR;
                    ourWorld[avatarX][avatarY + 1] = Tileset.AVATAR;
                    a.updateWorld(ourWorld);
                    avatarY = avatarY + 1;
                    a.updateAvatar(avatarX, avatarY);
                    vis = ableToSee(a);
                    ter.renderFrame(vis);
                    c = newTyper.getNextKey();
                    runThrough(newTyper, a);
                } else if (ourWorld[avatarX][avatarY + 1].equals(Tileset.WALL)) {
                    c = newTyper.getNextKey();
                    runThrough(newTyper, a);
                }
            } else if (c == 'S') {
                if (ourWorld[avatarX][avatarY - 1].equals(Tileset.FLOOR)) {
                    ourWorld[avatarX][avatarY] = Tileset.FLOOR;
                    ourWorld[avatarX][avatarY - 1] = Tileset.AVATAR;
                    avatarY = avatarY - 1;
                    a.updateAvatar(avatarX, avatarY);
                    a.updateWorld(ourWorld);
                    vis = ableToSee(a);
                    ter.renderFrame(vis);
                    c = newTyper.getNextKey();
                    runThrough(newTyper, a);
                } else if (ourWorld[avatarX][avatarY - 1].equals(Tileset.WALL)) {
                    c = newTyper.getNextKey();
                    runThrough(newTyper, a);
                }
            } else if (c == 'A') {
                if (ourWorld[avatarX - 1][avatarY].equals(Tileset.FLOOR)) {
                    ourWorld[avatarX][avatarY] = Tileset.FLOOR;
                    ourWorld[avatarX - 1][avatarY] = Tileset.AVATAR;
                    avatarX = avatarX - 1;
                    a.updateAvatar(avatarX, avatarY);
                    a.updateWorld(ourWorld);
                    vis = ableToSee(a);
                    ter.renderFrame(vis);
                    c = newTyper.getNextKey();
                    runThrough(newTyper, a);
                } else if (ourWorld[avatarX - 1][avatarY].equals(Tileset.WALL)) {
                    c = newTyper.getNextKey();
                    runThrough(newTyper, a);
                }
            } else if (c == 'D') {
                if (ourWorld[avatarX + 1][avatarY].equals(Tileset.FLOOR)) {
                    ourWorld[avatarX][avatarY] = Tileset.FLOOR;
                    ourWorld[avatarX + 1][avatarY] = Tileset.AVATAR;
                    avatarX = avatarX + 1;
                    a.updateAvatar(avatarX, avatarY);
                    a.updateWorld(ourWorld);
                    vis = ableToSee(a);
                    ter.renderFrame(vis);
                    c = newTyper.getNextKey();
                    runThrough(newTyper, a);
                } else if (ourWorld[avatarX + 1][avatarY].equals(Tileset.WALL)) {
                    c = newTyper.getNextKey();
                    runThrough(newTyper, a);

                }
            } else if (c == ':') {
                while ((newTyper.possibleNextInput())) {
                    c = newTyper.getNextKey();
                    if (c == 'Q') {
                        saveFile(a, false);
                    }
                }
            } else if (c == 'T') {
                a.setLight(!a.isLight());
                vis = ableToSee(a);
                ter.renderFrame(vis);
                c = newTyper.getNextKey();
            } else if (c == 'N') {
                StdDraw.clear();
                StdDraw.text(Engine.WIDTH / 2, 30, "Enter a random seed (numbers only)");
                StdDraw.text(Engine.WIDTH / 2, 20, "When done, press S");
                StdDraw.show();
                String seed = "";
                c = newTyper.getNextKey();
                while (Character.isDigit(c)) {
                    seed += c;
                    StdDraw.clear();
                    StdDraw.text(25, 30, "Enter a random seed");
                    StdDraw.text(25, 20, "When done, press S");
                    StdDraw.text(25, 25, seed);
                    StdDraw.show();
                    c = newTyper.getNextKey();
                }
                if (c == 'S') {
                    WorldState thisWorld = new WorldState();
                    ourWorld = new WorldGenerator(Long.parseLong(seed)).getWorld();
                    thisWorld.updateWorld(ourWorld);
                    ter.initialize(Engine.WIDTH, Engine.HEIGHT);
                    ter.renderFrame(ourWorld);
                    System.out.println(seed);

                    Random rand = new Random();
                    avatarX = RandomUtils.uniform(rand, Engine.WIDTH);
                    avatarY = RandomUtils.uniform(rand, Engine.HEIGHT);
                    while (!(ourWorld[avatarX][avatarY].equals(Tileset.FLOOR))) {
                        avatarX = RandomUtils.uniform(rand, Engine.WIDTH);
                        avatarY = RandomUtils.uniform(rand, Engine.HEIGHT);
                    }
                    ourWorld[avatarX][avatarY] = Tileset.AVATAR;
                    thisWorld.updateAvatar(avatarX, avatarY);
                    thisWorld.updateWorld(ourWorld);
                    vis = ableToSee(thisWorld);
                    ter.renderFrame(vis);
                    moveInteract(thisWorld, ourWorld, thisWorld.getAvaX(), thisWorld.getAvaY());
                }
            } else {
                c = newTyper.getNextKey();
            }
        }
    }

    private void runThrough(KeyboardInputSource n, WorldState a) {
        while (!n.possibleNextInput()) {
            mouseHover(a);
            StdDraw.clear();
            TETile[][] vis = ableToSee(a);
            ter.renderFrame(vis);
        }
    }


    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, both of these calls:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        StringInputDevice myInput = new StringInputDevice(input);
        TETile[][] ourWorld = new TETile[Engine.WIDTH][Engine.HEIGHT];
        WorldState thisWorld;
        int avatarX;
        int avatarY;

        while (myInput.possibleNextInput()) {
            char c = myInput.getNextKey();
            if (c == 'N') {
                String seed = "";
                c = myInput.getNextKey();
                while (Character.isDigit(c)) {
                    seed = seed + Character.toString(c);
                    if (myInput.possibleNextInput()) {
                        c = myInput.getNextKey();
                    }
                }
                if (c == 'S') {
                    thisWorld = new WorldState();
                    System.out.println(seed);
                    ourWorld = new WorldGenerator(Long.parseLong(seed)).getWorld();
                    thisWorld.updateWorld(ourWorld);

                    Random rand = new Random(5);
                    avatarX = RandomUtils.uniform(rand, Engine.WIDTH);
                    avatarY = RandomUtils.uniform(rand, Engine.HEIGHT);
                    while (!(ourWorld[avatarX][avatarY].equals(Tileset.FLOOR))) {
                        avatarX = RandomUtils.uniform(rand, Engine.WIDTH);
                        avatarY = RandomUtils.uniform(rand, Engine.HEIGHT);
                    }
                    ourWorld[avatarX][avatarY] = Tileset.AVATAR;
                    thisWorld.updateAvatar(avatarX, avatarY);
                    thisWorld.updateWorld(ourWorld);

                    if (myInput.possibleNextInput()) {
                        c = myInput.getNextKey();
                    }
                    stringMovement(c, myInput, thisWorld);
                }
            } else if (c == 'L') {
                WorldState w = loadWorld(true);
                stringMovement(myInput.getNextKey(), myInput, w);
                ourWorld = w.getWorld();
            }
        }
        return ourWorld;
    }

    private void stringMovement(char c, StringInputDevice myInput, WorldState w) {
        while (myInput.possibleNextInput()) {
            if (c == 'W') {
                if (!indexOutOfBounds(w.getAvaX(), w.getAvaY() + 1)
                        && w.getWorld()[w.getAvaX()][w.getAvaY() + 1].equals(Tileset.FLOOR)) {
                    w.getWorld()[w.getAvaX()][w.getAvaY()] = Tileset.FLOOR;
                    w.getWorld()[w.getAvaX()][w.getAvaY() + 1] = Tileset.AVATAR;
                    int avatarX = w.getAvaX();
                    int avatarY = w.getAvaY() + 1;
                    w.updateAvatar(avatarX, avatarY);
                    w.updateWorld(w.getWorld());
                    c = myInput.getNextKey();
                } else if (indexOutOfBounds(w.getAvaX(), w.getAvaY() + 1)
                        || w.getWorld()[w.getAvaX()][w.getAvaY() + 1].equals(Tileset.WALL)) {
                    if (myInput.possibleNextInput()) {
                        c = myInput.getNextKey();
                    }
                }
            } else if (c == 'S') {
                if (!indexOutOfBounds(w.getAvaX(), w.getAvaY() - 1)
                        && w.getWorld()[w.getAvaX()][w.getAvaY() - 1].equals(Tileset.FLOOR)) {
                    w.getWorld()[w.getAvaX()][w.getAvaY()] = Tileset.FLOOR;
                    w.getWorld()[w.getAvaX()][w.getAvaY() - 1] = Tileset.AVATAR;
                    w.updateAvatar(w.getAvaX(), w.getAvaY() - 1);
                    w.updateWorld(w.getWorld());
                    if (myInput.possibleNextInput()) {
                        c = myInput.getNextKey();
                    }
                } else if (indexOutOfBounds(w.getAvaX(), w.getAvaY() - 1)
                        || w.getWorld()[w.getAvaX()][w.getAvaY() - 1].equals(Tileset.WALL)) {
                    if (myInput.possibleNextInput()) {
                        c = myInput.getNextKey();
                    }
                }
            } else if (c == 'A') {
                if (!indexOutOfBounds(w.getAvaX() - 1, w.getAvaY())
                        && w.getWorld()[w.getAvaX() - 1][w.getAvaY()].equals(Tileset.FLOOR)) {
                    w.getWorld()[w.getAvaX()][w.getAvaY()] = Tileset.FLOOR;
                    w.getWorld()[w.getAvaX() - 1][w.getAvaY()] = Tileset.AVATAR;
                    int avatarX = w.getAvaX() - 1;
                    int avatarY = w.getAvaY();
                    w.updateAvatar(avatarX, avatarY);
                    w.updateWorld(w.getWorld());
                    if (myInput.possibleNextInput()) {
                        c = myInput.getNextKey();
                    }
                } else if (indexOutOfBounds(w.getAvaX() - 1, w.getAvaY())
                        || w.getWorld()[w.getAvaX() - 1][w.getAvaY()].equals(Tileset.WALL)) {
                    if (myInput.possibleNextInput()) {
                        c = myInput.getNextKey();
                    }
                }
            } else if (c == 'D') {
                if (!indexOutOfBounds(w.getAvaX() + 1, w.getAvaY())
                        && w.getWorld()[w.getAvaX() + 1][w.getAvaY()].equals(Tileset.FLOOR)) {
                    w.getWorld()[w.getAvaX()][w.getAvaY()] = Tileset.FLOOR;
                    w.getWorld()[w.getAvaX() + 1][w.getAvaY()] = Tileset.AVATAR;
                    int avatarX = w.getAvaX() + 1;
                    int avatarY = w.getAvaY();
                    w.updateAvatar(avatarX, avatarY);
                    w.updateWorld(w.getWorld());
                    if (myInput.possibleNextInput()) {
                        c = myInput.getNextKey();
                    }
                } else if (indexOutOfBounds(w.getAvaX() + 1, w.getAvaY())
                        || w.getWorld()[w.getAvaX() + 1][w.getAvaY()].equals(Tileset.WALL)) {
                    if (myInput.possibleNextInput()) {
                        c = myInput.getNextKey();
                    }
                }
            } else if (c == ':') {
                while ((myInput.possibleNextInput())) {
                    c = myInput.getNextKey();
                    if (c == 'Q') {
                        saveFile(w, true);
                    }
                }
            }
        }
    }

    private void saveFile(WorldState w, boolean txt) {
        File f;
        if (txt) {
            f = new File("./save_data.txt");
        } else {
            f = new File("./save_data");
        }
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(w);
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            if (!txt) {
                System.exit(0);
            }
        } catch (IOException e) {
            System.out.println(e);
            if (!txt) {
                System.exit(0);
            }
        }
        if (!txt) {
            System.exit(0);
        }
    }


    private static boolean indexOutOfBounds(int x, int y) {
        return x >= Engine.WIDTH || x < 0 || y >= Engine.HEIGHT || y < 0;
    }

    public static void main(String[] args) {
        /**TETile[][] myWorld = interactWithInputString("n8702095859193238354ssswadswwds");
        System.out.println(myWorld.toString());
        TERenderer ter = new TERenderer();
        ter.initialize(Engine.WIDTH, Engine.HEIGHT);
        ter.renderFrame(myWorld);*/
    }
}


