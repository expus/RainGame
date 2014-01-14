package com.gamedev.rain.graphics;

public class Sprite {

    public final int SIZE;
    private int x, y;
    public int[] pixels;
    private SpriteSheet sheet;

    public static Sprite grass = new Sprite(16, 0, 0, SpriteSheet.tiles);
    public static Sprite voidSprite = new Sprite(16, 0x96f5ff);

    public static Sprite[] playerUp = {
            new Sprite(32, 1, 4, SpriteSheet.tiles),
            new Sprite(32, 0, 4, SpriteSheet.tiles),
            new Sprite(32, 2, 4, SpriteSheet.tiles)
    };

    public static Sprite[] playerSide = {
            new Sprite(32, 1, 3, SpriteSheet.tiles),
            new Sprite(32, 0, 3, SpriteSheet.tiles),
            new Sprite(32, 2, 3, SpriteSheet.tiles)
    };

    public static Sprite[] playerDown = {
            new Sprite(32, 1, 2, SpriteSheet.tiles),
            new Sprite(32, 0, 2, SpriteSheet.tiles),
            new Sprite(32, 2, 2, SpriteSheet.tiles)
    };

    public Sprite(int size, int colour) {
        SIZE = size;
        pixels = new int[SIZE * SIZE];
        setColour(colour);
    }

    private void setColour(int colour) {
        for (int i = 0; i < SIZE * SIZE; i++) {
            pixels[i] = colour;
        }
    }

    public Sprite(int size, int x, int y, SpriteSheet sheet) {
        SIZE = size;
        pixels = new int[SIZE * SIZE];
        this.x = x * SIZE;
        this.y = y * SIZE;
        this.sheet = sheet;
        loadImage();
    }

    private void loadImage() {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                pixels[x + y * SIZE] = sheet.pixels[(x + this.x) + (y + this.y) * sheet.SIZE];
            }
        }
    }

}
