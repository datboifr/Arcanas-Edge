package objects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Object { // Renamed for clarity
    int x, y, width, height;
    BufferedImage sprite;
    public String prompt;

    public Object(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Draws the object on screen
     */
    public void draw(Graphics2D g2) {
        if (sprite != null) {
            g2.drawImage(sprite, x - (width / 2), y - (height / 2), width, height, null);
        } else {
            // Draw a placeholder rectangle if no sprite is set
            g2.setColor(Color.RED);
            g2.fillRect(x - (width / 2), y - (height / 2), width, height);
        }

        g2.setColor(Color.WHITE);
        if (prompt != null) {
            g2.drawString(prompt, x - width, y - (height / 2));
        }
    }

    public void update() {
    }

    /**
     * Returns the distance between two objects
     * 
     * @param other The object to be measured
     */
    public boolean touching(Object other) {
        Rectangle thisRect = new Rectangle(x, y, width, height);
        Rectangle otherRect = new Rectangle(other.x, other.y, other.width, other.height);
        return thisRect.intersects(otherRect);
    }

    /**
     * Returns the distance between two objects
     * 
     * @param other The object to be measured
     */
    public double distanceTo(Object other) {
        return Math.sqrt(Math.pow(other.x - x, 2) + Math.pow(other.y - y, 2));
    }
}
