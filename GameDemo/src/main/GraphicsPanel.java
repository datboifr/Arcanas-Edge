package main;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import objects.Enemy;
import objects.GameObject;
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
    ArrayList<GameObject> objects;
    ArrayList<Enemy> enemies;
    Player player;
    Random random;
    GameObject platform;

    boolean storeEnabled;
    boolean waveEnabled;
    int wave, enemyLimit, enemyCounter;

    // Constructor
    GraphicsPanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(new Color(.2f, .2f, .2f));
        this.setFocusable(true);
        this.addKeyListener(keyHandler);

        random = new Random();

        // Initialize lists
        objects = new ArrayList<>();
        enemies = new ArrayList<>();

        player = new Player(0, 0, 35, 35, keyHandler);
        player.loadPlayerImages();

        platform = new GameObject(WIDTH / 2, HEIGHT / 2, 100, 100);

        waveEnabled = false;
        wave = 0;

        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        long lastTime = System.nanoTime();
        double delta = 0;

        while (isRunning) {

            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / interval;
            lastTime = currentTime;

            if (delta >= 1) {
                update(delta);
                repaint();
                delta--;
            }
        }
    }

    public void startWave() {
        wave++;
        enemyLimit = wave;
        waveEnabled = true;
    }

    public void endWave() {
        waveEnabled = false;
        enemyCounter = 0;
        // fillStore
        storeEnabled = true;
    }

    /**
     * Creates an enemy object on a random position beyond the screen
     */
    private void spawnEnemy() {
        int spawnSide = random.nextInt(4); // 0 = top, 1 = bottom, 2 = left, 3 = right
        int x = 0, y = 0;

        switch (spawnSide) {
            case 0: // Top
                x = random.nextInt(WIDTH);
                y = -30; // Above screen
                break;
            case 1: // Bottom
                x = random.nextInt(WIDTH);
                y = HEIGHT + 30; // Below screen
                break;
            case 2: // Left
                x = -30; // Left of screen
                y = random.nextInt(HEIGHT);
                break;
            case 3: // Right
                x = WIDTH + 30; // Right of screen
                y = random.nextInt(HEIGHT);
                break;
        }
        enemies.add(new Enemy(x, y, 30, 30, player));
        enemyCounter++;
    }

    // Update game state
    public void update(double delta) {

        if (storeEnabled) storeEnabled = !keyHandler.zActive;
        else {
            player.update();
            if (waveEnabled) {
            // spawns enemy if limit hasn't been reached
            if (enemyCounter < enemyLimit)
                spawnEnemy();
            else if (enemies.isEmpty() && (enemyCounter == enemyLimit))
                endWave();

            // updates all enemies
            ArrayList<Enemy> dead = new ArrayList<>();
            for (Enemy enemy : enemies) {
                if (enemy.health > 0)
                    enemy.update(delta, enemies);
                else
                    dead.add(enemy);
            }
            enemies.removeAll(dead);
            
        } else if (player.touching(platform)) startWave();

            //adds all objects to render
            objects.clear();
            objects.add(platform);
            objects.addAll(enemies);
            objects.add(player);
        }
    }

    // Render the game objects
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;

        // synchronized (objects) {
        for (GameObject obj : objects) {
            if (obj != null) {
                obj.draw(g);
            }
        }

        if (storeEnabled) {
            Rectangle store = new Rectangle(50, 50, WIDTH - 100, HEIGHT - 100);
            Color background = new Color(0f, 0f, 0f, .5f);
            g.setColor(background);
            g.fill(store);
            g.setColor(Color.WHITE);
            g.drawString("Press 'P' key to close", 10, 10);
        }

        // debug stuff
        g.setColor(Color.WHITE);
        g.drawString("Store Active?: " + storeEnabled, 10, 210);
        g.drawString("Wave Active? " + waveEnabled, 10, 220);
        g.drawString("Wave: " + wave, 10, 230);

        g.drawString("Enemy Limit: " + enemyLimit, 10, 250);
        g.drawString("# of Enemies: " + enemies.size(), 10, 260);
        g.drawString("Enemy Counter: " + enemyCounter, 10, 270);
    }

}
