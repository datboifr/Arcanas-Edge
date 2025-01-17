// GraphicsPanel.java
package main;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import objects.*;
import objects.enemies.Enemy;
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

    private final Frame frame;
    Random random = new Random();

    // Screen settings
    private static final float SCREEN_SCALE = 2f;
    private static final int WIDTH = (int) (800 * SCREEN_SCALE);
    private static final int HEIGHT = (int) (450 * SCREEN_SCALE);

    private static Rectangle fade = new Rectangle(0, 0, WIDTH, HEIGHT);
    private float fadeLevel = 1f;
    private boolean gameActive = false;

    // Timing settings
    private static final double FRAME_INTERVAL = 1_000_000_000.0 / 60; // Time per frame in nanoseconds
    private static final int WAVE_COOLDOWN_FRAMES = 300; // 5 seconds in frames

    // Game objects and components
    private final ArrayList<GameObject> gameObjects;
    private final ArrayList<Enemy> enemies;
    private final ArrayList<Projectile> projectiles;
    private final ArrayList<Pickup> pickups;

    private final Player player;
    private final GameObject platform;
    private final GameObject background;

    private final KeyHandler keyHandler = new KeyHandler();

    private final UpgradeMenu upgradeMenu;
    public final float UPGRADEMENU_MARGIN = 0.15f; // percentage
    private final ParticleManager particleManager;

    // Game state variables
    private boolean isRunning;
    public boolean upgradeMenuActive;
    public boolean waveActive;

    private int wave;
    public int waveCooldown;
    private int enemyLimit;
    private int enemyCounter;
    private int spawnTimer;

    private Thread gameThread;

    // ui objects and components
    private final UIElement MoneyIndicator;
    private final UIElement WaveIndicator;
    private final UIElement StatDisplay;

    private static final Font FONT_SMALL = new Font("Arial", Font.PLAIN, 12);
    private static final Font FONT_MEDIUM = new Font("Arial", Font.BOLD, 15);
    private static final Color COLOR_UI = new Color(1f, 1f, 1f, 0.7f);

    /**
     * Constructor to initialize the GamePanel.
     */
    public GamePanel(Frame frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        addKeyListener(keyHandler);

        this.gameObjects = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.pickups = new ArrayList<>();
        this.particleManager = new ParticleManager();

        this.player = new Player(this, WIDTH / 2, HEIGHT / 2, 35, 35);
        this.platform = new GameObject(WIDTH / 2, HEIGHT / 2, "map/Platform");
        this.background = new GameObject(WIDTH / 2, HEIGHT / 2, "map/Map");

        this.upgradeMenu = new UpgradeMenu(
                new Rectangle(0 + (int) (WIDTH * UPGRADEMENU_MARGIN), 0 + (int) (HEIGHT * UPGRADEMENU_MARGIN),
                        WIDTH - (int) (WIDTH * UPGRADEMENU_MARGIN * 2), HEIGHT - (int) (WIDTH
                                * UPGRADEMENU_MARGIN)),
                UpgradePool.getUpgrades(this, player),
                this,
                3,
                2);

        MoneyIndicator = new UIElement((int) ((WIDTH / 2) - (40)), HEIGHT - 40, 80, 30);

        WaveIndicator = new UIElement("icons/WaveIndicator", 50, WIDTH - 100, 50);

        StatDisplay = new UIElement(0 + (int) (WIDTH * UPGRADEMENU_MARGIN),
                10, WIDTH - (int) (WIDTH * UPGRADEMENU_MARGIN * 2), 50);

        waveCooldown = WAVE_COOLDOWN_FRAMES;
        startGameLoop();
    }

    /**
     * Starts the main game loop in a separate thread.
     */
    private void startGameLoop() {
        repaint();
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
            delta += (currentTime - lastTime) / FRAME_INTERVAL;
            lastTime = currentTime;

            if (delta >= 1) {
                updateGameState();
                repaint();
                delta--;
            }

            if (System.currentTimeMillis() - timer >= 1000) {
                timer += 1000;
            }
        }
    }

    /**
     * Updates the game state, including handling waves, upgrades, and objects.
     */
    private void updateGameState() {

        // game loading & fade
        if (!gameActive) {
            if (keyHandler.aActive) {
                gameActive = true;
            } else if (keyHandler.bActive) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
            return;
        } else if (fadeLevel > 0) {
            fadeLevel -= 0.1;
            return;
        }

        // debug options
        if (keyHandler.cheatActive) {
            if (keyHandler.cActive) {
                if (waveActive) {
                    enemies.forEach(Enemy::die);
                    endWave();
                }
            }
            if (keyHandler.bActive) {
                player.addMoney(10);
            }
        }

        if (upgradeMenuActive) {
            upgradeMenu.update();
            return;
        }

        player.update();
        enemies.forEach(Enemy::update);
        projectiles.forEach(Projectile::update);
        pickups.forEach(Pickup::update);
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
            if (enemies.isEmpty() && enemyCounter >= enemyLimit) {
                endWave();
            } else if (enemyCounter < enemyLimit && spawnTimer <= 0) {
                int spawncount = random.nextInt(3, 3 + wave);
                for (int i = 0; i < spawncount; i++) {
                    spawnEnemy();
                }
                enemyCounter++;
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
        upgradeMenu.fillSlots();
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

        enemies.add(new Enemy(this, x, y, Enemy.getRandomEnemyType(), player, wave));
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
                if (object instanceof Pickup)
                    pickups.remove(object);
                if (object instanceof Player) {
                    endGame();
                }
                return true;
            }
            return false;
        });
    }

    /**
     * Organizes the game object list for rendering.
     */
    private void syncObjectList() {

        // pickups always remain on the floor
        gameObjects.clear();
        gameObjects.addAll(pickups);

        ArrayList<GameObject> sortedlist = new ArrayList<>();
        sortedlist.addAll(enemies);
        sortedlist.addAll(projectiles);
        sortedlist.add(player);

        // Sort gameObjects based on their y-coordinate and height
        sortedlist.sort((o1, o2) -> {
            int o1BaseY = o1.getY() + o1.getHeight() / 2;
            int o2BaseY = o2.getY() + o2.getHeight() / 2;
            return Integer.compare(o2BaseY, o1BaseY); // Higher baseY is drawn first
        });

        gameObjects.addAll(sortedlist);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        background.draw(g2d);
        platform.draw(g2d);
        gameObjects.forEach(object -> object.draw(g2d));
        particleManager.draw(g2d);

        drawui(g2d);

        if (upgradeMenuActive) {
            upgradeMenu.draw(g2d);
        }

        // Draw fade effect
        if (fadeLevel > 0) {
            drawMainMenu(g2d);
        }
    }

    public void drawMainMenu(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, Math.min(1f, fadeLevel)));
        g.fillRect(fade.x, fade.y, fade.width, fade.height);

        int logoSize = 1000;
        g.drawImage(loadSprite("Logo"), WIDTH / 2 - (logoSize / 2), 0, logoSize, logoSize, null);

        g.setFont(FONT_MEDIUM);

        String message = "Press 'A' to start!";
        String message2 = "Press 'B' to close";

        g.setColor(COLOR_UI);
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int fontWidth = metrics.stringWidth(message);

        g.drawString(message, WIDTH / 2 - (fontWidth / 2), HEIGHT - 100);
        g.drawString(message2, WIDTH / 2 - (fontWidth / 2), HEIGHT - 70);
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

    // ui stuff

    public void drawui(Graphics2D g) {

        if (keyHandler.cheatActive) {
            g.drawString("Cheats Active", 10, 40);
        }

        // player health bar
        if (player.isAlive()) {
            g.setColor(Color.BLACK);
            g.fillRect(player.getX() - (player.getWidth() / 2), player.getY() + (player.getHeight() / 2),
                    player.getWidth(),
                    5);
            g.setColor(Color.RED);
            g.fillRect(player.getX() - (player.getWidth() / 2), player.getY() + (player.getHeight() / 2),
                    (int) (player.getWidth() * (player.getHealth() / player.getMaxHealth())), 5);
        }

        // wave countdown
        if (!waveActive && !upgradeMenuActive) {

            g.setColor(COLOR_UI);
            String timer = String.format("%.1f", waveCooldown / 60.0);
            g.setFont(new Font("Arial", Font.BOLD, 60));
            FontMetrics titleMetrics = g.getFontMetrics(g.getFont());

            int timerWidth = titleMetrics.stringWidth(timer);
            int timerHeight = titleMetrics.getHeight();

            int timerX = (WIDTH / 2) - (timerWidth / 2);
            int timerY = (int) (HEIGHT / 1.2);

            g.drawString(String.format(timer), timerX, timerY);

            String subtext = "WAVE STARTS IN";
            g.setFont(new Font("Arial", Font.BOLD, 15));
            FontMetrics subtextMetrics = g.getFontMetrics(g.getFont());

            int subtextWidth = subtextMetrics.stringWidth(subtext);

            g.drawString(subtext, (WIDTH / 2) - (subtextWidth / 2), timerY - timerHeight);
        }

        // wave indicator
        WaveIndicator.draw(g);
        g.setFont(FONT_MEDIUM);
        g.setColor(Color.WHITE);
        g.drawString(String.format("%d", wave),
                WaveIndicator.getFrame().x + (WaveIndicator.getFrame().width / 2),
                WaveIndicator.getFrame().y + 15);

        drawStatDisplay(g);
        drawMoneyIndicator(g);

    }

    // used for the main menu
    public BufferedImage loadSprite(String spritePath) {
        try {
            return ImageIO
                    .read(getClass()
                            .getResourceAsStream("/res/" + spritePath + ".png"));
        } catch (IOException e) {
            return null;
        }
    }

    public void drawMoneyIndicator(Graphics2D g) {
        // Draw the cost at the bottom of the frame
        MoneyIndicator.draw(g);

        Rectangle frame = MoneyIndicator.getFrame();
        g.setFont(new Font("Arial", Font.BOLD, frame.height));
        FontMetrics costMetrics = g.getFontMetrics(g.getFont());
        String costText = String.format("%d", player.getMoney());

        int costWidth = costMetrics.stringWidth(costText);
        int costX = frame.x + (frame.width / 2) - (costWidth / 2);
        int costY = (int) (frame.y + frame.height / 1.2f);

        g.drawString(costText, costX, costY);
    }

    public void drawStatDisplay(Graphics2D g) {

        StatDisplay.draw(g);

        // Set the color and font for the stats
        final Color Stats = COLOR_UI;
        g.setColor(Stats);
        g.setFont(FONT_SMALL);

        // frame dimensions
        Rectangle statFrame = StatDisplay.getFrame();
        int frameWidth = statFrame.width;
        int frameHeight = statFrame.height;
        int frameX = statFrame.x;
        int frameY = statFrame.y;

        // the stats and their values
        String[] stats = {
                String.format("Max Health: %.1f", player.getMaxHealth()),
                String.format("Recovery: %.1f", player.getRecovery() * 10),
                String.format("Speed: %.1f", player.getSpeed()),

                String.format("Magic Damage: %.1f", player.getProjectileDamage()),
                String.format("Magic Speed: %.1f", player.getProjectileSpeed()),
                String.format("Magic Size: %.1f", player.getProjectileSize()),
                String.format("Magic Cooldown: %.1f", player.getAbilityCooldown()),

                String.format("Contact Damage: %.1f", player.getContactDamage()),
        };

        // Calculate rows and columns
        int rows = 2;
        int cols = (int) Math.ceil(stats.length / (double) rows);

        // Calculate cell dimensions
        int cellWidth = frameWidth / cols;
        int cellHeight = frameHeight / rows;

        // Adjust font size to fit within cells
        FontMetrics metrics = g.getFontMetrics();
        int fontHeight = metrics.getHeight();
        int fontAdjustment = Math.min(cellHeight / 2, fontHeight); // Ensure font fits within cell
        g.setFont(g.getFont().deriveFont((float) fontAdjustment));

        // Draw each stat
        for (int i = 0; i < stats.length; i++) {
            int row = i / cols;
            int col = i % cols;
            int x = frameX + col * cellWidth + (cellWidth / 2);
            int y = (frameY + 2) + row * cellHeight + (cellHeight / 2);
            int textWidth = metrics.stringWidth(stats[i]);
            int textHeight = metrics.getAscent();
            g.drawString(stats[i], x - textWidth / 2, y + textHeight / 4);
        }
    }

    public void endGame() {
        endWave();
        gameActive = false;
        fadeLevel = 1;
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

    public ArrayList<Pickup> getPickups() {
        return pickups;
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
