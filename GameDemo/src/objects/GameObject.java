package objects;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GameObject { 

    String direction;
	protected int directionLiteral;

    int x, y, width, height;
    protected String spritePath;
    BufferedImage sprite;
    public String prompt;

    protected boolean isDead = false;
    protected boolean isAttacking = false;

    // stats
    protected float health;
    protected float speed;
    protected float contactDamage;
    protected float projectileDamage;
	protected float projectileSpeed;
	protected float projectileSize;

    protected int frameCounter = 0; // Counts frames for animation timing
    protected final int SPRITES_PER_FRAME = 5;

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
        this.sprite = null;
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

    // setters

    public void setSize(float decrease) {
        this.width *= decrease;
        this.height *= decrease;
    }
    
    public void setContactDamage(float contactDamage) {
        this.contactDamage = contactDamage;
    }
}
