package upgrademenu;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Slot {

    private UpgradeMenu menu;
    private Rectangle frame;
    private Upgrade upgrade;
    boolean isSelected;
    boolean affordable;

    public Slot(UpgradeMenu menu, Rectangle frame, Upgrade upgrade) {
        this.menu = menu;
        this.frame = frame;
        this.upgrade = upgrade;
        this.isSelected = false;
    }

    public void draw(Graphics2D g) {

        // Draw the frame background
        g.setColor(Color.BLACK);
        g.fill(frame);

        // visual distinction for non rebuyable items
        String title = upgrade.isAvailable() ? upgrade.getTitle() : "Unavailable";
        String description = upgrade.isAvailable() ? upgrade.getDescription() : "Item already purchased";
        BufferedImage sprite = upgrade.isAvailable() ? upgrade.getSprite() : menu.unavailableIcon;

        // Title setup
        g.setColor(menu.TITLE_COLOR);
        g.setFont(menu.TITLE_TEXT);
        FontMetrics titleMetrics = g.getFontMetrics(g.getFont());
        int titleWidth = titleMetrics.stringWidth(title);
        int titleHeight = titleMetrics.getHeight();

        // Center and draw the title
        int titleX = frame.x + (frame.width / 2) - (titleWidth / 2);
        int titleY = frame.y + titleHeight + 10;
        g.drawString(title, titleX, titleY);

        // Description setup
        g.setColor(Color.GRAY);
        g.setFont(menu.DESCRIPTION_TEXT);
        FontMetrics descMetrics = g.getFontMetrics(g.getFont());
        int descWidth = descMetrics.stringWidth(description);
        int descHeight = descMetrics.getHeight();

        // Center and draw the description below the title
        int descX = frame.x + (frame.width / 2) - (descWidth / 2);
        int descY = titleY + descHeight + 5;
        g.drawString(description, descX, descY);

        if (sprite != null) {
            int spriteSize = (int) (Math.min(frame.width, frame.height) / 1.5);
            int spriteX = frame.x + (frame.width / 2) - (spriteSize / 2);
            int spriteY = descY + descHeight;

            g.drawImage(sprite, spriteX, spriteY, spriteSize, spriteSize, null);
        }

        // Draw the cost at the bottom of the frame
        if (upgrade.isAvailable()) {
            g.setColor(Color.WHITE);
            g.setFont(menu.TITLE_TEXT);
            FontMetrics costMetrics = g.getFontMetrics(g.getFont());
            String costText = String.format("%d", upgrade.getCost());
            int costWidth = costMetrics.stringWidth(costText);
            int costX = frame.x + (frame.width / 2) - (costWidth / 2);
            int costY = frame.y + frame.height - 10;
            g.drawString(costText, costX, costY);

            // Draw cost icon
            if (menu.costIcon != null) {
                int iconSize = menu.COST_ICON_SIZE;
                int iconX = costX - iconSize - 5;
                int iconY = costY - iconSize + 2;
                g.drawImage(menu.costIcon, iconX, iconY, iconSize, iconSize, null);
            }
        }

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
