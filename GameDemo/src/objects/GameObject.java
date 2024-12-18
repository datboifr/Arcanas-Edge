package objects;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class GameObject {

    // direction
    String direction;
    protected float directionLiteral;

    // values
    protected int x, y, width, height;
    protected boolean isDead = false;
    protected boolean isAttacking = false;

    // traits
    protected float health;
    protected float speed;
    protected float contactDamage;

    protected float projectileDamage;
    protected float projectileSpeed;
    protected float projectileSize;

    // sprites & animation

    protected final int FRAMES_PER_SPRITE = 5;

    protected String spritePath;
    BufferedImage sprite;
    public String prompt;

    protected HashMap<String, BufferedImage[]> animations = new HashMap<>();
    protected String currentAnimation;
    protected boolean animationLooping;
    protected int currentFrame = 1;
    protected int animationCounter = FRAMES_PER_SPRITE; // Counts frames for animation timing

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

    public GameObject(int x, int y, int width, int height, String spritePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        setSprite(spritePath);
    }

    /**
     * Draws the object on screen
     */
    public void draw(Graphics2D g) {
        // Draw the sprite if it's not null
        if (sprite != null) {
            g.drawImage(this.sprite, this.x - (this.width / 2), this.y - (this.height / 2), this.width, this.height,
                    null);
        } else {
            // Draw a placeholder rectangle if no sprite is set
            g.setColor(Color.WHITE);
            g.fillRect(this.x - (this.width / 2), this.y - (this.height / 2), this.width, this.height);
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
        this.currentFrame = 1;
        this.animationLooping = animationLooping;
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
                        this.currentFrame = this.animations.get(this.currentAnimation).length - 1;
                    }
                }
                this.sprite = this.animations.get(currentAnimation)[currentFrame];
            }
        }
    }

    /**
     * Returns the distance between two objects
     * 
     * @param other The object to be measured
     */
    public boolean isTouching(GameObject other) {
        Rectangle thisRect = new Rectangle(this.x, this.y, this.width, this.height);
        Rectangle otherRect = new Rectangle(other.x, other.y, other.width, other.height);
        return thisRect.intersects(otherRect);
    }

    /**
     * Returns the distance between two objects
     * 
     * @param other The object to be measured
     */
    public double distanceTo(GameObject other) {
        return Math.sqrt(Math.pow(other.x - this.x, 2) + Math.pow(other.y - this.y, 2));
    }

    // setters

    public void setSprite(String spritePath) {
        System.out.println("/res/" + spritePath + ".png");
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

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public boolean isDead() {
        return isDead;
    }

    public float getHealth() {
        return this.health;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getContactDamage() {
        return this.contactDamage;
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

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public float getdirectionLiteral() {
        return this.directionLiteral;
    }

    // setters

    public void setState(boolean state) {
        this.isDead = state;
    }

    public void setSize(float decrease) {
        this.width *= decrease;
        this.height *= decrease;
    }

    public void setContactDamage(float contactDamage) {
        this.contactDamage = contactDamage;
    }
}
