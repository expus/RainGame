package com.gamedev.rain;

import com.gamedev.rain.entity.mob.Player;
import com.gamedev.rain.graphics.Screen;
import com.gamedev.rain.input.Keyboard;
import com.gamedev.rain.input.Mouse;
import com.gamedev.rain.level.Level;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Game extends Canvas implements Runnable {

    private static int width = 300;
    private static int height = width * 9 / 16;
    private static int scale = 3;
    private static final String TITLE = "Rain Game";

    private Screen screen;

    private Thread thread;
    private boolean running = false;
    private JFrame frame;
    private Keyboard keyboard;

    private Level level;
    private Player player;

    private BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    public Game() {
        Dimension size = new Dimension(width * scale, height * scale);
        setPreferredSize(size);
        frame = new JFrame();
        screen = new Screen(width, height);
        keyboard = new Keyboard();

        addKeyListener(keyboard);
        Mouse mouse = new Mouse();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        level = Level.spawn;
        player = new Player(17 * 16, 83 * 16, keyboard);
        player.initLevel(level);
    }

    public static int getWindowWidth() {
        return width * scale;
    }

    public static int getWindowHeight() {
        return height * scale;
    }

    public void update() {
        keyboard.update();
        player.update();
        level.update();
    }

    public void render() {
        BufferStrategy bufferStrategy = getBufferStrategy();
        if (bufferStrategy == null) {
            createBufferStrategy(3);
            return;
        }

        screen.clear();
        int xScroll = player.x - width / 2;
        int yScroll = player.y - height / 2;
        level.render(xScroll, yScroll, screen);
        player.render(screen);

        System.arraycopy(screen.pixels, 0, pixels, 0, pixels.length);

        Graphics graphics = bufferStrategy.getDrawGraphics();

        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, getWidth(), getHeight());

        graphics.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        graphics.dispose();
        bufferStrategy.show();
    }

    public synchronized void start() {
        running = true;
        thread = new Thread(this, "Display");
        thread.start();
    }

    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double nanoSeconds = 1000000000.0 / 60.0;
        double deltaTime = 0;

        requestFocus();

        int frames = 0, updates = 0;
        while (running) {
            long currentTime = System.nanoTime();
            deltaTime += (currentTime - lastTime) / nanoSeconds;
            lastTime = currentTime;

            while (deltaTime >= 1) {
                update();
                updates++;
                deltaTime--;
            }

            render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frame.setTitle(TITLE + " | fps: " + frames + " ups: " + updates);
                updates = 0;
                frames = 0;
            }
        }
        stop();
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.frame.setResizable(false);
        game.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.frame.setVisible(true);
        game.frame.setTitle(TITLE);
        game.frame.add(game);
        game.frame.pack();
        game.frame.setLocationRelativeTo(null);

        game.start();
    }
}
