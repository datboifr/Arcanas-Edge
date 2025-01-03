package main;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import objects.*;
import objects.enemies.Enemy;
import objects.projectiles.Projectile;
import objects.particles.ParticleManager;
import upgrademenu.UpgradeMenu;
import upgrademenu.UpgradePool;

public class GraphicsPanel extends JPanel implements Runnable {

    // Screen settings
    final int WIDTH = 800;
    final int HEIGHT = 600;

    Thread gameThread;
    boolean isRunning = false;
    KeyHandler keyHandler = new KeyHandler();
    ParticleManager particleManager;

    // time
    final double interval = 1000000000.0 / 60; // Time per frame in nanoseconds
    private int fps = 0;
    private int fpsCounter = 0;

    // Game objects
    ArrayList<GameObject> objects;
    ArrayList<Enemy> enemies;
    ArrayList<Projectile> projectiles;
    Player player;
    Random random;
    GameObject platform;
    GameObject background;

    UpgradeMenu upgradeMenu;

    boolean upgradeMenuEnabled;
    boolean waveEnabled;
    int wave, enemyLimit, enemyCounter, spawnTimer;

    // Constructor
    GraphicsPanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(new Color(.2f, .2f, .2f));
        this.setFocusable(true);
        this.addKeyListener(keyHandler);

        random = new Random();
        particleManager = new ParticleManager();

        // Initialize lists
        this.objects = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.projectiles = new ArrayList<>();

        player = new Player(150, 150, 35, 35, keyHandler, projectiles);

        platform = new GameObject(WIDTH / 2, HEIGHT / 2, "map/Platform");
        background = new GameObject(WIDTH / 2, HEIGHT / 2, "map/Map");

        waveEnabled = false;
        wave = 0;

        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
        upgradeMenu = new UpgradeMenu(new Rectangle(50, 50, WIDTH - 100, HEIGHT - 100), keyHandler,
                UpgradePool.getUpgradePool(player));
    }

    // Inside your update method, just before rendering the objects
    public void sortObjectsByX() {
        Collections.sort(objects, new Comparator<GameObject>() {
            @Override
            public int compare(GameObject o1, GameObject o2) {
                // Compare based on the x-coordinate, from highest to lowest
                return Integer.compare(o2.getX(), o1.getX());
            }
        });
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime(); // Track time for frame rate control
        long timer = System.currentTimeMillis(); // Timer for FPS calculation
        double delta = 0;

        while (isRunning) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / interval; // Calculate the frame delta
            lastTime = currentTime; // Update the lastTime for the next loop

            // Update FPS every second (1000 milliseconds)
            if (System.currentTimeMillis() - timer >= 1000) {
                fps = fpsCounter; // Update FPS
                fpsCounter = 0; // Reset FPS counter
                timer += 1000; // Reset the timer
            }

            if (delta >= 1) {
                update(delta); // Update game objects
                repaint(); // Repaint the screen
                delta--; // Decrease delta to maintain frame rate
                fpsCounter++; // Increment FPS counter
            }
        }
    }

    public void startWave() {
        wave++;
        enemyLimit = wave + 4;
        waveEnabled = true;
        this.spawnTimer = 0;
    }

    public void endWave() {
        waveEnabled = false;
        enemyCounter = 0;
        upgradeMenu.fill();
        upgradeMenuEnabled = true;
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
        enemies.add(new Enemy(x, y, 30, 30, player, particleManager));
        enemyCounter++;
    }

    public void update(double delta) {

        // Handle the upgrade menu
        if (upgradeMenuEnabled) {
            upgradeMenu.update();
            upgradeMenuEnabled = !keyHandler.zActive;
        } else {
            // Update all game objects
            player.update();
            for (Enemy enemy : enemies) {
                enemy.update(delta, enemies, projectiles);
            }
            for (Projectile projectile : projectiles) {
                projectile.update();
                projectile.checkPositionOnScreen(WIDTH, HEIGHT);
            }

            // Handle the wave logic
            if (waveEnabled) {
                if (keyHandler.aActive) {
                    enemies.clear();
                    endWave();
                } else if (enemies.isEmpty() && (enemyCounter == enemyLimit)) {
                    endWave(); // End the wave if all enemies are defeated and the limit is reached
                } else if ((enemyCounter < enemyLimit) && (spawnTimer <= 0)) {
                    spawnEnemy(); // Spawn new enemies if the enemy counter is less than the limit
                    spawnTimer = (int) (random.nextFloat(1, 3) * 60);
                }
            } else if (player.isTouching(platform)) {
                startWave(); // Start a new wave if the player touches the platform
            }

            spawnTimer--;
            particleManager.update();

            // Clear the objects list and add all relevant objects for rendering
            objects.clear();
            objects.addAll(enemies);
            objects.addAll(projectiles);
            objects.add(player);

            // Collect all dead objects to remove them
            ArrayList<GameObject> dead = new ArrayList<>();
            for (GameObject object : objects) {
                if (object.isDead()) {
                    dead.add(object);
                    if (object instanceof Enemy) {
                        enemies.remove(object); // Remove dead enemies
                    } else if (object instanceof Projectile) {
                        projectiles.remove(object); // Remove dead projectiles
                    } else if (object instanceof Player) {
                        endGame();
                    }
                }
            }
            objects.removeAll(dead);
            sortObjectsByX();
        }
    }

    private void endGame() {
        isRunning = false;
    }

    // Render the game objects
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics;

        background.draw(g);
        platform.draw(g);

        // synchronized (objects) {
        for (GameObject obj : objects) {
            if (obj != null) {
                obj.draw(g);
            }
        }

        particleManager.draw(g);

        if (upgradeMenuEnabled) {
            upgradeMenu.draw(g);
        }

        // debug stuff
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 10));

        // Debug: Show FPS
        g.drawString("FPS: " + fps, 10, 180); // Display FPS at the bottom of the debug section

        g.drawString("Spawn Timer: " + spawnTimer, 10, 200);
        g.drawString("UpgradeMenu Active?: " + upgradeMenuEnabled, 10, 210);
        g.drawString("Wave Active? " + waveEnabled, 10, 220);
        g.drawString("Wave: " + wave, 10, 230);

        g.drawString("Enemy Limit: " + enemyLimit, 10, 250);
        g.drawString("# of Enemies: " + enemies.size(), 10, 260);
        g.drawString("Enemy Counter: " + enemyCounter, 10, 270);

        g.drawString("Player Health " + player.getHealth(), 10, 290);
        g.drawString("Player Agility " + player.getSpeed(), 10, 300);

        g.drawString("Player Contact Damage: " + player.getContactDamage(), 10, 310);
        g.drawString("Player Ability Cooldown: " + player.getAbilityCooldown(), 10, 320);

        g.drawString("Player Projectile Damage: " + player.getProjectileDamage(), 10, 330);
        g.drawString("Player Projectile Speed: " + player.getProjectileSpeed(), 10, 340);
        g.drawString("Player Projectile Size: " + player.getProjectileSize(), 10, 350);

        g.drawString("Player Cooldown 1: " + player.getAbilities()[0].getCooldownTimer(), 10, 370);
        g.drawString("Player Cooldown 2: " + player.getAbilities()[1].getCooldownTimer(), 10, 380);

        g.drawString("# of Projectiles: " + projectiles.size(), 10, 400);
    }
}
