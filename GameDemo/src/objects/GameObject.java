package objects;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

import main.GamePanel;
import objects.particles.Particle;

public class GameObject {

    protected GamePanel panel;

    // direction
    protected float direction;
    protected boolean rotates = false; // purely visual

    // values
    protected int x, y, width, height;
    protected boolean alive = true;
    protected boolean isAttacking = false;
    protected final int I_FRAMES = 15;
    protected int iFrames = I_FRAMES;
    protected GameObject target;

    // traits
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
    BufferedImage sprite = null;

    protected HashMap<String, BufferedImage[]> animations = new HashMap<>();
    protected String currentAnimation;
    protected boolean animationLooping;
    protected int currentFrame = 1;
    protected int animationCounter = FRAMES_PER_SPRITE; // Counts frames for animation timing

    // miscellaneous
    protected Color deathColor = new Color(160, 0, 0); // default red
    protected final Particle PARTICLE_DAMAGE = new Particle(30, Color.RED, 8, 0.1f);
    protected String prompt;

    /**
     * Constructs a new object.
     *
     * @param x      the initial x-coordinate
     * @param y      the initial y-coordinate
     * @param width  the width of the object
     * @param height the height of the object
     */
    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public GameObject(int x, int y, String spritePath) {
        this.x = x;
        this.y = y;
        setSprite(spritePath);
        this.width = this.sprite.getWidth();
        this.height = this.sprite.getHeight();
    }

    /**
     * Draws the object on screen
     */
    public void draw(Graphics2D g) {

        // Draw the shadow if enabled
        if (hasShadow) {
            g.setColor(Color.BLACK);
            // Align shadow with the bottom of the sprite or placeholder rectangle
            int shadowWidth = width / 2;
            int shadowHeight = 10;
            int shadowX = x - shadowWidth / 2;
            int shadowY = y + (height / 2) - shadowHeight / 2;

            g.fillOval(shadowX, shadowY, shadowWidth, shadowHeight);
        }

        // Draw the sprite if it's not null
        if (this.sprite != null) {
            // Save the original transform
            AffineTransform originalTransform = g.getTransform();
            if (rotates) {
                g.rotate(Math.toRadians(direction), x, y);
            }

            // Draw the rotated sprite
            g.drawImage(sprite, x - width / 2, y - height / 2, width, height, null);
            g.setTransform(originalTransform);
        } else {
            // Draw a placeholder if no sprite is set
            g.setColor(Color.WHITE);
            g.fillRect(x - width / 2, y - height / 2, width, height);
        }

        // Set the color for the text (white)
        g.setColor(Color.WHITE);

        if (prompt != null) {
            // Get FontMetrics for the current font to calculate text width and height
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            int textWidth = metrics.stringWidth(prompt);
            int textHeight = metrics.getHeight();

            // Draw the centered text above the object
            int textX = this.x - textWidth / 2;
            int textY = this.y - (height / 2) - textHeight / 2;

            g.drawString(this.prompt, textX, textY);
        }
    }

    public void update() {
        // does nothing by default
    }

    public void hit(GameObject other) {
        // does nothing by default
    }

    public void loadAnimation(String name, String path, int animationLength) {
        BufferedImage[] loadedFrames = new BufferedImage[animationLength];
        for (int i = 0; i < animationLength; i++) {
            try {
                loadedFrames[i] = ImageIO.read(getClass().getResourceAsStream("/res/" + path + (i + 1) + ".png"));
            } catch (IOException e) {
                System.out.println("Error loading frame: " + path + (i + 1) + ".png");
            }
        }
        this.animations.put(name, loadedFrames);
    }

    public void setAnimation(String name, boolean animationLooping) {
        this.currentAnimation = name;
        this.currentFrame = 0; // Start at the first frame (0-indexed)
        this.animationLooping = animationLooping;
        this.sprite = this.animations.get(this.currentAnimation)[this.currentFrame];
    }

    // Animation Update
    public void updateAnimation() {
        if (this.animations.containsKey(this.currentAnimation)) {
            this.animationCounter--;
            if (this.animationCounter == 0) {
                this.animationCounter = FRAMES_PER_SPRITE;
                this.currentFrame++;
                if (this.currentFrame >= this.animations.get(this.currentAnimation).length) {
                    if (this.animationLooping) {
                        this.currentFrame = 0;
                    } else {
                        this.currentFrame = 0;
                        die();
                    }
                }
                this.sprite = this.animations.get(this.currentAnimation)[this.currentFrame];
            }
        }
    }

    /**
     * Returns the distance between two objects
     * 
     * @param other The object to be measured
     */
    public boolean touching(GameObject other) {
        Rectangle thisRect = new Rectangle(x, y, width, height);
        Rectangle otherRect = new Rectangle(other.getX(), other.getY(), other.getWidth(),
                other.getHeight());
        return thisRect.intersects(otherRect);
    }

    /**
     * Returns the distance between two objects
     * 
     * @param other The object to be measured
     */
    public double distanceTo(GameObject other) {
        return Math.sqrt(Math.pow(other.getX() - x, 2) + Math.pow(other.getY() - y, 2));
    }

    /**
     * Returns the direction of another object from this position
     * 
     * @param other The object to be measured
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

    // setters

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

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void collectAura(Aura aura) {
        // does nothing by default
    }
}
