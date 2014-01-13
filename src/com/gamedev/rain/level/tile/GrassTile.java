package com.gamedev.rain.level.tile;

import com.gamedev.rain.graphics.Screen;
import com.gamedev.rain.graphics.Sprite;

public class GrassTile extends Tile {

    public GrassTile(Sprite sprite) {
        super(sprite);
    }

    public boolean solid() {
        return false;
    }

    public void render(int x, int y, Screen screen) {
        screen.renderTile(x << 4, y << 4, this); // (x << 4) <=> (x * 16)
    }

}
