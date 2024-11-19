package objects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Object { // Renamed for clarity
    int x, y, width, height;
    BufferedImage sprite;

    public Object(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.sprite = null;
    }

    // Draw method with optional dynamic scaling
    public void draw(Graphics2D g2) {
        if (sprite != null) {
            g2.drawImage(sprite, x, y, null);
        } else {
            // Draw a placeholder rectangle if no sprite is set
            System.err.println("Sprite is null");
            g2.setColor(Color.RED);
            g2.fillRect(x, y, width, height);
        }
    }

    // Check if this object intersects with another object
    public boolean touching(Object other) {
        Rectangle thisRect = new Rectangle(x, y, width, height);
        Rectangle otherRect = new Rectangle(other.x, other.y, other.width, other.height);
        return thisRect.intersects(otherRect);
    }
}
