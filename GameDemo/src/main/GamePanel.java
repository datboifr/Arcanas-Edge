// GraphicsPanel.java
package main;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import objects.*;
import objects.enemies.Enemy;
import objects.enemies.EnemyPool;
import objects.projectiles.Projectile;
import objects.particles.Particle;
import objects.particles.ParticleManager;
import upgrademenu.UpgradeMenu;
import upgrademenu.UpgradePool;

/**
 * GraphicsPanel is a custom JPanel responsible for rendering and updating the
 * game logic.
 * It handles the main game loop, object updates, rendering, and wave
 * management.
 */
public class GamePanel extends JPanel implements Runnable {

    // Screen settings
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private Thread gameThread;
    private boolean isRunning = false;
    private final KeyHandler keyHandler = new KeyHandler();
    private final ParticleManager particleManager;
    private final EnemyPool enemyPool;

    // Time and FPS settings
    private static final double INTERVAL = 1_000_000_000.0 / 60; // Time per frame in nanoseconds
    private int fps = 0;
    private int fpsCounter = 0;

    // Game objects
    private final ArrayList<GameObject> objects;
    private final ArrayList<Enemy> enemies;
    private final ArrayList<Projectile> projectiles;
    private final ArrayList<Aura> aura;

    private final Player player;
    private final Random random = new Random();
    private final GameObject platform;
    private final GameObject background;

    public final UpgradeMenu menuWaveComplete;
    public final UpgradeMenu menuLevelUp;

    private UpgradeMenu currentMenu;
    private boolean upgradeMenuActive;
    private boolean waveActive;
    private int wave;
    private int enemyLimit;
    private int enemyCounter;
    private int spawnTimer;

    /**
     * Constructor to initialize the GraphicsPanel.
     */
    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(0.2f, 0.2f, 0.2f));
        setFocusable(true);
        addKeyListener(keyHandler);

        this.objects = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.aura = new ArrayList<>();

        enemyPool = new EnemyPool(this);
        particleManager = new ParticleManager();

        player = new Player(this, WIDTH / 2, HEIGHT / 2, 35, 35);
        platform = new GameObject(WIDTH / 2, HEIGHT / 2, "map/Platform");
        background = new GameObject(WIDTH / 2, HEIGHT / 2, "map/Map");

        menuLevelUp = new UpgradeMenu(new Rectangle(50, 50, WIDTH - 100, HEIGHT - 100),
                UpgradePool.getUpgrades(this, player), this, 3, 2);
        menuWaveComplete = new UpgradeMenu(new Rectangle(50, 50, WIDTH - 100, HEIGHT - 100),
                UpgradePool.getUpgrades(this, player), this, 1, 2);

        startGameLoop();
    }

    /**
     * Starts the main game loop in a separate thread.
     */
    private void startGameLoop() {
        isRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        double delta = 0;

        while (isRunning) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / INTERVAL;
            lastTime = currentTime;

            if (System.currentTimeMillis() - timer >= 1000) {
                fps = fpsCounter;
                fpsCounter = 0;
                timer += 1000;
            }

            if (delta >= 1) {
                update(delta);
                repaint();
                delta--;
                fpsCounter++;
            }
        }
    }

    /**
     * Updates game logic, including handling the upgrade menu, waves, and game
     * objects.
     *
     * @param delta Time delta for frame-independent updates.
     */
    public void update(double delta) {

        if (upgradeMenuActive) {
            currentMenu.update();
            return;
        }

        player.update();
        enemies.forEach(enemy -> enemy.update());
        aura.forEach(Aura::update);
        projectiles.forEach(projectile -> {
            projectile.update();
            projectile.checkPositionOnScreen(WIDTH, HEIGHT);
        });

        handleWaveLogic();
        particleManager.update();
        updateObjectsList();
    }

    /**
     * Handles wave spawning logic, including starting and ending waves.
     */
    private void handleWaveLogic() {
        if (waveActive) {
            spawnTimer--;
            if (keyHandler.aActive) {
                enemies.clear();
                endWave();
            } else if (enemies.isEmpty() && enemyCounter == enemyLimit) {
                endWave();
            } else if (enemyCounter < enemyLimit && spawnTimer <= 0) {
                spawnEnemy();
                spawnTimer = random.nextInt(2) * 60 + 60; // Random spawn interval
            }
        } else if (player.touching(platform)) {
            startWave();
        }
    }

    /**
     * Starts a new wave of enemies.
     */
    private void startWave() {
        wave++;
        enemyLimit = wave + 4;
        waveActive = true;
        spawnTimer = 0;
    }

    /**
     * Ends the current wave.
     */
    private void endWave() {
        waveActive = false;
        enemyCounter = 0;
    }

    /**
     * Spawns enemies at random positions outside the screen boundaries.
     */
    private void spawnEnemy() {
        int spawnSide = random.nextInt(4);
        int enemyCount = random.nextInt(3, 3 + wave);

        for (int i = 0; i < enemyCount; i++) {
            int x = 0, y = 0;

            switch (spawnSide) {
                case 0 -> {
                    x = random.nextInt(WIDTH);
                    y = -random.nextInt(30, 100);
                } // Top
                case 1 -> {
                    x = random.nextInt(WIDTH);
                    y = HEIGHT + random.nextInt(30, 100);
                } // Bottom
                case 2 -> {
                    x = -random.nextInt(30, 100);
                    y = random.nextInt(HEIGHT);
                } // Left
                case 3 -> {
                    x = WIDTH + random.nextInt(30, 100);
                    y = random.nextInt(HEIGHT);
                } // Right
            }

            enemies.add(
                    new Enemy(this, x, y, enemyPool.getEnemyPool().get(random.nextInt(enemyPool.getEnemyPool().size())),
                            player));
        }
        enemyCounter++;
    }

    /**
     * Updates the list of objects for rendering and removes dead objects.
     */
    private void updateObjectsList() {
        objects.clear();
        objects.addAll(enemies);
        objects.addAll(projectiles);
        objects.addAll(aura);
        objects.add(player);

        objects.removeIf(object -> {
            if (!object.isAlive()) {
                if (object instanceof Enemy)
                    enemies.remove(object);
                if (object instanceof Projectile)
                    projectiles.remove(object);
                if (object instanceof Aura)
                    aura.remove(object);
                if (object instanceof Player)
                    endGame();
                return true;
            }
            return false;
        });

        // sortObjectsByY(); <-- this needs to be fixed lol
    }

    /**
     * Sorts objects by their x-coordinate in descending order.
     */
    private void sortObjectsByY() {
        objects.sort(Comparator.comparingInt(GameObject::getY).reversed());
    }

    /**
     * Ends the game by stopping the game loop.
     */
    private void endGame() {
        isRunning = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        background.draw(g2d);
        platform.draw(g2d);
        objects.forEach(obj -> obj.draw(g2d));
        particleManager.draw(g2d);

        // player health bar
        g.setColor(Color.BLACK);
        g.fillRect(player.getX() - (player.getWidth() / 2), player.getY() + (player.getHeight() / 2), player.getWidth(),
                5);
        g.setColor(Color.RED);
        g.fillRect(player.getX() - (player.getWidth() / 2), player.getY() + (player.getHeight() / 2),
                (int) (player.getWidth() * (player.getHealth() / player.getMaxHealth())), 5);

        if (upgradeMenuActive) {
            menuLevelUp.draw(g2d);
        }

        drawDebugInfo(g2d);
    }

    /**
     * Draws debug information on the screen.
     *
     * @param g Graphics2D object.
     */
    private void drawDebugInfo(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 10));

        g.drawString("FPS: " + fps, 10, 20);
        g.drawString("Wave: " + wave, 10, 40);
        g.drawString("Enemies: " + enemies.size(), 10, 60);
        g.drawString("Player Health: " + player.getHealth(), 10, 80);
        g.drawString("Spawn Timer: " + spawnTimer, 10, 100);
        g.drawString("Levels: " + player.getLevels(), 10, 120);
        g.drawString("Projectiles: " + projectiles.size(), 10, 140);

        float auraCompletion = (float) player.getCurrentAura() / player.getRequiredAura();
        Color targetColor = new Color(207, 93, 54);

        Color interpolatedColor = new Color(
                1 - auraCompletion + targetColor.getRed() / 255f * auraCompletion,
                1 - auraCompletion + targetColor.getGreen() / 255f * auraCompletion,
                1 - auraCompletion + targetColor.getBlue() / 255f * auraCompletion);
        g.setColor(interpolatedColor);

        if (player.getCurrentAura() != 0) {
            g.fillRect(0, HEIGHT - 10, (int) (WIDTH * auraCompletion),
                    10);
        }
    }

    public void openUpgradeMenu(UpgradeMenu upgradeMenu) {
        currentMenu = upgradeMenu;
        currentMenu.fillSlots();
        upgradeMenuActive = true;
    }

    public void closeUpgradeMenu() {
        upgradeMenuActive = false;
    }

    public void spawnParticles(GameObject creator, Particle type, int amount, float speed) {
        particleManager.spawn(creator.getX(), creator.getY(), amount, type.getColor(), type.getSize(), speed,
                type.getLife(), type.getGravity());
    }

    // Getters

    public KeyHandler getKeyHandler() {
        return keyHandler;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }

    public ArrayList<Aura> getAura() {
        return aura;
    }

    public ParticleManager getParticleManager() {
        return particleManager;
    }

    public Player getPlayer() {
        return player;
    }

    public int getWave() {
        return wave;
    }
}
