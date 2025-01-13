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

        // Draw the frame background
        g.setColor(Color.BLACK);
        g.fill(frame);

        // Title setup
        g.setColor(menu.TITLE_COLOR);
        g.setFont(menu.TITLE_TEXT);
        FontMetrics titleMetrics = g.getFontMetrics(g.getFont());
        int titleWidth = titleMetrics.stringWidth(upgrade.getTitle());
        int titleHeight = titleMetrics.getHeight();

        // Center and draw the title
        int titleX = frame.x + (frame.width / 2) - (titleWidth / 2);
        int titleY = frame.y + titleHeight + 10;
        g.drawString(upgrade.getTitle(), titleX, titleY);

        // Description setup
        g.setColor(Color.GRAY);
        g.setFont(menu.DESCRIPTION_TEXT); // Assume a smaller font is defined in the menu
        FontMetrics descMetrics = g.getFontMetrics(g.getFont());
        String description = upgrade.getDescription();
        int descWidth = descMetrics.stringWidth(description);
        int descHeight = descMetrics.getHeight();

        // Center and draw the description below the title
        int descX = frame.x + (frame.width / 2) - (descWidth / 2);
        int descY = titleY + descHeight + 5;
        g.drawString(description, descX, descY);

        // Draw the sprite (icon)
        Image sprite = upgrade.getSprite();
        if (sprite != null) {
            int spriteWidth = 150; // Example size
            int spriteHeight = 150; // Example size
            int spriteX = frame.x + (frame.width / 2) - (spriteWidth / 2);
            int spriteY = descY + 10;

            g.drawImage(sprite, spriteX, spriteY, spriteWidth, spriteHeight, null);
        }

        // Draw the cost at the bottom of the frame
        g.setColor(Color.WHITE);
        g.setFont(menu.TITLE_TEXT); // Assume a font for the cost is defined in the menu
        FontMetrics costMetrics = g.getFontMetrics(g.getFont());
        String costText = String.format("%d", upgrade.getCost());
        int costWidth = costMetrics.stringWidth(costText);
        int costX = frame.x + (frame.width / 2) - (costWidth / 2);
        int costY = frame.y + frame.height - 10;

        // Optional: Draw cost icon if available
        if (costIcon != null) {
            int iconSize = 20; // Example icon size
            int iconX = costX - iconSize - 5;
            int iconY = costY - iconSize + 5;
            g.drawImage(costIcon, iconX, iconY, iconSize, iconSize, null);
        }

        g.drawString(costText, costX, costY);

        // Draw a border if selected
        if (isSelected) {
            g.setStroke(new BasicStroke(menu.BORDER));
            g.setColor(Color.WHITE);
            g.draw(frame);
        }
    }

    public void purchase() {
        upgrade.buy();
    }

    // Getters
    public Upgrade getUpgrade() {
        return upgrade;
    }
}
