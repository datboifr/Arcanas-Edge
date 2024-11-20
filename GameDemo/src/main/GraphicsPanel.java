package main;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import objects.Enemy;
import objects.Object;
import objects.Player;

public class GraphicsPanel extends JPanel implements Runnable {

    // Screen settings
    final int WIDTH = 800;
    final int HEIGHT = 600;

    Thread gameThread;
    boolean isRunning = false;
    KeyHandler keyHandler = new KeyHandler();

    // time
    final double interval = 1000000000.0 / 60; // Time per frame in nanoseconds

    // Game objects
    ArrayList<Object> objects;
    ArrayList<Enemy> enemies;
    Player player;
    Random random;
    Object platform;

    Boolean waveActive;

    // Constructor
    GraphicsPanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(keyHandler);

        random = new Random();

        // Initialize lists
        objects = new ArrayList<>();
        enemies = new ArrayList<>();

        player = new Player(0, 0, 50, 50, keyHandler);
        player.loadPlayerImages();

        platform = new Object(WIDTH / 2, HEIGHT / 2, 100, 100);

    }

    // Start the game thread
    public void startGameThread() {
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    // Stop the game thread
    public void stopGameThread() {
        isRunning = false;
    }

    // Main game loop
    @Override
    public void run() {

        long lastTime = System.nanoTime();
        double delta = 0;
        waveActive = false;

        while (isRunning) {

            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / interval;
            lastTime = currentTime;

            if (delta >= 1) {
                // 1. UPDATE game state
                update(delta);
                repaint();
                delta--; // Consume one update step
            }
        }
    }

    // Update game state
    public void update(double delta) {

        player.update();

        if (waveActive) {
            if (enemies.size() < 1) enemies.add(new Enemy(WIDTH, HEIGHT, 50, 50, player));
            for (Enemy enemy : enemies) { enemy.moveTowardTarget(delta); }
        } else {
            if (player.touching(platform)) { waveActive = true; }
        }

        objects.clear();
        objects.addAll(enemies);
        objects.add(platform);
        objects.add(player);
    }

    // Render the game objects
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2d = (Graphics2D) graphics;

        // synchronized (objects) {
        for (Object obj : objects) {
            if (obj != null) {
                obj.draw(g2d);
            }
        }

    }

}
