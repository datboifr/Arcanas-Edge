package objects.projectiles;

import objects.GameObject;

/**
 * Represents a projectile in the game, which is a type of object that can be
 * fired or launched by a player or other entities.
 */
public class Projectile extends GameObject {

    private ProjectileType type;
    private int cooldown;
    public float vx, vy;
    public float angle, radius;
    public float angularSpeed;
    public int centerX, centerY;

    private final GameObject creator;

    /**
     * Constructor to initialize a projectile.
     * 
     * @param creator        The object that created the projectile (e.g., player,
     *                       enemy).
     * @param direction      The direction of travel for the projectile (in
     *                       degrees).
     * @param projectileType The type of the projectile (e.g., Lightning, Fire).
     */
    public Projectile(GameObject creator, float direction, ProjectileType projectileType) {
        super(creator.getX(), creator.getY(),
                (int) ((projectileType.getWidth() * projectileType.getSize()) * creator.getProjectileSize()),
                (int) ((projectileType.getHeight() * projectileType.getSize()) * creator.getProjectileSize()));

        // Initialize projectile properties based on the creator and type
        this.creator = creator;
        this.type = projectileType;
        this.panel = creator.getGamePanel();

        // Calculate the damage and speed based on the creator's attributes
        this.contactDamage = type.getContactDamage() * creator.getProjectileDamage();
        this.speed = type.getSpeed() * creator.getProjectileSpeed();

        this.cooldown = (int) type.getCooldown();

        // Set direction and animations
        this.direction = direction;
        this.animations = type.getAnimations();
        this.rotates = true;

        // Set the default animation for the projectile
        this.setAnimation("default", true);
        type.created(this);
    }

    /**
     * Updates the state of the projectile. This includes updating its position,
     * handling cooldowns, and updating its animation if it's animated.
     */
    @Override
    public void update() {
        type.update(this); // Call the update method for the specific projectile type

        // Handle cooldown and trigger the 'cooldownFinished' method
        if (cooldown != -1) {
            this.cooldown--;
            if (cooldown == 0) {
                type.cooldownFinished(this); // Trigger the cooldown finished action
                this.cooldown = (int) type.getCooldown(); // Reset the cooldown
            }
        }

        // Update the animation if the projectile is animated
        if (type.isAnimated()) {
            updateAnimation();
        }
    }

    /**
     * Called when the projectile hits an object. If the projectile can't pierce, it
     * will be destroyed.
     */
    public void hit() {
        if (!type.canPierce) {
            die();
        }
        type.hit(this, creator);
    }

    /**
     * Checks if the projectile is within the screen boundaries.
     */
    public boolean isOffscreen() {
        return this.x < 0 || this.x > panel.getWidth() || this.y < 0 || this.y > panel.getHeight();
    }

    // setters

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // getters

    public int getCooldown() {
        return this.cooldown;
    }

    public GameObject getCreator() {
        return this.creator;
    }
}
