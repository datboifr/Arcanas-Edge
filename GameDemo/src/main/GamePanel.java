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
 * GamePanel is a custom JPanel responsible for rendering and updating the game
 * logic.
 * It handles the main game loop, object updates, rendering, and wave
 * management.
 */
public class GamePanel extends JPanel implements Runnable {

    // Screen settings
    private static final float SCREEN_SCALE = 1.5f;
    private static final int WIDTH = (int) (800 * SCREEN_SCALE);
    private static final int HEIGHT = (int) (450 * SCREEN_SCALE);

    // Timing settings
    private static final double FRAME_INTERVAL = 1_000_000_000.0 / 60; // Time per frame in nanoseconds
    private static final int WAVE_COOLDOWN_FRAMES = 300; // 5 seconds in frames

    // Fonts and colors
    private static final Font FONT_SMALL = new Font("Arial", Font.PLAIN, 12);
    private static final Font FONT_LARGE = new Font("Arial", Font.BOLD, 50);
    private static final Font FONT_MEDIUM = new Font("Arial", Font.BOLD, 15);
    private static final Color COLOR_UI = new Color(1f, 1f, 1f);
    private static final Color COLOR_UI2 = new Color(1f, 1f, 1f, 0.5f);
    private static final Color COLOR_STATS = new Color(0, 0, 255);

    // ui objects and components
    private final UIElement WaveIndicator;

    // Game objects and components
    private final ArrayList<GameObject> gameObjects;
    private final ArrayList<Enemy> enemies;
    private final ArrayList<Projectile> projectiles;
    private final ArrayList<Aura> auras;

    private final Player player;
    private final GameObject platform;
    private final GameObject background;

    private final UpgradeMenu upgradeMenu;
    private final float UPGRADEMENU_MARGIN = 15f; // percentage
    private final ParticleManager particleManager;

    private final KeyHandler keyHandler = new KeyHandler();

    // Game state variables
    private boolean isRunning;
    private boolean upgradeMenuActive;
    private boolean waveActive;

    private int wave;
    private int waveCooldown;
    private int enemyLimit;
    private int enemyCounter;
    private int spawnTimer;

    private Thread gameThread;
    private int fps;

    /**
     * Constructor to initialize the GamePanel.
     */
    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        addKeyListener(keyHandler);

        WaveIndicator = new UIElement("icons/WaveIndicator", 50, WIDTH - 100, 50);

        this.gameObjects = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.auras = new ArrayList<>();
        this.particleManager = new ParticleManager();

        this.player = new Player(this, WIDTH / 2, HEIGHT / 2, 35, 35);
        this.platform = new GameObject(WIDTH / 2, HEIGHT / 2, "map/Platform");
        this.background = new GameObject(WIDTH / 2, HEIGHT / 2, "map/Map");

        this.upgradeMenu = new UpgradeMenu(
                new Rectangle(0 + (int) (WIDTH * UPGRADEMENU_MARGIN), 0 + (int) (HEIGHT * UPGRADEMENU_MARGIN),
                        WIDTH - (int) (WIDTH * UPGRADEMENU_MARGIN / 2), HEIGHT - -(int) (WIDTH
                                * UPGRADEMENU_MARGIN / 2)),
                UpgradePool.getUpgrades(this, player),
                this,
                3,
                2);

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
        int frameCount = 0;

        while (isRunning) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / FRAME_INTERVAL;
            lastTime = currentTime;

            if (delta >= 1) {
                updateGameState();
                repaint();
                delta--;
                frameCount++;
            }

            if (System.currentTimeMillis() - timer >= 1000) {
                fps = frameCount;
                frameCount = 0;
                timer += 1000;
            }
        }
    }

    /**
     * Updates the game state, including handling waves, upgrades, and objects.
     */
    private void updateGameState() {

        if (upgradeMenuActive) {
            upgradeMenu.update();
            return;
        }

        if (keyHandler.aActive)
            endWave();

        player.update();
        enemies.forEach(Enemy::update);
        projectiles.forEach(Projectile::update);
        auras.forEach(Aura::update);
        particleManager.update();

        handleWaveLogic();
        removeDeadObjects();
        syncObjectList();
    }

    /**
     * Handles the wave system logic, including starting, ending, and spawning
     * enemies.
     */
    private void handleWaveLogic() {
        if (waveActive) {
            spawnTimer--;
            if (enemies.isEmpty() && enemyCounter == enemyLimit) {
                endWave();
            } else if (enemyCounter < enemyLimit && spawnTimer <= 0) {
                spawnEnemy();
                spawnTimer = Math.max(30, 180 - (wave * 10));
            }
        } else if (waveCooldown-- <= 0) {
            startWave();
        }
    }

    /**
     * Starts a new wave of enemies.
     */
    private void startWave() {
        wave++;
        waveCooldown = WAVE_COOLDOWN_FRAMES;
        waveActive = true;
        enemyLimit = 8 + wave;
        enemyCounter = 0;
        spawnTimer = 0;
    }

    /**
     * Ends the current wave and opens the upgrade menu.
     */
    private void endWave() {
        waveActive = false;
        upgradeMenuActive = true;
    }

    /**
     * Spawns a new enemy at a random position off-screen.
     */
    private void spawnEnemy() {
        int spawnSide = new Random().nextInt(4);
        int x = 0, y = 0;

        switch (spawnSide) {
            case 0 -> {
                x = new Random().nextInt(WIDTH);
                y = -50;
            }
            case 1 -> {
                x = new Random().nextInt(WIDTH);
                y = HEIGHT + 50;
            }
            case 2 -> {
                x = -50;
                y = new Random().nextInt(HEIGHT);
            }
            case 3 -> {
                x = WIDTH + 50;
                y = new Random().nextInt(HEIGHT);
            }
        }

        enemies.add(new Enemy(this, x, y, EnemyPool.getRandomEnemy(this), player, wave));
        enemyCounter++;
    }

    /**
     * Removes objects that are no longer alive.
     */
    private void removeDeadObjects() {
        gameObjects.removeIf(object -> {
            if (!object.isAlive()) {
                if (object instanceof Enemy)
                    enemies.remove(object);
                if (object instanceof Projectile)
                    projectiles.remove(object);
                if (object instanceof Aura)
                    auras.remove(object);
                return true;
            }
            return false;
        });
    }

    /**
     * Synchronizes the game object list for rendering.
     */
    private void syncObjectList() {
        gameObjects.clear();
        gameObjects.addAll(enemies);
        gameObjects.addAll(projectiles);
        gameObjects.addAll(auras);
        gameObjects.add(player);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        background.draw(g2d);
        platform.draw(g2d);
        gameObjects.forEach(object -> object.draw(g2d));
        particleManager.draw(g2d);

        drawUI(g2d);

        if (upgradeMenuActive) {
            upgradeMenu.draw(g2d);
        }
    }

    /**
     * Draws the player's UI elements, such as health and stats.
     */
    /**
     * Draws debug information on the screen.
     *
     * @param g Graphics2D object.
     */
    private void drawUI(Graphics2D g) {

        final Color COLOR_UI = new Color(1f, 1f, 1f, 0.5f);
        g.setColor(COLOR_UI);
        g.setFont(new Font("Arial", Font.PLAIN, 10));

        g.drawString("FPS: " + fps, 10, 20);
        g.drawString("Wave: " + wave, 10, 40);
        g.drawString("Enemies: " + enemies.size(), 10, 60);
        g.drawString("Player Health: " + player.getHealth(), 10, 80);
        g.drawString("Spawn Timer: " + spawnTimer, 10, 100);
        g.drawString("Aura: " + player.getAura(), 10, 120);
        g.drawString("Projectiles: " + projectiles.size(), 10, 140);

        final Color Stats = COLOR_UI;
        g.setColor(Stats);
        g.setFont(FONT_SMALL);
        // Displays each stat and its value
        g.drawString(String.format("Magic Damage: %.1f", player.getProjectileDamage()), 10, 300);
        g.drawString(String.format("Magic Damage: %.1f", player.getProjectileDamage()), 10, 325);
        g.drawString(String.format("Max Health: %.1f", player.getMaxHealth()), 10, 350);
        g.drawString(String.format("Health: %.1f", player.getHealth()), 10, 375);
        g.drawString(String.format("Speed: %.1f", player.getSpeed()), 10, 400);
        g.drawString(String.format("Contact Damage: %.1f", player.getContactDamage()), 10, 425);
        g.drawString(String.format("Skill ooldown: %.1f", player.getAbilityCooldown()), 10, 450);
        g.drawString(String.format("Magic Speed: %.1f", player.getProjectileSpeed()), 10, 475);
        g.drawString(String.format("Magic Size: %.1f", player.getProjectileSize()), 10, 500);

        if (!waveActive && !upgradeMenuActive) {

            String timer = String.format("%.1f", waveCooldown / 60.0);
            g.setFont(new Font("Arial", Font.BOLD, 60));
            FontMetrics titleMetrics = g.getFontMetrics(g.getFont());

            int timerWidth = titleMetrics.stringWidth(timer);
            int timerHeight = titleMetrics.getHeight();

            int timerX = (WIDTH / 2) - (timerWidth / 2);
            int timerY = 100;

            g.drawString(String.format(timer), timerX, timerY);

            String subtext = "WAVE STARTS IN";
            g.setFont(new Font("Arial", Font.BOLD, 15));
            FontMetrics subtextMetrics = g.getFontMetrics(g.getFont());

            int subtextWidth = subtextMetrics.stringWidth(subtext);

            g.drawString(subtext, (WIDTH / 2) - (subtextWidth / 2), timerHeight - 30);
        }

        WaveIndicator.draw(g);
        g.setFont(FONT_MEDIUM);
        g.setColor(Color.WHITE);
        g.drawString(String.format("%d", wave), WaveIndicator.getFrame().x + (WaveIndicator.getFrame().width / 2),
                WaveIndicator.getFrame().y + 15);

        /**
         * float auraCompletion = (float) player.getAura() / player.getRequiredAura();
         * Color targetColor = new Color(207, 93, 54);
         * 
         * Color interpolatedColor = new Color(
         * 1 - auraCompletion + targetColor.getRed() / 255f * auraCompletion,
         * 1 - auraCompletion + targetColor.getGreen() / 255f * auraCompletion,
         * 1 - auraCompletion + targetColor.getBlue() / 255f * auraCompletion);
         * g.setColor(interpolatedColor);
         * 
         * if (player.getAura() != 0) {
         * g.fillRect(0, HEIGHT - 10, (int) (WIDTH * auraCompletion),
         * 10);
         * }
         **/
    }

    // spawn particles

    /**
     * Creates a particle effect
     * 
     * @param creator the creator of the effect
     * @param type    the visual style of each particle
     * @param amount  the number of particles created
     * @param speed   the speed of each particle
     */
    public void spawnParticles(GameObject creator, Particle type, int amount, float speed) {
        particleManager.spawn(creator.getX(), creator.getY(), amount, type.getColor(), type.getSize(), speed,
                type.getLife(), type.getGravity());
    }

    /**
     * Creates a damage indicator
     * 
     * @param creator the creator of the effect
     * @param damage  the number displayed
     */
    public void spawnDamageIndicator(GameObject creator, float damage) {
        particleManager.spawn(creator.getX(), creator.getY(), 1, Color.BLACK, 10, 1.5f,
                50, 0f, (int) damage);
    }

    // Setters

    public void closeUpgradeMenu() {
        upgradeMenuActive = false;
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

    public ArrayList<Aura> getAuras() {
        return auras;
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
