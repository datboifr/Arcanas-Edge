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
    int wave;
    int enemiesPerWave;

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

        wave = 0;
        waveActive = false;

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

    public void startWave() {
        waveActive = true;
        wave++;
        enemiesPerWave = 10;
    }
    
    // Update game state
    public void update(double delta) {

        player.update();

        if (waveActive) {
            if (enemies.size() < enemiesPerWave) spawnEnemy();
            for (Enemy enemy : enemies) enemy.update(delta, enemies);
        } else if (player.touching(platform)) startWave();        
    }
            
    private void spawnEnemy() {
        int side = random.nextInt(0, 3); // 0 = top, 1 = bottom, 2 = left, 3 = right
        int x = 0, y = 0;

        switch (side) {
            case 0: x = random.nextInt(WIDTH); y = -50; break;
            case 1: x = random.nextInt(WIDTH); y = HEIGHT + 50; break;
            case 2: x = - 50; y = random.nextInt(HEIGHT); break;
            case 3: x = WIDTH + 50; y = random.nextInt(HEIGHT); break;
        }

        enemies.add(new Enemy(x, y, 50, 50, player));
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics;
                      
        //render order
        objects.clear();
        objects.add(platform);
        objects.addAll(enemies);
        objects.add(player);

        // synchronized (objects) {
        for (Object obj : objects) {
            if (obj != null) {
                obj.draw(g2);
            }
        }
    }
}
