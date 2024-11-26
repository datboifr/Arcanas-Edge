package objects;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GameObject { // Renamed for clarity
    int x, y, width, height;
    BufferedImage sprite;
    public String prompt;

    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Draws the object on screen
     */
    public void draw(Graphics2D g2) {
        // Draw the sprite if it's not null
        if (sprite != null) {
            g2.drawImage(sprite, x - (width / 2), y - (height / 2), width, height, null);
        } else {
            // Draw a placeholder rectangle if no sprite is set
            g2.setColor(Color.RED);
            g2.fillRect(x - (width / 2), y - (height / 2), width, height);
        }

        // Set the color for the text (white)
        g2.setColor(Color.WHITE);
        
        if (prompt != null) {
            // Get FontMetrics for the current font to calculate text width and height
            FontMetrics metrics = g2.getFontMetrics(g2.getFont());

            // Calculate the text width and height
            int textWidth = metrics.stringWidth(prompt);
            int textHeight = metrics.getHeight();

            // Calculate the x position to center the text
            int textX = x - textWidth / 2;

            // Calculate the y position to place the text above the object
            int textY = y - (height / 2) - textHeight / 2;

            // Draw the centered text above the object
            g2.drawString(prompt, textX, textY);
        }
    }

    public void update() {
    }

    /**
     * Returns the distance between two objects
     * 
     * @param other The object to be measured
     */
    public boolean touching(GameObject other) {
        Rectangle thisRect = new Rectangle(x, y, width, height);
        Rectangle otherRect = new Rectangle(other.x, other.y, other.width, other.height);
        return thisRect.intersects(otherRect);
    }

    /**
     * Returns the distance between two objects
     * 
     * @param other The object to be measured
     */
    public double distanceTo(GameObject other) {
        return Math.sqrt(Math.pow(other.x - x, 2) + Math.pow(other.y - y, 2));
    }
}
