package objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

import main.GamePanel;
import objects.projectiles.Projectile;

public class Enemy extends GameObject {

    private GameObject target;

    // required to fix movement issues. better than using casts
    private double realX, realY;

    // Static pool of enemy types
    private static final ArrayList<EnemyType> ENEMY_POOL = new ArrayList<>();
    private static final Random RANDOM = new Random();

    // particle stuff
    private static final int PARTICLE_COUNT_ON_DEATH = 5;
    private static final float COLLISION_REPULSION_FACTOR = 0.5f;

    // Static initializer to populate the enemy pool
    static {
        // theres only one kind :(
        ENEMY_POOL.add(new EnemyType(1, 15, 1.5f, 5, "slime/SlimeWalk1"));
        ENEMY_POOL.add(new EnemyType(1.5f, 20, 1.5f, 5, "slime/SlimeWalk1"));
        ENEMY_POOL.add(new EnemyType(2f, 30, 1.5f, 10, "slime/SlimeWalk1"));
        ENEMY_POOL.add(new EnemyType(1.2f, 80, 1.2f, 30, "Dslime/dragonslime"));
    }

    // Constructor for individual enemies
    public Enemy(GamePanel gp, int x, int y, EnemyType type, GameObject target, float healthMultiplier) {
        super(x, y, 0, 0);
        this.realX = x;
        this.realY = y;

        this.target = target;
        this.panel = gp;

        this.maxHealth = (float) (type.getHealth() * (0.2 * healthMultiplier));
        this.health = maxHealth;
        this.contactDamage = type.getContactDamage();
        this.speed = type.getSpeed();

        setSprite(type.getSpritePath());
        this.width = (int) (sprite.getWidth() * type.getSize());
        this.height = (int) (sprite.getHeight() * type.getSize());
    }

    // Main update loop
    @Override
    public void update() {
        if (health <= 0) {
            die();
            panel.spawnParticles(this, PARTICLE_DAMAGE, PARTICLE_COUNT_ON_DEATH, 1);
            panel.getPickups().add(new Pickup(x, y, Pickup.getRandomPickup()));
        }

        if (iFrames > 0) {
            iFrames--;
        }

        for (Projectile projectile : panel.getProjectiles()) {
            if (touching(projectile) && iFrames <= 0) {
                projectile.hit();
                this.health -= projectile.getContactDamage();
                this.iFrames = I_FRAMES;
                panel.spawnDamageIndicator(this, projectile.getContactDamage());
            }
        }

        moveTowardsTarget();
    }

    /**
     * Simple NPC movement
     */
    private void moveTowardsTarget() {
        float angleToTarget = (directionTo(target) + 360) % 360;

        double deltaX = Math.cos(Math.toRadians(angleToTarget)) * speed;
        double deltaY = Math.sin(Math.toRadians(angleToTarget)) * speed;

        realX += deltaX;
        realY += deltaY;

        // Resolve collisions with other enemies
        resolveCollisionsWithEnemies();

        // Update the x, y positions after collision adjustments
        this.x = (int) realX;
        this.y = (int) realY;

        setDirection(angleToTarget);
    }

    /**
     * Ensures enemys dont overlap eachother
     */
    private void resolveCollisionsWithEnemies() {
        for (Enemy other : panel.getEnemies()) {
            if (other == this || !touching(other)) {
                continue;
            }

            // Correct the angle calculation for repulsion
            double angle = Math.atan2(other.realY - realY, other.realX - realX);

            // Repel enemies based on their speed and the collision repulsion factor
            realX -= Math.cos(angle) * speed * COLLISION_REPULSION_FACTOR;
            realY -= Math.sin(angle) * speed * COLLISION_REPULSION_FACTOR;
        }
    }

    public void setSprite(String spritePath) {
        try {
            this.sprite = ImageIO
                    .read(getClass()
                            .getResourceAsStream("/res/enemies/" + spritePath + ".png"));
        } catch (IOException e) {
            this.sprite = null;
            System.out.print("Couldn't Fetch Sprite");
        }
    }

    // Static methods for the enemy pool
    public static EnemyType getRandomEnemyType() {
        return ENEMY_POOL.get(RANDOM.nextInt(ENEMY_POOL.size()));
    }

    public static ArrayList<EnemyType> getEnemyPool() {
        return new ArrayList<>(ENEMY_POOL); // Return a copy to prevent modification
    }

    // Nested class for EnemyType
    public static class EnemyType {
        private final float size;
        private final float health;
        private final float speed;
        private final float contactDamage;
        private final String spritePath;

        public EnemyType(float size, float health, float speed, float contactDamage, String spritePath) {
            this.size = size;
            this.health = health;
            this.speed = speed;
            this.contactDamage = contactDamage;
            this.spritePath = spritePath;
        }

        public float getHealth() {
            return this.health;
        }

        public float getSpeed() {
            return speed;
        }

        public float getContactDamage() {
            return this.contactDamage;
        }

        public String getSpritePath() {
            return this.spritePath;
        }

        public float getSize() {
            return this.size;
        }
    }
}
