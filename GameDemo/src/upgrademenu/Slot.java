package upgrademenu;

import java.awt.*;

public class Slot {

    private Upgrade upgrade;
    private Rectangle frame;
    boolean isSelected;

    private final int TITLE_SIZE = 20;
    private final int BORDER = 3; // pixels

    public Slot(Rectangle frame, Upgrade upgrade) {
        this.frame = frame;
        this.upgrade = upgrade;
        this.isSelected = false;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fill(frame);

        // title initalization
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, TITLE_SIZE));
        FontMetrics metrics = g.getFontMetrics(g.getFont());

        // Calculate the text width and height
        int textWidth = metrics.stringWidth(upgrade.getTitle());
        int textHeight = metrics.getHeight();

        int titleX = (frame.x + (frame.width / 2) - (textWidth / 2));
        int titleY = frame.y + textHeight + 10;
        g.drawString(upgrade.getTitle(), titleX, titleY);

        Image sprite = upgrade.getSprite(); // Assuming Upgrade has a method to get the sprite
        if (sprite != null) {
            // Desired sprite size (modify as needed)
            int spriteWidth = 150; // Example: half the frame width
            int spriteHeight = 150; // Example: one-third of the frame height

            // Calculate the position to center the resized sprite
            int spriteX = frame.x + (frame.width / 2) - (spriteWidth / 2);
            int spriteY = titleY + 10; // Place it below the title with padding

            g.drawImage(sprite, spriteX, spriteY, spriteWidth, spriteHeight, null);
        }

        if (isSelected) {
            g.setStroke(new BasicStroke(BORDER));
            g.draw(frame);
        }
    }

    public void purchase() {
        upgrade.buy();
    }
}
