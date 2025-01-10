package upgrademenu;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Slot {

    private UpgradeMenu menu;
    private Rectangle frame;
    private Upgrade upgrade;
    boolean isSelected;
    boolean affordable;

    private BufferedImage costIcon;

    public Slot(UpgradeMenu menu, Rectangle frame, Upgrade upgrade) {

        this.menu = menu;
        this.frame = frame;
        this.upgrade = upgrade;
        this.isSelected = false;

        // Load the cost icon
        try {
            costIcon = ImageIO.read(getClass().getResourceAsStream("/res/icons/Levels.png"));
        } catch (IOException e) {
            costIcon = null;
            System.out.println("Couldn't fetch cost icon sprite.");
        }
    }

    public void draw(Graphics2D g) {

        // Draw the frame
        g.setColor(Color.BLACK);

        // Title initialization
        g.setColor(menu.TITLE_COLOR);
        g.setFont(menu.TITLE_TEXT);
        FontMetrics metrics = g.getFontMetrics(g.getFont());

        // Calculate the text width and height
        int textWidth = metrics.stringWidth(upgrade.getTitle());
        int textHeight = metrics.getHeight();

        int titleX = (frame.x + (frame.width / 2) - (textWidth / 2));
        int titleY = frame.y + textHeight + 10;
        g.drawString(upgrade.getTitle(), titleX, titleY);

        // Draw the sprite
        Image sprite = upgrade.getSprite();
        if (sprite != null) {
            int spriteWidth = 150; // Example size
            int spriteHeight = 150; // Example size

            // Center the sprite below the title
            int spriteX = frame.x + (frame.width / 2) - (spriteWidth / 2);
            int spriteY = titleY + 10;

            g.drawImage(sprite, spriteX, spriteY, spriteWidth, spriteHeight, null);
        }

        // Draw border if selected
        if (isSelected) {
            g.setStroke(new BasicStroke(menu.BORDER));
            g.setColor(Color.WHITE);
            g.draw(frame);
        }
    }

    public void purchase() {
        upgrade.buy();
    }

    // getters

    public Upgrade getUpgrade() {
        return upgrade;
    }
}
