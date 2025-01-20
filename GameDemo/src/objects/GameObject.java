package objects;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import main.GamePanel;
import objects.particles.Particle;

/**
 * Represents an object in the game world with attributes like position, size,
 * health, and animation.
 * GameObjects can interact with other objects, be drawn on the screen, and
 * perform actions like taking damage.
 */
public class GameObject {

    protected GamePanel panel;

    // direction
    protected float direction;
    protected boolean rotates = false; // purely visual

    // values
    protected int x, y;
    protected int width, height;
    protected boolean alive = true;
    protected boolean isAttacking = false;
    protected final int I_FRAMES = 15;
    protected int iFrames = I_FRAMES;
    protected GameObject target;

    // attributes related to health, damage, speed, and cooldown
    protected float maxHealth;
    protected float health;
    protected float recovery;
    protected float speed;
    protected float contactDamage;
    protected float abilityCooldown;
    protected float projectileDamage;
    protected float projectileSpeed;
    protected float projectileSize;
    protected float projectileBonus;

    // sprites & animation
    protected boolean hasShadow = false;
    protected final int FRAMES_PER_SPRITE = 5;

    protected String spritePath;
    protected BufferedImage sprite = null;

    // animations
    protected ArrayList<Animation> animations = new ArrayList<>();
    protected Animation currentAnimation;
    protected boolean animationLooping;
    protected int currentFrame = 1;
    protected int animationCounter = 0; // Counts frames for animation timing

    // miscellaneous
    protected Color deathColor = new Color(160, 0, 0); // default red
    protected final Particle PARTICLE_DAMAGE = new Particle(30, Color.RED, 8, 0.1f);

    /**
     * Constructs a new blank GameObject
     *
     * @param x      the initial x-coordinate of the object
     * @param y      the initial y-coordinate of the object
     * @param width  the width of the object
     * @param height the height of the object
     */
    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Constructs a new GameObject with a sprite specified by path.
     *
     * @param x          the initial x-coordinate of the object
     * @param y          the initial y-coordinate of the object
     * @param spritePath the path to the sprite image
     */
    public GameObject(int x, int y, String spritePath) {
        this.x = x;
        this.y = y;
        setSprite(spritePath);
        this.width = this.sprite.getWidth();
        this.height = this.sprite.getHeight();
    }

    /**
     * Draws the object on screen, including its sprite and shadow (if applicable).
     *
     * @param g the Graphics2D object to draw with
     */
    public void draw(Graphics2D g) {
        // Draw sprite
        if (this.sprite != null) {
            AffineTransform originalTransform = g.getTransform();
            if (rotates) {
                g.rotate(Math.toRadians(direction), x, y);
            }
            g.drawImage(sprite, x - width / 2, y - height / 2, width, height, null);
            g.setTransform(originalTransform);
        } else {
            // Draw a placeholder rectangle if no sprite is set
            g.setColor(Color.WHITE);
            g.fillRect(x - width / 2, y - height / 2, width, height);
        }
    }

    /**
     * Draws the objects shadow (if enabled)
     *
     * @param g the Graphics2D object to draw with
     */
    public void drawShadow(Graphics2D g) {
        if (hasShadow) {
            g.setColor(Color.BLACK);
            int shadowWidth = width / 2;
            int shadowHeight = 10;
            int shadowX = (int) (x - shadowWidth / 2);
            int shadowY = (int) (y + (height / 2) - shadowHeight / 2);
            g.fillOval(shadowX, shadowY, shadowWidth, shadowHeight);
        }
    }

    /**
     * Updates the state of the object
     */
    public void update() {
        // does nothing by default
    }

    /**
     * Handles a hit on the object by another GameObject
     *
     * @param other the object causing the hit
     */
    public void hit(GameObject other) {
        // does nothing by default
    }

    /**
     * Sets the current animation of the object.
     *
     * @param name             the name of the animation
     * @param animationLooping whether the animation should loop
     */
    public void setAnimation(String name, boolean animationLooping) {
        this.currentFrame = 0; // Start at the first frame (0-indexed)
        this.animationLooping = animationLooping;
        for (Animation anim : animations) {
            if (anim.getName().equals(name)) {
                currentAnimation = anim;
            }
        }
        updateAnimation();
    }

    /**
     * Updates the current animation frame.
     */
    public void updateAnimation() {
        animationCounter--;
        if (animationCounter <= 0) {
            if (currentAnimation != null) {
                currentFrame++;
                if (currentFrame >= currentAnimation.getLength()) {
                    if (animationLooping) {
                        currentFrame = 0;
                    } else {
                        die();
                        return;
                    }
                }
                sprite = currentAnimation.getFrame(currentFrame);
            } else {
                sprite = null;
            }
            animationCounter = FRAMES_PER_SPRITE;
        }
    }

    /**
     * Returns whether this object is touching another object.
     *
     * @param other the object to check collision with
     * @return true if the objects are touching, false otherwise
     */
    public boolean touching(GameObject other) {
        Rectangle thisRect = new Rectangle(x, y, width, height);
        Rectangle otherRect = new Rectangle(other.getX(), other.getY(), other.getWidth(),
                other.getHeight());
        return thisRect.intersects(otherRect);
    }

    /**
     * Returns the distance between this object and another object.
     *
     * @param other the object to measure distance to
     * @return the distance between the two objects
     */
    public double distanceTo(GameObject other) {
        return Math.sqrt(Math.pow(other.getX() - x, 2) + Math.pow(other.getY() - y, 2));
    }

    /**
     * Returns the direction from this object to another object.
     *
     * @param other the object to measure direction to
     * @return the direction angle in degrees (0-360)
     */
    public float directionTo(GameObject other) {
        float deltaX = other.getX() - this.x;
        float deltaY = other.getY() - this.y;

        // Calculate the angle using atan2 (returns angle in radians)
        float angle = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));

        // Normalize the angle to be between 0 and 360 degrees
        if (angle < 0) {
            angle += 360;
        }
        return angle;
    }

    // setters

    /**
     * Sets the sprite image for the object.
     *
     * @param spritePath the path to the sprite image
     */
    public void setSprite(String spritePath) {
        try {
            this.sprite = ImageIO
                    .read(getClass()
                            .getResourceAsStream("/res/" + spritePath + ".png"));
        } catch (IOException e) {
            this.sprite = null;
            System.out.print("Couldn't Fetch Sprite");
        }
    }

    public void doDamage(float damage) {
        this.health -= damage;
    }

    public void setSize(float decrease) {
        this.width *= decrease;
        this.height *= decrease;
    }

    public void setContactDamage(float contactDamage) {
        this.contactDamage = contactDamage;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public void die() {
        this.alive = false;
    }

    public void collectPickup(Pickup aura) {
        // does nothing by default
    }

    // getters

    public GamePanel getGamePanel() {
        return this.panel;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean isAlive() {
        return alive;
    }

    public Color getDeathColor() {
        return this.deathColor;
    }

    public float getMaxHealth() {
        return this.maxHealth;
    }

    public float getHealth() {
        return this.health;
    }

    public float getRecovery() {
        return recovery;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getContactDamage() {
        return this.contactDamage;
    }

    public float getAbilityCooldown() {
        return this.abilityCooldown;
    }

    public float getProjectileDamage() {
        return this.projectileDamage;
    }

    public float getProjectileSpeed() {
        return this.projectileSpeed;
    }

    public float getProjectileSize() {
        return this.projectileSize;
    }

    public float getProjectileBonus() {
        return this.projectileBonus;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public float getDirection() {
        return this.direction;
    }

    public GameObject getTarget() {
        return this.target;
    }

    public int getIFrames() {
        return this.iFrames;
    }
}
